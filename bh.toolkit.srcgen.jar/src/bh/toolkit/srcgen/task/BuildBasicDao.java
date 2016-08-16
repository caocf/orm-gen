package bh.toolkit.srcgen.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.io.ClassSrcExporter;
import bh.toolkit.srcgen.io.FileHandler;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.CommonAttrSpec;
import bh.toolkit.srcgen.model.artifact.DaoSpec;
import bh.toolkit.srcgen.model.artifact.VoSpec;
import bh.toolkit.srcgen.model.rdb.TableSpec;
import bh.toolkit.srcgen.model.src.ClassSrc;
import bh.toolkit.srcgen.registry.CtxCacheFacade;
import bh.toolkit.srcgen.util.ExceptionUtil;
import bh.toolkit.srcgen.util.JavaFormatter;

public class BuildBasicDao extends Task {

	private static Logger logger = Logger.getLogger(BuildBasicDao.class);

	private ArtifactSpec artifactSpec;

	public void addArtifactSpec(ArtifactSpec artifactSpec) {
		this.artifactSpec = artifactSpec;
	}

	public void execute() throws BuildException {

		try {

			CtxCacheFacade.clearCache(); // Clear all caches firstly.
			restoreProfile(); // 从缓存中恢复配置参数

			//			String skipTableNamePat = "([_a-zA-Z0-9]*)(\\.?[\\d].*)";
			String skipTableNamePat = "([_a-zA-Z]*)(\\.)([0-9])(.*)";
			writeVo(skipTableNamePat); // 输出VO
			writeDto(skipTableNamePat); // 输出DTO和DTOX
			writeMapper(skipTableNamePat); // 输出Mapper和MapperX
			writeDao(skipTableNamePat); // 输出DAO接口
			writeDaoX(skipTableNamePat); // 输出DAOX接口

		} catch (Throwable t) {
			logger.error(t.getMessage(), t.getCause());
			throw new BuildException(t);
		}

	}

	private void restoreProfile() {
		CommonAttrSpec commonAttrSpec = CtxCacheFacade.getCommonAttrSpec();
		artifactSpec.setCommonAttrSpec(commonAttrSpec);
	}

	private void writeVo(String skipTableNamePat) throws AppException {

		// Get the cached VO objects.
		String voClassFullName = null;
		String voClassFileName = null;
		ClassSrc voClass = null;
		String voSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();
		int fileCnt = 0;

		try {

			// 从缓存中过滤出符合条件的数据库表，并注册VO
			List<VoSpec> voSpecList = artifactSpec.getVoSpec();
			CtxCacheFacade.addVoClass(artifactSpec.getCommonAttrSpec().getWorkspaceSpec(), voSpecList, skipTableNamePat);

			logger.debug("VO: Begin to generate VO class files ...");

			for (Enumeration<String> voClassKeys = CtxCacheFacade.getVoCacheKeys(); voClassKeys.hasMoreElements();) {

				voClassFullName = voClassKeys.nextElement();
				voClass = CtxCacheFacade.lookupVoClass(voClassFullName);
				voSrc = classSrcExporter.exportClassSrc(artifactSpec, voClass, JavaSrcElm.EXP_TYPE_VO);

				// Write into file.
				voClassFileName = voClass.getFileName();
				FileHandler.writeTextFile(voClassFileName, voSrc, false);
				fileCnt++;

			}

			logger.info("【VO TOTAL】There are total " + fileCnt + " VO(s).");

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void writeDto(String skipTableNamePat) throws AppException {

		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();
		String tableFullName = null;
		String dtoIntfFullName = null;
		ClassSrc dtoIntf = null;
		String dtoIntfSrc = null;
		String dtoFileName = null;
		String dtoXIntfFullName = null;
		ClassSrc dtoXIntf = null;
		String dtoXIntfSrc = null;
		String dtoXFileName = null;

		int fileCnt = 0;

		try {

			logger.debug("DTO: Begin to generate DTO(X) class files ...");

			// 针对每个数据库表构建DTO(X)
			Pattern pattern = Pattern.compile(skipTableNamePat);
			Matcher matcher = null;
			for (Enumeration<String> tableSpecKeys = CtxCacheFacade.getTableSpecCacheKeys(); tableSpecKeys.hasMoreElements();) {
				tableFullName = tableSpecKeys.nextElement();
				matcher = pattern.matcher(tableFullName);
				if (matcher.matches() == true) {
					logger.info("SKIP TABLE: Table name \"" + tableFullName + "\" matches the given pattern: \"" + skipTableNamePat + "\", skip.");
					continue;
				}
				CtxCacheFacade.addDtoXClass(artifactSpec, tableFullName);
			}

			// 输出DTO
			for (Enumeration<String> dtoIntfKeys = CtxCacheFacade.getDtoCacheKeys(); dtoIntfKeys.hasMoreElements();) {
				dtoIntfFullName = dtoIntfKeys.nextElement();
				dtoIntf = CtxCacheFacade.lookupDtoClass(dtoIntfFullName);
				dtoIntfSrc = classSrcExporter.exportClassSrc(artifactSpec, dtoIntf, JavaSrcElm.EXP_TYPE_DTO);
				dtoFileName = dtoIntf.getFileName();
				FileHandler.writeTextFile(dtoFileName, dtoIntfSrc, false);
				fileCnt++;
			}

			logger.info("【DTO TOTAL】There are total " + fileCnt + " DTO(s).");
			fileCnt = 0;

			// 判断DTOX文件是否已经存在，如果不存在，则生成一个新的
			for (Enumeration<String> dtoXIntfKeys = CtxCacheFacade.getDtoXCacheKeys(); dtoXIntfKeys.hasMoreElements();) {

				dtoXIntfFullName = dtoXIntfKeys.nextElement();
				dtoXIntf = CtxCacheFacade.lookupDtoXClass(dtoXIntfFullName);
				if (dtoXIntf.isExistingFile() == true) {
					logger.info("--【 已存在，保留】" + dtoXIntf.getFileName());
				} else {
					// Write a new file.
					dtoXFileName = dtoXIntf.getFileName();
					dtoXIntfSrc = classSrcExporter.exportClassSrc(artifactSpec, dtoXIntf, JavaSrcElm.EXP_TYPE_DTO);
					FileHandler.writeTextFile(dtoXFileName, dtoXIntfSrc, false);
				}
				fileCnt++;

			}

			logger.info("【DTOX TOTAL】There are total " + fileCnt + " DTOX(s).");

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	/**
	 * Description: 为每个表格构建Mapper
	 * TODO 后续需要增加delete方法
	 * 
	 * Author: zhaoruibin
	 * Creation time: 2015年10月30日 上午10:01:11
	 *
	 * @throws IOException
	 * @throws AppException 
	 */
	private void writeMapper(String skipTableNamePat) throws AppException {

		// 获得基本配置
		bh.toolkit.srcgen.model.artifact.ObjectFactory artifactObjFactory = new bh.toolkit.srcgen.model.artifact.ObjectFactory();

		try {
			// Get the cached DAO objects.
			List<DaoSpec> daoSpecList = new ArrayList<DaoSpec>();
			DaoSpec daoSpec = null;
			String tableFullName = null;
			TableSpec tableSpec = null;

			logger.debug("MAPPER: Begin to generate Mapper(X) class files ...");

			// 针对每个数据库表构建daoSpec
			Pattern pattern = Pattern.compile(skipTableNamePat);
			Matcher matcher = null;
			for (Enumeration<String> tableSpecKeys = CtxCacheFacade.getTableSpecCacheKeys(); tableSpecKeys.hasMoreElements();) {
				tableFullName = tableSpecKeys.nextElement();
				matcher = pattern.matcher(tableFullName);
				if (matcher.matches() == true) {
					logger.info("SKIP TABLE: Table name \"" + tableFullName + "\" matches the given pattern: \"" + skipTableNamePat + "\", skip.");
					continue;
				}
				tableSpec = CtxCacheFacade.lookupTableSpec(tableFullName);
				daoSpec = artifactObjFactory.createDaoSpec();
				daoSpec.setShortName(tableFullName);
				daoSpec.setDesc(tableSpec.getComments());
				daoSpecList.add(daoSpec);
			}

			BuildTrx genTrx = new BuildTrx();
			genTrx.addArtifactSpec(artifactSpec);
			genTrx.buildMapper(daoSpecList);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	private void writeDao(String skipTableNamePat) throws IOException {

		// Get the cached DAO interfaces.
		String daoIntfFullName = null;
		String daoFileName = null;
		ClassSrc daoIntf = null;
		String javaSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();
		int fileCnt = 0;

		logger.debug("DAO INTF: Begin to generate DAO interface files ...");

		// 针对每个数据库表查找DAO
		Pattern pattern = Pattern.compile(skipTableNamePat);
		Matcher matcher = null;
		String tableFullName = null;
		for (Enumeration<String> tableSpecKeys = CtxCacheFacade.getTableSpecCacheKeys(); tableSpecKeys.hasMoreElements();) {

			tableFullName = tableSpecKeys.nextElement();
			matcher = pattern.matcher(tableFullName);
			if (matcher.matches() == true) {
				logger.info("SKIP TABLE: Table name \"" + tableFullName + "\" matches the given pattern: \"" + skipTableNamePat + "\", skip.");
				continue;
			}

			//		for (Enumeration<String> daoIntfKeys = CtxCacheFacade.getDaoIntfCacheKeys(); daoIntfKeys.hasMoreElements();) {
			//			daoIntfFullName = daoIntfKeys.nextElement();
			daoIntfFullName = JavaFormatter.getDaoFullName(artifactSpec.getCommonAttrSpec().getWorkspaceSpec(), tableFullName);
			daoIntf = CtxCacheFacade.lookupDaoIntf(daoIntfFullName);
			javaSrc = classSrcExporter.exportClassSrc(artifactSpec, daoIntf, JavaSrcElm.EXP_TYPE_DAO_INTF);
			daoFileName = daoIntf.getFileName();
			FileHandler.writeTextFile(daoFileName, javaSrc, false);
			fileCnt++;

		}

		logger.info("【VO TOTAL】There are total " + fileCnt + " DAO(s).");

	}

	private void writeDaoX(String skipTableNamePat) throws IOException {

		// Get the cached DAOX interfaces.
		String daoXIntfFullName = null;
		String daoXFileName = null;
		ClassSrc daoXIntf = null;
		String daoXIntfSrc = null;
		ClassSrcExporter classSrcExporter = ClassSrcExporter.getInstance();
		int fileCnt = 0;

		logger.debug("DAOX INTF: Begin to generate DAOX interface files ...");

		// 针对每个数据库表查找DAO
		Pattern pattern = Pattern.compile(skipTableNamePat);
		Matcher matcher = null;
		String tableFullName = null;
		for (Enumeration<String> tableSpecKeys = CtxCacheFacade.getTableSpecCacheKeys(); tableSpecKeys.hasMoreElements();) {

			tableFullName = tableSpecKeys.nextElement();
			matcher = pattern.matcher(tableFullName);
			if (matcher.matches() == true) {
				logger.info("SKIP TABLE: Table name \"" + tableFullName + "\" matches the given pattern: \"" + skipTableNamePat + "\", skip.");
				continue;
			}

			//		for (Enumeration<String> daoXIntfKeys = CtxCacheFacade.getDaoXIntfCacheKeys(); daoXIntfKeys.hasMoreElements();) {

			//			daoXIntfFullName = daoXIntfKeys.nextElement();
			daoXIntfFullName = JavaFormatter.getDaoXFullName(artifactSpec.getCommonAttrSpec().getWorkspaceSpec(), tableFullName);
			daoXIntf = CtxCacheFacade.lookupDaoXIntf(daoXIntfFullName);

			// 判断DAOX文件是否已经存在，如果不存在，则生成一个新的
			if (daoXIntf.isExistingFile() == true) {
				logger.info("--【 已存在，保留】" + daoXIntf.getFileName());
			} else {
				// Write a new file.
				daoXFileName = daoXIntf.getFileName();
				daoXIntfSrc = classSrcExporter.exportClassSrc(artifactSpec, daoXIntf, JavaSrcElm.EXP_TYPE_DAO_INTF);
				FileHandler.writeTextFile(daoXFileName, daoXIntfSrc, false);
			}
			fileCnt++;

		}

		logger.info("【VO TOTAL】There are total " + fileCnt + " DAOX(s).");

	}

}
