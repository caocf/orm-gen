/**
 * Description: 
 * Author: zhaoruibin
 * Creation time: 2015年12月15日 上午12:00:08
 * (C) Copyright 2013-2016, Cloud Business Chain Corporation Limited.
 * All rights reserved.
 */
package bh.toolkit.srcgen.util;

import bh.toolkit.srcgen.lang.HtmlElm;
import bh.toolkit.srcgen.lang.JavaSrcElm;

public class WebPageFormatter {

	public static String getIdAndName(String voParamName, String attrName) {
		return JavaSrcElm.UNIT_OF_INDENT + HtmlElm.LESS_THAN + HtmlElm.ATTR_ID + HtmlElm.EQUAL_WITHOUT_BLANK + HtmlElm.DOUBLE_QUOTATION + voParamName
				+ HtmlElm.UNDER_LINE + attrName + HtmlElm.DOUBLE_QUOTATION + HtmlElm.WHITE_SPACE + HtmlElm.ATTR_NAME + HtmlElm.EQUAL_WITHOUT_BLANK
				+ HtmlElm.DOUBLE_QUOTATION + voParamName + HtmlElm.DOT + attrName + HtmlElm.DOUBLE_QUOTATION + HtmlElm.LARGER_THAN;
	}

}
