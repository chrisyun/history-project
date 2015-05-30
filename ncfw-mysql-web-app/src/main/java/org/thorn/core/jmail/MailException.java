package org.thorn.core.jmail;

/**
 * 
 * @ClassName: MailException 
 * @Description:
 * @author chenyun
 * @date 2012-5-2 上午10:20:41 
 *
 */
public class MailException extends Exception {
	
	public MailException() {
		super();
	}
	
	public MailException(String message) {
		super(message);
	}
	
	public MailException(String message, Throwable cause) {
		super(message, cause);
	}
	
}

