package bh.toolkit.srcgen.util;

import java.util.HashSet;
import java.util.List;

import bh.toolkit.srcgen.model.artifact.AttributeSpec;
import bh.toolkit.srcgen.model.artifact.SelectSpec;

public class ProfileHelper {

	public static HashSet<String> getIncludedAttrName(SelectSpec selectSpec) {

		HashSet<String> attrNameSet = new HashSet<String>();
		List<AttributeSpec> attrSpecList = selectSpec.getIncludedAttr();

		if (attrSpecList == null) {
			return attrNameSet;
		}

		for (int i = 0; i < attrSpecList.size(); i++) {
			attrNameSet.add(attrSpecList.get(i).getName());
		}

		return attrNameSet;

	}

	public static HashSet<String> getExcludedAttrName(SelectSpec selectSpec) {

		HashSet<String> attrNameSet = new HashSet<String>();
		List<AttributeSpec> attrSpecList = selectSpec.getExcludedAttr();

		if (attrSpecList == null) {
			return attrNameSet;
		}

		for (int i = 0; i < attrSpecList.size(); i++) {
			attrNameSet.add(attrSpecList.get(i).getName());
		}

		return attrNameSet;

	}

	// public static boolean isMappedAttr(OrmAttr attr) {
	//
	// String attrType = attr.getDataType();
	// if (attrType.equals(JavaSrcElm.LANG_STRING_FULL) || attrType.equals(JavaSrcElm.LANG_LONG_FULL)
	// || attrType.equals(JavaSrcElm.LANG_BOOLEAN_FULL) || attrType.equals(JavaSrcElm.SQL_TIMESTAMP)
	// || attrType.equals(JavaSrcElm.MATH_BIG_DECIMAL) || attrType.equals(JavaSrcElm.BYTE_ARRAY)) {
	// return true;
	// } else {
	// return false;
	// }
	//
	// }

	// public static void validateFile(String fileName, String mapperFile) {
	//
	// if (fileName != null && mapperFile != null) {
	//
	// if (mapperFile.indexOf(MapperElm.NO_MATCHED_FOUND) >= 0) {
	// logger.warn("NOT MATCH: N/A found in file: " + fileName);
	// } else if (mapperFile.indexOf(MapperElm.NULL_PRIMARY_KEY) >= 0) {
	// logger.warn("NO PK: NO Primary Key in file: " + fileName);
	// } else {
	// logger.info("DONE: Generate successfully the SQL Map file: " + fileName);
	// }
	//
	// }
	//
	// }

}
