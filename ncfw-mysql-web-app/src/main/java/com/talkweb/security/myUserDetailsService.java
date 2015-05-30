package com.talkweb.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.talkweb.ncframework.pub.dao.IGenericDAO;
import com.talkweb.ncframework.pub.utils.spring.SpringContextUtils;
import com.talkweb.ncfw.entity.Org;
import com.talkweb.ncfw.entity.User;

public class myUserDetailsService implements UserDetailsService {

	private Logger logger = Logger.getLogger(this.getClass());
	
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
	    
			/** 获取用户详细信息 */
		    IGenericDAO genericDAO = (IGenericDAO) SpringContextUtils.getBean("genericDAO");
		    Map para = new HashMap();
		    para.put("userid", username.toUpperCase());
		    List<User> userList = genericDAO.queryList("UserMapper.query", para);
		    if (userList == null && userList.size() == 0) {	
		    	return null;
		    }
		    User user = userList.get(0);
		    
		    para.put("orgid", user.getCitynumber());
		    List<Org> orgList = genericDAO.queryList("OrgMapper.query", para);
		    if (orgList == null && orgList.size() == 0) {	
		    	return null;
		    }
		    Org org = orgList.get(0);
		    //para.put("usermobile", user.getMobile());
		    
		    Collection<RoleGrantedAuthority> authorities = new ArrayList<RoleGrantedAuthority>();
		    RoleGrantedAuthority roleGrantedAuthority = new RoleGrantedAuthority(user.getRoleid(), "");
		    authorities.add(roleGrantedAuthority);
		    
			UserSession userSession = new UserSession(username, "", true, true,
					true, true, authorities);
			userSession.setUser(user);
			userSession.setOrg(org);
	        return userSession;
	}


}
