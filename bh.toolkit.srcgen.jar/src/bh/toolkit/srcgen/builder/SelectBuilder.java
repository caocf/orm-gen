package bh.toolkit.srcgen.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import bh.toolkit.srcgen.connector.rdb.DBDialectFactory;
import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.CaseFilterSpec;
import bh.toolkit.srcgen.model.artifact.DaoSpec;
import bh.toolkit.srcgen.model.artifact.RelationshipSpec;
import bh.toolkit.srcgen.model.artifact.ResultFilterSpec;
import bh.toolkit.srcgen.model.artifact.SelectSpec;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;
import bh.toolkit.srcgen.model.mybatis.Choose;
import bh.toolkit.srcgen.model.mybatis.If;
import bh.toolkit.srcgen.model.mybatis.Include;
import bh.toolkit.srcgen.model.mybatis.Mapper;
import bh.toolkit.srcgen.model.mybatis.ObjectFactory;
import bh.toolkit.srcgen.model.mybatis.Select;
import bh.toolkit.srcgen.model.mybatis.Sql;
import bh.toolkit.srcgen.model.mybatis.Trim;
import bh.toolkit.srcgen.model.mybatis.When;
import bh.toolkit.srcgen.model.rdb.ColumnSpec;
import bh.toolkit.srcgen.model.rdb.TableSpec;
import bh.toolkit.srcgen.registry.CtxCacheFacade;
import bh.toolkit.srcgen.registry.MapperSql;
import bh.toolkit.srcgen.registry.OneToOneIdx;
import bh.toolkit.srcgen.registry.OneToOneIdxFacade;
import bh.toolkit.srcgen.util.ClassAnalyzer;
import bh.toolkit.srcgen.util.ExceptionUtil;
import bh.toolkit.srcgen.util.JavaFormatter;
import bh.toolkit.srcgen.util.MapperFormatter;
import bh.toolkit.srcgen.util.ProfileHelper;
import bh.toolkit.srcgen.util.StringHelper;

public class SelectBuilder {

	private static Logger logger = Logger.getLogger(SelectBuilder.class);
	private ObjectFactory mapperObjFactory;
	private DBDialectFactory dbDialectFactory;

	public SelectBuilder(ObjectFactory mapperObjFactory) {
		this.mapperObjFactory = mapperObjFactory;
	}

	/**
	 * 查询总数所选取的字段是固定的第一列
	 *
	 * @param mapper
	 * @param artifactSpec
	 * @param daoSpec
	 * @param ancesSelectSpec
	 * @param descOtoIdxList
	 * @throws AppException
	 */
	public void buildCountMultiTabBySql(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec ancesSelectSpec, List<OneToOneIdx> descOtoIdxList,
			CaseFilterSpec topCaseFilterSpec) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// The expected 'id' of 'select'.
			String selectId = MapperFormatter.getSelectIdOfMultiTabBySql(ancesSelectSpec, descOtoIdxList);

			if (selectId.startsWith(MapperElm.SQL_SELECT) == true) {
				selectId = MapperElm.SQL_COUNT + selectId.substring(MapperElm.SQL_SELECT.length(), selectId.length());
			} else {
				selectId = MapperElm.SQL_COUNT + StringUtils.capitalize(selectId);
			}
			logger.debug("SELECT: Prepare to build select with id '" + selectId + "'.");

			// Lookup 'select' element with id.
			Select select = CtxCacheFacade.lookupSelect(workspaceSpec, daoSpec, selectId);
			if (select != null) {
				return;
			}

			// Find Table corresponding to current Select, if no Table found, then return Select without elements.
			String ancesTableName = ancesSelectSpec.getTableName();
			TableSpec ancesTable = CtxCacheFacade.lookupTableSpec(ancesTableName);
			if (ancesTable == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + ancesTableName);
				return;
			}

			// Initiate a blank 'select' and register it.
			select = mapperObjFactory.createSelect();
			mapper.getSelect().add(select);
			CtxCacheFacade.addSelect(workspaceSpec, daoSpec, selectId, select);

			// Populate attributes of select.
			select.setId(selectId);
			String ancesDtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, ancesTableName);
			select.setParameterType(ancesDtoXFullName);
			// select.setParameterType(artifactSpec.getCommonAttrSpec().getWorkspaceSpec().getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.SQL_CLAUSE_FULL);
			// Populate the 'resultClass' attribute for Select.
			select.setResultType(JavaSrcElm.INT);

			MapperSql mapperSql = new MapperSql();
			computeSelectClause(mapperSql, artifactSpec, null, ancesSelectSpec, ancesTable); // Compute the select clause.
			setJoinType(mapperSql, descOtoIdxList); // 根据join关系定义计算joinType

			// int joinType = -1;
			// if (descOtoIdxList == null || descOtoIdxList.size() == 0) {
			// joinType = MapperSql.JOIN_TYPE_NONE;
			// } else if (descOtoIdxList != null && descOtoIdxList.size() != 0 && hasOuterJoin(descOtoIdxList) == true) {
			// joinType = MapperSql.JOIN_TYPE_OUTER;
			// } else {
			// joinType = MapperSql.JOIN_TYPE_PUBLIC_INNER;
			// }
			// mapperSql.setJoinType(joinType);

			computeSqlElmtForJoin(mapperSql, artifactSpec, ancesSelectSpec, descOtoIdxList); // 根据join type计算各子句

			int joinType = mapperSql.getJoinType();
			StringBuffer joinClause = mapperSql.getJoinClause();
			StringBuffer whereClause = mapperSql.getWhereClause();
			StringBuffer sqlClause = new StringBuffer(joinClause);
			if (joinType == MapperSql.JOIN_TYPE_OUTER || joinType == MapperSql.JOIN_TYPE_NONE) {
				// 如果mapperSql.getWhereClause()不为空
				if (StringUtils.isNotBlank(whereClause) == true) {
					if (StringHelper.leftTrim(whereClause.toString()).toLowerCase().startsWith(MapperElm.SQL_WHERE_SIMPLE.toLowerCase()) == true) { // 如果以where开始，则新启一行
						MapperFormatter.beginWithNewLine(sqlClause, whereClause.toString());
					} else if (StringHelper.leftTrim(whereClause.toString()).toLowerCase().startsWith(MapperElm.SQL_AND_SIMPLE.toLowerCase()) == true) { // 如果以and开始，则替换为where，并新启一行
						MapperFormatter.beginWithNewLine(sqlClause, whereClause.toString().replaceFirst(MapperElm.SQL_AND_SIMPLE, MapperElm.SQL_WHERE_SIMPLE));
					} else { // 其他情况增加where后，新启一行
						MapperFormatter.beginWithNewLine(sqlClause, MapperElm.SQL_WHERE_SIMPLE + MapperElm.WHITE_SPACE + whereClause.toString());
					}
				}
			} else {
				// 如果mapperSql.getWhereClause()不为空
				if (StringUtils.isNotBlank(whereClause) == true) {
					if (StringHelper.leftTrim(whereClause.toString()).toLowerCase().startsWith(MapperElm.SQL_WHERE_SIMPLE.toLowerCase()) == true) { // 如果以where开始，则替换为and，并新启一行
						MapperFormatter.beginWithNewLine(sqlClause, whereClause.toString().replaceFirst(MapperElm.SQL_WHERE_SIMPLE, MapperElm.SQL_AND_SIMPLE));
					} else if (StringHelper.leftTrim(whereClause.toString()).toLowerCase().startsWith(MapperElm.SQL_AND_SIMPLE.toLowerCase()) == true) { // 如果以and开始，则新启一行
						MapperFormatter.beginWithNewLine(sqlClause, whereClause.toString());
					} else { // 其他情况增加and后，新启一行
						MapperFormatter.beginWithNewLine(sqlClause, MapperElm.SQL_AND_SIMPLE + MapperElm.WHITE_SPACE + whereClause.toString());
					}
				}
			}

			/*
			 * Commented at 2016.03.09, because I assume the fromClause could not be blank. if (StringUtils.isBlank(fromClause)) { MapperFormatter.beginFromStatement(fromClause);
			 * fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, ancesSelectSpec.getTableName(), ancesSelectSpec.getTableAlias())); }
			 */

			// 把各子句拼接起来放入trim的CDATA
			Trim overAllTrim = mapperObjFactory.createTrim();
			select.getTrim().add(overAllTrim);
			StringBuffer fromClause = mapperSql.getFromClause();
			overAllTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + "select count(1)" + fromClause + sqlClause + MapperElm.CDATA_ANCHOR_END);

			// if (joinType == MapperSql.JOIN_TYPE_OUTER || joinType == MapperSql.JOIN_TYPE_NONE) {
			// // 如果whereClause不为空
			// if (StringUtils.isNotBlank(sqlClause) == true) {
			// // 且原先不包含where关键字，则增加where
			// if (sqlClause.indexOf(MapperElm.SQL_WHERE_SIMPLE) == -1) {
			// sqlClause = MapperElm.SQL_WHERE_SIMPLE + MapperElm.WHITE_SPACE + sqlClause;
			// }
			// }
			// } else {
			// overAllTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + "select count(1)" + fromClause + sqlClause + MapperElm.CDATA_ANCHOR_END);
			// }

			// 把针对mapperSqlClause对象的whereClause属性的判断放入trim
			overAllTrim = mapperObjFactory.createTrim();
			select.getTrim().add(overAllTrim);
			If trimIf = mapperObjFactory.createIf();
			overAllTrim.getIf().add(trimIf);
			trimIf.setTest(MapperElm.EXPR_WHERE_CLAUSE_ISNOT_NULL);
			Trim whereTrim = mapperObjFactory.createTrim();
			trimIf.getTrim().add(whereTrim);

			// Changed @2015.05.27，需要观察是否会产生连带影响
			if (joinType == MapperSql.JOIN_TYPE_OUTER || joinType == MapperSql.JOIN_TYPE_NONE) {
				if (sqlClause.indexOf(MapperElm.SQL_WHERE_SIMPLE) == -1) {
					whereTrim.setPrefix(MapperElm.SQL_WHERE_FULL);
				} else {
					whereTrim.setPrefix(MapperElm.SQL_AND_FULL);
				}
			} else {
				whereTrim.setPrefix(MapperElm.SQL_AND_FULL);
			}
			whereTrim.setPrefixOverrides(MapperElm.SQL_AND_SIMPLE + MapperElm.WHITE_SPACE);
			whereTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.ANCHOR_MAPPERSQL_WHERE_CLAUSE + MapperElm.CDATA_ANCHOR_END);

			// Added @2015.10.25, in order to support "caseFilterSpec".
			// 针对在查询中配置了<caseFilterSpec/>的情况
			if (topCaseFilterSpec != null) {

				// 增加对userIdentity的非空判断
				trimIf = mapperObjFactory.createIf();
				overAllTrim.getIf().add(trimIf);
				trimIf.setTest(topCaseFilterSpec.getTest());

				// 增加针对caseFilterSpec的各种分支
				Choose trimIfChoose = mapperObjFactory.createChoose();
				trimIf.getChoose().add(trimIfChoose);
				When trimIfChooseWhen = null;
				Trim trimIfChooseWhenTrim = null;
				List<ResultFilterSpec> ResultFilterSpecList = topCaseFilterSpec.getResultFilter();
				for (ResultFilterSpec resultFilterSpec : ResultFilterSpecList) {

					// 每一个分支用when元素标示
					trimIfChooseWhen = mapperObjFactory.createWhen();
					trimIfChoose.getWhen().add(trimIfChooseWhen);
					trimIfChooseWhen.setTest(resultFilterSpec.getTest());

					// 在when下面创建trim
					trimIfChooseWhenTrim = mapperObjFactory.createTrim();
					trimIfChooseWhen.getTrim().add(trimIfChooseWhenTrim);
					trimIfChooseWhenTrim.setPrefix(MapperElm.SQL_AND_FULL);
					trimIfChooseWhenTrim.setPrefixOverrides(MapperElm.SQL_AND_SIMPLE + MapperElm.WHITE_SPACE);
					trimIfChooseWhenTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + resultFilterSpec.getClause() + MapperElm.CDATA_ANCHOR_END);

				}

			}

			// Register DAO methods.
			List<String> paramTypeNameList = new ArrayList<String>();
			// paramTypeNameList.add(workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.SQL_CLAUSE_FULL);
			paramTypeNameList.add(ancesDtoXFullName);
			String comments = ancesSelectSpec.getDesc() + JavaSrcElm.COUNT_MULTI_TABLE;
			// CtxCacheFacade.addDaoMethod(artifactSpec, daoSpec.getShortName(), selectId, paramTypeNameList, JavaSrcElm.LANG_INTEGER_FULL, comments);
			CtxCacheFacade.addDaoMethod(artifactSpec, daoSpec.getShortName(), selectId, paramTypeNameList, JavaSrcElm.INT, comments);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	//
	// Build public select, including oneToOne and oneToMany
	//
	// <ancesSelectSpec>
	// ....<descOneToOne />
	// </ancesSelectSpec>
	//
	public void buildSelectMultiTabByPK(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec ancesSelectSpec, List<OneToOneIdx> descOtoIdxList)
			throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// The expected 'id' of 'select'.
			String selectId = MapperFormatter.getSelectIdOfMultiTabByPK(ancesSelectSpec, descOtoIdxList);
			logger.debug("SELECT: Prepare to build select with id '" + selectId + "'.");

			// Lookup 'select' element with composite id containing the select element name and the 'java.lang.Long' type name.
			String selectIdLong = selectId + JavaSrcElm.LANG_LONG_SIMPLE;
			Select select = CtxCacheFacade.lookupSelect(workspaceSpec, daoSpec, selectIdLong);
			if (select == null) {
				computeSelectMultiTabByPK(mapper, artifactSpec, daoSpec, ancesSelectSpec, descOtoIdxList, selectIdLong, JavaSrcElm.LANG_LONG_FULL);
			}

			// Lookup 'select' element with composite id containing the element name and the DTOX type name.
			String selectIdDtoX = selectId + JavaSrcElm.DTOX_NAME_SUFFIX;
			String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, ancesSelectSpec.getTableName());
			select = CtxCacheFacade.lookupSelect(workspaceSpec, daoSpec, selectIdDtoX);
			if (select == null) {
				computeSelectMultiTabByPK(mapper, artifactSpec, daoSpec, ancesSelectSpec, descOtoIdxList, selectIdDtoX, dtoXFullName);
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	//
	// Build public select, including oneToOne and oneToMany
	//
	// <ancesSelectSpec>
	// ....<descOneToOne />
	// </ancesSelectSpec>
	//
	public void buildSelectMultiTabBySql(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec ancesSelectSpec, List<OneToOneIdx> descOtoIdxList,
			CaseFilterSpec topCaseFilterSpec) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// The expected 'id' of 'select'.
			String selectId = MapperFormatter.getSelectIdOfMultiTabBySql(ancesSelectSpec, descOtoIdxList);
			logger.debug("SELECT: Prepare to build select with id '" + selectId + "'.");

			// Lookup 'select' element with id.
			Select select = CtxCacheFacade.lookupSelect(workspaceSpec, daoSpec, selectId);
			if (select != null) {
				return;
			}

			// Find Table corresponding to current Select, if no Table found, then return Select without elements.
			String ancesTableName = ancesSelectSpec.getTableName();
			TableSpec ancesTable = CtxCacheFacade.lookupTableSpec(ancesTableName);
			if (ancesTable == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + ancesTableName);
				return;
			}

			// Initiate a blank 'select' and register it.
			select = mapperObjFactory.createSelect();
			mapper.getSelect().add(select);
			CtxCacheFacade.addSelect(workspaceSpec, daoSpec, selectId, select);

			// Populate attributes of select.
			select.setId(selectId);
			String ancesDtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, ancesTableName);
			select.setParameterType(ancesDtoXFullName);
			// select.setParameterType(workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.SQL_CLAUSE_FULL);
			String resultMapId = MapperFormatter.getResultMapIdOfMultiTab(ancesSelectSpec, descOtoIdxList);
			select.setResultMap(resultMapId);

			// Compute the select clause.
			MapperSql mapperSql = new MapperSql();
			computeSelectClause(mapperSql, artifactSpec, null, ancesSelectSpec, ancesTable);

			// Define join type according to the relationship.
			setJoinType(mapperSql, descOtoIdxList);

			// int joinType = -1;
			// if (descOtoIdxList == null || descOtoIdxList.size() == 0) {
			// joinType = MapperSql.JOIN_TYPE_NONE;
			// } else if (descOtoIdxList != null && descOtoIdxList.size() != 0 && hasOuterJoin(descOtoIdxList) == true) {
			// joinType = MapperSql.JOIN_TYPE_OUTER;
			// } else {
			// joinType = MapperSql.JOIN_TYPE_PUBLIC_INNER;
			// }
			// mapperSql.setJoinType(joinType);

			// Populate different kinds of clause according to join tyoe.
			computeSqlElmtForJoin(mapperSql, artifactSpec, ancesSelectSpec, descOtoIdxList);

			// Append different kinds of clause as usable sql clause.
			String sqlId = MapperFormatter.getSqlIdOfMultiTabSel(ancesSelectSpec, descOtoIdxList, true);
			defineReusableSelectFrom4MultiTab(artifactSpec, mapperSql, mapper, daoSpec, sqlId);

			// 根据数据库类型定义分页查询SQL语句
			if (dbDialectFactory == null) {
				this.dbDialectFactory = DBDialectFactory.getInstance(artifactSpec.getCommonAttrSpec().getDatasourceSpec());
			}
			dbDialectFactory.getPageQueryBuilder().definePagingQuery(select, artifactSpec, ancesSelectSpec, descOtoIdxList, mapperSql, sqlId, topCaseFilterSpec, mapperObjFactory);

			// Register DAO methods.
			List<String> paramTypeNameList = new ArrayList<String>();
			// paramTypeNameList.add(workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.SQL_CLAUSE_FULL);
			paramTypeNameList.add(ancesDtoXFullName);
			String returnTypeFullName = JavaFormatter.getDtoXFullName(workspaceSpec, ancesTableName);
			String returnTypeList = JavaSrcElm.UTIL_LIST_FULL + JavaSrcElm.LESS_THAN + returnTypeFullName + JavaSrcElm.LARGER_THAN;
			String comments = ancesSelectSpec.getDesc() + JavaSrcElm.QUERY_MULTI_TABLE;
			CtxCacheFacade.addDaoMethod(artifactSpec, daoSpec.getShortName(), selectId, paramTypeNameList, returnTypeList, comments);

			// 增加针对根表的DTOX，用以封装返回值。
			CtxCacheFacade.addDtoXClass(artifactSpec, ancesSelectSpec.getTableName());

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	//
	// Internal selectOneToOne only comes from oneToMany, which could from both
	// top or higher oneToOne.
	//
	// <fatherSelectSpec>
	// ....<sonOneToMany>
	// ........<sonReultMapConfig>
	// ............<descOneToOne />
	// ........</sonReultMapConfig>
	// ........<fatherAttr />
	// ........<sonAttr />
	// ....</sonOneToMany>
	// </fatherSelectSpec>
	//
	public void buildInternalSelectOto(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec fatherSelectSpec, RelationshipSpec sonOtmSpec,
			List<OneToOneIdx> descOtoIdxList, String selectId, String resultMapId) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// Lookup 'select' element with id.
			Select select = CtxCacheFacade.lookupSelect(workspaceSpec, daoSpec, selectId);
			if (select != null) {
				return;
			}

			// Initiate a blank 'select' and register it.
			select = mapperObjFactory.createSelect();
			mapper.getSelect().add(select);
			CtxCacheFacade.addSelect(workspaceSpec, daoSpec, selectId, select);

			// Populate attributes of select.
			select.setId(selectId);
			select.setResultMap(resultMapId);

			// Find Table corresponding to current Select, if no Table found,
			// then return Select without elements.
			SelectSpec sonSelectSpec = sonOtmSpec.getSelectSpec();
			String sonTableName = sonSelectSpec.getTableName();
			String sonTableAlias = sonSelectSpec.getTableAlias();
			// String sonClassName = sonSelectSpec.getClassName();
			// select.setParameterType(sonClassName);
			TableSpec sonTable = CtxCacheFacade.lookupTableSpec(sonTableName);
			if (sonTable == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + sonTableName);
				return;
			}

			// Compute the select clause.
			MapperSql mapperSql = new MapperSql();
			StringBuffer selectClause = mapperSql.getSelectClause();
			computeSelectClause(mapperSql, artifactSpec, null, sonSelectSpec, sonTable);

			// Create where clause.
			StringBuffer whereClause = mapperSql.getWhereClause();
			MapperFormatter.beginWhereStatement(whereClause);
			String sonAttr = sonOtmSpec.getSonAttr();
			String sonColumnName = JavaFormatter.getDBStyle(sonAttr);
			String fatherTableName = fatherSelectSpec.getTableName();
			String fatherTableAlias = fatherSelectSpec.getTableAlias();
			String fatherColumnName = JavaFormatter.getDBStyle(sonOtmSpec.getFatherAttr());
			whereClause.append(MapperFormatter.getColumnFullName(artifactSpec, sonTableName, sonTableAlias, sonColumnName));
			whereClause.append(MapperElm.EQUAL);
			whereClause.append(MapperElm.POUND);
			whereClause.append(MapperElm.LEFT_BRACKET);
			whereClause.append(MapperFormatter.getColumnAlias(artifactSpec, fatherTableName, fatherTableAlias, fatherColumnName));
			whereClause.append(MapperElm.RIGHT_BRACKET);

			// Populate from clause, append those clause from oneToOne.
			StringBuffer fromClause = mapperSql.getFromClause();
			int joinType = (hasOuterJoin(descOtoIdxList) == true) ? MapperSql.JOIN_TYPE_OUTER : MapperSql.JOIN_TYPE_PRIVATE_INNER;
			mapperSql.setJoinType(joinType);
			computeSqlElmtForJoin(mapperSql, artifactSpec, sonSelectSpec, descOtoIdxList);

			select.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + selectClause.toString() + fromClause.toString() + whereClause.toString() + MapperElm.CDATA_ANCHOR_END);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	//
	// Internal selectOneTable comes from oneToOne, top or higher oneToMany.
	//
	// From oneToOne:
	// <ancesResultMap>
	// ....<fatherOtoIndex>
	// ........<fatherSelectSpec>
	// ............<sonOneToMany listOfSon="" fatherAttr="">
	// ................<sonSelectSpec />
	// ............</sonOneToMany>
	// ........</fatherSelectSpec>
	// ....</fatherOtoIndex>
	// ....<descOneToOne />
	// </ancesResultMap>
	//
	// From top or higher oneToMany:
	// <fatherSelectSpec>
	// ....<sonOneToMany listOfSon="", fatherAttr="">
	// ........<sonSelectSpec />
	// ....</sonOneToMany>
	// </fatherSelectSpec>
	//
	// public void buildInternalSelectOneTable(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec ancesSelectSpec,
	// OneToOneIdx fatherOtoIndex, SelectSpec fatherSelectSpec, RelationshipSpec sonOtmSpec) throws AppException {
	public void buildInternalSelectOneTable(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec fatherSelectSpec, RelationshipSpec sonOtmSpec)
			throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			SelectSpec sonSelectSpec = sonOtmSpec.getSelectSpec();
			String sonTableName = sonSelectSpec.getTableName();
			String sonTableAlias = sonSelectSpec.getTableAlias();
			String listOfSon = sonOtmSpec.getListOfSon(); // Done.

			// Populate the 'id' attribute for 'select'.
			String selectId = MapperFormatter.getSelectIdOfInternalOtm(artifactSpec, daoSpec, sonSelectSpec, listOfSon, false);

			// Lookup 'select' element with id.
			Select select = CtxCacheFacade.lookupSelect(workspaceSpec, daoSpec, selectId);
			if (select != null) {
				return;
			}

			logger.debug("INTERNAL SELECT: Begin to build internal select with id: " + selectId);

			// Initiate a blank 'select' and register it.
			select = mapperObjFactory.createSelect();
			mapper.getSelect().add(select);
			CtxCacheFacade.addSelect(workspaceSpec, daoSpec, selectId, select);

			// Populate the 'resultMap' attribute for 'select'.
			select.setId(selectId);
			String resultMapId = MapperFormatter.getResultMapIdOfInternalOtm(artifactSpec, daoSpec, sonSelectSpec, listOfSon);
			select.setResultMap(resultMapId);

			// Find Table corresponding to current Select, if no Table found,
			// then return Select without elements.
			TableSpec sonTable = CtxCacheFacade.lookupTableSpec(sonTableName);
			if (sonTable == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + sonTableName);
				return;
			}

			// Compute the select clause.
			MapperSql mapperSql = new MapperSql();
			StringBuffer selectClause = mapperSql.getSelectClause();
			computeSelectClause(mapperSql, artifactSpec, null, sonSelectSpec, sonTable);

			// Populate from clause.
			StringBuffer fromClause = mapperSql.getFromClause();
			MapperFormatter.beginFromStatement(fromClause);
			fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, sonTableName, sonTableAlias));

			// Create where clause.
			StringBuffer whereClause = mapperSql.getWhereClause();
			MapperFormatter.beginWhereStatement(whereClause);
			String sonAttr = sonOtmSpec.getSonAttr();
			String sonColumnName = JavaFormatter.getDBStyle(sonAttr);
			String fatherTableName = fatherSelectSpec.getTableName();
			String fatherTableAlias = fatherSelectSpec.getTableAlias();
			String fatherColumnName = JavaFormatter.getDBStyle(sonOtmSpec.getFatherAttr());
			whereClause.append(MapperFormatter.getColumnFullName(artifactSpec, sonTableName, sonTableAlias, sonColumnName));
			whereClause.append(MapperElm.EQUAL);
			whereClause.append(MapperElm.POUND);
			whereClause.append(MapperElm.LEFT_BRACKET);
			whereClause.append(MapperFormatter.getColumnAlias(artifactSpec, fatherTableName, fatherTableAlias, fatherColumnName));
			whereClause.append(MapperElm.RIGHT_BRACKET);

			// Append configured filter.
			computeResultFilterSpec(artifactSpec, whereClause, sonSelectSpec, MapperElm.SQL_AND_SIMPLE);

			select.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + selectClause.toString() + fromClause.toString() + whereClause.toString() + MapperElm.CDATA_ANCHOR_END);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	// /**
	// * Construct select element with different parameter type.
	// *
	// * @param mapper
	// * @param artifactSpec
	// * @param daoSpec
	// * @param selectSpec
	// * @param selectId
	// * @param paramTypeName
	// * @throws AppException
	// */
	// private void buildSelectRootTableByPK(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec selectSpec, String selectId)
	// throws AppException {
	//
	// WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
	//
	// try {
	//
	// // Initiate a new 'select' and register it.
	// Select select = mapperObjFactory.createSelect();
	// select.setId(selectId);
	// mapper.getSelect().add(select);
	// CtxCacheFacade.addSelect(workspaceSpec, daoSpec, selectId, select);
	//
	// // Populate the 'parameterType' attribute for select.
	// select.setParameterType(JavaSrcElm.LANG_LONG_FULL);
	//
	// // Populate the 'resultMap' attribute for select.
	// String rootTableName = selectSpec.getTableName();
	// // String rootTableAlias = selectSpec.getTableAlias();
	// String resultMapId = MapperFormatter.getResultMapIdOfRootTab(selectSpec);
	// select.setResultMap(resultMapId);
	//
	// // Find Table corresponding to current Select, if no Table found,
	// // then return Select without elements.
	// TableSpec rootTable = CtxCacheFacade.lookupTableSpec(rootTableName);
	// if (rootTable == null) {
	// logger.error("!!! NO TABLE !!!: No table found with table name: " + rootTableName);
	// return;
	// }
	//
	// // Define separate select and from clause for easy to reuse.
	// String sqlId = MapperFormatter.getSqlIdOfRootTabSel(selectSpec);
	// MapperSql mapperSql = new MapperSql();
	// defineReusableSelectFrom4RootTab(mapperSql, mapper, artifactSpec, daoSpec, selectSpec, rootTable, sqlId);
	//
	// // Anyway, need to populate primary key clause.
	// computeWhereClauseWithPK(mapperSql, artifactSpec, rootTable, selectSpec);
	// StringBuffer primaryKeyClause = mapperSql.getPrimaryKeyClause();
	// MapperFormatter.insertWhereStatement(primaryKeyClause);
	//
	// // Refer to common sql clause in 'select'.
	// Include include = mapperObjFactory.createInclude();
	// include.setRefid(sqlId);
	// select.getInclude().add(include);
	// select.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + primaryKeyClause.toString() + MapperElm.CDATA_ANCHOR_END);
	//
	// // Register a DAO method.
	// List<String> paramTypeNameList = new ArrayList<String>();
	// paramTypeNameList.add(JavaSrcElm.LANG_LONG_FULL);
	// String returnTypeName = JavaFormatter.getDtoXFullName(workspaceSpec, selectSpec.getTableName());
	// String comments = selectSpec.getDesc() + JavaSrcElm.QUERY_ONE_TABLE;
	// CtxCacheFacade.addDaoMethod(artifactSpec, daoSpec.getShortName(), selectId, paramTypeNameList, returnTypeName, comments);
	//
	// // 增加针对根表的DTOX，用以封装返回值。
	// CtxCacheFacade.addDtoXClass(artifactSpec, selectSpec.getTableName());
	//
	// } catch (Throwable t) {
	// ExceptionUtil.handleException(t, logger);
	// }
	//
	// }

	/**
	 * 构建以主键为条件的多表关联查询。以主键为条件的查询不关注在resultFilter中设置的查询条件。
	 *
	 * @param mapper
	 * @param artifactSpec
	 * @param daoSpec
	 * @param ancesSelectSpec
	 * @param descOtoIdxList
	 * @throws AppException
	 */
	private void computeSelectMultiTabByPK(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec ancesSelectSpec, List<OneToOneIdx> descOtoIdxList, String selectId,
			String paramTypeName) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// Initiate a blank 'select' and register it.
			Select select = mapperObjFactory.createSelect();
			mapper.getSelect().add(select);
			CtxCacheFacade.addSelect(workspaceSpec, daoSpec, selectId, select);

			// Populate attributes of Select.
			select.setId(selectId);
			select.setParameterType(paramTypeName);
			String resultMapId = MapperFormatter.getResultMapIdOfMultiTab(ancesSelectSpec, descOtoIdxList);
			select.setResultMap(resultMapId);

			// Find Table corresponding to current Select, if no Table found, then return Select without elements.
			String ancesTableName = ancesSelectSpec.getTableName();
			TableSpec ancesTable = CtxCacheFacade.lookupTableSpec(ancesTableName);
			if (ancesTable == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + ancesTableName);
				return;
			}

			// Refer to common sql clause in 'select'.
			String sqlId = MapperFormatter.getSqlIdOfMultiTabSel(ancesSelectSpec, descOtoIdxList, true);
			Include include = mapperObjFactory.createInclude();
			include.setRefid(sqlId);
			select.getInclude().add(include);

			// 构建基于主键的查询条件
			MapperSql mapperSql = new MapperSql();
			computePKWhereClause(mapperSql, artifactSpec, ancesTable, ancesSelectSpec, paramTypeName);
			StringBuffer primaryKeyClause = mapperSql.getPrimaryKeyClause();

			// 根据join关系定义计算joinType
			setJoinType(mapperSql, descOtoIdxList);
			int joinType = mapperSql.getJoinType();

			// int joinType = -1;
			// if (descOtoIdxList == null || descOtoIdxList.size() == 0) {
			// joinType = MapperSql.JOIN_TYPE_NONE;
			// } else if (descOtoIdxList != null && descOtoIdxList.size() != 0 && hasOuterJoin(descOtoIdxList) == true) {
			// joinType = MapperSql.JOIN_TYPE_OUTER;
			// } else {
			// joinType = MapperSql.JOIN_TYPE_PUBLIC_INNER;
			// }
			// mapperSql.setJoinType(joinType);

			if (joinType == MapperSql.JOIN_TYPE_NONE) {
				// computeSqlElmtForSingle(mapperSql, artifactSpec, ancesSelectSpec);
				computeSelectAndFromClauseForSingle(mapperSql, artifactSpec, ancesSelectSpec);
				MapperFormatter.insertWhereStatement(primaryKeyClause);
			} else if (joinType == MapperSql.JOIN_TYPE_OUTER) {
				// Compute the select clause.
				computeSelectClause(mapperSql, artifactSpec, null, ancesSelectSpec, ancesTable);
				// For outer join, join condition was included inside 'fromClause'.
				computeSqlElmtForJoin(mapperSql, artifactSpec, ancesSelectSpec, descOtoIdxList);
				MapperFormatter.insertWhereStatement(primaryKeyClause);
			} else {
				// Compute the select clause.
				computeSelectClause(mapperSql, artifactSpec, null, ancesSelectSpec, ancesTable);
				// For inner join, join condition was included inside separate 'joinClause'.
				computeSqlElmtForJoin(mapperSql, artifactSpec, ancesSelectSpec, descOtoIdxList);
				// StringBuffer joinClause = mapperSql.getJoinClause();
				// if (StringUtils.isNotBlank(joinClause)) {
				// MapperFormatter.insertAndStatement(primaryKeyClause);
				// } else {
				MapperFormatter.insertAndStatement(primaryKeyClause);
				// }
			}

			// Append different kinds of clause as usable sql clause.
			defineReusableSelectFrom4MultiTab(artifactSpec, mapperSql, mapper, daoSpec, sqlId);

			// Append where clause into mapper.
			// StringBuffer whereClause = mapperSql.getWhereClause();
			// select.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + primaryKeyClause.toString() + whereClause.toString() + MapperElm.CDATA_ANCHOR_END);
			select.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + primaryKeyClause.toString() + MapperElm.CDATA_ANCHOR_END);

			// Register a DAO method.
			List<String> paramTypeNameList = new ArrayList<String>();
			paramTypeNameList.add(paramTypeName);
			String returnTypeName = JavaFormatter.getDtoXFullName(workspaceSpec, ancesSelectSpec.getTableName());
			String comments = ancesSelectSpec.getDesc() + JavaSrcElm.QUERY_MULTI_TABLE;
			CtxCacheFacade.addDaoMethod(artifactSpec, daoSpec.getShortName(), selectId, paramTypeNameList, returnTypeName, comments);

			// 增加针对根表的DTOX，用以封装返回值。
			CtxCacheFacade.addDtoXClass(artifactSpec, ancesSelectSpec.getTableName());

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void computeSelectClause(MapperSql mapperSql, ArtifactSpec artifactSpec, OneToOneIdx oneToOneIndex, SelectSpec selectSpec, TableSpec ormTable) throws AppException {

		// Get table name and table alias.
		String tableName = null;
		String tableAlias = null;
		if (oneToOneIndex != null) {
			tableName = oneToOneIndex.getRightTableName();
			tableAlias = oneToOneIndex.getRightTableAlias();
		} else {
			tableName = selectSpec.getTableName();
			tableAlias = selectSpec.getTableAlias();
		}

		// Compute the select clause according to the the OrmClass definition.
		HashSet<String> includedAttrs = ProfileHelper.getIncludedAttrName(selectSpec);
		HashSet<String> excludedAttrs = ProfileHelper.getExcludedAttrName(selectSpec);
		List<ColumnSpec> columnSpecList = ormTable.getColumnList();
		ColumnSpec columnSpec = null;
		String columnName = null;
		String attrName = null;
		StringBuffer selectClause = mapperSql.getSelectClause();
		StringBuffer selectPrefix = mapperSql.getSelectPrefix();
		StringBuffer columnNameClause = mapperSql.getColumnNameClause();
		StringBuffer columnAliasClause = mapperSql.getColumnAliasClause();

		for (int i = 0; i < columnSpecList.size(); i++) {

			columnSpec = columnSpecList.get(i);
			columnName = columnSpec.getName();
			attrName = JavaFormatter.getJavaStyle(columnName, false, false);

			// 如果当前selectSpec中包含selectPrefix，则记录到mapperSql中
			if (i == 0 && StringUtils.isNotBlank(selectSpec.getSelectPrefix()) == true) {
				selectPrefix.append(selectSpec.getSelectPrefix());
			}

			// Check inclusion and exclusion, inclusion take higher preference.
			if (includedAttrs.size() != 0) {
				if (includedAttrs.contains(attrName) == false) {
					logger.debug("INCLUDE PROPERTY: Property '" + attrName + "' is NOT in the inclusion list, skipped.");
					continue;
				}
			} else if (excludedAttrs.size() != 0) {
				if (excludedAttrs.contains(JavaSrcElm.ASTERISK) == true) { // 排除所有的属性
					break;
				}
				if (excludedAttrs.contains(attrName) == true) {
					logger.debug("EXCLUDE PROPERTY: Property '" + attrName + "' is in the exclusion list, skipped.");
					continue;
				}
			}

			// 如果存在未处理selectPrefix，则追加
			if (selectPrefix.length() > 0) {
				if (StringUtils.isBlank(selectClause)) {
					selectClause.append(MapperFormatter.getSelectStmtWithPrefix(selectPrefix));
				} else {
					selectClause.append(selectPrefix);
				}
				selectPrefix.delete(0, selectPrefix.length()); // 处理完成后清除selectPrefix
			} else {
				if (StringUtils.isNotBlank(selectClause) == true) {
					selectClause.append(MapperElm.COMMA + MapperElm.WHITE_SPACE);
				} else {
					selectClause.append(MapperFormatter.getSelectStmtWithPrefix(selectPrefix));
				}
			}
			// if (StringUtils.isBlank(selectClause)) {
			// selectClause.append(MapperFormatter.getSelectStmtWithPrefix(selectSpec));
			// } else {
			// selectClause.append(MapperElm.COMMA + MapperElm.WHITE_SPACE);
			// }
			selectClause.append(MapperFormatter.getColumnNamePair(artifactSpec, tableName, tableAlias, columnName));
			MapperFormatter.checkWidth(selectClause);

			// Handle the column name clause
			if (StringUtils.isNotBlank(columnNameClause)) {
				columnNameClause.append(MapperElm.COMMA + MapperElm.WHITE_SPACE);
			}
			columnNameClause.append(MapperFormatter.getColumnNamePair(artifactSpec, tableName, tableAlias, columnName));
			MapperFormatter.checkWidth(columnNameClause);

			// Handle the column alias clause.
			if (StringUtils.isNotBlank(columnAliasClause)) {
				columnAliasClause.append(MapperElm.COMMA + MapperElm.WHITE_SPACE);
			}
			columnAliasClause.append(MapperFormatter.getColumnAlias(artifactSpec, tableName, tableAlias, columnName));
			MapperFormatter.checkWidth(columnAliasClause);

		}

	}

	// private void computeFromClause(MapperSql mapperSql, ArtifactSpec artifactSpec, SelectSpec selectSpec) {
	// String rootTableName = selectSpec.getTableName();
	// String rootTableAlias = selectSpec.getTableAlias();
	// StringBuffer fromClause = mapperSql.getFromClause();
	// MapperFormatter.beginFromStatement(fromClause);
	// fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, rootTableName, rootTableAlias));
	// }

	private void computePKWhereClause(MapperSql mapperSql, ArtifactSpec artifactSpec, TableSpec ormTable, SelectSpec selectSpec, String paramTypeName) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		List<ColumnSpec> columnSpecList = ormTable.getColumnList();
		ColumnSpec columnSpec = null;
		String columnName = null;
		String attrName = null;
		String tableName = selectSpec.getTableName();
		String tableAlias = selectSpec.getTableAlias();
		// String voName = JavaFormatter.getJavaStyle(tableName, false, false) + JavaSrcElm.VO_NAME_SUFFIX;
		String voFullName = JavaFormatter.getVoFullName(workspaceSpec, tableName);
		String voSimpleName = JavaFormatter.getClassSimpleName(voFullName);

		// Populate sql according to the the OrmClass definition.
		StringBuffer primaryKeyClause = mapperSql.getPrimaryKeyClause();
		for (int i = 0; i < columnSpecList.size(); i++) {

			columnSpec = columnSpecList.get(i);
			columnName = columnSpec.getName();
			attrName = JavaFormatter.getJavaStyle(columnName, false, false);

			if (columnSpec != null && columnSpec.isPrimaryKey() == true) {

				if (StringUtils.isNotBlank(primaryKeyClause)) {
					primaryKeyClause.append(MapperElm.SQL_AND_FULL);
				}

				primaryKeyClause.append(MapperFormatter.getColumnFullName(artifactSpec, tableName, tableAlias, columnName));
				primaryKeyClause.append(MapperElm.EQUAL);
				primaryKeyClause.append(MapperElm.POUND);
				primaryKeyClause.append(MapperElm.LEFT_BRACKET);
				if (paramTypeName.startsWith(JavaSrcElm.JAVA_LANG_PKG_PREFIX) == true) {
					primaryKeyClause.append(MapperElm.MAPPER_PK_VAL_ID);
				} else {
					primaryKeyClause.append(StringHelper.toLowerCase(voSimpleName, 0));
					primaryKeyClause.append(MapperElm.DOT);
					primaryKeyClause.append(attrName);
				}
				primaryKeyClause.append(MapperElm.RIGHT_BRACKET);

				logger.debug("FIND MAPPING: Find primary key mapping between property '" + attrName + "' and column '" + columnName + "'");

			}

		}

	}

	/**
	 * 为没有'oneToOne' join的'selectSpec'定义独立可重用的<sql>元素 *
	 */
	// private void computeSqlElmtForSingle(MapperSql mapperSql, ArtifactSpec artifactSpec, SelectSpec ancesSelectSpec) throws AppException {
	//
	// logger.debug("ENTRY: Prepare to execute 'computeSqlClauseForSingle'.");
	//
	// // 处理只包含单表查询的selectSpec，构建select子句和from子句
	// computeSelectAndFromClauseForSingle(mapperSql, artifactSpec, ancesSelectSpec);

	/**********************************************************************************************
	 * Handle 'filter' from 'ancesSelectSpec' for higher performance.
	 **********************************************************************************************/
	// List<ResultFilterSpec> filterList = ancesSelectSpec.getResultFilter();
	// if (filterList != null) {
	// logger.debug("FILTER: 'filterList' of current 'ancesSelectSpec' is not null.");
	// StringBuffer primaryKeyClause = mapperSql.getPrimaryKeyClause();
	// StringBuffer whereClause = mapperSql.getWhereClause();
	// if (StringUtils.isNotBlank(primaryKeyClause) || StringUtils.isNotBlank(whereClause)) {
	// computeResultFilterSpec(artifactSpec, whereClause, ancesSelectSpec, MapperElm.SQL_AND_SIMPLE);
	// } else {
	// computeResultFilterSpec(artifactSpec, whereClause, ancesSelectSpec, MapperElm.SQL_WHERE_SIMPLE);
	// }
	// } else {
	// logger.debug("FILTER: 'filterList' of current 'ancesSelectSpec' is null.");
	// }

	// }

	/**
	 * Description: 构建单表查询的select子句、from子句 Author: zhaoruibin Creation time: 2016年3月8日 下午5:25:05
	 */
	private void computeSelectAndFromClauseForSingle(MapperSql mapperSql, ArtifactSpec artifactSpec, SelectSpec ancesSelectSpec) throws AppException {

		logger.debug("ENTRY: Prepare to execute 'computeSelectClauseForSingle'.");

		try {

			// 构建select子句
			String tableName = ancesSelectSpec.getTableName();
			String tableAlias = ancesSelectSpec.getTableAlias();

			// If no DB table exist in cache according to the right table name, then return.
			TableSpec rightTableSpec = CtxCacheFacade.lookupTableSpec(tableName);
			if (rightTableSpec == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + tableName);
				return;
			}

			// Compute the select clause.
			computeSelectClause(mapperSql, artifactSpec, null, ancesSelectSpec, rightTableSpec);

			// 构建from子句
			StringBuffer fromClause = mapperSql.getFromClause();
			if (StringUtils.isNotBlank(fromClause)) {
				StringHelper.newLine(fromClause, JavaSrcElm.UNIT_OF_INDENT, 3);
				fromClause.append(MapperElm.COMMA + MapperElm.WHITE_SPACE);
			} else {
				MapperFormatter.beginFromStatement(fromClause);
			}

			fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, tableName, tableAlias));

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	/**
	 * This method is used to generate separate <sql> element for join. For outer join, join condition was included inside 'fromClause'. For inner join, join condition was included
	 * inside separate 'joinClause'.
	 */
	private void computeSqlElmtForJoin(MapperSql mapperSql, ArtifactSpec artifactSpec, SelectSpec ancesSelectSpec, List<OneToOneIdx> descOtoIdxList) throws AppException {

		logger.debug("ENTRY: Prepare to execute 'computeSqlClauseForJoin'.");

		// 根据joinType为空，即表示单表查询，单独构建from子句。
		int joinType = mapperSql.getJoinType();
		if (joinType == MapperSql.JOIN_TYPE_NONE) {

			String tableName = ancesSelectSpec.getTableName();
			String tableAlias = ancesSelectSpec.getTableAlias();

			// If no DB table exist in cache according to the right table name, then return.
			TableSpec rightTableSpec = CtxCacheFacade.lookupTableSpec(tableName);
			if (rightTableSpec == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + tableName);
				return;
			}

			// 构建from子句
			StringBuffer fromClause = mapperSql.getFromClause();
			if (StringUtils.isNotBlank(fromClause)) {
				StringHelper.newLine(fromClause, JavaSrcElm.UNIT_OF_INDENT, 3);
				fromClause.append(MapperElm.COMMA + MapperElm.WHITE_SPACE);
			} else {
				MapperFormatter.beginFromStatement(fromClause);
			}

			fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, tableName, tableAlias));

		} else {

			OneToOneIdx descOtoIdx = null;
			boolean isFirst = true;
			for (int i = 0; i < descOtoIdxList.size(); i++) {

				logger.debug("DESCENDANT: Get element at index '" + i + "'");
				descOtoIdx = descOtoIdxList.get(i);
				computeSelectClauseForJoin(mapperSql, artifactSpec, ancesSelectSpec, descOtoIdx, isFirst);

				// Set the flag 'the first oneToOne relationship' to false.
				isFirst = false;

			}

		}

		// 如果primaryClause不为空，则不需要再考虑<resultFilter>的配置
		StringBuffer primaryKeyClause = mapperSql.getPrimaryKeyClause();
		if (StringUtils.isNotBlank(primaryKeyClause) == true) {
			return;
		}

		List<ResultFilterSpec> filterList = ancesSelectSpec.getResultFilter();
		if (filterList != null) {
			// StringBuffer joinClause = mapperSql.getJoinClause();
			StringBuffer whereClause = mapperSql.getWhereClause();
			logger.debug("FILTER: 'filterList' of current 'ancesSelectSpec' is not null.");
			// if (StringUtils.isNotBlank(joinClause) || StringUtils.isNotBlank(primaryKeyClause) || StringUtils.isNotBlank(whereClause)) {
			if (joinType == MapperSql.JOIN_TYPE_PUBLIC_INNER) {
				computeResultFilterSpec(artifactSpec, whereClause, ancesSelectSpec, MapperElm.SQL_AND_SIMPLE);
			} else {
				if (StringUtils.isNotBlank(whereClause)) {
					computeResultFilterSpec(artifactSpec, whereClause, ancesSelectSpec, MapperElm.SQL_AND_SIMPLE);
				} else {
					computeResultFilterSpec(artifactSpec, whereClause, ancesSelectSpec, MapperElm.SQL_WHERE_SIMPLE);
				}
			}
		} else {
			logger.debug("FILTER: 'filterList' of current 'ancesSelectSpec' is null.");
		}

	}

	/**
	 * 构建包含各种 join 关系的 select 子句
	 *
	 * @param mapperSql
	 * @param artifactSpec
	 * @param ancesSelectSpec
	 * @param descOtoIdx
	 * @param isFirst
	 * @param reverse
	 * @throws AppException
	 */
	private void computeSelectClauseForJoin(MapperSql mapperSql, ArtifactSpec artifactSpec, SelectSpec ancesSelectSpec, OneToOneIdx descOtoIdx, boolean isFirst)
			throws AppException {

		logger.debug("ENTRY: Prepare to execute 'computeSelectClauseForJoin'.");

		try {

			/******************************************************************************************
			 * Build select clause.
			 ******************************************************************************************/
			// Left table and right table are used to describe join relationship in SQL.
			// And left table name is 'mediTableName' or 'fatherTableName', 'mediTableName' takes higher preference.
			String leftTableName = OneToOneIdxFacade.getRelLeftTableName(descOtoIdx);
			String leftTableAlias = OneToOneIdxFacade.getRelLeftTableAlias(descOtoIdx);
			String rightTableName = descOtoIdx.getRightTableName();
			String rightTableAlias = descOtoIdx.getRightTableAlias();

			// If no DB table exist in cache according to the right table name, then return.
			TableSpec rightTableSpec = CtxCacheFacade.lookupTableSpec(rightTableName);
			if (rightTableSpec == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: \"" + rightTableName + "\"");
				return;
			}

			// Compute the select clause.
			RelationshipSpec descOtoConfig = descOtoIdx.getOneToOne();
			computeSelectClause(mapperSql, artifactSpec, descOtoIdx, descOtoConfig.getSelectSpec(), rightTableSpec);

			/******************************************************************************************
			 * Build from clause.
			 ******************************************************************************************/
			int joinType = mapperSql.getJoinType();
			StringBuffer fromClause = mapperSql.getFromClause();
			if (joinType == MapperSql.JOIN_TYPE_PUBLIC_INNER || joinType == MapperSql.JOIN_TYPE_PRIVATE_INNER) {

				if (StringUtils.isNotBlank(fromClause)) {
					StringHelper.newLine(fromClause, JavaSrcElm.UNIT_OF_INDENT, 3);
					fromClause.append(MapperElm.COMMA + MapperElm.WHITE_SPACE);
				} else {
					MapperFormatter.beginFromStatement(fromClause);
				}

				// If it is the first oneToOne relationship
				if (isFirst == true) {
					// For reverse, append right table then left table.
					// if (reverse == true) {
					// fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, rightTableName, rightTableAlias));
					// StringHelper.newLine(fromClause, JavaSrcElm.UNIT_OF_INDENT, 3);
					// fromClause.append(MapperElm.COMMA + MapperElm.WHITE_SPACE);
					// fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, leftTableName, leftTableAlias));
					// } else {
					fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, leftTableName, leftTableAlias));
					StringHelper.newLine(fromClause, JavaSrcElm.UNIT_OF_INDENT, 3);
					fromClause.append(MapperElm.COMMA + MapperElm.WHITE_SPACE);
					fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, rightTableName, rightTableAlias));
					// }
				}
				// If it is not the first oneToOne relationship
				else if (isFirst == false) {
					// For reverse, then append left table only.
					// if (reverse == true) {
					// fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, leftTableName, leftTableAlias));
					// } else {
					fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, rightTableName, rightTableAlias));
					// }
				}

				if (joinType == MapperSql.JOIN_TYPE_PUBLIC_INNER) {
					// computeJoinClauseForPublicInnerJoin(mapperSql, artifactSpec, descOtoIdx, reverse);
					computeJoinClauseForPublicInnerJoin(mapperSql, artifactSpec, descOtoIdx);
				} else {
					// computeJoinClauseForInternalInnerJoin(mapperSql, artifactSpec, descOtoIdx, reverse);
					computeJoinClauseForInternalInnerJoin(mapperSql, artifactSpec, descOtoIdx);
				}

			} else {
				// computeJoinClauseForOuterJoin(mapperSql, artifactSpec, descOtoIdx, isFirst, reverse);
				computeJoinClauseForOuterJoin(mapperSql, artifactSpec, descOtoIdx, isFirst);
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	/**
	 * 构建被外部调用的内关联
	 *
	 * @param mapperSql
	 * @param artifactSpec
	 * @param descOtoIdx
	 * @param reverse
	 * @throws AppException
	 */
	private void computeJoinClauseForPublicInnerJoin(MapperSql mapperSql, ArtifactSpec artifactSpec, OneToOneIdx descOtoIdx) throws AppException {

		logger.debug("ENTRY: Prepare to execute 'computeJoinClauseForPublicInnerJoin'.");

		/******************************************************************************************
		 * Build join clause.
		 ******************************************************************************************/
		StringBuffer joinClause = mapperSql.getJoinClause();
		if (StringUtils.isNotBlank(joinClause)) {
			MapperFormatter.beginAndStatement(joinClause);
		} else {
			MapperFormatter.beginWhereStatement(joinClause);
		}

		// Attribute name of Java class, mapped to column name in DB table.
		RelationshipSpec descOtoConfig = descOtoIdx.getOneToOne();
		String leftTableName = OneToOneIdxFacade.getRelLeftTableName(descOtoIdx);
		String leftTableAlias = OneToOneIdxFacade.getRelLeftTableAlias(descOtoIdx);
		String rightTableName = descOtoIdx.getRightTableName();
		String rightTableAlias = descOtoIdx.getRightTableAlias();
		String leftAttrName = OneToOneIdxFacade.getRelLeftAttrName(descOtoIdx);
		String rightAttrName = descOtoConfig.getSonAttr();
		String leftColumnName = JavaFormatter.getDBStyle(leftAttrName);
		String rightColumnName = JavaFormatter.getDBStyle(rightAttrName);

		// Change the column name into full name containing alias.
		leftColumnName = MapperFormatter.getColumnFullName(artifactSpec, leftTableName, leftTableAlias, leftColumnName);
		rightColumnName = MapperFormatter.getColumnFullName(artifactSpec, rightTableName, rightTableAlias, rightColumnName);

		// Reverse the order because of handling tables from the last to the first.
		// if (reverse == true) {
		// joinClause.append(rightColumnName);
		// joinClause.append(MapperElm.EQUAL);
		// joinClause.append(leftColumnName);
		// } else {
		joinClause.append(leftColumnName);
		joinClause.append(MapperElm.EQUAL);
		joinClause.append(rightColumnName);
		// }

		/**********************************************************************************************
		 * Handle 'filter' from 'descSelectSpec' in every oneToOne.
		 **********************************************************************************************/
		SelectSpec descSelectSpec = descOtoConfig.getSelectSpec();
		List<ResultFilterSpec> filterList = descSelectSpec.getResultFilter();

		StringBuffer primaryKeyClause = mapperSql.getPrimaryKeyClause();
		StringBuffer whereClause = mapperSql.getWhereClause();
		if (filterList != null) {
			logger.debug("FILTER: 'filterList' of current 'descSelectSpec' is not null.");
			if (StringUtils.isNotBlank(joinClause) || StringUtils.isNotBlank(primaryKeyClause) || StringUtils.isNotBlank(whereClause)) {
				computeResultFilterSpec(artifactSpec, whereClause, descSelectSpec, MapperElm.SQL_AND_SIMPLE);
			} else {
				computeResultFilterSpec(artifactSpec, whereClause, descSelectSpec, MapperElm.SQL_WHERE_SIMPLE);
			}
		} else {
			logger.debug("FILTER: 'filterList' of current 'descSelectSpec' is null.");
		}

	}

	/**
	 * 构建内部的内关联
	 *
	 * @param mapperSql
	 * @param artifactSpec
	 * @param descOtoIdx
	 * @param reverse
	 * @throws AppException
	 */
	private void computeJoinClauseForInternalInnerJoin(MapperSql mapperSql, ArtifactSpec artifactSpec, OneToOneIdx descOtoIdx) throws AppException {

		logger.debug("ENTRY: Prepare to execute 'computeJoinClauseForPrivateInnerJoin'.");

		/******************************************************************************************
		 * Build join clause.
		 ******************************************************************************************/
		// Append join condition into where clause.
		StringBuffer whereClause = mapperSql.getWhereClause();
		if (StringUtils.isNotBlank(whereClause)) {
			MapperFormatter.beginAndStatement(whereClause);
		} else {
			MapperFormatter.beginWhereStatement(whereClause);
		}

		// Attribute name of Java class, mapped to column name in DB table.
		RelationshipSpec descOtoConfig = descOtoIdx.getOneToOne();
		String leftTableName = OneToOneIdxFacade.getRelLeftTableName(descOtoIdx);
		String leftTableAlias = OneToOneIdxFacade.getRelLeftTableAlias(descOtoIdx);
		String rightTableName = descOtoIdx.getRightTableName();
		String rightTableAlias = descOtoIdx.getRightTableAlias();
		String leftAttrName = OneToOneIdxFacade.getRelLeftAttrName(descOtoIdx);
		String rightAttrName = descOtoConfig.getSonAttr();
		String leftColumnName = JavaFormatter.getDBStyle(leftAttrName);
		String rightColumnName = JavaFormatter.getDBStyle(rightAttrName);

		// Change the column name into full name containing alias.
		leftColumnName = MapperFormatter.getColumnFullName(artifactSpec, leftTableName, leftTableAlias, leftColumnName);
		rightColumnName = MapperFormatter.getColumnFullName(artifactSpec, rightTableName, rightTableAlias, rightColumnName);

		// Reverse the order because of handling tables from the last to the first.
		// if (reverse == true) {
		// whereClause.append(rightColumnName);
		// whereClause.append(MapperElm.EQUAL);
		// whereClause.append(leftColumnName);
		// } else {
		whereClause.append(leftColumnName);
		whereClause.append(MapperElm.EQUAL);
		whereClause.append(rightColumnName);
		// }

		/**********************************************************************************************
		 * Handle 'filter' from 'descSelectSpec' in every oneToOne.
		 **********************************************************************************************/
		SelectSpec descSelectSpec = descOtoConfig.getSelectSpec();
		List<ResultFilterSpec> filterList = descSelectSpec.getResultFilter();

		if (filterList != null) {
			logger.debug("FILTER: 'filterList' of current 'descSelectSpec' is not null.");
			if (StringUtils.isNotBlank(whereClause)) {
				computeResultFilterSpec(artifactSpec, whereClause, descSelectSpec, MapperElm.SQL_AND_SIMPLE);
			} else {
				computeResultFilterSpec(artifactSpec, whereClause, descSelectSpec, MapperElm.SQL_WHERE_SIMPLE);
			}
		} else {
			logger.debug("FILTER: 'filterList' of current 'descSelectSpec' is null.");
		}

	}

	private void computeJoinClauseForOuterJoin(MapperSql mapperSql, ArtifactSpec artifactSpec, OneToOneIdx descOtoIdx, boolean isFirst) throws AppException {

		logger.debug("ENTRY: Prepare to execute 'computeJoinClauseForOuterJoin'.");

		// Build from clause.
		StringBuffer fromClause = mapperSql.getFromClause();
		if (StringUtils.isNotBlank(fromClause)) {
			StringHelper.newLine(fromClause, JavaSrcElm.UNIT_OF_INDENT, 3);
		} else {
			MapperFormatter.beginFromStatement(fromClause);
		}

		// If it is the first oneToOne relationship for reverse, then append right table then left table.
		String leftTableName = descOtoIdx.getLeftTableName();
		String leftTableAlias = descOtoIdx.getLeftTableAlias();
		// String rightTableName = descOtoIdx.getRightTableName();
		// String rightTableAlias = descOtoIdx.getRightTableAlias();
		if (isFirst == true) {
			// if (reverse == true) {
			// fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, rightTableName, rightTableAlias));
			// StringHelper.newLine(fromClause, JavaSrcElm.UNIT_OF_INDENT, 3);
			// fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, descOtoIdx, reverse));
			// } else {
			fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, leftTableName, leftTableAlias));
			StringHelper.newLine(fromClause, JavaSrcElm.UNIT_OF_INDENT, 3);
			fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, descOtoIdx));
			// }
		}
		// If it is not the first oneToOne relationship for reverse, then append left table only.
		else if (isFirst == false) {
			// if (reverse == true) {
			// fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, descOtoIdx, reverse));
			// } else {
			fromClause.append(MapperFormatter.getTableNamePair(artifactSpec, descOtoIdx));
			// }
		}

		StringBuffer primaryKeyClause = mapperSql.getPrimaryKeyClause();
		// 如果primaryKeyClause不为空，则不需要考虑<resultFilter>的配置内容
		if (StringUtils.isNotBlank(primaryKeyClause) == true) {
			return;
		}

		// Handle 'filter' from 'descSelectSpec' in every oneToOne.
		RelationshipSpec descOtoConfig = descOtoIdx.getOneToOne();
		SelectSpec descSelectSpec = descOtoConfig.getSelectSpec();
		List<ResultFilterSpec> filterList = descSelectSpec.getResultFilter();

		// Build where clause.
		StringBuffer whereClause = mapperSql.getWhereClause();
		// if (StringUtils.isNotBlank(whereClause)) {
		// StringHelper.newLine(whereClause, JavaSrcElm.UNIT_OF_INDENT, 3);
		// } else {
		// MapperFormatter.beginWhereStatement(whereClause);
		// }

		// StringBuffer whereClause = mapperSql.getWhereClause();

		// If the primaryKeyClause is not blank, then ignore the filter config.
		// if (primaryKeyClause == null || primaryKeyClause.length() == 0) {
		// populateResultFilterSpec(whereClause, descSelectSpec,
		// MapperElm.SQL_WHERE_SIMPLE);
		// }

		if (filterList != null) {
			// logger.debug("FILTER: 'filterList' of current 'descSelectSpec' is
			// not null.");
			// if (primaryKeyClause != null && primaryKeyClause.length() > 0 ||
			// whereClause.length() > 0) {

			computeResultFilterSpec(artifactSpec, whereClause, descSelectSpec, MapperElm.SQL_WHERE_SIMPLE);

			// } else {
			// populateResultFilterSpec(artifactSpec, whereClause,
			// descSelectSpec, MapperElm.SQL_WHERE_SIMPLE);
			// }
		} else {
			logger.debug("FILTER: 'filterList' of current 'descSelectSpec' is null.");
		}

	}

	private void computeResultFilterSpec(ArtifactSpec artifactSpec, StringBuffer filterClause, SelectSpec selectSpec, String filterPrefix) throws AppException {

		try {

			// Get 'filter' from current selectSpec.
			List<ResultFilterSpec> filterList = selectSpec.getResultFilter();
			if (filterList == null) {
				return;
			}

			String tableName = selectSpec.getTableName();
			String tableAlias = selectSpec.getTableAlias();
			ResultFilterSpec resultFilterSpec = null;
			String begin = null;
			String attribute = null;
			String comparator = null;
			String value = null;
			String constant = null;
			String end = null;
			ClassAnalyzer classAnalyzer = new ClassAnalyzer();

			logger.debug("FILTER: The size of current 'filterList' is: " + filterList.size());

			for (int j = 0; j < filterList.size(); j++) {

				resultFilterSpec = filterList.get(j);
				begin = resultFilterSpec.getBegin();
				attribute = resultFilterSpec.getAttribute();
				comparator = resultFilterSpec.getComparator();
				value = resultFilterSpec.getValue();
				constant = resultFilterSpec.getConstant();
				end = resultFilterSpec.getEnd();

				// Automatically set prefix for where clause according to filter.
				if (j == 0) {
					// 如果已经存在了filterSpec，则需要换行
					if (StringUtils.isNotBlank(filterClause) == true) {
						StringHelper.newLine(filterClause, JavaSrcElm.UNIT_OF_INDENT, 3);
						filterClause.append(MapperElm.SQL_AND_SIMPLE);
					} else {
						filterClause.append(filterPrefix);
					}
					filterClause.append(MapperElm.WHITE_SPACE);
				} else {
					StringHelper.newLine(filterClause, JavaSrcElm.UNIT_OF_INDENT, 3);
					if (StringUtils.isNotBlank(begin) == true) {
						// filterConfition.append(MapperElm.WHITE_SPACE);
						filterClause.append(begin);
						filterClause.append(MapperElm.WHITE_SPACE);
					}
				}
				if (StringUtils.isNotBlank(attribute) == true) {
					String columnName = JavaFormatter.getDBStyle(attribute);
					String columnFullName = MapperFormatter.getColumnFullName(artifactSpec, tableName, tableAlias, columnName);
					filterClause.append(columnFullName);
				}
				if (StringUtils.isNotBlank(comparator) == true) {
					filterClause.append(MapperElm.WHITE_SPACE);
					filterClause.append(comparator);
				}
				if (StringUtils.isNotBlank(value) == true) {
					filterClause.append(MapperElm.WHITE_SPACE);
					filterClause.append(value);
				} else if (StringUtils.isNotBlank(constant) == true) {
					Object objValue = classAnalyzer.getConstValue(constant);
					filterClause.append(MapperElm.WHITE_SPACE);
					if (objValue.getClass().getName().equals(JavaSrcElm.LANG_STRING_FULL) == true) {
						filterClause.append(JavaSrcElm.SINGLE_QUOTATION);
						filterClause.append(objValue.toString());
						filterClause.append(JavaSrcElm.SINGLE_QUOTATION);
					} else {
						filterClause.append(objValue.toString());
					}
				}
				if (StringUtils.isNotBlank(end) == true) {
					filterClause.append(MapperElm.WHITE_SPACE);
					filterClause.append(end);
				}

			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	// private void defineReusableSelectFrom4RootTab(MapperSql mapperSql, Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec,
	// SelectSpec selectSpec, TableSpec rootTable, String sqlId) throws AppException {
	//
	// WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
	//
	// // Lookup 'sql' element with 'sqlId', if not found, then create one.
	// Sql sql = CtxCacheFacade.lookupSql(workspaceSpec, daoSpec, sqlId);
	// if (sql != null) {
	// return;
	// }
	//
	// // Initiate a blank 'sql'. And register the new 'sql' element.
	// sql = mapperObjFactory.createSql();
	// mapper.getSql().add(sql);
	// sql.setId(sqlId);
	// CtxCacheFacade.addSql(workspaceSpec, daoSpec, sqlId, sql);
	//
	// // Compute the select clause.
	// computeSelectClause(mapperSql, artifactSpec, null, selectSpec, rootTable);
	//
	// // Populate from clause.
	// computeFromClause(mapperSql, artifactSpec, selectSpec);
	//
	// // Set select clause and from clause into mapper.
	// StringBuffer selectClause = mapperSql.getSelectClause();
	// StringBuffer fromClause = mapperSql.getFromClause();
	// sql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + selectClause.toString() + fromClause.toString() + MapperElm.CDATA_ANCHOR_END);
	//
	// // Define separate column name clause for easy to reuse.
	// String columnNameId = sqlId + MapperElm.MAPPER_COL_NM;
	// StringBuffer columnNameClause = mapperSql.getColumnNameClause();
	// Sql columnNameSql = mapperObjFactory.createSql();
	// mapper.getSql().add(columnNameSql);
	// columnNameSql.setId(columnNameId);
	// columnNameSql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + columnNameClause + MapperElm.CDATA_ANCHOR_END);
	//
	// // Define separate column alias clause for easy to reuse.
	// String columnAliasId = sqlId + MapperElm.MAPPER_COL_ALIAS;
	// StringBuffer columnAliasClause = mapperSql.getColumnAliasClause();
	// Sql columnAliasSql = mapperObjFactory.createSql();
	// mapper.getSql().add(columnAliasSql);
	// columnAliasSql.setId(columnAliasId);
	// columnAliasSql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + columnAliasClause + MapperElm.CDATA_ANCHOR_END);
	//
	// // Define separate from clause for easy to reuse.
	// String fromId = sqlId + MapperElm.MAPPER_FROM;
	// Sql fromSql = mapperObjFactory.createSql();
	// mapper.getSql().add(fromSql);
	// fromSql.setId(fromId);
	// fromSql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + fromClause + MapperElm.CDATA_ANCHOR_END);
	//
	// }

	private void defineReusableSelectFrom4MultiTab(ArtifactSpec artifactSpec, MapperSql mapperSql, Mapper mapper, DaoSpec daoSpec, String sqlId) {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		// Lookup 'sql' element with 'sqlId', if not found, then create one.
		Sql sql = CtxCacheFacade.lookupSql(workspaceSpec, daoSpec, sqlId);
		if (sql == null) {

			// Initiate a blank 'sql'. And register the new 'sql' element.
			sql = mapperObjFactory.createSql();
			mapper.getSql().add(sql);
			sql.setId(sqlId);
			CtxCacheFacade.addSql(workspaceSpec, daoSpec, sqlId, sql);

			// For outer join, join condition was included inside 'fromClause'.
			StringBuffer selectClause = mapperSql.getSelectClause();
			StringBuffer fromClause = mapperSql.getFromClause();
			StringBuffer joinClause = mapperSql.getJoinClause();
			int joinType = mapperSql.getJoinType();
			if (joinType == MapperSql.JOIN_TYPE_OUTER) {
				sql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + selectClause.toString() + fromClause.toString() + MapperElm.CDATA_ANCHOR_END);
			}
			// For inner join, join condition was included inside 'joinClause'.
			else {
				sql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + selectClause.toString() + fromClause.toString() + joinClause.toString() + MapperElm.CDATA_ANCHOR_END);
			}

			// Define separate column name clause for easy to reuse.
			String columnNameId = sqlId + MapperElm.MAPPER_COL_NM;
			StringBuffer columnNameClause = mapperSql.getColumnNameClause();
			// String columnClause =
			// selectClause.substring((MapperElm.SQL_SELECT +
			// MapperElm.WHITE_SPACE).length());
			Sql columnNameSql = mapperObjFactory.createSql();
			mapper.getSql().add(columnNameSql);
			columnNameSql.setId(columnNameId);
			columnNameSql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + columnNameClause + MapperElm.CDATA_ANCHOR_END);

			// Define separate column alias clause for easy to reuse.
			String columnAliasId = sqlId + MapperElm.MAPPER_COL_ALIAS;
			StringBuffer columnAliasClause = mapperSql.getColumnAliasClause();
			Sql columnAliasSql = mapperObjFactory.createSql();
			mapper.getSql().add(columnAliasSql);
			columnAliasSql.setId(columnAliasId);
			columnAliasSql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + columnAliasClause + MapperElm.CDATA_ANCHOR_END);

			// Define separate from clause for easy to reuse.
			String fromId = sqlId + MapperElm.MAPPER_FROM;
			Sql fromSql = mapperObjFactory.createSql();
			mapper.getSql().add(fromSql);
			fromSql.setId(fromId);
			// For outer join, join condition was included inside 'fromClause'.
			if (joinType == MapperSql.JOIN_TYPE_OUTER) {
				fromSql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + fromClause + MapperElm.CDATA_ANCHOR_END);
			}
			// For inner join, join condition was included inside 'joinClause'.
			else {
				fromSql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + fromClause + joinClause.toString() + MapperElm.CDATA_ANCHOR_END);
			}

		}

	}

	/**
	 * Whether or not the including join are all inner join.
	 *
	 * @param descOtoIdxList
	 * @return
	 */
	private boolean hasOuterJoin(List<OneToOneIdx> otoIndexList) throws AppException {

		boolean result = false;

		try {

			if (otoIndexList == null || otoIndexList.size() == 0) {
				throw new AppException("The given 'otoIndexList' could not be null or blank.");
			}

			OneToOneIdx otoIndex = null;
			RelationshipSpec otoSpec = null;
			String joinType = null;
			for (int i = 0; i < otoIndexList.size(); i++) {

				otoIndex = otoIndexList.get(i);
				otoSpec = otoIndex.getOneToOne();
				joinType = otoSpec.getJoinType();

				if (StringUtils.isNotBlank(joinType) == true && (otoSpec.getJoinType().equalsIgnoreCase(MapperElm.SQL_JOIN_LEFT) == true
						|| otoSpec.getJoinType().equalsIgnoreCase(MapperElm.SQL_JOIN_RIGHT) == true || otoSpec.getJoinType().equalsIgnoreCase(MapperElm.SQL_JOIN_FULL) == true)) {

					result = true;

				}
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return result;

	}

	private void setJoinType(MapperSql mapperSql, List<OneToOneIdx> descOtoIdxList) throws AppException {
		int joinType = -1;
		if (descOtoIdxList == null || descOtoIdxList.size() == 0) {
			joinType = MapperSql.JOIN_TYPE_NONE;
		} else if (descOtoIdxList != null && descOtoIdxList.size() != 0 && hasOuterJoin(descOtoIdxList) == true) {
			joinType = MapperSql.JOIN_TYPE_OUTER;
		} else {
			joinType = MapperSql.JOIN_TYPE_PUBLIC_INNER;
		}
		mapperSql.setJoinType(joinType);
	}

}