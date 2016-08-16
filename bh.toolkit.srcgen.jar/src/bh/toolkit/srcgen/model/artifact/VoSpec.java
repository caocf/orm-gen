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
 * Vo说明
 * 
 * <p>VoSpec complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="VoSpec"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://srcgen.toolkit.bh/model/artifact}ClassSpec"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dbTableSpec" type="{http://srcgen.toolkit.bh/model/artifact}DBTableSpec"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VoSpec", propOrder = {
    "dbTableSpec"
})
public class VoSpec
    extends ClassSpec
{

    @XmlElement(required = true)
    protected DBTableSpec dbTableSpec;

    /**
     * 获取dbTableSpec属性的值。
     * 
     * @return
     *     possible object is
     *     {@link DBTableSpec }
     *     
     */
    public DBTableSpec getDbTableSpec() {
        return dbTableSpec;
    }

    /**
     * 设置dbTableSpec属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link DBTableSpec }
     *     
     */
    public void setDbTableSpec(DBTableSpec value) {
        this.dbTableSpec = value;
    }

}
