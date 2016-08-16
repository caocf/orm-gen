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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>TrxSpec complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="TrxSpec"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ctxSpec" type="{http://srcgen.toolkit.bh/model/artifact}CtxSpec"/&gt;
 *         &lt;element name="mgrSpec" type="{http://srcgen.toolkit.bh/model/artifact}MgrSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrxSpec", propOrder = { "ctxSpec", "mgrSpec" })
public class TrxSpec {

	@XmlElement(required = true)
	protected CtxSpec ctxSpec;
	protected List<MgrSpec> mgrSpec;

	// =============================================================
	public void addCtxSpec(CtxSpec theCtxSpec) {
		ctxSpec = theCtxSpec;
	}

	public void addMgrSpec(MgrSpec theMgrSpec) {
		if (mgrSpec == null) {
			mgrSpec = new ArrayList<MgrSpec>();
		}
		mgrSpec.add(theMgrSpec);
	}
	// =============================================================

	/**
	 * 获取ctxSpec属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link CtxSpec }
	 *     
	 */
	public CtxSpec getCtxSpec() {
		return ctxSpec;
	}

	/**
	 * 设置ctxSpec属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link CtxSpec }
	 *     
	 */
	public void setCtxSpec(CtxSpec value) {
		this.ctxSpec = value;
	}

	/**
	 * Gets the value of the mgrSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the mgrSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getMgrSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link MgrSpec }
	 * 
	 * 
	 */
	public List<MgrSpec> getMgrSpec() {
		if (mgrSpec == null) {
			mgrSpec = new ArrayList<MgrSpec>();
		}
		return this.mgrSpec;
	}

}
