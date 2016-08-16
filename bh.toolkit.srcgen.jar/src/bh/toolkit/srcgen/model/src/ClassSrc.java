package bh.toolkit.srcgen.model.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;
import bh.toolkit.srcgen.util.StringHelper;

public class ClassSrc {

	private Class<Object> javaClass;
	private String pkgName;
	private List<String> importList;
	private HashSet<String> importIdx;
	private String comments;
	private List<String> annotList;
	private String modifier;
	private String simpleName;
	private String fullName;
	private List<String> extendsList;
	private List<String> implementsList;
	private List<AttributeSrc> attrList;
	private Hashtable<String, AttributeSrc> attrIdx;
	private String pkAttr; // Not support composite primary key.
	private List<MethodSrc> methodList;
	private Hashtable<String, MethodSrc> methodIdx;
	private String fileName;
	private boolean existingFile;

	// private StringBuffer importSegment; // the segment of import
	// private StringBuffer attributeSegment; // the segment of attribute
	// private StringBuffer methodSegment; // the segment of method

	public ClassSrc(WorkspaceSpec workspaceSpec, int classType) {
		this.importList = new ArrayList<String>();
		this.importIdx = new HashSet<String>();
		this.annotList = new ArrayList<String>();
		this.extendsList = new ArrayList<String>();
		this.implementsList = new ArrayList<String>();
		this.attrList = new ArrayList<AttributeSrc>();
		this.attrIdx = new Hashtable<String, AttributeSrc>();
		this.methodList = new ArrayList<MethodSrc>();
		this.methodIdx = new Hashtable<String, MethodSrc>();

		if (classType == JavaSrcElm.EXP_TYPE_VO || classType == JavaSrcElm.EXP_TYPE_DTO) {

			// 增加对一些Java包的import声明
			importList.add(JavaSrcElm.IO_SERIALIZABLE_FULL);
			importIdx.add(JavaSrcElm.IO_SERIALIZABLE_FULL);

			// 增加对Log4j包的import声明
			importList.add(JavaSrcElm.LOG4J_LOGGER_FULL);
			importIdx.add(JavaSrcElm.LOG4J_LOGGER_FULL);

			// 增加默认实现接口的声明
			implementsList.add(JavaSrcElm.LANG_CLONEABLE_SIMPLE);
			implementsList.add(JavaSrcElm.IO_SERIALIZABLE_SIMPLE);

		} else if (classType == JavaSrcElm.EXP_TYPE_OPR_INTF || classType == JavaSrcElm.EXP_TYPE_MGR_INTF) {

			// 增加对AppException的引用
			importList.add(workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.APP_EXCEPTION_FULL);

		} else if (classType == JavaSrcElm.EXP_TYPE_OPR_IMPL || classType == JavaSrcElm.EXP_TYPE_MGR_IMPL || classType == JavaSrcElm.EXP_TYPE_CTRL) {

			// 增加对Log4j包的import声明
			importList.add(JavaSrcElm.LOG4J_LOGGER_FULL);
			importIdx.add(JavaSrcElm.LOG4J_LOGGER_FULL);

			// 增加对Spring的包的引用
			importList.add(JavaSrcElm.SPRING_AUTOWIRED);
			importList.add(JavaSrcElm.SPRING_COMPONENT);

			// 增加对AppException的引用
			importList.add(workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.APP_EXCEPTION_FULL);

			// 增加对ExceptinUtil的引用
			// importList.add(workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + JavaSrcElm.EXCEPTION_UTIL_FULL);

			// 增加对SvcMsgCode的引用
			importList.add(workspaceSpec.getPkgPrefix() + JavaSrcElm.DOT + workspaceSpec.getMsgCdPkgName() + JavaSrcElm.DOT
					+ JavaSrcElm.SVC_MSG_CODE_SIMPLE);

		}

	}

	/**
	 * 新增import
	 * 
	 * @param importedRsrc
	 */
	public void addImport(String typeName) {

		// 如果不是完整的类声明，或者是java.lang的类，则不必import
		if (typeName.indexOf(JavaSrcElm.DOT) == -1 || typeName.startsWith(JavaSrcElm.JAVA_LANG_PKG_PREFIX) == true) {
			return;
		}

		// 如果是带有范型声明的class，则需要对泛型的内和外分别考虑。
		int lessThan = typeName.indexOf(JavaSrcElm.LESS_THAN);
		if (lessThan != -1) {
			int largerThan = typeName.indexOf(JavaSrcElm.LARGER_THAN);
			String genericType = typeName.substring(lessThan + 1, largerThan);
			int comma = genericType.indexOf(JavaSrcElm.COMMA);
			if (comma != -1) {
				String genericTypeLeft = StringUtils.trim(genericType.substring(0, comma));
				// 如果是java.lang的类，则不必import
				if (genericTypeLeft.startsWith(JavaSrcElm.JAVA_LANG_PKG_PREFIX) == false) {
					if (importIdx.contains(genericTypeLeft) == false) {
						importList.add(genericTypeLeft);
						importIdx.add(genericTypeLeft);
					}
				}
				// 如果是java.lang的类，则不必import
				String genericTypeRight = StringUtils.trim(genericType.substring(comma + 1));
				if (genericTypeRight.startsWith(JavaSrcElm.JAVA_LANG_PKG_PREFIX) == false) {
					if (importIdx.contains(genericTypeRight) == false) {
						importList.add(genericTypeRight);
						importIdx.add(genericTypeRight);
					}
				}
			} else {
				if (importIdx.contains(genericType) == false) {
					importList.add(genericType);
					importIdx.add(genericType);
				}
			}
			String containerType = typeName.substring(0, lessThan);
			if (importIdx.contains(containerType) == false) {
				importList.add(containerType);
				importIdx.add(containerType);
			}
		}
		// 如果没有泛型，则直接判断是否需要import
		else {
			if (importIdx.contains(typeName) == false) {
				importList.add(typeName);
				importIdx.add(typeName);
			}
		}

	}

	/**
	 * 新增属性
	 * 
	 * @param attr
	 */
	public void addAttr(AttributeSrc attrSrc, boolean isCreateAccess) {

		String attrName = attrSrc.getName();
		AttributeSrc currentAttrSrc = attrIdx.get(attrName);
		if (currentAttrSrc == null) {
			attrList.add(attrSrc);
			attrIdx.put(attrName, attrSrc);
			attrSrc.setOrdInAttrList(attrList.size());
			if (isCreateAccess) {
				createAccessor(attrSrc);
			}
		} else {
			currentAttrSrc.addVerbComments(attrSrc.getVerbComments());
			currentAttrSrc.addObjComments(attrSrc.getObjComments());
		}

	}

	/**
	 * Description: 新增方法
	 * Author: zhaoruibin
	 * Creation time: 2015年10月29日 下午7:28:56
	 *
	 * @param methodName
	 * @param methodSrc
	 */
	public void addMethod(String methodName, MethodSrc methodSrc) {

		if (methodIdx.containsKey(methodName) == true) {
			return;
		}
		methodIdx.put(methodName, methodSrc);
		methodList.add(methodSrc);

	}

	/**
	 * 新增get和set方法
	 * 
	 * @param attrSrc
	 */
	public void createAccessor(AttributeSrc attrSrc) {

		String attrName = attrSrc.getName();
		String methodNameSuffix = StringHelper.toUpperCase(attrName, 0);
		String dataType = attrSrc.getDataType();
		String name = attrSrc.getName();

		// Add a setter method.
		MethodSrc setterMethod = new MethodSrc();
		setterMethod.setName(JavaSrcElm.SET + methodNameSuffix);
		setterMethod.addParam(dataType, name);
		setterMethod.setReturnType(JavaSrcElm.VOID);
		methodList.add(setterMethod);

		// Add a getter method.
		MethodSrc getterMethod = new MethodSrc();
		getterMethod.setName(JavaSrcElm.GET + methodNameSuffix);
		getterMethod.setReturnType(dataType);
		methodList.add(getterMethod);

	}

	/**
	 * @return the javaClass
	 */
	public Class<Object> getJavaClass() {
		return javaClass;
	}

	/**
	 * @param javaClass
	 *            the javaClass to set
	 */
	public void setJavaClass(Class<Object> javaClass) {
		this.javaClass = javaClass;
	}

	/**
	 * @return the pkgName
	 */
	public String getPkgName() {
		return pkgName;
	}

	/**
	 * @param pkgName
	 *            the pkgName to set
	 */
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	/**
	 * @return the importList
	 */
	public List<String> getImportList() {
		return importList;
	}

	/**
	 * @param importList
	 *            the importList to set
	 */
	public void setImportList(List<String> importList) {
		this.importList = importList;
	}

	/**
	 * @return the importIdx
	 */
	public HashSet<String> getImportIdx() {
		return importIdx;
	}

	/**
	 * @param importIdx
	 *            the importIdx to set
	 */
	public void setImportIdx(HashSet<String> importIdx) {
		this.importIdx = importIdx;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the annotList
	 */
	public List<String> getAnnotList() {
		return annotList;
	}

	/**
	 * @param annotList
	 *            the annotList to set
	 */
	public void setAnnotList(List<String> annotList) {
		this.annotList = annotList;
	}

	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * @param modifier
	 *            the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * @return the simpleName
	 */
	public String getSimpleName() {
		return simpleName;
	}

	/**
	 * @param simpleName
	 *            the simpleName to set
	 */
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the extendsList
	 */
	public List<String> getExtendsList() {
		return extendsList;
	}

	/**
	 * @param extendsList
	 *            the extendsList to set
	 */
	public void setExtendsList(List<String> extendsList) {
		this.extendsList = extendsList;
	}

	/**
	 * @return the implementsList
	 */
	public List<String> getImplementsList() {
		return implementsList;
	}

	/**
	 * @param implementsList
	 *            the implementsList to set
	 */
	public void setImplementsList(List<String> implementsList) {
		this.implementsList = implementsList;
	}

	/**
	 * @return the attrList
	 */
	public List<AttributeSrc> getAttrList() {
		return attrList;
	}

	/**
	 * @param attrList
	 *            the attrList to set
	 */
	public void setAttrList(List<AttributeSrc> attrList) {
		this.attrList = attrList;
	}

	/**
	 * @return the attrIdx
	 */
	public Hashtable<String, AttributeSrc> getAttrIdx() {
		return attrIdx;
	}

	/**
	 * @param attrIdx
	 *            the attrIdx to set
	 */
	public void setAttrIdx(Hashtable<String, AttributeSrc> attrIdx) {
		this.attrIdx = attrIdx;
	}

	/**
	 * @return the pkAttr
	 */
	public String getPkAttr() {
		return pkAttr;
	}

	/**
	 * @param pkAttr
	 *            the pkAttr to set
	 */
	public void setPkAttr(String pkAttr) {
		this.pkAttr = pkAttr;
	}

	/**
	 * @return the methodList
	 */
	public List<MethodSrc> getMethodList() {
		return methodList;
	}

	/**
	 * @param methodList
	 *            the methodList to set
	 */
	public void setMethodList(List<MethodSrc> methodList) {
		this.methodList = methodList;
	}

	/**
	 * @return the methodIdx
	 */
	public Hashtable<String, MethodSrc> getMethodIdx() {
		return methodIdx;
	}

	/**
	 * @param methodIdx
	 *            the methodIdx to set
	 */
	public void setMethodIdx(Hashtable<String, MethodSrc> methodIdx) {
		this.methodIdx = methodIdx;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the existingFile
	 */
	public boolean isExistingFile() {
		return existingFile;
	}

	/**
	 * @param existingFile
	 *            the existingFile to set
	 */
	public void setExistingFile(boolean existingFile) {
		this.existingFile = existingFile;
	}

}