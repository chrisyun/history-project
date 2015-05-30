package org.thorn.user.task;

import org.thorn.core.jmail.MailCard;
import org.thorn.core.jmail.MailEntity;

/**
 * @ClassName: MailTemplete
 * @Description:
 * @author chenyun
 * @date 2012-8-9 下午03:39:19
 */
public class MailTemplete {

	private final static String MAIL_HEADER = "<html><body style=\"font-size: 15px; font: 宋体;\">";

	private final static String MAIL_FOOTER = "<p>----------------------------------------------------------"
			+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			+ "系统邮件，请勿回复。</p></body></html>";

	public static MailEntity registerUser(String userName, String email,
			String pwd, String userId) {
		MailEntity mail = new MailEntity();

		MailCard card = new MailCard(email, userName);
		mail.getReceiver().add(card);
		
		mail.setSubject("非物质文化遗产项目管理平台 - 新用户注册");
		
		StringBuilder content = new StringBuilder(MAIL_HEADER);

		content.append("<p>尊敬的&nbsp;<span style=\"color: blue;\">");
		content.append(userName).append("</span>&nbsp;用户，您好： </p><br>");
		content.append("<p>您的系统账号已经开通：<br>");
		content.append("登录账号：&nbsp;<span style=\"color: blue;\">");
		content.append(userId).append("</span>&nbsp;<br>");
		content.append("登录密码：&nbsp;<span style=\"color: blue;\">");
		content.append(pwd).append("</span>&nbsp;</p>");
		content.append("<p>请您尽快登录系统，将密码修改！</p>");
		content.append(MAIL_FOOTER);
		mail.setContent(content.toString());
		
		return mail;
	}

	public static MailEntity findPassWd(String userName, String email,
			String pwd) {
		MailEntity mail = new MailEntity();

		MailCard card = new MailCard(email, userName);
		mail.getReceiver().add(card);

		mail.setSubject("非物质文化遗产项目管理平台 - 找回密码");

		StringBuilder content = new StringBuilder(MAIL_HEADER);

		content.append("<p>尊敬的&nbsp;<span style=\"color: blue;\">");
		content.append(userName).append("</span>&nbsp;用户，您好： </p><br>");
		content.append("<p>您的密码已经被重置，新的密码为：&nbsp;<span style=\"color: blue;\">");
		content.append(pwd).append("</span>&nbsp;。</p>");
		content.append("<p>请您尽快登录系统，将密码修改！</p>");
		content.append(MAIL_FOOTER);
		mail.setContent(content.toString());
		
		return mail;
	}

	public static MailEntity changePassWd(String userName, String email,
			String pwd) {
		MailEntity mail = new MailEntity();

		MailCard card = new MailCard(email, userName);
		mail.getReceiver().add(card);

		mail.setSubject("非物质文化遗产项目管理平台 - 密码修改");

		StringBuilder content = new StringBuilder(MAIL_HEADER);

		content.append("<p>尊敬的&nbsp;<span style=\"color: blue;\">");
		content.append(userName).append("</span>&nbsp;用户，您好： </p><br>");
		content.append("<p>您的密码已经被管理员修改，新的密码为：&nbsp;<span style=\"color: blue;\">");
		content.append(pwd).append("</span>&nbsp;。</p>");
		content.append("<p>请您尽快登录系统，将密码修改！</p>");
		content.append(MAIL_FOOTER);
		mail.setContent(content.toString());
		
		return mail;
	}

}
