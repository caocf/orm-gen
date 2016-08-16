package bh.toolkit.srcgen.io;

import java.util.ArrayList;
import java.util.List;

import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;
import bh.toolkit.srcgen.model.src.ClassSrc;
import bh.toolkit.srcgen.util.StringHelper;

public class ExportHelper {

	/**
	 * 导出包
	 * 
	 * @param pkg
	 * @return
	 */
	public static String exportPackage(String pkg) {

		StringBuffer pkgSrc = new StringBuffer();
		pkgSrc.append(JavaSrcElm.PACKAGE);
		pkgSrc.append(JavaSrcElm.WHITE_SPACE);
		pkgSrc.append(pkg);
		pkgSrc.append(JavaSrcElm.SEMICOLON);
		pkgSrc.append(JavaSrcElm.LINE_SEPARATOR);

		return pkgSrc.toString();

	}

	/**
	 * 导出所有import引用
	 * 
	 * @param importList
	 * @return
	 */
	public static String exportAllImport(ArtifactSpec artifactSpec, ClassSrc classSrc) {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
		StringBuffer importSrc = new StringBuffer();

		List<String> importList = classSrc.getImportList();
		int importListSize = importList.size();
		if (importListSize == 0) {
			return importSrc.toString();
		}

		List<String> javaImport = new ArrayList<String>();
		List<String> orgImport = new ArrayList<String>();
		List<String> transObjImport = new ArrayList<String>();
		List<String> appImport = new ArrayList<String>();
		List<String> otherImport = new ArrayList<String>();

		String importLine = null;
		for (int i = 0; i < importListSize; i++) {

			importLine = importList.get(i).toString();

			// 甄别出对JRE基础包的引用
			if (importLine.startsWith(JavaSrcElm.JAVA_PKG_PREFIX) == true) {
				javaImport.add(importLine);
			}
			// 其次是org开头的第三方包
			else if (importLine.startsWith(JavaSrcElm.ORG_PKG_PREFIX) == true) {
				orgImport.add(importLine);
			}
			// 对VO、DTO的应用
			else if (importLine.indexOf(JavaSrcElm.VO_PKG_NAME_PAT) != -1 || importLine.indexOf(JavaSrcElm.DTO_PKG_NAME_PAT) != -1) {
				transObjImport.add(importLine);
			}
			// 对应用模块包的引用
			else if (importLine.startsWith(workspaceSpec.getPkgPrefix()) == true) {
				appImport.add(importLine);
			}
			// 其他引用
			else {
				otherImport.add(importLine);
			}

		}

		importSrc.append(exportImportGrp(javaImport));
		if (javaImport.size() > 0) {
			importSrc.append(JavaSrcElm.LINE_SEPARATOR);
		}
		importSrc.append(exportImportGrp(orgImport));
		if (orgImport.size() > 0) {
			importSrc.append(JavaSrcElm.LINE_SEPARATOR);
		}
		importSrc.append(exportImportGrp(transObjImport));
		importSrc.append(exportImportGrp(appImport));
		if (transObjImport.size() > 0 || appImport.size() > 0) {
			importSrc.append(JavaSrcElm.LINE_SEPARATOR);
		}
		importSrc.append(exportImportGrp(otherImport));
		if (otherImport.size() > 0) {
			importSrc.append(JavaSrcElm.LINE_SEPARATOR);
		}

		return importSrc.toString();

	}

	/**
	 * 导出一组import引用
	 * 
	 * @param importList
	 * @return
	 */
	public static String exportImportGrp(List<String> importList) {

		StringBuffer importSrc = new StringBuffer();

		int importListSize = importList.size();
		if (importListSize == 0) {
			return importSrc.toString();
		}

		String importLine = null;
		for (int i = 0; i < importListSize; i++) {
			importLine = importList.get(i).toString();
			importSrc.append(JavaSrcElm.IMPORT);
			importSrc.append(JavaSrcElm.WHITE_SPACE);
			importSrc.append(importLine);
			importSrc.append(JavaSrcElm.SEMICOLON);
			importSrc.append(JavaSrcElm.LINE_SEPARATOR);
		}

		return importSrc.toString();

	}

	// public static String exportAnnot(ClassSrc classSrc) {
	//
	// StringBuffer srcCode = new StringBuffer();
	//
	// List<String> annotList = classSrc.getAnnotList();
	// int annotListSize = annotList.size();
	// if (annotListSize == 0) {
	// return srcCode.toString();
	// }
	//
	// for (int i = 0; i < annotListSize; i++) {
	// srcCode.append(JavaSrcElm.ANNOT_COMPN);
	// srcCode.append(JavaSrcElm.LINE_SEPARATOR);
	// }
	//
	// return srcCode.toString();
	//
	// }

	/**
	 * 导出声明名称
	 * 
	 * @param classSrc
	 * @param isClass
	 * @return
	 */
	public static String exportTypeStmt(ClassSrc classSrc, boolean isClass) {

		StringBuffer srcCode = new StringBuffer();

		// Build type and name.
		srcCode.append(JavaSrcElm.PUBLIC);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		if (isClass == true) {
			srcCode.append(JavaSrcElm.CLASS);
		} else {
			srcCode.append(JavaSrcElm.INTERFACE);
		}
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(classSrc.getSimpleName());

		return srcCode.toString();

	}

	/**
	 * 导出超类或接口
	 * 
	 * @param classSrc
	 * @return
	 */
	public static String exportSuperCls(ClassSrc classSrc) {

		List<String> extendsList = classSrc.getExtendsList();
		List<String> implementsList = classSrc.getImplementsList();

		StringBuffer srcCode = new StringBuffer();
		int extendsListSize = extendsList.size();
		if (extendsListSize != 0) {
			for (int i = 0; i < extendsListSize; i++) {
				if (i == 0) {
					srcCode.append(JavaSrcElm.WHITE_SPACE);
					srcCode.append(JavaSrcElm.EXTENDS);
				} else {
					srcCode.append(JavaSrcElm.COMMA);
				}
				srcCode.append(JavaSrcElm.WHITE_SPACE);
				srcCode.append(extendsList.get(i));
			}
		}

		// 导出实现接口
		int implementsListSize = implementsList.size();
		if (implementsListSize != 0) {
			for (int i = 0; i < implementsListSize; i++) {
				if (i == 0) {
					srcCode.append(JavaSrcElm.WHITE_SPACE);
					srcCode.append(JavaSrcElm.IMPLEMENTS);
				} else {
					srcCode.append(JavaSrcElm.COMMA);
				}
				srcCode.append(JavaSrcElm.WHITE_SPACE);
				srcCode.append(implementsList.get(i));
			}
		}

		return srcCode.toString();

	}

	/**
	 * 导出左括号
	 * 
	 * @return
	 */
	public static String exportOpeningBrace() {

		StringBuffer srcCode = new StringBuffer();

		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.LEFT_BRACKET);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();

	}

	/**
	 * 导出隐性构造函数
	 * 
	 * @param classSrc
	 * @return
	 */
	public static String exportImplicitConstructor(ClassSrc classSrc) {

		StringBuffer srcCode = new StringBuffer();
		String indents = exportIndents();

		srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		srcCode.append(indents);
		srcCode.append(JavaSrcElm.PUBLIC);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(classSrc.getSimpleName());
		srcCode.append(JavaSrcElm.CLOSE_PARENTHESIS);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(exportOpeningBracket());
		srcCode.append(exportClosingBracket());

		return srcCode.toString();

	}

	/**
	 * 导出用于对象序列化时识别版本的编号
	 * 
	 * @return
	 */
	public static String exportVerUid() {

		StringBuffer srcCode = new StringBuffer();
		String indents = exportIndents();

		srcCode.append(indents);
		srcCode.append(JavaSrcElm.PRIVATE_STATIC_FINAL);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.LONG);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.SERIAL_VERSION_UID);
		srcCode.append(JavaSrcElm.EQUAL);
		srcCode.append("1");
		srcCode.append(JavaSrcElm.LONG_SUFFIX);
		srcCode.append(JavaSrcElm.SEMICOLON);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();

	}

	/**
	 * 导出用于识别对象是否为空的属性标识
	 * 
	 * @return
	 */
	public static String exportIsBlankObj() {

		StringBuffer srcCode = new StringBuffer();
		String indents = exportIndents();

		srcCode.append(indents);
		srcCode.append(JavaSrcElm.PRIVATE);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.BOOLEAN);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.IS_BLANK_OBJ);
		srcCode.append(JavaSrcElm.EQUAL);
		srcCode.append(JavaSrcElm.TRUE);
		srcCode.append(JavaSrcElm.SEMICOLON);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();

	}

	public static String exportCloneMeth() {

		StringBuffer srcCode = new StringBuffer();
		String indents = exportIndents();

		srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		srcCode.append(indents);
		srcCode.append(JavaSrcElm.PUBLIC);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.EXPR_OBJ_CLONE_METH);
		StringHelper.newLine(srcCode, indents, 2);
		srcCode.append(JavaSrcElm.RETURN);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(JavaSrcElm.EXPR_SUPER_CLONE);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		srcCode.append(exportClosingBracket());

		return srcCode.toString();

	}

	public static String exportOpeningBracket() {

		StringBuffer srcCode = new StringBuffer();

		srcCode.append(JavaSrcElm.LEFT_BRACKET);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();

	}

	public static String exportClosingBracket() {

		String indents = exportIndents();

		StringBuffer srcCode = new StringBuffer();
		srcCode.append(indents);
		srcCode.append(JavaSrcElm.RIGHT_BRACKET);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();

	}

	public static String exportIndents() {

		StringBuffer srcCode = new StringBuffer();
		int count = JavaSrcElm.NUM_OF_SPACE_PER_TAB;
		for (int i = 0; i < count; i++) {
			srcCode.append(JavaSrcElm.WHITE_SPACE);
		}

		return srcCode.toString();

	}

	public static String exportLineComments(String comments) {

		StringBuffer srcCode = new StringBuffer();
		srcCode.append(JavaSrcElm.LINE_COMMENT);
		srcCode.append(JavaSrcElm.WHITE_SPACE);
		srcCode.append(comments);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();

	}

	public static String exportLog4JStmt(String classSimpleName) {

		StringBuffer srcCode = new StringBuffer();
		srcCode.append(ExportHelper.exportIndents());
		srcCode.append("private static final Logger logger = Logger.getLogger(" + classSimpleName + ".class);");
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();
	}

	public static String exportAnnot(String annot) {

		StringBuffer srcCode = new StringBuffer();
		srcCode.append(annot);
		srcCode.append(JavaSrcElm.LINE_SEPARATOR);

		return srcCode.toString();

	}

	public static String exportAnnot(String indents, List<String> annotList) {

		if (annotList.size() == 0) {
			return "";
		}

		StringBuffer srcCode = new StringBuffer();
		for (String annot : annotList) {
			srcCode.append(indents);
			srcCode.append(annot);
			srcCode.append(JavaSrcElm.LINE_SEPARATOR);
		}

		return srcCode.toString();

	}

}
