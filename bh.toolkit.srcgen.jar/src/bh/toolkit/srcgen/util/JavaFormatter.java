package bh.toolkit.srcgen.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.artifact.MethodSpec;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;
import bh.toolkit.srcgen.model.rdb.ColumnSpec;
import bh.toolkit.srcgen.registry.CtxCacheFacade;

public class JavaFormatter {

	public static int OPR_TYPE_INTF = 0;
	public static int OPR_TYPE_IMPL = 1;
	public static int OPR_TYPE_XINTF = 2;
	public static int OPR_TYPE_XIMPL = 3;

	/**
	 * 将给定的Java语句转换为对应的DB声明
	 * 
	 * @param javaPhrase
	 * @return
	 */
	public static String getDBStyle(String javaPhrase) {

		// The return value.
		StringBuffer javaWord = new StringBuffer();
		StringBuffer dbPhrase = new StringBuffer();
		String dbWord = null;

		int len = javaPhrase.length();
		char c = 0;
		for (int i = 0; i < len; i++) {

			c = javaPhrase.charAt(i);

			// Java Bean属性名可能包含下划线"_"或者数字
			if (String.valueOf(c).equals(MapperElm.UNDER_LINE) == true) {
				continue;
			}

			if (Character.isLowerCase(c) == true || Character.isDigit(c) == true) {
				javaWord.append(c);
				if (i < len - 1) {
					continue;
				}
			}

			if (javaWord.length() != 0) {

				// Find corresponding dbWord with previous javaWord.
				dbWord = javaWord.toString().toUpperCase();
				if (dbPhrase.length() != 0) {
					dbPhrase.append(MapperElm.UNDER_LINE);
				}
				dbPhrase.append(dbWord);
			}

			// Begin a new javaWord.
			javaWord.delete(0, javaWord.length());
			if (Character.isUpperCase(c) == true) {
				javaWord.append(c);
			}

		}

		return dbPhrase.toString();

	}

	public static String getJavaStyle(String dbPhrase, boolean keepSchema, boolean isLargeCamel) {

		// 将dbPhrase分离为两部分
		String schema = "";
		String dbWord = dbPhrase;
		int idx = dbPhrase.indexOf(MapperElm.DOT);
		if (idx != -1) {
			schema = dbPhrase.substring(0, idx);
			dbWord = dbPhrase.substring(idx + 1, dbPhrase.length());
		}

		// 去除数据库表的"T_"前缀
		if (dbWord.startsWith(MapperElm.SQL_TABLE_NAME_PREFIX) == true) {
			dbWord = dbWord.substring(MapperElm.SQL_TABLE_NAME_PREFIX.length(), dbWord.length());
		}

		// 将"_"分隔的单词转换为驼峰风格的单词
		StringBuffer charBuff = new StringBuffer();
		StringBuffer javaPhrase = new StringBuffer();
		String javaWord = null;
		int len = dbWord.length();
		char c = 0;
		for (int i = 0; i < len; i++) {

			c = dbWord.charAt(i);

			// 如果当前字符不是"_"，则缓存
			if (String.valueOf(c).equals(MapperElm.UNDER_LINE) == false) {
				charBuff.append(c);
				if (i < len - 1) {
					continue;
				}
			}

			if (charBuff.length() != 0) {
				javaWord = charBuff.toString().toLowerCase();
				// 如果是大驼峰风格，或者是第一个单词，则首字母大写
				if (isLargeCamel == true || javaPhrase.length() != 0) {
					javaWord = StringHelper.toUpperCase(javaWord, 0);
				}
				javaPhrase.append(javaWord);

				// 清除缓存内容
				charBuff.delete(0, charBuff.length());
			}

		}

		if (keepSchema == true && StringUtils.isNotBlank(schema) == true) {
			return StringUtils.lowerCase(schema) + JavaSrcElm.DOT + javaPhrase.toString();
		} else {
			return javaPhrase.toString();
		}

	}

	/**
	 * 优先考虑 MethodSpec的verb和subject属性，如果为空，则根据tableName定义对应的方法名
	 * 
	 * @param workspaceSpec
	 * @param methodSpec
	 * @param oprVerb
	 * @return
	 */
	public static String getMethodName(WorkspaceSpec workspaceSpec, MethodSpec methodSpec, String oprVerb) {

		// methodSpec中的verb优先级更高
		String verb = methodSpec.getVerb();
		if (StringUtils.isBlank(verb) == true) {
			verb = oprVerb;
		}

		// methodSpec中的subject优先级更高
		String subject = methodSpec.getSubject();
		if (StringUtils.isBlank(subject) == true) {
			String tableName = methodSpec.getTableName();
			String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
			String dtoXSimpleName = JavaFormatter.getClassSimpleName(dtoXFullName);
			subject = StringHelper.toUpperCase(dtoXSimpleName, 0);
		}

		return verb + StringHelper.toUpperCase(subject, 0);

	}

	/**
	 * 优先考虑MethodSpec的paramName属性，如果为空，则根据tableName定义对应的DTOX
	 * 
	 * @param workspaceSpec
	 * @param methodSpec
	 * @return
	 */
	// public static String getMethodParamDataType(WorkspaceSpec workspaceSpec, MethodSpec methodSpec) {
	//
	// String paramDataType = methodSpec.getParamDataType();
	// if (StringUtils.isNotBlank(paramDataType) == true) {
	// return paramDataType;
	// }
	// String tableName = methodSpec.getTableName();
	// paramDataType = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
	// return paramDataType;
	//
	// }

	/**
	 * 优先考虑MethodSpec的returnType属性，如果为空，则根据tableName定义对应的DTOX
	 * 
	 * @param workspaceSpec
	 * @param methodSpec
	 * @return
	 */
	// public static String getMethodReturnType(WorkspaceSpec workspaceSpec, MethodSpec methodSpec) {
	//
	// String returnDataType = methodSpec.getReturnDataType();
	// if (StringUtils.isNotBlank(returnDataType) == true) {
	// return returnDataType;
	// }
	// String tableName = methodSpec.getTableName();
	// returnDataType = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
	// return returnDataType;
	//
	// }

	public static String getJavaNullExpr(ColumnSpec columnSpec) throws AppException {
		String jdbcTypeName = columnSpec.getJdbcTypeName();
		String javaTypeName = CtxCacheFacade.lookupJavaDataType(columnSpec.getJdbcTypeName());
		if (StringUtils.isNotBlank(javaTypeName) == true) {
			return JavaSrcElm.EXPR_OBJ_NOT_NULL;
		} else {
			throw new AppException("NOT FOUND: Could not find Java type name with JDBC type name: " + jdbcTypeName);
		}
	}

	public static String getClassSimpleName(String classFullName) {

		if (StringUtils.isBlank(classFullName) == true) {
			return "";
		}

		// 如果不包含泛型声明，则取最后一个“.”之后的类型
		int lessThan = classFullName.indexOf(JavaSrcElm.LESS_THAN);
		if (lessThan == -1) {
			int lastDot = classFullName.lastIndexOf(JavaSrcElm.DOT);
			if (lastDot != -1) {
				return classFullName.substring(lastDot + 1, classFullName.length());
			} else {
				return classFullName;
			}
		}

		// 如果包含泛型，则将各部分的全名改成短名
		String classSimpleName = "";
		String containerType = classFullName.substring(0, lessThan);
		int lastDot = containerType.lastIndexOf(JavaSrcElm.DOT);
		if (lastDot != -1) {
			classSimpleName += containerType.substring(lastDot + 1, containerType.length());
		} else {
			classSimpleName += containerType;
		}
		classSimpleName += JavaSrcElm.LESS_THAN;

		int largerThan = classFullName.indexOf(JavaSrcElm.LARGER_THAN);
		String genericType = classFullName.substring(lessThan + 1, largerThan);
		int comma = genericType.indexOf(JavaSrcElm.COMMA);
		if (comma != -1) {
			String genericTypeLeft = StringUtils.trim(genericType.substring(0, comma));
			lastDot = genericTypeLeft.lastIndexOf(JavaSrcElm.DOT);
			if (lastDot != -1) {
				classSimpleName += genericTypeLeft.substring(lastDot + 1, genericTypeLeft.length());
			} else {
				classSimpleName += genericTypeLeft;
			}
			classSimpleName += JavaSrcElm.COMMA + JavaSrcElm.WHITE_SPACE;

			String genericTypeRight = StringUtils.trim(genericType.substring(comma + 1));
			lastDot = genericTypeRight.lastIndexOf(JavaSrcElm.DOT);
			if (lastDot != -1) {
				classSimpleName += genericTypeRight.substring(lastDot + 1, genericTypeRight.length());
			} else {
				classSimpleName += genericTypeRight;
			}

		} else {
			lastDot = genericType.lastIndexOf(JavaSrcElm.DOT);
			if (lastDot != -1) {
				classSimpleName += genericType.substring(lastDot + 1, genericType.length());
			} else {
				classSimpleName += genericType;
			}
		}
		classSimpleName += JavaSrcElm.LARGER_THAN;

		return classSimpleName;

	}

	// public static String getParamName(String typeName, boolean isLargeCamel) {
	//
	// if (StringUtils.isBlank(typeName) == true) {
	// return "";
	// }
	//
	// // 由全名称得到简单名称
	// String simpleName = getClassSimpleName(typeName);
	//
	// // 如果是容器类型，得到其中的元素类型，转格式化为参数名
	// String paramName = null;
	// if (simpleName.contains(JavaSrcElm.UTIL_LIST_SIMPLE) == true && simpleName.contains(JavaSrcElm.LARGER_THAN) == true) {
	// paramName = JavaFormatter.getTypeOfList(simpleName) + JavaSrcElm.UTIL_LIST_SIMPLE;
	// } else if (simpleName.contains(JavaSrcElm.UTIL_MAP_SIMPLE) == true && simpleName.contains(JavaSrcElm.LARGER_THAN) == true) {
	// paramName = JavaFormatter.getTypeOfMap(simpleName) + JavaSrcElm.UTIL_MAP_SIMPLE;
	// } else {
	// paramName = simpleName;
	// }
	//
	// if (isLargeCamel == true) {
	// paramName = StringHelper.toUpperCase(paramName, 0);
	// } else {
	// paramName = StringHelper.toLowerCase(paramName, 0);
	// }
	//
	// return paramName;
	//
	// }

	public static String getClassPkgName(String classFullName) {
		String classPkgName = classFullName;
		int idx = classFullName.lastIndexOf(JavaSrcElm.DOT);
		if (idx != -1) {
			classPkgName = classFullName.substring(0, idx);
		}
		return classPkgName;
	}

	public static String getTypeOfList(String listStmt) {

		if (StringUtils.isBlank(listStmt) == true) {
			return "";
		}
		Pattern pat = Pattern.compile("(.+<)(.+)(>)");
		Matcher matcher = pat.matcher(listStmt);
		if (matcher.matches() == true) {
			return matcher.group(2);
		} else {
			return "";
		}

	}

	public static String getTypeOfMap(String mapStmt) {

		if (StringUtils.isBlank(mapStmt) == true) {
			return "";
		}
		Pattern pat = Pattern.compile("(.+<)(.+,)(\\s+)(.+)(>)");
		Matcher matcher = pat.matcher(mapStmt);
		if (matcher.matches() == true) {
			return matcher.group(4);
		} else {
			return "";
		}

	}

	public static String getVoFullName(WorkspaceSpec workspaceSpec, String tableName) {
		String voPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getVoPkgName();
		String voSimpleName = getJavaStyle(tableName, true, true) + JavaSrcElm.VO_NAME_SUFFIX;
		return voPkgPrefix + JavaSrcElm.DOT + voSimpleName;
	}

	public static String getVoAbsoluteName(WorkspaceSpec workspaceSpec, String tableName) throws AppException {

		// Change package name to file name.
		String voFullName = getVoFullName(workspaceSpec, tableName);
		String voFileName = classNameToFileName(voFullName);

		// Assemble the complete file name for Sql Map XML file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getTransObjPrj()) + voFileName + JavaSrcElm.JAVA_FILE_EXT;

		return absoluteName;

	}

	public static String getCatSysCodeFullName(WorkspaceSpec workspaceSpec) {
		return workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.EXPR_CAT_SYS_CD_FULL;
	}

	public static String getCmCatSysCodeFullName(WorkspaceSpec workspaceSpec) {
		return workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.EXPR_CM_CAT_SYS_CD_FULL;
	}

	public static String getPtyCatSysCodeFullName(WorkspaceSpec workspaceSpec) {
		return workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.EXPR_PTY_CAT_SYS_CD_FULL;
	}

	public static String getBaseDtoFullName(WorkspaceSpec workspaceSpec) {
		return workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.BASE_DTO_FULL;
	}

	public static String getBaseOprFullName(WorkspaceSpec workspaceSpec) {
		return workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.BASE_OPR_FULL;
	}

	public static String getBaseMgrFullName(WorkspaceSpec workspaceSpec) {
		return workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.BASE_MGR_FULL;
	}

	public static String getDtoFullName(WorkspaceSpec workspaceSpec, String tableName) {
		String dtoPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getDtoPkgName();
		String dtoSimpleName = getJavaStyle(tableName, true, true) + JavaSrcElm.DTO_NAME_SUFFIX;
		return dtoPkgPrefix + JavaSrcElm.BASE_PKG_PAT + dtoSimpleName;
	}

	public static String getDtoAbsoluteName(WorkspaceSpec workspaceSpec, String tableName) throws AppException {
		// Change package name to file name.
		String dtoFullName = getDtoFullName(workspaceSpec, tableName);
		String dtoFileName = classNameToFileName(dtoFullName);
		// Assemble the complete file name for Sql Map XML file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getTransObjPrj()) + dtoFileName + JavaSrcElm.JAVA_FILE_EXT;
		return absoluteName;
	}

	public static String getDtoXFullName(WorkspaceSpec workspaceSpec, String tableName) {
		String dtoPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getDtoPkgName();
		String dtoSimpleName = getJavaStyle(tableName, true, true) + JavaSrcElm.DTOX_NAME_SUFFIX;
		return dtoPkgPrefix + JavaSrcElm.EXT_PKG_PAT + dtoSimpleName;
	}

	public static String getDtoXAbsoluteName(WorkspaceSpec workspaceSpec, String tableName) throws AppException {
		// Change package name to file name.
		String dtoXFullName = getDtoXFullName(workspaceSpec, tableName);
		String dtoXFileName = classNameToFileName(dtoXFullName);
		// Assemble the complete file name for Sql Map XML file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getTransObjPrj()) + dtoXFileName + JavaSrcElm.JAVA_FILE_EXT;
		return absoluteName;
	}

	public static String getDaoFullName(WorkspaceSpec workspaceSpec, String tableName) {
		String daoPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getDaoPkgName();
		String schemaName = MapperFormatter.getTableSchema(tableName);
		String daoSimpleName = JavaSrcElm.INTF_PREFIX + getJavaStyle(tableName, false, true) + JavaSrcElm.DAO_INTF_SUFFIX;
		return daoPkgPrefix + JavaSrcElm.BASE_PKG_PAT + StringUtils.lowerCase(schemaName) + JavaSrcElm.DOT + daoSimpleName;
	}

	public static String getDaoAbsoluteName(WorkspaceSpec workspaceSpec, String tableName) throws AppException {
		// Change package name to file name.
		String daoFullName = getDaoFullName(workspaceSpec, tableName);
		String daoFileName = classNameToFileName(daoFullName);
		// Assemble the complete file name for DAO file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getOrmPrj()) + daoFileName + JavaSrcElm.JAVA_FILE_EXT;
		return absoluteName;
	}

	public static String getDaoXFullName(WorkspaceSpec workspaceSpec, String tableName) {
		String daoXPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getDaoPkgName();
		String schemaName = MapperFormatter.getTableSchema(tableName);
		String daoXSimpleName = JavaSrcElm.INTF_PREFIX + getJavaStyle(tableName, false, true) + JavaSrcElm.DAOX_INTF_SUFFIX;
		return daoXPkgPrefix + JavaSrcElm.EXT_PKG_PAT + StringUtils.lowerCase(schemaName) + JavaSrcElm.DOT + daoXSimpleName;
	}

	public static String getDaoXAbsoluteName(WorkspaceSpec workspaceSpec, String tableName) throws AppException {
		// Change package name to file name.
		String daoXFullName = getDaoXFullName(workspaceSpec, tableName);
		String daoXFileName = classNameToFileName(daoXFullName);
		// Assemble the complete file name for DAOX file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getOrmPrj()) + daoXFileName + JavaSrcElm.JAVA_FILE_EXT;
		return absoluteName;
	}

	public static String getOprIntfFullName(WorkspaceSpec workspaceSpec, String tableName) {
		String oprPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getOprPkgName();
		String schemaName = MapperFormatter.getTableSchema(tableName);
		String oprSimpleName = JavaSrcElm.INTF_PREFIX + getJavaStyle(tableName, false, true) + JavaSrcElm.OPR_INTF_SUFFIX;
		return oprPkgPrefix + JavaSrcElm.BASE_PKG_PAT + StringUtils.lowerCase(schemaName) + JavaSrcElm.DOT + oprSimpleName;
	}

	public static String getOprIntfAbsoluteName(WorkspaceSpec workspaceSpec, String tableName) throws AppException {
		// Change package name to file name.
		String oprFullName = getOprIntfFullName(workspaceSpec, tableName);
		String oprFileName = classNameToFileName(oprFullName);
		// Assemble the complete file name for OPR file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getOrmPrj()) + oprFileName + JavaSrcElm.JAVA_FILE_EXT;
		return absoluteName;
	}

	public static String getOprImplFullName(WorkspaceSpec workspaceSpec, String tableName) {
		String oprPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getOprPkgName();
		String schemaName = MapperFormatter.getTableSchema(tableName);
		String oprImplSimpleName = getJavaStyle(tableName, false, true) + JavaSrcElm.OPR_IMPL_SUFFIX;
		return oprPkgPrefix + JavaSrcElm.BASE_PKG_PAT + StringUtils.lowerCase(schemaName) + JavaSrcElm.DOT + oprImplSimpleName;
	}

	public static String getOprImplAbsoluteName(WorkspaceSpec workspaceSpec, String tableName) throws AppException {
		// Change package name to file name.
		String oprImplFullName = getOprImplFullName(workspaceSpec, tableName);
		String oprImplFileName = classNameToFileName(oprImplFullName);
		// Assemble the complete file name for OPR file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getOrmPrj()) + oprImplFileName + JavaSrcElm.JAVA_FILE_EXT;
		return absoluteName;
	}

	public static String getOprXIntfFullName(WorkspaceSpec workspaceSpec, String tableName) {
		String oprPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getOprPkgName();
		String schemaName = MapperFormatter.getTableSchema(tableName);
		String oprXSimpleName = JavaSrcElm.INTF_PREFIX + getJavaStyle(tableName, false, true) + JavaSrcElm.OPRX_INTF_SUFFIX;
		return oprPkgPrefix + JavaSrcElm.EXT_PKG_PAT + StringUtils.lowerCase(schemaName) + JavaSrcElm.DOT + oprXSimpleName;
	}

	public static String getOprXIntfAbsoluteName(WorkspaceSpec workspaceSpec, String oprShortName) throws AppException {
		// Change package name to file name.
		String oprXFullName = getOprXIntfFullName(workspaceSpec, oprShortName);
		String oprXFileName = classNameToFileName(oprXFullName);
		// Assemble the complete file name for OPR file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getOrmPrj()) + oprXFileName + JavaSrcElm.JAVA_FILE_EXT;
		return absoluteName;
	}

	public static String getOprXImplFullName(WorkspaceSpec workspaceSpec, String tableName) {
		String oprPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getOprPkgName();
		String schemaName = MapperFormatter.getTableSchema(tableName);
		String oprXImplSimpleName = getJavaStyle(tableName, false, true) + JavaSrcElm.OPRX_IMPL_SUFFIX;
		return oprPkgPrefix + JavaSrcElm.EXT_PKG_PAT + StringUtils.lowerCase(schemaName) + JavaSrcElm.DOT + oprXImplSimpleName;
	}

	public static String getOprXImplAbsoluteName(WorkspaceSpec workspaceSpec, String tableName) throws AppException {
		// Change package name to file name.
		String oprXImplFullName = getOprXImplFullName(workspaceSpec, tableName);
		String oprXImplFileName = classNameToFileName(oprXImplFullName);
		// Assemble the complete file name for OPR file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getOrmPrj()) + oprXImplFileName + JavaSrcElm.JAVA_FILE_EXT;
		return absoluteName;
	}

	public static String getMgrIntfFullName(WorkspaceSpec workspaceSpec, String tableName) {
		String mgrPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getMgrPkgName();
		String domainName = JavaFormatter.getMgrDomainName(workspaceSpec, tableName);
		String mgrIntfSimpleName = JavaSrcElm.INTF_PREFIX + getJavaStyle(tableName, false, true) + JavaSrcElm.MGR_INTF_SUFFIX;
		return mgrPkgPrefix + JavaSrcElm.BASE_PKG_PAT + StringUtils.lowerCase(domainName) + JavaSrcElm.DOT + mgrIntfSimpleName;
	}

	public static String getMgrIntfAbsoluteName(WorkspaceSpec workspaceSpec, String tableName) throws AppException {
		// Change package name to file name.
		String mgrIntfFullName = getMgrIntfFullName(workspaceSpec, tableName);
		String mgrIntfFileName = classNameToFileName(mgrIntfFullName);
		// Assemble the complete file name for OPR file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getCompDescPrj()) + mgrIntfFileName + JavaSrcElm.JAVA_FILE_EXT;
		return absoluteName;
	}

	public static String getMgrImplFullName(WorkspaceSpec workspaceSpec, String tableName) {
		String mgrPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getMgrPkgName();
		String domainName = JavaFormatter.getMgrDomainName(workspaceSpec, tableName);
		String mgrImplSimpleName = getJavaStyle(tableName, false, true) + JavaSrcElm.MGR_IMPL_SUFFIX;
		return mgrPkgPrefix + JavaSrcElm.BASE_PKG_PAT + StringUtils.lowerCase(domainName) + JavaSrcElm.DOT + mgrImplSimpleName;
	}

	public static String getMgrImplAbsoluteName(WorkspaceSpec workspaceSpec, String tableName) throws AppException {
		// Change package name to file name.
		String mgrImplFullName = getMgrImplFullName(workspaceSpec, tableName);
		String mgrImplFileName = classNameToFileName(mgrImplFullName);
		// Assemble the complete file name for OPR file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getMgrPrj()) + mgrImplFileName + JavaSrcElm.JAVA_FILE_EXT;
		return absoluteName;
	}

	public static String getMgrXIntfFullName(WorkspaceSpec workspaceSpec, String tableName) {
		String mgrPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getMgrPkgName();
		String domainName = JavaFormatter.getMgrDomainName(workspaceSpec, tableName);
		String mgrXIntfSimpleName = JavaSrcElm.INTF_PREFIX + getJavaStyle(tableName, false, true) + JavaSrcElm.MGRX_INTF_SUFFIX;
		return mgrPkgPrefix + JavaSrcElm.EXT_PKG_PAT + StringUtils.lowerCase(domainName) + JavaSrcElm.DOT + mgrXIntfSimpleName;
	}

	public static String getMgrXIntfAbsoluteName(WorkspaceSpec workspaceSpec, String tableName) throws AppException {
		// Change package name to file name.
		String mgrXIntfFullName = getMgrXIntfFullName(workspaceSpec, tableName);
		String mgrXIntfFileName = classNameToFileName(mgrXIntfFullName);
		// Assemble the complete file name for OPR file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getCompDescPrj()) + mgrXIntfFileName + JavaSrcElm.JAVA_FILE_EXT;
		return absoluteName;
	}

	public static String getMgrXImplFullName(WorkspaceSpec workspaceSpec, String tableName) {
		String mgrPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getMgrPkgName();
		String domainName = JavaFormatter.getMgrDomainName(workspaceSpec, tableName);
		String mgrXImplSimpleName = getJavaStyle(tableName, false, true) + JavaSrcElm.MGRX_IMPL_SUFFIX;
		return mgrPkgPrefix + JavaSrcElm.EXT_PKG_PAT + StringUtils.lowerCase(domainName) + JavaSrcElm.DOT + mgrXImplSimpleName;
	}

	public static String getMgrXImplAbsoluteName(WorkspaceSpec workspaceSpec, String tableName) throws AppException {
		// Change package name to file name.
		String mgrXImplFullName = getMgrXImplFullName(workspaceSpec, tableName);
		String mgrXImplFileName = classNameToFileName(mgrXImplFullName);
		// Assemble the complete file name for OPR file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getMgrPrj()) + mgrXImplFileName + JavaSrcElm.JAVA_FILE_EXT;
		return absoluteName;
	}

	public static String getMgrDomainName(WorkspaceSpec workspaceSpec, String tableName) {
		Pattern pattern = Pattern.compile("([a-zA-Z]*)(\\.|_)(.*)");
		Matcher matcher = pattern.matcher(tableName);
		if (matcher.matches() == true) {
			return matcher.group(1);
		} else {
			return tableName;
		}
	}

	public static String getSvcMsgCodeClassFullName(WorkspaceSpec workspaceSpec) {
		String msgCdPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getMsgCdPkgName();
		return msgCdPkgPrefix + JavaSrcElm.DOT + JavaSrcElm.SVC_MSG_CODE_SIMPLE;
	}

	public static String getSvcMsgCodeAbsoluteName(WorkspaceSpec workspaceSpec) throws AppException {
		// Change package name to file name.
		String svcMsgCodeFullName = getSvcMsgCodeClassFullName(workspaceSpec);
		String svcMsgCodeFileName = classNameToFileName(svcMsgCodeFullName);
		// Assemble the complete file name for Sql Map XML file.
		String absoluteName = WorkspaceUtil.getSrcPath(workspaceSpec, workspaceSpec.getCommonPrj()) + svcMsgCodeFileName + JavaSrcElm.JAVA_FILE_EXT;
		return absoluteName;
	}

	public static String getMsgPropFullName(WorkspaceSpec workspaceSpec) {
		String msgCdPkgPrefix = workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getMsgPropPkgName();
		return msgCdPkgPrefix + JavaSrcElm.DOT + JavaSrcElm.MSG_PROP_SIMPLE;
	}

	public static String getMsgPropAbsoluteName(WorkspaceSpec workspaceSpec) throws AppException {
		// Change package name to file name.
		String msgPropFullName = getMsgPropFullName(workspaceSpec);
		String msgPropFileName = classNameToFileName(msgPropFullName);
		// Assemble the complete file name for Sql Map XML file.
		String absoluteName = WorkspaceUtil.getRsrcPath(workspaceSpec, workspaceSpec.getCommonPrj()) + msgPropFileName + JavaSrcElm.PROP_FILE_EXT;
		return absoluteName;
	}

	public static String oprFullNameToDaoFullName(String oprFullName, int oprType) {

		// Change OPR package name into DAO package name.
		String oprPkgName = getClassPkgName(oprFullName);
		Pattern pattern = Pattern.compile(JavaSrcElm.OPR_BASE_PKG_NAME_PAT + "|" + JavaSrcElm.OPR_EXT_PKG_NAME_PAT);
		Matcher matcher = pattern.matcher(oprPkgName);
		String daoPkgName = "";
		if (matcher.matches() == true) {
			daoPkgName = matcher.replaceAll(JavaSrcElm.DAO_BASE_PKG_NAME_PAT);
		}

		// Change OPR class class name into DAO class name.
		String oprSimpleName = getClassSimpleName(oprFullName);
		if (oprType == JavaFormatter.OPR_TYPE_INTF) {
			pattern = Pattern.compile(JavaSrcElm.OPR_INTF_SUFFIX);
		} else if (oprType == JavaFormatter.OPR_TYPE_IMPL) {
			pattern = Pattern.compile(JavaSrcElm.OPR_IMPL_SUFFIX);
		} else if (oprType == JavaFormatter.OPR_TYPE_XINTF) {
			pattern = Pattern.compile(JavaSrcElm.OPRX_INTF_SUFFIX);
		} else if (oprType == JavaFormatter.OPR_TYPE_XIMPL) {
			pattern = Pattern.compile(JavaSrcElm.OPRX_IMPL_SUFFIX);
		}
		matcher = pattern.matcher(oprSimpleName);
		String daoSimpleName = "";
		if (matcher.matches() == true) {
			daoSimpleName = JavaSrcElm.INTF_PREFIX + matcher.replaceAll("") + JavaSrcElm.DAO_INTF_SUFFIX;
		}

		String daoFullName = daoPkgName + JavaSrcElm.DOT + daoSimpleName;

		return daoFullName;

	}

	public static String classNameToFileName(String className) {
		String fileName = StringHelper.replace(className, JavaSrcElm.DOT, File.separator);
		return fileName;
	}

	public static String pkgNameToPathName(String pkgName) {
		String pathName = StringHelper.replace(pkgName, JavaSrcElm.DOT, File.separator);
		if (pathName.endsWith(File.separator) == false) {
			pathName = pathName + File.separator;
		}
		return pathName;
	}

}
