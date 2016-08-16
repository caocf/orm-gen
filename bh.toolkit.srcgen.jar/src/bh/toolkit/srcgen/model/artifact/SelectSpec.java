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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>SelectSpec complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="SelectSpec"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://srcgen.toolkit.bh/model/artifact}MethodSpec"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="includedAttr" type="{http://srcgen.toolkit.bh/model/artifact}AttributeSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="excludedAttr" type="{http://srcgen.toolkit.bh/model/artifact}AttributeSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="resultFilter" type="{http://srcgen.toolkit.bh/model/artifact}ResultFilterSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="caseFilter" type="{http://srcgen.toolkit.bh/model/artifact}CaseFilterSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="selectPrefix" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="defaultOrderBy" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SelectSpec", propOrder = { "includedAttr", "excludedAttr", "resultFilter", "caseFilter" })
public class SelectSpec extends MethodSpec {

	protected List<AttributeSpec> includedAttr;
	protected List<AttributeSpec> excludedAttr;
	protected List<ResultFilterSpec> resultFilter;
	protected List<CaseFilterSpec> caseFilter;
	@XmlAttribute(name = "selectPrefix")
	protected String selectPrefix;
	@XmlAttribute(name = "defaultOrderBy")
	protected String defaultOrderBy;

	// =============================================================
	public void addIncludedAttr(AttributeSpec theIncludedAttr) {
		if (includedAttr == null) {
			includedAttr = new ArrayList<AttributeSpec>();
		}
		includedAttr.add(theIncludedAttr);
	}

	public void addExcludedAttr(AttributeSpec theExcludedAttr) {
		if (excludedAttr == null) {
			excludedAttr = new ArrayList<AttributeSpec>();
		}
		excludedAttr.add(theExcludedAttr);
	}

	public void addResultFilter(ResultFilterSpec theResultFilter) {
		if (resultFilter == null) {
			resultFilter = new ArrayList<ResultFilterSpec>();
		}
		resultFilter.add(theResultFilter);
	}

	public void addCaseFilter(CaseFilterSpec theCaseFilter) {
		if (caseFilter == null) {
			caseFilter = new ArrayList<CaseFilterSpec>();
		}
		caseFilter.add(theCaseFilter);
	}

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
	// =============================================================

	/**
	 * Gets the value of the includedAttr property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the includedAttr property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getIncludedAttr().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link AttributeSpec }
	 * 
	 * 
	 */
	public List<AttributeSpec> getIncludedAttr() {
		if (includedAttr == null) {
			includedAttr = new ArrayList<AttributeSpec>();
		}
		return this.includedAttr;
	}

	/**
	 * Gets the value of the excludedAttr property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the excludedAttr property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getExcludedAttr().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link AttributeSpec }
	 * 
	 * 
	 */
	public List<AttributeSpec> getExcludedAttr() {
		if (excludedAttr == null) {
			excludedAttr = new ArrayList<AttributeSpec>();
		}
		return this.excludedAttr;
	}

	/**
	 * Gets the value of the resultFilter property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the resultFilter property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getResultFilter().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ResultFilterSpec }
	 * 
	 * 
	 */
	public List<ResultFilterSpec> getResultFilter() {
		if (resultFilter == null) {
			resultFilter = new ArrayList<ResultFilterSpec>();
		}
		return this.resultFilter;
	}

	/**
	 * Gets the value of the caseFilter property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the caseFilter property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getCaseFilter().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link CaseFilterSpec }
	 * 
	 * 
	 */
	public List<CaseFilterSpec> getCaseFilter() {
		if (caseFilter == null) {
			caseFilter = new ArrayList<CaseFilterSpec>();
		}
		return this.caseFilter;
	}

	/**
	 * 获取selectPrefix属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getSelectPrefix() {
		return selectPrefix;
	}

	/**
	 * 设置selectPrefix属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setSelectPrefix(String value) {
		this.selectPrefix = value;
	}

	/**
	 * 获取defaultOrderBy属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getDefaultOrderBy() {
		return defaultOrderBy;
	}

	/**
	 * 设置defaultOrderBy属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setDefaultOrderBy(String value) {
		this.defaultOrderBy = value;
	}

}
