//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.11 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2016.01.06 时间 07:42:57 PM CST 
//

package bh.toolkit.srcgen.model.artifact;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>MgrSpec complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="MgrSpec"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://srcgen.toolkit.bh/model/artifact}ClassSpec"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="mgrSpec" type="{http://srcgen.toolkit.bh/model/artifact}MgrSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="oprSpec" type="{http://srcgen.toolkit.bh/model/artifact}OprSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="daoSpec" type="{http://srcgen.toolkit.bh/model/artifact}DaoSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="svcMethodSpec" type="{http://srcgen.toolkit.bh/model/artifact}MgrMethodSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MgrSpec", propOrder = { "mgrSpec", "oprSpec", "daoSpec", "svcMethodSpec" })
public class MgrSpec extends ClassSpec {

	protected List<MgrSpec> mgrSpec;
	protected List<OprSpec> oprSpec;
	protected List<DaoSpec> daoSpec;
	protected List<MgrMethodSpec> svcMethodSpec;

	// =============================================================
	public void addMgrSpec(MgrSpec theMgrSpec) {
		if (mgrSpec == null) {
			mgrSpec = new ArrayList<MgrSpec>();
		}
		mgrSpec.add(theMgrSpec);
	}

	public void addOprSpec(OprSpec theOprSpec) {
		if (oprSpec == null) {
			oprSpec = new ArrayList<OprSpec>();
		}
		oprSpec.add(theOprSpec);
	}

	public void addDaoSpec(DaoSpec theDaoSpec) {
		if (daoSpec == null) {
			daoSpec = new ArrayList<DaoSpec>();
		}
		daoSpec.add(theDaoSpec);
	}

	public void addSvcMethodSpec(MgrMethodSpec theSvcMethodSpec) {
		if (svcMethodSpec == null) {
			svcMethodSpec = new ArrayList<MgrMethodSpec>();
		}
		svcMethodSpec.add(theSvcMethodSpec);
	}
	// =============================================================

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
	 * Gets the value of the svcMethodSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the svcMethodSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getSvcMethodSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link MgrMethodSpec }
	 * 
	 * 
	 */
	public List<MgrMethodSpec> getSvcMethodSpec() {
		if (svcMethodSpec == null) {
			svcMethodSpec = new ArrayList<MgrMethodSpec>();
		}
		return this.svcMethodSpec;
	}

}
