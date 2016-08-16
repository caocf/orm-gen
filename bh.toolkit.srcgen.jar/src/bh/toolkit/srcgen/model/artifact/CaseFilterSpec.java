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
 * <p>CaseFilterSpec complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="CaseFilterSpec"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="resultFilter" type="{http://srcgen.toolkit.bh/model/artifact}ResultFilterSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="test" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CaseFilterSpec", propOrder = { "resultFilter" })
public class CaseFilterSpec {

	protected List<ResultFilterSpec> resultFilter;
	@XmlAttribute(name = "test")
	protected String test;

	// =============================================================
	public void addResultFilter(ResultFilterSpec theResultFilter) {
		if (resultFilter == null) {
			resultFilter = new ArrayList<ResultFilterSpec>();
		}
		resultFilter.add(theResultFilter);
	}
	// =============================================================

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
	 * 获取test属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getTest() {
		return test;
	}

	/**
	 * 设置test属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setTest(String value) {
		this.test = value;
	}

}
