//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.11 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2015.12.12 时间 08:37:42 PM CST 
//

package bh.toolkit.srcgen.model.artifact;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>CommonAttrSpec complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="CommonAttrSpec"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="datasourceSpec" type="{http://srcgen.toolkit.bh/model/artifact}DatasourceSpec"/&gt;
 *         &lt;element name="workspaceSpec" type="{http://srcgen.toolkit.bh/model/artifact}WorkspaceSpec"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommonAttrSpec", propOrder = { "datasourceSpec", "workspaceSpec" })
public class CommonAttrSpec {

	@XmlElement(required = true)
	protected DatasourceSpec datasourceSpec;
	@XmlElement(required = true)
	protected WorkspaceSpec workspaceSpec;

	// =============================================================
	public void addDatasourceSpec(DatasourceSpec theDatasourceSpec) {
		datasourceSpec = theDatasourceSpec;
	}

	public void addWorkspaceSpec(WorkspaceSpec theWorkspaceSpec) {
		workspaceSpec = theWorkspaceSpec;
	}
	// =============================================================

	/**
	 * 获取datasourceSpec属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link DatasourceSpec }
	 *     
	 */
	public DatasourceSpec getDatasourceSpec() {
		return datasourceSpec;
	}

	/**
	 * 设置datasourceSpec属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link DatasourceSpec }
	 *     
	 */
	public void setDatasourceSpec(DatasourceSpec value) {
		this.datasourceSpec = value;
	}

	/**
	 * 获取workspaceSpec属性的值。
	 * 
	 * @return
	 *     possible object is
	 *     {@link WorkspaceSpec }
	 *     
	 */
	public WorkspaceSpec getWorkspaceSpec() {
		return workspaceSpec;
	}

	/**
	 * 设置workspaceSpec属性的值。
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link WorkspaceSpec }
	 *     
	 */
	public void setWorkspaceSpec(WorkspaceSpec value) {
		this.workspaceSpec = value;
	}

}
