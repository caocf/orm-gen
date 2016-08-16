/**
 * Description: 
 * Author: zhaoruibin
 * Creation time: 2015年12月13日 下午2:15:10
 * (C) Copyright 2013-2016, Cloud Business Chain Corporation Limited.
 * All rights reserved.
 */
package bh.toolkit.srcgen.lang;

public class MsgCodeComments {

	public static final String MSG_CD_COMMENTS_L1 = "########################################" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L2 = "// ##  系统提示信息分类原则" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L3 = "// ########################################" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L4 = "// ##" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L5 = "// ##  1. 根据头两位区分业务类别" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L6 = "// ##    000000 - 099999: 预留区段" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L7 = "// ##" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L8 = "// ##    110000 - 119999: 参与方管理 Party Service" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L9 = "// ##    140000 - 149999: 社区管理 Community Service" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L10 = "// ##    170000 - 179999: 商品管理 Goods Service" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L11 = "// ##    180000 - 189999: 采购管理 Purchase Service" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L12 = "// ##    190000 - 199999: 销售管理 Sales Service" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L13 = "// ##    200000 - 209999: 库存管理 Inventory Service" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L14 = "// ##    230000 - 239999: 运输管理 Transportation Service" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L15 = "// ##    260000 - 269999: 设备管理 Facility Service" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L16 = "// ##    290000 - 299999: 路线管理 Route Service" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L17 = "// ##" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L18 = "// ##    300000 - 809999: 预留区段 Reserved" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L19 = "// ##" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L20 = "// ##    810000 - 819999: 基础服务 Base Service" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L21 = "// ##    820000 - 999999: 预留区段 Reserved" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L22 = "// ##" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L23 = "// ##  2. 每个业务类别内部，系统的提示信息分三个层次（不强制要求）" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L24 = "// ##    XX2XXX - XX2XXX: 信息" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L25 = "// ##    XX4XXX - XX4XXX: 警告" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L26 = "// ##    XX6XXX - XX6XXX: 错误" + JavaSrcElm.LINE_SEPARATOR;
	public static final String MSG_CD_COMMENTS_L27 = "// ########################################" + JavaSrcElm.LINE_SEPARATOR;

	public static final String MSG_CD_COMMENTS = MSG_CD_COMMENTS_L1 + MSG_CD_COMMENTS_L2 + MSG_CD_COMMENTS_L3 + MSG_CD_COMMENTS_L4
			+ MSG_CD_COMMENTS_L5 + MSG_CD_COMMENTS_L6 + MSG_CD_COMMENTS_L7 + MSG_CD_COMMENTS_L8 + MSG_CD_COMMENTS_L9 + MSG_CD_COMMENTS_L10
			+ MSG_CD_COMMENTS_L11 + MSG_CD_COMMENTS_L12 + MSG_CD_COMMENTS_L13 + MSG_CD_COMMENTS_L14 + MSG_CD_COMMENTS_L15 + MSG_CD_COMMENTS_L16
			+ MSG_CD_COMMENTS_L17 + MSG_CD_COMMENTS_L18 + MSG_CD_COMMENTS_L19 + MSG_CD_COMMENTS_L20 + MSG_CD_COMMENTS_L21 + MSG_CD_COMMENTS_L22
			+ MSG_CD_COMMENTS_L23 + MSG_CD_COMMENTS_L24 + MSG_CD_COMMENTS_L25 + MSG_CD_COMMENTS_L26 + MSG_CD_COMMENTS_L27;

}
