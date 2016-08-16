package bh.toolkit.srcgen.connector.rdb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.artifact.DatasourceSpec;
import bh.toolkit.srcgen.util.DBConnector;

public class DBAnalyzer {

	private static Logger logger = Logger.getLogger(DBAnalyzer.class);

	public static void analyzeTables(DatasourceSpec datasource)
			throws SQLException, ClassNotFoundException, AppException {

		Connection conn = null;

		try {

			DBDialectFactory dbDialectFactory = DBDialectFactory.getInstance(datasource);

			// 依次分析各Schema包含的表
			String[] schemaArray = StringUtils.split(datasource.getSchema(), MapperElm.COMMA);
			logger.info(
					"CONNECT DB: Begin to connect to DB: [" + datasource.getDbType() + "]" + datasource.getConnUrl());
			conn = DBConnector.getConnection(datasource);
			logger.info("CONNECT DB: DB connected, begin to analyze...");
			DatabaseMetaData dbMetaData = conn.getMetaData();
			if (schemaArray != null && schemaArray.length != 0) {
				for (String theSchema : schemaArray) {
					logger.info("ANALYZE SCHEMA: Prepare to analyze DB schema: " + theSchema);
					dbDialectFactory.getTableAnalyzer().analyzeAllTables(conn, dbMetaData, theSchema);
				}
			}

		} finally {
			if (conn != null) {
				conn.close();
			}
		}

	}

}
