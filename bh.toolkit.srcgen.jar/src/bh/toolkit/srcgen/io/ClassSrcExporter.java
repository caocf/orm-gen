package bh.toolkit.srcgen.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.src.AttributeSrc;
import bh.toolkit.srcgen.model.src.ClassSrc;
import bh.toolkit.srcgen.model.src.MethodSrc;
import bh.toolkit.srcgen.util.JavaFormatter;
import bh.toolkit.srcgen.util.StringHelper;

public class ClassSrcExporter {

	private static ClassSrcExporter classSrcExporter;

	private ClassSrcExporter() {
	}

	public synchronized static ClassSrcExporter getInstance() {

		if (classSrcExporter == null) {
			classSrcExporter = new ClassSrcExporter();
		}

		return classSrcExporter;

	}

	public String exportClassSrc(ArtifactSpec artifactSpec, ClassSrc classSrc, int expType) {

		// 如果是MSG PROP则仅导出属性
		StringBuffer srcCode = new StringBuffer();
		if (expType == JavaSrcElm.EXP_TYPE_MSGCD_PROP) {
			srcCode.append(exportAllAttrs(classSrc, expType, true));
			return srcCode.toString();
		}

		/////////////////////////////////////////////////////////
		// 导出包
		StringBuffer pkgSrc = new StringBuffer();
		pkgSrc.append(ExportHelper.exportPackage(classSrc.getPkgName()));

		/////////////////////////////////////////////////////////
		// 导出注解
		StringBuffer commentsSrc = new StringBuffer();
		if (StringUtils.isBlank(classSrc.getComments()) == false) {
			commentsSrc.append(ExportHelper.exportLineComments(classSrc.getComments()));
		}

		/////////////////////////////////////////////////////////
		// 添加@Component标注
		StringBuffer typeStmt = new StringBuffer();
		if (expType == JavaSrcElm.EXP_TYPE_OPR_IMPL || expType == JavaSrcElm.EXP_TYPE_MGR_IMPL) {
			typeStmt.append(ExportHelper.exportAnnot(JavaSrcElm.ANNOT_COMPN));
		}

		/////////////////////////////////////////////////////////
		// 导出类型声明以及继承（实现）声明
		if (expType == JavaSrcElm.EXP_TYPE_VO || expType == JavaSrcElm.EXP_TYPE_DTO || expType == JavaSrcElm.EXP_TYPE_OPR_IMPL || expType == JavaSrcElm.EXP_TYPE_MGR_IMPL
				|| expType == JavaSrcElm.EXP_TYPE_MSGCD_CLS) {
			typeStmt.append(ExportHelper.exportTypeStmt(classSrc, true));
		} else {
			typeStmt.append(ExportHelper.exportTypeStmt(classSrc, false));
		}
		typeStmt.append(ExportHelper.exportSuperCls(classSrc));
		typeStmt.append(ExportHelper.exportOpeningBrace());

		/////////////////////////////////////////////////////////
		// 导出属性
		StringBuffer attrSrc = new StringBuffer();
		// 如果是VO、DTO，导出VerUid
		if (expType == JavaSrcElm.EXP_TYPE_VO || expType == JavaSrcElm.EXP_TYPE_DTO) {
			attrSrc.append(ExportHelper.exportVerUid());
		}
		// 如果是VO导出空对象标识
		if (expType == JavaSrcElm.EXP_TYPE_VO) {
			attrSrc.append(ExportHelper.exportIsBlankObj());
		}
		// 如果是VO、DTO、OPR_IMPL、MGR IMPL，导出Log4J声明
		if (expType == JavaSrcElm.EXP_TYPE_VO || expType == JavaSrcElm.EXP_TYPE_DTO || expType == JavaSrcElm.EXP_TYPE_OPR_IMPL || expType == JavaSrcElm.EXP_TYPE_MGR_IMPL) {
			String classSimpleName = classSrc.getSimpleName();
			attrSrc.append(ExportHelper.exportLog4JStmt(classSimpleName));
		}
		// 导出其他属性
		if (expType == JavaSrcElm.EXP_TYPE_MSGCD_CLS) {
			attrSrc.append(exportAllAttrs(classSrc, expType, true));
		} else if (expType == JavaSrcElm.EXP_TYPE_VO || expType == JavaSrcElm.EXP_TYPE_DTO || expType == JavaSrcElm.EXP_TYPE_OPR_IMPL || expType == JavaSrcElm.EXP_TYPE_MGR_IMPL) {
			attrSrc.append(exportAllAttrs(classSrc, expType, false));
		}

		/////////////////////////////////////////////////////////
		// 导出方法
		// 如果是VO，导出“clone”方法，导出“newEmptyCopy”方法
		if (expType == JavaSrcElm.EXP_TYPE_VO) {
			attrSrc.append(ExportHelper.exportCloneMeth());
			attrSrc.append(JavaSrcElm.LINE_SEPARATOR);
			attrSrc.append(exportNewEmptyCopyMeth(classSrc));
			attrSrc.append(JavaSrcElm.LINE_SEPARATOR);
		}

		// 导出其他方法
		StringBuffer methodSrc = new StringBuffer();
		if (expType != JavaSrcElm.EXP_TYPE_MSGCD_CLS) {
			methodSrc.append(exportAllMethods(classSrc, expType));
			if (expType == JavaSrcElm.EXP_TYPE_VO) { // 如果是VO
				// 导出枚举类型设置方法“setCatSysCode”
				methodSrc.append(exportAllEnumSetters(classSrc.getAttrList()));
				methodSrc.append(JavaSrcElm.LINE_SEPARATOR);
				// 导出标记用于标识空值属性的方法
				methodSrc.append(exportBlankFlagGetter());
			}
		}

		/////////////////////////////////////////////////////////
		// 计算需要的引用
		StringBuffer impSrc = new StringBuffer();
		impSrc.append(ExportHelper.exportAllImport(artifactSpec, classSrc));

		/////////////////////////////////////////////////////////
		// 将各段代码合并处理
		srcCode.append(pkgSrc);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		srcCode.append(impSrc);
		srcCode.append(commentsSrc);
		srcCode.append(typeStmt);
		if (StringUtils.isNotBlank(attrSrc) == true) {
			srcCode.append(JavaSrcElm.LINE_SEPARATOR);
			srcCode.append(attrSrc);
		}
		srcCode.append(methodSrc);
		if (StringUtils.isBlank(methodSrc) == true) {
			srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		}

		srcCode.append(JavaSrcElm.RIGHT_BRACKET);

		return srcCode.toString();

	}

	private String exportAllAttrs(ClassSrc classSrc, int expType, boolean needSort) {

		StringBuffer srcCode = new StringBuffer();
		List<AttributeSrc> attrList = classSrc.getAttrList();
		int attrListSize = attrList.size();
		if (attrListSize == 0) {
			return srcCode.toString();
		}

		// 判断是否需要对属性排序
		if (needSort == true) {
			Collections.sort(attrList);
		}
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		for (AttributeSrc attrSrc : attrList) {
			if (expType == JavaSrcElm.EXP_TYPE_MSGCD_PROP) {
				srcCode.append(exportProp(attrSrc));
			} else {
				srcCode.append(exportAttr(attrSrc));
			}
		}

		return srcCode.toString();

	}

	private String exportAttr(AttributeSrc attrSrc) {

		String indents = ExportHelper.exportIndents();
		StringBuffer srcCode = new StringBuffer();

		String dataTypeFullName = attrSrc.getDataType();
		String dataTypeSimpleName = JavaFormatter.getClassSimpleName(dataTypeFullName);
		srcCode.append(ExportHelper.exportAnnot(indents, attrSrc.getAnnotList()));
		srcCode.append(indents);
		srcCode.append(attrSrc.getModifier());
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(dataTypeSimpleName);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(attrSrc.getName());
		srcCode.append(JavaSrcElm.EQUAL);
		srcCode.append(attrSrc.getDefaultValue());
		srcCode.append(JavaSrcElm.SEMICOLON);
		String comments = (StringUtils.isBlank(attrSrc.getVerbComments()) ? "" : attrSrc.getVerbComments() + JavaSrcElm.WHITE_SPACE)
				+ (StringUtils.isBlank(attrSrc.getObjComments()) ? "" : attrSrc.getObjComments());
		if (StringUtils.isNotBlank(StringUtils.trimToEmpty(comments)) == true) {
			srcCode.append(ExportHelper.exportIndents());
			srcCode.append(JavaSrcElm.LINE_COMMENT);
			srcCode.append(JavaSrcElm.WHITE_SPACE);
			srcCode.append(comments);
		} else {
			srcCode.append(ExportHelper.exportIndents());
			srcCode.append(JavaSrcElm.LINE_COMMENT);
			srcCode.append(JavaSrcElm.WHITE_SPACE);
		}
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();

	}

	private String exportProp(AttributeSrc attrSrc) {

		StringBuffer srcCode = new StringBuffer();
		String defaultValue = attrSrc.getDefaultValue();
		if (defaultValue.startsWith(JavaSrcElm.DOUBLE_QUOTATION) == true) {
			defaultValue = defaultValue.substring(1, defaultValue.length());
		}
		if (defaultValue.endsWith(JavaSrcElm.DOUBLE_QUOTATION) == true) {
			defaultValue = defaultValue.substring(0, defaultValue.length() - 1);
		}
		srcCode.append(defaultValue);
		srcCode.append(JavaSrcElm.EQUAL_WITHOUT_BLANK);
		String comments = (StringUtils.isBlank(attrSrc.getVerbComments()) ? "" : attrSrc.getVerbComments() + JavaSrcElm.WHITE_SPACE)
				+ (StringUtils.isBlank(attrSrc.getObjComments()) ? "" : attrSrc.getObjComments());
		if (StringUtils.isNotBlank(StringUtils.trimToEmpty(comments)) == true) {
			srcCode.append(StringHelper.GBK2Unicode(comments));
		}
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();

	}

	private String exportNewEmptyCopyMeth(ClassSrc classSrc) {

		StringBuffer srcCode = new StringBuffer();

		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(JavaSrcElm.PUBLIC);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(classSrc.getSimpleName());
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.EXPR_NEW_EMPTY_COPY);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.LEFT_BRACKET);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(classSrc.getSimpleName());
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.NEW + classSrc.getSimpleName());
		srcCode.append(JavaSrcElm.EQUAL);
		srcCode.append(JavaSrcElm.NEW);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(classSrc.getSimpleName());
		srcCode.append(JavaSrcElm.CLOSE_PARENTHESIS);
		srcCode.append(JavaSrcElm.SEMICOLON);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		// 拷贝主键
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(JavaSrcElm.NEW + classSrc.getSimpleName());
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append(JavaSrcElm.SET);
		srcCode.append(StringHelper.toUpperCase(classSrc.getPkAttr(), 0));
		srcCode.append(JavaSrcElm.LEFT_PARENTHESIS);
		srcCode.append(JavaSrcElm.THIS);
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append(classSrc.getPkAttr());
		srcCode.append(JavaSrcElm.RIGHT_PARENTHESIS);
		srcCode.append(JavaSrcElm.SEMICOLON);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		// 拷贝创建时间
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(JavaSrcElm.NEW + classSrc.getSimpleName());
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append(JavaSrcElm.SET);
		srcCode.append(StringHelper.toUpperCase("crtTm", 0));
		srcCode.append(JavaSrcElm.LEFT_PARENTHESIS);
		srcCode.append(JavaSrcElm.THIS);
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append("crtTm");
		srcCode.append(JavaSrcElm.RIGHT_PARENTHESIS);
		srcCode.append(JavaSrcElm.SEMICOLON);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		// 拷贝创建人
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(JavaSrcElm.NEW + classSrc.getSimpleName());
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append(JavaSrcElm.SET);
		srcCode.append(StringHelper.toUpperCase("crtBy", 0));
		srcCode.append(JavaSrcElm.LEFT_PARENTHESIS);
		srcCode.append(JavaSrcElm.THIS);
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append("crtBy");
		srcCode.append(JavaSrcElm.RIGHT_PARENTHESIS);
		srcCode.append(JavaSrcElm.SEMICOLON);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		// 拷贝更新时间
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(JavaSrcElm.NEW + classSrc.getSimpleName());
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append(JavaSrcElm.SET);
		srcCode.append(StringHelper.toUpperCase("updTm", 0));
		srcCode.append(JavaSrcElm.LEFT_PARENTHESIS);
		srcCode.append(JavaSrcElm.THIS);
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append("updTm");
		srcCode.append(JavaSrcElm.RIGHT_PARENTHESIS);
		srcCode.append(JavaSrcElm.SEMICOLON);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		// 拷贝更新人
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(JavaSrcElm.NEW + classSrc.getSimpleName());
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append(JavaSrcElm.SET);
		srcCode.append(StringHelper.toUpperCase("updBy", 0));
		srcCode.append(JavaSrcElm.LEFT_PARENTHESIS);
		srcCode.append(JavaSrcElm.THIS);
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append("updBy");
		srcCode.append(JavaSrcElm.RIGHT_PARENTHESIS);
		srcCode.append(JavaSrcElm.SEMICOLON);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		// 拷贝编辑标识
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(JavaSrcElm.NEW + classSrc.getSimpleName());
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append(JavaSrcElm.SET);
		srcCode.append(StringHelper.toUpperCase("editFlag", 0));
		srcCode.append(JavaSrcElm.LEFT_PARENTHESIS);
		srcCode.append(JavaSrcElm.THIS);
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append("editFlag");
		srcCode.append(JavaSrcElm.RIGHT_PARENTHESIS);
		srcCode.append(JavaSrcElm.SEMICOLON);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		// 拷贝版本标识
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(JavaSrcElm.NEW + classSrc.getSimpleName());
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append(JavaSrcElm.SET);
		srcCode.append(StringHelper.toUpperCase("relsNbr", 0));
		srcCode.append(JavaSrcElm.LEFT_PARENTHESIS);
		srcCode.append(JavaSrcElm.THIS);
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append("relsNbr");
		srcCode.append(JavaSrcElm.RIGHT_PARENTHESIS);
		srcCode.append(JavaSrcElm.SEMICOLON);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		// 返回对象拷贝
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(JavaSrcElm.RETURN);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.NEW);
		srcCode.append(classSrc.getSimpleName());
		srcCode.append(JavaSrcElm.SEMICOLON);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append(JavaSrcElm.RIGHT_BRACKET);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();

	}

	private String exportAllMethods(ClassSrc classSrc, int expType) {

		StringBuffer srcCode = new StringBuffer();
		List<MethodSrc> methodSrcList = classSrc.getMethodList();
		int methodListSize = methodSrcList.size();
		if (methodListSize == 0) {
			return srcCode.toString();
		}

		String classFullName = classSrc.getFullName();
		MethodSrc methodSrc = null;
		for (int i = 0; i < methodListSize; i++) {
			methodSrc = methodSrcList.get(i);
			srcCode.append(exportMethod(classFullName, methodSrc, expType));
		}
		if (methodListSize == 0) {
			srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		}
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();

	}

	private String exportMethod(String classFullName, MethodSrc methodSrc, int expType) {

		StringBuffer srcCode = new StringBuffer();
		String indents = ExportHelper.exportIndents();

		// 处理方法的名称、修饰符、返回值
		String comments = methodSrc.getComments();
		List<String> annotList = methodSrc.getAnnotList();
		String returnType = methodSrc.getReturnType();
		String methodName = methodSrc.getName();

		srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		srcCode.append(indents);
		if (StringUtils.isNotBlank(comments) == true) {
			srcCode.append(JavaSrcElm.LINE_COMMENT);
			srcCode.append(JavaSrcElm.WHITE_SPACE);
			srcCode.append(comments);
			srcCode.append(JavaSrcElm.LINE_SEPARATOR);
			srcCode.append(indents);
		}
		if (annotList != null && annotList.size() != 0) {
			for (String annot : annotList) {
				if (StringUtils.isNotBlank(annot)) {
					srcCode.append(annot);
					srcCode.append(JavaSrcElm.LINE_SEPARATOR);
					srcCode.append(indents);
				}
			}
		}
		srcCode.append(StringUtils.isBlank(methodSrc.getModifiers()) == true ? JavaSrcElm.PUBLIC : methodSrc.getModifiers());
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		if (StringUtils.isNotBlank(returnType) == true) {
			srcCode.append(returnType);
			srcCode.append(JavaSrcElm.WHITE_SPACE);
		}
		srcCode.append(methodName);
		srcCode.append(JavaSrcElm.LEFT_PARENTHESIS);

		// 处理输入参数
		List<String> paramTypeList = methodSrc.getParamTypeList();
		int paramListSize = paramTypeList.size();
		if (paramListSize != 0) {
			String paramType = null;
			for (int i = 0; i < paramListSize; i++) {
				if (paramListSize >= JavaSrcElm.MAX_NUM_OF_PARAM) {
					if (i > 0) {
						srcCode.append(JavaSrcElm.COMMA);
					}
					srcCode.append(JavaSrcElm.LINE_SEPARATOR);
					srcCode.append(indents);
					srcCode.append(indents);
				} else {
					if (i > 0) {
						srcCode.append(JavaSrcElm.COMMA);
						srcCode.append(JavaSrcElm.WHITE_SPACE);
					}
				}
				paramType = paramTypeList.get(i);
				srcCode.append(paramType);
				srcCode.append(JavaSrcElm.WHITE_SPACE);
				srcCode.append(methodSrc.getParamNameList().get(i));
			}
		}
		srcCode.append(JavaSrcElm.RIGHT_PARENTHESIS);

		// 处理异常
		if (methodSrc.getExceptionList() != null && methodSrc.getExceptionList().size() > 0) {
			srcCode.append(JavaSrcElm.WHITE_SPACE);
			srcCode.append(JavaSrcElm.THROWS);
			srcCode.append(JavaSrcElm.WHITE_SPACE);
			int expCnt = 0;
			for (String expStmt : methodSrc.getExceptionList()) {
				if (expCnt > 0) {
					srcCode.append(JavaSrcElm.COMMA);
					srcCode.append(JavaSrcElm.WHITE_SPACE);
				}
				srcCode.append(JavaFormatter.getClassSimpleName(expStmt));
				expCnt++;
			}
		}

		// 处理方法体
		if (expType == JavaSrcElm.EXP_TYPE_DAO_INTF || expType == JavaSrcElm.EXP_TYPE_OPR_INTF || expType == JavaSrcElm.EXP_TYPE_MGR_INTF) {
			srcCode.append(JavaSrcElm.SEMICOLON);
			srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		} else if (expType == JavaSrcElm.EXP_TYPE_OPR_IMPL || expType == JavaSrcElm.EXP_TYPE_MGR_IMPL) {
			srcCode.append(JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET);
			srcCode.append(JavaSrcElm.LINE_SEPARATOR);
			srcCode.append(methodSrc.getContentBody());
		} else if (expType == JavaSrcElm.EXP_TYPE_VO || expType == JavaSrcElm.EXP_TYPE_DTO) {
			srcCode.append(JavaSrcElm.WHITE_SPACE);
			srcCode.append(JavaSrcElm.LEFT_BRACKET);
			srcCode.append(JavaSrcElm.LINE_SEPARATOR);
			srcCode.append(indents);
			srcCode.append(indents);
			String attrName = null;
			if (methodName.startsWith(JavaSrcElm.GET) == true) {
				attrName = StringHelper.leftTrim(methodName, JavaSrcElm.GET);
				attrName = StringHelper.toLowerCase(attrName, 0);
				if (expType == JavaSrcElm.EXP_TYPE_VO) { // 导出VO时增加对属性的使用日志
					srcCode.append("logger.trace(\"" + classFullName + "\t<includedAttr name=\\\"" + attrName + "\\\">\");");
					srcCode.append(JavaSrcElm.LINE_SEPARATOR);
					srcCode.append(indents);
					srcCode.append(indents);
				}
				srcCode.append(JavaSrcElm.RETURN);
				srcCode.append(JavaSrcElm.WHITE_SPACE);
				srcCode.append(attrName);
			} else if (methodName.startsWith(JavaSrcElm.SET) == true) {
				attrName = StringHelper.leftTrim(methodName, JavaSrcElm.SET);
				attrName = StringHelper.toLowerCase(attrName, 0);
				// 如果是VO，只要是发生过属性设置，就认为对象为非空
				if (expType == JavaSrcElm.EXP_TYPE_VO) {
					srcCode.append(JavaSrcElm.IS_BLANK_OBJ);
					srcCode.append(JavaSrcElm.EQUAL);
					srcCode.append(JavaSrcElm.FALSE);
					srcCode.append(JavaSrcElm.SEMICOLON);
					srcCode.append(JavaSrcElm.LINE_SEPARATOR);
					srcCode.append(indents);
					srcCode.append(indents);
				}
				srcCode.append(JavaSrcElm.THIS);
				srcCode.append(JavaSrcElm.DOT);
				srcCode.append(attrName);
				srcCode.append(JavaSrcElm.EQUAL);
				srcCode.append(attrName);
			}
			srcCode.append(JavaSrcElm.SEMICOLON);
			srcCode.append(JavaSrcElm.LINE_SEPARATOR);
			srcCode.append(indents);
			srcCode.append(JavaSrcElm.RIGHT_BRACKET);
			srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		}

		return srcCode.toString();

	}

	/**
	 * 为连续出现的，以Cd、NmCn、NmEn结尾的属性提供枚举类型的设置方法
	 * 
	 * @param attrList
	 * @return
	 */
	private String exportAllEnumSetters(List<AttributeSrc> attrList) {

		StringBuffer srcCode = new StringBuffer();

		int attrListSize = attrList.size();
		AttributeSrc attrSrc = null;
		String attrName = null;
		// String attrNameLowerCase = null;
		String cdAttrName = null;
		Pattern cdPat = Pattern.compile(JavaSrcElm.ENUM_CODE_PAT, Pattern.CASE_INSENSITIVE);
		Pattern nmPat = Pattern.compile(JavaSrcElm.ENUM_NAME_PAT, Pattern.CASE_INSENSITIVE);
		Matcher cdMatcher = null;
		Matcher nmMatcher = null;
		List<String> attrSrcNameStack = new ArrayList<String>();
		boolean inEnumBatch = false;
		int catSysCnt = 0;
		for (int i = 0; i < attrListSize; i++) {

			attrSrc = attrList.get(i);
			attrName = attrSrc.getName();
			// attrNameLowerCase = attrName.toLowerCase();

			// 如果当前属性以“cd”结尾，则标记批次开始并缓存当前属性
			cdMatcher = cdPat.matcher(attrName);
			if (cdMatcher.matches() == true) {
				// if (attrNameLowerCase.endsWith(JavaSrcElm.VO_ATTR_CD_SUFFIX.toLowerCase())) {

				// 如果上一批还没结束，并且需要设置枚举类型的属性个数大于等于2，则为他们提供枚举类型方法，并开始新的批次。
				if (inEnumBatch == true) {
					if (catSysCnt >= 2) {
						srcCode.append(exportCodeSetters(cdAttrName, attrSrcNameStack));
					}
					attrSrcNameStack.clear();
					catSysCnt = 0;
				}

				inEnumBatch = true;
				cdAttrName = attrName;
				attrSrcNameStack.add(attrName);
				catSysCnt++;
				continue;

			}
			// 如果当前属性不以“cd”结尾
			else {

				// 如果仍然在一个处理批次里
				if (inEnumBatch == true) {

					// 如果当前属性以“nmCn”或“nmEn”结尾
					nmMatcher = nmPat.matcher(attrName);
					if (nmMatcher.matches() == true) {
						// if (attrNameLowerCase.endsWith(JavaSrcElm.VO_ATTR_NMCN_SUFFIX.toLowerCase())
						// || attrNameLowerCase.endsWith(JavaSrcElm.VO_ATTR_NMEN_SUFFIX.toLowerCase())) {

						attrSrcNameStack.add(attrName);
						catSysCnt++;

						// 如果需要处理的属性个数达到了上限“3”，则强制导出。
						if (catSysCnt >= 3) {
							srcCode.append(exportCodeSetters(cdAttrName, attrSrcNameStack));
							// 复位标志
							attrSrcNameStack.clear();
							cdAttrName = null;
							inEnumBatch = false;
							catSysCnt = 0;
						}

					}
					// 如果当前属性不以“nmCn”或“nmEn”结尾，则检查需处理的属性个数是否达到了2，如果达到则进行导出
					else {

						if (catSysCnt >= 2) {
							srcCode.append(exportCodeSetters(cdAttrName, attrSrcNameStack));
						}
						// 复位标志
						attrSrcNameStack.clear();
						cdAttrName = null;
						inEnumBatch = false;
						catSysCnt = 0;
					}

				}
				// 如果不处于一个处理批次里，则不需要关注。
				else {
					// 保持各标志为初始状态
					attrSrcNameStack.clear();
					cdAttrName = null;
					inEnumBatch = false;
					catSysCnt = 0;
				}

			}

		}

		return srcCode.toString();

	}

	private String exportCodeSetters(String cdAttrName, List<String> attrSrcNameStack) {

		StringBuffer srcCode = new StringBuffer();

		// Process the method modifiers, return value and name.
		String indents = ExportHelper.exportIndents();
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		srcCode.append(indents);
		srcCode.append(JavaSrcElm.PUBLIC);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.VOID);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.SET);
		srcCode.append(StringHelper.toUpperCase(cdAttrName, 0));
		srcCode.append(JavaSrcElm.LEFT_PARENTHESIS);
		srcCode.append(JavaSrcElm.EXPR_CAT_SYS_CD_SIMPLE);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(StringHelper.toLowerCase(JavaSrcElm.EXPR_CAT_SYS_CD_SIMPLE, 0));
		srcCode.append(JavaSrcElm.RIGHT_PARENTHESIS);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.LEFT_BRACKET);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		String attrSrcName = null;
		String attrLowerCaseName = null;
		for (int j = 0; j < attrSrcNameStack.size(); j++) {
			attrSrcName = attrSrcNameStack.get(j);
			attrLowerCaseName = attrSrcName.toLowerCase();
			srcCode.append(indents);
			srcCode.append(indents);
			srcCode.append(JavaSrcElm.THIS);
			srcCode.append(JavaSrcElm.DOT);
			srcCode.append(attrSrcName);
			srcCode.append(JavaSrcElm.WHITE_SPACE);
			srcCode.append(JavaSrcElm.EQUAL);
			srcCode.append(JavaSrcElm.WHITE_SPACE);
			if (attrLowerCaseName.endsWith(JavaSrcElm.VO_ATTR_CD_SUFFIX.toLowerCase())) {
				srcCode.append(JavaSrcElm.EXPR_SET_CD);
			} else if (attrLowerCaseName.endsWith(JavaSrcElm.VO_ATTR_NMCN_SUFFIX.toLowerCase())) {
				srcCode.append(JavaSrcElm.EXPR_SET_NMCN);
			} else if (attrLowerCaseName.endsWith(JavaSrcElm.VO_ATTR_NMEN_SUFFIX.toLowerCase())) {
				srcCode.append(JavaSrcElm.EXPR_SET_NMEN);
			}
			srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		}

		srcCode.append(indents);
		srcCode.append(JavaSrcElm.RIGHT_BRACKET);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();

	}

	/**
	 * 生成用于访问空值标志的方法
	 * 
	 * @param attrList
	 * @return
	 */
	private String exportBlankFlagGetter() {

		StringBuffer srcCode = new StringBuffer();

		String indents = ExportHelper.exportIndents();
		srcCode.append(indents);
		srcCode.append(JavaSrcElm.PUBLIC);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.BOOLEAN);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		// srcCode.append(JavaSrcElm.GET);
		// srcCode.append(StringHelper.toUpperCase(JavaSrcElm.IS_BLANK_OBJ, 0));
		srcCode.append(JavaSrcElm.IS_BLANK_OBJ);
		srcCode.append(JavaSrcElm.LEFT_PARENTHESIS);
		srcCode.append(JavaSrcElm.RIGHT_PARENTHESIS);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.LEFT_BRACKET);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		srcCode.append(indents);
		srcCode.append(indents);
		srcCode.append(JavaSrcElm.RETURN);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.THIS);
		srcCode.append(JavaSrcElm.DOT);
		srcCode.append(JavaSrcElm.IS_BLANK_OBJ);
		srcCode.append(JavaSrcElm.SEMICOLON);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		srcCode.append(indents);
		srcCode.append(JavaSrcElm.RIGHT_BRACKET);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();

	}

}
