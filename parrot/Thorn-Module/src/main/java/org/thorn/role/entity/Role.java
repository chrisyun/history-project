package org.thorn.role.entity;

import java.io.Serializable;

/** 
 * @ClassName: Role 
 * @Description: 
 * @author chenyun
 * @date 2012-5-4 下午03:16:12 
 */
public class Role implements Serializable {

	/** */
	private static final long serialVersionUID = 4289803425268912580L;
	
	private String roleCode;
	
	private String roleName;
	
	private String roleDesc;
	
	private String isDisabled;

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(String isDisabled) {
		this.isDisabled = isDisabled;
	}
	
	
}

