package org.thorn.core.jmail;

/**
 * 
 * @ClassName: MailCard 
 * @Description: 邮件名片实体对象
 * @author chenyun
 * @date 2012-5-10 上午09:41:40 
 *
 */
public class MailCard {
	/** 邮箱地址 */
	private String eMail;
	
	/** 邮件名字 */
	private String name;
	
	public MailCard() {
		
	}
	
	public MailCard(String eMail, String name) {
		this.eMail = eMail;
		this.name = name;
	}
	
	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}