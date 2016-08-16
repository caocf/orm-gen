//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.11 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2015.12.12 时间 08:37:42 PM CST 
//


package bh.toolkit.srcgen.model.artifact;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the bh.toolkit.srcgen.model.artifact package. 
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

    private final static QName _ArtifactSpec_QNAME = new QName("http://srcgen.toolkit.bh/model/artifact", "artifactSpec");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: bh.toolkit.srcgen.model.artifact
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ArtifactSpec }
     * 
     */
    public ArtifactSpec createArtifactSpec() {
        return new ArtifactSpec();
    }

    /**
     * Create an instance of {@link DatasourceSpec }
     * 
     */
    public DatasourceSpec createDatasourceSpec() {
        return new DatasourceSpec();
    }

    /**
     * Create an instance of {@link WorkspaceSpec }
     * 
     */
    public WorkspaceSpec createWorkspaceSpec() {
        return new WorkspaceSpec();
    }

    /**
     * Create an instance of {@link AttributeSpec }
     * 
     */
    public AttributeSpec createAttributeSpec() {
        return new AttributeSpec();
    }

    /**
     * Create an instance of {@link ResultFilterSpec }
     * 
     */
    public ResultFilterSpec createResultFilterSpec() {
        return new ResultFilterSpec();
    }

    /**
     * Create an instance of {@link DaoSpec }
     * 
     */
    public DaoSpec createDaoSpec() {
        return new DaoSpec();
    }

    /**
     * Create an instance of {@link OprSpec }
     * 
     */
    public OprSpec createOprSpec() {
        return new OprSpec();
    }

    /**
     * Create an instance of {@link VoSpec }
     * 
     */
    public VoSpec createVoSpec() {
        return new VoSpec();
    }

    /**
     * Create an instance of {@link ClassSpec }
     * 
     */
    public ClassSpec createClassSpec() {
        return new ClassSpec();
    }

    /**
     * Create an instance of {@link CommonAttrSpec }
     * 
     */
    public CommonAttrSpec createCommonAttrSpec() {
        return new CommonAttrSpec();
    }

    /**
     * Create an instance of {@link TrxSpec }
     * 
     */
    public TrxSpec createTrxSpec() {
        return new TrxSpec();
    }

    /**
     * Create an instance of {@link CtxSpec }
     * 
     */
    public CtxSpec createCtxSpec() {
        return new CtxSpec();
    }

    /**
     * Create an instance of {@link DBTableSpec }
     * 
     */
    public DBTableSpec createDBTableSpec() {
        return new DBTableSpec();
    }

    /**
     * Create an instance of {@link RelationshipSpec }
     * 
     */
    public RelationshipSpec createRelationshipSpec() {
        return new RelationshipSpec();
    }

    /**
     * Create an instance of {@link SelectSpec }
     * 
     */
    public SelectSpec createSelectSpec() {
        return new SelectSpec();
    }

    /**
     * Create an instance of {@link InsertSpec }
     * 
     */
    public InsertSpec createInsertSpec() {
        return new InsertSpec();
    }

    /**
     * Create an instance of {@link UpdateSpec }
     * 
     */
    public UpdateSpec createUpdateSpec() {
        return new UpdateSpec();
    }

    /**
     * Create an instance of {@link MethodSpec }
     * 
     */
    public MethodSpec createMethodSpec() {
        return new MethodSpec();
    }

    /**
     * Create an instance of {@link DeleteSpec }
     * 
     */
    public DeleteSpec createDeleteSpec() {
        return new DeleteSpec();
    }

    /**
     * Create an instance of {@link OprMethodSpec }
     * 
     */
    public OprMethodSpec createOprMethodSpec() {
        return new OprMethodSpec();
    }

    /**
     * Create an instance of {@link MgrSpec }
     * 
     */
    public MgrSpec createMgrSpec() {
        return new MgrSpec();
    }

    /**
     * Create an instance of {@link CaseFilterSpec }
     * 
     */
    public CaseFilterSpec createCaseFilterSpec() {
        return new CaseFilterSpec();
    }

    /**
     * Create an instance of {@link MgrMethodSpec }
     * 
     */
    public MgrMethodSpec createMgrMethodSpec() {
        return new MgrMethodSpec();
    }

    /**
     * Create an instance of {@link CtrlrSpec }
     * 
     */
    public CtrlrSpec createCtrlrSpec() {
        return new CtrlrSpec();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArtifactSpec }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srcgen.toolkit.bh/model/artifact", name = "artifactSpec")
    public JAXBElement<ArtifactSpec> createArtifactSpec(ArtifactSpec value) {
        return new JAXBElement<ArtifactSpec>(_ArtifactSpec_QNAME, ArtifactSpec.class, null, value);
    }

}
