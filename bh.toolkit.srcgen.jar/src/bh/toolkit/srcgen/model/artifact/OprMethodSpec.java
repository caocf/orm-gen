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
 * <p>OprMethodSpec complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="OprMethodSpec"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://srcgen.toolkit.bh/model/artifact}MethodSpec"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="insertSpec" type="{http://srcgen.toolkit.bh/model/artifact}InsertSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="updateSpec" type="{http://srcgen.toolkit.bh/model/artifact}UpdateSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="deleteSpec" type="{http://srcgen.toolkit.bh/model/artifact}DeleteSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OprMethodSpec", propOrder = { "insertSpec", "updateSpec", "deleteSpec" })
public class OprMethodSpec extends MethodSpec {

	protected List<InsertSpec> insertSpec;
	protected List<UpdateSpec> updateSpec;
	protected List<DeleteSpec> deleteSpec;

	// =============================================================
	public void addInsertSpec(InsertSpec theInsertSpec) {
		if (insertSpec == null) {
			insertSpec = new ArrayList<InsertSpec>();
		}
		insertSpec.add(theInsertSpec);
	}

	public void addUpdateSpec(UpdateSpec theUpdateSpec) {
		if (updateSpec == null) {
			updateSpec = new ArrayList<UpdateSpec>();
		}
		updateSpec.add(theUpdateSpec);
	}

	public void addDeleteSpec(DeleteSpec theDeleteSpec) {
		if (deleteSpec == null) {
			deleteSpec = new ArrayList<DeleteSpec>();
		}
		deleteSpec.add(theDeleteSpec);
	}
	// =============================================================

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
	 * Gets the value of the updateSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the updateSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getUpdateSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link UpdateSpec }
	 * 
	 * 
	 */
	public List<UpdateSpec> getUpdateSpec() {
		if (updateSpec == null) {
			updateSpec = new ArrayList<UpdateSpec>();
		}
		return this.updateSpec;
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
	 * {@link DeleteSpec }
	 * 
	 * 
	 */
	public List<DeleteSpec> getDeleteSpec() {
		if (deleteSpec == null) {
			deleteSpec = new ArrayList<DeleteSpec>();
		}
		return this.deleteSpec;
	}

}
