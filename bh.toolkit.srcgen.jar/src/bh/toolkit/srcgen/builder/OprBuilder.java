package bh.toolkit.srcgen.builder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.InsertSpec;
import bh.toolkit.srcgen.model.artifact.MethodSpec;
import bh.toolkit.srcgen.model.artifact.OprMethodSpec;
import bh.toolkit.srcgen.model.artifact.OprSpec;
import bh.toolkit.srcgen.model.artifact.RelationshipSpec;
import bh.toolkit.srcgen.model.artifact.UpdateSpec;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;
import bh.toolkit.srcgen.model.src.AttributeSrc;
import bh.toolkit.srcgen.model.src.ClassSrc;
import bh.toolkit.srcgen.model.src.MethodSrc;
import bh.toolkit.srcgen.registry.CtxCacheFacade;
import bh.toolkit.srcgen.util.ExceptionUtil;
import bh.toolkit.srcgen.util.JavaFormatter;
import bh.toolkit.srcgen.util.MapperFormatter;
import bh.toolkit.srcgen.util.StringHelper;

public class OprBuilder extends JavaSrcBuilder {

	private static Logger logger = Logger.getLogger(OprBuilder.class);

	public void buildAllOpr(ArtifactSpec artifactSpec, List<OprSpec> oprSpecList) throws AppException, IllegalArgumentException,
			ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, IOException {

		// 构建所有OPR相关资源，包括接口和实现
		logger.debug("BUILD OPR: Begin to build operation ...");
		for (OprSpec oprSpec : oprSpecList) {
			buildSingleOpr(artifactSpec, oprSpec);
		}

	}

	/**
	 * Description: 构建所有OPR接口的方法，以及对应的实现
	 * Author: zhaoruibin
	 * Creation time: 2015年11月15日 下午10:33:06
	 */
	private void buildSingleOpr(ArtifactSpec artifactSpec, OprSpec oprSpec) throws IllegalArgumentException, ClassNotFoundException,
			IllegalAccessException, InstantiationException, InvocationTargetException, IOException, AppException {

		// 构建”Add“方法
		buildAdd(artifactSpec, oprSpec);

		// 构建”Change“方法
		buildChangeByPK(artifactSpec, oprSpec);

		// 构建“Remove”方法
		buildRemoveByPK(artifactSpec, oprSpec);
		buildRemoveBySql(artifactSpec, oprSpec);

		// 构建“Delete”方法
		// buildDelete(artifactSpec, oprSpec);

		// 如果是OPR实现，还需要调用所对应的DAO中定义的方法
		// buildDaoMethod(artifactSpec, oprSpec);

	}

	//	private void buildDelete(ArtifactSpec artifactSpec, OprSpec oprSpec) throws IllegalArgumentException, ClassNotFoundException,
	//			IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
	//
	//		// 读取全局配置
	//		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
	//
	//		// 构建 "remove"方法
	//		List<OprMethodSpec> removeSpecList = oprSpec.getRemoveSpec();
	//		String removeMethodName = null;
	//		String removeSpecTableName = null;
	//		MethodSrc oprRemoveMethod = null;
	//		StringBuffer methodBody = new StringBuffer();
	//		for (OprMethodSpec removeSpec : removeSpecList) {
	//
	//			// 注册一个OPR IMPL的remove方法
	//			removeMethodName = JavaFormatter.getMethodName(workspaceSpec, removeSpec, JavaSrcElm.VERB_REMOVE);
	//			removeSpecTableName = removeSpec.getTableName();
	//			List<String> oprIntfParamTypeNameList = new ArrayList<String>();
	//			List<String> oprImplParamTypeNameList = new ArrayList<String>();
	//			String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, removeSpecTableName);
	//			oprIntfParamTypeNameList.add(dtoXFullName);
	//			oprImplParamTypeNameList.add(dtoXFullName);
	//			String comments = removeSpec.getDesc();
	//			CtxCacheFacade.addOprIntfMethod(artifactSpec, oprSpec, removeMethodName, oprIntfParamTypeNameList, JavaSrcElm.INT, comments);
	//			oprRemoveMethod = CtxCacheFacade.addOprImplMethod(artifactSpec, oprSpec, removeMethodName, oprImplParamTypeNameList, JavaSrcElm.INT,
	//					comments);
	//
	//			// 初始化返回值
	//			initCurrentIndents();
	//			increaseIndents();
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			methodBody.append(currentIndents + oprRemoveMethod.getReturnType() + JavaSrcElm.WHITE_SPACE + oprRemoveMethod.getReturnName()
	//					+ JavaSrcElm.EQUAL + JavaSrcElm.ZERO + JavaSrcElm.SEMICOLON);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//
	//			// 声明异常开始
	//			methodBody.append(currentIndents + JavaSrcElm.TRY + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			increaseIndents();
	//
	//			//			// 根据insertSpec定义基于DAOX的insert调用
	//			//			for (InsertSpec rootInsertSpec : addSpec.getInsertSpec()) {
	//			//
	//			//				// 把DAOX作为OPR IMPL的属性，并增加对它的引用
	//			//				String oprImplFullName = JavaFormatter.getOprImplFullName(workspaceSpec, oprSpec.getShortName());
	//			//				ClassSrc oprImpl = CtxCacheFacade.lookupOprImpl(oprImplFullName);
	//			//
	//			//				// 调用DAOX的insert接口
	//			//				buildOneToOne(oprNewMethod, methodBody, artifactSpec, addSpec, null, null, rootInsertSpec, oprImpl);
	//			//
	//			//			}
	//
	//			// 声明异常结束
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			decreaseIndents();
	//			methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET + JavaSrcElm.WHITE_SPACE + JavaSrcElm.CATCH_THROABLE + JavaSrcElm.WHITE_SPACE
	//					+ JavaSrcElm.LEFT_BRACKET);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			increaseIndents();
	//			methodBody.append(currentIndents + JavaSrcElm.EXCEPTION_UTIL_HANDLE_EXCEPTION + JavaSrcElm.LEFT_PARENTHESIS
	//					+ "SvcMsgCode.REMOVE_OPERATION_FAILURE" + JavaSrcElm.COMMA + JavaSrcElm.WHITE_SPACE + JavaSrcElm.NULL + JavaSrcElm.COMMA
	//					+ JavaSrcElm.WHITE_SPACE + JavaSrcElm.PARAM_T + JavaSrcElm.COMMA + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LOG4J_LOGGER_OBJ
	//					+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			decreaseIndents();
	//			methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//
	//			// 返回值
	//			methodBody.append(currentIndents + JavaSrcElm.RETURN + JavaSrcElm.WHITE_SPACE + oprRemoveMethod.getReturnName() + JavaSrcElm.SEMICOLON);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			decreaseIndents();
	//			methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//
	//			// 填充方法体到方法对象
	//			oprRemoveMethod.setContentBody(methodBody.toString());
	//
	//			methodBody.delete(0, methodBody.length());
	//
	//		}
	//
	//		// 一个完整的方法构建完毕，清除缩进设定
	//		cleanIndents();
	//
	//	}

	//	/**
	//	 * Description: 
	//	 * Author: zhaoruibin
	//	 * Creation time: 2015年10月22日 下午10:28:58
	//	 *
	//	 * @param artifactSpec
	//	 * @param oprSpec
	//	 * @throws IllegalArgumentException
	//	 * @throws ClassNotFoundException
	//	 * @throws IllegalAccessException
	//	 * @throws InstantiationException
	//	 * @throws InvocationTargetException
	//	 * @throws IOException
	//	 */
	//	private void buildDaoMethod(ArtifactSpec artifactSpec, OprSpec oprSpec) throws IllegalArgumentException, ClassNotFoundException,
	//			IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
	//
	//		// 读取全局配置
	//		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
	//
	//		// 把DAO的引用也加入到当前OPR实现中
	//		String daoIntfFullName = JavaFormatter.getDaoFullName(workspaceSpec, oprSpec.getShortName());
	//		String daoXIntfFullName = JavaFormatter.getDaoXFullName(workspaceSpec, oprSpec.getShortName());
	//		String daoXIntfSimpleName = JavaFormatter.getClassSimpleName(daoXIntfFullName);
	//		String oprImplFullName = JavaFormatter.getOprImplFullName(workspaceSpec, oprSpec.getShortName());
	//		ClassSrc daoIntf = CtxCacheFacade.lookupDaoIntf(daoIntfFullName);
	//		ClassSrc oprImpl = CtxCacheFacade.lookupOprImpl(oprImplFullName);
	//		List<String> importList = oprImpl.getImportList();
	//		HashSet<String> importIdx = oprImpl.getImportIdx();
	//		Iterator<String> daoImpKeys = daoIntf.getImportIdx().iterator();
	//		String daoImpKey = null;
	//		while (daoImpKeys.hasNext() == true) {
	//			daoImpKey = daoImpKeys.next();
	//			if (importIdx.contains(daoImpKey) == false) {
	//				importList.add(daoImpKey);
	//				importIdx.add(daoImpKey);
	//			}
	//		}
	//
	//		// 构建调用DAOX的方法
	//		List<MethodSrc> methodSrcList = daoIntf.getMethodList();
	//		MethodSrc oprImplMethod = null;
	//		String methodComments = null;
	//		String returnTypeFullName = null;
	//		String returnTypeSimpleName = null;
	//		String returnVarName = null;
	//		StringBuffer methodBody = new StringBuffer();
	//		AttributeSrc daoXIntf = null;
	//		int methodCnt = 0;
	//		initCurrentIndents();
	//		for (MethodSrc methodSrc : methodSrcList) {
	//
	//			// 在调用第一个DAOX方法之前，添加注释
	//			if (methodCnt == 0) {
	//				methodComments = "============================== 实现 DAO 的方法 ==============================" + JavaSrcElm.LINE_SEPARATOR
	//						+ ExportHelper.exportIndents() + JavaSrcElm.LINE_COMMENT + JavaSrcElm.WHITE_SPACE + methodSrc.getComments();
	//			} else {
	//				methodComments = methodSrc.getComments();
	//			}
	//
	//			// 把DAOX作为oprImpl的属性
	//			daoXIntf = new AttributeSrc();
	//			daoXIntf.addAnnot(JavaSrcElm.ANNOT_AUTOWIRED);
	//			daoXIntf.setName(StringHelper.toLowerCase(daoXIntfSimpleName, 0));
	//			daoXIntf.setModifier(JavaSrcElm.PROTECTED);
	//			daoXIntf.setDefaultValue(JavaSrcElm.NULL);
	//			daoXIntf.setDataType(daoXIntfFullName);
	//			oprImpl.addAttr(daoXIntf, false);
	//
	//			// 把DAOX加入到当前oprImpl的import中
	//			oprImpl.addImport(daoXIntfFullName);
	//
	//			// 注册一个调用DAOX的方法
	//			returnTypeFullName = methodSrc.getReturnType();
	//			returnTypeSimpleName = JavaFormatter.getClassSimpleName(returnTypeFullName);
	//			returnVarName = JavaSrcElm.VAR_PREFIX + JavaFormatter.getParamName(returnTypeSimpleName, true);
	//			oprImplMethod = CtxCacheFacade.addOprImplMethod(artifactSpec, oprSpec, methodSrc.getName(), methodSrc.getParamTypeList(),
	//					returnTypeFullName, methodComments);
	//
	//			// 初始化返回值
	//			increaseIndents();
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			methodBody.append(currentIndents + returnTypeSimpleName + JavaSrcElm.WHITE_SPACE + returnVarName + JavaSrcElm.EQUAL);
	//			// DAOX的返回值，目前除了“int”就是对象类型
	//			if (returnTypeSimpleName.equalsIgnoreCase(JavaSrcElm.INT) == true) {
	//				methodBody.append(JavaSrcElm.ZERO);
	//			} else {
	//				methodBody.append(JavaSrcElm.NULL);
	//			}
	//			methodBody.append(JavaSrcElm.SEMICOLON);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//
	//			// 声明异常开始
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			methodBody.append(currentIndents + JavaSrcElm.TRY + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//
	//			// 调用DAOX的方法
	//			increaseIndents();
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			methodBody.append(currentIndents + returnVarName + JavaSrcElm.EQUAL + StringHelper.toLowerCase(daoXIntfSimpleName, 0) + JavaSrcElm.DOT
	//					+ methodSrc.getName() + JavaSrcElm.LEFT_PARENTHESIS);
	//			List<String> paramNameList = methodSrc.getParamNameList();
	//			String paramName = null;
	//			for (int i = 0; i < paramNameList.size(); i++) {
	//				paramName = paramNameList.get(i);
	//				methodBody.append(paramName);
	//				if (i > 0) {
	//					methodBody.append(JavaSrcElm.COMMA + JavaSrcElm.WHITE_SPACE);
	//				}
	//			}
	//			methodBody.append(JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//
	//			// 声明异常结束
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			decreaseIndents();
	//			methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET + JavaSrcElm.WHITE_SPACE + JavaSrcElm.CATCH_THROABLE + JavaSrcElm.WHITE_SPACE
	//					+ JavaSrcElm.LEFT_BRACKET);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			increaseIndents();
	//			methodBody.append(currentIndents + JavaSrcElm.EXCEPTION_UTIL_HANDLE_EXCEPTION + JavaSrcElm.LEFT_PARENTHESIS
	//					+ "SvcMsgCode.CHANGE_OPERATION_FAILURE" + JavaSrcElm.COMMA + JavaSrcElm.WHITE_SPACE + JavaSrcElm.NULL + JavaSrcElm.COMMA
	//					+ JavaSrcElm.WHITE_SPACE + JavaSrcElm.PARAM_T + JavaSrcElm.COMMA + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LOG4J_LOGGER_OBJ
	//					+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			decreaseIndents();
	//			methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//
	//			// 返回值
	//			methodBody.append(currentIndents + JavaSrcElm.RETURN + JavaSrcElm.WHITE_SPACE + returnVarName + JavaSrcElm.SEMICOLON);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//			decreaseIndents();
	//			methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET);
	//			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
	//
	//			// 填充方法体到方法对象
	//			oprImplMethod.setContentBody(methodBody.toString());
	//
	//			methodBody.delete(0, methodBody.length());
	//			methodCnt++;
	//
	//		}
	//
	//		// 一个完整的方法构建完毕，清除缩进设定
	//		cleanIndents();
	//
	//	}

	private void buildAdd(ArtifactSpec artifactSpec, OprSpec oprSpec) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			List<OprMethodSpec> addSpecList = oprSpec.getAddSpec();
			String addMethodName = null;
			//			String addSpecTableName = null;
			MethodSrc oprAddMethod = null;
			//			String methodComments = null;

			// 构建OPR方法
			StringBuffer methodBody = new StringBuffer();
			initCurrentIndents();
			for (OprMethodSpec addSpec : addSpecList) {

				// 注册一个OPR的add方法
				addMethodName = JavaFormatter.getMethodName(workspaceSpec, addSpec, JavaSrcElm.VERB_ADD);
				oprAddMethod = registerOprMethod(artifactSpec, oprSpec, addSpec, addMethodName, JavaSrcElm.VERB_ADD);

				// 开始方法体：初始化返回值、声明异常开始
				beginMethodBody(methodBody, oprAddMethod);

				// 根据insertSpec，构建对DAOX的insert调用
				for (InsertSpec rootInsertSpec : addSpec.getInsertSpec()) {

					// 根据oneToOne构建对DAOX的insert调用
					String oprImplFullName = JavaFormatter.getOprImplFullName(workspaceSpec, oprSpec.getShortName());
					ClassSrc oprImpl = CtxCacheFacade.lookupOprImpl(oprImplFullName);
					buildOneToOneInsert(oprAddMethod, methodBody, artifactSpec, addSpec.getTableName(), null, null, null, rootInsertSpec, oprImpl);

				}

				// 结束方法体
				endMethodBody(methodBody, oprAddMethod, JavaSrcElm.MSG_CODE_ADD_ERR);

				// 填充方法体到方法对象
				oprAddMethod.setContentBody(methodBody.toString());

				methodBody.delete(0, methodBody.length());

			}

			// 一个完整的方法构建完毕，清除缩进设定
			cleanIndents();

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	/**
	 * Description: 在Ant配置文件中，insertSpec的上一层有可能是addSpec，也有可能是oneToOneSpec
	 * Author: zhaoruibin
	 * Creation time: 2015年11月15日 下午11:04:33
	 */
	private void buildOneToOneInsert(MethodSrc oprMethod, StringBuffer methodBody, ArtifactSpec artifactSpec, String paramTableName,
			RelationshipSpec parentRelationship, InsertSpec parentInsertSpec, RelationshipSpec currentOneToOne, InsertSpec currentInsertSpec,
			ClassSrc oprImpl) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// 获取insertSpec的comments，作为对代码段的注释
			buildSectionComments(methodBody, currentInsertSpec);

			// insertSpec的tableName决定了VO的名字
			String insertTableName = currentInsertSpec.getTableName();
			String insertVoFullName = JavaFormatter.getVoFullName(workspaceSpec, insertTableName);
			String insertVoSimpleName = JavaFormatter.getClassSimpleName(insertVoFullName);
			String refToSon = currentOneToOne != null ? currentOneToOne.getRefToSon() : null;
			String insertVoParamName = StringUtils.isBlank(refToSon) == true ? StringHelper.toLowerCase(insertVoSimpleName, 0) : refToSon;
			// 把VO加入到当前oprImpl的import中
			oprImpl.addImport(insertVoFullName);
			// 把VO加入到paramTableName所指向的DTO
			CtxCacheFacade.addDtoAttr(artifactSpec, paramTableName, insertTableName, insertVoParamName, JavaSrcElm.VERB_ADD, false);

			// paramTableName决定了DTOX，从DTOX中获取VO
			String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, paramTableName);
			String dtoXSimpleName = JavaFormatter.getClassSimpleName(dtoXFullName);
			methodBody.append(currentIndents + insertVoSimpleName + JavaSrcElm.WHITE_SPACE + insertVoParamName + JavaSrcElm.EQUAL);
			// 命名用以获取VO的get方法名，如果oneToOne所提供的refToSon为空，则采用insertSpec的tableName计算得出的名称
			methodBody.append(StringHelper.toLowerCase(dtoXSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.GET
					+ StringHelper.toUpperCase(insertVoParamName, 0) + JavaSrcElm.CLOSE_PARENTHESIS + JavaSrcElm.SEMICOLON);
			methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			// 如果不应跳过insert谓词，则调用DAO
			if (shouldSkipVerb(currentInsertSpec, MapperElm.SQL_INSERT) == false) {

				// 非空判断
				methodBody.append(currentIndents + JavaSrcElm.IF + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_PARENTHESIS
						+ StringHelper.toLowerCase(insertVoParamName, 0) + JavaSrcElm.EXPR_OBJ_NOT_NULL + JavaSrcElm.RIGHT_PARENTHESIS
						+ JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

				// 根据对应String更新VO中Date和Timestamp类型的值
				increaseIndents();
				copyVoAttrStr2Tm(methodBody, insertVoParamName);

				// 如果parentInsertSpec不为空，则表明不是顶级的oneToOne调用，需要将父VO的ID赋值给子VO
				if (parentInsertSpec != null) {
					updateParentId(methodBody, workspaceSpec, parentInsertSpec, parentRelationship, currentOneToOne, insertVoParamName);
				}

				// 如果设置了属性初始值，则需要赋值
				if (StringUtils.isNotBlank(currentInsertSpec.getInsertExpr()) == true) {
					updateInitValue(methodBody, currentIndents + StringHelper.toLowerCase(insertVoParamName, 0), currentInsertSpec.getInsertExpr());
				}

				// 设置crtTm、crtBy
				buildCreationSignature(methodBody, dtoXSimpleName, insertVoParamName);

				// insertSpec的tableName决定了DAOX的名字
				String daoXIntfFullName = JavaFormatter.getDaoXFullName(workspaceSpec, insertTableName);
				String daoXIntfSimpleName = JavaFormatter.getClassSimpleName(daoXIntfFullName);

				// 把DAOX作为oprImpl的属性
				prepareDaoXIntf(oprImpl, daoXIntfFullName, daoXIntfSimpleName);

				// 调用DAOX的insert方法，如果是本次调用是顶级的oneToOne调用DAOX，则记录返回值
				String insertId = MapperFormatter.getInsertId(currentInsertSpec, false, false);
				if (parentInsertSpec == null) {
					methodBody.append(currentIndents + oprMethod.getReturnName() + JavaSrcElm.EQUAL + StringHelper.toLowerCase(daoXIntfSimpleName, 0)
							+ JavaSrcElm.DOT + insertId + JavaSrcElm.LEFT_PARENTHESIS + insertVoParamName + JavaSrcElm.RIGHT_PARENTHESIS
							+ JavaSrcElm.SEMICOLON);
				} else {
					methodBody.append(currentIndents + StringHelper.toLowerCase(daoXIntfSimpleName, 0) + JavaSrcElm.DOT + insertId
							+ JavaSrcElm.LEFT_PARENTHESIS + insertVoParamName + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
				}

				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
				decreaseIndents();
				methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			} else {
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
			}

			// 如果存在oneToOne，则继续调用
			List<RelationshipSpec> childOneToOneList = currentInsertSpec.getOneToOne();
			List<InsertSpec> childInsertSpecList = null;
			for (RelationshipSpec childOneToOne : childOneToOneList) {
				childInsertSpecList = childOneToOne.getInsertSpec();
				for (InsertSpec childInsertSpec : childInsertSpecList) {
					buildOneToOneInsert(oprMethod, methodBody, artifactSpec, paramTableName, currentOneToOne, currentInsertSpec, childOneToOne,
							childInsertSpec, oprImpl);
				}
			}

			// 如果存在oneToMany，则继续调用
			List<RelationshipSpec> childOneToManyList = currentInsertSpec.getOneToMany();
			for (RelationshipSpec childOneToMany : childOneToManyList) {
				childInsertSpecList = childOneToMany.getInsertSpec();
				for (InsertSpec childInsertSpec : childInsertSpecList) {
					buildOneToManyInsert(oprMethod, methodBody, artifactSpec, paramTableName, null, currentInsertSpec, childOneToMany,
							childInsertSpec, oprImpl);
				}
			}

			// 如果存在manyToMany，则继续调用
			List<RelationshipSpec> childManyToManyList = currentInsertSpec.getManyToMany();
			for (RelationshipSpec childManyToMany : childManyToManyList) {
				childInsertSpecList = childManyToMany.getInsertSpec();
				// 在manyToMany元素下，子insertSpec元素只能出现两次
				if (childInsertSpecList == null) {
					throw new IllegalArgumentException("在manyToMany元素下必须设置insertSpec。");
				} else if (childInsertSpecList.size() != 2) {
					throw new IllegalArgumentException("在manyToMany元素下必须设置2个并列的insertSpec。");
				}
				buildManyToManyInsert(oprMethod, methodBody, artifactSpec, paramTableName, null, currentInsertSpec, childManyToMany,
						childInsertSpecList, oprImpl);
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void buildOneToManyInsert(MethodSrc oprMethod, StringBuffer methodBody, ArtifactSpec artifactSpec, String paramTableName,
			RelationshipSpec parentRelationship, InsertSpec parentInsertSpec, RelationshipSpec currentOneToMany, InsertSpec currentInsertSpec,
			ClassSrc oprImpl) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// 获取insertSpec的comments，作为对代码段的注释
			buildSectionComments(methodBody, currentInsertSpec);

			// insertSpec的tableName决定了子DTOX的名字
			String insertTableName = currentInsertSpec.getTableName();
			String insertDtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, insertTableName);
			String insertDtoXSimpleName = JavaFormatter.getClassSimpleName(insertDtoXFullName);
			String listOfSon = currentOneToMany != null ? currentOneToMany.getListOfSon() : null;
			String insertDtoXParamName = StringUtils.isBlank(listOfSon) == true
					? StringHelper.toLowerCase(insertDtoXSimpleName, 0) + JavaSrcElm.UTIL_LIST_SIMPLE : listOfSon;
			// 把DTOX和java.util.List加入到当前oprImpl的import中
			oprImpl.addImport(JavaSrcElm.UTIL_LIST_FULL);
			oprImpl.addImport(insertDtoXFullName);
			// 把DTOX加入到父DTO中
			CtxCacheFacade.addDtoAttr(artifactSpec, paramTableName, insertTableName, insertDtoXParamName, JavaSrcElm.VERB_CHANGE, true);

			// paramTableName决定了父DTOX，从父DTOX中获取子DTOX的列表
			String paramTableDtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, paramTableName);
			String paramTableDtoXSimpleName = JavaFormatter.getClassSimpleName(paramTableDtoXFullName);
			methodBody.append(currentIndents + JavaSrcElm.UTIL_LIST_SIMPLE + JavaSrcElm.LESS_THAN + insertDtoXSimpleName + JavaSrcElm.LARGER_THAN
					+ JavaSrcElm.WHITE_SPACE + insertDtoXParamName + JavaSrcElm.EQUAL + StringHelper.toLowerCase(paramTableDtoXSimpleName, 0)
					+ JavaSrcElm.DOT + JavaSrcElm.GET + StringHelper.toUpperCase(insertDtoXParamName, 0) + JavaSrcElm.CLOSE_PARENTHESIS
					+ JavaSrcElm.SEMICOLON);
			methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			/////////////////////////////////////// For 循环
			// 根据oneToMany要求执行for循环
			methodBody.append(currentIndents + JavaSrcElm.FOR + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_PARENTHESIS + insertDtoXSimpleName
					+ JavaSrcElm.WHITE_SPACE + StringHelper.toLowerCase(insertDtoXSimpleName, 0) + JavaSrcElm.COLON_WITH_BLANK + insertDtoXParamName
					+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET);
			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
			methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			// 如果是本次调用是首次访问DAOX，则记录返回值
			if (parentInsertSpec == null) {
				methodBody.append(currentIndents + oprMethod.getReturnName() + JavaSrcElm.DOUBLE_PLUS);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
			}

			// insertSpec的tableName决定了VO的名字
			String insertVoFullName = JavaFormatter.getVoFullName(workspaceSpec, insertTableName);
			String insertVoSimpleName = JavaFormatter.getClassSimpleName(insertVoFullName);
			// 把VO加入到当前oprImpl的import中
			oprImpl.addImport(insertVoFullName);
			increaseIndents();
			methodBody.append(currentIndents + insertVoSimpleName + JavaSrcElm.WHITE_SPACE + StringHelper.toLowerCase(insertVoSimpleName, 0)
					+ JavaSrcElm.EQUAL + StringHelper.toLowerCase(insertDtoXSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.GET + insertVoSimpleName
					+ JavaSrcElm.CLOSE_PARENTHESIS + JavaSrcElm.SEMICOLON);
			methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			// 如果不应跳过insert谓词，则调用DAO
			if (shouldSkipVerb(currentInsertSpec, MapperElm.SQL_INSERT) == false) {

				// 非空判断
				methodBody.append(currentIndents + JavaSrcElm.IF + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_PARENTHESIS
						+ StringHelper.toLowerCase(insertVoSimpleName, 0) + JavaSrcElm.EXPR_OBJ_NOT_NULL + JavaSrcElm.RIGHT_PARENTHESIS
						+ JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

				// 根据对应String更新VO中Date和Timestamp类型的值increaseIndents();
				increaseIndents();
				copyVoAttrStr2Tm(methodBody, StringHelper.toLowerCase(insertVoSimpleName, 0));

				// 将父VO的ID赋值给子VO
				if (parentInsertSpec != null) {
					updateParentId(methodBody, workspaceSpec, parentInsertSpec, parentRelationship, currentOneToMany, insertVoSimpleName);
				}

				// 如果设置了属性初始值，则需要赋值
				if (StringUtils.isNotBlank(currentInsertSpec.getInsertExpr()) == true) {
					updateInitValue(methodBody, currentIndents + StringHelper.toLowerCase(insertVoSimpleName, 0), currentInsertSpec.getInsertExpr());
				}

				// 设置crtTm、crtBy
				buildCreationSignature(methodBody, paramTableDtoXSimpleName, StringHelper.toLowerCase(insertVoSimpleName, 0));

				// insertSpec的tableName决定了DAOX的名字
				String daoXIntfFullName = JavaFormatter.getDaoXFullName(workspaceSpec, insertTableName);
				String daoXIntfSimpleName = JavaFormatter.getClassSimpleName(daoXIntfFullName);

				// 把DAOX作为oprImpl的属性
				prepareDaoXIntf(oprImpl, daoXIntfFullName, daoXIntfSimpleName);

				// 调用DAOX的insert方法
				String insertId = MapperFormatter.getInsertId(currentInsertSpec, false, false);
				if (parentInsertSpec == null) {
					methodBody.append(
							currentIndents + StringHelper.toLowerCase(daoXIntfSimpleName, 0) + JavaSrcElm.DOT + insertId + JavaSrcElm.LEFT_PARENTHESIS
									+ StringHelper.toLowerCase(insertVoSimpleName, 0) + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
				} else {
					methodBody.append(
							currentIndents + StringHelper.toLowerCase(daoXIntfSimpleName, 0) + JavaSrcElm.DOT + insertId + JavaSrcElm.LEFT_PARENTHESIS
									+ StringHelper.toLowerCase(insertVoSimpleName, 0) + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
				}
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
				decreaseIndents();
				methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			} else {
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
			}

			///////////////// 循环体内递归调用开始
			// 如果存在oneToOne，则继续调用
			List<RelationshipSpec> childOneToOneList = currentInsertSpec.getOneToOne();
			List<InsertSpec> childInsertSpecList = null;
			for (RelationshipSpec childOneToOne : childOneToOneList) {
				childInsertSpecList = childOneToOne.getInsertSpec();
				for (InsertSpec childInsertSpec : childInsertSpecList) {
					buildOneToOneInsert(oprMethod, methodBody, artifactSpec, currentInsertSpec.getTableName(), null, currentInsertSpec, childOneToOne,
							childInsertSpec, oprImpl);
				}
			}

			// 如果存在oneToMany，则继续调用
			List<RelationshipSpec> childOneToManyList = currentInsertSpec.getOneToMany();
			for (RelationshipSpec childOneToMany : childOneToManyList) {
				childInsertSpecList = childOneToMany.getInsertSpec();
				// 注意在oneToMany循环体内应该获得的上级insertSpec是childOneToMany的insertSpec
				for (InsertSpec childInsertSpec : childInsertSpecList) {
					buildOneToManyInsert(oprMethod, methodBody, artifactSpec, currentInsertSpec.getTableName(), null, currentInsertSpec,
							childOneToMany, childInsertSpec, oprImpl);
				}
			}

			// 如果存在manyToMany，则继续调用
			List<RelationshipSpec> childManyToManyList = currentInsertSpec.getManyToMany();
			for (RelationshipSpec childManyToMany : childManyToManyList) {
				childInsertSpecList = childManyToMany.getInsertSpec();
				// 在manyToMany元素下，子insertSpec元素只能出现两次
				if (childInsertSpecList == null) {
					throw new IllegalArgumentException("在manyToMany元素下必须设置insertSpec。");
				} else if (childInsertSpecList.size() != 2) {
					throw new IllegalArgumentException("在manyToMany元素下必须设置2个并列的insertSpec。");
				}
				buildManyToManyInsert(oprMethod, methodBody, artifactSpec, currentInsertSpec.getTableName(), null, currentInsertSpec, childManyToMany,
						childInsertSpecList, oprImpl);
			}
			///////////////// 循环体内递归调用结束

			decreaseIndents();
			methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET);
			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
			methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			//			// 注册一个DAO
			//			List<String> paramTypeNameList = new ArrayList<String>();
			//			paramTypeNameList.add(insertVoFullName);
			//			CtxCacheFacade.addDaoMethod(artifactSpec, insertTableName, insertId, paramTypeNameList, JavaSrcElm.INT,
			//					comments + JavaSrcElm.IMPACT_COL_ALL);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	/**
	 * Description: 在manyToMany标签下，第二个insertSpec表示针对实体表的insert，第一个insertSpec表示针对关系表的insert。
	 * Author: zhaoruibin
	 * Creation time: 2015年10月26日 下午10:30:19
	 */
	private void buildManyToManyInsert(MethodSrc oprMethod, StringBuffer methodBody, ArtifactSpec artifactSpec, String paramTableName,
			RelationshipSpec parentRelationship, InsertSpec leftInsertSpec, RelationshipSpec currentManyToMany,
			List<InsertSpec> relAndRightInsertSpecList, ClassSrc oprImpl) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			///////////////////////////////////////////////////////////////////////////////////////////
			// 第二个insertSpec表示针对右实体表的insert
			InsertSpec rightInsertSpec = relAndRightInsertSpecList.get(1); // 对右实体表的insertSpec

			// 获取insertSpec的comments，作为对代码段的注释
			buildSectionComments(methodBody, rightInsertSpec);

			// insertSpec的tableName决定了VO的名字 - 右边表
			String rightTableName = rightInsertSpec.getTableName();
			String rightVoFullName = JavaFormatter.getVoFullName(workspaceSpec, rightTableName);
			String rightVoSimpleName = JavaFormatter.getClassSimpleName(rightVoFullName);
			String refToRight = currentManyToMany != null ? currentManyToMany.getRefToRight() : null;
			String rightVoParamName = StringUtils.isBlank(refToRight) == true ? StringHelper.toLowerCase(rightVoSimpleName, 0) : refToRight;
			// 把VO加入到当前oprImpl的import中  - 右边表
			oprImpl.addImport(rightVoFullName);
			// 把VO加入到paramTableName所指向的DTO - 右边表
			CtxCacheFacade.addDtoAttr(artifactSpec, paramTableName, rightTableName, rightVoParamName, JavaSrcElm.VERB_ADD, false);

			// paramTableName决定了DTOX，从DTOX中获取VO - 右边表
			String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, paramTableName);
			String dtoXSimpleName = JavaFormatter.getClassSimpleName(dtoXFullName);
			methodBody.append(currentIndents + rightVoSimpleName + JavaSrcElm.WHITE_SPACE + rightVoParamName + JavaSrcElm.EQUAL);
			// 命名用以获取VO的get方法名，如果manyToMany所提供的refToRight为空，则采用insertSpec的tableName计算得出的名称
			methodBody.append(StringHelper.toLowerCase(dtoXSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.GET
					+ StringHelper.toUpperCase(rightVoParamName, 0) + JavaSrcElm.CLOSE_PARENTHESIS + JavaSrcElm.SEMICOLON);
			methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			// 如果currentInsertSpec不应跳过insert，则将VO插入到DB
			if (shouldSkipVerb(rightInsertSpec, MapperElm.SQL_INSERT) == false) {

				// 对非空的判断
				methodBody.append(currentIndents + JavaSrcElm.IF + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_PARENTHESIS
						+ StringHelper.toLowerCase(rightVoParamName, 0) + JavaSrcElm.EXPR_OBJ_NOT_NULL + JavaSrcElm.RIGHT_PARENTHESIS
						+ JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

				// 根据对应String更新VO中Date和Timestamp类型的值
				increaseIndents();
				copyVoAttrStr2Tm(methodBody, StringHelper.toLowerCase(rightVoParamName, 0));

				// 如果设置了属性初始值，则需要赋值 - 右边表
				if (StringUtils.isNotBlank(rightInsertSpec.getInsertExpr()) == true) {
					updateInitValue(methodBody, currentIndents + StringHelper.toLowerCase(rightVoParamName, 0), rightInsertSpec.getInsertExpr());
				}

				// 设置crtTm、crtBy
				buildCreationSignature(methodBody, dtoXSimpleName, rightVoParamName);

				// insertSpec的tableName决定了DAOX的名字 - 右边表
				String rightDaoXIntfFullName = JavaFormatter.getDaoXFullName(workspaceSpec, rightTableName);
				String rightDaoXIntfSimpleName = JavaFormatter.getClassSimpleName(rightDaoXIntfFullName);

				// 把DAOX作为oprImpl的属性 - 右边表
				prepareDaoXIntf(oprImpl, rightDaoXIntfFullName, rightDaoXIntfSimpleName);

				// 调用DAOX的insert方法，插入右边表记录
				String rightInsertId = MapperFormatter.getInsertId(rightInsertSpec, false, false);
				methodBody.append(currentIndents + StringHelper.toLowerCase(rightDaoXIntfSimpleName, 0) + JavaSrcElm.DOT + rightInsertId
						+ JavaSrcElm.LEFT_PARENTHESIS + rightVoParamName + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
				decreaseIndents();
				methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			} else {
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
			}

			///////////////////////////////////////////////////////////////////////////////////////////
			// 第一个insertSpec表示针对关系表的insert
			InsertSpec relInsertSpec = relAndRightInsertSpecList.get(0); // 对关系表的insertSpec

			// 获取insertSpec的comments，作为对代码段的注释
			buildSectionComments(methodBody, relInsertSpec);

			// insertSpec的tableName决定了VO的名字 - 关系表
			String relTableName = relInsertSpec.getTableName();
			String relVoFullName = JavaFormatter.getVoFullName(workspaceSpec, relTableName);
			String relVoSimpleName = JavaFormatter.getClassSimpleName(relVoFullName);
			String refToRel = currentManyToMany != null ? currentManyToMany.getRefToRel() : null;
			String relVoParamName = StringUtils.isBlank(refToRel) == true ? StringHelper.toLowerCase(relVoSimpleName, 0) : refToRel;
			// 把VO加入到当前oprImpl的import中 - 关系表
			oprImpl.addImport(relVoFullName);
			// 把VO加入到paramTableName所指向的DTO - 关系表
			CtxCacheFacade.addDtoAttr(artifactSpec, paramTableName, relTableName, relVoParamName, JavaSrcElm.VERB_ADD, false);

			// paramTableName决定了DTOX，从DTOX中获取VO - 关系表
			methodBody.append(currentIndents + relVoSimpleName + JavaSrcElm.WHITE_SPACE + relVoParamName + JavaSrcElm.EQUAL);
			// 命名用以获取VO的get方法名，如果manyToMany所提供的refToRel为空，则采用insertSpec的tableName计算得出的名称
			methodBody.append(StringHelper.toLowerCase(dtoXSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.GET
					+ StringHelper.toUpperCase(relVoParamName, 0) + JavaSrcElm.CLOSE_PARENTHESIS + JavaSrcElm.SEMICOLON);
			methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			// 如果不应跳过insert谓词，则调用DAO
			if (shouldSkipVerb(relInsertSpec, MapperElm.SQL_INSERT) == false) {

				// 将左VO的ID赋值给关系VO
				String leftInsertTableName = leftInsertSpec.getTableName();
				String leftInsertVoFullName = JavaFormatter.getVoFullName(workspaceSpec, leftInsertTableName);
				String leftInsertVoSimpleName = null;
				if (parentRelationship != null) {
					// 如果父关系是manyToMany
					if (StringUtils.isNotBlank(parentRelationship.getRefToRight()) == true) {
						leftInsertVoSimpleName = parentRelationship.getRefToRight();
					}
					// 如果父关系是oneToOne
					else if (StringUtils.isNotBlank(parentRelationship.getRefToSon()) == true) {
						leftInsertVoSimpleName = parentRelationship.getRefToSon();
					} else {
						leftInsertVoSimpleName = JavaFormatter.getClassSimpleName(leftInsertVoFullName);
					}
				} else {
					leftInsertVoSimpleName = JavaFormatter.getClassSimpleName(leftInsertVoFullName);
				}
				String fatherAttr = currentManyToMany.getFatherAttr();
				String leftAttr = currentManyToMany.getLeftAttr();
				String rightAttr = currentManyToMany.getRightAttr();
				String sonAttr = currentManyToMany.getSonAttr();

				// 对非空的判断
				methodBody.append(currentIndents + JavaSrcElm.IF + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_PARENTHESIS
						+ StringHelper.toLowerCase(relVoParamName, 0) + JavaSrcElm.EXPR_OBJ_NOT_NULL + JavaSrcElm.RIGHT_PARENTHESIS
						+ JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

				// 根据对应String更新VO中Date和Timestamp类型的值
				increaseIndents();
				copyVoAttrStr2Tm(methodBody, relVoParamName);

				// 将左VO的ID赋值给关系VO
				methodBody.append(currentIndents + relVoParamName + JavaSrcElm.DOT + JavaSrcElm.SET
						+ StringHelper.toUpperCase(StringUtils.isBlank(leftAttr) == true ? "?" : leftAttr, 0) + JavaSrcElm.LEFT_PARENTHESIS
						+ StringHelper.toLowerCase(leftInsertVoSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.GET
						+ StringHelper.toUpperCase(StringUtils.isBlank(fatherAttr) == true ? "?" : fatherAttr, 0) + JavaSrcElm.CLOSE_PARENTHESIS
						+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

				// 将右VO的ID赋值给关系VO
				methodBody.append(currentIndents + relVoParamName + JavaSrcElm.DOT + JavaSrcElm.SET
						+ StringHelper.toUpperCase(StringUtils.isBlank(rightAttr) == true ? "?" : rightAttr, 0) + JavaSrcElm.LEFT_PARENTHESIS
						+ StringHelper.toLowerCase(rightVoParamName, 0) + JavaSrcElm.DOT + JavaSrcElm.GET
						+ StringHelper.toUpperCase(StringUtils.isBlank(sonAttr) == true ? "?" : sonAttr, 0) + JavaSrcElm.CLOSE_PARENTHESIS
						+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

				// 如果设置了属性初始值，则需要赋值 - 关系表
				if (StringUtils.isNotBlank(relInsertSpec.getInsertExpr()) == true) {
					updateInitValue(methodBody, currentIndents + relVoParamName, relInsertSpec.getInsertExpr());
				}

				// 设置crtTm、crtBy
				buildCreationSignature(methodBody, dtoXSimpleName, relVoParamName);

				// insertSpec的tableName决定了DAOX的名字 - 关系表
				String relDaoXIntfFullName = JavaFormatter.getDaoXFullName(workspaceSpec, relTableName);
				String relDaoXIntfSimpleName = JavaFormatter.getClassSimpleName(relDaoXIntfFullName);

				// 把DAOX作为oprImpl的属性 - 关系表
				prepareDaoXIntf(oprImpl, relDaoXIntfFullName, relDaoXIntfSimpleName);

				// 调用DAOX的insert方法 - 关系表
				String relInsertId = MapperFormatter.getInsertId(relInsertSpec, false, false);
				methodBody.append(currentIndents + StringHelper.toLowerCase(relDaoXIntfSimpleName, 0) + JavaSrcElm.DOT + relInsertId
						+ JavaSrcElm.LEFT_PARENTHESIS + relVoParamName + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
				decreaseIndents();
				methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			} else {
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
			}

			// 如果存在oneToOne，则继续调用
			List<RelationshipSpec> childOneToOneList = rightInsertSpec.getOneToOne();
			List<InsertSpec> descendantInsertSpecList = null;
			for (RelationshipSpec childOneToOne : childOneToOneList) {
				descendantInsertSpecList = childOneToOne.getInsertSpec();
				for (InsertSpec descInsertSpec : descendantInsertSpecList) {
					buildOneToOneInsert(oprMethod, methodBody, artifactSpec, paramTableName, currentManyToMany, rightInsertSpec, childOneToOne,
							descInsertSpec, oprImpl);
				}
			}

			// 如果存在oneToMany，则继续调用
			List<RelationshipSpec> childOneToManyList = rightInsertSpec.getOneToMany();
			for (RelationshipSpec childOneToMany : childOneToManyList) {
				descendantInsertSpecList = childOneToMany.getInsertSpec();
				for (InsertSpec descInsertSpec : descendantInsertSpecList) {
					buildOneToManyInsert(oprMethod, methodBody, artifactSpec, paramTableName, currentManyToMany, rightInsertSpec, childOneToMany,
							descInsertSpec, oprImpl);
				}
			}

			// 如果存在manyToMany，则继续调用
			List<RelationshipSpec> childManyToManyList = rightInsertSpec.getManyToMany();
			for (RelationshipSpec childManyToMany : childManyToManyList) {
				descendantInsertSpecList = childManyToMany.getInsertSpec();
				// 在manyToMany元素下，子insertSpec元素只能出现两次
				if (descendantInsertSpecList == null) {
					throw new IllegalArgumentException("在manyToMany元素下必须设置insertSpec。");
				} else if (descendantInsertSpecList.size() != 2) {
					throw new IllegalArgumentException("在manyToMany元素下必须设置2个并列的insertSpec。");
				}
				buildManyToManyInsert(oprMethod, methodBody, artifactSpec, paramTableName, currentManyToMany, rightInsertSpec, childManyToMany,
						descendantInsertSpecList, oprImpl);
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	/**
	 * Description: 根据"Add"方法定义的对象间关系，构件"change"方法。针对非空VO，调用对应DAO执行update，期间需要跳过“manyToMany"的关系表。
	 * Author: zhaoruibin
	 * Creation time: 2015年11月12日 下午7:50:58
	 */
	private void buildChangeByPK(ArtifactSpec artifactSpec, OprSpec oprSpec) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			List<OprMethodSpec> addSpecList = oprSpec.getAddSpec();
			String changeMethodName = null;
			MethodSrc oprChangeMethod = null;
			StringBuffer methodBody = new StringBuffer();

			// 构建OPR方法
			initCurrentIndents();
			for (OprMethodSpec addSpec : addSpecList) {

				// 注册一个OPR的change方法
				changeMethodName = JavaFormatter.getMethodName(workspaceSpec, addSpec, JavaSrcElm.VERB_CHANGE) + MapperElm.MAPPER_BYPK;
				oprChangeMethod = registerOprMethod(artifactSpec, oprSpec, addSpec, changeMethodName, JavaSrcElm.VERB_CHANGE);

				// 开始方法体：初始化返回值、声明异常开始
				beginMethodBody(methodBody, oprChangeMethod);

				// 参考insertSpec，构建oprImpl的update方法
				for (InsertSpec rootInsertSpec : addSpec.getInsertSpec()) {

					// 构建针对oneToOne的update方法
					String oprImplFullName = JavaFormatter.getOprImplFullName(workspaceSpec, oprSpec.getShortName());
					ClassSrc oprImpl = CtxCacheFacade.lookupOprImpl(oprImplFullName);
					buildOneToOneUpdate(oprChangeMethod, methodBody, artifactSpec, addSpec.getTableName(), null, null, null, rootInsertSpec, oprImpl);

				}

				// 结束方法体
				endMethodBody(methodBody, oprChangeMethod, JavaSrcElm.MSG_CODE_CHANGE_ERR);

				// 填充方法体到方法对象
				oprChangeMethod.setContentBody(methodBody.toString());
				methodBody.delete(0, methodBody.length());

			}

			// 一个完整的方法构建完毕，清除缩进设定
			cleanIndents();

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	/**
	 * Description: 在Ant配置文件中，insertSpec的上一层有可能是changeSpec，也有可能是oneToOneSpec
	 * Author: zhaoruibin
	 * Creation time: 2015年11月13日 下午4:31:05
	 */
	private void buildOneToOneUpdate(MethodSrc oprMethod, StringBuffer methodBody, ArtifactSpec artifactSpec, String paramTableName,
			RelationshipSpec parentRelationship, InsertSpec parentInsertSpec, RelationshipSpec currentOneToOne, InsertSpec currentInsertSpec,
			ClassSrc oprImpl) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// 获取insertSpec的comments，作为对代码段的注释
			buildSectionComments(methodBody, currentInsertSpec);

			// insertSpec的tableName决定了VO的名字
			String insertTableName = currentInsertSpec.getTableName();
			String insertVoFullName = JavaFormatter.getVoFullName(workspaceSpec, insertTableName);
			String insertVoSimpleName = JavaFormatter.getClassSimpleName(insertVoFullName);
			String refToSon = currentOneToOne != null ? currentOneToOne.getRefToSon() : null;
			String insertVoParamName = StringHelper.toUpperCase(StringUtils.isBlank(refToSon) == true ? insertVoSimpleName : refToSon, 0);
			String insertVoParamNameOld = JavaSrcElm.VO_OLD_VALUE_PREFIX + insertVoParamName;
			String insertVoParamNameNew = JavaSrcElm.VO_NEW_VALUE_PREFIX + insertVoParamName;
			// 把VO加入到当前oprImpl的import中
			oprImpl.addImport(insertVoFullName);
			// 把VO加入到paramTableName所指向的DTO
			CtxCacheFacade.addDtoAttr(artifactSpec, paramTableName, insertTableName, insertVoParamNameNew, JavaSrcElm.VERB_CHANGE, false);

			// paramTableName决定了DTOX，从DTOX中获取VO
			String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, paramTableName);
			String dtoXSimpleName = JavaFormatter.getClassSimpleName(dtoXFullName);
			methodBody.append(currentIndents + insertVoSimpleName + JavaSrcElm.WHITE_SPACE + insertVoParamNameOld + JavaSrcElm.EQUAL
					+ StringHelper.toLowerCase(dtoXSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.GET + StringHelper.toUpperCase(insertVoParamName, 0)
					+ JavaSrcElm.CLOSE_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR); // 获取旧值
			methodBody.append(currentIndents + insertVoSimpleName + JavaSrcElm.WHITE_SPACE + insertVoParamNameNew + JavaSrcElm.EQUAL
					+ StringHelper.toLowerCase(dtoXSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.GET
					+ StringHelper.toUpperCase(insertVoParamNameNew, 0) + JavaSrcElm.CLOSE_PARENTHESIS + JavaSrcElm.SEMICOLON
					+ JavaSrcElm.LINE_SEPARATOR); // 获取新值

			// 如果不应跳过update谓词，则调用DAO
			if (shouldSkipVerb(currentInsertSpec, MapperElm.SQL_UPDATE) == false) {

				// 如果额外提供了新值
				methodBody.append(currentIndents + JavaSrcElm.IF + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_PARENTHESIS + insertVoParamNameNew
						+ JavaSrcElm.EXPR_OBJ_NOT_NULL + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LOGIC_AND + JavaSrcElm.WHITE_SPACE + insertVoParamNameOld
						+ JavaSrcElm.EXPR_OBJ_NOT_NULL + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET
						+ JavaSrcElm.LINE_SEPARATOR);

				// 如果旧值的主键为空，在抛出异常
				increaseIndents();
				judgeNullPKCondition(methodBody, insertVoFullName, insertVoParamNameOld);

				// 根据对应String更新VO中Date和Timestamp类型的值
				copyVoAttrStr2Tm(methodBody, insertVoParamNameOld);
				copyVoAttrStr2Tm(methodBody, insertVoParamNameNew);

				// 创建旧值的备份
				//				String insertOldVoId = MapperFormatter.getInsertId(currentInsertSpec, false, false);
				//				methodBody.append(currentIndents + insertVoParamNameOld + JavaSrcElm.DOT + JavaSrcElm.SET + "EditFlag" + JavaSrcElm.LEFT_PARENTHESIS
				//						+ "CmCatSysCode.RevisionDeleted.cd()" + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
				//				buildUpdateSignature(methodBody, dtoXSimpleName, insertVoParamNameOld);
				//				methodBody.append(currentIndents + StringHelper.toLowerCase(daoXIntfSimpleName, 0) + JavaSrcElm.DOT + insertOldVoId
				//						+ JavaSrcElm.LEFT_PARENTHESIS + insertVoParamNameOld + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON
				//						+ JavaSrcElm.LINE_SEPARATOR);

				// 如果设置了属性初始值，则需要赋值
				if (StringUtils.isNotBlank(currentInsertSpec.getUpdateExpr()) == true) {
					updateInitValue(methodBody, currentIndents + insertVoParamNameNew, currentInsertSpec.getUpdateExpr());
				}

				// 更新新值的updTm、updBy
				buildUpdateSignature(methodBody, dtoXSimpleName, insertVoParamNameNew, "CmCatSysCode.RevisionUpdated.cd()");

				// 构建包含新值和旧值的Map对象
				methodBody.append(currentIndents + JavaSrcElm.UTIL_HASHMAP_SIMPLE + JavaSrcElm.LESS_THAN + JavaSrcElm.STRING + JavaSrcElm.COMMA
						+ JavaSrcElm.WHITE_SPACE + insertVoSimpleName + JavaSrcElm.LARGER_THAN + JavaSrcElm.WHITE_SPACE + JavaSrcElm.EXPR_VO_MAP
						+ JavaSrcElm.EQUAL + JavaSrcElm.NEW + JavaSrcElm.WHITE_SPACE + JavaSrcElm.UTIL_HASHMAP_SIMPLE + JavaSrcElm.LESS_THAN
						+ JavaSrcElm.STRING + JavaSrcElm.COMMA + JavaSrcElm.WHITE_SPACE + insertVoSimpleName + JavaSrcElm.LARGER_THAN
						+ JavaSrcElm.CLOSE_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
				methodBody.append(currentIndents + JavaSrcElm.EXPR_VO_MAP + JavaSrcElm.DOT + JavaSrcElm.PUT + JavaSrcElm.LEFT_PARENTHESIS
						+ JavaSrcElm.EXPR_OLD_VALUE_KEY + JavaSrcElm.COMMA + JavaSrcElm.WHITE_SPACE + insertVoParamNameOld
						+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
				methodBody.append(currentIndents + JavaSrcElm.EXPR_VO_MAP + JavaSrcElm.DOT + JavaSrcElm.PUT + JavaSrcElm.LEFT_PARENTHESIS
						+ JavaSrcElm.EXPR_NEW_VALUE_KEY + JavaSrcElm.COMMA + JavaSrcElm.WHITE_SPACE + insertVoParamNameNew
						+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
				oprImpl.addImport(JavaSrcElm.UTIL_HASHMAP_FULL);

				// 把DAOX作为oprImpl的属性，insertSpec的tableName决定了DAOX的名字
				String daoXIntfFullName = JavaFormatter.getDaoXFullName(workspaceSpec, insertTableName);
				String daoXIntfSimpleName = JavaFormatter.getClassSimpleName(daoXIntfFullName);
				AttributeSrc daoXIntf = new AttributeSrc();
				daoXIntf.addAnnot(JavaSrcElm.ANNOT_AUTOWIRED);
				daoXIntf.setName(StringHelper.toLowerCase(daoXIntfSimpleName, 0));
				daoXIntf.setModifier(JavaSrcElm.PROTECTED);
				daoXIntf.setDefaultValue(JavaSrcElm.NULL);
				daoXIntf.setDataType(daoXIntfFullName);
				oprImpl.addAttr(daoXIntf, false);

				// 把DAOX加入到当前oprImpl的import中
				oprImpl.addImport(daoXIntfFullName);

				///////////////////////////////////////////////////////////////
				// 针对旧值和新值同时出现的情况，调用DAOX
				bh.toolkit.srcgen.model.artifact.ObjectFactory artifactObjFactory = new bh.toolkit.srcgen.model.artifact.ObjectFactory();
				UpdateSpec seudoUpdateByPKSpec = artifactObjFactory.createUpdateSpec();
				seudoUpdateByPKSpec.setTableName(currentInsertSpec.getTableName());
				seudoUpdateByPKSpec.setDesc(currentInsertSpec.getDesc());
				String updateWithTmLck = MapperFormatter.getUpdateIdByPK(seudoUpdateByPKSpec, false, true);

				// 如果是本次调用是顶级的oneToOne调用DAOX，则记录返回值
				if (parentInsertSpec == null) {
					methodBody.append(currentIndents + oprMethod.getReturnName() + JavaSrcElm.EQUAL + StringHelper.toLowerCase(daoXIntfSimpleName, 0)
							+ JavaSrcElm.DOT + updateWithTmLck + JavaSrcElm.LEFT_PARENTHESIS + JavaSrcElm.EXPR_VO_MAP + JavaSrcElm.RIGHT_PARENTHESIS
							+ JavaSrcElm.SEMICOLON);
				} else {
					methodBody.append(currentIndents + StringHelper.toLowerCase(daoXIntfSimpleName, 0) + JavaSrcElm.DOT + updateWithTmLck
							+ JavaSrcElm.LEFT_PARENTHESIS + JavaSrcElm.EXPR_VO_MAP + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
				}
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

				///////////////////////////////////////////////////////////////
				// 针对只有旧值的情况，调用DAOX
				String updateWithoutTmLck = MapperFormatter.getUpdateIdByPK(seudoUpdateByPKSpec, false, false);
				decreaseIndents();
				methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET + JavaSrcElm.WHITE_SPACE + JavaSrcElm.ELSE + JavaSrcElm.WHITE_SPACE
						+ JavaSrcElm.IF + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_PARENTHESIS + insertVoParamNameOld + JavaSrcElm.EXPR_OBJ_NOT_NULL
						+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET + JavaSrcElm.LINE_SEPARATOR);

				// 如果旧值的主键为空，在抛出异常
				increaseIndents();
				judgeNullPKCondition(methodBody, insertVoFullName, insertVoParamNameOld);

				// 根据对应String更新VO中Date和Timestamp类型的值
				copyVoAttrStr2Tm(methodBody, insertVoParamNameOld);

				// 如果是本次调用是顶级的oneToOne调用DAOX，则记录返回值
				buildUpdateSignature(methodBody, dtoXSimpleName, insertVoParamNameOld, "CmCatSysCode.RevisionUpdated.cd()"); // 更新旧值的updTm、updBy
				if (parentInsertSpec == null) {
					methodBody.append(currentIndents + oprMethod.getReturnName() + JavaSrcElm.EQUAL + StringHelper.toLowerCase(daoXIntfSimpleName, 0)
							+ JavaSrcElm.DOT + updateWithoutTmLck + JavaSrcElm.LEFT_PARENTHESIS + insertVoParamNameOld + JavaSrcElm.RIGHT_PARENTHESIS
							+ JavaSrcElm.SEMICOLON);
				} else {
					methodBody.append(currentIndents + StringHelper.toLowerCase(daoXIntfSimpleName, 0) + JavaSrcElm.DOT + updateWithoutTmLck
							+ JavaSrcElm.LEFT_PARENTHESIS + insertVoParamNameOld + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
				}
				///////////////////////////////////////////////////////////////

				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
				decreaseIndents();
				methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			} else {
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
			}

			// 如果存在oneToOne，则继续调用
			List<RelationshipSpec> childOneToOneList = currentInsertSpec.getOneToOne();
			List<InsertSpec> childInsertSpecList = null;
			for (RelationshipSpec childOneToOne : childOneToOneList) {
				childInsertSpecList = childOneToOne.getInsertSpec();
				for (InsertSpec childInsertSpec : childInsertSpecList) {
					buildOneToOneUpdate(oprMethod, methodBody, artifactSpec, paramTableName, currentOneToOne, currentInsertSpec, childOneToOne,
							childInsertSpec, oprImpl);
				}
			}

			//			// 如果存在oneToMany，则继续调用
			//			List<RelationshipSpec> childOneToManyList = currentInsertSpec.getOneToMany();
			//			for (RelationshipSpec childOneToMany : childOneToManyList) {
			//				childInsertSpecList = childOneToMany.getInsertSpec();
			//				for (InsertSpec childInsertSpec : childInsertSpecList) {
			//					buildOneToManyInsert(oprMethod, methodBody, artifactSpec, paramTableName, null, currentInsertSpec, childOneToMany,
			//							childInsertSpec, oprImpl);
			//				}
			//			}

			// 如果存在manyToMany，则继续调用
			List<RelationshipSpec> childManyToManyList = currentInsertSpec.getManyToMany();
			for (RelationshipSpec childManyToMany : childManyToManyList) {
				childInsertSpecList = childManyToMany.getInsertSpec();
				// 在manyToMany元素下，子insertSpec元素只能出现两次
				if (childInsertSpecList == null) {
					throw new IllegalArgumentException("在manyToMany元素下必须设置insertSpec。");
				} else if (childInsertSpecList.size() != 2) {
					throw new IllegalArgumentException("在manyToMany元素下必须设置2个并列的insertSpec。");
				}
				buildManyToManyUpdate(oprMethod, methodBody, artifactSpec, paramTableName, null, currentInsertSpec, childManyToMany,
						childInsertSpecList, oprImpl);
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	/**
	 * Description: 在manyToMany标签下，第二个insertSpec表示针对实体表的insert，第一个insertSpec表示针对关系表的insert。
	 * Author: zhaoruibin
	 * Creation time: 2015年10月26日 下午10:30:19
	 */
	private void buildManyToManyUpdate(MethodSrc oprMethod, StringBuffer methodBody, ArtifactSpec artifactSpec, String paramTableName,
			RelationshipSpec parentRelationship, InsertSpec leftInsertSpec, RelationshipSpec currentManyToMany,
			List<InsertSpec> relAndRightInsertSpecList, ClassSrc oprImpl) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			///////////////////////////////////////////////////////////////////////////////////////////
			// 第二个insertSpec表示针对右实体表的insert
			InsertSpec rightInsertSpec = relAndRightInsertSpecList.get(1); // 对右实体表的insertSpec

			// 获取insertSpec的comments，作为对代码段的注释
			buildSectionComments(methodBody, rightInsertSpec);

			// insertSpec的tableName决定了VO的名字 - 右边表
			String rightTableName = rightInsertSpec.getTableName();
			String rightVoFullName = JavaFormatter.getVoFullName(workspaceSpec, rightTableName);
			String rightVoSimpleName = JavaFormatter.getClassSimpleName(rightVoFullName);
			String refToRight = currentManyToMany != null ? currentManyToMany.getRefToRight() : null;
			String rightVoParamName = StringHelper.toUpperCase(StringUtils.isBlank(refToRight) == true ? rightVoSimpleName : refToRight, 0);
			String rightVoParamNameOld = JavaSrcElm.VO_OLD_VALUE_PREFIX + rightVoParamName;
			String rightVoParamNameNew = JavaSrcElm.VO_NEW_VALUE_PREFIX + rightVoParamName;
			// 把VO加入到当前oprImpl的import中  - 右边表
			oprImpl.addImport(rightVoFullName);
			// 把VO加入到paramTableName所指向的DTO - 右边表
			CtxCacheFacade.addDtoAttr(artifactSpec, paramTableName, rightTableName, rightVoParamNameNew, JavaSrcElm.VERB_CHANGE, false);

			// paramTableName决定了DTOX，从DTOX中获取VO - 右边表
			String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, paramTableName);
			String dtoXSimpleName = JavaFormatter.getClassSimpleName(dtoXFullName);
			methodBody.append(currentIndents + rightVoSimpleName + JavaSrcElm.WHITE_SPACE + rightVoParamNameOld + JavaSrcElm.EQUAL
					+ StringHelper.toLowerCase(dtoXSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.GET + StringHelper.toUpperCase(rightVoParamName, 0)
					+ JavaSrcElm.CLOSE_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR); // 获取旧值
			methodBody.append(currentIndents + rightVoSimpleName + JavaSrcElm.WHITE_SPACE + rightVoParamNameNew + JavaSrcElm.EQUAL
					+ StringHelper.toLowerCase(dtoXSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.GET + StringHelper.toUpperCase(rightVoParamNameNew, 0)
					+ JavaSrcElm.CLOSE_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR); // 获取新值

			// 如果不应跳过update谓词，则调用DAO
			if (shouldSkipVerb(rightInsertSpec, MapperElm.SQL_UPDATE) == false) {

				// 非空判断
				methodBody.append(currentIndents + JavaSrcElm.IF + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_PARENTHESIS + rightVoParamNameNew
						+ JavaSrcElm.EXPR_OBJ_NOT_NULL + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LOGIC_AND + JavaSrcElm.WHITE_SPACE + rightVoParamNameOld
						+ JavaSrcElm.EXPR_OBJ_NOT_NULL + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

				// 创建VO拷贝
				//				increaseIndents();
				//				methodBody.append(currentIndents + rightVoSimpleName + JavaSrcElm.WHITE_SPACE + rightVoParamNameOld + JavaSrcElm.EQUAL
				//						+ rightVoParamNameNew + JavaSrcElm.DOT + JavaSrcElm.EXPR_NEW_EMPTY_COPY + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);

				// 如果旧值的主键为空，在抛出异常
				increaseIndents();
				judgeNullPKCondition(methodBody, rightVoFullName, rightVoParamNameOld);

				// 根据对应String更新VO中Date和Timestamp类型的值
				copyVoAttrStr2Tm(methodBody, rightVoParamNameOld);
				copyVoAttrStr2Tm(methodBody, rightVoParamNameNew);

				// 如果设置了属性初始值，则需要赋值 - 右边表
				if (StringUtils.isNotBlank(rightInsertSpec.getUpdateExpr()) == true) {
					updateInitValue(methodBody, currentIndents + StringHelper.toLowerCase(rightVoParamNameNew, 0), rightInsertSpec.getUpdateExpr());
				}

				// 更新updTm、updBy
				buildUpdateSignature(methodBody, dtoXSimpleName, rightVoParamNameNew, "CmCatSysCode.RevisionUpdated.cd()");

				// 构建包含新值和旧值的Map对象
				methodBody.append(currentIndents + JavaSrcElm.UTIL_HASHMAP_SIMPLE + JavaSrcElm.LESS_THAN + JavaSrcElm.STRING + JavaSrcElm.COMMA
						+ JavaSrcElm.WHITE_SPACE + rightVoSimpleName + JavaSrcElm.LARGER_THAN + JavaSrcElm.WHITE_SPACE + JavaSrcElm.EXPR_VO_MAP
						+ JavaSrcElm.EQUAL + JavaSrcElm.NEW + JavaSrcElm.WHITE_SPACE + JavaSrcElm.UTIL_HASHMAP_SIMPLE + JavaSrcElm.LESS_THAN
						+ JavaSrcElm.STRING + JavaSrcElm.COMMA + JavaSrcElm.WHITE_SPACE + rightVoSimpleName + JavaSrcElm.LARGER_THAN
						+ JavaSrcElm.CLOSE_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
				methodBody.append(currentIndents + JavaSrcElm.EXPR_VO_MAP + JavaSrcElm.DOT + JavaSrcElm.PUT + JavaSrcElm.LEFT_PARENTHESIS
						+ JavaSrcElm.EXPR_OLD_VALUE_KEY + JavaSrcElm.COMMA + JavaSrcElm.WHITE_SPACE + rightVoParamNameOld
						+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
				methodBody.append(currentIndents + JavaSrcElm.EXPR_VO_MAP + JavaSrcElm.DOT + JavaSrcElm.PUT + JavaSrcElm.LEFT_PARENTHESIS
						+ JavaSrcElm.EXPR_NEW_VALUE_KEY + JavaSrcElm.COMMA + JavaSrcElm.WHITE_SPACE + rightVoParamNameNew
						+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
				oprImpl.addImport(JavaSrcElm.UTIL_HASHMAP_FULL);

				// insertSpec的tableName决定了DAOX的名字 - 右边表
				String rightDaoXIntfFullName = JavaFormatter.getDaoXFullName(workspaceSpec, rightTableName);
				String rightDaoXIntfSimpleName = JavaFormatter.getClassSimpleName(rightDaoXIntfFullName);

				// 把DAOX作为oprImpl的属性 - 右边表
				AttributeSrc rightDaoXIntf = new AttributeSrc();
				rightDaoXIntf.addAnnot(JavaSrcElm.ANNOT_AUTOWIRED);
				rightDaoXIntf.setName(StringHelper.toLowerCase(rightDaoXIntfSimpleName, 0));
				rightDaoXIntf.setModifier(JavaSrcElm.PROTECTED);
				rightDaoXIntf.setDefaultValue(JavaSrcElm.NULL);
				rightDaoXIntf.setDataType(rightDaoXIntfFullName);
				oprImpl.addAttr(rightDaoXIntf, false);

				// 把DAOX加入到当前oprImpl的import中 - 右边表
				oprImpl.addImport(rightDaoXIntfFullName);

				///////////////////////////////////////////////////////////////
				// 针对旧值和新值同时出现的情况，调用DAOX - 右边表
				bh.toolkit.srcgen.model.artifact.ObjectFactory artifactObjFactory = new bh.toolkit.srcgen.model.artifact.ObjectFactory();
				UpdateSpec seudoUpdateByPKSpec = artifactObjFactory.createUpdateSpec();
				seudoUpdateByPKSpec.setTableName(rightInsertSpec.getTableName());
				seudoUpdateByPKSpec.setDesc(rightInsertSpec.getDesc());
				String updateWithTmLck = MapperFormatter.getUpdateIdByPK(seudoUpdateByPKSpec, false, true);
				methodBody.append(currentIndents + StringHelper.toLowerCase(rightDaoXIntfSimpleName, 0) + JavaSrcElm.DOT + updateWithTmLck
						+ JavaSrcElm.LEFT_PARENTHESIS + JavaSrcElm.EXPR_VO_MAP + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

				///////////////////////////////////////////////////////////////
				// 针对只有旧值的情况，调用DAOX - 右边表
				String updateWithoutTmLck = MapperFormatter.getUpdateIdByPK(seudoUpdateByPKSpec, false, false);
				decreaseIndents();
				methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET + JavaSrcElm.WHITE_SPACE + JavaSrcElm.ELSE + JavaSrcElm.WHITE_SPACE
						+ JavaSrcElm.IF + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_PARENTHESIS + rightVoParamNameOld + JavaSrcElm.EXPR_OBJ_NOT_NULL
						+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET + JavaSrcElm.LINE_SEPARATOR);

				// 如果旧值的主键为空，在抛出异常
				increaseIndents();
				judgeNullPKCondition(methodBody, rightVoFullName, rightVoParamNameOld);

				// 根据对应String更新VO中Date和Timestamp类型的值
				copyVoAttrStr2Tm(methodBody, rightVoParamNameOld);

				methodBody.append(currentIndents + StringHelper.toLowerCase(rightDaoXIntfSimpleName, 0) + JavaSrcElm.DOT + updateWithoutTmLck
						+ JavaSrcElm.LEFT_PARENTHESIS + rightVoParamNameOld + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
				///////////////////////////////////////////////////////////////

				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
				decreaseIndents();
				methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			} else {
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
			}

			// 如果存在oneToOne，则继续调用
			List<RelationshipSpec> childOneToOneList = rightInsertSpec.getOneToOne();
			List<InsertSpec> descendantInsertSpecList = null;
			for (RelationshipSpec childOneToOne : childOneToOneList) {
				descendantInsertSpecList = childOneToOne.getInsertSpec();
				for (InsertSpec descInsertSpec : descendantInsertSpecList) {
					buildOneToOneUpdate(oprMethod, methodBody, artifactSpec, paramTableName, currentManyToMany, rightInsertSpec, childOneToOne,
							descInsertSpec, oprImpl);
				}
			}

			//			// 如果存在oneToMany，则继续调用
			//			List<RelationshipSpec> childOneToManyList = rightInsertSpec.getOneToMany();
			//			for (RelationshipSpec childOneToMany : childOneToManyList) {
			//				descendantInsertSpecList = childOneToMany.getInsertSpec();
			//				for (InsertSpec descInsertSpec : descendantInsertSpecList) {
			//					buildOneToManyInsert(oprMethod, methodBody, artifactSpec, paramTableName, currentManyToMany, rightInsertSpec, childOneToMany,
			//							descInsertSpec, oprImpl);
			//				}
			//			}

			// 如果存在manyToMany，则继续调用
			List<RelationshipSpec> childManyToManyList = rightInsertSpec.getManyToMany();
			for (RelationshipSpec childManyToMany : childManyToManyList) {
				descendantInsertSpecList = childManyToMany.getInsertSpec();
				// 在manyToMany元素下，子insertSpec元素只能出现两次
				if (descendantInsertSpecList == null) {
					throw new IllegalArgumentException("在manyToMany元素下必须设置insertSpec。");
				} else if (descendantInsertSpecList.size() != 2) {
					throw new IllegalArgumentException("在manyToMany元素下必须设置2个并列的insertSpec。");
				}
				buildManyToManyUpdate(oprMethod, methodBody, artifactSpec, paramTableName, currentManyToMany, rightInsertSpec, childManyToMany,
						descendantInsertSpecList, oprImpl);
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void buildRemoveByPK(ArtifactSpec artifactSpec, OprSpec oprSpec) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		// 构建 "remove"方法
		List<OprMethodSpec> removeSpecList = oprSpec.getRemoveSpec();
		String removeMethodName = null;
		//		String removeSpecTableName = null;
		MethodSrc oprRemoveMethod = null;
		StringBuffer methodBody = new StringBuffer();
		for (OprMethodSpec removeSpec : removeSpecList) {

			// 注册OPR的remove方法
			removeMethodName = JavaFormatter.getMethodName(workspaceSpec, removeSpec, JavaSrcElm.VERB_REMOVE) + MapperElm.MAPPER_BYPK;
			oprRemoveMethod = registerOprMethod(artifactSpec, oprSpec, removeSpec, removeMethodName, JavaSrcElm.VERB_REMOVE);

			// 开始方法体：初始化返回值、声明异常开始
			beginMethodBody(methodBody, oprRemoveMethod);

			// 填充方法体
			buildRemove(methodBody, workspaceSpec, removeSpec, oprRemoveMethod, MapperElm.MAPPER_BYPK);

			// 结束方法体
			endMethodBody(methodBody, oprRemoveMethod, JavaSrcElm.MSG_CODE_REMOVE_ERR);

			// 填充方法体到方法对象
			oprRemoveMethod.setContentBody(methodBody.toString());
			methodBody.delete(0, methodBody.length());

		}

		// 一个完整的方法构建完毕，清除缩进设定
		cleanIndents();

	}

	private void buildRemoveBySql(ArtifactSpec artifactSpec, OprSpec oprSpec) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		// 构建 "remove"方法
		List<OprMethodSpec> removeSpecList = oprSpec.getRemoveSpec();
		String removeMethodName = null;
		//		String removeSpecTableName = null;
		MethodSrc oprRemoveMethod = null;
		StringBuffer methodBody = new StringBuffer();
		for (OprMethodSpec removeSpec : removeSpecList) {

			// 注册OPR的remove方法
			removeMethodName = JavaFormatter.getMethodName(workspaceSpec, removeSpec, JavaSrcElm.VERB_REMOVE) + MapperElm.MAPPER_BYSQL;
			oprRemoveMethod = registerOprMethod(artifactSpec, oprSpec, removeSpec, removeMethodName, JavaSrcElm.VERB_REMOVE);

			// 开始方法体：初始化返回值、声明异常开始
			beginMethodBody(methodBody, oprRemoveMethod);

			// 填充方法体
			buildRemove(methodBody, workspaceSpec, removeSpec, oprRemoveMethod, MapperElm.MAPPER_BYSQL);

			// 结束方法体
			endMethodBody(methodBody, oprRemoveMethod, JavaSrcElm.MSG_CODE_REMOVE_ERR);

			// 填充方法体到方法对象
			oprRemoveMethod.setContentBody(methodBody.toString());
			methodBody.delete(0, methodBody.length());

		}

		// 一个完整的方法构建完毕，清除缩进设定
		cleanIndents();

	}

	private void buildRemove(StringBuffer methodBody, WorkspaceSpec workspaceSpec, OprMethodSpec removeSpec, MethodSrc oprRemoveMethod,
			String removeCondition) {

		String removeSpecTableName = removeSpec.getTableName();
		String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, removeSpecTableName);
		String dtoXSimpleName = JavaFormatter.getClassSimpleName(dtoXFullName);
		String dtoXParamName = StringHelper.toLowerCase(dtoXSimpleName, 0);

		// 从DTOX中获取VO
		String updateVoFullName = JavaFormatter.getVoFullName(workspaceSpec, removeSpecTableName);
		String updateVoSimpleName = JavaFormatter.getClassSimpleName(updateVoFullName);
		String updateVoParamName = StringHelper.toLowerCase(updateVoSimpleName, 0);
		methodBody.append(currentIndents + updateVoSimpleName + JavaSrcElm.WHITE_SPACE + updateVoParamName + JavaSrcElm.EQUAL
				+ StringHelper.toLowerCase(dtoXSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.GET + StringHelper.toUpperCase(updateVoSimpleName, 0)
				+ JavaSrcElm.CLOSE_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);

		// 对VO非空的判断
		methodBody.append(currentIndents + JavaSrcElm.IF + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_PARENTHESIS + updateVoParamName
				+ JavaSrcElm.EXPR_OBJ_NOT_NULL + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.WHITE_SPACE + JavaSrcElm.LEFT_BRACKET
				+ JavaSrcElm.LINE_SEPARATOR);

		// 如果SQL条件为空，在抛出异常
		increaseIndents();
		if (MapperElm.MAPPER_BYPK.equalsIgnoreCase(removeCondition) == true) {
			judgeNullPKCondition(methodBody, updateVoFullName, updateVoParamName);
		} else if (MapperElm.MAPPER_BYSQL.equalsIgnoreCase(removeCondition) == true) {
			judgeNullSqlCondition(methodBody, dtoXSimpleName);
		}

		// 更新editFlag, updTm、updBy
		//		methodBody.append(currentIndents + updateVoParamName + JavaSrcElm.DOT + JavaSrcElm.SET + "EditFlag" + JavaSrcElm.LEFT_PARENTHESIS
		//				+ "CmCatSysCode.RevisionDeleted.cd()" + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
		buildUpdateSignature(methodBody, dtoXSimpleName, updateVoParamName, "CmCatSysCode.RevisionDeleted.cd()");

		// removeSpec的tableName决定了DAOX的名字
		String daoXIntfFullName = JavaFormatter.getDaoXFullName(workspaceSpec, removeSpecTableName);
		String daoXIntfSimpleName = JavaFormatter.getClassSimpleName(daoXIntfFullName);

		// 调用DAOX的update方法，记录返回值
		bh.toolkit.srcgen.model.artifact.ObjectFactory artifactObjFactory = new bh.toolkit.srcgen.model.artifact.ObjectFactory();
		UpdateSpec updateSpec = artifactObjFactory.createUpdateSpec();
		updateSpec.setTableName(removeSpecTableName);
		String updateId = null;
		if (StringUtils.isNotBlank(removeCondition) == true && removeCondition.equalsIgnoreCase(MapperElm.MAPPER_BYPK) == true) {
			updateId = MapperFormatter.getUpdateIdByPK(updateSpec, false, false);
			methodBody.append(currentIndents + oprRemoveMethod.getReturnName() + JavaSrcElm.EQUAL);
			methodBody.append(StringHelper.toLowerCase(daoXIntfSimpleName, 0) + JavaSrcElm.DOT + updateId + JavaSrcElm.LEFT_PARENTHESIS
					+ updateVoParamName + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
		} else if (StringUtils.isNotBlank(removeCondition) == true && removeCondition.equalsIgnoreCase(MapperElm.MAPPER_BYSQL) == true) {
			updateId = MapperFormatter.getUpdateIdBySql(updateSpec, false);
			methodBody.append(currentIndents + oprRemoveMethod.getReturnName() + JavaSrcElm.EQUAL);
			methodBody.append(StringHelper.toLowerCase(daoXIntfSimpleName, 0) + JavaSrcElm.DOT + updateId + JavaSrcElm.LEFT_PARENTHESIS
					+ dtoXParamName + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
		} else {
			throw new IllegalArgumentException("Illegal removeCondition: " + removeCondition);
		}
		methodBody.append(JavaSrcElm.LINE_SEPARATOR);
		decreaseIndents();
		methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET);
		methodBody.append(JavaSrcElm.LINE_SEPARATOR);
		methodBody.append(JavaSrcElm.LINE_SEPARATOR);

	}

	private MethodSrc registerOprMethod(ArtifactSpec artifactSpec, OprSpec oprSpec, OprMethodSpec oprMethodSpec, String oprMethodName, String verb)
			throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		MethodSrc oprMethodImpl = null;

		try {

			String tableName = oprMethodSpec.getTableName();
			List<String> oprIntfParamTypeNameList = new ArrayList<String>();
			List<String> oprImplParamTypeNameList = new ArrayList<String>();
			String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
			oprIntfParamTypeNameList.add(dtoXFullName);
			oprImplParamTypeNameList.add(dtoXFullName);
			String methodComments = oprMethodSpec.getDesc();
			CtxCacheFacade.addOprIntfMethod(artifactSpec, oprSpec, oprMethodName, oprIntfParamTypeNameList, JavaSrcElm.INT,
					JavaSrcElm.LEFT_SQUARE_BRACKET + verb + JavaSrcElm.RIGHT_SQUARE_BRACKET + methodComments);
			oprMethodImpl = CtxCacheFacade.addOprImplMethod(artifactSpec, oprSpec, oprMethodName, oprImplParamTypeNameList, JavaSrcElm.INT,
					JavaSrcElm.LEFT_SQUARE_BRACKET + verb + JavaSrcElm.RIGHT_SQUARE_BRACKET + methodComments);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return oprMethodImpl;

	}

	/**
	 * Description: 判断是否需要跳过指定verb
	 * Author: zhaoruibin
	 * Creation time: 2015年11月13日 下午3:40:03
	 */
	private boolean shouldSkipVerb(InsertSpec insertSpec, String verbPattern) {
		String skipVerb = insertSpec.getSkipVerb();
		if (StringUtils.isNotBlank(skipVerb) == true) {
			if (skipVerb.equalsIgnoreCase("all") == true) {
				return true;
			} else {
				Pattern pattern = Pattern.compile("(,?)" + verbPattern + "(,?)");
				Matcher matcher = pattern.matcher(skipVerb);
				if (matcher.find() == true) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	private void updateParentId(StringBuffer methodBody, WorkspaceSpec workspaceSpec, MethodSpec parentMethodSpec,
			RelationshipSpec parentRelationship, RelationshipSpec currentRelationship, String insertVoParamName) {

		String parentInsertTable = parentMethodSpec.getTableName();
		String parentVoFullName = JavaFormatter.getVoFullName(workspaceSpec, parentInsertTable);
		String parentVoSimpleName = null;
		if (parentRelationship != null) {
			// 如果父关系是manyToMany
			if (StringUtils.isNotBlank(parentRelationship.getRefToRight()) == true) {
				parentVoSimpleName = parentRelationship.getRefToRight();
			}
			// 如果父关系是oneToOne
			else if (StringUtils.isNotBlank(parentRelationship.getRefToSon()) == true) {
				parentVoSimpleName = parentRelationship.getRefToSon();
			} else {
				parentVoSimpleName = JavaFormatter.getClassSimpleName(parentVoFullName);
			}
		} else {
			parentVoSimpleName = JavaFormatter.getClassSimpleName(parentVoFullName);
		}
		String fatherAttr = currentRelationship.getFatherAttr();
		String sonAttr = currentRelationship.getSonAttr();

		methodBody.append(currentIndents + StringHelper.toLowerCase(insertVoParamName, 0) + JavaSrcElm.DOT + JavaSrcElm.SET
				+ StringHelper.toUpperCase(StringUtils.isBlank(sonAttr) == true ? "?" : sonAttr, 0) + JavaSrcElm.LEFT_PARENTHESIS
				+ StringHelper.toLowerCase(parentVoSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.GET
				+ StringHelper.toUpperCase(StringUtils.isBlank(fatherAttr) == true ? "?" : fatherAttr, 0) + JavaSrcElm.CLOSE_PARENTHESIS
				+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
		methodBody.append(JavaSrcElm.LINE_SEPARATOR);

	}

	/**
	 * Description: 根据表达式设置属性的初始值
	 * Author: zhaoruibin
	 * Creation time: 2015年11月15日 下午10:36:28
	 */
	private void updateInitValue(StringBuffer methodBody, String linePrefix, String stmtExpr) {

		Pattern pattern = Pattern.compile("(.*)(=)(.*)");
		Matcher matcher = null;

		// 如果语句表达式不为空
		if (StringUtils.isNotBlank(stmtExpr) == true) {

			// 基于分隔符将语句分割为多段
			String[] exprArray = stmtExpr.split(";");
			for (String singleExpr : exprArray) {
				singleExpr = StringUtils.trimToEmpty(singleExpr);
				matcher = pattern.matcher(singleExpr);
				if (matcher.matches() == true) {
					methodBody.append(linePrefix + JavaSrcElm.DOT + JavaSrcElm.SET + StringHelper.toUpperCase(matcher.group(1), 0)
							+ JavaSrcElm.LEFT_PARENTHESIS + matcher.group(3) + JavaSrcElm.RIGHT_PARENTHESIS);
				} else {
					methodBody.append(singleExpr);
				}
				methodBody.append(JavaSrcElm.SEMICOLON);
				methodBody.append(JavaSrcElm.LINE_SEPARATOR);
			}

		}

	}

	/**
	 * Description: 设置crtTm、crtBy
	 * Author: zhaoruibin
	 * Creation time: 2015年11月15日 下午7:35:43
	 */
	private void buildCreationSignature(StringBuffer methodBody, String dtoXSimpleName, String voParamName) {
		methodBody.append(currentIndents + voParamName + JavaSrcElm.DOT + JavaSrcElm.EXPR_SET_CRT_TM + JavaSrcElm.LEFT_PARENTHESIS
				+ StringHelper.toLowerCase(dtoXSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.EXPR_GET_TRX_TIME + JavaSrcElm.RIGHT_PARENTHESIS
				+ JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
		methodBody.append(currentIndents + voParamName + JavaSrcElm.DOT + JavaSrcElm.EXPR_SET_CRT_BY + JavaSrcElm.LEFT_PARENTHESIS
				+ StringHelper.toLowerCase(dtoXSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.EXPR_GET_PTY_ACCT_LOGIN_NM + JavaSrcElm.RIGHT_PARENTHESIS
				+ JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
	}

	/**
	 * Description: 更新updTm、updBy
	 * Author: zhaoruibin
	 * Creation time: 2015年11月15日 下午8:13:18
	 */
	private void buildUpdateSignature(StringBuffer methodBody, String dtoXSimpleName, String voParamName, String editFlag) {
		methodBody.append(currentIndents + voParamName + JavaSrcElm.DOT + JavaSrcElm.SET + "EditFlag" + JavaSrcElm.LEFT_PARENTHESIS + editFlag
				+ JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
		methodBody.append(currentIndents + voParamName + JavaSrcElm.DOT + JavaSrcElm.EXPR_SET_UPD_TM + JavaSrcElm.LEFT_PARENTHESIS
				+ StringHelper.toLowerCase(dtoXSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.EXPR_GET_TRX_TIME + JavaSrcElm.RIGHT_PARENTHESIS
				+ JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
		methodBody.append(currentIndents + voParamName + JavaSrcElm.DOT + JavaSrcElm.EXPR_SET_UPD_BY + JavaSrcElm.LEFT_PARENTHESIS
				+ StringHelper.toLowerCase(dtoXSimpleName, 0) + JavaSrcElm.DOT + JavaSrcElm.EXPR_GET_PTY_ACCT_LOGIN_NM + JavaSrcElm.RIGHT_PARENTHESIS
				+ JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
	}

	private AttributeSrc prepareDaoXIntf(ClassSrc oprImpl, String daoXIntfFullName, String daoXIntfSimpleName) {

		// 把DAOX作为oprImpl的属性
		AttributeSrc daoXIntf = new AttributeSrc();
		daoXIntf.addAnnot(JavaSrcElm.ANNOT_AUTOWIRED);
		daoXIntf.setName(StringHelper.toLowerCase(daoXIntfSimpleName, 0));
		daoXIntf.setModifier(JavaSrcElm.PROTECTED);
		daoXIntf.setDefaultValue(JavaSrcElm.NULL);
		daoXIntf.setDataType(daoXIntfFullName);
		oprImpl.addAttr(daoXIntf, false);

		// 把DAOX加入到当前oprImpl的import中
		oprImpl.addImport(daoXIntfFullName);

		return daoXIntf;

	}

}
