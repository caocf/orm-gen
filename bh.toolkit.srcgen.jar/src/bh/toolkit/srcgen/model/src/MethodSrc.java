package bh.toolkit.srcgen.model.src;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.util.JavaFormatter;
import bh.toolkit.srcgen.util.StringHelper;

public class MethodSrc {

	private String comments;
	private List<String> annotList;
	private String modifiers;
	private String returnType;
	private String returnName;
	private String name;
	private List<String> paramTypeList;
	private List<String> paramNameList;
	private List<String> exceptionList;
	private String contentBody;

	public MethodSrc() {
		this.annotList = new ArrayList<String>();
		this.paramTypeList = new ArrayList<String>();
		this.paramNameList = new ArrayList<String>();
		this.exceptionList = new ArrayList<String>();
	}
	
	public void addAnnot(String annot) {
		annotList.add(annot);
	}

	public void addParam(String paramType, String paramName) {

		// 增加参数类型
		paramTypeList.add(paramType);
		// 如果已经提供了paramName，则直接使用
		if (StringUtils.isNotBlank(paramName) == true) {
			paramNameList.add(paramName);
			return;
		}

		// 增加对应的参数名称
		if (paramType.contains(JavaSrcElm.UTIL_LIST_SIMPLE) == true && paramType.contains(JavaSrcElm.LARGER_THAN) == true) {
			paramNameList.add(StringHelper.toLowerCase(JavaFormatter.getTypeOfList(paramType), 0) + JavaSrcElm.UTIL_LIST_SIMPLE);
		} else if (paramType.contains(JavaSrcElm.UTIL_MAP_SIMPLE) == true && paramType.contains(JavaSrcElm.LARGER_THAN) == true) {
			paramNameList.add(StringHelper.toLowerCase(JavaFormatter.getTypeOfMap(paramType), 0) + JavaSrcElm.UTIL_MAP_SIMPLE);
		} else if (paramType.endsWith(JavaSrcElm.VO_NAME_SUFFIX) || paramType.endsWith(JavaSrcElm.DTO_NAME_SUFFIX)
				|| paramType.endsWith(JavaSrcElm.DTOX_NAME_SUFFIX)) {
			paramNameList.add(StringHelper.toLowerCase(paramType, 0));
		} else {
			paramNameList.add(JavaSrcElm.PARAM_PREFIX + paramNameList.size());
		}

	}

	public void addException(String expFullName) {
		exceptionList.add(expFullName);
	}

	public String getModifiers() {
		return modifiers;
	}

	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getParamTypeList() {
		return paramTypeList;
	}

	public void setParamTypeList(List<String> paramTypeList) {
		this.paramTypeList = paramTypeList;
	}

	public List<String> getParamNameList() {
		return paramNameList;
	}

	public void setParamNameList(List<String> paramNameList) {
		this.paramNameList = paramNameList;
	}

	public List<String> getExceptionList() {
		return exceptionList;
	}

	public void setExceptionList(List<String> exceptionList) {
		this.exceptionList = exceptionList;
	}

	// public Hashtable<String, String> getBody() {
	// return body;
	// }
	//
	// public void setBody(Hashtable<String, String> body) {
	// this.body = body;
	// }

	// public String getType() {
	// return type;
	// }

	// public void setType(String type) {
	// this.type = type;
	// }

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<String> getAnnotList() {
		return annotList;
	}

	public void setAnnotList(List<String> annotList) {
		this.annotList = annotList;
	}

	public String getContentBody() {
		return contentBody;
	}

	public void setContentBody(String contentBody) {
		this.contentBody = contentBody;
	}

}
