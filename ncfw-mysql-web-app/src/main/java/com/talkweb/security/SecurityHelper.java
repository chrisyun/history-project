package com.talkweb.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.talkweb.ncframework.modules.rights.role.rolemanage.entity.Role;

/**
 * <p>文件名称: SecurityHelper.java</p>
 * <p>文件描述: 安全管理帮助类</p>
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
public class SecurityHelper {

	/**
	 * 系统管理员
	 */
	public static final String SYSADMIN 	= "sysadmin";
	
	/**
	 * 普通注册用户
	 */
	public static final String COMMON_USER 	= "commonuser";
	
	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2011-5-2
	 * @Description：获取当前用户session信息
	 * @return
	 */
	public static UserSession getCurrentUser () {
		return SecurityUserHolder.getCurrentUser();
	}
	
	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2011-5-2
	 * @Description：获取当前用户角色信息
	 * @return
	 */
	public static List<Role> getCurrentUserRoles () {
		UserSession userSession = getCurrentUser();
		if (userSession == null) {
			return null;
		}
		Collection authorities = getCurrentUser().getAuthorities();
		if (CollectionUtils.isEmpty(authorities)) {
			return null;
		}
		Iterator iter = authorities.iterator();
		List<Role> roleList = new ArrayList<Role>();
		while (iter.hasNext()) {
			roleList.add((Role)iter.next());
		}
		return roleList;
	}

	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2011-5-2
	 * @Description：判断是否拥有系统管理员角色
	 * @return
	 */
	public static boolean hasSysadminRight () {
		List<Role> roleList = getCurrentUserRoles();
		if (CollectionUtils.isEmpty(roleList)) {
			return false;
		}
		for (Role role : roleList) {
			if (StringUtils.equals(SYSADMIN, role.getRoleId())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2011-5-2
	 * @Description：判断是否为普通用户
	 * @return
	 */
	public static boolean isCommonUser () {
		List<Role> roleList = getCurrentUserRoles();
		if (CollectionUtils.isEmpty(roleList)) {
			return true;
		}
		for (Role role : roleList) {
			if (StringUtils.equals(COMMON_USER, role.getRoleId())) {
				return true;
			}
		}
		return false;
	}
}

