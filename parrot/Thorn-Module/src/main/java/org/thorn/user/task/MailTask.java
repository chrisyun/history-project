package org.thorn.user.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thorn.core.context.SpringContext;
import org.thorn.core.jmail.MailEntity;
import org.thorn.core.jmail.MailException;
import org.thorn.core.jmail.MailHelper;

/** 
 * @ClassName: MailTask 
 * @Description: 
 * @author chenyun
 * @date 2012-8-8 下午01:40:21 
 */
public class MailTask implements Runnable {
	
	static Logger logger = LoggerFactory.getLogger(MailTask.class);
	
	private MailEntity mail;
	
	public MailTask(MailEntity mail) {
		this.mail = mail;
	}
	
	public void run() {
		MailHelper helper =  SpringContext.getBean("mailHelper");
		
		try {
			helper.sendMail(mail);
		} catch (MailException e) {
			logger.error("MailTask do Send [MailEntity] - " + e.getMessage(), e);
		}
	}

}

