package org.thorn.core.jftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

/**
 * 
 * @ClassName: FtpHelper 
 * @Description:
 * @author chenyun
 * @date 2012-5-2 上午10:20:51 
 *
 */
public class FtpHelper extends JFtpHelper {

	private static Logger log = LoggerFactory.getLogger(FtpHelper.class);

	private FtpClient ftpClient;

	public FtpHelper() {
		super();
	}

	public FtpHelper(String userName, String password, String serverIp) {
		super(userName, password, serverIp);
	}

	public FtpHelper(String userName, String password, String serverIp, int port) {
		super(userName, password, serverIp, port);
	}

	@Override
	public boolean login() {
		boolean flag = false;

		try {
			ftpClient = new FtpClient(this.serverIp, this.port);
			ftpClient.login(this.userName, this.password);

			flag = true;
		} catch (IOException e) {
			log.error("ftp login failure", e);
		}

		return flag;
	}

	@Override
	public void closeConnect() {
		if (this.ftpClient != null) {
			try {
				ftpClient.closeServer();
			} catch (IOException e) {
				log.error("close ftp connection happens IOException", e);
			}
		}

		log.debug("ftp connection closed...");

	}

	@Override
	public void upload(File file, String targetFolder) throws FtpException {

		log.debug("begin to upload file...");

		if (!file.exists()) {
			throw new FtpException("file does not exist");
		}

		TelnetOutputStream os = null;
		FileInputStream is = null;

		try {
			// 进入ftp服务器的文件目录
			if (targetFolder.length() != 0) {
				ftpClient.cd(targetFolder);

				log.debug("cd the ftp server folder:{}", targetFolder);
			}
			// 采用二进制的传输方式
			ftpClient.binary();

			os = ftpClient.put(file.getName());
			is = new FileInputStream(file);

			byte[] bytes = new byte[10240];
			int c;
			while ((c = is.read(bytes)) != -1) {
				os.write(bytes, 0, c);
			}

		} catch (IOException e) {
			throw new FtpException("upload file happens IOException", e);
		} finally {

			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				throw new FtpException("close stream happens IOException", e);
			}

			log.debug("upload file over!");
		}

	}

	@Override
	public void download(String fileName, String targetFolder, File file)
			throws FtpException {

		log.debug("begin to download file...");
		TelnetInputStream is = null;
		FileOutputStream out = null;

		try {
			// 进入ftp服务器的文件目录
			if (targetFolder.length() != 0) {
				ftpClient.cd(targetFolder);
			}
			// 采用二进制的传输方式
			ftpClient.binary();

			is = ftpClient.get(fileName);

			out = new FileOutputStream(file);
			byte[] bytes = new byte[10240];
			int c;
			while ((c = is.read(bytes)) != -1) {
				out.write(bytes, 0, c);
			}

		} catch (IOException e) {
			throw new FtpException("download file happens IOException", e);
		} finally {

			try {
				if (is != null) {
					is.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				throw new FtpException("close stream happens IOException", e);
			}

			log.debug("download file over!");
		}

	}
}
