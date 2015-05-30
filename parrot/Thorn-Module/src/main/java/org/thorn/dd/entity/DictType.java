package org.thorn.dd.entity;

import java.io.Serializable;

/** 
 * @ClassName: DictType 
 * @Description: 
 * @author chenyun
 * @date 2012-5-7 上午09:53:10 
 */
public class DictType implements Serializable {
	
	/** */
	private static final long serialVersionUID = -6102661629065640332L;

	private String ename;
	
	private String cname;
	
	private String typeDesc;

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
}

