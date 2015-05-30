package com.talkweb.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.talkweb.ncfw.entity.Org;

/**
 * <p>文件名称: UserSession.java</p>
 * <p>文件描述: 用户登录Session实体类</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>公　　司: 拓维信息系统股份有限公司</p>
 * <p>内容摘要: 用户登录Session实体类</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2010-9-8</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  成刚
 */
public class UserSession extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5833144863868984254L;
	
	public UserSession(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		// TODO Auto-generated constructor stub
	}
	
	private com.talkweb.ncfw.entity.User user;			//系统用户信息

	private Org org;
	
	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public com.talkweb.ncfw.entity.User getUser() {
		return user;
	}

	public void setUser(com.talkweb.ncfw.entity.User user) {
		this.user = user;
	}

}

