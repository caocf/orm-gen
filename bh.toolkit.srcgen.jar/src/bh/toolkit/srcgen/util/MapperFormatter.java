package bh.toolkit.srcgen.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.CommonAttrSpec;
import bh.toolkit.srcgen.model.artifact.DaoSpec;
import bh.toolkit.srcgen.model.artifact.DatasourceSpec;
import bh.toolkit.srcgen.model.artifact.DeleteSpec;
import bh.toolkit.srcgen.model.artifact.InsertSpec;
import bh.toolkit.srcgen.model.artifact.RelationshipSpec;
import bh.toolkit.srcgen.model.artifact.SelectSpec;
import bh.toolkit.srcgen.model.artifact.UpdateSpec;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;
import bh.toolkit.srcgen.registry.OneToOneIdx;
import bh.toolkit.srcgen.registry.OneToOneIdxFacade;

public class MapperFormatter {

	private static Logger logger = Logger.getLogger(MapperFormatter.class);

	public static void checkWidth(StringBuffer line) {
		StringHelper.newLine(line, JavaSrcElm.UNIT_OF_INDENT, 3, MapperElm.MAPPER_MAX_WIDTH);
	}

	public static void checkWidth(StringBuffer line, String newLine, int countOfIndents) {

		if (line.length() + newLine.length() > MapperElm.MAPPER_MAX_WIDTH) {
			StringHelper.newLine(line, JavaSrcElm.UNIT_OF_INDENT, countOfIndents);
		}

		line.append(newLine);
	}

	public static void beginWithNewLine(StringBuffer line, String newLine) {
		StringHelper.newLine(line, JavaSrcElm.UNIT_OF_INDENT, 3);
		line.append(newLine);
	}

	public static void beginSetStatement(StringBuffer statement) {
		statement.append(MapperElm.SQL_SET);
		StringHelper.newLine(statement, JavaSrcElm.UNIT_OF_INDENT, 3);
		statement.append(MapperElm.WHITE_SPACE);
		statement.append(MapperElm.WHITE_SPACE);
	}

	public static void beginValuesStatement(StringBuffer statement) {
		StringHelper.newLine(statement, JavaSrcElm.UNIT_OF_INDENT, 3);
		statement.append(MapperElm.SQL_VALUES_FULL);
		StringHelper.newLine(statement, JavaSrcElm.UNIT_OF_INDENT, 3);
		statement.append(MapperElm.WHITE_SPACE + MapperElm.LEFT_PARENTHESIS);
	}

	public static void beginFromStatement(StringBuffer statement) {
		StringHelper.newLine(statement, JavaSrcElm.UNIT_OF_INDENT, 3);
		statement.append(MapperElm.SQL_FROM_WHITE_SPACE);
	}

	public static void beginWhereStatement(StringBuffer statement) {
		StringHelper.newLine(statement, JavaSrcElm.UNIT_OF_INDENT, 3);
		statement.append(MapperElm.SQL_WHERE_SIMPLE + MapperElm.WHITE_SPACE);
	}

	public static void insertWhereStatement(StringBuffer statement) {
		StringBuffer prefix = new StringBuffer();
		StringHelper.newLine(prefix, JavaSrcElm.UNIT_OF_INDENT, 3);
		prefix.append(MapperElm.SQL_WHERE_SIMPLE + MapperElm.WHITE_SPACE);
		statement.insert(0, prefix.toString());
	}

	public static void beginAndStatement(StringBuffer statement) {
		StringHelper.newLine(statement, JavaSrcElm.UNIT_OF_INDENT, 3);
		statement.append(MapperElm.SQL_AND_SIMPLE + MapperElm.WHITE_SPACE);
	}

	public static void insertAndStatement(StringBuffer statement) {
		StringBuffer prefix = new StringBuffer();
		StringHelper.newLine(prefix, JavaSrcElm.UNIT_OF_INDENT, 3);
		prefix.append(MapperElm.SQL_AND_SIMPLE + MapperElm.WHITE_SPACE);
		statement.insert(0, prefix.toString());
	}

	public static String getGlobalId(WorkspaceSpec workspaceSpec, DaoSpec daoSpec, String shortId) {
		// Get global id for Mapper element.
		String globalId = JavaFormatter.getDaoFullName(workspaceSpec, daoSpec.getShortName()) + MapperElm.DOT + shortId;
		return globalId;
	}

	public static String getDefaultSchema(ArtifactSpec artifactSpec) {

		CommonAttrSpec commonAttrSpec = artifactSpec.getCommonAttrSpec();
		DatasourceSpec datasourceSpec = commonAttrSpec.getDatasourceSpec();
		String allSchema = datasourceSpec.getSchema();

		if (allSchema.indexOf(MapperElm.COMMA) != -1) {
			String[] schemaArray = StringUtils.split(allSchema, MapperElm.COMMA);
			if (schemaArray.length > 0) {
				return schemaArray[0];
			} else {
				return allSchema;
			}
		} else {
			return allSchema;
		}

	}

	public static String getMapperFullName(WorkspaceSpec workspaceSpec, DaoSpec daoSpec) {
		String mapperPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getMapperPkgName();
		String schemaName = getTableSchema(daoSpec.getShortName());
		String mapperSimpleName = JavaSrcElm.INTF_PREFIX + JavaFormatter.getJavaStyle(daoSpec.getShortName(), false, true) + JavaSrcElm.DAO_INTF_SUFFIX; // Mapper的短名与DaoSpec的短名相同
		return mapperPkgPrefix + JavaSrcElm.BASE_PKG_PAT + StringUtils.lowerCase(schemaName) + JavaSrcElm.DOT + mapperSimpleName;
	}

	public static String getMapperAbsoluteName(WorkspaceSpec workspaceSpec, DaoSpec daoSpec) throws AppException {

		// Change package name to file name.
		String mapperFullName = getMapperFullName(workspaceSpec, daoSpec);
		String mapperFileName = JavaFormatter.classNameToFileName(mapperFullName);

		// Assemble the complete file name for Sql Map XML file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getOrmPrj()) + mapperFileName + MapperElm.MAPPER_FILENAME_EXT;

		return absoluteName;

	}

	public static String getMapperxFullName(WorkspaceSpec workspaceSpec, DaoSpec daoSpec) {
		String mapperxPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getMapperPkgName();
		String schemaName = getTableSchema(daoSpec.getShortName());
		String mapperxSimpleName = JavaSrcElm.INTF_PREFIX + JavaFormatter.getJavaStyle(daoSpec.getShortName(), false, true) + JavaSrcElm.DAOX_INTF_SUFFIX; // Mapper的短名与DaoSpec的短名相同
		return mapperxPkgPrefix + JavaSrcElm.EXT_PKG_PAT + StringUtils.lowerCase(schemaName) + JavaSrcElm.DOT + mapperxSimpleName;
	}

	public static String getMapperxAbsoluteName(WorkspaceSpec workspaceSpec, DaoSpec daoSpec) throws AppException {

		// Change package name to file name.
		String mapperxFullName = getMapperxFullName(workspaceSpec, daoSpec);
		String mapperxFileName = JavaFormatter.classNameToFileName(mapperxFullName);

		// Assemble the complete file name for Sql Map XML file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getOrmPrj()) + mapperxFileName + MapperElm.MAPPER_FILENAME_EXT;

		return absoluteName;

	}

	/**
	 * 返回包含Schema的，以下划线分隔的Table Alias
	 * 
	 * @param artifactSpec
	 * @param tableName
	 * @return
	 */
	public static String getTableFullAlias(ArtifactSpec artifactSpec, String tableName) {
		String tableFullName = getTableFullName(artifactSpec, tableName);
		return StringHelper.replace(tableFullName, MapperElm.DOT, MapperElm.DOUBLE_UNDER_LINE);
	}

	/**
	 * 返回带有Schema的Table Name
	 * 
	 * @param artifactSpec
	 * @param tableName
	 * @return
	 */
	public static String getTableFullName(ArtifactSpec artifactSpec, String tableName) {

		// If tableName contain schema name, then use it.
		String schema = null;
		int indexOfDot = tableName.indexOf(MapperElm.DOT);
		if (indexOfDot != -1) {
			return tableName;
		} else {
			schema = getDefaultSchema(artifactSpec);
			return schema + MapperElm.DOT + tableName;
		}

	}

	public static String getTableSchema(String tableName) {

		// If tableName contain schema name, then remove it.
		int indexOfDot = tableName.indexOf(MapperElm.DOT);
		if (indexOfDot == -1) {
			return "";
		} else {
			return tableName.substring(0, indexOfDot);
		}

	}

	/**
	 * 返回不包含Schema的Table Name
	 * 
	 * @param tableName
	 * @return
	 */
	public static String getTableShortName(String tableName, String tableAlias) {

		// If tableAlias is not null and is not blank, then use it.
		if (StringUtils.isNotBlank(tableAlias) == true) {
			return tableAlias;
		}

		// If tableName contain schema name, then remove it.
		int indexOfDot = tableName.indexOf(MapperElm.DOT);
		if (indexOfDot == -1) {
			return tableName;
		} else {
			return tableName.substring(indexOfDot + 1, tableName.length());
		}

	}

	/**
	 * 获取包含表名和表别名在内的名称对 如果Table Alias非空，则选用；如果Table Alias空，则采用Table Name。
	 * 
	 * @param artifactSpec
	 * @param tableName
	 * @param tableAlias
	 * @return
	 */
	public static String getTableNamePair(ArtifactSpec artifactSpec, String tableName, String tableAlias) {

		String tableFullName = getTableFullName(artifactSpec, tableName);
		if (StringUtils.isNotBlank(tableAlias) == true) {
			return tableFullName + MapperElm.WHITE_SPACE + tableAlias;
		}

		String tableFullAlias = getTableFullAlias(artifactSpec, tableName);
		return tableFullName + MapperElm.WHITE_SPACE + tableFullAlias;

	}

	public static String getTableNamePair(ArtifactSpec artifactSpec, OneToOneIdx oneToOneIndex) {

		RelationshipSpec oneToOne = oneToOneIndex.getOneToOne();
		String joinType = oneToOne.getJoinType();

		String leftTableName = OneToOneIdxFacade.getRelLeftTableName(oneToOneIndex);
		String leftTableAlias = OneToOneIdxFacade.getRelLeftTableAlias(oneToOneIndex);
		String leftAttrName = OneToOneIdxFacade.getRelLeftAttrName(oneToOneIndex);
		String leftColumnName = JavaFormatter.getDBStyle(leftAttrName);

		String rightTableName = oneToOneIndex.getRightTableName();
		String rightTableAlias = oneToOneIndex.getRightTableAlias();
		String rightAttrName = oneToOne.getSonAttr();
		String rightColumnName = JavaFormatter.getDBStyle(rightAttrName);

		// Change the table name into full name containing schema.
		String leftTableFullName = getTableFullName(artifactSpec, leftTableName);
		String leftTableFullAlias = getTableFullAlias(artifactSpec, leftTableName);
		String rightTableFullName = getTableFullName(artifactSpec, rightTableName);
		String rightTableFullAlias = getTableFullAlias(artifactSpec, rightTableName);

		// Change the column name into full name containing alias.
		String leftColumnFullName = MapperFormatter.getColumnFullName(artifactSpec, leftTableName, leftTableAlias, leftColumnName);
		String rightColumnFullName = MapperFormatter.getColumnFullName(artifactSpec, rightTableName, rightTableAlias, rightColumnName);

		// Determine the join statement prefix according to the configured
		// 'joinType' in one to one association.
		String joinPrefix = null;
		if (StringUtils.isNotBlank(joinType) == true) {
			if (joinType.equalsIgnoreCase(MapperElm.SQL_JOIN_LEFT) == true) {
				// if (reverse == false) {
				joinPrefix = MapperElm.SQL_JOIN_LEFT;
				// } else {
				// joinPrefix = MapperElm.SQL_JOIN_RIGHT;
				// }
			} else if (joinType.equalsIgnoreCase(MapperElm.SQL_JOIN_RIGHT) == true) {
				// if (reverse == false) {
				joinPrefix = MapperElm.SQL_JOIN_RIGHT;
				// } else {
				// joinPrefix = MapperElm.SQL_JOIN_LEFT;
				// }
			} else if (joinType.equalsIgnoreCase(MapperElm.SQL_JOIN_FULL) == true) {
				joinPrefix = MapperElm.SQL_JOIN_FULL;
			} else {
				joinPrefix = MapperElm.SQL_JOIN_INNER;
			}
		} else {
			joinPrefix = MapperElm.SQL_JOIN_INNER;
		}

		// Append join condition into where clause.
		StringBuffer joinStatement = new StringBuffer();
		joinStatement.append(joinPrefix);
		joinStatement.append(MapperElm.SQL_JOIN);
		// if (reverse == false) {
		joinStatement.append(rightTableFullName);
		joinStatement.append(MapperElm.WHITE_SPACE);
		if (StringUtils.isNotBlank(rightTableAlias) == true) {
			joinStatement.append(rightTableAlias);
		} else {
			joinStatement.append(rightTableFullAlias);
		}
		joinStatement.append(MapperElm.SQL_JOIN_ON);
		joinStatement.append(leftColumnFullName);
		joinStatement.append(MapperElm.EQUAL);
		joinStatement.append(rightColumnFullName);
		// } else {
		// joinStatement.append(leftTableFullName);
		// joinStatement.append(MapperElm.WHITE_SPACE);
		// if (StringUtils.isNotBlank(leftTableAlias) == true) {
		// joinStatement.append(leftTableAlias);
		// } else {
		// joinStatement.append(leftTableFullAlias);
		// }
		// joinStatement.append(MapperElm.SQL_JOIN_ON);
		// joinStatement.append(rightColumnFullName);
		// joinStatement.append(MapperElm.EQUAL);
		// joinStatement.append(leftColumnFullName);
		// }

		return joinStatement.toString();

	}

	public static String getColumnFullName(ArtifactSpec artifactSpec, String tableName, String tableAlias, String columnName) {

		String colFullName = null;
		if (StringUtils.isNotBlank(tableAlias) == true) {
			colFullName = tableAlias + MapperElm.DOT + columnName;
		} else {
			// // If tableName contain schema name, then remove it.
			// int indexOfDot = tableName.indexOf(MapperElm.DOT);
			// if (indexOfDot != -1) {
			// tableName = tableName.substring(indexOfDot + 1,
			// tableName.length());
			// }
			String tableFullAlias = getTableFullAlias(artifactSpec, tableName);
			colFullName = tableFullAlias + MapperElm.DOT + columnName;
		}

		return colFullName;

	}

	public static String getColumnAlias(ArtifactSpec artifactSpec, String tableName, String tableAlias, String columnName) {

		// If tableName contain schema name, then use it.
		String schema = null;
		int indexOfDot = tableName.indexOf(MapperElm.DOT);
		if (indexOfDot != -1) {
			schema = tableName.substring(0, indexOfDot);
		} else {
			schema = getDefaultSchema(artifactSpec);
		}

		StringBuffer columnAlias = new StringBuffer();
		if (StringUtils.isNotBlank(tableAlias) == true) {
			columnAlias.append(tableAlias);
		} else {
			String tableShortName = getTableShortName(tableName, tableAlias);
			columnAlias.append(schema);
			columnAlias.append(MapperElm.DOUBLE_UNDER_LINE);
			columnAlias.append(tableShortName);
		}
		columnAlias.append(MapperElm.DOUBLE_UNDER_LINE);
		columnAlias.append(columnName);

		return columnAlias.toString();

	}

	public static String getColumnNamePair(ArtifactSpec artifactSpec, String tableName, String tableAlias, String columnName) throws AppException {

		// If tableName contain schema name, then use it.
		String tableFullAlias = getTableFullAlias(artifactSpec, tableName);

		// String schema = null;
		// String dot = MapperElm.DOT;
		// int indexOfDot = tableName.indexOf(dot);
		// if (indexOfDot != -1) {
		// schema = tableName.substring(0, indexOfDot);
		// tableName = tableName.substring(indexOfDot + 1, tableName.length());
		// } else {
		// schema = getDefaultSchema(artifactSpec);
		// }

		StringBuffer colNamePair = new StringBuffer();
		if (StringUtils.isNotBlank(tableAlias) == true) {
			colNamePair.append(tableAlias);
		} else {
			colNamePair.append(tableFullAlias);
		}
		colNamePair.append(MapperElm.DOT);
		// colNamePair.append(keyWordsTransformer.getTransferredStr(columnName));
		colNamePair.append(columnName);
		colNamePair.append(MapperElm.WHITE_SPACE);

		if (StringUtils.isNotBlank(tableAlias) == true) {
			colNamePair.append(tableAlias);
		} else {
			colNamePair.append(tableFullAlias);
		}
		// fullName.append(schema);
		// fullName.append(MapperElm.DOUBLE_UNDER_LINE);
		// if (StringUtils.isNotBlank(tableAlias) == true) {
		// fullName.append(tableAlias);
		// } else {
		// fullName.append(tableName);
		// }
		colNamePair.append(MapperElm.DOUBLE_UNDER_LINE);
		colNamePair.append(columnName);

		return colNamePair.toString();

	}

	public static String getSelectStmtWithPrefix(StringBuffer selectPrefix) {

		StringBuffer id = new StringBuffer();
		id.append(MapperElm.SQL_SELECT);
		if (StringUtils.isNotBlank(selectPrefix)) {
			id.append(MapperElm.WHITE_SPACE);
			id.append(selectPrefix);
		}
		id.append(MapperElm.WHITE_SPACE);

		logger.debug("ID: The return value of 'getSelectStmtWithPrefix' is: " + id);
		return id.toString();

	}

	public static String getResultMapIdOfRootTab(SelectSpec selectSpec) {

		String selectId = getSelectIdOfRootTable(selectSpec, false);
		String id = selectId + MapperElm.MAPPER_RESULT_MAP;

		logger.debug("ID: The return value of 'getResultMapId' is: " + id);
		return id;

	}

	public static String getResultMapIdOfMultiTab(SelectSpec selectSpec, List<OneToOneIdx> otoIndexList) {

		String selectId = getSelectIdOfMultiTable(selectSpec, otoIndexList);
		String id = selectId + MapperElm.MAPPER_RESULT_MAP;

		logger.debug("ID: The return value of 'getResultMapIdOfMultiTabSelect' is: " + id);
		return id.toString();

	}

	public static String getResultMapIdOfInternalOtm(ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec selectSpec, String attrName) {

		// Initiate the return value.
		String selectId = getSelectIdOfInternalOtm(artifactSpec, daoSpec, selectSpec, attrName, false);
		String id = selectId + MapperElm.MAPPER_RESULT_MAP;
		logger.debug("ID: The return value of 'getResultMapIdOfInternalOTM' is: " + id);
		return id;

	}

	public static String getSqlIdOfRootTabSel(SelectSpec selectSpec) {

		String selectId = getSelectIdOfRootTable(selectSpec, false);
		String id = MapperElm.MAPPER_SQL + selectId;
		logger.debug("ID: The return value of 'getSqlIdOfSelectRootTable' is: " + id);
		return id;

	}

	public static String getSqlIdOfMultiTabSel(SelectSpec ancesSelectSpec, List<OneToOneIdx> otoIndexList, boolean needPrefix) {

		String selectId = getSelectIdOfMultiTable(ancesSelectSpec, otoIndexList);
		String id = MapperElm.MAPPER_SQL + selectId;
		logger.debug("ID: The return value of 'getSqlIdOfMultiTabSelect' is: " + id);
		return id.toString();

	}

	public static String getSqlIdOfUpdByPK(UpdateSpec updateSpec, boolean isUpdAll, boolean hasTmLck) {

		String updId = getUpdateId(updateSpec, isUpdAll, MapperElm.MAPPER_BYPK, hasTmLck);
		String id = MapperElm.MAPPER_SQL + updId;
		logger.debug("ID: The return value of 'getSqlIdOfUpdByPK' is: " + id);
		return id;

	}

	public static String getSqlIdOfUpdBySql(UpdateSpec updateSpec, boolean isUpdAll, boolean hasTmLck) {

		String updId = getUpdateId(updateSpec, isUpdAll, MapperElm.MAPPER_BYSQL, hasTmLck);
		String id = MapperElm.MAPPER_SQL + updId;
		logger.debug("ID: The return value of 'getSqlIdOfUpdBySql' is: " + id);
		return id;

	}

	public static String getSqlIdOfDel(DeleteSpec deleteSpec) {

		String delId = getDeleteId(deleteSpec, null, false);
		String id = MapperElm.MAPPER_SQL + delId;
		logger.debug("ID: The return value of 'getSqlIdOfDel' is: " + id);
		return id;

	}

	public static String getSqlIdOfDelByPK(DeleteSpec deleteSpec, boolean hasTmLck) {

		String delId = getDeleteId(deleteSpec, MapperElm.MAPPER_BYPK, hasTmLck);
		String id = MapperElm.MAPPER_SQL + delId;
		logger.debug("ID: The return value of 'getSqlIdOfDel' is: " + id);
		return id;

	}

	public static String getSqlIdOfDelBySql(DeleteSpec deleteSpec, boolean hasTmLck) {

		String delId = getDeleteId(deleteSpec, MapperElm.MAPPER_BYSQL, hasTmLck);
		String id = MapperElm.MAPPER_SQL + delId;
		logger.debug("ID: The return value of 'getSqlIdOfDel' is: " + id);
		return id;

	}

	public static String getSqlIdOfInsertSelCol(InsertSpec insertSpec) {

		String id = getSqlIdOfInsert(insertSpec, false, false) + MapperElm.MAPPER_COL;
		logger.debug("ID: The return value of 'getSqlIdOfInsertSelCol' is: " + id);
		return id;

	}

	public static String getSqlIdOfInsertSelVal(InsertSpec insertSpec) {

		String id = getSqlIdOfInsert(insertSpec, false, false) + MapperElm.MAPPER_VALUE;
		logger.debug("ID: The return value of 'getSqlIdOfInsertSelVal' is: " + id);
		return id;

	}

	public static String getSqlIdOfInsertAllBatchCol(InsertSpec insertSpec, boolean withoutPk) {
		String id = getSqlIdOfInsert(insertSpec, true, true);
		if (withoutPk) {
			id += MapperElm.MAPPER_WITHOUT_PK;
		} else {
			id += MapperElm.MAPPER_WITH_PK;
		}
		id += MapperElm.MAPPER_COL;
		logger.debug("ID: The return value of 'getSqlIdOfInsertAllBatchCol' is: " + id);
		return id;

	}

	public static String getSqlIdOfInsertAllBatchVal(InsertSpec insertSpec, boolean withoutPk) {
		String id = getSqlIdOfInsert(insertSpec, true, true);
		if (withoutPk) {
			id += MapperElm.MAPPER_WITHOUT_PK;
		} else {
			id += MapperElm.MAPPER_WITH_PK;
		}
		id += MapperElm.MAPPER_VALUE;
		logger.debug("ID: The return value of 'getSqlIdOfInsertAllBatchVal' is: " + id);
		return id;

	}

	public static String getSqlIdOfInsert(InsertSpec insertSpec, boolean isAll, boolean isBatch) {

		String insertId = getInsertId(insertSpec, isAll, isBatch);
		String id = MapperElm.MAPPER_SQL + insertId;
		logger.debug("ID: The return value of 'getSqlIdOfInsert' is: " + id);
		return id;

	}

	public static String getSelectIdOfCountBySql(SelectSpec selectSpec) {

		String tableName = selectSpec.getTableName();
		String tableAlias = selectSpec.getTableAlias();
		String tableShortName = getTableShortName(tableName, tableAlias);
		String javaName = JavaFormatter.getJavaStyle(tableShortName, false, true);
		String id = MapperElm.SQL_COUNT + javaName + MapperElm.MAPPER_BYSQL;
		logger.debug("ID: The return value of 'getSelIdOfCountBySql' is: " + id);
		return id;

	}

	public static String getSelectIdOfRootTabByPK(SelectSpec selectSpec) {

		String selectId = getSelectIdOfRootTable(selectSpec, false);
		String id = selectId + MapperElm.MAPPER_BYPK;
		logger.debug("ID: The return value of 'getSelIdOfSelectRootTableByPK' is: " + id);
		return id;

	}

	public static String getSelectIdOfRootTabBySql(SelectSpec selectSpec) {

		String selectId = getSelectIdOfRootTable(selectSpec, false);
		String id = selectId + MapperElm.MAPPER_BYSQL;
		logger.debug("ID: The return value of 'getSelIdOfSelectRootTableBySql' is: " + id);
		return id;

	}

	public static String getSelectIdOfMultiTabByPK(SelectSpec ancesSelectSpec, List<OneToOneIdx> otoIndexList) {

		String selectId = getSelectIdOfMultiTable(ancesSelectSpec, otoIndexList);
		String id = selectId + MapperElm.MAPPER_BYPK;
		logger.debug("ID: The return value of 'getSelIdOfMultiTabSelectByPK' is: " + id);
		return id;

	}

	public static String getSelectIdOfMultiTabBySql(SelectSpec ancesSelectSpec, List<OneToOneIdx> otoIndexList) {

		String selectId = getSelectIdOfMultiTable(ancesSelectSpec, otoIndexList);
		String id = selectId + MapperElm.MAPPER_BYSQL;
		logger.debug("ID: The return value of 'getSelIdOfPublicSelectBySql' is: " + id);
		return id;

	}

	//
	// String tableName = insertSpec.getTableName();
	// String javaName = JavaFormatter.getJavaStyle(tableName, true);
	// javaName = StringHelper.toUpperCase(javaName, 0);
	//
	// id = MapperElm.MAPPER_SQL + MapperElm.SQL_INSERT + javaName +
	// MapperElm.MAPPER_ALL;
	//
	// logger.debug("ID: The return value of 'getSqlIdOfInsertAll' is: " + id);
	// return id;
	//
	// }

	// public static String getSqlIdOfInsertSelCol(InsertSpec insertSpec) {
	//
	// // Define the return value.
	// String id = new String();
	//
	// // If the given 'insertStatement' attribute is not null, then use it.
	// String insertStatement = insertSpec.getInsertStmt();
	// if (StringUtils.isNotBlank(insertStatement)) {
	// id = MapperElm.MAPPER_SQL + insertStatement + MapperElm.MAPPER_COLUMN;
	// logger.debug("ID: The return value of 'getSqlIdOfInsertSelCol' is: " +
	// id);
	// return id;
	// }
	//
	// String tableName = insertSpec.getTableName();
	// String javaName = JavaFormatter.getJavaStyle(tableName, true);
	// javaName = StringHelper.toUpperCase(javaName, 0);
	//
	// id = MapperElm.MAPPER_SQL + MapperElm.SQL_INSERT + javaName +
	// MapperElm.MAPPER_SEL + MapperElm.MAPPER_COLUMN;
	//
	// logger.debug("ID: The return value of 'getSqlIdOfInsertSelCol' is: " +
	// id);
	// return id;
	//
	// }
	//

	// public static String getInsertIdOfAll(InsertSpec insertSpec) {
	//
	// // Define the return value.
	// String id = new String();
	//
	// // If the given 'insertStatement' attribute is not null, then use it.
	// String insertStmt = insertSpec.getInsertStmt();
	// if (StringUtils.isNotBlank(insertStmt)) {
	// id = insertStmt;
	// logger.debug("ID: The return value of 'getInsertIdOfAll' is: " + id);
	// return id;
	// }
	//
	// String tableName = insertSpec.getTableName();
	// String javaName = JavaFormatter.getJavaStyle(tableName, true);
	// javaName = StringHelper.toUpperCase(javaName, 0);
	//
	// // The default value of id.
	// id = MapperElm.SQL_INSERT + javaName + MapperElm.MAPPER_ALL;
	//
	// logger.debug("ID: The return value of 'getInsertIdOfAll' is: " + id);
	// return id;
	//
	// }
	//
	// public static String getInsertIdOfSel(InsertSpec insertSpec) {
	//
	// // Define the return value.
	// String id = new String();
	//
	// // If the given 'insertStatement' attribute is not null, then use it.
	// String insertStmt = insertSpec.getInsertStmt();
	// if (StringUtils.isNotBlank(insertStmt)) {
	// id = insertStmt;
	// logger.debug("ID: The return value of 'getInsertIdOfSel' is: " + id);
	// return id;
	// }
	//
	// String tableName = insertSpec.getTableName();
	// String javaName = JavaFormatter.getJavaStyle(tableName, true);
	// javaName = StringHelper.toUpperCase(javaName, 0);
	//
	// // The defaut value of id.
	// id = MapperElm.SQL_INSERT + javaName + MapperElm.MAPPER_SEL;
	//
	// logger.debug("ID: The return value of 'getInsertIdOfSel' is: " + id);
	// return id;
	//
	// }

	// public static String getSqlIdOfUpdateAll(UpdateSpec updateSpec) {
	//
	// // Define the return value.
	// String id = new String();
	//
	// // If the given 'updateStatement' attribute is not null, then use it.
	// String updateStatement = updateSpec.getUpdateStmt();
	// if (StringUtils.isNotBlank(updateStatement)) {
	// id = MapperElm.MAPPER_SQL + updateStatement;
	// logger.debug("ID: The return value of 'getSqlIdOfUpdateAll' is: " + id);
	// return id;
	// }
	//
	// id = MapperElm.MAPPER_SQL + MapperElm.MAPPER_UPDATE;
	//
	// logger.debug("ID: The return value of 'getSqlIdOfUpdateAll' is: " + id);
	// return id;
	//
	// }
	//
	// public static String getSqlIdOfUpdateSel(UpdateSpec updateSpec) {
	//
	// String updateNamePrefix = getUpdateId(updateSpec, true, false);
	// String sqlId = updateNamePrefix + MapperElm.MAPPER_SEL;
	//
	// logger.debug("ID: The return value of 'getSqlIdOfUpdateSel' is: " +
	// sqlId);
	// return sqlId;
	//
	// }

	// public static String getUpdateIdOfAll(UpdateSpec updateSpec) {
	//
	// // Define the return value.
	// String id = new String();
	//
	// // If the given 'updateStatement' attribute is not null, then use it.
	// String updateStatement = updateSpec.getUpdateStmt();
	// if (StringUtils.isNotBlank(updateStatement)) {
	// id = updateStatement;
	// logger.debug("ID: The return value of 'getUpdateIdOfAll' is: " + id);
	// return id;
	// }
	//
	// id = MapperElm.MAPPER_STMT_UPD_ALL_BYPK;
	//
	// logger.debug("ID: The return value of 'getUpdateIdOfAll' is: " + id);
	// return id;
	//
	// }

	// public static String getUpdateIdOfSel(UpdateSpec updateElm) {
	//
	// // Define the return value.
	// String id = new String();
	//
	// // If the given 'updateStatement' attribute is not null, then use it.
	// String updateStatement = updateElm.getUpdateStmt();
	// if (StringUtils.isNotBlank(updateStatement)) {
	// id = updateStatement;
	// logger.debug("ID: The return value of 'getUpdateIdOfSelective' is: " +
	// id);
	// return id;
	// }
	//
	// id = MapperElm.MAPPER_STMT_UPD_SEL_BYPK;
	//
	// logger.debug("ID: The return value of 'getUpdateIdOfSelective' is: " +
	// id);
	// return id;
	//
	// }

	/**
	 * 获取内部OTM查询的Id
	 * 
	 * @param artifactSpec
	 * @param daoSpec
	 * @param selectSpec
	 * @param attrName
	 * @param useNameSpace
	 * @return
	 */
	public static String getSelectIdOfInternalOtm(ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec selectSpec, String attrName, boolean useNameSpace) {

		// Initiate the return value.
		StringBuffer id = new StringBuffer();

		// Compose the id with verb and subject.
		String verb = selectSpec.getVerb();
		if (StringUtils.isBlank(verb) == true) {
			verb = MapperElm.SQL_SELECT; // 如果未提供，采用默认verb
		}
		id.append(MapperElm.UNDER_LINE);
		id.append(verb);
		String subject = selectSpec.getSubject();
		if (StringUtils.isNotBlank(subject)) {
			id.append(StringHelper.toUpperCase(subject, 0));
		} else {
			String tableName = selectSpec.getTableName();
			if (StringUtils.isNotBlank(attrName) == true) {
				id.append(StringHelper.toUpperCase(attrName, 0));
			} else {
				String dtoXFullName = JavaFormatter.getDtoXFullName(artifactSpec.getCommonAttrSpec().getWorkspaceSpec(), tableName);
				id.append(StringHelper.toUpperCase(JavaFormatter.getClassSimpleName(dtoXFullName), 0) + JavaSrcElm.UTIL_LIST_SIMPLE);
			}

		}

		if (useNameSpace == false) {
			logger.debug("ID: The return value of 'getSelIdOfInternalOTM' is: " + id);
			return id.toString();
		} else {
			// 获取命名空间
			String pkgName = daoSpec.getPkgName() + MapperElm.DOT;
			logger.debug("ID: The return value of 'getSelIdOfInternalOTM' is: " + id);
			return pkgName + id;
		}

	}

	public static String getUpdateIdByPK(UpdateSpec updateSpec, boolean isUpdAll, boolean hasTmLck) {

		String id = getUpdateId(updateSpec, isUpdAll, MapperElm.MAPPER_BYPK, hasTmLck);
		logger.debug("ID: The return value of 'getUpdIdByPK' is: " + id);
		return id;

	}

	public static String getUpdateIdBySql(UpdateSpec updateSpec, boolean isUpdAll) {

		String id = getUpdateId(updateSpec, isUpdAll, MapperElm.MAPPER_BYSQL, false);
		logger.debug("ID: The return value of 'getUpdIdBySql' is: " + id);
		return id;

	}

	// // TODO
	// public static String getDelId(UpdateSpec updateSpec, boolean hasTmLck) {
	//
	// String id = getDelId(updateSpec, null , hasTmLck);
	// logger.debug("ID: The return value of 'getDelIdByPK' is: " + id);
	// return id;
	//
	// }

	public static String getDeleteIdByPK(DeleteSpec deleteSpec, boolean isByLong, boolean hasTmLck) {
		String id = getDeleteId(deleteSpec, isByLong == true ? MapperElm.MAPPER_BYPK + JavaSrcElm.LANG_LONG_SIMPLE : MapperElm.MAPPER_BYPK + JavaSrcElm.VO_NAME_SUFFIX, hasTmLck);
		logger.debug("ID: The return value of 'getDelIdByPK' is: " + id);
		return id;

	}

	public static String getDeleteIdBySql(DeleteSpec deleteSpec) {

		String id = getDeleteId(deleteSpec, MapperElm.MAPPER_BYSQL, false);
		logger.debug("ID: The return value of 'getDelIdBySql' is: " + id);
		return id;

	}

	public static String getInsertId(InsertSpec insertSpec, boolean isAll, boolean isBatch) {

		// Define the return value.
		String id = new String();

		// Compose the id with verb and subject.
		String verb = insertSpec.getVerb();
		if (StringUtils.isBlank(verb) == true) {
			verb = MapperElm.SQL_INSERT;
		}
		String subject = insertSpec.getSubject();
		if (StringUtils.isNotBlank(subject)) {
			id = verb + StringHelper.toUpperCase(subject, 0);
		} else {
			String tableName = insertSpec.getTableName();
			String javaName = JavaFormatter.getJavaStyle(tableName, false, true);
			// javaName = StringHelper.toUpperCase(javaName, 0);
			id = verb + javaName;
		}

		if (isAll == true) {
			id = id + MapperElm.MAPPER_ALL;
		} else {
			id = id + MapperElm.MAPPER_SELECTIVE;
		}

		if (isBatch == true) {
			id = id + MapperElm.MAPPER_BATCH;
		}

		logger.debug("ID: The return value of 'getInsertId' is: " + id);
		return id;

	}

	public static String getAddDtoMethodName(WorkspaceSpec workspaceSpec, InsertSpec insertSpec) throws AppException {

		// Define the return value.
		String methodName = new String();

		try {

			// If the given 'insertStmt' attribute is not null, then use it.
			String subject = insertSpec.getSubject();
			if (StringUtils.isNotBlank(subject)) {
				methodName = JavaSrcElm.ADD + StringHelper.toUpperCase(subject, 0);
			} else {
				String tableName = insertSpec.getTableName();
				String paramFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
				if (StringUtils.isBlank(paramFullName) == true) {
					throw new AppException("The given class name should not be blank.");
				}
				String classSimpleName = JavaFormatter.getClassSimpleName(paramFullName);
				methodName = JavaSrcElm.ADD + classSimpleName;
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		logger.debug("ID: The return value of 'getAddDtoMethodName' is: " + methodName);
		return methodName;

	}

	// public static String getUpdateByPKMethodName(UpdateSpec updateSpec)
	// throws AppException {
	//
	// // Define the return value.
	// String methodName = new String();
	//
	// try {
	//
	// // Compose the id with verb and subject.
	// String verb = updateSpec.getVerb();
	// if (StringUtils.isBlank(verb) == true) {
	// verb = MapperElm.SQL_UPDATE;
	// }
	// String subject = updateSpec.getSubject();
	// if (StringUtils.isNotBlank(subject)) {
	// methodName = verb + subject;
	// } else {
	// String paramFullName = updateSpec.getParamName();
	// if (StringUtils.isBlank(paramFullName) == true) {
	// throw new AppException("The given class name should not be blank.");
	// }
	// String classSimpleName = JavaFormatter.getClassSimpleName(paramFullName);
	// methodName = verb + classSimpleName + MapperElm.MAPPER_BYPK;
	// }
	//
	// } catch (Throwable t) {
	// ExceptionUtil.handleException(t, logger);
	// }
	//
	// logger.debug("ID: The return value of 'getChgStatMethodName' is: " +
	// methodName);
	// return methodName;
	//
	// }

	public static String getDeleteByPKMethodName(WorkspaceSpec workspaceSpec, UpdateSpec updateSpec) throws AppException {

		// Define the return value.
		String methodName = new String();

		try {

			// Compose the id with verb and subject.
			String verb = updateSpec.getVerb();
			if (StringUtils.isBlank(verb) == true) {
				verb = MapperElm.SQL_DELETE;
			}
			String subject = updateSpec.getSubject();
			if (StringUtils.isNotBlank(subject)) {
				methodName = verb + StringHelper.toUpperCase(subject, 0);
			} else {
				String tableName = updateSpec.getTableName();
				String paramFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
				if (StringUtils.isBlank(paramFullName) == true) {
					throw new AppException("The given class name should not be blank.");
				}
				String classSimpleName = JavaFormatter.getClassSimpleName(paramFullName);
				methodName = verb + classSimpleName + MapperElm.MAPPER_BYPK;
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		logger.debug("ID: The return value of 'getDelByPKMethodName' is: " + methodName);
		return methodName;

	}

	public static String getDeleteBySqlMethodName(WorkspaceSpec workspaceSpec, UpdateSpec updateSpec) throws AppException {

		// Define the return value.
		String methodName = new String();

		try {

			// Compose the id with verb and subject.
			String verb = updateSpec.getVerb();
			if (StringUtils.isBlank(verb) == true) {
				verb = MapperElm.SQL_DELETE;
			}
			String subject = updateSpec.getSubject();
			if (StringUtils.isNotBlank(subject)) {
				methodName = verb + StringHelper.toUpperCase(subject, 0);
			} else {
				String tableName = updateSpec.getTableName();
				String paramFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
				if (StringUtils.isBlank(paramFullName) == true) {
					throw new AppException("The given class name should not be blank.");
				}
				String classSimpleName = JavaFormatter.getClassSimpleName(paramFullName);
				methodName = verb + classSimpleName + MapperElm.MAPPER_BYSQL;
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		logger.debug("ID: The return value of 'getDelBySqlMethodName' is: " + methodName);
		return methodName;

	}

	private static String getSelectIdOfMultiTable(SelectSpec ancesSelectSpec, List<OneToOneIdx> otoIndexList) {

		String verb = ancesSelectSpec.getVerb();
		String subject = ancesSelectSpec.getSubject();
		String ancesTableName = ancesSelectSpec.getTableName();
		String ancesJavaName = null;
		StringBuffer id = new StringBuffer();

		// Compose the id with verb and subject.
		if (StringUtils.isBlank(verb) == true) {
			verb = MapperElm.SQL_SELECT;
		}
		if (StringUtils.isNotBlank(subject)) {
			id.append(verb);
			id.append(StringHelper.toUpperCase(subject, 0));
			logger.debug("ID: The return value of 'getSelIdOfMultiTabSelect' is: " + id);
			return id.toString();
		}

		OneToOneIdx descOtoIdx = null;
		RelationshipSpec descOneToOne = null;
		SelectSpec descResultMapElm = null;
		String descTableName = null;
		String descTableAlias = null;

		// Change to Java style.
		ancesJavaName = JavaFormatter.getJavaStyle(ancesTableName, false, true);
		// ancesJavaName = StringHelper.toUpperCase(ancesJavaName, 0);
		id.append(verb);
		id.append(ancesJavaName);

		if (otoIndexList != null) {

			for (int i = 0; i < otoIndexList.size(); i++) {

				descOtoIdx = otoIndexList.get(i);
				descOneToOne = descOtoIdx.getOneToOne();
				descResultMapElm = descOneToOne.getSelectSpec();
				descTableName = descResultMapElm.getTableName();
				descTableAlias = descResultMapElm.getTableAlias();

				if (StringUtils.isNotBlank(descTableAlias) == true) {
					// Change to Java style.
					descTableAlias = JavaFormatter.getJavaStyle(descTableAlias, false, true);
					// descTableAlias = StringHelper.toUpperCase(descTableAlias,
					// 0);
					id.append(descTableAlias);
				} else {
					// Change to Java style.
					descTableName = JavaFormatter.getJavaStyle(descTableName, false, true);
					// descTableName = StringHelper.toUpperCase(descTableName,
					// 0);
					id.append(descTableName);
				}

			}

			// Determine the id of select according to the number of sub OTO or
			// OTM
			if (otoIndexList.size() > 0) {
				id.append(MapperElm.RESULT_MAP_ONE_TO_ONE);
			} else if (ancesSelectSpec.getOneToMany().size() > 0) {
				id.append(MapperElm.RESULT_MAP_ONE_TO_MANY);
			}

		}

		logger.debug("ID: The return value of 'getSelIdOfMultiTabSelect' is: " + id);
		return id.toString();

	}

	private static String getSelectIdOfRootTable(SelectSpec selectSpec, boolean isCount) {

		// Get attributes from selectSpec.
		String tableName = selectSpec.getTableName();
		String tableAlias = selectSpec.getTableAlias();

		// Change to Java style.
		String tableShortName = getTableShortName(tableName, tableAlias);
		String javaName = JavaFormatter.getJavaStyle(tableShortName, false, true);

		// Define the return value.
		String verb = selectSpec.getVerb();
		if (StringUtils.isBlank(verb) == true) {
			verb = MapperElm.SQL_SELECT;
		}
		String id = verb + javaName;

		logger.debug("ID: The return value of 'getSelIdOfRootTab' is: " + id);
		return id;

	}

	private static String getUpdateId(UpdateSpec updateSpec, boolean isUpdAll, String whereClause, boolean hasTmLck) {

		// Define the return value.
		String id = new String();

		// Compose the id with verb and subject.
		String verb = updateSpec.getVerb();
		if (StringUtils.isBlank(verb) == true) {
			verb = MapperElm.SQL_UPDATE;
		}
		String subject = updateSpec.getSubject();
		if (StringUtils.isNotBlank(subject)) {
			id = verb + StringHelper.toUpperCase(subject, 0);
		} else {
			String tableName = updateSpec.getTableName();
			String javaName = JavaFormatter.getJavaStyle(tableName, false, true);
			// javaName = StringHelper.toUpperCase(javaName, 0);
			id = verb + javaName;
		}

		// Update all columns or not.
		if (isUpdAll == true) {
			id = id + MapperElm.MAPPER_ALL;
		} else {
			id = id + MapperElm.MAPPER_SELECTIVE;
		}

		// By primary key or sql clause.
		id = id + whereClause;

		// With time lock or not.
		if (hasTmLck == true) {
			id = id + MapperElm.MAPPER_TM_LCK;
		}

		logger.debug("ID: The return value of 'getUpdId' is: " + id);
		return id;

	}

	private static String getDeleteId(DeleteSpec deleteSpec, String whereClause, boolean hasTmLck) {

		// Define the return value.
		String id = new String();

		// Compose the id with verb and subject.
		String verb = deleteSpec.getVerb();
		if (StringUtils.isBlank(verb) == true) {
			verb = MapperElm.SQL_DELETE;
		}
		String subject = deleteSpec.getSubject();
		if (StringUtils.isNotBlank(subject)) {
			id = verb + StringHelper.toUpperCase(subject, 0);
		} else {
			String tableName = deleteSpec.getTableName();
			String javaName = JavaFormatter.getJavaStyle(tableName, false, true);
			// javaName = StringHelper.toUpperCase(javaName, 0);
			id = verb + javaName;
		}

		// By primary key or sql clause.
		if (null != whereClause) {
			id = id + whereClause;
		}

		// With time lock or not.
		if (hasTmLck == true) {
			// Delete带有时间锁的信息来自于VO
			id = id + MapperElm.MAPPER_TM_LCK;
		}

		logger.debug("ID: The return value of 'getDelId' is: " + id);
		return id;

	}

}
