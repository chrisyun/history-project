package org.thorn.org.entity;

import java.io.Serializable;

/** 
 * @ClassName: Org 
 * @Description: 
 * @author chenyun
 * @date 2012-5-9 下午10:13:56 
 */
public class Org implements Serializable {

	/** */
	private static final long serialVersionUID = -1039376828336559675L;
	
	private Integer orgId;
	
	private String orgCode;
	
	private String orgName;
	
	private String parentOrg;
	
	private String showName;
	
	private String orgType;
	
	private String orgMail;
	
	private String isShow;
	
	private String isDisabled;
	
	private String area;
	
	private int sortNum;

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getParentOrg() {
		return parentOrg;
	}

	public void setParentOrg(String parentOrg) {
		this.parentOrg = parentOrg;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOrgMail() {
		return orgMail;
	}

	public void setOrgMail(String orgMail) {
		this.orgMail = orgMail;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(String isDisabled) {
		this.isDisabled = isDisabled;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}
	
	
}

