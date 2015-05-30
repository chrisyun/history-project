package com.talkweb.ncfw.mail;

import org.thorn.core.jmail.MailCard;
import org.thorn.core.jmail.MailEntity;
import org.thorn.core.jmail.MailException;
import org.thorn.core.jmail.MailHelper;

import com.talkweb.ncframework.pub.utils.spring.SpringContextUtils;

public class MailTemplete {

	private final static String MAIL_HEADER = "<html><body style=\"font-size: 15px; font: 宋体;\">";

	private final static String MAIL_FOOTER = "<p>----------------------------------------------------------"
			+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			+ "系统邮件，请勿回复。</p></body></html>";
	
	public static MailEntity findPassWd(String userName, String email,
			String pwd) {
		MailEntity mail = new MailEntity();

		MailCard card = new MailCard(email, userName);
		mail.getReceiver().add(card);

		mail.setSubject("文化部科研项目申报平台 - 密码修改");

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
	
	public static MailEntity auditRegistUser(String userName, String email, boolean auditResult) {
		MailEntity mail = new MailEntity();

		MailCard card = new MailCard(email, userName);
		mail.getReceiver().add(card);

		mail.setSubject("文化部科研项目申报平台 - 用户审核");

		StringBuilder content = new StringBuilder(MAIL_HEADER);

		content.append("<p>尊敬的&nbsp;<span style=\"color: blue;\">");
		content.append(userName).append("</span>&nbsp;用户，您好： </p><br>");
		if(auditResult) {
			content.append("<p>您在文化部科研项目申报平台的注册申请审核通过，您可以使用注册账号登录系统。</p>");
		} else {
			content.append("<p>您在文化部科研项目申报平台的注册申请审核未通过。</p>");
		}
		
		content.append(MAIL_FOOTER);
		mail.setContent(content.toString());
		
		return mail;
	}
	
	public static void sendMail(MailEntity mail) throws MailException {
		MailHelper helper =  SpringContextUtils.getBean("mailHelper");
		helper.sendMail(mail);
	}
	
}
