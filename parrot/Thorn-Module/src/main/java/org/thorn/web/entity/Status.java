package org.thorn.web.entity;

import java.io.Serializable;

/** 
 * @ClassName: Status 
 * @Description: 表示操作结果的数据对象，供给前台使用，只传递状态和消息
 * @author chenyun
 * @date 2012-5-6 下午08:08:52 
 */
public class Status implements Serializable {
	
	/** */
	private static final long serialVersionUID = 1902952901629816953L;
	
	private boolean success = true;
	
	private String message;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

