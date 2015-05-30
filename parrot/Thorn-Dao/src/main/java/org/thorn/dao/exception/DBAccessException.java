package org.thorn.dao.exception;

import org.thorn.core.util.LocalStringUtils;

/**
 * 
 * @ClassName: DBAccessException
 * @Description:
 * @author chenyun
 * @date 2012-4-26 上午11:21:48
 * 
 */
public class DBAccessException extends Exception {

	/** */
	private static final long serialVersionUID = -5225119417254795559L;
	
	private String dbExceptionMsg = "";
	
	public DBAccessException() {
		super();
	}

	public DBAccessException(String msg) {
		super(msg);
		dbExceptionMsg = msg;
	}

	public DBAccessException(Throwable cause) {
		super(cause);
	}

	public DBAccessException(String msg, Throwable cause) {
		super(msg, cause);
		dbExceptionMsg = msg;
	}

	public DBAccessException(String cls, String method, String msg,
			Throwable cause) {
		super(cause);
		
		StringBuilder message = new StringBuilder(cls);
		message.append(" do ").append(method).append(" exception,");
		message.append(msg).append(",").append(cause.getMessage());
		
		dbExceptionMsg = message.toString();
	}
	
	public DBAccessException(String cls, String method,
			Throwable cause) {
		super(cause);
		
		StringBuilder message = new StringBuilder(cls);
		message.append(" do ").append(method).append(" exception");
		message.append(",").append(cause.getMessage());
		
		dbExceptionMsg = message.toString();
	}
	
	public String getMessage() {
		if(LocalStringUtils.isEmpty(dbExceptionMsg)) {
			return super.getMessage();
		}
		
		return this.dbExceptionMsg;
	}

}
