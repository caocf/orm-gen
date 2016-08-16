package bh.toolkit.srcgen.connector.rdb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.rdb.ColumnSpec;
import bh.toolkit.srcgen.model.rdb.TableSpec;
import bh.toolkit.srcgen.registry.CtxCacheFacade;

/**
 * 分析MySQL的表结构
 * 
 * @author zhaorb
 */
public class MySqlTableAnalyzer implements ITableAnalyzer {

	private static Logger logger = Logger.getLogger(MySqlTableAnalyzer.class);

	private final String DBMETA_TABLE_NAME = "TABLE_NAME";
	//	private final String DBMETA_TABLE_REMARKS = "REMARKS";
	private final String COLMETA_COLUMN_NAME = "COLUMN_NAME";
	private final String COLMETA_DATA_TYPE = "DATA_TYPE";
	private final String COLMETA_TYPE_NAME = "TYPE_NAME";
	private final String COLMETA_REMARKS = "REMARKS";

	@Override
	public void analyzeAllTables(Connection conn, DatabaseMetaData dbMetaData, String schema) throws SQLException, ClassNotFoundException {

		// Get all tables belonging to the given schema. For MySQL, the first parameter "catalog" is the schema name (and database name).
		ResultSet tableRs = dbMetaData.getTables(schema, null, null, null);
		Statement stmt = conn.createStatement();
		ResultSet createRs = null;
		ResultSet columnRs = null;
		ResultSet pkRs = null;
		TableSpec tableSpec = null;
		ColumnSpec columnSpec = null;
		String createStmt = null;
		String tableComment = null;
		String tableName = null;
		String tableFullName = null;
		String columnName = null;
		int tableCnt = 0;
		while (tableRs.next()) {

			tableCnt = tableCnt + 1;

			// Get current table name, and initiate a Table.
			tableSpec = new TableSpec();
			tableName = tableRs.getString(DBMETA_TABLE_NAME).toUpperCase();
			tableSpec.setSchema(schema);
			tableSpec.setShortName(tableName);
			tableSpec.setFullName(schema + MapperElm.DOT + tableName);

			// 获取MySQL的表注释
			createRs = stmt.executeQuery("SHOW CREATE TABLE " + schema + MapperElm.DOT + tableName);
			if (createRs != null && createRs.next()) {
				createStmt = createRs.getString(2);
				tableComment = parseCreate(createStmt);
			}
			tableSpec.setComments(tableComment);
			//			tableSpec.setComments(tableRs.getString(DBMETA_TABLE_REMARKS));
			logger.info("GOT TABLE: Got DB table: " + tableName);

			// Get columns belonging to current table and corresponding Java attribute.
			columnRs = dbMetaData.getColumns(null, null, tableName, null);
			while (columnRs.next()) {

				// Get current column name, and initiate a OrmColumn.
				columnSpec = new ColumnSpec();
				columnSpec.setName(columnRs.getString(COLMETA_COLUMN_NAME));
				if ("INT".equals(columnRs.getString(COLMETA_TYPE_NAME))) {
					columnSpec.setJdbcTypeName(JavaSrcElm.JDBC_TYPE_INTEGER);
				} else if ("DATETIME".equals(columnRs.getString(COLMETA_TYPE_NAME))) {
					columnSpec.setJdbcTypeName(JavaSrcElm.JDBC_TYPE_TIMESTAMP);
				} else {
					columnSpec.setJdbcTypeName(columnRs.getString(COLMETA_TYPE_NAME));
				}
				columnSpec.setJdbcType(columnRs.getInt(COLMETA_DATA_TYPE));
				columnSpec.setComments(columnRs.getString(COLMETA_REMARKS));

				// Put current Column into current OrmTable.
				tableSpec.addColumn(columnSpec);
			}

			// Get primary keys belonging to current table.
			pkRs = dbMetaData.getPrimaryKeys(null, null, tableName);
			while (pkRs.next()) {
				// Get current column name, and update corresponding Column.
				columnName = pkRs.getString(COLMETA_COLUMN_NAME);
				logger.debug("PRIMARY KEY: Get primary key with column name: " + columnName);
				columnSpec = tableSpec.getColumnIdx().get(columnName);
				columnSpec.setPrimaryKey(true);
			}

			tableFullName = schema + MapperElm.DOT + tableName;
			CtxCacheFacade.addTableSpec(tableFullName, tableSpec);

		}

		logger.info("【TABLE TOTAL】  There are " + tableCnt + " table(s) in schema: " + schema);

	}

	private String parseCreate(String createStmt) {

		String tableComment = null;
		int index = createStmt.indexOf("COMMENT='");
		if (index < 0) {
			return "";
		}
		tableComment = createStmt.substring(index + 9);
		tableComment = tableComment.substring(0, tableComment.length() - 1);
		return tableComment;

	}

}
