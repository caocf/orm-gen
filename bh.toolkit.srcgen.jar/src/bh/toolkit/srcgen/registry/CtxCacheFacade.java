package bh.toolkit.srcgen.registry;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.io.ClassSrcImporter;
import bh.toolkit.srcgen.io.FileHandler;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.lang.MsgCodeComments;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.CommonAttrSpec;
import bh.toolkit.srcgen.model.artifact.DBTableSpec;
import bh.toolkit.srcgen.model.artifact.DaoSpec;
import bh.toolkit.srcgen.model.artifact.MgrSpec;
import bh.toolkit.srcgen.model.artifact.OprSpec;
import bh.toolkit.srcgen.model.artifact.VoSpec;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;
import bh.toolkit.srcgen.model.mybatis.ResultMap;
import bh.toolkit.srcgen.model.mybatis.Select;
import bh.toolkit.srcgen.model.mybatis.Sql;
import bh.toolkit.srcgen.model.rdb.ColumnSpec;
import bh.toolkit.srcgen.model.rdb.TableSpec;
import bh.toolkit.srcgen.model.src.AttributeSrc;
import bh.toolkit.srcgen.model.src.ClassSrc;
import bh.toolkit.srcgen.model.src.MethodSrc;
import bh.toolkit.srcgen.util.ExceptionUtil;
import bh.toolkit.srcgen.util.JavaFormatter;
import bh.toolkit.srcgen.util.MapperFormatter;
import bh.toolkit.srcgen.util.StringHelper;
import bh.toolkit.srcgen.util.WebPageFormatter;
import bh.toolkit.srcgen.util.WorkspaceUtil;

public class CtxCacheFacade {

	private static Logger logger = Logger.getLogger(CtxCacheFacade.class);
	private static CtxCache ctxCache = CtxCache.getInstance();

	public static void clearCache() {
		ctxCache.clearCache();
	}

	public static Enumeration<String> getTableSpecCacheKeys() {
		Hashtable<String, TableSpec> tableSpecCache = ctxCache.getTableSpecCache();
		Enumeration<String> keys = tableSpecCache.keys();
		return keys;
	}

	public static Enumeration<String> getVoCacheKeys() {
		Hashtable<String, ClassSrc> voCache = ctxCache.getVoCache();
		Enumeration<String> keys = voCache.keys();
		return keys;
	}

	public static Enumeration<String> getDtoCacheKeys() {
		Hashtable<String, ClassSrc> dtoCatch = ctxCache.getDtoCache();
		Enumeration<String> keys = dtoCatch.keys();
		return keys;
	}

	public static Enumeration<String> getDtoXCacheKeys() {
		Hashtable<String, ClassSrc> dtoXCatch = ctxCache.getDtoXCache();
		Enumeration<String> keys = dtoXCatch.keys();
		return keys;
	}

	public static Enumeration<String> getDaoIntfCacheKeys() {
		Hashtable<String, ClassSrc> daoIntfCache = ctxCache.getDaoIntfCache();
		Enumeration<String> keys = daoIntfCache.keys();
		return keys;
	}

	public static Enumeration<String> getDaoXIntfCacheKeys() {
		Hashtable<String, ClassSrc> daoXIntfCache = ctxCache.getDaoXIntfCache();
		Enumeration<String> keys = daoXIntfCache.keys();
		return keys;
	}

	public static Enumeration<String> getOprIntfCacheKeys() {
		Hashtable<String, ClassSrc> oprIntfCache = ctxCache.getOprIntfCache();
		Enumeration<String> keys = oprIntfCache.keys();
		return keys;
	}

	public static Enumeration<String> getOprImplCacheKeys() {
		Hashtable<String, ClassSrc> oprImplCache = ctxCache.getOprImplCache();
		Enumeration<String> keys = oprImplCache.keys();
		return keys;
	}

	public static Enumeration<String> getOprXIntfCacheKeys() {
		Hashtable<String, ClassSrc> oprXIntfCache = ctxCache.getOprXIntfCache();
		Enumeration<String> keys = oprXIntfCache.keys();
		return keys;
	}

	public static Enumeration<String> getOprXImplCacheKeys() {
		Hashtable<String, ClassSrc> oprXImplCache = ctxCache.getOprXImplCache();
		Enumeration<String> keys = oprXImplCache.keys();
		return keys;
	}

	public static Enumeration<String> getMgrIntfCacheKeys() {
		Hashtable<String, ClassSrc> mgrIntfCache = ctxCache.getMgrIntfCache();
		Enumeration<String> keys = mgrIntfCache.keys();
		return keys;
	}

	public static Enumeration<String> getMgrImplCacheKeys() {
		Hashtable<String, ClassSrc> mgrImplCache = ctxCache.getMgrImplCache();
		Enumeration<String> keys = mgrImplCache.keys();
		return keys;
	}

	public static Enumeration<String> getMgrXIntfCacheKeys() {
		Hashtable<String, ClassSrc> mgrXIntfCache = ctxCache.getMgrXIntfCache();
		Enumeration<String> keys = mgrXIntfCache.keys();
		return keys;
	}

	public static Enumeration<String> getMgrXImplCacheKeys() {
		Hashtable<String, ClassSrc> mgrXImplCache = ctxCache.getMgrXImplCache();
		Enumeration<String> keys = mgrXImplCache.keys();
		return keys;
	}

	public static Enumeration<String> getMsgCodeCacheKeys() {
		Hashtable<String, ClassSrc> msgCodeCache = ctxCache.getMsgCodeCache();
		Enumeration<String> keys = msgCodeCache.keys();
		return keys;
	}

	public static String lookupJavaDataType(String dbDataTypeName) {
		Hashtable<String, String> dataTypeMappingCache = ctxCache.getDataTypeMappingCache();
		String javaDataType = dataTypeMappingCache.get(dbDataTypeName);
		return javaDataType;
	}

	public static CommonAttrSpec getCommonAttrSpec() {
		return ctxCache.getCommonAttrSpec();
	}

	public static void setCommonAttrSpec(CommonAttrSpec commonAttrSpec) {
		ctxCache.setCommonAttrSpec(commonAttrSpec);
	}

	public static void addTableSpec(String tableFullName, TableSpec tableSpec) {
		Hashtable<String, TableSpec> tableSpecCache = ctxCache.getTableSpecCache();
		tableSpecCache.put(tableFullName, tableSpec);
	}

	public static TableSpec lookupTableSpec(String tableFullName) {
		Hashtable<String, TableSpec> tableSpecCache = ctxCache.getTableSpecCache();
		TableSpec tableSpec = tableSpecCache.get(tableFullName);
		return tableSpec;
	}

	public static void addResultMap(WorkspaceSpec workspaceSpec, DaoSpec daoSpec, String resultMapId, ResultMap resultMap) {

		// Get prefix for resultMap id.
		String globalId = MapperFormatter.getGlobalId(workspaceSpec, daoSpec, resultMapId);

		// Put resultMap element with global id.
		Hashtable<String, ResultMap> resultMapCache = ctxCache.getResultMapCache();
		resultMapCache.put(globalId, resultMap);

		logger.debug("REGISTER: Register a resultMap element with id '" + globalId + "'");

	}

	public static ResultMap lookupResultMap(WorkspaceSpec workspaceSpec, DaoSpec daoSpec, String resultMapId) {

		// Get ResultMap's global id.
		String globalId = MapperFormatter.getGlobalId(workspaceSpec, daoSpec, resultMapId);

		// Look up element with global id.
		Hashtable<String, ResultMap> resultMapCache = ctxCache.getResultMapCache();
		ResultMap resultMap = resultMapCache.get(globalId);

		if (resultMap == null) {
			logger.debug("NO RESULTMAP: No resultMap element found with id '" + globalId + "'");
		} else {
			logger.debug("FIND RESULTMAP: Find resultMap element with id '" + globalId + "'");
		}

		return resultMap;

	}

	public static void addSql(WorkspaceSpec workspaceSpec, DaoSpec daoSpec, String sqlId, Sql sql) {

		// Get prefix for 'sqlId'.
		String globalId = MapperFormatter.getGlobalId(workspaceSpec, daoSpec, sqlId);

		// Put 'sql' element with global id.
		Hashtable<String, Sql> sqlCache = ctxCache.getSqlCache();
		sqlCache.put(globalId, sql);

		logger.debug("REGISTER: Register a sql element with id '" + globalId + "'");

	}

	public static Sql lookupSql(WorkspaceSpec workspaceSpec, DaoSpec daoSpec, String sqlId) {

		// Get global id for sql.
		String globalId = MapperFormatter.getGlobalId(workspaceSpec, daoSpec, sqlId);

		// Look up element with global id.
		Hashtable<String, Sql> sqlCache = ctxCache.getSqlCache();
		Sql sql = sqlCache.get(globalId);

		if (sql == null) {
			logger.debug("NO SQL: No sql element found with id '" + globalId + "'");
		} else {
			logger.debug("FIND SQL: Find sql element with id '" + globalId + "'");
		}

		return sql;

	}

	public static void addSelect(WorkspaceSpec workspaceSpec, DaoSpec daoSpec, String selectId, Select select) {

		// Get prefix for select id.
		String globalId = MapperFormatter.getGlobalId(workspaceSpec, daoSpec, selectId);

		// Put select element with global id.
		Hashtable<String, Select> selectCache = ctxCache.getSelectCache();
		selectCache.put(globalId, select);

		logger.debug("REGISTER: Register a select element with id '" + globalId + "'");

	}

	public static Select lookupSelect(WorkspaceSpec workspaceSpec, DaoSpec daoSpec, String searchId) {

		// Get global id for select.
		String globalId = MapperFormatter.getGlobalId(workspaceSpec, daoSpec, searchId);

		// Look up element with global id.
		Hashtable<String, Select> selectCache = ctxCache.getSelectCache();
		Select select = selectCache.get(globalId);

		if (select == null) {
			logger.debug("NO SELECT: No select element found with id '" + globalId + "'");
		} else {
			logger.debug("FIND SELECT: Find select element with id '" + globalId + "'");
		}

		return select;

	}

	public static void addVoClass(WorkspaceSpec workspaceSpec, List<VoSpec> voSpecList, String skipTableNamePat) throws AppException {

		// 从缓存中获取所有TableSpec的key
		Enumeration<String> tableSpecKeys = getTableSpecCacheKeys();

		String pkgName = null;
		TableSpec tableSpec = null;
		String tableFullName = null;
		DBTableSpec dbTableSpec = null;
		int voCnt = 0;
		Pattern pattern = Pattern.compile(skipTableNamePat);
		Matcher matcher = null;

		while (tableSpecKeys.hasMoreElements() == true) {

			tableSpec = lookupTableSpec(tableSpecKeys.nextElement());

			// 判断匹配条件，针对符合条件的TableSpec，生成VO并保存到缓存。schema和dbTableSpec所提出的条件都需要满足
			for (VoSpec voSpec : voSpecList) {

				// 如果Schema不必配则跳过
				pkgName = voSpec.getPkgName();
				if (tableSpec.getSchema().equalsIgnoreCase(pkgName) == false) {
					continue;
				}

				// 如果DB表名符合给定格式，则跳过
				tableFullName = tableSpec.getFullName();
				matcher = pattern.matcher(tableFullName);
				if (matcher.matches() == true) {
					logger.info("SKIP TABLE: Table name \"" + tableFullName + "\" matches the given pattern: \"" + skipTableNamePat + "\", skip.");
					continue;
				}

				dbTableSpec = voSpec.getDbTableSpec();
				if (dbTableSpec != null) {
					if (tableFullName.equalsIgnoreCase(dbTableSpec.getFullName()) == false) { // 如果当前tableSpec的表名与dbTableSpec的表名不匹配则跳过
						continue;
					}
				}

				addVoClass(workspaceSpec, tableFullName);
				voCnt++;

			}

		}

		logger.info("【VO TOTAL】 There are totally " + voCnt + " tableSpec(s) with the pkgName \"" + pkgName + "\" were transformed into VO.");

	}

	public static ClassSrc addVoClass(WorkspaceSpec workspaceSpec, String tableFullName) throws AppException {

		// Look up TableSpec from cache.
		TableSpec tableSpec = lookupTableSpec(tableFullName);
		if (tableSpec == null) {
			logger.error("!!! NO TABLE FOUND: No table found with table name \"" + tableFullName + "\"");
			return null;
		}

		// Look up ClassSrc from cache.
		String voFullName = JavaFormatter.getVoFullName(workspaceSpec, tableFullName);
		String voSimpleName = JavaFormatter.getClassSimpleName(voFullName);
		ClassSrc voClass = lookupVoClass(voFullName);
		if (voClass != null) {
			logger.debug("VO FOUND: Found VO class with name \"" + voFullName + "\"");
			return voClass;
		}
		logger.debug("VO NEW: Add VO class with name \"" + voFullName + "\"");

		voClass = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_VO);
		ctxCache.getVoCache().put(voFullName, voClass);
		voClass.setPkgName(JavaFormatter.getClassPkgName(voFullName));
		voClass.setSimpleName(voSimpleName);
		voClass.setFullName(voFullName);
		voClass.setComments(tableSpec.getComments());
		voClass.setFileName(JavaFormatter.getVoAbsoluteName(workspaceSpec, tableFullName));

		List<ColumnSpec> columnList = tableSpec.getColumnList();
		String columnName = null;
		String attrName = null;
		// String attrLowerCaseName = null;
		Pattern cdPat = Pattern.compile(JavaSrcElm.ENUM_CODE_PAT, Pattern.CASE_INSENSITIVE);
		Pattern nmPat = Pattern.compile(JavaSrcElm.ENUM_NAME_PAT, Pattern.CASE_INSENSITIVE);
		Matcher cdMatcher = null;
		Matcher nmMatcher = null;
		String cdAttrName = null;
		// String nmCnAttrName = null;
		// String nmEnAttrName = null;
		String javaDataType = null;
		String dataTypeSimpleName = null;

		// Define attributes and corresponding methods.
		AttributeSrc voAttr = null;
		AttributeSrc shadowAttr = null;
		int setCatSysCdFlag = 0;
		for (ColumnSpec columnSpec : columnList) {

			// Create a new voAttr.
			columnName = columnSpec.getName();
			attrName = JavaFormatter.getJavaStyle(columnName, false, false);
			javaDataType = lookupJavaDataType(columnSpec.getJdbcTypeName());
			if (StringUtils.isBlank(javaDataType) == true) {
				throw new AppException("!!! NO FOUND: Not found column data type \"" + columnSpec.getJdbcTypeName() + "\" for column: \"" + columnName + "\".");
			}
			// Judge whether put the paramTypeName into import.
			voClass.addImport(javaDataType);
			dataTypeSimpleName = JavaFormatter.getClassSimpleName(javaDataType);

			voAttr = new AttributeSrc();
			voAttr.setModifier(JavaSrcElm.PRIVATE);
			voAttr.setDataType(dataTypeSimpleName);
			voAttr.setName(attrName);
			voAttr.addObjComments(columnSpec.getComments() + WebPageFormatter.getIdAndName(StringHelper.toLowerCase(voSimpleName, 0), attrName));
			voAttr.setDefaultValue(JavaSrcElm.NULL);
			voClass.addAttr(voAttr, true); // Put the new voAttr into current ClassSrc.
			if (columnSpec.isPrimaryKey() == true) {
				voAttr.setPrimaryKey(true);
				voClass.setPkAttr(attrName);
				// 针对主键，构建String类型的用来表示多个主键的属性
				shadowAttr = new AttributeSrc();
				shadowAttr.setModifier(JavaSrcElm.PRIVATE);
				shadowAttr.setDataType(JavaSrcElm.STRING);
				shadowAttr.setName(attrName + JavaSrcElm.VO_ATTR_SHADOW_SUFFIX);
				shadowAttr.addObjComments(
						columnSpec.getComments() + WebPageFormatter.getIdAndName(StringHelper.toLowerCase(voSimpleName, 0), attrName + JavaSrcElm.VO_ATTR_SHADOW_SUFFIX));
				shadowAttr.setDefaultValue(JavaSrcElm.NULL);
				voClass.addAttr(shadowAttr, true);
			}

			// 针对DATE和TIMESTAMP的数据类型，构建String类型的“影子”属性
			if (JavaSrcElm.JDBC_TYPE_DATETIME.equals(columnSpec.getJdbcTypeName()) || JavaSrcElm.JDBC_TYPE_TIMESTAMP.equals(columnSpec.getJdbcTypeName())
					|| JavaSrcElm.JDBC_TYPE_DATE.equals(columnSpec.getJdbcTypeName())) {
				shadowAttr = new AttributeSrc();
				shadowAttr.setModifier(JavaSrcElm.PRIVATE);
				shadowAttr.setDataType(JavaSrcElm.STRING);
				shadowAttr.setName(attrName + JavaSrcElm.VO_ATTR_SHADOW_SUFFIX);
				shadowAttr.addObjComments(columnSpec.getComments() + JavaSrcElm.VO_ATTR_SHADOW_COMMENTS
						+ WebPageFormatter.getIdAndName(StringHelper.toLowerCase(voSimpleName, 0), attrName + JavaSrcElm.VO_ATTR_SHADOW_SUFFIX));
				shadowAttr.setDefaultValue(JavaSrcElm.NULL);
				voClass.addAttr(shadowAttr, true);
			}

			// 当发现连续的 "cd", "nmCn", and "nmEn"
			cdMatcher = cdPat.matcher(attrName);
			nmMatcher = nmPat.matcher(attrName);
			if (cdMatcher.matches() == true) {
				cdAttrName = attrName;
				setCatSysCdFlag++;
			} else if (nmMatcher.matches() == true) {
				setCatSysCdFlag++;
			} else {
				setCatSysCdFlag = 0;
				cdAttrName = null;
			}

			// 当"cd"、"nmCn"、"nmEn"中至少两个出现，且一定包含"cd"时，增加对枚举类的import声明。
			if (setCatSysCdFlag >= 2 && StringUtils.isNotBlank(cdAttrName) == true) {
				voClass.addImport(JavaFormatter.getCatSysCodeFullName(workspaceSpec));
				// Reset the flag.
				setCatSysCdFlag = 0;
				cdAttrName = null;
			}

		}

		return voClass;

	}

	public static ClassSrc lookupVoClass(String voClassName) {
		Hashtable<String, ClassSrc> voClassCache = ctxCache.getVoCache();
		ClassSrc voClass = voClassCache.get(voClassName);
		return voClass;
	}

	/**
	 * 增加DTO的属性，如果DTO不存在，则先增加DTO
	 * 
	 * @param artifactSpec
	 * @param rootTableName
	 * @param attrTableName
	 * @param attrRefName
	 * @param isOneToMany
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IOException
	 * @throws AppException
	 */
	public static ClassSrc addDtoAttr(ArtifactSpec artifactSpec, String rootTableName, String attrTableName, String attrRefName, String verb, boolean isOneToMany)
			throws AppException {

		// Get global naming and local setting.
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		TableSpec tableSpec = null;
		String dtoFullName = JavaFormatter.getDtoFullName(workspaceSpec, rootTableName);
		ClassSrc dtoClass = lookupDtoClass(dtoFullName);

		try {

			// 判断DTO是否已经存在于缓存中
			if (dtoClass == null) {

				logger.debug("NO DTO: No DTO found with name: " + dtoFullName + ", create new.");

				// 初始化一个DTO并注册
				dtoClass = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_DTO);
				ctxCache.getDtoCache().put(dtoFullName, dtoClass);
				dtoClass.setPkgName(JavaFormatter.getClassPkgName(dtoFullName));
				dtoClass.setSimpleName(JavaFormatter.getClassSimpleName(dtoFullName));
				dtoClass.setFullName(dtoFullName);
				String dtoAbsoluteName = JavaFormatter.getDtoAbsoluteName(workspaceSpec, rootTableName);
				dtoClass.setFileName(dtoAbsoluteName);

				// 从文件系统中扫描DTO，如果发现则导入已经存在其中的属性
				ClassSrcImporter.importDtoAttr(dtoAbsoluteName, dtoClass);

				// 增加对BaseDto的继承和import
				dtoClass.setExtendsList(Arrays.asList(JavaSrcElm.BASE_DTO_SIMPLE));
				dtoClass.addImport(JavaFormatter.getBaseDtoFullName(workspaceSpec));

				// 增加根VO属性的类型声明，以及对其的import
				AttributeSrc rootVoAttr = new AttributeSrc();
				rootVoAttr.setModifier(JavaSrcElm.PROTECTED);
				String rootVoFullName = JavaFormatter.getVoFullName(workspaceSpec, rootTableName);
				String rootVoSimpleName = JavaFormatter.getClassSimpleName(rootVoFullName);
				rootVoAttr.setDataType(rootVoSimpleName);
				// RootVO的属性名不需要采用提供的attrRefName，直接根据Root表的名称进行命名
				rootVoAttr.setName(StringHelper.toLowerCase(rootVoSimpleName, 0));
				// RootVO的Constructor也可以直接命名
				rootVoAttr.setDefaultValue(JavaSrcElm.NEW + JavaSrcElm.WHITE_SPACE + rootVoSimpleName + JavaSrcElm.CLOSE_PARENTHESIS);
				// RootVo的注释
				tableSpec = lookupTableSpec(rootTableName);
				if (tableSpec == null) {
					logger.error("!!! NO TABLE FOUND: No table found with table name \"" + rootTableName + "\"");
					return null;
				}
				rootVoAttr.addVerbComments(MapperElm.LEFT_SQUARE_BRACKET + verb + MapperElm.RIGHT_SQUARE_BRACKET);
				rootVoAttr.addObjComments(tableSpec.getComments());
				dtoClass.addAttr(rootVoAttr, true);
				dtoClass.addImport(rootVoFullName);
				addVoClass(workspaceSpec, rootTableName);

			} else {
				logger.debug("FOUND DTO: Found DTO with name: " + dtoFullName);
			}

			// 如果没有提供属性表，则不必再继续分析处理。
			if (StringUtils.isBlank(attrTableName) == true) {
				return dtoClass;
			}

			// 如果是oneToMany，则提供的attrTableName对应的类是DTOX的List，否则提供的attrTableName对应的类是VO
			String attrFullName = null;
			String attrSimpleName = null;
			String attrDataType = null;
			AttributeSrc attrSrc = new AttributeSrc();
			if (isOneToMany == true) {
				attrFullName = JavaFormatter.getDtoXFullName(workspaceSpec, attrTableName);
				attrSimpleName = JavaFormatter.getClassSimpleName(attrFullName);
				attrDataType = JavaSrcElm.UTIL_LIST_SIMPLE + JavaSrcElm.LESS_THAN + attrSimpleName + JavaSrcElm.LARGER_THAN;
				// 如果提供的属性变量名为空，则采用由attrTable得出的名称
				if (StringUtils.isBlank(attrRefName) == true) {
					attrSrc.setName(StringHelper.toLowerCase(attrSimpleName, 0) + JavaSrcElm.UTIL_LIST_SIMPLE);
				} else {
					attrSrc.setName(attrRefName);
				}
				attrSrc.setDefaultValue(JavaSrcElm.NEW + JavaSrcElm.WHITE_SPACE + JavaSrcElm.UTIL_ARRAYLIST_SIMPLE + JavaSrcElm.LESS_THAN + attrSimpleName + JavaSrcElm.LARGER_THAN
						+ JavaSrcElm.CLOSE_PARENTHESIS);
				// 子VO的注释
				tableSpec = lookupTableSpec(attrTableName);
				if (tableSpec == null) {
					logger.error("!!! NO TABLE FOUND: No table found with table name \"" + attrTableName + "\"");
					return null;
				}
				attrSrc.addVerbComments(MapperElm.LEFT_SQUARE_BRACKET + verb + MapperElm.RIGHT_SQUARE_BRACKET);
				attrSrc.addObjComments(tableSpec.getComments());
				dtoClass.addImport(JavaSrcElm.UTIL_LIST_FULL);
				dtoClass.addImport(JavaSrcElm.UTIL_ARRAYLIST_FULL);
				dtoClass.addImport(attrFullName);
				// 针对一对多的情况，需要增加对应子表的DTOX
				addDtoXClass(artifactSpec, attrTableName);
			} else {
				attrFullName = JavaFormatter.getVoFullName(workspaceSpec, attrTableName);
				attrSimpleName = JavaFormatter.getClassSimpleName(attrFullName);
				attrDataType = attrSimpleName;
				// 如果提供的属性变量名为空，则采用由attrTable得出的名称
				if (StringUtils.isBlank(attrRefName) == true) {
					attrSrc.setName(StringHelper.toLowerCase(attrSimpleName, 0));
				} else {
					attrSrc.setName(attrRefName);
				}
				if (verb.equalsIgnoreCase(JavaSrcElm.VERB_CHANGE) == true) { // 谓词是"change"时，注册的VO必然是newXXX，此时默认值设置为null
					attrSrc.setDefaultValue(JavaSrcElm.NULL);
				} else {
					attrSrc.setDefaultValue(JavaSrcElm.NEW + JavaSrcElm.WHITE_SPACE + attrSimpleName + JavaSrcElm.CLOSE_PARENTHESIS);
				}
				// 子DTOX列表的注释
				tableSpec = lookupTableSpec(attrTableName);
				if (tableSpec == null) {
					logger.error("!!! NO TABLE FOUND: No table found with table name \"" + attrTableName + "\"");
					return null;
				}
				attrSrc.addVerbComments(MapperElm.LEFT_SQUARE_BRACKET + verb + MapperElm.RIGHT_SQUARE_BRACKET);
				attrSrc.addObjComments(tableSpec.getComments());
				dtoClass.addImport(attrFullName);
				// 针对一对一的情况，需要增加对应子表的VO
				addVoClass(workspaceSpec, attrTableName);
			}

			attrSrc.setModifier(JavaSrcElm.PROTECTED);
			attrSrc.setDataType(attrDataType);
			dtoClass.addAttr(attrSrc, true);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return dtoClass;

	}

	public static ClassSrc lookupDtoClass(String dtoClassFullName) {
		Hashtable<String, ClassSrc> dtoClassCache = ctxCache.getDtoCache();
		ClassSrc dtoClass = dtoClassCache.get(dtoClassFullName);
		return dtoClass;
	}

	/**
	 * 增加DTOX的属性，如果DTOX不存在，则先增加DTOX
	 * 
	 * @param artifactSpec
	 * @param rootTableName
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IOException
	 * @throws AppException
	 */
	public static ClassSrc addDtoXClass(ArtifactSpec artifactSpec, String rootTableName) throws AppException {

		// Get global naming and local setting.
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		String dtoFullName = JavaFormatter.getDtoFullName(workspaceSpec, rootTableName);
		String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, rootTableName);
		ClassSrc dtoXClass = lookupDtoXClass(dtoXFullName);

		try {
			// 判断DTOX是否已经存在于缓存中， 如果不存在于缓存中，则初始化一个DTOX模型
			if (dtoXClass == null) {

				logger.debug("NO DTOX: No DTOX found with name: " + dtoXFullName + ", create new.");
				String dtoXAbsoluteName = JavaFormatter.getDtoXAbsoluteName(workspaceSpec, rootTableName);
				dtoXClass = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_DTO);
				ctxCache.getDtoXCache().put(dtoXFullName, dtoXClass);
				dtoXClass.setPkgName(JavaFormatter.getClassPkgName(dtoXFullName));
				dtoXClass.setSimpleName(JavaFormatter.getClassSimpleName(dtoXFullName));
				dtoXClass.setFullName(dtoXFullName);
				dtoXClass.setFileName(dtoXAbsoluteName);
				// 增加import和需要继承的DTO
				dtoXClass.setExtendsList(Arrays.asList(JavaFormatter.getClassSimpleName(dtoFullName)));
				dtoXClass.addImport(dtoFullName);
				// 在增加DTO的同时增加对应根VO
				addDtoAttr(artifactSpec, rootTableName, null, null, MapperElm.SQL_SELECT, false);

				// 从文件系统中扫描DTOX，如果发现，则标记为已存在的文件
				if (FileHandler.isFile(dtoXAbsoluteName) == true) {
					dtoXClass.setExistingFile(true);
				}

			} else {
				logger.debug("FOUND DTOX: Found DTOX with name: " + dtoFullName);
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return dtoXClass;

	}

	public static ClassSrc lookupDtoXClass(String dtoXClassFullName) {
		Hashtable<String, ClassSrc> dtoXClassCache = ctxCache.getDtoXCache();
		ClassSrc dtoXClass = dtoXClassCache.get(dtoXClassFullName);
		return dtoXClass;
	}

	public static MethodSrc addDaoMethod(ArtifactSpec artifactSpec, String daoSpecShortName, String methodName, List<String> paramTypeNameList, String returnTypeName,
			String comments) throws AppException {

		// Get global naming and local setting.
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		MethodSrc daoIntfMeth = null;

		try {

			// The DAO package name is identical to the mapper namespace.
			String daoFullName = JavaFormatter.getDaoFullName(workspaceSpec, daoSpecShortName);
			String daoSimpleName = JavaFormatter.getClassSimpleName(daoFullName);
			String daoPkgName = JavaFormatter.getClassPkgName(daoFullName);
			String daoFileName = JavaFormatter.classNameToFileName(daoFullName);
			String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getOrmPrj()) + daoFileName + JavaSrcElm.JAVA_FILE_EXT;

			// Look up existing DAO interface from daoIntfCache.
			ClassSrc daoIntf = lookupDaoIntf(daoFullName);
			// If not found, then initiate a new ClassSrc.
			if (daoIntf == null) {
				logger.debug("NO DAO: No DAO interface found with name: " + daoFullName + ", create new.");
				// Put new DAO interface into cache.
				daoIntf = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_DAO_INTF);
				daoIntf.setPkgName(daoPkgName);
				daoIntf.setComments(JavaSrcElm.WARN_STMT_COMMENTS);
				daoIntf.setSimpleName(daoSimpleName);
				daoIntf.setFullName(daoFullName);
				daoIntf.setFileName(absoluteName);
				ctxCache.getDaoIntfCache().put(daoFullName, daoIntf);
				// 增加对应的DAOX
				addDaoXIntf(artifactSpec, daoSpecShortName);
			} else {
				logger.debug("FOUND DAO: Found DAO interface with name: " + daoFullName);
			}

			// Initiate new MethSrc and put them into ClassSrc.
			daoIntfMeth = new MethodSrc();
			daoIntfMeth.setName(methodName);
			daoIntf.addMethod(methodName, daoIntfMeth);

			// Judge the type of parameter and return.
			String paramTypeName = null;
			for (int i = 0; i < paramTypeNameList.size(); i++) {
				paramTypeName = paramTypeNameList.get(i);
				// Judge whether put the paramTypeName into import.
				daoIntf.addImport(paramTypeName);
				// Handle every parameter for method.
				daoIntfMeth.addParam(JavaFormatter.getClassSimpleName(paramTypeName), null);
			}

			// Handle return type for method.
			daoIntf.addImport(returnTypeName);
			daoIntfMeth.setReturnType(JavaFormatter.getClassSimpleName(returnTypeName));
			daoIntfMeth.setComments(comments);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return daoIntfMeth;

	}

	public static ClassSrc lookupDaoIntf(String daoIntfFullName) {
		Hashtable<String, ClassSrc> daoIntfCache = ctxCache.getDaoIntfCache();
		ClassSrc daoIntf = daoIntfCache.get(daoIntfFullName);
		return daoIntf;
	}

	public static ClassSrc addDaoXIntf(ArtifactSpec artifactSpec, String daoSpecShortName) throws IOException, AppException {

		// Get global naming and local setting.
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		// 判断DAOX是否已经存在于缓存中
		String daoFullName = JavaFormatter.getDaoFullName(workspaceSpec, daoSpecShortName);
		String daoXFullName = JavaFormatter.getDaoXFullName(workspaceSpec, daoSpecShortName);
		ClassSrc daoXIntf = lookupDaoXIntf(daoXFullName);

		// 如果不存在于缓存中，则初始化一个DAOX模型
		if (daoXIntf == null) {

			logger.debug("NO DAOX: No DAOX found with name: " + daoXFullName + ", create new.");

			// 如果不存在于缓存中，则初始化一个DAOX模型
			daoXIntf = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_DAO_INTF);
			ctxCache.getDaoXIntfCache().put(daoXFullName, daoXIntf);
			String absoluteName = JavaFormatter.getDaoXAbsoluteName(workspaceSpec, daoSpecShortName);
			daoXIntf.setPkgName(JavaFormatter.getClassPkgName(daoXFullName));
			daoXIntf.setSimpleName(JavaFormatter.getClassSimpleName(daoXFullName));
			// 增加import和需要继承的DAO
			daoXIntf.setExtendsList(Arrays.asList(JavaFormatter.getClassSimpleName(daoFullName)));
			daoXIntf.setFullName(daoXFullName);
			daoXIntf.setFileName(absoluteName);
			daoXIntf.addImport(daoFullName);

			// 从文件系统中扫描DAOX，如果发现，则标记为已存在的文件
			if (FileHandler.isFile(absoluteName) == true) {
				daoXIntf.setExistingFile(true);
			}

		} else {
			logger.debug("FOUND DAOX: Found DAOX with name: " + daoXFullName);
		}

		return daoXIntf;

	}

	public static ClassSrc lookupDaoXIntf(String daoXIntfFullName) {
		Hashtable<String, ClassSrc> daoXIntfCache = ctxCache.getDaoXIntfCache();
		ClassSrc daoXClass = daoXIntfCache.get(daoXIntfFullName);
		return daoXClass;
	}

	public static MethodSrc addOprIntfMethod(ArtifactSpec artifactSpec, OprSpec oprSpec, String methodName, List<String> paramTypeNameList, String returnTypeName, String comments)
			throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		MethodSrc oprIntfMeth = null;

		try {

			// // 根据OPR的shortName计算DAOX的名字
			// String daoXFullName =
			// JavaFormatter.getDaoXFullName(workspaceSpec,
			// oprSpec.getShortName());
			// 计算与OPR接口相关的名称
			String oprIntfFullName = JavaFormatter.getOprIntfFullName(workspaceSpec, oprSpec.getShortName());
			String oprIntfSimpleName = JavaFormatter.getClassSimpleName(oprIntfFullName);
			String oprIntfPkgName = JavaFormatter.getClassPkgName(oprIntfFullName);
			String oprIntfFileName = JavaFormatter.classNameToFileName(oprIntfFullName);
			String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getOrmPrj()) + oprIntfFileName + JavaSrcElm.JAVA_FILE_EXT;

			// Look up existing OPR interface.
			ClassSrc oprIntf = lookupOprIntf(oprIntfFullName);
			// If not found, then initiate a new ClassSrc.
			if (oprIntf == null) {
				logger.debug("NO OPR: No OPR interface found with name: " + oprIntfFullName + ", create new.");
				// Put new DAO interface into cache.
				oprIntf = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_OPR_INTF);
				oprIntf.setPkgName(oprIntfPkgName);
				oprIntf.setComments(JavaSrcElm.WARN_STMT_COMMENTS);
				oprIntf.setSimpleName(oprIntfSimpleName);
				oprIntf.setFullName(oprIntfFullName);
				oprIntf.setFileName(absoluteName);
				// 增加对应的接口和实现
				ctxCache.getOprIntfCache().put(oprIntfFullName, oprIntf);
				addOprXIntf(artifactSpec, oprSpec.getShortName());
			} else {
				logger.debug("FOUND OPR: Found OPR interface with name: " + oprIntfFullName);
			}

			// Initiate new MethSrc and put them into ClassSrc.
			oprIntfMeth = new MethodSrc();
			oprIntfMeth.setName(methodName);
			oprIntf.addMethod(methodName, oprIntfMeth);

			// Judge the type of parameter and return.
			String paramTypeName = null;
			for (int i = 0; i < paramTypeNameList.size(); i++) {
				paramTypeName = paramTypeNameList.get(i);
				// Judge whether put the paramTypeName into import.
				oprIntf.addImport(paramTypeName);
				// Handle every parameter for method.
				oprIntfMeth.addParam(JavaFormatter.getClassSimpleName(paramTypeName), null);
			}

			// 方法的返回值和异常
			String appExpFullName = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.APP_EXCEPTION_FULL;
			oprIntfMeth.setReturnType(JavaFormatter.getClassSimpleName(returnTypeName));
			oprIntfMeth.setReturnName("the" + JavaFormatter.getClassSimpleName(returnTypeName));
			oprIntfMeth.addException(appExpFullName);
			oprIntfMeth.setComments(comments);
			oprIntf.addImport(returnTypeName);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return oprIntfMeth;

	}

	public static ClassSrc lookupOprIntf(String oprIntfFullName) {
		Hashtable<String, ClassSrc> oprIntfCache = ctxCache.getOprIntfCache();
		ClassSrc oprIntf = oprIntfCache.get(oprIntfFullName);
		return oprIntf;
	}

	public static ClassSrc addOprXIntf(ArtifactSpec artifactSpec, String oprSpecShortName) throws IOException, AppException {

		// Get global naming and local setting.
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		// 判断OPRX是否已经存在于缓存中
		String oprIntfFullName = JavaFormatter.getOprIntfFullName(workspaceSpec, oprSpecShortName);
		String oprXIntfFullName = JavaFormatter.getOprXIntfFullName(workspaceSpec, oprSpecShortName);
		ClassSrc oprXIntf = lookupDaoXIntf(oprXIntfFullName);

		// 如果不存在于缓存中，则初始化一个OPRX模型
		if (oprXIntf == null) {

			logger.debug("NO OPRX: No OPRX interface found with name: " + oprXIntfFullName + ", create new.");

			// 如果不存在于缓存中，则初始化一个OPRX模型
			oprXIntf = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_OPR_INTF);
			ctxCache.getOprXIntfCache().put(oprXIntfFullName, oprXIntf);
			String absoluteName = JavaFormatter.getOprXIntfAbsoluteName(workspaceSpec, oprSpecShortName);
			oprXIntf.setPkgName(JavaFormatter.getClassPkgName(oprXIntfFullName));
			oprXIntf.setSimpleName(JavaFormatter.getClassSimpleName(oprXIntfFullName));
			oprXIntf.setFullName(oprXIntfFullName);
			oprXIntf.setFileName(absoluteName);
			// 增加import和需要继承的OPR
			oprXIntf.setExtendsList(Arrays.asList(JavaFormatter.getClassSimpleName(oprIntfFullName)));
			oprXIntf.addImport(oprIntfFullName);

			// 从文件系统中扫描OPRX，如果发现，则标记为已存在的文件
			if (FileHandler.isFile(absoluteName) == true) {
				oprXIntf.setExistingFile(true);
			}

		} else {
			logger.debug("FOUND OPRX: Found OPRX interface with name: " + oprXIntfFullName);
		}

		return oprXIntf;

	}

	public static ClassSrc lookupOprXIntf(String oprXIntfFullName) {
		Hashtable<String, ClassSrc> oprXIntfCache = ctxCache.getOprXIntfCache();
		ClassSrc oprXIntf = oprXIntfCache.get(oprXIntfFullName);
		return oprXIntf;
	}

	public static MethodSrc addOprImplMethod(ArtifactSpec artifactSpec, OprSpec oprSpec, String methodName, List<String> paramTypeList, String returnType, String comments)
			throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		MethodSrc oprImplMeth = null;

		try {

			// 根据OPR的shortName计算OPR interface的名字
			String oprIntfFullName = JavaFormatter.getOprIntfFullName(workspaceSpec, oprSpec.getShortName());
			// 计算与OPR实现相关的名称
			String oprImplFullName = JavaFormatter.getOprImplFullName(workspaceSpec, oprSpec.getShortName());
			String oprImplSimpleName = JavaFormatter.getClassSimpleName(oprImplFullName);
			String oprImplPkgName = JavaFormatter.getClassPkgName(oprImplFullName);
			String oprImplFileName = JavaFormatter.classNameToFileName(oprImplFullName);
			String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getOrmPrj()) + oprImplFileName + JavaSrcElm.JAVA_FILE_EXT;

			// Look up existing OPR implementation.
			ClassSrc oprImpl = lookupOprImpl(oprImplFullName);
			// If not found, then initiate a new ClassSrc.
			if (oprImpl == null) {
				logger.debug("NO OPR: No OPR implementation found with name: " + oprImplFullName + ", create new.");
				// Put new DAO interface into cache.
				oprImpl = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_OPR_IMPL);
				oprImpl.setPkgName(oprImplPkgName);
				oprImpl.addImport(JavaFormatter.getCmCatSysCodeFullName(workspaceSpec));
				oprImpl.addImport(JavaFormatter.getPtyCatSysCodeFullName(workspaceSpec));
				oprImpl.addImport(oprIntfFullName);
				oprImpl.addImport(JavaFormatter.getBaseOprFullName(workspaceSpec));
				oprImpl.setComments(JavaSrcElm.WARN_STMT_COMMENTS);
				oprImpl.setSimpleName(oprImplSimpleName);
				oprImpl.setImplementsList(Arrays.asList(JavaFormatter.getClassSimpleName(oprIntfFullName)));
				oprImpl.setExtendsList(Arrays.asList(JavaSrcElm.BASE_OPR_SIMPLE));
				oprImpl.setFullName(oprImplFullName);
				oprImpl.setFileName(absoluteName);
				ctxCache.getOprImplCache().put(oprImplFullName, oprImpl);
				// 增加对应的OPRX实现
				addOprXImpl(artifactSpec, oprSpec.getShortName());
			} else {
				logger.debug("FOUND OPR: Found OPR implementation with name: " + oprImplFullName);
			}

			// Initiate new MethSrc and put them into ClassSrc.
			oprImplMeth = new MethodSrc();
			oprImplMeth.setName(methodName);
			oprImpl.addMethod(methodName, oprImplMeth);

			// Judge the type of parameter and return.
			String paramType = null;
			for (int i = 0; i < paramTypeList.size(); i++) {
				paramType = paramTypeList.get(i);
				// Judge whether put the paramTypeName into import.
				oprImpl.addImport(paramType);
				// Handle every parameter for method.
				oprImplMeth.addParam(JavaFormatter.getClassSimpleName(paramType), null);
			}

			// 方法的返回值和异常
			String appExpFullName = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.APP_EXCEPTION_FULL;
			oprImplMeth.setReturnType(JavaFormatter.getClassSimpleName(returnType));
			oprImplMeth.setReturnName("the" + StringHelper.toUpperCase(JavaFormatter.getClassSimpleName(returnType), 0));
			oprImplMeth.addException(appExpFullName);
			oprImplMeth.setComments(comments);
			oprImpl.addImport(returnType);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return oprImplMeth;

	}

	public static ClassSrc lookupOprImpl(String oprImplFullName) {
		Hashtable<String, ClassSrc> oprImplCache = ctxCache.getOprImplCache();
		ClassSrc oprImpl = oprImplCache.get(oprImplFullName);
		return oprImpl;
	}

	public static ClassSrc addOprXImpl(ArtifactSpec artifactSpec, String oprSpecShortName) throws IOException, AppException {

		// Get global naming and local setting.
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		// 判断OPRX是否已经存在于缓存中
		String oprXIntfFullName = JavaFormatter.getOprXIntfFullName(workspaceSpec, oprSpecShortName);
		String oprImplFullName = JavaFormatter.getOprImplFullName(workspaceSpec, oprSpecShortName);
		String oprXImplFullName = JavaFormatter.getOprXImplFullName(workspaceSpec, oprSpecShortName);
		ClassSrc oprXImpl = lookupOprXImpl(oprXImplFullName);

		// 如果不存在于缓存中，则初始化一个OPRX模型
		if (oprXImpl == null) {

			logger.debug("NO OPRX: No OPRX implementation found with name: " + oprImplFullName + ", create new.");

			// 如果不存在于缓存中，则初始化一个OPRX模型
			oprXImpl = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_OPR_IMPL);
			ctxCache.getOprXImplCache().put(oprXImplFullName, oprXImpl);
			String absoluteName = JavaFormatter.getOprXImplAbsoluteName(workspaceSpec, oprSpecShortName);
			oprXImpl.setPkgName(JavaFormatter.getClassPkgName(oprXImplFullName));
			oprXImpl.setSimpleName(JavaFormatter.getClassSimpleName(oprXImplFullName));
			oprXImpl.setFullName(oprXImplFullName);
			oprXImpl.setFileName(absoluteName);
			// 增加import和需要继承的OPR
			oprXImpl.setImplementsList(Arrays.asList(JavaFormatter.getClassSimpleName(oprXIntfFullName)));
			oprXImpl.setExtendsList(Arrays.asList(JavaFormatter.getClassSimpleName(oprImplFullName)));
			oprXImpl.addImport(oprImplFullName);

			// 从文件系统中扫描OPRX，如果发现，则标记为已存在的文件
			if (FileHandler.isFile(absoluteName) == true) {
				oprXImpl.setExistingFile(true);
			}

		} else {
			logger.debug("FOUND OPRX: Found OPRX implementation with name: " + oprXImplFullName);
		}

		return oprXImpl;

	}

	public static ClassSrc lookupOprXImpl(String oprXImplFullName) {
		Hashtable<String, ClassSrc> oprXImplCache = ctxCache.getOprXImplCache();
		ClassSrc oprXImpl = oprXImplCache.get(oprXImplFullName);
		return oprXImpl;
	}

	public static ClassSrc addMgrIntf(ArtifactSpec artifactSpec, String mgrSpecShortName) throws IOException, AppException {

		// Get global naming and local setting.
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		// 判断mgrIntf是否已经存在于缓存中
		String mgrIntfFullName = JavaFormatter.getMgrIntfFullName(workspaceSpec, mgrSpecShortName);
		ClassSrc mgrIntf = lookupMgrXIntf(mgrIntfFullName);

		// 如果不存在于缓存中，则初始化一个MGR模型
		if (mgrIntf == null) {

			logger.debug("NO MGR: No MGR interface found with name: " + mgrIntfFullName + ", create new.");

			// 如果不存在于缓存中，则初始化一个MGR模型
			mgrIntf = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_MGR_INTF);
			ctxCache.getMgrIntfCache().put(mgrIntfFullName, mgrIntf);
			String absoluteName = JavaFormatter.getMgrIntfAbsoluteName(workspaceSpec, mgrSpecShortName);
			mgrIntf.setPkgName(JavaFormatter.getClassPkgName(mgrIntfFullName));
			mgrIntf.setComments(JavaSrcElm.WARN_STMT_COMMENTS + JavaSrcElm.LINE_SEPARATOR + JavaSrcElm.LINE_COMMENT + JavaSrcElm.WHITE_SPACE + JavaSrcElm.MGR_VERB_DESC
					+ JavaSrcElm.WHITE_SPACE);
			mgrIntf.setSimpleName(JavaFormatter.getClassSimpleName(mgrIntfFullName));
			mgrIntf.setFullName(mgrIntfFullName);
			mgrIntf.setFileName(absoluteName);

		} else {
			logger.debug("FOUND MGR: Found MGR interface with name: " + mgrIntfFullName);
		}

		return mgrIntf;

	}

	public static MethodSrc addMgrIntfMethod(ArtifactSpec artifactSpec, MgrSpec mgrSpec, String methodName, List<String> paramTypeNameList, String returnTypeName, String comments)
			throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		MethodSrc mgrIntfMeth = null;

		try {

			// 计算与MGR接口相关的名称
			String mgrIntfFullName = JavaFormatter.getMgrIntfFullName(workspaceSpec, mgrSpec.getShortName());

			// 判断mgrIntf是否已经存在于缓存中，如果不存在，则初始化一个MGR模型
			ClassSrc mgrIntf = lookupMgrIntf(mgrIntfFullName);
			if (mgrIntf == null) {
				addMgrIntf(artifactSpec, mgrSpec.getShortName());
			}

			// Initiate new MethSrc and put them into ClassSrc.
			mgrIntfMeth = new MethodSrc();
			mgrIntfMeth.setName(methodName);
			mgrIntf.addMethod(methodName, mgrIntfMeth);

			// Judge the type of parameter and return.
			String paramTypeName = null;
			for (int i = 0; i < paramTypeNameList.size(); i++) {
				paramTypeName = paramTypeNameList.get(i);
				// Judge whether put the paramTypeName into import.
				mgrIntf.addImport(paramTypeName);
				// Handle every parameter for method.
				mgrIntfMeth.addParam(JavaFormatter.getClassSimpleName(paramTypeName), null);
			}

			// 方法的返回值和异常
			String appExpFullName = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.APP_EXCEPTION_FULL;
			mgrIntfMeth.setReturnType(JavaFormatter.getClassSimpleName(returnTypeName));
			// mgrIntfMeth.setReturnName("the" +
			// JavaFormatter.getClassSimpleName(JavaFormatter.getTypeOfList(returnTypeName)));
			mgrIntfMeth.addException(appExpFullName);
			mgrIntfMeth.setComments(comments);
			mgrIntf.addImport(returnTypeName);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return mgrIntfMeth;

	}

	public static ClassSrc lookupMgrIntf(String mgrIntfFullName) {
		Hashtable<String, ClassSrc> mgrIntfCache = ctxCache.getMgrIntfCache();
		ClassSrc mgrIntf = mgrIntfCache.get(mgrIntfFullName);
		return mgrIntf;
	}

	public static ClassSrc addMgrXIntf(ArtifactSpec artifactSpec, String mgrSpecShortName) throws IOException, AppException {

		// Get global naming and local setting.
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		// 判断mgrXIntf是否已经存在于缓存中
		String mgrXIntfFullName = JavaFormatter.getMgrXIntfFullName(workspaceSpec, mgrSpecShortName);
		String mgrIntfFullName = JavaFormatter.getMgrIntfFullName(workspaceSpec, mgrSpecShortName);
		ClassSrc mgrXIntf = lookupMgrXIntf(mgrXIntfFullName);

		// 如果不存在于缓存中，则初始化一个MGRX模型
		if (mgrXIntf == null) {

			logger.debug("NO MGRX: No MGRX interface found with name: " + mgrXIntfFullName + ", create new.");

			mgrXIntf = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_MGR_INTF);
			ctxCache.getMgrXIntfCache().put(mgrXIntfFullName, mgrXIntf);
			String absoluteName = JavaFormatter.getMgrXIntfAbsoluteName(workspaceSpec, mgrSpecShortName);
			mgrXIntf.setPkgName(JavaFormatter.getClassPkgName(mgrXIntfFullName));
			mgrXIntf.setComments(JavaSrcElm.MGR_VERB_DESC);
			mgrXIntf.setSimpleName(JavaFormatter.getClassSimpleName(mgrXIntfFullName));
			mgrXIntf.setFullName(mgrXIntfFullName);
			mgrXIntf.setFileName(absoluteName);
			// 增加import和需要继承的BaseMgr
			mgrXIntf.setExtendsList(Arrays.asList(JavaFormatter.getClassSimpleName(mgrIntfFullName)));
			mgrXIntf.addImport(mgrIntfFullName);

			// 从文件系统中扫描MGRX，如果发现，则标记为已存在的文件
			if (FileHandler.isFile(absoluteName) == true) {
				mgrXIntf.setExistingFile(true);
			}

		} else {
			logger.debug("FOUND MGRX: Found MGRX interface with name: " + mgrXIntfFullName);
		}

		return mgrXIntf;

	}

	public static ClassSrc lookupMgrXIntf(String mgrXIntfFullName) {
		Hashtable<String, ClassSrc> mgrXIntfCache = ctxCache.getMgrXIntfCache();
		ClassSrc mgrXIntf = mgrXIntfCache.get(mgrXIntfFullName);
		return mgrXIntf;
	}

	public static ClassSrc addMgrImpl(ArtifactSpec artifactSpec, String mgrSpecShortName) throws IOException, AppException {

		// Get global naming and local setting.
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		// 计算与MGR实现相关的名称
		String mgrIntfFullName = JavaFormatter.getMgrIntfFullName(workspaceSpec, mgrSpecShortName);
		String mgrImplFullName = JavaFormatter.getMgrImplFullName(workspaceSpec, mgrSpecShortName);
		ClassSrc mgrImpl = lookupMgrImpl(mgrImplFullName);

		// 判断MGR是否已经存在于缓存中，如果不存在，则初始化一个MGR模型
		if (mgrImpl == null) {

			logger.debug("NO MGR: No MGR implementation found with name: " + mgrImplFullName + ", create new.");

			mgrImpl = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_MGR_IMPL);
			ctxCache.getMgrImplCache().put(mgrImplFullName, mgrImpl);
			String absoluteName = JavaFormatter.getMgrImplAbsoluteName(workspaceSpec, mgrSpecShortName);
			mgrImpl.setPkgName(JavaFormatter.getClassPkgName(mgrImplFullName));
			mgrImpl.addImport(mgrIntfFullName);
			mgrImpl.addImport(JavaFormatter.getBaseMgrFullName(workspaceSpec));
			mgrImpl.setComments(JavaSrcElm.WARN_STMT_COMMENTS + JavaSrcElm.LINE_SEPARATOR + JavaSrcElm.LINE_COMMENT + JavaSrcElm.WHITE_SPACE + JavaSrcElm.MGR_VERB_DESC
					+ JavaSrcElm.WHITE_SPACE);
			mgrImpl.setSimpleName(JavaFormatter.getClassSimpleName(mgrImplFullName));
			mgrImpl.setFullName(mgrImplFullName);
			mgrImpl.setFileName(absoluteName);
			// 增加需要继承的BaseMgr
			mgrImpl.setImplementsList(Arrays.asList(JavaFormatter.getClassSimpleName(mgrIntfFullName)));
			mgrImpl.setExtendsList(Arrays.asList(JavaSrcElm.BASE_MGR_SIMPLE));

		} else {
			logger.debug("FOUND MGR: Found MGR implementation with name: " + mgrImplFullName);
		}

		return mgrImpl;

	}

	public static MethodSrc addMgrImplPublicMethod(ArtifactSpec artifactSpec, MgrSpec mgrSpec, String methodName, List<String> paramTypeList, String returnTypeName,
			String comments) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		MethodSrc mgrImplMeth = null;

		try {

			// 计算与MGR实现相关的名称
			String mgrImplFullName = JavaFormatter.getMgrImplFullName(workspaceSpec, mgrSpec.getShortName());

			// 判断MGR是否已经存在于缓存中，如果不存在，则初始化一个MGR模型
			ClassSrc mgrImpl = lookupMgrImpl(mgrImplFullName);
			if (mgrImpl == null) {
				addMgrImpl(artifactSpec, mgrSpec.getShortName());
			}

			// Initiate new MethSrc and put them into ClassSrc.
			mgrImplMeth = new MethodSrc();
			mgrImplMeth.setName(methodName);
			mgrImpl.addMethod(methodName, mgrImplMeth);

			// Judge the type of parameter and return.
			String paramType = null;
			for (int i = 0; i < paramTypeList.size(); i++) {
				paramType = paramTypeList.get(i);
				// Judge whether put the paramTypeName into import.
				mgrImpl.addImport(paramType);
				// Handle every parameter for method.
				mgrImplMeth.addParam(JavaFormatter.getClassSimpleName(paramType), null);
			}

			// 方法的返回值和异常
			String appExpFullName = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.APP_EXCEPTION_FULL;
			mgrImplMeth.setReturnType(JavaFormatter.getClassSimpleName(returnTypeName));
			if (StringUtils.isNotBlank(returnTypeName) == true) {
				if (returnTypeName.contains(JavaSrcElm.LESS_THAN) && returnTypeName.contains(JavaSrcElm.LARGER_THAN)) {
					mgrImplMeth.setReturnName(JavaSrcElm.RETURN_PREFIX + StringHelper.toUpperCase(JavaFormatter.getClassSimpleName(JavaFormatter.getTypeOfList(returnTypeName)), 0)
							+ JavaSrcElm.UTIL_LIST_SIMPLE);
				} else if (returnTypeName.equalsIgnoreCase(JavaSrcElm.INT) == true) {
					mgrImplMeth.setReturnName(JavaSrcElm.RETURN_PREFIX + StringHelper.toUpperCase(returnTypeName, 0));
				} else {
					mgrImplMeth.setReturnName(JavaSrcElm.RETURN_PREFIX + StringHelper.toUpperCase(JavaFormatter.getClassSimpleName(returnTypeName), 0));
				}
			} else {
				mgrImplMeth.setReturnName("theReturn");
			}
			mgrImplMeth.addException(appExpFullName);
			mgrImplMeth.setComments(comments);
			mgrImpl.addImport(returnTypeName);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return mgrImplMeth;

	}

	public static MethodSrc addMgrImplDummyMethod(ArtifactSpec artifactSpec, MgrSpec mgrSpec, String methodName, List<String> paramTypeList, String returnTypeName, String comments)
			throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		MethodSrc mgrImplMeth = null;

		try {

			// 计算与MGR实现相关的名称
			String mgrImplFullName = JavaFormatter.getMgrImplFullName(workspaceSpec, mgrSpec.getShortName());

			// 判断MGR是否已经存在于缓存中，如果不存在，则初始化一个MGR模型
			ClassSrc mgrImpl = lookupMgrImpl(mgrImplFullName);
			if (mgrImpl == null) {
				addMgrImpl(artifactSpec, mgrSpec.getShortName());
			}

			// Initiate new MethSrc and put them into ClassSrc.
			mgrImplMeth = new MethodSrc();
			mgrImplMeth.setName(methodName);
			mgrImpl.addMethod(methodName, mgrImplMeth);

			// Judge the type of parameter and return.
			String paramType = null;
			for (int i = 0; i < paramTypeList.size(); i++) {
				paramType = paramTypeList.get(i);
				// Judge whether put the paramTypeName into import.
				mgrImpl.addImport(paramType);
				// Handle every parameter for method.
				mgrImplMeth.addParam(JavaFormatter.getClassSimpleName(paramType), null);
			}

			// 方法的返回值和异常
			mgrImplMeth.setModifiers(JavaSrcElm.PROTECTED);
			mgrImplMeth.setReturnType(JavaFormatter.getClassSimpleName(returnTypeName));
			// if (StringUtils.isNotBlank(returnTypeName) == true) {
			// mgrImplMeth.setReturnName("the" +
			// (returnTypeName.contains(JavaSrcElm.LESS_THAN) &&
			// returnTypeName.contains(JavaSrcElm.LARGER_THAN)
			// ?
			// JavaFormatter.getClassSimpleName(JavaFormatter.getTypeOfList(returnTypeName))
			// :
			// StringHelper.toUpperCase(JavaFormatter.getClassSimpleName(returnTypeName),
			// 0)));
			// } else {
			// mgrImplMeth.setReturnName("theReturn");
			// }
			mgrImplMeth.addException(JavaSrcElm.LANG_THROWABLE_SIMPLE);
			mgrImplMeth.setComments(comments);
			mgrImpl.addImport(returnTypeName);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return mgrImplMeth;

	}

	public static ClassSrc lookupMgrImpl(String mgrImplFullName) {
		Hashtable<String, ClassSrc> mgrImplCache = ctxCache.getMgrImplCache();
		ClassSrc mgrImpl = mgrImplCache.get(mgrImplFullName);
		return mgrImpl;
	}

	public static ClassSrc addMgrXImpl(ArtifactSpec artifactSpec, String mgrSpecShortName) throws IOException, AppException {

		// Get global naming and local setting.
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		// 判断mgrXImpl是否已经存在于缓存中
		String mgrXIntfFullName = JavaFormatter.getMgrXIntfFullName(workspaceSpec, mgrSpecShortName);
		String mgrXImplFullName = JavaFormatter.getMgrXImplFullName(workspaceSpec, mgrSpecShortName);
		String mgrImplFullName = JavaFormatter.getMgrImplFullName(workspaceSpec, mgrSpecShortName);
		ClassSrc mgrXImpl = lookupMgrXImpl(mgrXImplFullName);

		// 如果不存在于缓存中，则初始化一个MGRX模型
		if (mgrXImpl == null) {

			logger.debug("NO MGRX: No MGRX implementation found with name: " + mgrXImplFullName + ", create new.");

			// 如果不存在于缓存中，则初始化一个MGRX模型
			mgrXImpl = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_MGR_IMPL);
			ctxCache.getMgrXImplCache().put(mgrXImplFullName, mgrXImpl);
			String absoluteName = JavaFormatter.getMgrXImplAbsoluteName(workspaceSpec, mgrSpecShortName);
			mgrXImpl.setPkgName(JavaFormatter.getClassPkgName(mgrXImplFullName));
			mgrXImpl.setComments(JavaSrcElm.MGR_VERB_DESC);
			mgrXImpl.setSimpleName(JavaFormatter.getClassSimpleName(mgrXImplFullName));
			mgrXImpl.setFullName(mgrXImplFullName);
			mgrXImpl.setFileName(absoluteName);
			// 增加import和需要继承的BaseMgr
			mgrXImpl.setImplementsList(Arrays.asList(JavaFormatter.getClassSimpleName(mgrXIntfFullName)));
			mgrXImpl.setExtendsList(Arrays.asList(JavaFormatter.getClassSimpleName(mgrImplFullName)));
			mgrXImpl.addImport(mgrXIntfFullName);
			mgrXImpl.addImport(mgrImplFullName);

			// 从文件系统中扫描MGRX，如果发现，则标记为已存在的文件
			if (FileHandler.isFile(absoluteName) == true) {
				mgrXImpl.setExistingFile(true);
			}

		} else {
			logger.debug("FOUND MGRX: Found MGRX implementation with name: " + mgrXImplFullName);
		}

		return mgrXImpl;

	}

	public static MethodSrc addMgrXImplDummyMethod(ArtifactSpec artifactSpec, MgrSpec mgrSpec, String methodName, List<String> paramTypeList, String returnTypeName,
			String comments) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		MethodSrc mgrXImplDummyMeth = null;

		try {

			// 计算与MGRX实现相关的名称
			String mgrXImplFullName = JavaFormatter.getMgrXImplFullName(workspaceSpec, mgrSpec.getShortName());

			// 判断MGRX是否已经存在于缓存中，如果不存在，则初始化一个MGR模型
			ClassSrc mgrXImpl = lookupMgrXImpl(mgrXImplFullName);
			if (mgrXImpl == null) {
				addMgrXImpl(artifactSpec, mgrSpec.getShortName());
			}

			// Initiate new MethSrc and put them into ClassSrc.
			mgrXImplDummyMeth = new MethodSrc();
			mgrXImplDummyMeth.setName(methodName);
			mgrXImpl.addMethod(methodName, mgrXImplDummyMeth);

			// Judge the type of parameter and return.
			String paramType = null;
			for (int i = 0; i < paramTypeList.size(); i++) {
				paramType = paramTypeList.get(i);
				// Judge whether put the paramTypeName into import.
				mgrXImpl.addImport(paramType);
				// Handle every parameter for method.
				mgrXImplDummyMeth.addParam(JavaFormatter.getClassSimpleName(paramType), null);
			}

			// 方法的返回值和异常
			mgrXImplDummyMeth.setModifiers(JavaSrcElm.PROTECTED);
			mgrXImplDummyMeth.setReturnType(JavaFormatter.getClassSimpleName(returnTypeName));
			if (StringUtils.isNotBlank(returnTypeName) == true) {
				mgrXImplDummyMeth.setReturnName("the" + (returnTypeName.contains(JavaSrcElm.LESS_THAN) && returnTypeName.contains(JavaSrcElm.LARGER_THAN)
						? JavaFormatter.getClassSimpleName(JavaFormatter.getTypeOfList(returnTypeName))
						: StringHelper.toUpperCase(JavaFormatter.getClassSimpleName(returnTypeName), 0)));
			} else {
				mgrXImplDummyMeth.setReturnName("theReturn");
			}
			mgrXImplDummyMeth.addException(JavaSrcElm.LANG_THROWABLE_SIMPLE);
			mgrXImplDummyMeth.setComments(comments);
			mgrXImplDummyMeth.addAnnot(JavaSrcElm.ANNOT_OVER_RIDE);
			mgrXImpl.addImport(returnTypeName);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return mgrXImplDummyMeth;

	}

	public static ClassSrc lookupMgrXImpl(String mgrXImplFullName) {
		Hashtable<String, ClassSrc> mgrXImplCache = ctxCache.getMgrXImplCache();
		ClassSrc mgrXImpl = mgrXImplCache.get(mgrXImplFullName);
		return mgrXImpl;
	}

	public static ClassSrc addSvcMsgCodeAttr(ArtifactSpec artifactSpec, String code, String comments) throws AppException {

		// Get global naming and local setting.
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		String svcMsgCodeFullName = JavaFormatter.getSvcMsgCodeClassFullName(workspaceSpec);
		ClassSrc svcMsgCodeClass = lookupMsgCodeClass(svcMsgCodeFullName);

		try {

			// 判断SvcMsgCode是否已经存在于缓存中
			if (svcMsgCodeClass == null) {
				logger.debug("NO MSG CD: No MsgCode found with name: " + svcMsgCodeFullName + ", create new.");
				// 初始化一个SvcMsgCode并注册
				svcMsgCodeClass = new ClassSrc(workspaceSpec, JavaSrcElm.EXP_TYPE_MSGCD_CLS);
				ctxCache.getMsgCodeCache().put(svcMsgCodeFullName, svcMsgCodeClass);
				svcMsgCodeClass.setPkgName(JavaFormatter.getClassPkgName(svcMsgCodeFullName));
				svcMsgCodeClass.setComments(MsgCodeComments.MSG_CD_COMMENTS);
				svcMsgCodeClass.setSimpleName(JavaFormatter.getClassSimpleName(svcMsgCodeFullName));
				svcMsgCodeClass.setFullName(svcMsgCodeFullName);
				String absoluteName = JavaFormatter.getSvcMsgCodeAbsoluteName(workspaceSpec);
				svcMsgCodeClass.setFileName(absoluteName);
				// 从文件系统中扫描DTO，如果发现则导入已经存在其中的属性
				ClassSrcImporter.importSvcMsgCodeAttr(absoluteName, svcMsgCodeClass);
			} else {
				logger.debug("FOUND MSG CD: Found MsgCode with name: " + svcMsgCodeFullName);
			}

			AttributeSrc attrSrc = new AttributeSrc();
			attrSrc.setName(JavaSrcElm.UNDER_LINE + code);
			attrSrc.setDefaultValue(JavaSrcElm.DOUBLE_QUOTATION + code + JavaSrcElm.DOUBLE_QUOTATION);
			attrSrc.addObjComments(comments);
			attrSrc.setModifier(JavaSrcElm.PUBLIC_STATIC_FINAL);
			attrSrc.setDataType(JavaSrcElm.STRING);
			svcMsgCodeClass.addAttr(attrSrc, false);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return svcMsgCodeClass;

	}

	public static ClassSrc lookupMsgCodeClass(String msgCodeFullName) {
		Hashtable<String, ClassSrc> msgCodeCache = ctxCache.getMsgCodeCache();
		ClassSrc msgCodeClass = msgCodeCache.get(msgCodeFullName);
		return msgCodeClass;
	}

}
