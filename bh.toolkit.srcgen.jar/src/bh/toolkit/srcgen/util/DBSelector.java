package bh.toolkit.srcgen.util;

import bh.toolkit.srcgen.lang.GlobalConst;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.model.artifact.CommonAttrSpec;
import bh.toolkit.srcgen.model.artifact.DatasourceSpec;

/**
 * 用于判断所连接的数据库的类型
 * 
 * @author FuRong 2015年6月24日
 *
 */
public class DBSelector {

	/*************************************************************
	 * 
	 * connection.url=jdbc:mysql://localhost:3306/fz_cms datasource.connUrl=jdbc:db2://10.98.0.192:60005/SLPDB_D1
	 * 
	 *************************************************************/

	/**
	 * 根据GlobalProfile，获取所连接的数据库类型 其实本质上还是根据连接数据库的url来判断
	 * 
	 * @param globalProfile
	 * @return 如果参数有问题，返回null
	 */
	public static String getDBType(CommonAttrSpec commonAttrSpec) {
		String dbType = null;
		if (null == commonAttrSpec) {
			return dbType;
		}
		DatasourceSpec datasourceSpec = commonAttrSpec.getDatasourceSpec();
		if (null == datasourceSpec) {
			return dbType;
		}
		String connUrl = datasourceSpec.getConnUrl();
		return getDBType(connUrl);
	}

	/**
	 * 根据连接数据库的url来获取数据库类型
	 * 
	 * @param globalProfile
	 * @return 如果参数有问题，返回null
	 */
	public static String getDBType(String connUrl) {
		String dbType = null;
		if (null == connUrl) {
			return dbType;
		}

		// 找到第一个冒号
		int colonFirstIndex = connUrl.indexOf(JavaSrcElm.COLON);

		if (colonFirstIndex != -1) {
			if (colonFirstIndex + 1 >= connUrl.length()) {
				return dbType;
			}
			// 找到第二个冒号
			int colonSecondIndex = connUrl.indexOf(JavaSrcElm.COLON, colonFirstIndex + 1);
			if (colonSecondIndex != -1) {
				// 获取两个冒号之间的字符串，即所数据库名称
				String type = connUrl.substring(colonFirstIndex + 1, colonSecondIndex);
				type = type.toUpperCase();
				// 匹配数据库类型，三种： MYSQL，DB2，其他
				if (type.equals(GlobalConst.DB_TYPE_MYSQL)) {
					dbType = GlobalConst.DB_TYPE_MYSQL;
				} else if (type.equals(GlobalConst.DB_TYPE_DB2)) {
					dbType = GlobalConst.DB_TYPE_DB2;
				} else {
					dbType = GlobalConst.DB_TYPE_OTHER;
				}
			}
		}
		return dbType;
	}

	/**
	 * 测试main函数
	 */
	public static void main(String[] args) {
		String url1 = "connection.url=jdbc:mysql://localhost:3306/fz_cms";
		String url2 = "datasource.connUrl=jdbc:db2://10.98.0.192:60005/SLPDB_D1";
		CommonAttrSpec commonAttrSpec = new CommonAttrSpec();
		DatasourceSpec datasourceSpec = commonAttrSpec.getDatasourceSpec();
		datasourceSpec.setConnUrl(url1);
		commonAttrSpec.setDatasourceSpec(datasourceSpec);

		System.out.println(">>" + getDBType(url1));
		System.out.println(">>" + getDBType(url2));
		System.out.println(">>" + getDBType(commonAttrSpec));
	}

}
