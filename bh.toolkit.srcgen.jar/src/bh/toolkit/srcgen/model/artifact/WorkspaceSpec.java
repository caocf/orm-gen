//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.11 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2015.12.13 时间 02:41:27 PM CST 
//


package bh.toolkit.srcgen.model.artifact;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>WorkspaceSpec complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="WorkspaceSpec"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="svnWorkspace" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="ideWorkspace" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="commonPrj" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="transObjPrj" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="ormPrj" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="compDescPrj" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="mgrPrj" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="srcPathName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="rsrcPathName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="pkgPrefix" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="msgCdPkgName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="msgPropPkgName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="voPkgName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="dtoPkgName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="mapperPkgName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="daoPkgName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="oprPkgName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="mgrPkgName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="ctrlrPkgName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkspaceSpec")
public class WorkspaceSpec {

    @XmlAttribute(name = "svnWorkspace")
    protected String svnWorkspace;
    @XmlAttribute(name = "ideWorkspace")
    protected String ideWorkspace;
    @XmlAttribute(name = "commonPrj")
    protected String commonPrj;
    @XmlAttribute(name = "transObjPrj")
    protected String transObjPrj;
    @XmlAttribute(name = "ormPrj")
    protected String ormPrj;
    @XmlAttribute(name = "compDescPrj")
    protected String compDescPrj;
    @XmlAttribute(name = "mgrPrj")
    protected String mgrPrj;
    @XmlAttribute(name = "srcPathName")
    protected String srcPathName;
    @XmlAttribute(name = "rsrcPathName")
    protected String rsrcPathName;
    @XmlAttribute(name = "pkgPrefix")
    protected String pkgPrefix;
    @XmlAttribute(name = "msgCdPkgName")
    protected String msgCdPkgName;
    @XmlAttribute(name = "msgPropPkgName")
    protected String msgPropPkgName;
    @XmlAttribute(name = "voPkgName")
    protected String voPkgName;
    @XmlAttribute(name = "dtoPkgName")
    protected String dtoPkgName;
    @XmlAttribute(name = "mapperPkgName")
    protected String mapperPkgName;
    @XmlAttribute(name = "daoPkgName")
    protected String daoPkgName;
    @XmlAttribute(name = "oprPkgName")
    protected String oprPkgName;
    @XmlAttribute(name = "mgrPkgName")
    protected String mgrPkgName;
    @XmlAttribute(name = "ctrlrPkgName")
    protected String ctrlrPkgName;

    /**
     * 获取svnWorkspace属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSvnWorkspace() {
        return svnWorkspace;
    }

    /**
     * 设置svnWorkspace属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSvnWorkspace(String value) {
        this.svnWorkspace = value;
    }

    /**
     * 获取ideWorkspace属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdeWorkspace() {
        return ideWorkspace;
    }

    /**
     * 设置ideWorkspace属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdeWorkspace(String value) {
        this.ideWorkspace = value;
    }

    /**
     * 获取commonPrj属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommonPrj() {
        return commonPrj;
    }

    /**
     * 设置commonPrj属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommonPrj(String value) {
        this.commonPrj = value;
    }

    /**
     * 获取transObjPrj属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransObjPrj() {
        return transObjPrj;
    }

    /**
     * 设置transObjPrj属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransObjPrj(String value) {
        this.transObjPrj = value;
    }

    /**
     * 获取ormPrj属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrmPrj() {
        return ormPrj;
    }

    /**
     * 设置ormPrj属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrmPrj(String value) {
        this.ormPrj = value;
    }

    /**
     * 获取compDescPrj属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompDescPrj() {
        return compDescPrj;
    }

    /**
     * 设置compDescPrj属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompDescPrj(String value) {
        this.compDescPrj = value;
    }

    /**
     * 获取mgrPrj属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMgrPrj() {
        return mgrPrj;
    }

    /**
     * 设置mgrPrj属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMgrPrj(String value) {
        this.mgrPrj = value;
    }

    /**
     * 获取srcPathName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrcPathName() {
        return srcPathName;
    }

    /**
     * 设置srcPathName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrcPathName(String value) {
        this.srcPathName = value;
    }

    /**
     * 获取rsrcPathName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRsrcPathName() {
        return rsrcPathName;
    }

    /**
     * 设置rsrcPathName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRsrcPathName(String value) {
        this.rsrcPathName = value;
    }

    /**
     * 获取pkgPrefix属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPkgPrefix() {
        return pkgPrefix;
    }

    /**
     * 设置pkgPrefix属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPkgPrefix(String value) {
        this.pkgPrefix = value;
    }

    /**
     * 获取msgCdPkgName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgCdPkgName() {
        return msgCdPkgName;
    }

    /**
     * 设置msgCdPkgName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgCdPkgName(String value) {
        this.msgCdPkgName = value;
    }

    /**
     * 获取msgPropPkgName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgPropPkgName() {
        return msgPropPkgName;
    }

    /**
     * 设置msgPropPkgName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgPropPkgName(String value) {
        this.msgPropPkgName = value;
    }

    /**
     * 获取voPkgName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVoPkgName() {
        return voPkgName;
    }

    /**
     * 设置voPkgName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVoPkgName(String value) {
        this.voPkgName = value;
    }

    /**
     * 获取dtoPkgName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDtoPkgName() {
        return dtoPkgName;
    }

    /**
     * 设置dtoPkgName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDtoPkgName(String value) {
        this.dtoPkgName = value;
    }

    /**
     * 获取mapperPkgName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMapperPkgName() {
        return mapperPkgName;
    }

    /**
     * 设置mapperPkgName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMapperPkgName(String value) {
        this.mapperPkgName = value;
    }

    /**
     * 获取daoPkgName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDaoPkgName() {
        return daoPkgName;
    }

    /**
     * 设置daoPkgName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDaoPkgName(String value) {
        this.daoPkgName = value;
    }

    /**
     * 获取oprPkgName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOprPkgName() {
        return oprPkgName;
    }

    /**
     * 设置oprPkgName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOprPkgName(String value) {
        this.oprPkgName = value;
    }

    /**
     * 获取mgrPkgName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMgrPkgName() {
        return mgrPkgName;
    }

    /**
     * 设置mgrPkgName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMgrPkgName(String value) {
        this.mgrPkgName = value;
    }

    /**
     * 获取ctrlrPkgName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtrlrPkgName() {
        return ctrlrPkgName;
    }

    /**
     * 设置ctrlrPkgName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtrlrPkgName(String value) {
        this.ctrlrPkgName = value;
    }

}
