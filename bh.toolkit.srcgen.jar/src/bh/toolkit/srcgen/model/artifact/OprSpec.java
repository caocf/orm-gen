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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>OprSpec complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="OprSpec"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://srcgen.toolkit.bh/model/artifact}ClassSpec"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="addSpec" type="{http://srcgen.toolkit.bh/model/artifact}OprMethodSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="changeSpec" type="{http://srcgen.toolkit.bh/model/artifact}OprMethodSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="removeSpec" type="{http://srcgen.toolkit.bh/model/artifact}OprMethodSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="deleteSpec" type="{http://srcgen.toolkit.bh/model/artifact}OprMethodSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OprSpec", propOrder = { "addSpec", "changeSpec", "removeSpec", "deleteSpec" })
public class OprSpec extends ClassSpec {

	protected List<OprMethodSpec> addSpec;
	protected List<OprMethodSpec> changeSpec;
	protected List<OprMethodSpec> removeSpec;
	protected List<OprMethodSpec> deleteSpec;

	// =============================================================
	public void addAddSpec(OprMethodSpec theAddSpec) {
		if (addSpec == null) {
			addSpec = new ArrayList<OprMethodSpec>();
		}
		addSpec.add(theAddSpec);
	}

	public void addChangeSpec(OprMethodSpec theChangeSpec) {
		if (changeSpec == null) {
			changeSpec = new ArrayList<OprMethodSpec>();
		}
		changeSpec.add(theChangeSpec);
	}

	public void addRemoveSpec(OprMethodSpec theRemoveSpec) {
		if (removeSpec == null) {
			removeSpec = new ArrayList<OprMethodSpec>();
		}
		removeSpec.add(theRemoveSpec);
	}

	public void addDeleteSpec(OprMethodSpec theDeleteSpec) {
		if (deleteSpec == null) {
			deleteSpec = new ArrayList<OprMethodSpec>();
		}
		deleteSpec.add(theDeleteSpec);
	}
	// =============================================================

	/**
	 * Gets the value of the addSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the addSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getAddSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OprMethodSpec }
	 * 
	 * 
	 */
	public List<OprMethodSpec> getAddSpec() {
		if (addSpec == null) {
			addSpec = new ArrayList<OprMethodSpec>();
		}
		return this.addSpec;
	}

	/**
	 * Gets the value of the changeSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the changeSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getChangeSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OprMethodSpec }
	 * 
	 * 
	 */
	public List<OprMethodSpec> getChangeSpec() {
		if (changeSpec == null) {
			changeSpec = new ArrayList<OprMethodSpec>();
		}
		return this.changeSpec;
	}

	/**
	 * Gets the value of the removeSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the removeSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getRemoveSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OprMethodSpec }
	 * 
	 * 
	 */
	public List<OprMethodSpec> getRemoveSpec() {
		if (removeSpec == null) {
			removeSpec = new ArrayList<OprMethodSpec>();
		}
		return this.removeSpec;
	}

	/**
	 * Gets the value of the deleteSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the deleteSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getDeleteSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OprMethodSpec }
	 * 
	 * 
	 */
	public List<OprMethodSpec> getDeleteSpec() {
		if (deleteSpec == null) {
			deleteSpec = new ArrayList<OprMethodSpec>();
		}
		return this.deleteSpec;
	}

}
