package org.thorn.web.entity;

import java.io.Serializable;

/** 
 * @ClassName: Relation 
 * @Description: 表示2个对象间是否有关系
 * @author chenyun
 * @date 2012-5-22 下午09:35:22 
 */
public class Relation implements Serializable {
	
	/** */
	private static final long serialVersionUID = -5908234226940243602L;

	private Object subject;
	
	private Object object;
	
	private boolean relevance;

	public Object getSubject() {
		return subject;
	}

	public void setSubject(Object subject) {
		this.subject = subject;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public boolean isRelevance() {
		return relevance;
	}

	public void setRelevance(boolean relevance) {
		this.relevance = relevance;
	}
	
}

