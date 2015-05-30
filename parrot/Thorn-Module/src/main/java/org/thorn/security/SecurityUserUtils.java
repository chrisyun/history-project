package org.thorn.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.thorn.auth.service.IAuthService;
import org.thorn.core.context.SpringContext;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.org.entity.Org;
import org.thorn.org.service.IOrgService;
import org.thorn.security.entity.UserSecurity;
import org.thorn.user.entity.User;

/**
 * @ClassName: SecurityUserUtil
 * @Description: security User工具类，提供对session中的user数据获取方法
 * @author chenyun
 * @date 2012-5-6 下午10:56:26
 */
public class SecurityUserUtils {
	
	static Logger log = LoggerFactory.getLogger(SecurityUserUtils.class);
	
	/**
	 * 
	 * @Description：获取当前session中的user用户
	 * @author：chenyun 	        
	 * @date：2012-5-25 下午01:41:23
	 * @return
	 */
	public static User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		if (auth == null) {
			return null;
		}

		UserSecurity us = (UserSecurity) auth.getPrincipal();
		User user = us.getUser();
		return user;
	}
	
	/**
	 * 
	 * @Description：获取用户的角色列表
	 * @author：chenyun 	        
	 * @date：2012-5-25 下午01:41:41
	 * @return
	 */
	public static List<String> getRoleList() {
		List<String> list = new ArrayList<String>();

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		if (auth == null) {
			return list;
		}

		UserSecurity us = (UserSecurity) auth.getPrincipal();
		Collection<GrantedAuthority> authorities = us.getAuthorities();

		for (GrantedAuthority authority : authorities) {
			list.add(authority.getAuthority());
		}

		return list;
	}
	
	/**
	 * 
	 * @Description：获取用户的资源集合
	 * @author：chenyun 	        
	 * @date：2012-6-1 下午04:27:23
	 * @return
	 */
	public static List<String> getSoucrceList() {
		List<String> roleList = getRoleList();
		IAuthService service = SpringContext.getBean("authService");
		
		List<String> source = new ArrayList<String>();
		try {
			source = service.queryResourceByRole(roleList);
		} catch (Exception e) {
			log.error("query user auth exception", e);
		}
		
		return source;
	}
	
	/**
	 * 
	 * @Description：判断用户是否系统管理员
	 * @author：chenyun 	        
	 * @date：2012-5-25 下午01:43:12
	 * @return
	 */
	public static boolean isSysAdmin() {
		List<String> list = getRoleList();

		boolean isAdmin = false;
		for (String role : list) {
			if (LocalStringUtils.equals(SecurityConfiguration.SYS_ADMIN_ROLE, role)
					|| LocalStringUtils.equals(SecurityConfiguration.ADMIN_ROLE, role)) {
				isAdmin = true;
				break;
			}
		}

		return isAdmin;
	}
	
	public static Org getUserParentOrg() {
		User user = getCurrentUser();
		Org org = null;
		
		try {
			IOrgService service = SpringContext.getBean("orgService");
			org = service.queryParentOrg(user.getOrgCode());
		} catch (DBAccessException e) {
			log.error("query parent org exception", e);
		}
		
		return org;
	}

}
