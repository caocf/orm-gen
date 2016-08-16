package bh.toolkit.srcgen.connector.rdb;

import org.apache.log4j.Logger;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.lang.GlobalConst;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.artifact.DatasourceSpec;

// TODO 需要进行改造
public class DBDialectFactory {

	private static Logger logger = Logger.getLogger(DBDialectFactory.class);

	private static DBDialectFactory dbDialectFactory;
	private String dbType;
	private ITableAnalyzer tableAnalyzer;
	private IPageQueryBuilder pageQueryBuilder;

	public static synchronized DBDialectFactory getInstance(DatasourceSpec datasourceSpec) throws AppException {
		if (dbDialectFactory == null) {
			dbDialectFactory = new DBDialectFactory(datasourceSpec);
		}
		return dbDialectFactory;
	}

	private DBDialectFactory(DatasourceSpec datasourceSpec) throws AppException {

		// 确定DB的类型
		dbType = datasourceSpec.getDbType();

		if (tableAnalyzer == null) {
			if (GlobalConst.DB_TYPE_DB2.equalsIgnoreCase(dbType)) {
				tableAnalyzer = new DB2TableAnalyzer();
				logger.info("Create DB2 tableAnalyzer!");
			} else if (GlobalConst.DB_TYPE_MYSQL.equalsIgnoreCase(dbType)) {
				tableAnalyzer = new MySqlTableAnalyzer();
				logger.info("create MySql tableAnalyzer!");
			} else {
				logger.error("Invalid DB type: " + dbType);
				throw new AppException("Invalid DB type: " + dbType);
			}
		}

		if (pageQueryBuilder == null) {
			if (GlobalConst.DB_TYPE_DB2.equalsIgnoreCase(dbType)) {
				pageQueryBuilder = new DB2PageQueryBuilder();
				logger.info("create DB2 pageQueryBuilder!");
			} else if (GlobalConst.DB_TYPE_MYSQL.equalsIgnoreCase(dbType)) {
				pageQueryBuilder = new MySqlPageQueryBuilder();
				logger.info("create MySql pageQueryBuilder!");
			} else {
				logger.error("Invalid DB type: " + dbType);
				throw new AppException("Invalid DB type: " + dbType);
			}
		}

	}

	/**
	 * 获得不同分析器用以分析表结构
	 * 
	 * @return
	 * @throws AppException
	 */
	public ITableAnalyzer getTableAnalyzer() throws AppException {
		return tableAnalyzer;
	}

	/**
	 * 获得不同分页查询器用以构建分页查询
	 * 
	 * @param mapperProfile
	 * @return
	 * @throws AppException
	 */
	public IPageQueryBuilder getPageQueryBuilder() throws AppException {
		return pageQueryBuilder;
	}

	/**
	 * 获得用以构建数据库主键的函数
	 * 
	 * @return
	 */
	public String getDBIdentityValueFunction() throws AppException {

		if (GlobalConst.DB_TYPE_DB2.equalsIgnoreCase(dbType)) {
			return MapperElm.DB2_IDENTITY_VAL;
		} else if (GlobalConst.DB_TYPE_MYSQL.equalsIgnoreCase(dbType)) {
			return MapperElm.MYSQL_IDENTITY_VAL;
		} else {
			logger.error("Invalid DB type: " + dbType);
			throw new AppException("Invalid DB type: " + dbType);
		}
		
	}

}
