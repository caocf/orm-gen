package bh.toolkit.srcgen.connector.rdb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.rdb.ColumnSpec;
import bh.toolkit.srcgen.model.rdb.TableSpec;
import bh.toolkit.srcgen.registry.CtxCacheFacade;

/**
 * 分析DB2的表结构
 * 
 * @author zhaorb
 */
public class DB2TableAnalyzer implements ITableAnalyzer {

	private static Logger logger = Logger.getLogger(DB2TableAnalyzer.class);

	private final String DBMETA_TABLE_NAME = "TABLE_NAME";
	private final String DBMETA_TABLE_REMARKS = "REMARKS";
	private final String COLMETA_COLUMN_NAME = "COLUMN_NAME";
	private final String COLMETA_DATA_TYPE = "DATA_TYPE";
	private final String COLMETA_TYPE_NAME = "TYPE_NAME";
	private final String COLMETA_REMARKS = "REMARKS";

	@Override
	public void analyzeAllTables(Connection conn, DatabaseMetaData dbMetaData, String schema) throws SQLException, ClassNotFoundException {

		// Get all tables belonging to the given schema.
		ResultSet tableRs = dbMetaData.getTables(null, schema, null, null);

		ResultSet columnRs = null;
		ResultSet pkRs = null;
		TableSpec tableSpec = null;
		ColumnSpec columnSpec = null;
		String tableName = null;
		String tableFullName = null;
		String columnName = null;
		int tableCnt = 0;
		while (tableRs.next()) {

			tableCnt = tableCnt + 1;

			// Get current table name, and initiate a TableSpec.
			tableSpec = new TableSpec();
			tableName = tableRs.getString(DBMETA_TABLE_NAME).toUpperCase();
			tableSpec.setSchema(schema);
			tableSpec.setShortName(tableName);
			tableSpec.setFullName(schema + MapperElm.DOT + tableName);
			tableSpec.setComments(tableRs.getString(DBMETA_TABLE_REMARKS));
			logger.info("GOT TABLE: " + tableName);

			// Get columns belonging to current table and corresponding Java attribute.
			columnRs = dbMetaData.getColumns(null, schema, tableName, null);
			while (columnRs.next()) {

				// Get current column name, and initiate a ColumnSpec.
				columnSpec = new ColumnSpec();
				columnSpec.setName(columnRs.getString(COLMETA_COLUMN_NAME));
				columnSpec.setJdbcTypeName(columnRs.getString(COLMETA_TYPE_NAME));
				columnSpec.setJdbcType(columnRs.getInt(COLMETA_DATA_TYPE));
				columnSpec.setComments(columnRs.getString(COLMETA_REMARKS));

				// Put current ColumnSpec into current TableSpec.
				tableSpec.addColumn(columnSpec);

			}

			// Get primary keys belonging to current table.
			pkRs = dbMetaData.getPrimaryKeys(null, schema, tableName);
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

		logger.info("GOT TABLE: There are " + tableCnt + " table(s) in schema: " + schema);

	}

}
