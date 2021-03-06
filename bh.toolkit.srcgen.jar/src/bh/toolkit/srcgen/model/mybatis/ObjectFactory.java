//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.18 at 09:24:53 PM CST 
//


package bh.toolkit.srcgen.model.mybatis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the slp.toolkit.ormgen.model.mybatis package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Mapper_QNAME = new QName("http://slp/toolkit/ormgen/model/mybatis", "mapper");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: slp.toolkit.ormgen.model.mybatis
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Mapper }
     * 
     */
    public Mapper createMapper() {
        return new Mapper();
    }

    /**
     * Create an instance of {@link Arg }
     * 
     */
    public Arg createArg() {
        return new Arg();
    }

    /**
     * Create an instance of {@link Insert }
     * 
     */
    public Insert createInsert() {
        return new Insert();
    }

    /**
     * Create an instance of {@link CacheRef }
     * 
     */
    public CacheRef createCacheRef() {
        return new CacheRef();
    }

    /**
     * Create an instance of {@link Association }
     * 
     */
    public Association createAssociation() {
        return new Association();
    }

    /**
     * Create an instance of {@link Update }
     * 
     */
    public Update createUpdate() {
        return new Update();
    }

    /**
     * Create an instance of {@link Trim }
     * 
     */
    public Trim createTrim() {
        return new Trim();
    }

    /**
     * Create an instance of {@link SelectKey }
     * 
     */
    public SelectKey createSelectKey() {
        return new SelectKey();
    }

    /**
     * Create an instance of {@link Otherwise }
     * 
     */
    public Otherwise createOtherwise() {
        return new Otherwise();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link Bind }
     * 
     */
    public Bind createBind() {
        return new Bind();
    }

    /**
     * Create an instance of {@link Choose }
     * 
     */
    public Choose createChoose() {
        return new Choose();
    }

    /**
     * Create an instance of {@link Where }
     * 
     */
    public Where createWhere() {
        return new Where();
    }

    /**
     * Create an instance of {@link ResultMap }
     * 
     */
    public ResultMap createResultMap() {
        return new ResultMap();
    }

    /**
     * Create an instance of {@link Case }
     * 
     */
    public Case createCase() {
        return new Case();
    }

    /**
     * Create an instance of {@link Foreach }
     * 
     */
    public Foreach createForeach() {
        return new Foreach();
    }

    /**
     * Create an instance of {@link Delete }
     * 
     */
    public Delete createDelete() {
        return new Delete();
    }

    /**
     * Create an instance of {@link Include }
     * 
     */
    public Include createInclude() {
        return new Include();
    }

    /**
     * Create an instance of {@link Set }
     * 
     */
    public Set createSet() {
        return new Set();
    }

    /**
     * Create an instance of {@link Parameter }
     * 
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link TypeAlias }
     * 
     */
    public TypeAlias createTypeAlias() {
        return new TypeAlias();
    }

    /**
     * Create an instance of {@link Collection }
     * 
     */
    public Collection createCollection() {
        return new Collection();
    }

    /**
     * Create an instance of {@link Result }
     * 
     */
    public Result createResult() {
        return new Result();
    }

    /**
     * Create an instance of {@link Cache }
     * 
     */
    public Cache createCache() {
        return new Cache();
    }

    /**
     * Create an instance of {@link Sql }
     * 
     */
    public Sql createSql() {
        return new Sql();
    }

    /**
     * Create an instance of {@link When }
     * 
     */
    public When createWhen() {
        return new When();
    }

    /**
     * Create an instance of {@link Select }
     * 
     */
    public Select createSelect() {
        return new Select();
    }

    /**
     * Create an instance of {@link Discriminator }
     * 
     */
    public Discriminator createDiscriminator() {
        return new Discriminator();
    }

    /**
     * Create an instance of {@link ParameterMap }
     * 
     */
    public ParameterMap createParameterMap() {
        return new ParameterMap();
    }

    /**
     * Create an instance of {@link Id }
     * 
     */
    public Id createId() {
        return new Id();
    }

    /**
     * Create an instance of {@link Constructor }
     * 
     */
    public Constructor createConstructor() {
        return new Constructor();
    }

    /**
     * Create an instance of {@link IdArg }
     * 
     */
    public IdArg createIdArg() {
        return new IdArg();
    }

    /**
     * Create an instance of {@link If }
     * 
     */
    public If createIf() {
        return new If();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Mapper }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://slp/toolkit/ormgen/model/mybatis", name = "mapper")
    public JAXBElement<Mapper> createMapper(Mapper value) {
        return new JAXBElement<Mapper>(_Mapper_QNAME, Mapper.class, null, value);
    }

}
