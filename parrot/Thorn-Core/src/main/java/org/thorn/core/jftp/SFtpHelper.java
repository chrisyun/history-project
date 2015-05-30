package org.thorn.core.jftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * 
 * @ClassName: SFtpHelper 
 * @Description:
 * @author chenyun
 * @date 2012-5-2 上午10:25:05 
 * 出现java.security.NoSuchAlgorithmException: DH KeyPairGenerator not available
 * 添加sunJDK:jdk1.5.0_15\jre\lib\ext\sunjce_provider.jar和sunpkcs11.jar
 *
 */
public class SFtpHelper extends JFtpHelper {

	private static Logger log = LoggerFactory.getLogger(SFtpHelper.class);

	private ChannelSftp sftp;

	public SFtpHelper() {
		super();
	}

	public SFtpHelper(String userName, String password, String serverIp) {
		super(userName, password, serverIp);
		// sftp方式的默认端口为22
		this.port = 22;
	}

	public SFtpHelper(String userName, String password, String serverIp,
			int port) {
		super(userName, password, serverIp, port);
	}

	@Override
	public boolean login() {
		boolean flag = false;

		JSch jsch = new JSch();

		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		try {
			jsch.getSession(this.userName, this.serverIp, this.port);
			Session sshSession = jsch.getSession(this.userName, this.serverIp,
					this.port);
			sshSession.setPassword(this.password);
			sshSession.setConfig(sshConfig);

			sshSession.connect();

			sftp = (ChannelSftp) sshSession.openChannel("sftp");
			sftp.connect();

			flag = true;
		} catch (JSchException e) {
			log.error("sftp login failure", e);
		}
		return flag;
	}

	@Override
	public void closeConnect() {
		if (sftp != null && sftp.isConnected()) {
			sftp.disconnect();
		}

		log.debug("sftp connection closed...");
	}

	@Override
	public void upload(File file, String targetFolder) throws FtpException {
		log.debug("begin to upload file...");

		if (!file.exists()) {
			throw new FtpException("file does not exist");
		}

		try {
			// 进入ftp服务器的文件目录
			if (targetFolder.length() != 0) {
				sftp.cd(targetFolder);
				log.debug("cd the sftp server folder:{}", targetFolder);
			}

			sftp.put(new FileInputStream(file), file.getName());

		} catch (SftpException e) {
			throw new FtpException("upload file happens SftpException", e);
		} catch (IOException e) {
			throw new FtpException("upload file happens IOException", e);
		}
	}

	@Override
	public void download(String fileName, String targetFolder, File file)
			throws FtpException {
		log.debug("begin to download file...");

		InputStream is = null;
		FileOutputStream out = null;

		try {
			// 进入ftp服务器的文件目录
			if (targetFolder.length() != 0) {
				sftp.cd(targetFolder);
				log.debug("cd the sftp server folder:{}", targetFolder);
			}

			is = sftp.get(fileName);
			out = new FileOutputStream(file);
			byte[] bytes = new byte[10240];
			int c;
			while ((c = is.read(bytes)) != -1) {
				out.write(bytes, 0, c);
			}

		} catch (SftpException e) {
			throw new FtpException("upload file happens SftpException", e);
		} catch (IOException e) {
			throw new FtpException("upload file happens IOException", e);
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
