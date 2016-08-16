package bh.toolkit.srcgen.task;

import java.io.IOException;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.io.ClassSrcExporter;
import bh.toolkit.srcgen.io.FileHandler;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.CommonAttrSpec;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;
import bh.toolkit.srcgen.model.src.ClassSrc;
import bh.toolkit.srcgen.registry.CtxCacheFacade;
import bh.toolkit.srcgen.util.JavaFormatter;

public class PrintArtifacts extends Task {

	private static Logger logger = Logger.getLogger(PrintArtifacts.class);

	private ArtifactSpec artifactSpec;

	public void addArtifactSpec(ArtifactSpec artifactSpec) {
		this.artifactSpec = artifactSpec;
	}

	public void execute() throws BuildException {

		try {

			restoreProfile(); // 从缓存中恢复配置参数

			writeDao(); // 输出DAO接口
			writeDaoX(); // 输出DAOX接口

			writeVo(); // 输出VO
			writeDto(); // 输出DTO
			writeDtoX(); // 输出DTOX

			writeOprIntf(); // 输出OPR接口
			writeOprImpl(); // 输出OPR接口的实现
			writeOprXIntf(); // 输出OPRX接口
			writeOprXImpl(); // 输出OPRX接口的实现
			writeMgrIntf(); // 输出MGRX接口
			writeMgrImpl(); // 输出MGRX接口的实现
			writeMgrXIntf(); // 输出MGRX接口
			writeMgrXImpl(); // 输出MGRX接口的实现

			writeMsgCodeCls(); // 输出MSGCD类文件
			writeMsgCodeProp(); // 输出MSGCD资源文件

		} catch (Throwable t) {
			logger.error(t.getMessage(), t.getCause());
			throw new BuildException(t);
		}

	}

	private void restoreProfile() {
		// 从缓存中恢复配置信息
		CommonAttrSpec commonAttrSpec = CtxCacheFacade.getCommonAttrSpec();
		// 以个性化配置覆盖缓存配置
		if (artifactSpec != null && artifactSpec.getCommonAttrSpec() != null) {
			commonAttrSpec.getWorkspaceSpec().setMgrPrj(artifactSpec.getCommonAttrSpec().getWorkspaceSpec().getMgrPrj());
		}
		artifactSpec.setCommonAttrSpec(commonAttrSpec);
	}

	private void writeDao() throws IOException {

		// Get the cached DAO interfaces.
		String daoIntfFullName = null;
		String daoFileName = null;
		ClassSrc daoIntf = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("DAO INTF: Begin to generate DAO interface files ...");

		for (Enumeration<String> daoIntfKeys = CtxCacheFacade.getDaoIntfCacheKeys(); daoIntfKeys.hasMoreElements();) {
			daoIntfFullName = daoIntfKeys.nextElement();
			daoIntf = CtxCacheFacade.lookupDaoIntf(daoIntfFullName);
			javaSrc = classSrcExporter.exportClassSrc(artifactSpec, daoIntf, JavaSrcElm.EXP_TYPE_DAO_INTF);
			daoFileName = daoIntf.getFileName();
			FileHandler.writeTextFile(daoFileName, javaSrc, false);
		}

	}

	private void writeDaoX() throws IOException {

		// Get the cached DAOX interfaces.
		String daoXIntfFullName = null;
		String daoXFileName = null;
		ClassSrc daoXIntf = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("DAOX INTF: Begin to generate DAOX interface files ...");

		for (Enumeration<String> daoXIntfKeys = CtxCacheFacade.getDaoXIntfCacheKeys(); daoXIntfKeys.hasMoreElements();) {

			daoXIntfFullName = daoXIntfKeys.nextElement();
			daoXIntf = CtxCacheFacade.lookupDaoXIntf(daoXIntfFullName);

			// 判断DAOX文件是否已经存在，如果不存在，则生成一个新的
			if (daoXIntf.isExistingFile() == true) {
				logger.info("---【 已存在，保留】" + daoXIntf.getFileName());
			} else {
				// Write a new file.
				daoXFileName = daoXIntf.getFileName();
				javaSrc = classSrcExporter.exportClassSrc(artifactSpec, daoXIntf, JavaSrcElm.EXP_TYPE_DAO_INTF);
				FileHandler.writeTextFile(daoXFileName, javaSrc, false);
			}

		}

	}

	private void writeVo() throws IOException {

		// Get the cached VO objects.
		String voClassFullName = null;
		String voClassFileName = null;
		ClassSrc voClass = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("VO: Begin to generate VO class files ...");

		for (Enumeration<String> voClassKeys = CtxCacheFacade.getVoCacheKeys(); voClassKeys.hasMoreElements();) {
			voClassFullName = voClassKeys.nextElement();
			voClass = CtxCacheFacade.lookupVoClass(voClassFullName);
			javaSrc = classSrcExporter.exportClassSrc(artifactSpec, voClass, JavaSrcElm.EXP_TYPE_VO);
			voClassFileName = voClass.getFileName();
			FileHandler.writeTextFile(voClassFileName, javaSrc, false);
		}

	}

	private void writeDto() throws IOException {

		// Get the cached DTO objects.
		String dtoFullName = null;
		String dtoFileName = null;
		ClassSrc dtoClass = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("DTO: Begin to generate DTO class files ...");
		for (Enumeration<String> dtoClassKeys = CtxCacheFacade.getDtoCacheKeys(); dtoClassKeys.hasMoreElements();) {
			dtoFullName = dtoClassKeys.nextElement();
			dtoClass = CtxCacheFacade.lookupDtoClass(dtoFullName);
			javaSrc = classSrcExporter.exportClassSrc(artifactSpec, dtoClass, JavaSrcElm.EXP_TYPE_DTO);
			dtoFileName = dtoClass.getFileName();
			FileHandler.writeTextFile(dtoFileName, javaSrc, false);
		}

	}

	private void writeDtoX() throws IOException {

		// Get the cached DTOX objects.
		String dtoXFullName = null;
		String dtoXFileName = null;
		ClassSrc dtoXClass = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("DTOX: Begin to generate DTOX class files ...");
		for (Enumeration<String> dtoXClassKeys = CtxCacheFacade.getDtoXCacheKeys(); dtoXClassKeys.hasMoreElements();) {

			dtoXFullName = dtoXClassKeys.nextElement();
			dtoXClass = CtxCacheFacade.lookupDtoXClass(dtoXFullName);

			// 判断DTOX文件是否已经存在，如果不存在，则生成一个新的
			if (dtoXClass.isExistingFile() == true) {
				logger.info("---【 已存在，保留】" + dtoXClass.getFileName());
			} else {
				// Write a new file.
				dtoXFileName = dtoXClass.getFileName();
				javaSrc = classSrcExporter.exportClassSrc(artifactSpec, dtoXClass, JavaSrcElm.EXP_TYPE_DTO);
				FileHandler.writeTextFile(dtoXFileName, javaSrc, false);
			}

		}

	}

	private void writeOprIntf() throws IOException {

		// Get the cached OPR interfaces.
		String oprIntfFullName = null;
		String oprIntfFileName = null;
		ClassSrc oprIntf = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("OPR: Begin to generate OPR interface files ...");
		for (Enumeration<String> oprIntfKeys = CtxCacheFacade.getOprIntfCacheKeys(); oprIntfKeys.hasMoreElements();) {
			oprIntfFullName = oprIntfKeys.nextElement();
			oprIntf = CtxCacheFacade.lookupOprIntf(oprIntfFullName);
			javaSrc = classSrcExporter.exportClassSrc(artifactSpec, oprIntf, JavaSrcElm.EXP_TYPE_OPR_INTF);
			oprIntfFileName = oprIntf.getFileName();
			FileHandler.writeTextFile(oprIntfFileName, javaSrc, false);
		}

	}

	private void writeOprImpl() throws IOException {

		// Get the cached OPR implementation.
		String oprImplFullName = null;
		String oprImplFileName = null;
		ClassSrc oprImpl = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("OPR: Begin to generate OPR implementation files ...");
		for (Enumeration<String> oprImplKeys = CtxCacheFacade.getOprImplCacheKeys(); oprImplKeys.hasMoreElements();) {
			oprImplFullName = oprImplKeys.nextElement();
			oprImpl = CtxCacheFacade.lookupOprImpl(oprImplFullName);
			javaSrc = classSrcExporter.exportClassSrc(artifactSpec, oprImpl, JavaSrcElm.EXP_TYPE_OPR_IMPL);
			oprImplFileName = oprImpl.getFileName();
			FileHandler.writeTextFile(oprImplFileName, javaSrc, false);
		}

	}

	private void writeOprXIntf() throws IOException {

		// Get the cached OPRX interfaces.
		String oprXIntfFullName = null;
		String oprXFileName = null;
		ClassSrc oprXIntf = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("OPRX INTF: Begin to generate OPRX interface files ...");

		for (Enumeration<String> oprXIntfKeys = CtxCacheFacade.getOprXIntfCacheKeys(); oprXIntfKeys.hasMoreElements();) {

			oprXIntfFullName = oprXIntfKeys.nextElement();
			oprXIntf = CtxCacheFacade.lookupOprXIntf(oprXIntfFullName);

			// 判断OPRX文件是否已经存在，如果不存在，则生成一个新的
			if (oprXIntf.isExistingFile() == true) {
				logger.info("---【 已存在，保留】" + oprXIntf.getFileName());
			} else {
				// Write a new file.
				oprXFileName = oprXIntf.getFileName();
				javaSrc = classSrcExporter.exportClassSrc(artifactSpec, oprXIntf, JavaSrcElm.EXP_TYPE_OPR_INTF);
				FileHandler.writeTextFile(oprXFileName, javaSrc, false);
			}

		}

	}

	private void writeOprXImpl() throws IOException {

		// Get the cached OPRX implementation.
		String oprXImplFullName = null;
		String oprXImplFileName = null;
		ClassSrc oprXImpl = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("OPRX: Begin to generate OPRX implementation files ...");
		for (Enumeration<String> oprXImplKeys = CtxCacheFacade.getOprXImplCacheKeys(); oprXImplKeys.hasMoreElements();) {

			oprXImplFullName = oprXImplKeys.nextElement();
			oprXImpl = CtxCacheFacade.lookupOprXImpl(oprXImplFullName);

			// 判断OPRX IMPL文件是否已经存在，如果不存在，则生成一个新的
			if (oprXImpl.isExistingFile() == true) {
				logger.info("---【 已存在，保留】" + oprXImpl.getFileName());
			} else {
				// Write a new file.
				oprXImplFileName = oprXImpl.getFileName();
				javaSrc = classSrcExporter.exportClassSrc(artifactSpec, oprXImpl, JavaSrcElm.EXP_TYPE_OPR_IMPL);
				FileHandler.writeTextFile(oprXImplFileName, javaSrc, false);
			}

		}

	}

	private void writeMgrIntf() throws IOException {

		// Get the cached MGR interface.
		String mgrIntfFullName = null;
		String mgrIntfFileName = null;
		ClassSrc mgrIntf = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("MGR: Begin to generate MGR interface files ...");
		for (Enumeration<String> mgrIntfKeys = CtxCacheFacade.getMgrIntfCacheKeys(); mgrIntfKeys.hasMoreElements();) {
			mgrIntfFullName = mgrIntfKeys.nextElement();
			mgrIntf = CtxCacheFacade.lookupMgrIntf(mgrIntfFullName);
			mgrIntfFileName = mgrIntf.getFileName();
			javaSrc = classSrcExporter.exportClassSrc(artifactSpec, mgrIntf, JavaSrcElm.EXP_TYPE_MGR_INTF);
			FileHandler.writeTextFile(mgrIntfFileName, javaSrc, false);
		}

	}

	private void writeMgrImpl() throws IOException {

		// Get the cached MGR implementation.
		String mgrImplFullName = null;
		String mgrImplFileName = null;
		ClassSrc mgrImpl = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("MGR: Begin to generate MGR implementation files ...");
		for (Enumeration<String> mgrImplKeys = CtxCacheFacade.getMgrImplCacheKeys(); mgrImplKeys.hasMoreElements();) {
			mgrImplFullName = mgrImplKeys.nextElement();
			mgrImpl = CtxCacheFacade.lookupMgrImpl(mgrImplFullName);
			mgrImplFileName = mgrImpl.getFileName();
			javaSrc = classSrcExporter.exportClassSrc(artifactSpec, mgrImpl, JavaSrcElm.EXP_TYPE_MGR_IMPL);
			FileHandler.writeTextFile(mgrImplFileName, javaSrc, false);
		}

	}

	private void writeMgrXIntf() throws IOException {

		// Get the cached MGRX interface.
		String mgrXIntfFullName = null;
		String mgrXIntfFileName = null;
		ClassSrc mgrXIntf = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("MGR: Begin to generate MGRX interface files ...");
		for (Enumeration<String> mgrXIntfKeys = CtxCacheFacade.getMgrXIntfCacheKeys(); mgrXIntfKeys.hasMoreElements();) {

			mgrXIntfFullName = mgrXIntfKeys.nextElement();
			mgrXIntf = CtxCacheFacade.lookupMgrXIntf(mgrXIntfFullName);

			// 判断MGRX INTF文件是否已经存在，如果不存在，则生成一个新的
			if (mgrXIntf.isExistingFile() == true) {
				logger.info("---【 已存在，保留】" + mgrXIntf.getFileName());
			} else {
				// Write a new file.
				mgrXIntfFileName = mgrXIntf.getFileName();
				javaSrc = classSrcExporter.exportClassSrc(artifactSpec, mgrXIntf, JavaSrcElm.EXP_TYPE_MGR_INTF);
				FileHandler.writeTextFile(mgrXIntfFileName, javaSrc, false);
			}

		}

	}

	private void writeMgrXImpl() throws IOException {

		// Get the cached MGRX implementation.
		String mgrXImplFullName = null;
		String mgrXImplFileName = null;
		ClassSrc mgrXImpl = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("MGR: Begin to generate MGRX implementation files ...");
		for (Enumeration<String> mgrXImplKeys = CtxCacheFacade.getMgrXImplCacheKeys(); mgrXImplKeys.hasMoreElements();) {

			mgrXImplFullName = mgrXImplKeys.nextElement();
			mgrXImpl = CtxCacheFacade.lookupMgrXImpl(mgrXImplFullName);

			// 判断MGRX IMPL文件是否已经存在，如果不存在，则生成一个新的
			if (mgrXImpl.isExistingFile() == true) {
				logger.info("---【 已存在，保留】" + mgrXImpl.getFileName());
			} else {
				// Write a new file.
				mgrXImplFileName = mgrXImpl.getFileName();
				javaSrc = classSrcExporter.exportClassSrc(artifactSpec, mgrXImpl, JavaSrcElm.EXP_TYPE_MGR_IMPL);
				FileHandler.writeTextFile(mgrXImplFileName, javaSrc, false);
			}

		}

	}

	private void writeMsgCodeCls() throws IOException {

		String msgCodeFullName = null;
		String msgCodeFileName = null;
		ClassSrc msgCodeClass = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("MSG CD CLS: Begin to generate MSG CD class files ...");
		for (Enumeration<String> msgCodeKeys = CtxCacheFacade.getMsgCodeCacheKeys(); msgCodeKeys.hasMoreElements();) {

			msgCodeFullName = msgCodeKeys.nextElement();
			msgCodeClass = CtxCacheFacade.lookupMsgCodeClass(msgCodeFullName);
			msgCodeFileName = msgCodeClass.getFileName();
			javaSrc = classSrcExporter.exportClassSrc(artifactSpec, msgCodeClass, JavaSrcElm.EXP_TYPE_MSGCD_CLS);
			FileHandler.writeTextFile(msgCodeFileName, javaSrc, false);

		}
	}

	private void writeMsgCodeProp() throws IOException, AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		String msgCodeFullName = null;
		ClassSrc msgCodeClass = null;
		StringBuffer javaSrc = new StringBuffer();
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();

		logger.debug("MSG CD PROP: Begin to generate MSG CD property files ...");
		for (Enumeration<String> msgCodeKeys = CtxCacheFacade.getMsgCodeCacheKeys(); msgCodeKeys.hasMoreElements();) {
			msgCodeFullName = msgCodeKeys.nextElement();
			msgCodeClass = CtxCacheFacade.lookupMsgCodeClass(msgCodeFullName);
			javaSrc.append(classSrcExporter.exportClassSrc(artifactSpec, msgCodeClass, JavaSrcElm.EXP_TYPE_MSGCD_PROP));
		}

		// 将多个MsgCodeClass对应的属性输出在一个文件里
		if (StringUtils.isNotBlank(javaSrc.toString()) == true) {
			String msgPropFileName = JavaFormatter.getMsgPropAbsoluteName(workspaceSpec);
			FileHandler.writeTextFile(msgPropFileName, javaSrc.toString(), false);
		}

	}

}
