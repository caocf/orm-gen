/**
 * Description:
 * Author: zhaoruibin
 * Creation time: 2015年10月22日 上午11:34:36
 * (C) Copyright 2013-2016, Cloud Business Chain Corporation Limited.
 * All rights reserved.
 */
package bh.toolkit.srcgen.io;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.model.src.AttributeSrc;
import bh.toolkit.srcgen.model.src.ClassSrc;

/**
 * @author zhaorb
 */
public class ClassSrcImporter {

	/**
	 * 导入DTO已经存在的属性，由于DTO为工具生成，因此对被导入的内容不需要强大的语法校验，达到基本的特征识别即可
	 * 
	 * @param absoluteName
	 * @param dtoClass
	 * @throws IOException
	 */
	public static void importDtoAttr(String absoluteName, ClassSrc dtoClass) throws IOException {

		File dtoFile = new File(absoluteName);
		if (dtoFile.exists() == false || dtoFile.isFile() == false) {
			return;
		}
		List<String> fileLines = FileUtils.readLines(dtoFile);

		// 逐行读取发现的DTO文件，并识别出已经存在的属性
		Pattern pattern = null;
		Matcher matcher = null;
		String dataType = null;
		String attrName = null;
		String defaultValue = null;
		String verbComments = null;
		String objComments = null;
		AttributeSrc attrSrc = null;
		int bracketCnt = 0;
		for (String line : fileLines) {

			if (StringUtils.isBlank(line) == true) {
				continue;
			}

			// 如果包含2个（含）"{"，则跳过
			if (line.contains("{") == true) {
				bracketCnt++;
			}
			if (line.contains("}") == true) {
				bracketCnt--;
				continue;
			}
			if (bracketCnt >= 2) {
				continue;
			}

			// 如果以"package", "private static final"或"public static final"开始，则跳过
			//			pattern = Pattern
			//					.compile("^([ ]*package[ ]*|[ ]*" + JavaSrcElm.PRIVATE_STATIC_FINAL + "[ ]*|[ ]*" + JavaSrcElm.PUBLIC_STATIC_FINAL + "[ ]*)");
			pattern = Pattern.compile(
					"^([ \\t]*package\\s*|[ \\t]*" + JavaSrcElm.PRIVATE_STATIC_FINAL + "\\s*|[ \\t]*" + JavaSrcElm.PUBLIC_STATIC_FINAL + "\\s*)");
			matcher = pattern.matcher(line);
			if (matcher.matches() == true) {
				continue;
			}

			// 如果以"import"开始，则需要记录import的类型
			pattern = Pattern.compile("^([ ]*import)([ ]*)(.*)(;)");
			matcher = pattern.matcher(line);
			if (matcher.matches() == true) {
				dtoClass.addImport(matcher.group(3));
				continue;
			}

			// 如果是“private TheType typeName = "ABC"; \\ [verb]|[verb] Comments”格式，则被认为是完整的一行属性定义
			pattern = Pattern.compile(
					"^(\\s*private\\s*|\\s*protected\\s*|\\s*public\\s*)(\\w*)(\\s*)(\\w*)(\\s*=\\s*)(.*)(;\\s*\\x2F\\x2F\\s*)(\\[.*\\]\\|?)*(.*)");
			matcher = pattern.matcher(line);
			if (matcher.matches() == true) {

				// 数据类型
				dataType = matcher.group(2);
				// 名称
				attrName = matcher.group(4);
				// 默认值
				defaultValue = matcher.group(6);
				// 注释
				verbComments = StringUtils.isBlank(matcher.group(8)) ? "" : StringUtils.trimToEmpty(matcher.group(8));
				objComments = StringUtils.isBlank(matcher.group(9)) ? "" : StringUtils.trimToEmpty(matcher.group(9));
				// 初始化AttrSrc
				attrSrc = new AttributeSrc();
				attrSrc.setModifier(JavaSrcElm.PROTECTED);
				attrSrc.setDataType(dataType);
				attrSrc.setName(attrName);
				attrSrc.setDefaultValue(defaultValue);
				attrSrc.addVerbComments(verbComments);
				attrSrc.addObjComments(objComments);
				dtoClass.addAttr(attrSrc, true);

			}

		}

	}

	/**
	 * 导入SvcMsgCode中已经存在的属性，由于SvcMsgCode固定为工具生成，因此对被导入的内容不需要强大的语法校验，达到基本的特征识别即可
	 * 
	 * @param absoluteName
	 * @param dtoClass
	 * @throws IOException
	 */
	public static void importSvcMsgCodeAttr(String absoluteName, ClassSrc svcMsgCodeClass) throws IOException {

		File svcMsgCodeFile = new File(absoluteName);
		if (svcMsgCodeFile.exists() == false || svcMsgCodeFile.isFile() == false) {
			return;
		}
		List<String> fileLines = FileUtils.readLines(svcMsgCodeFile);

		// 逐行读取发现的SvcMsgCode文件，并识别出已经存在的属性
		Pattern pattern = null;
		Matcher matcher = null;
		String dataType = null;
		String attrName = null;
		String defaultValue = null;
		String comments = null;
		AttributeSrc attrSrc = null;
		for (String line : fileLines) {

			if (StringUtils.isBlank(line) == true) {
				continue;
			}

			// 如果以"package", "private static final"开始，则跳过
			pattern = Pattern.compile("^([ \\t]*package\\s*|[ \\t]*" + JavaSrcElm.PRIVATE_STATIC_FINAL + "\\s*)");
			matcher = pattern.matcher(line);
			if (matcher.matches() == true) {
				continue;
			}

			// 如果以"import"开始，则需要记录import的类型
			pattern = Pattern.compile("^([ ]*import)([ ]*)(.*)(;)");
			matcher = pattern.matcher(line);
			if (matcher.matches() == true) {
				svcMsgCodeClass.addImport(matcher.group(3));
				continue;
			}

			// 如果是“private TheType typeName = "ABC"; \\ [verb]|[verb] Comments”格式，则被认为是完整的一行属性定义
			pattern = Pattern.compile("^(\\s*public static final\\s*)(\\w*)(\\s*)(\\w*)(\\s*=\\s*)(.*)(;\\s*\\x2F\\x2F\\s*)(.*)");
			matcher = pattern.matcher(line);
			if (matcher.matches() == true) {

				// 数据类型
				dataType = matcher.group(2);
				// 名称
				attrName = matcher.group(4);
				// 默认值
				defaultValue = matcher.group(6);
				// 注释
				comments = StringUtils.isBlank(matcher.group(8)) ? "" : StringUtils.trimToEmpty(matcher.group(8));
				// 初始化AttrSrc
				attrSrc = new AttributeSrc();
				attrSrc.setModifier(JavaSrcElm.PUBLIC_STATIC_FINAL);
				attrSrc.setDataType(dataType);
				attrSrc.setName(attrName);
				attrSrc.setDefaultValue(defaultValue);
				attrSrc.addObjComments(comments);
				svcMsgCodeClass.addAttr(attrSrc, true);

			}

		}

	}

}
