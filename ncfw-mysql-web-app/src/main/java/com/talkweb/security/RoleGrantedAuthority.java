package com.talkweb.security;

import org.springframework.security.core.GrantedAuthority;

import com.talkweb.ncframework.modules.rights.role.rolemanage.entity.Role;

/**
 * <p>文件名称: RoleGrantedAuthority.java</p>
 * <p>文件描述: 角色权限</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>公　　司: 拓维信息系统股份有限公司</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2011-5-2</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  Wuqingming
 */
public class RoleGrantedAuthority extends Role implements GrantedAuthority {
	
	public RoleGrantedAuthority () {}
	
	public RoleGrantedAuthority (String roleId) {
		super.setRoleId(roleId);
	}
	
	public RoleGrantedAuthority (String roleId, String roleName) {
		super.setRoleId(roleId);
		super.setRoleName(roleName);
	}

	public String getAuthority() {
		return this.getRoleId();
	}

}

