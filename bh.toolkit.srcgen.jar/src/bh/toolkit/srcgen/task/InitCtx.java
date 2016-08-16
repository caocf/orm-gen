package bh.toolkit.srcgen.task;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import bh.toolkit.srcgen.connector.rdb.DBAnalyzer;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.CommonAttrSpec;
import bh.toolkit.srcgen.model.artifact.DatasourceSpec;
import bh.toolkit.srcgen.registry.CtxCacheFacade;

public class InitCtx extends Task {

	private static Logger logger = Logger.getLogger(InitCtx.class);

	private ArtifactSpec artifactSpec;

	public void addArtifactSpec(ArtifactSpec artifactSpec) {
		this.artifactSpec = artifactSpec;
	}

	public void execute() throws BuildException {

		try {

			// Clear all caches firstly.
			CtxCacheFacade.clearCache();

			// Get the global settings.
			CommonAttrSpec commonAttrSpec = artifactSpec.getCommonAttrSpec();
			DatasourceSpec datasourceSpec = commonAttrSpec.getDatasourceSpec();

			// Connection to DB, then cache the Tables and Columns into CtxCache.
			DBAnalyzer.analyzeTables(datasourceSpec);

			// 为了避免重复配置，全局性配置信息仅有当前Ant任务读取。
			// 考虑到Ant任务的边界，需要把当前Ant任务生命周期内读取的配置信息进行复制并且缓存，便于后续Ant任务使用。
			CommonAttrSpec copyOfCommonAttrSpec  = (CommonAttrSpec) BeanUtils.cloneBean(commonAttrSpec);
			CtxCacheFacade.setCommonAttrSpec(copyOfCommonAttrSpec);

		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new BuildException(t);
		}

	}

}
