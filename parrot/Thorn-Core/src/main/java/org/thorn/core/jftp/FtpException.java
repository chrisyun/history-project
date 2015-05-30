package org.thorn.core.jftp;

/**
 * 
 * @ClassName: FtpException 
 * @Description:
 * @author chenyun
 * @date 2012-5-2 上午10:20:41 
 *
 */
public class FtpException extends Exception {
	
	public FtpException() {
		super();
	}
	
	public FtpException(String message) {
		super(message);
	}
	
	public FtpException(String message, Throwable cause) {
		super(message, cause);
	}
	
}

