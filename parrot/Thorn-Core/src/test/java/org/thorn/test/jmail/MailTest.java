package org.thorn.test.jmail;



import org.thorn.core.jmail.MailCard;
import org.thorn.core.jmail.MailEntity;
import org.thorn.core.jmail.MailException;
import org.thorn.core.jmail.MailHelper;

import junit.framework.TestCase;

public class MailTest extends TestCase {
	
	private MailEntity entity;
	private MailCard cytw = new MailCard("chenyun@talkweb.com.cn", "");
	
	protected void setUp() {
		MailCard cy163 = new MailCard("chenyun313@163.com", "陈云");
		MailCard cygm = new MailCard("chenyun@gmail.com", "陈云-gm");
		
		entity = new MailEntity();
		entity.setSubject("测试");
		entity.setContent("<b>一封测试邮件</b>");
		
		entity.getReceiver().add(cy163);
		entity.getCopyer().add(cytw);
		entity.getAttAddress().add("D:\\cms\\index.jhtml");
	}
	
	public void testSendMail() {
		
		
//		MailHelper mh = new MailHelper(cy163, "chenyun313", "313chenyun", "smtp.163.com");
		MailHelper mh = new MailHelper(cytw, "chenyun", "3450380cy", "mail.talkweb.com.cn");
		
		try {
			mh.sendMail(entity, true);
		} catch (MailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mh.closeMailConnect();
	}
}
