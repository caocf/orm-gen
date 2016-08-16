/**
 * Description: 
 * Author: zhaoruibin
 * Creation time: 2015年11月17日 上午10:21:15
 * (C) Copyright 2013-2016, Cloud Business Chain Corporation Limited.
 * All rights reserved.
 */
package bh.toolkit.srcgen.builder;

import org.apache.commons.lang3.StringUtils;

import bh.toolkit.srcgen.io.ExportHelper;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.model.artifact.MethodSpec;
import bh.toolkit.srcgen.model.src.MethodSrc;
import bh.toolkit.srcgen.registry.CtxCacheFacade;
import bh.toolkit.srcgen.util.StringHelper;

public class JavaSrcBuilder {

	protected String currentIndents = null;

	protected void beginMethodBody(StringBuffer methodBody, MethodSrc methodSrc) {

		// 初始化返回值
		initCurrentIndents();
		increaseIndents();
		methodBody.append(JavaSrcElm.LINE_SEPARATOR);
		String returnType = methodSrc.getReturnType();
		if (StringUtils.isNotBlank(returnType) == true && returnType.equalsIgnoreCase(JavaSrcElm.INT) == true) {
			methodBody.append(currentIndents + returnType + JavaSrcElm.WHITE_SPACE + methodSrc.getReturnName() + JavaSrcElm.EQUAL + JavaSrcElm.ZERO
					+ JavaSrcElm.SEMICOLON);
		} else {
			methodBody.append(currentIndents + returnType + JavaSrcElm.WHITE_SPACE + methodSrc.getReturnName() + JavaSrcElm.EQUAL + JavaSrcElm.NULL
					+ JavaSrcElm.SEMICOLON);
		}
		methodBody.append(JavaSrcElm.LINE_SEPARATOR);
		methodBody.append(JavaSrcElm.LINE_SEPARATOR);

		// 声明异常开始
		methodBody.append(currentIndents + JavaSrcElm.TRY + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET);
		methodBody.append(JavaSrcElm.LINE_SEPARATOR);
		methodBody.append(JavaSrcElm.LINE_SEPARATOR);
		increaseIndents();

	}

	protected void endMethodBody(StringBuffer methodBody, MethodSrc oprMethod, String errCode) {

		// 声明异常结束
		decreaseIndents();
		methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET + JavaSrcElm.WHITE_SPACE + JavaSrcElm.CATCH_THROABLE + JavaSrcElm.WHITE_SPACE
				+ JavaSrcElm.LEFT_BRACKET + JavaSrcElm.LINE_SEPARATOR);
		increaseIndents();
		methodBody.append(currentIndents + "throwException(new AppException(SvcMsgCode._" + errCode + ", null, t), logger);");
		methodBody.append(JavaSrcElm.WHITE_SPACE + JavaSrcElm.LINE_COMMENT + JavaSrcElm.WHITE_SPACE + "public static final String _" + errCode
				+ JavaSrcElm.EQUAL + "\"" + errCode + "\"; // " + oprMethod.getComments() + " Failure" + JavaSrcElm.LINE_SEPARATOR);
		decreaseIndents();
		methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET + JavaSrcElm.LINE_SEPARATOR);
		methodBody.append(JavaSrcElm.LINE_SEPARATOR);

		// 返回值
		methodBody.append(currentIndents + JavaSrcElm.RETURN + JavaSrcElm.WHITE_SPACE + oprMethod.getReturnName() + JavaSrcElm.SEMICOLON
				+ JavaSrcElm.LINE_SEPARATOR);
		methodBody.append(JavaSrcElm.LINE_SEPARATOR);
		decreaseIndents();
		methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET + JavaSrcElm.LINE_SEPARATOR);

	}

	protected void buildSectionComments(StringBuffer methodBody, MethodSpec methodSpec) {
		String comments = methodSpec.getDesc();
		if (StringUtils.isNotBlank(comments) == true) {
			methodBody.append(currentIndents + JavaSrcElm.LINE_COMMENT + JavaSrcElm.WHITE_SPACE + comments + JavaSrcElm.LINE_SEPARATOR);
		}
	}

	protected String initCurrentIndents() {
		if (currentIndents == null) {
			currentIndents = ExportHelper.exportIndents();
		}
		return currentIndents;
	}

	protected String increaseIndents() {
		currentIndents = currentIndents + ExportHelper.exportIndents();
		return currentIndents;
	}

	protected String decreaseIndents() {
		String indents = ExportHelper.exportIndents();
		if (currentIndents != null) {
			if (currentIndents.length() > indents.length()) {
				currentIndents = currentIndents.substring(0, currentIndents.length() - indents.length());
				return currentIndents;
			}
		}
		return "";
	}

	protected void cleanIndents() {
		currentIndents = null;
	}

	protected void judgeNullPKCondition(StringBuffer methodBody, String voFullName, String voParamName) {

		// 如果旧值的主键为空，在抛出异常
		String pkName = CtxCacheFacade.lookupVoClass(voFullName).getPkAttr();
		methodBody.append(currentIndents + JavaSrcElm.IF + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_PARENTHESIS + voParamName + JavaSrcElm.DOT
				+ JavaSrcElm.GET + StringHelper.toUpperCase(pkName, 0) + JavaSrcElm.CLOSE_PARENTHESIS + JavaSrcElm.EXPR_OBJ_IS_NULL
				+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET + JavaSrcElm.LINE_SEPARATOR);
		increaseIndents();
		methodBody.append(currentIndents + "throwException(new AppException(SvcMsgCode._810025, new String[] {\"" + voParamName + JavaSrcElm.DOT
				+ JavaSrcElm.GET + StringHelper.toUpperCase(pkName, 0) + JavaSrcElm.CLOSE_PARENTHESIS + "\"}, null), logger);"
				+ JavaSrcElm.LINE_SEPARATOR);
		decreaseIndents();
		methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET + JavaSrcElm.LINE_SEPARATOR);

	}

	protected void judgeNullSqlCondition(StringBuffer methodBody, String dtoXSimpleName) {

		// 如果whereClause为空，在抛出异常
		methodBody.append(currentIndents + JavaSrcElm.IF + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_PARENTHESIS
				+ StringHelper.toLowerCase(dtoXSimpleName, 0) + ".getMapperSqlClause().getWhereClause()" + JavaSrcElm.EXPR_OBJ_IS_NULL
				+ JavaSrcElm.WHITE_SPACE + JavaSrcElm.LOGIC_OR + JavaSrcElm.WHITE_SPACE + StringHelper.toLowerCase(dtoXSimpleName, 0)
				+ ".getMapperSqlClause().getWhereClause().toString().trim().length()" + JavaSrcElm.EXPR_VALUE_IS_ZERO + JavaSrcElm.RIGHT_PARENTHESIS
				+ JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET + JavaSrcElm.LINE_SEPARATOR);
		increaseIndents();
		methodBody.append(
				currentIndents + "throwException(new AppException(SvcMsgCode._810025, new String[] {\"" + StringHelper.toLowerCase(dtoXSimpleName, 0)
						+ ".getMapperSqlClause().getWhereClause()" + "\"}, null), logger);" + JavaSrcElm.LINE_SEPARATOR);
		decreaseIndents();
		methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET + JavaSrcElm.LINE_SEPARATOR);

	}

	protected void copyVoAttrStr2Tm(StringBuffer methodBody, String voParamName) {
		methodBody.append(
				currentIndents + JavaSrcElm.EXPR_COPY_STR2TM + JavaSrcElm.LEFT_PARENTHESIS + voParamName + JavaSrcElm.COMMA + JavaSrcElm.WHITE_SPACE
						+ JavaSrcElm.LOG4J_LOGGER_OBJ + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
	}

}
