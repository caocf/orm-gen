package bh.toolkit.srcgen.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.DaoSpec;
import bh.toolkit.srcgen.model.artifact.DeleteSpec;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;
import bh.toolkit.srcgen.model.mybatis.Delete;
import bh.toolkit.srcgen.model.mybatis.Foreach;
import bh.toolkit.srcgen.model.mybatis.Include;
import bh.toolkit.srcgen.model.mybatis.Mapper;
import bh.toolkit.srcgen.model.mybatis.ObjectFactory;
import bh.toolkit.srcgen.model.mybatis.Sql;
import bh.toolkit.srcgen.model.mybatis.Trim;
import bh.toolkit.srcgen.model.rdb.ColumnSpec;
import bh.toolkit.srcgen.model.rdb.TableSpec;
import bh.toolkit.srcgen.registry.CtxCacheFacade;
import bh.toolkit.srcgen.util.ExceptionUtil;
import bh.toolkit.srcgen.util.JavaFormatter;
import bh.toolkit.srcgen.util.MapperFormatter;

/**
 * Description: 构建删除语句
 * Author: zhaoruibin
 * Creation time: 2015年11月7日 下午3:19:49
 */
public class DeleteBuilder {

	private static Logger logger = Logger.getLogger(DeleteBuilder.class);

	private ObjectFactory mapperObjFactory;

	public DeleteBuilder(ObjectFactory mapperObjFactory) {
		this.mapperObjFactory = mapperObjFactory;
	}

	public void buildAllDelete(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec) throws AppException {

		// 通过构建配置对象，模拟Ant任务中的配置信息
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		List<DeleteSpec> deleteByPKSpecList = daoSpec.getDeleteByPKSpec();
		List<DeleteSpec> deleteBySqlSpecList = daoSpec.getDeleteBySqlSpec();
		simulateAntConfig(daoSpec, deleteByPKSpecList, deleteBySqlSpecList);

		// Delete all columns by primary key.
		deleteByPKSpecList = daoSpec.getDeleteByPKSpec();
		for (DeleteSpec deleteByPKSpec : deleteByPKSpecList) {

			logger.debug("DELETE BY PK: Handle \"deleteByPKSpec\" for table \"" + deleteByPKSpec.getTableName() + ".\"");

			// Map DB table and column into VO class.
			CtxCacheFacade.addVoClass(workspaceSpec, deleteByPKSpec.getTableName());

			// Delete with PK of type java.lang.Long
			buildDeleteByPK(mapper, artifactSpec, daoSpec, deleteByPKSpec, true, false);

			// Delete with PK of type VO without time lock.
			buildDeleteByPK(mapper, artifactSpec, daoSpec, deleteByPKSpec, false, false);

			// Delete with PK of type VO with time lock.
			buildDeleteByPK(mapper, artifactSpec, daoSpec, deleteByPKSpec, false, true);

			// Delete with PK list.
			buildDeleteByPKList(mapper, artifactSpec, daoSpec, deleteByPKSpec);
		}

		// Delete all columns by sql clause.
		deleteBySqlSpecList = daoSpec.getDeleteBySqlSpec();
		for (DeleteSpec deleteBySqlSpec : deleteBySqlSpecList) {
			logger.debug("DELETE BY SQL: Handle \"updateBySqlSpec\" for table \"" + deleteBySqlSpec.getTableName() + ".\"");
			buildDeleteBySql(mapper, artifactSpec, daoSpec, deleteBySqlSpec);
		}

	}

	private void simulateAntConfig(DaoSpec daoSpec, List<DeleteSpec> deleteByPKSpecList, List<DeleteSpec> deleteBySqlSpecList) {

		bh.toolkit.srcgen.model.artifact.ObjectFactory artifactObjFactory = new bh.toolkit.srcgen.model.artifact.ObjectFactory();
		DeleteSpec seudoDeleteByPKSpec = artifactObjFactory.createDeleteSpec();
		seudoDeleteByPKSpec.setTableName(daoSpec.getShortName());
		seudoDeleteByPKSpec.setDesc(daoSpec.getDesc());
		deleteByPKSpecList.add(seudoDeleteByPKSpec);

		DeleteSpec seudoDeleteBySqlSpec = artifactObjFactory.createDeleteSpec();
		seudoDeleteBySqlSpec.setTableName(daoSpec.getShortName());
		seudoDeleteBySqlSpec.setDesc(daoSpec.getDesc());
		deleteBySqlSpecList.add(seudoDeleteBySqlSpec);

	}

	private void buildDeleteByPK(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, DeleteSpec deleteByPKSpec, boolean isByLong,
			boolean hasTmLck) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// Initiate a blank Delete.
			Delete delete = mapperObjFactory.createDelete();
			mapper.getDelete().add(delete);

			// Populate the 'id' attribute for 'delete'.
			String deleteId = MapperFormatter.getDeleteIdByPK(deleteByPKSpec, isByLong, hasTmLck);
			delete.setId(deleteId);
			String tableName = deleteByPKSpec.getTableName();
			String voFullName = JavaFormatter.getVoFullName(workspaceSpec, tableName);
			if (hasTmLck == true) {
				delete.setParameterType(voFullName);
			} else {
				if (isByLong == true) {
					delete.setParameterType(JavaSrcElm.LANG_LONG_FULL);
				} else {
					delete.setParameterType(voFullName);
				}
			}

			// Find tableSpec corresponding to current Delete, if no Table found, then return Delete without elements.
			TableSpec tableSpec = CtxCacheFacade.lookupTableSpec(tableName);
			if (tableSpec == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + tableName);
				return;
			}

			// Compute 'id' attribute for 'sql'.
			String sqlId = MapperFormatter.getSqlIdOfDel(deleteByPKSpec);

			// Lookup 'sql' element with 'sqlId', if not found, then create one.
			Sql sql = CtxCacheFacade.lookupSql(workspaceSpec, daoSpec, sqlId);
			if (sql == null) {
				buildDeleteClause(mapper, artifactSpec, daoSpec, deleteByPKSpec, tableSpec, false, hasTmLck, sqlId);
			}

			// Anyway, need to populate primary key clause.
			StringBuffer primaryKeyClause = new StringBuffer();
			computePKClause(primaryKeyClause, tableSpec, hasTmLck);

			// Refer to common sql clause in 'select'.
			Include include = mapperObjFactory.createInclude();
			include.setRefid(sqlId);
			delete.getInclude().add(include);
			delete.setCDataEnd(MapperElm.CDATA_ANCHOR_BEGIN + primaryKeyClause.toString() + MapperElm.CDATA_ANCHOR_END);

			// Register a DAO method.
			String comments = deleteByPKSpec.getDesc();
			List<String> paramTypeList = new ArrayList<String>();
			// 如果包含时间锁，则时间锁信息需要从DTOX中获取
			if (isByLong == true) {
				paramTypeList.add(JavaSrcElm.LANG_LONG_FULL);
			} else {
				if (hasTmLck == true) {
					paramTypeList.add(voFullName);
					comments += JavaSrcElm.DELETE_UPD_WITH_TMLCK;
				} else {
					paramTypeList.add(voFullName);
					comments += JavaSrcElm.UPD_WITHOUT_TMLCK;
				}
			}

			CtxCacheFacade.addDaoMethod(artifactSpec, daoSpec.getShortName(), deleteId, paramTypeList, JavaSrcElm.INT, comments);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void buildDeleteByPKList(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, DeleteSpec deleteByPKSpec) throws AppException {
		try {

			// Initiate a blank Delete.
			Delete delete = mapperObjFactory.createDelete();
			mapper.getDelete().add(delete);
			delete.setParameterType(JavaSrcElm.UTIL_LIST_FULL);

			// Populate the 'id' attribute for 'delete'.
			String deleteId = MapperFormatter.getDeleteIdByPK(deleteByPKSpec, true, false);
			deleteId = deleteId + JavaSrcElm.UTIL_LIST_SIMPLE;
			delete.setId(deleteId);

			// Find TableSpec corresponding to current Delete, if no Table found, then return Delete without elements.
			String tableName = deleteByPKSpec.getTableName();
			TableSpec tableSpec = CtxCacheFacade.lookupTableSpec(tableName);
			if (tableSpec == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + tableName);
				return;
			}

			// Compute 'id' attribute for 'sql'.
			String sqlId = MapperFormatter.getSqlIdOfDel(deleteByPKSpec);

			// Lookup 'sql' element with 'sqlId', if not found, then create one.
			Sql sql = CtxCacheFacade.lookupSql(artifactSpec.getCommonAttrSpec().getWorkspaceSpec(), daoSpec, sqlId);
			if (sql == null) {
				buildDeleteClause(mapper, artifactSpec, daoSpec, deleteByPKSpec, tableSpec, false, false, sqlId);
			}

			// Refer to common sql clause in 'select'.
			Include include = mapperObjFactory.createInclude();
			include.setRefid(sqlId);
			delete.getInclude().add(include);

			StringBuffer beginWhereInClause = new StringBuffer();
			computeWhereInClause(beginWhereInClause, tableSpec);
			delete.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + beginWhereInClause.toString() + MapperElm.CDATA_ANCHOR_END);

			// Add the foreach
			Foreach foreach = mapperObjFactory.createForeach();
			foreach.setCollection(MapperElm.FOR_EACH_COLLECTION_LIST);
			foreach.setItem(MapperElm.MAPPER_PK_VAL_ID);
			foreach.setIndex(MapperElm.FOR_EACH_INDEX);
			foreach.setSeparator(MapperElm.COMMA);
			foreach.setOpen(MapperElm.LEFT_PARENTHESIS);
			foreach.setClose(MapperElm.RIGHT_PARENTHESIS);
			foreach.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.MAPPER_ANCHOR_ID + MapperElm.CDATA_ANCHOR_END);
			delete.getForeach().add(foreach);

			// Register a DAO method.
			String comments = deleteByPKSpec.getDesc();
			List<String> paramTypeList = new ArrayList<String>();
			paramTypeList.add(JavaSrcElm.UTIL_LIST_FULL + JavaSrcElm.LESS_THAN + JavaSrcElm.LANG_LONG_FULL + JavaSrcElm.LARGER_THAN);
			comments += JavaSrcElm.PK_LIST;
			CtxCacheFacade.addDaoMethod(artifactSpec, daoSpec.getShortName(), deleteId, paramTypeList, JavaSrcElm.INT, comments);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}
	}

	private void buildDeleteBySql(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, DeleteSpec deleteBySqlSpec) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// Initiate a blank Delete.
			Delete delete = mapperObjFactory.createDelete();
			mapper.getDelete().add(delete);

			// Populate the 'id' attribute for 'delete'.
			String deleteId = MapperFormatter.getDeleteIdBySql(deleteBySqlSpec);
			delete.setId(deleteId);
			// Populate the 'paramType' attribute for 'delete'.
			String tableName = deleteBySqlSpec.getTableName();
			String paramFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
			delete.setParameterType(paramFullName);

			// Find TableSpec corresponding to current Delete, if no Table found, then return Delete without elements.
			TableSpec tableSpec = CtxCacheFacade.lookupTableSpec(tableName);
			if (tableSpec == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + tableName);
				return;
			}

			// Compute 'id' attribute for 'sql'.
			String sqlId = MapperFormatter.getSqlIdOfDel(deleteBySqlSpec);

			// Lookup 'sql' element with 'sqlId', if not found, then create one.
			Sql sql = CtxCacheFacade.lookupSql(workspaceSpec, daoSpec, sqlId);
			if (sql == null) {
				buildDeleteClause(mapper, artifactSpec, daoSpec, deleteBySqlSpec, tableSpec, false, false, sqlId);
			}
			// Refer to common sql clause in 'select'.
			Include include = mapperObjFactory.createInclude();
			include.setRefid(sqlId);
			delete.getInclude().add(include);

			Trim whereTrim = mapperObjFactory.createTrim();
			delete.getTrim().add(whereTrim);
			whereTrim.setPrefix(MapperElm.SQL_WHERE_SIMPLE);
			whereTrim.setPrefixOverrides(MapperElm.SQL_AND_OR);
			whereTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.ANCHOR_MAPPERSQL_WHERE_CLAUSE + MapperElm.CDATA_ANCHOR_END);

			// Register a DAO method.
			String comments = deleteBySqlSpec.getDesc();
			List<String> paramTypeNameList = new ArrayList<String>();
			String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
			paramTypeNameList.add(dtoXFullName);
			//			paramTypeNameList.add(workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.SQL_CLAUSE_FULL);
			CtxCacheFacade.addDaoMethod(artifactSpec, daoSpec.getShortName(), deleteId, paramTypeNameList, JavaSrcElm.INT, comments);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void buildDeleteClause(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, DeleteSpec deleteSpec, TableSpec tableSpec,
			boolean isBySql, boolean hasTmLck, String sqlId) throws AppException {

		// Initiate a blank 'sql'.
		Sql sql = mapperObjFactory.createSql();
		mapper.getSql().add(sql);
		sql.setId(sqlId);

		// Populate delete clause.
		String tableName = deleteSpec.getTableName();
		StringBuffer clause = new StringBuffer();
		clause.append(MapperElm.SQL_DELETE).append(MapperElm.SQL_FROM_FULL).append(tableName).append(MapperElm.WHITE_SPACE);
		sql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + clause.toString() + MapperElm.CDATA_ANCHOR_END);

		// Register the new 'sql' element.
		CtxCacheFacade.addSql(artifactSpec.getCommonAttrSpec().getWorkspaceSpec(), daoSpec, sqlId, sql);

	}

	private void computePKClause(StringBuffer primaryKeyClause, TableSpec tableSpec, boolean hasTmLck) throws AppException {

		try {

			// Prepare to populate primary key clause.
			ColumnSpec columnSpec = null;
			String columnName = null;
			String attrName = null;

			// Populate sql according to the the TableSpec definition.
			List<ColumnSpec> columnSpecList = tableSpec.getColumnList();
			for (int i = 0; i < columnSpecList.size(); i++) {

				// Get Java style according to columnName.
				columnSpec = columnSpecList.get(i);
				columnName = columnSpec.getName();
				attrName = JavaFormatter.getJavaStyle(columnName, false, false);

				if (columnSpec.isPrimaryKey() == true) {

					// Record the matched column.
					// TableSpecFacade.addMatchedColumn(tableSpec, column);

					if (primaryKeyClause.length() == 0) {
						MapperFormatter.beginWhereStatement(primaryKeyClause);
					} else {
						primaryKeyClause.append(MapperElm.SQL_AND_FULL);
					}

					primaryKeyClause.append(columnName);
					primaryKeyClause.append(MapperElm.EQUAL);
					primaryKeyClause.append(MapperElm.POUND);
					primaryKeyClause.append(MapperElm.LEFT_BRACKET);

					// if (hasTmLck == true) {
					// primaryKeyClause.append(JavaSrcElm.PARAM_MAP_OLD_VAL +
					// JavaSrcElm.DOT + attrName);
					// } else {
					// primaryKeyClause.append(attrName);
					// }
					primaryKeyClause.append(attrName);

					primaryKeyClause.append(MapperElm.RIGHT_BRACKET);
					// primaryKeyClause.append(columnSpec.getJdbcTypeName());
					// primaryKeyClause.append(MapperElm.POUND);

					logger.debug("FIND MAPPING: Find primary mapping between property '" + attrName + "' and column '" + columnName + "'");

				}

			}

			if (hasTmLck == true) {
				primaryKeyClause.append(MapperElm.DELETE_TM_LCK_CLAUSE);
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void computeWhereInClause(StringBuffer beginWhereInClause, TableSpec tableSpec) throws AppException {
		try {

			// Prepare to populate primary key clause.
			ColumnSpec columnSpec = null;
			String columnName = null;

			// Populate sql according to the the TableSpec definition.
			List<ColumnSpec> columnSpecList = tableSpec.getColumnList();
			for (int i = 0; i < columnSpecList.size(); i++) {

				columnSpec = columnSpecList.get(i);
				columnName = columnSpec.getName();

				if (columnSpec.isPrimaryKey() == true) {

					// Record the matched column.
					// TableSpecFacade.addMatchedColumn(tableSpec, column);

					if (beginWhereInClause.length() == 0) {
						MapperFormatter.beginWhereStatement(beginWhereInClause);
					} else {
						beginWhereInClause.append(MapperElm.SQL_AND_FULL);
					}

					beginWhereInClause.append(columnName);
					beginWhereInClause.append(MapperElm.SQL_IN);
				}
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

}
