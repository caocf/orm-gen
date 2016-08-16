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
 * Dao说明
 * 
 * <p>DaoSpec complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="DaoSpec"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://srcgen.toolkit.bh/model/artifact}ClassSpec"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="insertSpec" type="{http://srcgen.toolkit.bh/model/artifact}InsertSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="selectSpec" type="{http://srcgen.toolkit.bh/model/artifact}SelectSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="updateByPKSpec" type="{http://srcgen.toolkit.bh/model/artifact}UpdateSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="updateBySqlSpec" type="{http://srcgen.toolkit.bh/model/artifact}UpdateSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="deleteByPKSpec" type="{http://srcgen.toolkit.bh/model/artifact}DeleteSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="deleteBySqlSpec" type="{http://srcgen.toolkit.bh/model/artifact}DeleteSpec" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DaoSpec", propOrder = { "insertSpec", "selectSpec", "updateByPKSpec", "updateBySqlSpec", "deleteByPKSpec", "deleteBySqlSpec" })
public class DaoSpec extends ClassSpec {

	protected List<InsertSpec> insertSpec;
	protected List<SelectSpec> selectSpec;
	protected List<UpdateSpec> updateByPKSpec;
	protected List<UpdateSpec> updateBySqlSpec;
	protected List<DeleteSpec> deleteByPKSpec;
	protected List<DeleteSpec> deleteBySqlSpec;

	// =============================================================
	public void addSelectSpec(SelectSpec theSelectSpec) {
		if (selectSpec == null) {
			selectSpec = new ArrayList<SelectSpec>();
		}
		selectSpec.add(theSelectSpec);
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
	 * Gets the value of the selectSpec property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the selectSpec property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getSelectSpec().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link SelectSpec }
	 * 
	 * 
	 */
	public List<SelectSpec> getSelectSpec() {
		if (selectSpec == null) {
			selectSpec = new ArrayList<SelectSpec>();
		}
		return this.selectSpec;
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

}
