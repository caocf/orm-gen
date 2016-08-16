//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.11 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2016.05.25 时间 09:53:48 PM CST 
//

package bh.toolkit.srcgen.model.artifact;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * MethodSpec complex type的 Java 类。
 * 
 * <p>
 * 以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="MethodSpec"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="oneToOne" type="{http://srcgen.toolkit.bh/model/artifact}RelationshipSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="oneToMany" type="{http://srcgen.toolkit.bh/model/artifact}RelationshipSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="manyToMany" type="{http://srcgen.toolkit.bh/model/artifact}RelationshipSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="verb" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="subject" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="tableName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="tableAlias" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="paramDataType" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="insertExpr" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="updateExpr" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="returnDataType" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="expCode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="desc" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="skipVerb" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MethodSpec", propOrder = { "oneToOne", "oneToMany", "manyToMany" })
@XmlSeeAlso({ SelectSpec.class, InsertSpec.class, UpdateSpec.class, DeleteSpec.class, OprMethodSpec.class, MgrMethodSpec.class })
public class MethodSpec {

	protected List<RelationshipSpec> oneToOne;
	protected List<RelationshipSpec> oneToMany;
	protected List<RelationshipSpec> manyToMany;
	@XmlAttribute(name = "verb")
	protected String verb;
	@XmlAttribute(name = "subject")
	protected String subject;
	@XmlAttribute(name = "tableName")
	protected String tableName;
	@XmlAttribute(name = "tableAlias")
	protected String tableAlias;
	@XmlAttribute(name = "paramDataType")
	protected String paramDataType;
	@XmlAttribute(name = "insertExpr")
	protected String insertExpr;
	@XmlAttribute(name = "updateExpr")
	protected String updateExpr;
	@XmlAttribute(name = "returnDataType")
	protected String returnDataType;
	@XmlAttribute(name = "expCode")
	protected String expCode;
	@XmlAttribute(name = "desc")
	protected String desc;
	@XmlAttribute(name = "skipVerb")
	protected String skipVerb;

	// =============================================================
	public void addOneToOne(RelationshipSpec theOneToOne) {
		if (oneToOne == null) {
			oneToOne = new ArrayList<RelationshipSpec>();
		}
		oneToOne.add(theOneToOne);
	}

	public void addOneToMany(RelationshipSpec theOneToMany) {
		if (oneToMany == null) {
			oneToMany = new ArrayList<RelationshipSpec>();
		}
		oneToMany.add(theOneToMany);
	}

	public void addManyToMany(RelationshipSpec theManyToMany) {
		if (manyToMany == null) {
			manyToMany = new ArrayList<RelationshipSpec>();
		}
		manyToMany.add(theManyToMany);
	}
	// =============================================================

	/**
	 * Gets the value of the oneToOne property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the oneToOne property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getOneToOne().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link RelationshipSpec }
	 * 
	 * 
	 */
	public List<RelationshipSpec> getOneToOne() {
		if (oneToOne == null) {
			oneToOne = new ArrayList<RelationshipSpec>();
		}
		return this.oneToOne;
	}

	/**
	 * Gets the value of the oneToMany property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the oneToMany property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getOneToMany().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link RelationshipSpec }
	 * 
	 * 
	 */
	public List<RelationshipSpec> getOneToMany() {
		if (oneToMany == null) {
			oneToMany = new ArrayList<RelationshipSpec>();
		}
		return this.oneToMany;
	}

	/**
	 * Gets the value of the manyToMany property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the manyToMany property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getManyToMany().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link RelationshipSpec }
	 * 
	 * 
	 */
	public List<RelationshipSpec> getManyToMany() {
		if (manyToMany == null) {
			manyToMany = new ArrayList<RelationshipSpec>();
		}
		return this.manyToMany;
	}

	/**
	 * 获取verb属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVerb() {
		return verb;
	}

	/**
	 * 设置verb属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVerb(String value) {
		this.verb = value;
	}

	/**
	 * 获取subject属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * 设置subject属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSubject(String value) {
		this.subject = value;
	}

	/**
	 * 获取tableName属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置tableName属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTableName(String value) {
		this.tableName = value;
	}

	/**
	 * 获取tableAlias属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTableAlias() {
		return tableAlias;
	}

	/**
	 * 设置tableAlias属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTableAlias(String value) {
		this.tableAlias = value;
	}

	/**
	 * 获取paramDataType属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getParamDataType() {
		return paramDataType;
	}

	/**
	 * 设置paramDataType属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setParamDataType(String value) {
		this.paramDataType = value;
	}

	/**
	 * 获取insertExpr属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInsertExpr() {
		return insertExpr;
	}

	/**
	 * 设置insertExpr属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setInsertExpr(String value) {
		this.insertExpr = value;
	}

	/**
	 * 获取updateExpr属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUpdateExpr() {
		return updateExpr;
	}

	/**
	 * 设置updateExpr属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUpdateExpr(String value) {
		this.updateExpr = value;
	}

	/**
	 * 获取returnDataType属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getReturnDataType() {
		return returnDataType;
	}

	/**
	 * 设置returnDataType属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setReturnDataType(String value) {
		this.returnDataType = value;
	}

	/**
	 * 获取expCode属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getExpCode() {
		return expCode;
	}

	/**
	 * 设置expCode属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setExpCode(String value) {
		this.expCode = value;
	}

	/**
	 * 获取desc属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * 设置desc属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDesc(String value) {
		this.desc = value;
	}

	/**
	 * 获取skipVerb属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSkipVerb() {
		return skipVerb;
	}

	/**
	 * 设置skipVerb属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSkipVerb(String value) {
		this.skipVerb = value;
	}

}
