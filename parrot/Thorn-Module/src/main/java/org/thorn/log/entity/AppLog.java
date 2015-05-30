package org.thorn.log.entity;

import java.io.Serializable;

/** 
 * @ClassName: AppLog 
 * @Description: 
 * @author chenyun
 * @date 2012-5-26 上午11:42:50 
 */
public class AppLog implements Serializable {

	/** */
	private static final long serialVersionUID = -8589445271312303571L;
	
	private Integer id;
	
	private String moduleName;
	
	private String methodName;
	
	private String parameters;
	
	private String userId;
	
	private String handleResult;
	
	private String executeTime;
	
	private String errorMsg;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}

