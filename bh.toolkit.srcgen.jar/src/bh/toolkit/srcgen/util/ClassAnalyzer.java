package bh.toolkit.srcgen.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.model.src.ClassSrc;

public class ClassAnalyzer {

	private static Logger logger = Logger.getLogger(ClassAnalyzer.class);

	@SuppressWarnings("unchecked")
	public Object getConstValue(String constName) throws AppException {

		Object value = null;
		try {
			int lastDot = constName.lastIndexOf(JavaSrcElm.DOT);
			String className = constName.substring(0, lastDot);
			String attrName = constName.substring(lastDot + 1, constName.length());

			Class<Object> javaClass = (Class<Object>) Class.forName(className);
			Field field = javaClass.getField(attrName);
			value = field.get(javaClass);
		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return value;

	}

	// @SuppressWarnings("unchecked")
	// public ClassSrc getAllAttrNames(String className, boolean getAll, boolean useCache) throws ClassNotFoundException, IllegalAccessException,
	// InstantiationException, IllegalArgumentException, InvocationTargetException {
	//
	// // If found a ClassSrc in 'classCache', then register mapped table and return.
	// Hashtable<String, ClassSrc> classSrcCache = null;
	// ClassSrc classSrc = null;
	// if (useCache == true) {
	//
	// classSrcCache = CtxCacheFacade.getClassSrcCache();
	// classSrc = classSrcCache.get(className);
	//
	// if (classSrc != null) {
	// return classSrc;
	// }
	//
	// }
	//
	// // If not found, then prepare to create a ClassSrc object and cache it into SrcGenContext.
	// Class<Object> javaClass = (Class<Object>) Class.forName(className);
	//
	// // If no method defined then no need further analysis.
	// Method[] methodArray = javaClass.getMethods();
	// if (methodArray == null || methodArray.length == 0) {
	// return classSrc;
	// }
	//
	// // Create a ClassSrc object, and cache it into SrcGenContext.
	// classSrc = new ClassSrc();
	// classSrc.setJavaClass(javaClass);
	// classSrc.setFullName(javaClass.getName());
	// classSrc.setPkgName(javaClass.getPackage().getName());
	// classSrc.setSimpleName(javaClass.getSimpleName());
	// if (useCache == true) {
	// classSrcCache.put(className, classSrc);
	// }
	//
	// // Prepare to analyze property with corresponding "get" method.
	// Method method = null;
	// Class methodDeclaringClass = null;
	// Class methodDeclaringSuperClass = null;
	// String methodDeclaringClassName = null;
	// String methodDeclaringSuperClassName = null;
	// String methodName = null;
	// String attrName = null;
	// String attrType = null;
	// for (int i = 0; i < methodArray.length; i++) {
	//
	// method = methodArray[i];
	// methodName = method.getName();
	// methodDeclaringClass = method.getDeclaringClass();
	//
	// // Skip those property declared in interface.
	// if (methodDeclaringClass.isInterface() == true) {
	// continue;
	// }
	//
	// // Skip those property declared in java.lang.Object.
	// methodDeclaringClassName = methodDeclaringClass.getName();
	// if (methodDeclaringClassName.equals(JavaSrcElm.LANG_OBJECT_FULL) == true) {
	// continue;
	// }
	//
	// // If NOT need to get all properties inside both DTO/BO and VO, then store properties ONLY declared in VO, and those overrode in DTO/BO.
	// // So skip properties ONLY declared in DTO/BO.
	// if (getAll == false) {
	// // && (methodDeclaringClassName.indexOf(JavaSrcElm.PKG_DEF_OF_DTO) != -1
	// // && (methodDeclaringClassName.endsWith(JavaSrcElm.DTO_SUFFIX) || methodDeclaringClassName
	// // .endsWith(JavaSrcElm.CLASS_SUFFIX_OF_BO)) || methodDeclaringClassName.indexOf(JavaSrcElm.PKG_DEF_OF_BO) != -1
	// // && (methodDeclaringClassName.endsWith(JavaSrcElm.DTO_SUFFIX) || methodDeclaringClassName
	// // .endsWith(JavaSrcElm.CLASS_SUFFIX_OF_BO)))) {
	//
	// // Get super class of DTO.
	// // If the super class is not java.lang.Object, then
	// // check whether a method with same name exist.
	// methodDeclaringSuperClass = methodDeclaringClass.getSuperclass();
	// methodDeclaringSuperClassName = methodDeclaringSuperClass.getName();
	// // logger.debug("The super class is: " +
	// // methodDeclaringSuperClassName);
	// if (methodDeclaringSuperClass != null && methodDeclaringSuperClassName.equals(JavaSrcElm.LANG_OBJECT_FULL) == false) {
	//
	// try {
	// // To detect whether a method with same name exist in
	// // VO. If yes (no
	// // exception), then treat it as property that need
	// // analysis.
	// methodDeclaringSuperClass.getMethod(methodName, null);
	// } catch (SecurityException e) {
	// continue;
	// } catch (NoSuchMethodException e) {
	// continue;
	// }
	//
	// }
	//
	// }
	//
	// attrType = method.getReturnType().getName();
	//
	// if (methodName.startsWith(JavaSrcElm.GET) == true) {
	// attrName = methodName.substring(JavaSrcElm.GET.length());
	// } else if (methodName.startsWith(JavaSrcElm.IS) == true) {
	// attrName = methodName.substring(JavaSrcElm.IS.length());
	// } else {
	// // Skip those methods are not 'getter'.
	// continue;
	// }
	//
	// // Change the first character into lower case.
	// attrName = StringHelper.toLowerCase(attrName, 0);
	//
	// classSrc.addAttr(attrName, attrType);
	//
	// }
	//
	// // Analyze all of the 'extends' for current Class.
	// analyzeAllSuperClass(classSrc, javaClass);
	//
	// return classSrc;
	//
	// }

	// /**
	// * Get all properties in class 'className'.
	// *
	// * @param className
	// * @return
	// * @throws ClassNotFoundException
	// * @throws IllegalAccessException
	// * @throws InstantiationException
	// * @throws IllegalArgumentException
	// * @throws InvocationTargetException
	// */
	// public ClassSrc cacheAllAttrNames(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException,
	// IllegalArgumentException, InvocationTargetException {
	//
	// return getAllAttrNames(className, false, true);
	//
	// }

	// /**
	// * Get all properties in class 'className.attrName'.
	// *
	// * @param className
	// * @param attrName
	// * @return
	// * @throws ClassNotFoundException
	// * @throws IllegalArgumentException
	// * @throws IllegalAccessException
	// * @throws InstantiationException
	// * @throws InvocationTargetException
	// */
	// @SuppressWarnings("unchecked")
	// public ClassSrc cacheAllAttrNames(String className, String attrName) throws ClassNotFoundException, IllegalArgumentException,
	// IllegalAccessException, InstantiationException, InvocationTargetException {
	//
	// // Get type of the given property.
	// Class attrType = getAttrType(className, attrName);
	// if (attrType == null) {
	// return null;
	// }
	//
	// return cacheAllAttrNames(attrType.getName());
	//
	// }

	@SuppressWarnings("unchecked")
	public Class getAttrType(String className, String attrName) throws ClassNotFoundException {

		// Initiate a class with the given class name.
		Class<Object> javaClass = (Class<Object>) Class.forName(className);

		// If no method defined.
		Method[] methodArray = javaClass.getMethods();
		if (methodArray == null || methodArray.length == 0) {
			return null;
		}

		// Search method for the given property.
		Method method = null;
		String methodName = null;
		Class fieldType = null;
		// String fieldName = null;
		String lowerCasePrefixAttrName = null;

		for (int i = 0; i < methodArray.length; i++) {

			method = methodArray[i];

			if (method.getDeclaringClass().getName().equals(JavaSrcElm.LANG_OBJECT_FULL) == true) {
				continue;
			}

			methodName = method.getName();
			if (methodName.startsWith(JavaSrcElm.GET) == true) {
				attrName = methodName.substring(JavaSrcElm.GET.length());
			} else if (methodName.startsWith(JavaSrcElm.IS) == true) {
				attrName = methodName.substring(JavaSrcElm.IS.length());
			} else {
				continue;
			}

			// Change the first character into lower case.
			lowerCasePrefixAttrName = StringHelper.toLowerCase(attrName, 0);

			// Try to match with lower case prefix, if not find, then use upper case prefix.
			// The purpose of these actions is to support attributes defined with both upper case prefix and lower case prefix, lower takes higher
			// preference.
			if (lowerCasePrefixAttrName.equals(attrName) == true || attrName.equals(attrName) == true) {
				fieldType = method.getReturnType();
				break;
			}

		}

		if (fieldType == null) {
			logger.error("NO TYPE: Fail to get type for: " + attrName + " in class: " + className);
		}

		return fieldType;

	}

	// /**
	// * Cache const into const class.
	// *
	// * @param constClassName
	// * @param constName
	// * @param constValue
	// * @throws IllegalAccessException
	// * @throws IllegalArgumentException
	// */
	// @SuppressWarnings("unchecked")
	// public void cacheConst(String classFullName, String constName, String constValue) throws IllegalArgumentException, IllegalAccessException {
	//
	// // Check whether 'constClass' is exist inside cache.
	// Hashtable<String, ClassSrc> constClassCache = CtxCacheFacade.getConstClassCache();
	// ClassSrc constClass = constClassCache.get(classFullName);
	//
	// // If not found 'constClass' in 'constClassCache', then initiate a new one and populate it by loading or creating.
	// if (constClass == null) {
	//
	// logger.debug("CACHE CONST: No const class found from cache with name: " + classFullName);
	//
	// constClass = new ClassSrc();
	//
	// // Load class with the given class name.
	// try {
	//
	// Class<Object> javaClass = (Class<Object>) Class.forName(classFullName);
	//
	// logger.debug("CACHE CONST: " + "Load const class with name: " + classFullName);
	//
	// // If the class is found in class path, then use it to describe the new one.
	// Field[] fieldArray = javaClass.getFields();
	//
	// if (fieldArray != null) {
	//
	// Field field = null;
	// OrmAttr existingConst = null;
	// for (int i = 0; i < fieldArray.length; i++) {
	//
	// field = fieldArray[i];
	//
	// existingConst = new OrmAttr();
	// existingConst.setName(field.getName());
	// existingConst.setDefaultValue(field.get(javaClass).toString());
	//
	// constClass.addAttr(existingConst);
	// // constClass.addAttrIdx(field.getName(), existingConst);
	//
	// }
	//
	// }
	//
	// } catch (ClassNotFoundException e) {
	//
	// // If no class found in class path, then no additional description needed for the new one.
	// logger.debug("CACHE CONST: " + classFullName + " is not in classpath, consider it as new one");
	// constClass.setFullName(classFullName);
	// String classSimpleName = JavaFormatter.getClassSimpleName(classFullName);
	// constClass.setSimpleName(classSimpleName);
	//
	// }
	//
	// // Put 'constClass' into cache.
	// constClassCache.put(classFullName, constClass);
	//
	// }
	//
	// // Add or update the given const into const class.
	// OrmAttr newConst = constClass.getAttrIdx().get(constName);
	// if (newConst == null) {
	// newConst = new OrmAttr();
	// newConst.setName(constName);
	//
	// }
	//
	// newConst.setDefaultValue(constValue);
	// constClass.addAttr(newConst);
	// // constClass.addAttrIdx(constName, newConst);
	//
	// }

//	private void analyzeAllSuperClass(ClassSrc classSrc, Class javaClass) {
//
//		Class superClass = javaClass.getSuperclass();
//		if (superClass != null) {
//
//			// Add name of super class into extendsList inside classEntity.
//			classSrc.getExtendsList().add(superClass.getName());
//
//			// Invoke recursively.
//			analyzeAllSuperClass(classSrc, superClass);
//
//		}
//
//	}

	// public static void main(String[] args) {
	//
	// try {
	//
	// ClassAnalyzer ca = new ClassAnalyzer();
	// // ClassEntity entity = ca.getAllPropNames("com.ibm.chas.model.meta.adt.dto.AccommodationDTO", true, false);
	// ClassSrc entity = ca.getAllAttrNames("com.ibm.chas.toolkit.srcgen.common.adapter.MyTestDTO", true, false);
	//
	// List<OrmAttr> attrList = entity.getAttrList();
	// System.out.println("The size of 'attrList' is: " + attrList.size());
	// OrmAttr attrEntity = null;
	// for (int i = 0; i < attrList.size(); i++) {
	// attrEntity = attrList.get(i);
	// System.out.println(attrEntity.getName());
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

}
