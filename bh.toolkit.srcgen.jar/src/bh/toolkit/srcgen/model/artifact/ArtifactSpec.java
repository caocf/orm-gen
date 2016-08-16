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
 * <p>ArtifactSpec complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArtifactSpec"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="commonAttrSpec" type="{http://srcgen.toolkit.bh/model/artifact}CommonAttrSpec"/&gt;
 *         &lt;element name="voSpec" type="{http://srcgen.toolkit.bh/model/artifact}VoSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="daoSpec" type="{http://srcgen.toolkit.bh/model/artifact}DaoSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="oprSpec" type="{http://srcgen.toolkit.bh/model/artifact}OprSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="trxSpec" type="{http://srcgen.toolkit.bh/model/artifact}TrxSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ctrlrSpec" type="{http://srcgen.toolkit.bh/model/artifact}CtrlrSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArtifactSpec", propOrder = { "commonAttrSpec", "voSpec", "daoSpec", "oprSpec", "trxSpec", "ctrlrSpec" })
public class ArtifactSpec {

	@XmlElement(required = true)
	protected CommonAttrSpec commonAttrSpec;
	protected List<VoSpec> voSpec;
	protected List<DaoSpec> daoSpec;
	protected List<OprSpec> oprSpec;
	protected List<TrxSpec> trxSpec;
	protected List<CtrlrSpec> ctrlrSpec;

	// =============================================================
	public void addCommonAttrSpec(CommonAttrSpec theCommonAttrSpec) {
		commonAttrSpec = theCommonAttrSpec;
	}

	public void addVoSpec(VoSpec theVoSpec) {
		if (voSpec == null) {
			voSpec = new ArrayList<VoSpec>();
		}
		voSpec.add(theVoSpec);
	}

	public void addDaoSpec(DaoSpec theDaoSpec) {
		if (daoSpec == null) {
			daoSpec = new ArrayList<DaoSpec>();
		}
		daoSpec.add(theDaoSpec);
	}

	public void addOprSpec(OprSpec theOprSpec) {
		if (oprSpec == null) {
			oprSpec = new ArrayList<OprSpec>();
		}
		oprSpec.add(theOprSpec);
	}

	public void addTrxSpec(TrxSpec theTrxSpec) {
		if (trxSpec == null) {
			trxSpec = new ArrayList<TrxSpec>();
		}
		trxSpec.add(theTrxSpec);
	}

	public void addCtrlrSpec(CtrlrSpec theCtrlrSpec) {
		if (ctrlrSpec == null) {
			ctrlrSpec = new ArrayList<CtrlrSpec>();
		}
		ctrlrSpec.add(theCtrlrSpec);
	}
	// =============================================================

	/**
	 * 获取commonAttrSpec属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link CommonAttrSpec }
	 *     
	 */
	public CommonAttrSpec getCommonAttrSpec() {
		return commonAttrSpec;
	}

	/**
	 * 设置commonAttrSpec属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link CommonAttrSpec }
	 *     
	 */
	public void setCommonAttrSpec(CommonAttrSpec value) {
		this.commonAttrSpec = value;
	}

	/**
	 * Gets the value of the voSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the voSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getVoSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link VoSpec }
	 * 
	 * 
	 */
	public List<VoSpec> getVoSpec() {
		if (voSpec == null) {
			voSpec = new ArrayList<VoSpec>();
		}
		return this.voSpec;
	}

	/**
	 * Gets the value of the daoSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the daoSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getDaoSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link DaoSpec }
	 * 
	 * 
	 */
	public List<DaoSpec> getDaoSpec() {
		if (daoSpec == null) {
			daoSpec = new ArrayList<DaoSpec>();
		}
		return this.daoSpec;
	}

	/**
	 * Gets the value of the oprSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the oprSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getOprSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OprSpec }
	 * 
	 * 
	 */
	public List<OprSpec> getOprSpec() {
		if (oprSpec == null) {
			oprSpec = new ArrayList<OprSpec>();
		}
		return this.oprSpec;
	}

	/**
	 * Gets the value of the trxSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the trxSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getTrxSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link TrxSpec }
	 * 
	 * 
	 */
	public List<TrxSpec> getTrxSpec() {
		if (trxSpec == null) {
			trxSpec = new ArrayList<TrxSpec>();
		}
		return this.trxSpec;
	}

	/**
	 * Gets the value of the ctrlrSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the ctrlrSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getCtrlrSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link CtrlrSpec }
	 * 
	 * 
	 */
	public List<CtrlrSpec> getCtrlrSpec() {
		if (ctrlrSpec == null) {
			ctrlrSpec = new ArrayList<CtrlrSpec>();
		}
		return this.ctrlrSpec;
	}

}
