package bh.toolkit.srcgen.builder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.DaoSpec;
import bh.toolkit.srcgen.model.artifact.MgrMethodSpec;
import bh.toolkit.srcgen.model.artifact.MgrSpec;
import bh.toolkit.srcgen.model.artifact.OprSpec;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;
import bh.toolkit.srcgen.model.src.AttributeSrc;
import bh.toolkit.srcgen.model.src.ClassSrc;
import bh.toolkit.srcgen.model.src.MethodSrc;
import bh.toolkit.srcgen.registry.CtxCacheFacade;
import bh.toolkit.srcgen.util.ExceptionUtil;
import bh.toolkit.srcgen.util.JavaFormatter;
import bh.toolkit.srcgen.util.StringHelper;

public class MgrBuilder extends JavaSrcBuilder {

	private static Logger logger = Logger.getLogger(MgrBuilder.class);

	public void buildAllMgr(ArtifactSpec artifactSpec, List<MgrSpec> mgrSpecList)
			throws AppException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, IOException {

		// 构建所有MGR相关资源，包括接口和实现
		logger.debug("BUILD MGR: Begin to build manager ...");
		for (MgrSpec mgrSpec : mgrSpecList) {
			buildSingleMgr(artifactSpec, mgrSpec);
		}

	}

	/**
	 * Description: 构建所有OPR接口的方法，以及对应的实现 Author: zhaoruibin Creation time: 2015年11月16日 下午9:59:24
	 * 
	 * @throws AppException
	 */
	private void buildSingleMgr(ArtifactSpec artifactSpec, MgrSpec mgrSpec) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// 构建mgrImpl的属性
			CtxCacheFacade.addMgrIntf(artifactSpec, mgrSpec.getShortName());
			ClassSrc mgrImpl = CtxCacheFacade.addMgrImpl(artifactSpec, mgrSpec.getShortName());
			CtxCacheFacade.addMgrXIntf(artifactSpec, mgrSpec.getShortName());
			CtxCacheFacade.addMgrXImpl(artifactSpec, mgrSpec.getShortName());
			buildMgrAttr(workspaceSpec, mgrSpec, mgrImpl);

			List<MgrMethodSpec> svcMethodSpecList = mgrSpec.getSvcMethodSpec();
			for (MgrMethodSpec svcMethodSpec : svcMethodSpecList) {
				// 构建mgrIntf、mgrImpl的方法
				buildMgrMethod(artifactSpec, mgrSpec, svcMethodSpec);
			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void buildMgrAttr(WorkspaceSpec workspaceSpec, MgrSpec mgrSpec, ClassSrc mgrImpl) {

		// 把每个mgrXIntf作为mgrImpl的import和属性
		String childMgrXIntfFullName = null;
		String childMgrXIntfSimpleName = null;
		AttributeSrc childMgrXIntf = null;
		List<MgrSpec> childMgrSpecList = mgrSpec.getMgrSpec();
		for (MgrSpec childMgrSpec : childMgrSpecList) {

			childMgrXIntfFullName = JavaFormatter.getMgrXIntfFullName(workspaceSpec, childMgrSpec.getShortName());
			childMgrXIntfSimpleName = JavaFormatter.getClassSimpleName(childMgrXIntfFullName);

			// 把mgrXIntf作为mgrImpl的属性
			childMgrXIntf = new AttributeSrc();
			childMgrXIntf.addObjComments(childMgrSpec.getDesc());
			childMgrXIntf.addAnnot(JavaSrcElm.ANNOT_AUTOWIRED);
			childMgrXIntf.setName(StringHelper.toLowerCase(childMgrXIntfSimpleName, 0));
			childMgrXIntf.setModifier(JavaSrcElm.PROTECTED);
			childMgrXIntf.setDefaultValue(JavaSrcElm.NULL);
			childMgrXIntf.setDataType(childMgrXIntfFullName);
			mgrImpl.addAttr(childMgrXIntf, false);

			// 把mgrXIntf加入到当前mgrXImpl的import中
			mgrImpl.addImport(childMgrXIntfFullName);

		}

		// 把每个oprXIntf作为mgrImpl的import和属性
		String oprXIntfFullName = null;
		String oprXIntfSimpleName = null;
		AttributeSrc oprXIntf = null;
		List<OprSpec> oprSpecList = mgrSpec.getOprSpec();
		for (OprSpec oprSpec : oprSpecList) {

			oprXIntfFullName = JavaFormatter.getOprXIntfFullName(workspaceSpec, oprSpec.getShortName());
			oprXIntfSimpleName = JavaFormatter.getClassSimpleName(oprXIntfFullName);

			// 把oprXIntf作为mgrImpl的属性
			oprXIntf = new AttributeSrc();
			oprXIntf.addObjComments(oprSpec.getDesc());
			oprXIntf.addAnnot(JavaSrcElm.ANNOT_AUTOWIRED);
			oprXIntf.setName(StringHelper.toLowerCase(oprXIntfSimpleName, 0));
			oprXIntf.setModifier(JavaSrcElm.PROTECTED);
			oprXIntf.setDefaultValue(JavaSrcElm.NULL);
			oprXIntf.setDataType(oprXIntfFullName);
			mgrImpl.addAttr(oprXIntf, false);

			// 把oprXIntf加入到当前mgrXImpl的import中
			mgrImpl.addImport(oprXIntfFullName);

		}

		// 把每个daoXIntf作为mgrImpl的import和属性
		String daoXIntfFullName = null;
		String daoXIntfSimpleName = null;
		AttributeSrc daoXIntf = null;
		List<DaoSpec> daoSpecList = mgrSpec.getDaoSpec();
		for (DaoSpec daoSpec : daoSpecList) {

			daoXIntfFullName = JavaFormatter.getDaoXFullName(workspaceSpec, daoSpec.getShortName());
			daoXIntfSimpleName = JavaFormatter.getClassSimpleName(daoXIntfFullName);

			// 把daoXIntf作为mgrImpl的属性
			daoXIntf = new AttributeSrc();
			daoXIntf.addObjComments(daoSpec.getDesc());
			daoXIntf.addAnnot(JavaSrcElm.ANNOT_AUTOWIRED);
			daoXIntf.setName(StringHelper.toLowerCase(daoXIntfSimpleName, 0));
			daoXIntf.setModifier(JavaSrcElm.PROTECTED);
			daoXIntf.setDefaultValue(JavaSrcElm.NULL);
			daoXIntf.setDataType(daoXIntfFullName);
			mgrImpl.addAttr(daoXIntf, false);

			// 把daoXIntf加入到当前mgrXImpl的import中
			mgrImpl.addImport(daoXIntfFullName);

		}

	}

	private void buildMgrMethod(ArtifactSpec artifactSpec, MgrSpec mgrSpec, MgrMethodSpec svcMethodSpec) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// 注册一个MGR的方法
			String svcMethodName = JavaFormatter.getMethodName(workspaceSpec, svcMethodSpec, JavaSrcElm.VERB_EXE);
			registerMgrMethod(artifactSpec, mgrSpec, svcMethodSpec, svcMethodName);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private MethodSrc registerMgrMethod(ArtifactSpec artifactSpec, MgrSpec mgrSpec, MgrMethodSpec svcMethodSpec, String svcMethodName) throws AppException {

		// 读取全局配置
		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		MethodSrc svcMethodImplPublic = null;

		try {

			String tableName = svcMethodSpec.getTableName();
			String dtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
			// String paramDataType = JavaFormatter.getMethodParamDataType(workspaceSpec, svcMethodSpec);
			String paramDataType = svcMethodSpec.getParamDataType();
			if (StringUtils.isNotBlank(paramDataType) == true) {
				if (paramDataType.equalsIgnoreCase(JavaSrcElm.UTIL_LIST_FULL) == true || paramDataType.equalsIgnoreCase(JavaSrcElm.UTIL_LIST_SIMPLE) == true) {
					paramDataType = JavaSrcElm.UTIL_LIST_FULL + JavaSrcElm.LESS_THAN + dtoXFullName + JavaSrcElm.LARGER_THAN;
				} else if (paramDataType.equalsIgnoreCase(JavaSrcElm.LANG_STRING_FULL) == true || paramDataType.equalsIgnoreCase(JavaSrcElm.STRING) == true) {
					paramDataType = JavaSrcElm.STRING;
				}
			} else {
				paramDataType = dtoXFullName; // 默认返回值数据类型
			}

			String returnDataType = svcMethodSpec.getReturnDataType();
			if (StringUtils.isNotBlank(returnDataType) == true) {
				if (returnDataType.equalsIgnoreCase(JavaSrcElm.UTIL_LIST_FULL) == true || returnDataType.equalsIgnoreCase(JavaSrcElm.UTIL_LIST_SIMPLE) == true) {
					returnDataType = JavaSrcElm.UTIL_LIST_FULL + JavaSrcElm.LESS_THAN + dtoXFullName + JavaSrcElm.LARGER_THAN;
				} else if (returnDataType.equalsIgnoreCase(JavaSrcElm.INT) == true) {
					returnDataType = JavaSrcElm.INT;
				} else if (returnDataType.equalsIgnoreCase(JavaSrcElm.LANG_STRING_FULL) == true || returnDataType.equalsIgnoreCase(JavaSrcElm.STRING) == true) {
					returnDataType = JavaSrcElm.STRING;
				} else {
					returnDataType = dtoXFullName; // 默认返回值数据类型
				}
			} else {
				returnDataType = dtoXFullName; // 默认返回值数据类型
			}
			String methodComments = svcMethodSpec.getDesc();

			List<String> svcIntfParamTypeNameList = new ArrayList<String>();
			List<String> svcImplParamTypeNameList = new ArrayList<String>();
			svcIntfParamTypeNameList.add(paramDataType);
			svcImplParamTypeNameList.add(paramDataType);

			///////////////////////////////////////////////////////
			// 注册一个MGR INTF的方法
			MethodSrc svcMethodIntfPublic = CtxCacheFacade.addMgrIntfMethod(artifactSpec, mgrSpec, svcMethodName, svcIntfParamTypeNameList, returnDataType, methodComments);

			///////////////////////////////////////////////////////
			// 注册一个MGR IMPL的方法
			svcMethodImplPublic = CtxCacheFacade.addMgrImplPublicMethod(artifactSpec, mgrSpec, svcMethodName, svcImplParamTypeNameList, returnDataType, methodComments);

			///////////////////////////////////////////////////////
			// 注册一个消息编码属性
			CtxCacheFacade.addSvcMsgCodeAttr(artifactSpec, svcMethodSpec.getExpCode(), methodComments + JavaSrcElm.MSG_CODE_SUFFIX_ERR);

			// 开始方法体：初始化返回值、声明异常开始
			StringBuffer methodBody = new StringBuffer();
			initCurrentIndents();
			beginMethodBody(methodBody, svcMethodImplPublic);

			// 调用逻辑扩展方法
			methodBody.append(currentIndents + svcMethodImplPublic.getReturnName() + JavaSrcElm.EQUAL + JavaSrcElm.UNDER_LINE + svcMethodName + JavaSrcElm.LEFT_PARENTHESIS
					+ svcMethodIntfPublic.getParamNameList().get(0) + JavaSrcElm.RIGHT_PARENTHESIS + JavaSrcElm.SEMICOLON);
			methodBody.append(JavaSrcElm.LINE_SEPARATOR);
			methodBody.append(JavaSrcElm.LINE_SEPARATOR);

			// 结束方法体
			endMethodBody(methodBody, svcMethodImplPublic, svcMethodSpec.getExpCode());

			// 填充方法体到方法对象
			svcMethodImplPublic.setContentBody(methodBody.toString());
			methodBody.delete(0, methodBody.length());

			///////////////////////////////////////////////////////
			// 注册一个空的、用于进行逻辑扩展的方法
			MethodSrc svcMethodImplDummy = CtxCacheFacade.addMgrImplDummyMethod(artifactSpec, mgrSpec, JavaSrcElm.UNDER_LINE + svcMethodName, svcImplParamTypeNameList,
					returnDataType, methodComments);

			// 开始方法体：返回空值
			increaseIndents();
			methodBody.append(currentIndents + JavaSrcElm.RETURN + JavaSrcElm.WHITE_SPACE);
			if (returnDataType.equalsIgnoreCase(JavaSrcElm.INT) == true) {
				methodBody.append(JavaSrcElm.ZERO);
			} else {
				methodBody.append(JavaSrcElm.NULL);
			}
			methodBody.append(JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
			decreaseIndents();
			methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET + JavaSrcElm.LINE_SEPARATOR);

			// 填充方法体到方法对象
			svcMethodImplDummy.setContentBody(methodBody.toString());
			methodBody.delete(0, methodBody.length());

			///////////////////////////////////////////////////////
			// 注册一个MGRX IMPL的方法
			MethodSrc svcMethodImplXDummy = CtxCacheFacade.addMgrXImplDummyMethod(artifactSpec, mgrSpec, JavaSrcElm.UNDER_LINE + svcMethodName, svcImplParamTypeNameList,
					returnDataType, methodComments);

			// 开始方法体：返回空值
			increaseIndents();
			methodBody.append(currentIndents + JavaSrcElm.RETURN + JavaSrcElm.WHITE_SPACE);
			if (returnDataType.equalsIgnoreCase(JavaSrcElm.INT) == true) {
				methodBody.append(JavaSrcElm.ZERO);
			} else {
				methodBody.append(JavaSrcElm.NULL);
			}
			methodBody.append(JavaSrcElm.SEMICOLON + JavaSrcElm.LINE_SEPARATOR);
			decreaseIndents();
			methodBody.append(currentIndents + JavaSrcElm.RIGHT_BRACKET + JavaSrcElm.LINE_SEPARATOR);

			// 填充方法体到方法对象
			svcMethodImplXDummy.setContentBody(methodBody.toString());
			methodBody.delete(0, methodBody.length());

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return svcMethodImplPublic;

	}

}
