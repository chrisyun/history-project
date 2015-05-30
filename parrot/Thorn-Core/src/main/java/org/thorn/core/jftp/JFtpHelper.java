package org.thorn.core.jftp;

import java.io.File;

/**
 * 
 * @ClassName: JFtpHelper
 * @Description:
 * @author chenyun
 * @date 2012-5-2 上午10:21:45
 * 
 */
public abstract class JFtpHelper {

	/** 用户名 */
	protected String userName;
	/** 密码 */
	protected String password;
	/** 服务器主机地址 */
	protected String serverIp;
	/** 端口号 */
	protected int port = 21;

	public JFtpHelper() {

	}

	public JFtpHelper(String userName, String password, String serverIp) {
		this.userName = userName;
		this.password = password;
		this.serverIp = serverIp;
	}

	public JFtpHelper(String userName, String password, String serverIp,
			int port) {
		this(userName, password, serverIp);
		this.port = port;
	}

	/**
	 * 
	 * @Description：登陆ftp服务器
	 * @author：chenyun
	 * @date：2012-5-2 上午10:22:04
	 * @return 登陆成功返回true，失败则返回false
	 */
	public abstract boolean login();

	/**
	 * 
	 * @Description：关闭ftp连接
	 * @author：chenyun
	 * @date：2012-5-2 上午10:22:39
	 */
	public abstract void closeConnect();

	/**
	 * 
	 * @Description：上传文件
	 * @author：chenyun
	 * @date：2012-5-2 上午10:22:53
	 * @param file			上传的文件
	 * @param targetFolder	目标文件夹
	 * @throws FtpException
	 */
	public abstract void upload(File file, String targetFolder)
			throws FtpException;

	/**
	 * 
	 * @Description：下载文件
	 * @author：chenyun 	        
	 * @date：2012-5-2 上午10:23:50
	 * @param fileName		下载的文件名称
	 * @param targetFolder	FTP服务器目标文件夹
	 * @param file			下载的文件
	 * @throws FtpException
	 */
	public abstract void download(String fileName, String targetFolder,
			File file) throws FtpException;

}
