//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.11 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2015.12.12 时间 08:37:42 PM CST 
//

package bh.toolkit.srcgen.model.artifact;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>RelationshipSpec complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="RelationshipSpec"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="selectSpec" type="{http://srcgen.toolkit.bh/model/artifact}SelectSpec"/&gt;
 *         &lt;element name="insertSpec" type="{http://srcgen.toolkit.bh/model/artifact}InsertSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="updateByPKSpec" type="{http://srcgen.toolkit.bh/model/artifact}UpdateSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="updateBySqlSpec" type="{http://srcgen.toolkit.bh/model/artifact}UpdateSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="deleteByPKSpec" type="{http://srcgen.toolkit.bh/model/artifact}DeleteSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="deleteBySqlSpec" type="{http://srcgen.toolkit.bh/model/artifact}DeleteSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="listOfSon" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="refToSon" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="refToRel" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="refToRight" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="fatherAttr" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="sonAttr" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="leftAttr" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="rightAttr" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="joinType" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RelationshipSpec", propOrder = { "selectSpec", "insertSpec", "updateByPKSpec", "updateBySqlSpec", "deleteByPKSpec",
		"deleteBySqlSpec" })
public class RelationshipSpec {

	@XmlElement(required = true)
	protected SelectSpec selectSpec;
	protected List<InsertSpec> insertSpec;
	protected List<UpdateSpec> updateByPKSpec;
	protected List<UpdateSpec> updateBySqlSpec;
	protected List<DeleteSpec> deleteByPKSpec;
	protected List<DeleteSpec> deleteBySqlSpec;
	@XmlAttribute(name = "listOfSon")
	protected String listOfSon;
	@XmlAttribute(name = "refToSon")
	protected String refToSon;
	@XmlAttribute(name = "refToRel")
	protected String refToRel;
	@XmlAttribute(name = "refToRight")
	protected String refToRight;
	@XmlAttribute(name = "fatherAttr")
	protected String fatherAttr;
	@XmlAttribute(name = "sonAttr")
	protected String sonAttr;
	@XmlAttribute(name = "leftAttr")
	protected String leftAttr;
	@XmlAttribute(name = "rightAttr")
	protected String rightAttr;
	@XmlAttribute(name = "joinType")
	protected String joinType;

	// =============================================================
	public void addSelectSpec(SelectSpec theSelectSpec) {
		selectSpec = theSelectSpec;
	}

	public void addInsertSpec(InsertSpec theInsertSpec) {
		if (insertSpec == null) {
			insertSpec = new ArrayList<InsertSpec>();
		}
		insertSpec.add(theInsertSpec);
	}

	public void addUpdateByPKSpec(UpdateSpec theUpdateByPKSpec) {
		if (updateByPKSpec == null) {
			updateByPKSpec = new ArrayList<UpdateSpec>();
		}
		updateByPKSpec.add(theUpdateByPKSpec);
	}

	public void addUpdateBySqlSpec(UpdateSpec theUpdateBySqlSpec) {
		if (updateBySqlSpec == null) {
			updateBySqlSpec = new ArrayList<UpdateSpec>();
		}
		updateBySqlSpec.add(theUpdateBySqlSpec);
	}

	public void addDeleteByPKSpec(DeleteSpec theDeleteByPKSpec) {
		if (deleteByPKSpec == null) {
			deleteByPKSpec = new ArrayList<DeleteSpec>();
		}
		deleteByPKSpec.add(theDeleteByPKSpec);
	}

	public void addDeleteBySqlSpec(DeleteSpec theDeleteBySqlSpec) {
		if (deleteBySqlSpec == null) {
			deleteBySqlSpec = new ArrayList<DeleteSpec>();
		}
		deleteBySqlSpec.add(theDeleteBySqlSpec);
	}
	// =============================================================

	/**
	 * 获取selectSpec属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link SelectSpec }
	 *     
	 */
	public SelectSpec getSelectSpec() {
		return selectSpec;
	}

	/**
	 * 设置selectSpec属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link SelectSpec }
	 *     
	 */
	public void setSelectSpec(SelectSpec value) {
		this.selectSpec = value;
	}

	/**
	 * Gets the value of the insertSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the insertSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getInsertSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link InsertSpec }
	 * 
	 * 
	 */
	public List<InsertSpec> getInsertSpec() {
		if (insertSpec == null) {
			insertSpec = new ArrayList<InsertSpec>();
		}
		return this.insertSpec;
	}

	/**
	 * Gets the value of the updateByPKSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the updateByPKSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getUpdateByPKSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link UpdateSpec }
	 * 
	 * 
	 */
	public List<UpdateSpec> getUpdateByPKSpec() {
		if (updateByPKSpec == null) {
			updateByPKSpec = new ArrayList<UpdateSpec>();
		}
		return this.updateByPKSpec;
	}

	/**
	 * Gets the value of the updateBySqlSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the updateBySqlSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getUpdateBySqlSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link UpdateSpec }
	 * 
	 * 
	 */
	public List<UpdateSpec> getUpdateBySqlSpec() {
		if (updateBySqlSpec == null) {
			updateBySqlSpec = new ArrayList<UpdateSpec>();
		}
		return this.updateBySqlSpec;
	}

	/**
	 * Gets the value of the deleteByPKSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the deleteByPKSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getDeleteByPKSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link DeleteSpec }
	 * 
	 * 
	 */
	public List<DeleteSpec> getDeleteByPKSpec() {
		if (deleteByPKSpec == null) {
			deleteByPKSpec = new ArrayList<DeleteSpec>();
		}
		return this.deleteByPKSpec;
	}

	/**
	 * Gets the value of the deleteBySqlSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the deleteBySqlSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getDeleteBySqlSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link DeleteSpec }
	 * 
	 * 
	 */
	public List<DeleteSpec> getDeleteBySqlSpec() {
		if (deleteBySqlSpec == null) {
			deleteBySqlSpec = new ArrayList<DeleteSpec>();
		}
		return this.deleteBySqlSpec;
	}

	/**
	 * 获取listOfSon属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getListOfSon() {
		return listOfSon;
	}

	/**
	 * 设置listOfSon属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setListOfSon(String value) {
		this.listOfSon = value;
	}

	/**
	 * 获取refToSon属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getRefToSon() {
		return refToSon;
	}

	/**
	 * 设置refToSon属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setRefToSon(String value) {
		this.refToSon = value;
	}

	/**
	 * 获取refToRel属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getRefToRel() {
		return refToRel;
	}

	/**
	 * 设置refToRel属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setRefToRel(String value) {
		this.refToRel = value;
	}

	/**
	 * 获取refToRight属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getRefToRight() {
		return refToRight;
	}

	/**
	 * 设置refToRight属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setRefToRight(String value) {
		this.refToRight = value;
	}

	/**
	 * 获取fatherAttr属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getFatherAttr() {
		return fatherAttr;
	}

	/**
	 * 设置fatherAttr属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setFatherAttr(String value) {
		this.fatherAttr = value;
	}

	/**
	 * 获取sonAttr属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getSonAttr() {
		return sonAttr;
	}

	/**
	 * 设置sonAttr属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setSonAttr(String value) {
		this.sonAttr = value;
	}

	/**
	 * 获取leftAttr属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getLeftAttr() {
		return leftAttr;
	}

	/**
	 * 设置leftAttr属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setLeftAttr(String value) {
		this.leftAttr = value;
	}

	/**
	 * 获取rightAttr属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getRightAttr() {
		return rightAttr;
	}

	/**
	 * 设置rightAttr属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setRightAttr(String value) {
		this.rightAttr = value;
	}

	/**
	 * 获取joinType属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getJoinType() {
		return joinType;
	}

	/**
	 * 设置joinType属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setJoinType(String value) {
		this.joinType = value;
	}

}
