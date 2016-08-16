package bh.toolkit.srcgen.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import bh.toolkit.srcgen.connector.rdb.DBDialectFactory;
import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.DaoSpec;
import bh.toolkit.srcgen.model.artifact.InsertSpec;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;
import bh.toolkit.srcgen.model.mybatis.Foreach;
import bh.toolkit.srcgen.model.mybatis.If;
import bh.toolkit.srcgen.model.mybatis.Include;
import bh.toolkit.srcgen.model.mybatis.Insert;
import bh.toolkit.srcgen.model.mybatis.Mapper;
import bh.toolkit.srcgen.model.mybatis.ObjectFactory;
import bh.toolkit.srcgen.model.mybatis.SelectKey;
import bh.toolkit.srcgen.model.mybatis.Sql;
import bh.toolkit.srcgen.model.mybatis.Trim;
import bh.toolkit.srcgen.model.rdb.ColumnSpec;
import bh.toolkit.srcgen.model.rdb.TableSpec;
import bh.toolkit.srcgen.registry.CtxCacheFacade;
import bh.toolkit.srcgen.util.ExceptionUtil;
import bh.toolkit.srcgen.util.JavaFormatter;
import bh.toolkit.srcgen.util.MapperFormatter;

public class InsertBuilder {

	private static Logger logger = Logger.getLogger(InsertBuilder.class);

	private ObjectFactory mapperObjFactory;
	private String dbIdentityValue;

	public InsertBuilder(ObjectFactory mapperObjFactory) {
		this.mapperObjFactory = mapperObjFactory;
	}

	public void buildAllInsert(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec) throws AppException {

		try {

			DBDialectFactory dbDialectFactory = DBDialectFactory.getInstance(artifactSpec.getCommonAttrSpec().getDatasourceSpec());
			this.dbIdentityValue = dbDialectFactory.getDBIdentityValueFunction();

			// List<DaoSpec> daoSpecList = artifactSpec.getDaoSpec();
			// 通过构建配置对象，模拟Ant任务中的配置信息
			List<InsertSpec> insertSpecList = daoSpec.getInsertSpec();
			simulateAntConfig(daoSpec, insertSpecList);

			// Handle every single "insertSpec" element.
			for (InsertSpec insertSpec : insertSpecList) {

				logger.debug("INSERT: Handle \"insertSpec\" for table \"" + insertSpec.getTableName() + ".\"");

				// Map DB table and column into VO class.
				CtxCacheFacade.addVoClass(artifactSpec.getCommonAttrSpec().getWorkspaceSpec(), insertSpec.getTableName());

				// Insert all columns.
				buildInsertAll(mapper, artifactSpec, daoSpec, insertSpec);

				// Insert selective columns.
				buildInsertSel(mapper, artifactSpec, daoSpec, insertSpec);

				// TODO Batch insert all columns.
				buildInsertAllInBatch(mapper, artifactSpec, daoSpec, insertSpec, false);

				// TODO Batch insert all without pk columns
				buildInsertAllInBatch(mapper, artifactSpec, daoSpec, insertSpec, true);

			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void simulateAntConfig(DaoSpec daoSpec, List<InsertSpec> insertSpecList) {

		bh.toolkit.srcgen.model.artifact.ObjectFactory artifactObjFactory = new bh.toolkit.srcgen.model.artifact.ObjectFactory();
		InsertSpec seudoInsertSpec = artifactObjFactory.createInsertSpec();
		seudoInsertSpec.setTableName(daoSpec.getShortName());
		seudoInsertSpec.setDesc(daoSpec.getDesc());
		seudoInsertSpec.setEnableSelectKey("true");
		insertSpecList.add(seudoInsertSpec);

	}

	private void buildInsertAll(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, InsertSpec insertSpec) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// Initiate a blank Insert.
			Insert insert = mapperObjFactory.createInsert();
			mapper.getInsert().add(insert);

			// Populate attributes inside Insert.
			String insertId = MapperFormatter.getInsertId(insertSpec, true, false);
			insert.setId(insertId);
			String tableName = insertSpec.getTableName();
			String paramFullName = JavaFormatter.getVoFullName(workspaceSpec, tableName);
			insert.setParameterType(paramFullName);

			// Find Table corresponding to current Insert, if no Table found,
			// then return Insert without elements.
			TableSpec tableSpec = CtxCacheFacade.lookupTableSpec(tableName);
			if (tableSpec == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + tableName);
				return;
			}

			// Refer to common sql clause in 'insert'.
			Include include = mapperObjFactory.createInclude();
			insert.getInclude().add(include);
			String sqlId = MapperFormatter.getSqlIdOfInsert(insertSpec, true, false);
			include.setRefid(sqlId);

			// Lookup 'sql' element with 'sqlId', if not found, then create a
			// new 'sql' and register it.
			Sql sql = CtxCacheFacade.lookupSql(workspaceSpec, daoSpec, sqlId);
			if (sql == null) {
				sql = mapperObjFactory.createSql();
				sql.setId(sqlId);
				mapper.getSql().add(sql);
				populateInsertAllSql(insert, sql, insertSpec, tableSpec);
				CtxCacheFacade.addSql(workspaceSpec, daoSpec, sqlId, sql);
			}

			// Register a DAO method.
			List<String> paramTypeNameList = new ArrayList<String>();
			paramTypeNameList.add(paramFullName);
			String comments = insertSpec.getDesc() + JavaSrcElm.IMPACT_COL_ALL;
			CtxCacheFacade.addDaoMethod(artifactSpec, daoSpec.getShortName(), insertId, paramTypeNameList, JavaSrcElm.INT, comments);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void populateInsertAllSql(Insert insert, Sql sql, InsertSpec insertAllConfig, TableSpec tableSpec) throws AppException {

		try {

			String tableName = insertAllConfig.getTableName();
			ColumnSpec ormColumn = null;
			String columnName = null;
			String dbDataTypeName = null;
			String attrName = null;
			StringBuffer columnClause = new StringBuffer();
			StringBuffer valueClause = new StringBuffer();
			String enableSelectKey = insertAllConfig.getEnableSelectKey();

			// Populate sql according to the the OrmTable definition.
			List<ColumnSpec> ormColumnList = tableSpec.getColumnList();
			for (int i = 0; i < ormColumnList.size(); i++) {

				// Get Java style according to columnName.
				ormColumn = ormColumnList.get(i);
				columnName = ormColumn.getName();
				attrName = JavaFormatter.getJavaStyle(columnName, false, false);

				// In case the column is primary key.
				if (StringUtils.isNotBlank(enableSelectKey) == true && enableSelectKey.equalsIgnoreCase(JavaSrcElm.TRUE) == true) {
					if (ormColumn.isPrimaryKey() == true) {
						// Add selectKey element.
						SelectKey selectKey = mapperObjFactory.createSelectKey();
						insert.getSelectKey().add(selectKey);
						selectKey.setKeyProperty(attrName);
						dbDataTypeName = ormColumn.getJdbcTypeName();
						selectKey.setResultType(CtxCacheFacade.lookupJavaDataType(dbDataTypeName));
						selectKey.setOrder(MapperElm.AFTER);

						selectKey.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + dbIdentityValue + MapperElm.CDATA_ANCHOR_END);
						// Skip primary key to accommodate the IDENTITY column
						// in DB2.
						// continue;
					}
				}

				// Process the first column.
				if (columnClause.length() == 0) {
					columnClause.append(MapperElm.SQL_INSERT_INTO + MapperElm.WHITE_SPACE + tableName);
					MapperFormatter.beginWithNewLine(columnClause, MapperElm.WHITE_SPACE + MapperElm.LEFT_PARENTHESIS);
					MapperFormatter.beginValuesStatement(valueClause);
				} else {
					columnClause.append(MapperElm.COMMA + MapperElm.WHITE_SPACE);
					valueClause.append(MapperElm.COMMA + MapperElm.WHITE_SPACE);
				}

				// Populate column clause and value clause.
				//				columnClause.append(keyWordsTransformer.getTransferredStr(columnName));
				columnClause.append(columnName);
				valueClause.append(MapperElm.POUND + MapperElm.LEFT_BRACKET + attrName + MapperElm.RIGHT_BRACKET);

				// Add new line symbol if necessary.
				MapperFormatter.checkWidth(columnClause);
				MapperFormatter.checkWidth(valueClause);

			}

			// Add right parenthesis.
			columnClause.append(MapperElm.RIGHT_PARENTHESIS);
			valueClause.append(MapperElm.RIGHT_PARENTHESIS);

			sql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + columnClause.toString() + valueClause.toString() + MapperElm.CDATA_ANCHOR_END);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void buildInsertSel(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, InsertSpec insertSpec) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// Initiate a blank Insert.
			Insert insert = mapperObjFactory.createInsert();
			mapper.getInsert().add(insert);

			// Populate attributes inside Insert.
			String insertId = MapperFormatter.getInsertId(insertSpec, false, false);
			insert.setId(insertId);
			String tableName = insertSpec.getTableName();
			String paramFullName = JavaFormatter.getVoFullName(workspaceSpec, tableName);
			insert.setParameterType(paramFullName);

			// Find Table corresponding to current InsertSelective, if no Table
			// found, then return InsertSelective without elements.
			TableSpec tableSpec = CtxCacheFacade.lookupTableSpec(tableName);
			if (tableSpec == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + tableName);
				return;
			}

			// Compute 'id' attribute for 'sql'.
			String columnSqlId = MapperFormatter.getSqlIdOfInsertSelCol(insertSpec);
			String valueSqlId = MapperFormatter.getSqlIdOfInsertSelVal(insertSpec);

			// Refer to common sql clause in 'insert'.
			Include columnInclude = mapperObjFactory.createInclude();
			columnInclude.setRefid(columnSqlId);
			insert.getInclude().add(columnInclude);
			Include valueInclude = mapperObjFactory.createInclude();
			valueInclude.setRefid(valueSqlId);
			insert.getInclude().add(valueInclude);

			// Lookup 'sql' element with 'sqlId'. If not found, then create new
			// 'columnSql' and 'valueSql', then register 'columnSql'.
			Sql columnSql = CtxCacheFacade.lookupSql(workspaceSpec, daoSpec, columnSqlId);
			if (columnSql == null) {
				columnSql = mapperObjFactory.createSql();
				mapper.getSql().add(columnSql);
				columnSql.setId(columnSqlId);
				Sql valueSql = mapperObjFactory.createSql();
				mapper.getSql().add(valueSql);
				valueSql.setId(valueSqlId);
				populateInsertSelSql(insert, columnSql, valueSql, insertSpec, tableSpec);

				CtxCacheFacade.addSql(workspaceSpec, daoSpec, columnSqlId, columnSql);
			}

			// Register a DAO method.
			List<String> paramTypeList = new ArrayList<String>();
			paramTypeList.add(paramFullName);
			String comments = insertSpec.getDesc() + JavaSrcElm.IMPACT_COL_SEL;
			CtxCacheFacade.addDaoMethod(artifactSpec, daoSpec.getShortName(), insertId, paramTypeList, JavaSrcElm.INT, comments);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void populateInsertSelSql(Insert insert, Sql columnSql, Sql valueSql, InsertSpec insertSelConfig, TableSpec tableSpec)
			throws AppException {

		try {

			String tableName = insertSelConfig.getTableName();

			// Create two 'trim' elements and set them into 'columnSql' and
			// 'valueSql' respectively.
			Trim colTrim = mapperObjFactory.createTrim();
			columnSql.getTrim().add(colTrim);
			Trim valTrim = mapperObjFactory.createTrim();
			valueSql.getTrim().add(valTrim);

			// Populate 'prefix' and 'suffix' attributes of 'trim'.
			colTrim.setPrefix(MapperElm.SQL_INSERT_INTO + MapperElm.WHITE_SPACE + tableName + MapperElm.WHITE_SPACE + MapperElm.LEFT_PARENTHESIS);
			colTrim.setPrefixOverrides(MapperElm.COMMA + MapperElm.WHITE_SPACE);
			colTrim.setSuffix(MapperElm.RIGHT_PARENTHESIS);
			valTrim.setPrefix(MapperElm.SQL_VALUES_FULL + MapperElm.LEFT_PARENTHESIS);
			valTrim.setPrefixOverrides(MapperElm.COMMA + MapperElm.WHITE_SPACE);
			valTrim.setSuffix(MapperElm.RIGHT_PARENTHESIS);

			// Get all 'OrmColumn' inside 'OrmTable'.
			List<ColumnSpec> ormColumnList = tableSpec.getColumnList();
			List<If> colIfList = colTrim.getIf();
			List<If> valIfList = valTrim.getIf();

			// Prepare to populate 'if' elements inside 'trim'.
			ColumnSpec ormColumn = null;
			String columnName = null;
			String dbDataTypeName = null;
			String attrName = null;
			String javaNullExpr = null;
			If columnIf = null;
			If valueIf = null;
			String enableSelectKey = insertSelConfig.getEnableSelectKey();

			// Populate 'if' elements according to the the 'OrmColumn' definition.
			for (int i = 0; i < ormColumnList.size(); i++) {

				// Translate column name to java attribute name.
				ormColumn = ormColumnList.get(i);
				columnName = ormColumn.getName();
				attrName = JavaFormatter.getJavaStyle(columnName, false, false);
				javaNullExpr = JavaFormatter.getJavaNullExpr(ormColumn);

				// In case the column is primary key.
				if (StringUtils.isNotBlank(enableSelectKey) == true && enableSelectKey.equalsIgnoreCase(JavaSrcElm.TRUE) == true) {
					if (ormColumn.isPrimaryKey() == true) {
						// Add selectKey element.
						SelectKey selectKey = mapperObjFactory.createSelectKey();
						insert.getSelectKey().add(selectKey);
						selectKey.setKeyProperty(attrName);
						dbDataTypeName = ormColumn.getJdbcTypeName();
						selectKey.setResultType(CtxCacheFacade.lookupJavaDataType(dbDataTypeName));
						selectKey.setOrder(MapperElm.AFTER);
						selectKey.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + dbIdentityValue + MapperElm.CDATA_ANCHOR_END);
						// Skip primary key to accommodate the IDENTITY column in DB2.
						// continue;
					}
				}

				columnIf = mapperObjFactory.createIf();
				columnIf.setTest(attrName + javaNullExpr);
				//				columnIf.setCDataBegin(MapperElm.COMMA + MapperElm.WHITE_SPACE + keyWordsTransformer.getTransferredStr(columnName));
				columnIf.setCDataBegin(MapperElm.COMMA + MapperElm.WHITE_SPACE + columnName);
				colIfList.add(columnIf);
				valueIf = mapperObjFactory.createIf();
				valueIf.setTest(attrName + javaNullExpr);
				valueIf.setCDataBegin(
						MapperElm.COMMA + MapperElm.WHITE_SPACE + MapperElm.POUND + MapperElm.LEFT_BRACKET + attrName + MapperElm.RIGHT_BRACKET);
				valIfList.add(valueIf);

			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void buildInsertAllInBatch(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, InsertSpec insertSpec, boolean withoutPk)
			throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// Initiate a blank Insert.
			Insert insert = mapperObjFactory.createInsert();
			mapper.getInsert().add(insert);

			// Populate attributes inside Insert.
			String insertId = MapperFormatter.getInsertId(insertSpec, true, true);
			if (withoutPk) {
				insertId += MapperElm.MAPPER_WITHOUT_PK;
			} else {
				insertId += MapperElm.MAPPER_WITH_PK;
			}

			insert.setId(insertId);
			insert.setParameterType(java.util.List.class.getName());

			// Find Table corresponding to current Insert, if no Table found, then return Insert without elements.
			String tableName = insertSpec.getTableName();
			TableSpec tableSpec = CtxCacheFacade.lookupTableSpec(tableName);
			if (tableSpec == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + tableName);
				return;
			}

			// include the columnSql
			String columnSqlId = MapperFormatter.getSqlIdOfInsertAllBatchCol(insertSpec, withoutPk);
			Include columnInclude = mapperObjFactory.createInclude();
			columnInclude.setRefid(columnSqlId);
			insert.getInclude().add(columnInclude);
			insert.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.SQL_VALUES_SIMPLE + MapperElm.CDATA_ANCHOR_END);

			// Add the foreach
			String voFullName = JavaFormatter.getVoFullName(workspaceSpec, tableName);
			String voSimpleName = JavaFormatter.getClassSimpleName(voFullName);
			Foreach foreach = mapperObjFactory.createForeach();
			foreach.setCollection(MapperElm.FOR_EACH_COLLECTION_LIST);
			foreach.setItem(voSimpleName);
			foreach.setIndex(MapperElm.FOR_EACH_INDEX);
			foreach.setSeparator(MapperElm.COMMA);

			// Include the column value
			String valueSqlId = MapperFormatter.getSqlIdOfInsertAllBatchVal(insertSpec, withoutPk);
			Include valueInclude = mapperObjFactory.createInclude();
			valueInclude.setRefid(valueSqlId);
			foreach.getInclude().add(valueInclude);

			insert.getForeach().add(foreach);

			// Lookup 'sql' element with 'sqlId'. If not found, then create new
			// 'columnSql' and 'valueSql', then register 'columnSql'.
			Sql columnSql = CtxCacheFacade.lookupSql(workspaceSpec, daoSpec, columnSqlId);
			if (columnSql == null) {
				columnSql = mapperObjFactory.createSql();
				mapper.getSql().add(columnSql);
				columnSql.setId(columnSqlId);
				Sql valueSql = mapperObjFactory.createSql();
				mapper.getSql().add(valueSql);
				valueSql.setId(valueSqlId);
				populateInsertAllBatchSql(insert, columnSql, valueSql, insertSpec, tableSpec, withoutPk);

				CtxCacheFacade.addSql(workspaceSpec, daoSpec, columnSqlId, columnSql);
			}

			// Register a DAO method.
			// String classFullName = insertSpec.getParamName();
			// String classSimpleName = JavaFormatter.getClassSimpleName(classFullName);
			List<String> paramTypeList = new ArrayList<String>();
			paramTypeList.add(JavaSrcElm.UTIL_LIST_FULL + JavaSrcElm.LESS_THAN + voFullName + JavaSrcElm.LARGER_THAN);
			String comments = insertSpec.getDesc() + JavaSrcElm.IMPACT_COL_ALL + JavaSrcElm.IN_BATCH;
			if (withoutPk) {
				comments += JavaSrcElm.WITHOUT_PK;
			} else {
				comments += JavaSrcElm.WITH_PK;
			}
			comments += JavaSrcElm.VO_SIZE_LIMITED;

			CtxCacheFacade.addDaoMethod(artifactSpec, daoSpec.getShortName(), insertId, paramTypeList, JavaSrcElm.INT, comments);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void populateInsertAllBatchSql(Insert insert, Sql columnSql, Sql valueSql, InsertSpec insertAllBatchConfig, TableSpec tableSpec,
			boolean withoutPk) throws AppException {

		try {

			String tableName = insertAllBatchConfig.getTableName();
			String voSimpleName = JavaFormatter.getJavaStyle(tableName, false, false) + JavaSrcElm.VO_NAME_SUFFIX;
			ColumnSpec ormColumn = null;
			String columnName = null;
			// String dbDataTypeName = null;
			String attrName = null;
			StringBuffer columnClause = new StringBuffer();
			StringBuffer valueClause = new StringBuffer();
			// String enableSelectKey =
			// insertAllBatchConfig.getEnableSelectKey();

			// Populate sql according to the the OrmTable definition.
			List<ColumnSpec> ormColumnList = tableSpec.getColumnList();
			for (int i = 0; i < ormColumnList.size(); i++) {

				// Get Java style according to columnName.
				ormColumn = ormColumnList.get(i);
				columnName = ormColumn.getName();
				attrName = JavaFormatter.getJavaStyle(columnName, false, false);

				if (ormColumn.isPrimaryKey() && withoutPk)
					continue;

				// Process the first column.
				if (columnClause.length() == 0) {
					columnClause.append(MapperElm.SQL_INSERT_INTO + MapperElm.WHITE_SPACE + tableName);
					MapperFormatter.beginWithNewLine(columnClause, MapperElm.WHITE_SPACE + MapperElm.LEFT_PARENTHESIS);
					valueClause.append(MapperElm.WHITE_SPACE + MapperElm.LEFT_PARENTHESIS);
				} else {
					columnClause.append(MapperElm.COMMA + MapperElm.WHITE_SPACE);
					valueClause.append(MapperElm.COMMA + MapperElm.WHITE_SPACE);
				}

				// Populate column clause and value clause.
				//				columnClause.append(keyWordsTransformer.getTransferredStr(columnName));
				columnClause.append(columnName);
				valueClause.append(MapperElm.POUND + MapperElm.LEFT_BRACKET + voSimpleName + MapperElm.DOT + attrName + MapperElm.RIGHT_BRACKET);

				// Add new line symbol if necessary.
				MapperFormatter.checkWidth(columnClause);
				MapperFormatter.checkWidth(valueClause);

			}

			// Add right parenthesis.
			columnClause.append(MapperElm.RIGHT_PARENTHESIS);
			valueClause.append(MapperElm.RIGHT_PARENTHESIS);

			columnSql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + columnClause.toString() + MapperElm.CDATA_ANCHOR_END);
			valueSql.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + valueClause.toString() + MapperElm.CDATA_ANCHOR_END);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

}
