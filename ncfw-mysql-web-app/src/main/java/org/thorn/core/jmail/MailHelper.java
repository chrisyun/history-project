package org.thorn.core.jmail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

/**
 * 
 * @ClassName: MailHelper
 * @Description: 邮件发送辅助类,提供邮件发送及关闭连接的方法
 * @author chenyun
 * @date 2012-5-10 上午09:40:22
 * 
 */
public class MailHelper {

	private static Logger log = Logger.getLogger(MailHelper.class);

	/** 发件人邮箱 */
	private MailCard sender;

	/** 邮箱账号 */
	private String userName;

	/** 邮箱密码 */
	private String password;

	/** 邮件服务器Host */
	private String smtpHost;

	/** 是否需要服务器认证 */
	private boolean needAuth = true;

	/** 邮件发送器 */
	private Transport transport;

	/** 会话 */
	private Session session;

	public MailHelper() {

	}

	public MailHelper(MailCard sender, String userName, String password,
			String smtpHost) {
		this.sender = sender;
		this.userName = userName;
		this.password = password;
		this.smtpHost = smtpHost;
	}

	public MailHelper(MailCard sender, String userName, String password,
			String smtpHost, boolean needAuth) {

		this(sender, userName, password, smtpHost);
		this.needAuth = needAuth;
	}

	/**
	 * 
	 * @author：chenyun
	 * @date：2012-3-12
	 * @Description：发送邮件，不开启调试模式
	 * @param entity
	 *            邮件实体内容
	 * @return 成功返回true，失败返回false
	 * @throws MailException 
	 */
	public boolean sendMail(MailEntity entity) throws MailException {
		return this.sendMail(entity, false);
	}

	/**
	 * 
	 * @author：chenyun
	 * @date：2012-3-12
	 * @Description：发送邮件
	 * @param entity
	 *            邮件实体内容
	 * @param isDebug
	 *            是否开启调试模式
	 * @return 成功返回true，失败返回false
	 * @throws MailException
	 */
	public boolean sendMail(MailEntity entity, boolean isDebug)
			throws MailException {
		log.debug("begin to send mail...");

		boolean flag = false;

		try {
			this.initTransport();

			this.session.setDebug(isDebug);

			MimeMessage message = new MimeMessage(this.session);
			// 设置发件人
			message.setFrom(new InternetAddress(this.sender.geteMail(),
					this.sender.getName()));
			// 设置发件日期
			message.setSentDate(new Date());

			log.debug("set mail sender and date");
			// 设置邮件接收人
			InternetAddress[] recipients = new InternetAddress[entity
					.getReceiver().size()];
			for (int i = 0; i < recipients.length; i++) {
				MailCard recCard = entity.getReceiver().get(i);
				recipients[i] = new InternetAddress(recCard.geteMail(),
						recCard.getName());
			}
			message.addRecipients(Message.RecipientType.TO, recipients);

			log.debug("set mail receivers,size: " + recipients.length);
			// 设置邮件抄送人
			InternetAddress[] copyers = new InternetAddress[entity
					.getCopyer().size()];
			for (int i = 0; i < copyers.length; i++) {
				MailCard recCard = entity.getCopyer().get(i);
				copyers[i] = new InternetAddress(recCard.geteMail(),
						recCard.getName());
			}
			message.addRecipients(Message.RecipientType.CC, copyers);

			log.debug("set mail copyers,size : " + copyers.length);
			// 设置邮件主题
			message.setSubject(entity.getSubject());

			Multipart multipart = new MimeMultipart();
			// 设置邮件内容，为html语法格式
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent(entity.getContent(), "text/html;charset=GBK");
			multipart.addBodyPart(textPart);

			log.debug("set mail content");
			// 设置邮件附件
			for (String filePath : entity.getAttAddress()) {
				MimeBodyPart contentPart = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(filePath);

				contentPart.setDataHandler(new DataHandler(fds));
				contentPart.setFileName(fds.getName());

				multipart.addBodyPart(contentPart);
			}
			log.debug("set mail atts,size : " + entity.getAttAddress().size());

			message.setContent(multipart);
			message.saveChanges();
			// 发送邮件
			this.transport.sendMessage(message, message.getAllRecipients());

			log.debug("send mail success!");
			flag = true;
		} catch (MessagingException e) {
			throw new MailException("do send mail MessagingException", e);
		} catch (UnsupportedEncodingException e) {
			throw new MailException(
					"do send mail UnsupportedEncodingException", e);
		}

		return flag;
	}

	/**
	 * 
	 * @author：chenyun
	 * @date：2012-3-12
	 * @Description：关闭邮件发送器连接
	 */
	public void closeMailConnect() {
		if (this.transport != null && this.transport.isConnected()) {

			try {
				this.transport.close();
			} catch (MessagingException e) {
				log.error("close mail connection happens MessagingException:",
						e);
			}
		}
	}

	/**
	 * 
	 * @author：chenyun
	 * @date：2012-3-12
	 * @Description：初始化发送器，依赖于会话创建
	 * @throws MessagingException
	 */
	private void initTransport() throws MessagingException {
		if (this.transport != null && this.transport.isConnected()) {
			return;
		}

		log.debug("begin to init transport");

		this.initSession();
		this.transport = this.session.getTransport("smtp");
		this.transport.connect(this.smtpHost, this.userName, this.password);

	}

	/**
	 * 
	 * @author：chenyun
	 * @date：2012-3-12
	 * @Description：初始化会话属性，最早调用
	 */
	private void initSession() {

		if (this.session != null) {
			return;
		}

		log.debug("begin to init mail session");
		// 系统属性
		Properties props = System.getProperties();

		// 设置SMTP主机
		props.put("mail.smtp.host", this.smtpHost);

		if (this.needAuth) {
			props.put("mail.smtp.auth", "true");

			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, password);
				}
			};

			this.session = Session.getInstance(props, auth);
		} else {
			props.put("mail.smtp.auth", "false");
			this.session = Session.getInstance(props);
		}
	}

	public MailCard getSender() {
		return sender;
	}

	public void setSender(MailCard sender) {
		this.sender = sender;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public boolean isNeedAuth() {
		return needAuth;
	}

	public void setNeedAuth(boolean needAuth) {
		this.needAuth = needAuth;
	}
}