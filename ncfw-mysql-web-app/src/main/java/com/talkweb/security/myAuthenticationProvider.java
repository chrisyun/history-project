package com.talkweb.security;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.lang.xwork.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;

import com.talkweb.ncframework.pub.utils.encode.EncoderUtils;

public class myAuthenticationProvider implements AuthenticationProvider,
		InitializingBean, MessageSourceAware {

	protected MessageSourceAccessor messages = SpringSecurityMessageSource
			.getAccessor();
	private UserCache userCache = new NullUserCache();
	private boolean forcePrincipalAsString = false;
	protected boolean hideUserNotFoundExceptions = true;
	private UserDetailsService userDetailsService;
	private SqlSessionTemplate sqlSessionTemplate;

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		System.out.println("begin to excute myAuthenticationProvider authenticate...");
		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED"
				: authentication.getName();
		boolean cacheWasUsed = true;
		UserDetails user = this.userCache.getUserFromCache(username);
		if (user == null) {
			cacheWasUsed = false;
			try {
				user = retrieveUser(username, authentication.getCredentials()
						.toString());
			} catch (UsernameNotFoundException notFound) {
				if (hideUserNotFoundExceptions) {
					throw new BadCredentialsException(
						messages.getMessage(
								"AbstractUserDetailsAuthenticationProvider.badCredentials",
								"Bad credentials"
						)
					);
				} else {
					throw notFound;
				}
			}

		}
		if (!cacheWasUsed) {
			this.userCache.putUserInCache(user);
		}
		Object principalToReturn = user;
		if (forcePrincipalAsString) {
			principalToReturn = user.getUsername();
		}
		return createSuccessAuthentication(principalToReturn, authentication,
				user);
	}

	private Authentication createSuccessAuthentication(
			Object principalToReturn, Authentication authentication,
			UserDetails user) {
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
				principalToReturn, authentication.getCredentials(), user
						.getAuthorities());
		result.setDetails(authentication.getDetails());
		return result;
	}

	private UserDetails retrieveUser(String username, String password) {
		UserDetails loadedUser;
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {	//用户名与密码不能为空
			throw new BadCredentialsException(
			"UserDetailsService returned null, which is an interface contract violation");
		}
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("userid", username.toUpperCase());
		hashmap.put("status", "enable");
		try {
			hashmap.put("userpwd", EncoderUtils.base64Encode(password));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (Long.valueOf(this.getSqlSessionTemplate().selectOne(
				"UserMapper.getCount", hashmap).toString()) <= 0) {
			throw new BadCredentialsException(
					"UserDetailsService returned null, which is an interface contract violation");
		}
		try {
			loadedUser = this.getUserDetailsService().loadUserByUsername(
					username);
		} catch (DataAccessException repositoryProblem) {
			throw new AuthenticationServiceException(repositoryProblem
					.getMessage(), repositoryProblem);
		}
		if (loadedUser == null) {
			throw new AuthenticationServiceException(
					"UserDetailsService returned null, which is an interface contract violation");
		}
		UserSession userSession = (UserSession) loadedUser;
		
		if (userSession.getAuthorities() == null || userSession.getAuthorities().size() == 0) {//无权限
			throw new AuthenticationServiceException(
				"UserDetailsService returned null, which is an interface contract violation");
		}
		return loadedUser;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class authentication) {
		return (UsernamePasswordAuthenticationToken.class
				.isAssignableFrom(authentication));
	}

	public void afterPropertiesSet() throws Exception {

	}

	public void setMessageSource(MessageSource messageSource) {
		this.messages = new MessageSourceAccessor(messageSource);
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setHideUserNotFoundExceptions(boolean hideUserNotFoundExceptions) {
		this.hideUserNotFoundExceptions = hideUserNotFoundExceptions;
	}

	public boolean isHideUserNotFoundExceptions() {
		return hideUserNotFoundExceptions;
	}

	/**
	 * @param sqlSessionTemplate
	 *            the sqlSessionTemplate to set
	 */
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	/**
	 * @return the sqlSessionTemplate
	 */
	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}
}
