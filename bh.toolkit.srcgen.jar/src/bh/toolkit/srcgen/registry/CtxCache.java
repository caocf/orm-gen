package bh.toolkit.srcgen.registry;

import java.util.Hashtable;

import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.model.artifact.CommonAttrSpec;
import bh.toolkit.srcgen.model.mybatis.ResultMap;
import bh.toolkit.srcgen.model.mybatis.Select;
import bh.toolkit.srcgen.model.mybatis.Sql;
import bh.toolkit.srcgen.model.rdb.TableSpec;
import bh.toolkit.srcgen.model.src.ClassSrc;

public class CtxCache {

	private static CtxCache ctxCache;

	private CommonAttrSpec commonAttrSpec;
	private Hashtable<String, String> dataTypeMappingCache;
	private Hashtable<String, TableSpec> tableSpecCache;
	private Hashtable<String, ResultMap> resultMapCache;
	private Hashtable<String, Sql> sqlCache;
	private Hashtable<String, Select> selectCache;
	private Hashtable<String, ClassSrc> voCache;
	private Hashtable<String, ClassSrc> dtoCache;
	private Hashtable<String, ClassSrc> dtoXCache;
	private Hashtable<String, ClassSrc> daoIntfCache;
	private Hashtable<String, ClassSrc> daoXIntfCache;
	private Hashtable<String, ClassSrc> oprIntfCache;
	private Hashtable<String, ClassSrc> oprImplCache;
	private Hashtable<String, ClassSrc> oprXIntfCache;
	private Hashtable<String, ClassSrc> oprXImplCache;
	private Hashtable<String, ClassSrc> mgrIntfCache;
	private Hashtable<String, ClassSrc> mgrImplCache;
	private Hashtable<String, ClassSrc> mgrXIntfCache;
	private Hashtable<String, ClassSrc> mgrXImplCache;
	private Hashtable<String, ClassSrc> msgCodeCache;

	void initDataTypeMapping() {
		// Initiate data mapping from DB type to Java Type.
		dataTypeMappingCache.put(JavaSrcElm.JDBC_TYPE_VARCHAR, "java.lang.String");
		dataTypeMappingCache.put(JavaSrcElm.JDBC_TYPE_CHAR, "java.lang.String");
		dataTypeMappingCache.put(JavaSrcElm.JDBC_TYPE_BIGINT, "java.lang.Long");
		dataTypeMappingCache.put(JavaSrcElm.JDBC_TYPE_INTEGER, "java.lang.Integer");
		dataTypeMappingCache.put(JavaSrcElm.JDBC_TYPE_SHORT, "java.lang.Short");
		dataTypeMappingCache.put(JavaSrcElm.JDBC_TYPE_FLOAT, "java.lang.Double");
		dataTypeMappingCache.put(JavaSrcElm.JDBC_TYPE_DECIMAL, "java.lang.Double");
		dataTypeMappingCache.put(JavaSrcElm.JDBC_TYPE_DOUBLE, "java.lang.Double");
		dataTypeMappingCache.put(JavaSrcElm.JDBC_TYPE_REAL, "java.lang.Float");
		dataTypeMappingCache.put(JavaSrcElm.JDBC_TYPE_TIMESTAMP, "java.sql.Timestamp");
		dataTypeMappingCache.put(JavaSrcElm.JDBC_TYPE_DATE, "java.sql.Date");
		dataTypeMappingCache.put(JavaSrcElm.JDBC_TYPE_DATETIME, "java.sql.Timestamp");
	}

	private CtxCache() {
		dataTypeMappingCache = new Hashtable<String, String>();
		tableSpecCache = new Hashtable<String, TableSpec>();
		resultMapCache = new Hashtable<String, ResultMap>();
		sqlCache = new Hashtable<String, Sql>();
		selectCache = new Hashtable<String, Select>();
		voCache = new Hashtable<String, ClassSrc>();
		dtoCache = new Hashtable<String, ClassSrc>();
		dtoXCache = new Hashtable<String, ClassSrc>();
		daoIntfCache = new Hashtable<String, ClassSrc>();
		daoXIntfCache = new Hashtable<String, ClassSrc>();
		oprIntfCache = new Hashtable<String, ClassSrc>();
		oprImplCache = new Hashtable<String, ClassSrc>();
		oprXIntfCache = new Hashtable<String, ClassSrc>();
		oprXImplCache = new Hashtable<String, ClassSrc>();
		mgrIntfCache = new Hashtable<String, ClassSrc>();
		mgrImplCache = new Hashtable<String, ClassSrc>();
		mgrXIntfCache = new Hashtable<String, ClassSrc>();
		mgrXImplCache = new Hashtable<String, ClassSrc>();
		msgCodeCache = new Hashtable<String, ClassSrc>();
		initDataTypeMapping();
	}

	synchronized static CtxCache getInstance() {
		if (ctxCache == null) {
			ctxCache = new CtxCache();
		}
		return ctxCache;
	}

	void clearCache() {

		if (voCache != null) {
			voCache.clear();
		}
		// TODO 由于DTO需要被多个Ant任务文件所共享，故不能清除。但不清除DTO存在覆盖输出，需要优化
		if (dtoCache != null) {
			dtoCache.clear();
		}
		if (dtoXCache != null) {
			dtoXCache.clear();
		}
		if (daoIntfCache != null) {
			daoIntfCache.clear();
		}
		if (daoXIntfCache != null) {
			daoXIntfCache.clear();
		}
		if (oprIntfCache != null) {
			oprIntfCache.clear();
		}
		if (oprImplCache != null) {
			oprImplCache.clear();
		}
		if (oprXIntfCache != null) {
			oprXIntfCache.clear();
		}
		if (oprXImplCache != null) {
			oprXImplCache.clear();
		}
		if (mgrIntfCache != null) {
			mgrIntfCache.clear();
		}
		if (mgrImplCache != null) {
			mgrImplCache.clear();
		}
		if (mgrXIntfCache != null) {
			mgrXIntfCache.clear();
		}
		if (mgrXImplCache != null) {
			mgrXImplCache.clear();
		}
		if (msgCodeCache != null) {
			msgCodeCache.clear();
		}

	}

	static CtxCache getCtxCache() {
		return ctxCache;
	}

	static void setCtxCache(CtxCache ctxCache) {
		CtxCache.ctxCache = ctxCache;
	}

	CommonAttrSpec getCommonAttrSpec() {
		return commonAttrSpec;
	}

	void setCommonAttrSpec(CommonAttrSpec commonAttrSpec) {
		this.commonAttrSpec = commonAttrSpec;
	}

	Hashtable<String, String> getDataTypeMappingCache() {
		return dataTypeMappingCache;
	}

	void setDataTypeMappingCache(Hashtable<String, String> dataTypeMappingCache) {
		this.dataTypeMappingCache = dataTypeMappingCache;
	}

	Hashtable<String, TableSpec> getTableSpecCache() {
		return tableSpecCache;
	}

	void setTableSpecCache(Hashtable<String, TableSpec> tableSpecCache) {
		this.tableSpecCache = tableSpecCache;
	}

	Hashtable<String, ResultMap> getResultMapCache() {
		return resultMapCache;
	}

	void setResultMapCache(Hashtable<String, ResultMap> resultMapCache) {
		this.resultMapCache = resultMapCache;
	}

	Hashtable<String, Sql> getSqlCache() {
		return sqlCache;
	}

	void setSqlCache(Hashtable<String, Sql> sqlCache) {
		this.sqlCache = sqlCache;
	}

	Hashtable<String, Select> getSelectCache() {
		return selectCache;
	}

	void setSelectCache(Hashtable<String, Select> selectCache) {
		this.selectCache = selectCache;
	}

	// Hashtable<String, ClassSrc> getConstClassCache() {
	// return constClassCache;
	// }
	//
	// void setConstClassCache(Hashtable<String, ClassSrc> constClassCache) {
	// this.constClassCache = constClassCache;
	// }

	Hashtable<String, ClassSrc> getVoCache() {
		return voCache;
	}

	void setVoCache(Hashtable<String, ClassSrc> voCache) {
		this.voCache = voCache;
	}

	Hashtable<String, ClassSrc> getDtoCache() {
		return dtoCache;
	}

	void setDtoCache(Hashtable<String, ClassSrc> dtoCache) {
		this.dtoCache = dtoCache;
	}

	Hashtable<String, ClassSrc> getDtoXCache() {
		return dtoXCache;
	}

	void setDtoXCache(Hashtable<String, ClassSrc> dtoXCache) {
		this.dtoXCache = dtoXCache;
	}

	Hashtable<String, ClassSrc> getDaoIntfCache() {
		return daoIntfCache;
	}

	void setDaoIntfCache(Hashtable<String, ClassSrc> daoIntfCache) {
		this.daoIntfCache = daoIntfCache;
	}

	/**
	 * @return the daoXIntfCache
	 */
	Hashtable<String, ClassSrc> getDaoXIntfCache() {
		return daoXIntfCache;
	}

	/**
	 * @param daoXIntfCache
	 *            the daoXIntfCache to set
	 */
	void setDaoXIntfCache(Hashtable<String, ClassSrc> daoXIntfCache) {
		this.daoXIntfCache = daoXIntfCache;
	}

	Hashtable<String, ClassSrc> getOprIntfCache() {
		return oprIntfCache;
	}

	void setOprIntfCache(Hashtable<String, ClassSrc> oprIntfCache) {
		this.oprIntfCache = oprIntfCache;
	}

	Hashtable<String, ClassSrc> getOprImplCache() {
		return oprImplCache;
	}

	void setOprImplCache(Hashtable<String, ClassSrc> oprImplCache) {
		this.oprImplCache = oprImplCache;
	}

	Hashtable<String, ClassSrc> getOprXIntfCache() {
		return oprXIntfCache;
	}

	void setOprXIntfCache(Hashtable<String, ClassSrc> oprXIntfCache) {
		this.oprXIntfCache = oprXIntfCache;
	}

	Hashtable<String, ClassSrc> getOprXImplCache() {
		return oprXImplCache;
	}

	void setOprXImplCache(Hashtable<String, ClassSrc> oprXImplCache) {
		this.oprXImplCache = oprXImplCache;
	}

	Hashtable<String, ClassSrc> getMgrIntfCache() {
		return mgrIntfCache;
	}

	void setMgrIntfCache(Hashtable<String, ClassSrc> mgrIntfCache) {
		this.mgrIntfCache = mgrIntfCache;
	}

	Hashtable<String, ClassSrc> getMgrImplCache() {
		return mgrImplCache;
	}

	void setMgrImplCache(Hashtable<String, ClassSrc> mgrImplCache) {
		this.mgrImplCache = mgrImplCache;
	}

	Hashtable<String, ClassSrc> getMgrXIntfCache() {
		return mgrXIntfCache;
	}

	void setMgrXIntfCache(Hashtable<String, ClassSrc> mgrXIntfCache) {
		this.mgrXIntfCache = mgrXIntfCache;
	}

	Hashtable<String, ClassSrc> getMgrXImplCache() {
		return mgrXImplCache;
	}

	void setMgrXImplCache(Hashtable<String, ClassSrc> mgrXImplCache) {
		this.mgrXImplCache = mgrXImplCache;
	}

	Hashtable<String, ClassSrc> getMsgCodeCache() {
		return msgCodeCache;
	}

	void setMsgCodeCache(Hashtable<String, ClassSrc> msgCodeCache) {
		this.msgCodeCache = msgCodeCache;
	}

}
