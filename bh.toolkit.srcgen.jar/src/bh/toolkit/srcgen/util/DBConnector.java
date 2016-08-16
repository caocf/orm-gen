package bh.toolkit.srcgen.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import bh.toolkit.srcgen.model.artifact.DatasourceSpec;

/**
 * 获取DB连接，如果需要采用其他方式获取DB连接，则一次性修改此类。
 * 
 * @author zhaorb
 */
public class DBConnector {

	private static Logger logger = Logger.getLogger(DBConnector.class);

	public static Connection getConnection(DatasourceSpec datasource) throws ClassNotFoundException, SQLException {

		String driver = StringUtils.trim(datasource.getDriver());
		String connUrl = StringUtils.trim(datasource.getConnUrl());
		String userName = StringUtils.trim(datasource.getUser());
		String password = StringUtils.trim(datasource.getPassword());

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(connUrl, userName, password);
		logger.debug("Connect to DB \"" + connUrl + "\" with userName \"" + userName + "\" and password \"" + password + "\"");
		return conn;

	}

}
