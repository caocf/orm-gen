/**
 * Description: 
 * Author: zhaoruibin
 * Creation time: 2015年10月22日 上午11:34:36
 * (C) Copyright 2013-2016, Cloud Business Chain Corporation Limited.
 * All rights reserved.
 */
package bh.toolkit.srcgen.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;

/**
 * @author zhaorb
 */
public class WorkspaceUtil {

	/**
	 * 获取源代码路径
	 * 
	 * @param workspaceSpec
	 * @param prjName
	 * @return
	 * @throws AppException
	 */
	public static String getSrcPath(WorkspaceSpec workspaceSpec, String prjName) throws AppException {
		// String srcPath = workspaceSpec.getIdeWorkspace() + File.separator + prjName + File.separator + workspaceSpec.getSrcPathName() + File.separator;
		String srcPath = getWorkspacePath() + File.separator + prjName + File.separator + workspaceSpec.getSrcPathName() + File.separator;
		return srcPath;
	}

	public static String getRsrcPath(WorkspaceSpec workspaceSpec, String prjName) throws AppException {
		// String srcPath = workspaceSpec.getIdeWorkspace() + File.separator + prjName + File.separator + workspaceSpec.getRsrcPathName() + File.separator;
		String srcPath = getWorkspacePath() + File.separator + prjName + File.separator + workspaceSpec.getRsrcPathName() + File.separator;
		return srcPath;
	}

	private static String getWorkspacePath() throws AppException {

		String classPath = WorkspaceUtil.class.getResource("").toString();
		String filePath = classPath.replaceFirst("file:/", "");
		filePath = filePath.replaceFirst("/bin", "/src");

		Pattern workspacePat = Pattern.compile("(.*)/(.*)/src/(.*)"); // 根据正则表达式解析出工作区所在目录
		Matcher matcher = workspacePat.matcher(filePath);
		if (matcher.matches() == false) {
			throw new AppException("定位工作区目录出错，当前类WorkspaceUtil的绝对路径：" + classPath);
		}
		return matcher.group(1);

	}

	// public static void main(String[] args) {
	//
	// String classPath = WorkspaceUtil.class.getResource("").toString();
	// String filePath = classPath.replaceFirst("file:/", "");
	// filePath = filePath.replaceFirst("/bin", "/src");
	// Pattern workspacePat = Pattern.compile("(.*)/(.*)/src/(.*)"); // 根据正则表达式解析出工作区所在目录
	// Matcher matcher = workspacePat.matcher(filePath);
	// if (matcher.matches() == false) {
	// throw new AppException();
	// }
	// matcher.group(1);
	//
	// }

}
