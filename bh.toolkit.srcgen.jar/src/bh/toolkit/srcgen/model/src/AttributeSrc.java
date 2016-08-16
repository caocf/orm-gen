package bh.toolkit.srcgen.model.src;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import bh.toolkit.srcgen.lang.JavaSrcElm;

public class AttributeSrc implements Comparable<AttributeSrc> {

	private List<String> annotList;
	private String modifier;
	private String dataType;
	private String name;
	private String defaultValue;
	private String verbComments;
	private String objComments;
	private boolean isPrimaryKey;
	private int ordInAttrList = -1;

	@Override
	public int compareTo(AttributeSrc peerAttrSrc) {
		String peerName = peerAttrSrc.getName();
		return name.compareTo(peerName);
	}

	public void addVerbComments(String comments) {
		if (StringUtils.isBlank(verbComments) == true) {
			verbComments = comments;
		} else { // 不重复增加注释
			if (verbComments.contains(comments) == false) {
				verbComments = verbComments + JavaSrcElm.VERTICAL_SIMPLE + comments;
			}
		}
	}

	public void addObjComments(String comments) {
		if (StringUtils.isBlank(comments) == true) {
			return;
		}
		if (StringUtils.isBlank(objComments) == true) {
			objComments = comments;
		} else { // 不重复增加注释
			if (objComments.contains(comments) == false) {
				objComments = objComments + JavaSrcElm.VERTICAL_SIMPLE + comments;
			}
		}
	}

	public AttributeSrc() {
		this.annotList = new ArrayList<String>();
	}

	public void addAnnot(String annot) {
		annotList.add(annot);
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getOrdInAttrList() {
		return ordInAttrList;
	}

	public void setOrdInAttrList(int ordInAttrList) {
		this.ordInAttrList = ordInAttrList;
	}

	public List<String> getAnnotList() {
		return annotList;
	}

	public void setAnnotList(List<String> annotList) {
		this.annotList = annotList;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public String getVerbComments() {
		return verbComments;
	}

	public String getObjComments() {
		return objComments;
	}

}
