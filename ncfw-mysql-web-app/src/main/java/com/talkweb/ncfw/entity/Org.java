package com.talkweb.ncfw.entity;
/**
 * <p>文件名称: Org.java</p>
 * <p>文件描述: 本类描述</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2011-10-21</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  chenyun
 */
public class Org implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -925745613109335550L;
	private int 	orgid;
	private String 	orgname;
	private String 	orglevel;
	private String 	orgtype;
	private String 	provinceNumber;
	private int 	parentorg;
	private int 	sort;
	private String 	isshow;
	private String  orgsbname;
	
	public String getOrgsbname() {
		return orgsbname;
	}
	public void setOrgsbname(String orgsbname) {
		this.orgsbname = orgsbname;
	}
	public int getOrgid() {
		return orgid;
	}
	public void setOrgid(int orgid) {
		this.orgid = orgid;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getOrglevel() {
		return orglevel;
	}
	public void setOrglevel(String orglevel) {
		this.orglevel = orglevel;
	}
	public String getOrgtype() {
		return orgtype;
	}
	public void setOrgtype(String orgtype) {
		this.orgtype = orgtype;
	}
	public String getProvinceNumber() {
		return provinceNumber;
	}
	public void setProvinceNumber(String provinceNumber) {
		this.provinceNumber = provinceNumber;
	}
	public int getParentorg() {
		return parentorg;
	}
	public void setParentorg(int parentorg) {
		this.parentorg = parentorg;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getIsshow() {
		return isshow;
	}
	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}
}

