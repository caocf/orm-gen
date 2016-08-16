package bh.toolkit.srcgen.connector.rdb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public interface ITableAnalyzer {

	public void analyzeAllTables(Connection conn, DatabaseMetaData dbMetaData, String schema) throws SQLException, ClassNotFoundException;

}
