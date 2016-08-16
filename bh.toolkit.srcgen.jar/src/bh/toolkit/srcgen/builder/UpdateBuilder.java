package bh.toolkit.srcgen.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.DaoSpec;
import bh.toolkit.srcgen.model.artifact.UpdateSpec;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;
import bh.toolkit.srcgen.model.mybatis.If;
import bh.toolkit.srcgen.model.mybatis.Include;
import bh.toolkit.srcgen.model.mybatis.Mapper;
import bh.toolkit.srcgen.model.mybatis.ObjectFactory;
import bh.toolkit.srcgen.model.mybatis.Sql;
import bh.toolkit.srcgen.model.mybatis.Trim;
import bh.toolkit.srcgen.model.mybatis.Update;
import bh.toolkit.srcgen.model.rdb.ColumnSpec;
import bh.toolkit.srcgen.model.rdb.TableSpec;
import bh.toolkit.srcgen.registry.CtxCacheFacade;
import bh.toolkit.srcgen.util.ExceptionUtil;
import bh.toolkit.srcgen.util.JavaFormatter;
import bh.toolkit.srcgen.util.MapperFormatter;

public class UpdateBuilder {

	private static Logger logger = Logger.getLogger(UpdateBuilder.class);

	private ObjectFactory mapperObjFactory;

	public UpdateBuilder(ObjectFactory mapperObjFactory) {
		this.mapperObjFactory = mapperObjFactory;
	}

	public void buildAllUpdate(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec) throws AppException {

		List<UpdateSpec> updateByPKSpecList = null;
		List<UpdateSpec> updateBySqlSpecList = null;

		// 通过构建配置对象，模拟Ant任务中的配置信息
		updateByPKSpecList = daoSpec.getUpdateByPKSpec();
		updateBySqlSpecList = daoSpec.getUpdateBySqlSpec();
		simulateAntConfig(daoSpec, updateByPKSpecList, updateBySqlSpecList);

		// Update all columns by primary key.
		for (UpdateSpec updateByPKSpec : updateByPKSpecList) {

			logger.debug("UPDATE BY PK: Handle \"updateByPKSpec\" for table \"" + updateByPKSpec.getTableName() + ".\"");

			// Map DB table and column into VO class.
			CtxCacheFacade.addVoClass(artifactSpec.getCommonAttrSpec().getWorkspaceSpec(), updateByPKSpec.getTableName());
			// Without time lock.
			buildUpdateByPK(mapper, artifactSpec, daoSpec, updateByPKSpec, true, false);
			// With time lock.
			buildUpdateByPK(mapper, artifactSpec, daoSpec, updateByPKSpec, true, true);

		}

		// Update selective columns by primary key.
		for (UpdateSpec updateByPKSpec : updateByPKSpecList) {
			// Without time lock.
			buildUpdateByPK(mapper, artifactSpec, daoSpec, updateByPKSpec, false, false);
			// With time lock.
			buildUpdateByPK(mapper, artifactSpec, daoSpec, updateByPKSpec, false, true);
		}

		// Update all columns by sql clause.
		for (UpdateSpec updBySqlSpec : updateBySqlSpecList) {
			logger.debug("UPDATE BY SQL: Handle \"updBySqlSpec\" for table \"" + updBySqlSpec.getTableName() + ".\"");
			buildUpdateBySql(mapper, artifactSpec, daoSpec, updBySqlSpec, true);
		}

		// Update selective columns by sql clause.
		for (UpdateSpec updBySqlSpec : updateBySqlSpecList) {
			buildUpdateBySql(mapper, artifactSpec, daoSpec, updBySqlSpec, false);
		}

	}

	private void simulateAntConfig(DaoSpec daoSpec, List<UpdateSpec> updateByPKSpecList, List<UpdateSpec> updateBySqlSpecList) {

		bh.toolkit.srcgen.model.artifact.ObjectFactory artifactObjFactory = new bh.toolkit.srcgen.model.artifact.ObjectFactory();
		UpdateSpec seudoUpdateByPKSpec = artifactObjFactory.createUpdateSpec();
		seudoUpdateByPKSpec.setTableName(daoSpec.getShortName());
		seudoUpdateByPKSpec.setDesc(daoSpec.getDesc());
		updateByPKSpecList.add(seudoUpdateByPKSpec);

		UpdateSpec seudoUpdateBySqlSpec = artifactObjFactory.createUpdateSpec();
		seudoUpdateBySqlSpec.setTableName(daoSpec.getShortName());
		seudoUpdateBySqlSpec.setDesc(daoSpec.getDesc());
		updateBySqlSpecList.add(seudoUpdateBySqlSpec);

	}

	private void buildUpdateByPK(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, UpdateSpec updateByPKSpec, boolean isUpdAll,
			boolean hasTmLck) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// Initiate a blank Update.
			Update update = mapperObjFactory.createUpdate();
			mapper.getUpdate().add(update);

			// Populate the 'id' attribute for 'update'.
			String updateId = MapperFormatter.getUpdateIdByPK(updateByPKSpec, isUpdAll, hasTmLck);
			update.setId(updateId);
			// Populate the 'paramType' attribute for 'update'.
			String tableName = updateByPKSpec.getTableName();
			if (hasTmLck == true) {
				update.setParameterType(java.util.Map.class.getName());
			} else {
				String paramFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
				update.setParameterType(paramFullName);
			}

			// Find TableSpec corresponding to current Update, if no Table found, then return Update without elements.
			TableSpec tableSpec = CtxCacheFacade.lookupTableSpec(tableName);
			if (tableSpec == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + tableName);
				return;
			}

			// Compute 'id' attribute for 'sql'.
			String sqlId = MapperFormatter.getSqlIdOfUpdByPK(updateByPKSpec, isUpdAll, hasTmLck);

			// Lookup 'sql' element with 'sqlId', if not found, then create one.
			Sql sql = CtxCacheFacade.lookupSql(workspaceSpec, daoSpec, sqlId);
			if (sql == null) {
				buildSetClause(mapper, artifactSpec, daoSpec, updateByPKSpec, tableSpec, isUpdAll, false, hasTmLck, sqlId);
			}

			// Anyway, need to populate primary key clause.
			StringBuffer primaryKeyClause = new StringBuffer();
			computePKClause(primaryKeyClause, tableSpec, hasTmLck);

			// Refer to common sql clause in 'select'.
			Include include = mapperObjFactory.createInclude();
			include.setRefid(sqlId);
			update.getInclude().add(include);
			update.setCDataEnd(MapperElm.CDATA_ANCHOR_BEGIN + primaryKeyClause.toString() + MapperElm.CDATA_ANCHOR_END);

			// Register a DAO method.
			String comments = updateByPKSpec.getDesc();
			if (isUpdAll == true) {
				comments += JavaSrcElm.IMPACT_COL_ALL;
			} else {
				comments += JavaSrcElm.IMPACT_COL_SEL;
			}

			List<String> paramTypeList = new ArrayList<String>();
			String voFullName = JavaFormatter.getVoFullName(workspaceSpec, tableName);
			// String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
			if (hasTmLck == true) {
				paramTypeList.add(JavaSrcElm.UTIL_MAP_FULL + JavaSrcElm.LESS_THAN + JavaSrcElm.LANG_STRING_FULL + JavaSrcElm.COMMA
						+ JavaSrcElm.WHITE_SPACE + voFullName + JavaSrcElm.LARGER_THAN);
				comments += JavaSrcElm.UPD_WITH_TMLCK;
			} else {
				paramTypeList.add(voFullName);
				comments += JavaSrcElm.UPD_WITHOUT_TMLCK;
			}

			CtxCacheFacade.addDaoMethod(artifactSpec, daoSpec.getShortName(), updateId, paramTypeList, JavaSrcElm.INT, comments);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void buildUpdateBySql(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, UpdateSpec updateBySqlSpec, boolean isUpdAll)
			throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// Initiate a blank Update.
			Update update = mapperObjFactory.createUpdate();
			mapper.getUpdate().add(update);

			// Populate the 'id' attribute for 'update'.
			String updateId = MapperFormatter.getUpdateIdBySql(updateBySqlSpec, isUpdAll);
			update.setId(updateId);
			// Populate the 'paramType' attribute for 'update'.
			String tableName = updateBySqlSpec.getTableName();
			String paramFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
			update.setParameterType(paramFullName);

			// Find TableSpec corresponding to current Update, if no Table found, then return Update without elements.
			TableSpec tableSpec = CtxCacheFacade.lookupTableSpec(tableName);
			if (tableSpec == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + tableName);
				return;
			}

			// Compute 'id' attribute for 'sql'.
			String sqlId = MapperFormatter.getSqlIdOfUpdBySql(updateBySqlSpec, isUpdAll, false);

			// Lookup 'sql' element with 'sqlId', if not found, then create one.
			Sql sql = CtxCacheFacade.lookupSql(workspaceSpec, daoSpec, sqlId);
			if (sql == null) {
				buildSetClause(mapper, artifactSpec, daoSpec, updateBySqlSpec, tableSpec, isUpdAll, true, false, sqlId);
			}

			// Refer to common sql clause in 'select'.
			Include include = mapperObjFactory.createInclude();
			include.setRefid(sqlId);
			update.getInclude().add(include);

			Trim whereTrim = mapperObjFactory.createTrim();
			update.getTrim().add(whereTrim);
			whereTrim.setPrefix(MapperElm.SQL_WHERE_SIMPLE);
			whereTrim.setPrefixOverrides(MapperElm.SQL_AND_OR);
			whereTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.ANCHOR_MAPPERSQL_WHERE_CLAUSE + MapperElm.CDATA_ANCHOR_END);

			// Register a DAO method.
			String comments = updateBySqlSpec.getDesc();
			if (isUpdAll == true) {
				comments += JavaSrcElm.IMPACT_COL_ALL;
			} else {
				comments += JavaSrcElm.IMPACT_COL_SEL;
			}
			List<String> paramTypeNameList = new ArrayList<String>();
			String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
			paramTypeNameList.add(dtoXFullName);
			CtxCacheFacade.addDaoMethod(artifactSpec, daoSpec.getShortName(), updateId, paramTypeNameList, JavaSrcElm.INT, comments);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void buildSetClause(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, UpdateSpec updateConfig, TableSpec tableSpec,
			boolean isUpdAll, boolean isBySql, boolean hasTmLck, String sqlId) throws AppException {

		// Initiate a blank 'sql'.
		Sql sql = mapperObjFactory.createSql();
		mapper.getSql().add(sql);
		sql.setId(sqlId);

		// Populate set clause.
		// StringBuffer setClause = new StringBuffer();
		Trim trim = mapperObjFactory.createTrim();
		sql.getTrim().add(trim);
		if (isUpdAll == true) {
			populateSetAll(trim, updateConfig, tableSpec, isBySql, hasTmLck);
		} else {
			populateSetSel(trim, updateConfig, tableSpec, isBySql, hasTmLck);
		}

		// Register the new 'sql' element.
		CtxCacheFacade.addSql(artifactSpec.getCommonAttrSpec().getWorkspaceSpec(), daoSpec, sqlId, sql);

	}

	private void populateSetAll(Trim updTrim, UpdateSpec updateConfig, TableSpec tableSpec, boolean isBySql, boolean hasTmLck) throws AppException {

		try {

			String tableName = updateConfig.getTableName();
			String voSimpleName = JavaFormatter.getJavaStyle(tableName, false, false) + JavaSrcElm.VO_NAME_SUFFIX;
			ColumnSpec ormColumn = null;
			String columnName = null;
			String attrName = null;
			StringBuffer setClause = new StringBuffer();
			updTrim.setPrefix(MapperElm.SQL_UPDATE + MapperElm.WHITE_SPACE + tableName + MapperElm.WHITE_SPACE + MapperElm.SQL_SET);
			updTrim.setPrefixOverrides(MapperElm.COMMA + MapperElm.WHITE_SPACE);

			// Populate sql according to the the TableSpec definition.
			List<ColumnSpec> ormColumnList = tableSpec.getColumnList();
			for (int i = 0; i < ormColumnList.size(); i++) {

				// Get Java style according to columnName.
				ormColumn = ormColumnList.get(i);
				columnName = ormColumn.getName();
				attrName = JavaFormatter.getJavaStyle(columnName, false, false);
				MapperFormatter.beginWithNewLine(setClause, MapperElm.COMMA + MapperElm.WHITE_SPACE);

				// Set value for OrmColumn.
				//				setClause.append(keyWordsTransformer.getTransferredStr(columnName) + MapperElm.EQUAL);
				setClause.append(columnName + MapperElm.EQUAL);
				// The case that update by sql clause.
				if (isBySql == true) {
					setClause.append(MapperElm.POUND + MapperElm.LEFT_BRACKET + voSimpleName + JavaSrcElm.DOT + attrName + MapperElm.RIGHT_BRACKET);
				}
				// The case that update by primary key.
				else {
					if (hasTmLck == true) {
						setClause.append(MapperElm.POUND + MapperElm.LEFT_BRACKET + JavaSrcElm.PARAM_MAP_NEW_VAL_KEY + JavaSrcElm.DOT + attrName
								+ MapperElm.RIGHT_BRACKET);
					} else {
						setClause.append(MapperElm.POUND + MapperElm.LEFT_BRACKET + attrName + MapperElm.RIGHT_BRACKET);
					}
				}

			}

			updTrim.setCDataEnd(MapperElm.CDATA_ANCHOR_BEGIN + setClause.toString() + MapperElm.CDATA_ANCHOR_END);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void populateSetSel(Trim updTrim, UpdateSpec updateConfig, TableSpec tableSpec, boolean isBySql, boolean hasTmLck) throws AppException {

		try {

			// UpdateSpec updateByPKConfig =
			// artifactSpec.getSingleArtifactSpec().getUpdateByPKSpec();
			String tableName = updateConfig.getTableName();
			String voSimpleName = JavaFormatter.getJavaStyle(tableName, false, false) + JavaSrcElm.VO_NAME_SUFFIX;
			ColumnSpec ormColumn = null;
			String columnName = null;
			String attrName = null;
			StringBuffer setClause = new StringBuffer();
			updTrim.setPrefix(MapperElm.SQL_UPDATE + MapperElm.WHITE_SPACE + tableName + MapperElm.WHITE_SPACE + MapperElm.SQL_SET);
			updTrim.setPrefixOverrides(MapperElm.COMMA);
			List<If> valIfList = updTrim.getIf();
			If valIf = null;
			String javaNullExpr = null;

			// Populate sql according to the the TableSpec definition.
			List<ColumnSpec> ormColumnList = tableSpec.getColumnList();
			for (int i = 0; i < ormColumnList.size(); i++) {

				// Get Java style according to columnName.
				ormColumn = ormColumnList.get(i);
				columnName = ormColumn.getName();
				attrName = JavaFormatter.getJavaStyle(columnName, false, false);
				javaNullExpr = JavaFormatter.getJavaNullExpr(ormColumn);
				setClause.append(MapperElm.COMMA);
				setClause.append(MapperElm.WHITE_SPACE);
				//				setClause.append(keyWordsTransformer.getTransferredStr(columnName));
				setClause.append(columnName);
				setClause.append(MapperElm.EQUAL);
				setClause.append(MapperElm.POUND);
				setClause.append(MapperElm.LEFT_BRACKET);

				// Populate 'if' element with not null expression.
				valIf = mapperObjFactory.createIf();
				// The case that update by sql clause.
				if (isBySql == true) {
					valIf.setTest(voSimpleName + JavaSrcElm.DOT + attrName + javaNullExpr);
					setClause.append(voSimpleName);
					setClause.append(JavaSrcElm.DOT);
					setClause.append(attrName);
					setClause.append(MapperElm.RIGHT_BRACKET);
				}
				// The case that update by primary key.
				else {
					if (hasTmLck == true) {
						valIf.setTest(JavaSrcElm.PARAM_MAP_NEW_VAL_KEY + JavaSrcElm.DOT + attrName + javaNullExpr);
						setClause.append(JavaSrcElm.PARAM_MAP_NEW_VAL_KEY);
						setClause.append(JavaSrcElm.DOT);
						setClause.append(attrName);
						setClause.append(MapperElm.RIGHT_BRACKET);
					} else {
						valIf.setTest(attrName + javaNullExpr);
						setClause.append(attrName);
						setClause.append(MapperElm.RIGHT_BRACKET);
					}
				}

				valIf.setCDataBegin(setClause.toString());
				valIfList.add(valIf);
				setClause.delete(0, setClause.length());

			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void computePKClause(StringBuffer primaryKeyClause, TableSpec tableSpec, boolean hasTmLck) throws AppException {

		try {

			// Prepare to populate primary key clause.
			ColumnSpec ormColumn = null;
			String columnName = null;
			String attrName = null;

			// Populate sql according to the the TableSpec definition.
			List<ColumnSpec> ormColumnList = tableSpec.getColumnList();
			for (int i = 0; i < ormColumnList.size(); i++) {

				// Get Java style according to columnName.
				ormColumn = ormColumnList.get(i);
				columnName = ormColumn.getName();
				attrName = JavaFormatter.getJavaStyle(columnName, false, false);

				if (ormColumn.isPrimaryKey() == true) {

					// Record the matched column.
					// TableSpecFacade.addMatchedColumn(tableSpec, column);

					if (primaryKeyClause.length() == 0) {
						MapperFormatter.beginWhereStatement(primaryKeyClause);
					} else {
						primaryKeyClause.append(MapperElm.SQL_AND_FULL);
					}

					//					primaryKeyClause.append(keyWordsTransformer.getTransferredStr(columnName));
					primaryKeyClause.append(columnName);
					primaryKeyClause.append(MapperElm.EQUAL);
					primaryKeyClause.append(MapperElm.POUND);
					primaryKeyClause.append(MapperElm.LEFT_BRACKET);
					if (hasTmLck == true) {
						primaryKeyClause.append(JavaSrcElm.PARAM_MAP_OLD_VAL_KEY + JavaSrcElm.DOT + attrName);
					} else {
						primaryKeyClause.append(attrName);
					}
					primaryKeyClause.append(MapperElm.RIGHT_BRACKET);
					// primaryKeyClause.append(ormColumn.getJdbcTypeName());
					// primaryKeyClause.append(MapperElm.POUND);

					logger.debug("FIND MAPPING: Find primary mapping between property '" + attrName + "' and column '" + columnName + "'");

				}

			}

			if (hasTmLck == true) {
				primaryKeyClause.append(MapperElm.TM_LCK_CLAUSE);
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

}
