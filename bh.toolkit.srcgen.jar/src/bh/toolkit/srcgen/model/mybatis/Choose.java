//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.18 at 09:24:53 PM CST 
//


package bh.toolkit.srcgen.model.mybatis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Choose complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Choose">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="when" type="{http://slp/toolkit/ormgen/model/mybatis}When" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="otherwise" type="{http://slp/toolkit/ormgen/model/mybatis}Otherwise" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Choose", propOrder = {
    "when",
    "otherwise"
})
public class Choose {

    protected List<When> when;
    protected Otherwise otherwise;

    /**
     * Gets the value of the when property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the when property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWhen().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link When }
     * 
     * 
     */
    public List<When> getWhen() {
        if (when == null) {
            when = new ArrayList<When>();
        }
        return this.when;
    }

    /**
     * Gets the value of the otherwise property.
     * 
     * @return
     *     possible object is
     *     {@link Otherwise }
     *     
     */
    public Otherwise getOtherwise() {
        return otherwise;
    }

    /**
     * Sets the value of the otherwise property.
     * 
     * @param value
     *     allowed object is
     *     {@link Otherwise }
     *     
     */
    public void setOtherwise(Otherwise value) {
        this.otherwise = value;
    }

}
