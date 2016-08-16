package bh.toolkit.srcgen.registry;

import org.apache.commons.lang3.StringUtils;

import bh.toolkit.srcgen.model.artifact.RelationshipSpec;

public class OneToOneIdxFacade {

	/**
	 * Take consideration for both 'mediTableName' and 'fatherTableName' in an one to one association, 'mediTableName' takes higher preference.
	 * 
	 * @param oneToOneIndex
	 * @return
	 */
	public static String getRelLeftTableName(OneToOneIdx oneToOneIndex) {

		//		String mediTableName = null;
		String leftTableName = oneToOneIndex.getLeftTableName();

		//		if (StringUtils.isNotBlank(mediTableName) == true) {
		//			return mediTableName;
		//		} else {
		return leftTableName;
		//		}

	}

	/**
	 * Take consideration for both 'mediTableAlias' and 'fatherTableAlias' in an one to one association, 'mediTableAlias' takes higher preference.
	 * 
	 * @param oneToOneIndex
	 * @return
	 */
	public static String getRelLeftTableAlias(OneToOneIdx oneToOneIndex) {

		//		String mediTableAlias = null;
		String leftTableAlias = oneToOneIndex.getLeftTableAlias();

		//		if (StringUtils.isNotBlank(mediTableAlias) == true) {
		//			return mediTableAlias;
		//		} else {
		return leftTableAlias;
		//		}

	}

	/**
	 * Take consideration for both 'mediAttr' and 'fatherAttr' in an one to one association, 'mediAttr' takes higher preference.
	 * 
	 * @param oneToOneIndex
	 * @return
	 */
	public static String getRelLeftAttrName(OneToOneIdx oneToOneIndex) {

		RelationshipSpec oneToOne = oneToOneIndex.getOneToOne();
		//		String medAttr = oneToOne.getMediumAttr();
		String medAttr = null;
		String fatherAttr = oneToOne.getFatherAttr();

		if (StringUtils.isNotBlank(medAttr) == true) {
			return medAttr;
		} else {
			return fatherAttr;
		}

	}

}
