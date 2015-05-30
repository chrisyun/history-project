package org.thorn.user.entity;

import java.io.Serializable;

/** 
 * @ClassName: User 
 * @Description: 
 * @author chenyun
 * @date 2012-5-4 下午02:18:24 
 */
public class User implements Serializable {

	/** */
	private static final long serialVersionUID = 2309227663174907942L;
	
	private String userId;
	
	private String userName;
	
	private String sn;
	
	private String userAccount;
	
	private String userPwd;
	
	private String gender;
	
	private String cumail;
	
	private String phone;
	
	private String orgCode;
	
	private String orgType;
	
	private String defaultRole;
	
	private String isShow;
	
	private String isDisabled;
	
	private int sortNum;

	private String orgName;
	
	private String area;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCumail() {
		return cumail;
	}

	public void setCumail(String cumail) {
		this.cumail = cumail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(String defaultRole) {
		this.defaultRole = defaultRole;
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

	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
}

