package bh.toolkit.srcgen.registry;

import bh.toolkit.srcgen.model.artifact.CaseFilterSpec;
import bh.toolkit.srcgen.model.artifact.RelationshipSpec;

public class OneToOneIdx {

	private RelationshipSpec oneToOne;
	private String attrNameChain;
	private String leftTableName;
	private String leftTableAlias;
	private String rightTableName;
	private String rightTableAlias;
	private boolean hasPeer;
	private OneToOneIdx fatherOtoIndex;
	private int fatherOtoIndexLoc;
	private CaseFilterSpec caseFilterSpec; // 在所有一对一中出现的"caseFilterSpec"都被收集到该对象中

	/**
	 * @return the oneToOne
	 */
	public RelationshipSpec getOneToOne() {
		return oneToOne;
	}

	/**
	 * @param oneToOne
	 *            the oneToOne to set
	 */
	public void setOneToOne(RelationshipSpec oneToOne) {
		this.oneToOne = oneToOne;
	}

	public String getAttrNameChain() {
		return attrNameChain;
	}

	public void setAttrNameChain(String attrNameChain) {
		this.attrNameChain = attrNameChain;
	}

	public boolean isHasPeer() {
		return hasPeer;
	}

	public void setHasPeer(boolean hasPeer) {
		this.hasPeer = hasPeer;
	}

	public OneToOneIdx getFatherOtoIndex() {
		return fatherOtoIndex;
	}

	public void setFatherOtoIndex(OneToOneIdx fatherOtoIndex) {
		this.fatherOtoIndex = fatherOtoIndex;
	}

	public int getFatherOtoIndexLoc() {
		return fatherOtoIndexLoc;
	}

	public void setFatherOtoIndexLoc(int fatherOtoIndexLoc) {
		this.fatherOtoIndexLoc = fatherOtoIndexLoc;
	}

	public CaseFilterSpec getCaseFilterSpec() {
		return caseFilterSpec;
	}

	public void setCaseFilterSpec(CaseFilterSpec caseFilterSpec) {
		this.caseFilterSpec = caseFilterSpec;
	}

	public String getLeftTableName() {
		return leftTableName;
	}

	public void setLeftTableName(String leftTableName) {
		this.leftTableName = leftTableName;
	}

	public String getLeftTableAlias() {
		return leftTableAlias;
	}

	public void setLeftTableAlias(String leftTableAlias) {
		this.leftTableAlias = leftTableAlias;
	}

	public String getRightTableName() {
		return rightTableName;
	}

	public void setRightTableName(String rightTableName) {
		this.rightTableName = rightTableName;
	}

	public String getRightTableAlias() {
		return rightTableAlias;
	}

	public void setRightTableAlias(String rightTableAlias) {
		this.rightTableAlias = rightTableAlias;
	}

}
