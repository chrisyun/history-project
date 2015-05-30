package org.thorn.core.jmail;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName: MailEntity 
 * @Description: 邮件内容的实体对象
 * @author chenyun
 * @date 2012-5-10 上午09:41:27 
 *
 */
public class MailEntity {
	private static final long serialVersionUID = 6190395311350810672L;

	/** 邮件主题 */
	private String subject;
	
	/** 邮件内容 */
	private String content;
	
	/** 接收人 */
	private List<MailCard> receiver = new ArrayList<MailCard>();
	
	/** 抄送人 */
	private List<MailCard> copyer = new ArrayList<MailCard>();
	
	/** 附件地址 */
	private List<String> attAddress = new ArrayList<String>();;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<MailCard> getReceiver() {
		return receiver;
	}

	public void setReceiver(List<MailCard> receiver) {
		this.receiver = receiver;
	}

	public List<MailCard> getCopyer() {
		return copyer;
	}

	public void setCopyer(List<MailCard> copyer) {
		this.copyer = copyer;
	}

	public List<String> getAttAddress() {
		return attAddress;
	}

	public void setAttAddress(List<String> attAddress) {
		this.attAddress = attAddress;
	}
}


