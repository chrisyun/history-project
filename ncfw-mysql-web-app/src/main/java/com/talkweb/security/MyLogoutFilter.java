package com.talkweb.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * <p>文件名称: MyLogoutFilter.java</p>
 * <p>文件描述: 注销过滤器</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>公　　司: 拓维信息系统股份有限公司</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2011-5-24</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  Wuqingming
 */
public class MyLogoutFilter extends LogoutFilter {
    private List<LogoutHandler> handlers;
    private LogoutSuccessHandler logoutSuccessHandler;

	public MyLogoutFilter(LogoutSuccessHandler logoutSuccessHandler,
			LogoutHandler... handlers) {
		super(logoutSuccessHandler, handlers);
		this.handlers = Arrays.asList(handlers);
        this.logoutSuccessHandler = logoutSuccessHandler;
	}
	
	 public MyLogoutFilter(String logoutSuccessUrl, LogoutHandler... handlers) {
		 super(logoutSuccessUrl, handlers);
		 Assert.notEmpty(handlers, "LogoutHandlers are required");
		 this.handlers = Arrays.asList(handlers);
		 Assert.isTrue(!StringUtils.hasLength(logoutSuccessUrl) ||
                UrlUtils.isValidRedirectUrl(logoutSuccessUrl), logoutSuccessUrl + " isn't a valid redirect URL");
		 SimpleUrlLogoutSuccessHandler urlLogoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
		 if (StringUtils.hasText(logoutSuccessUrl)) {
            urlLogoutSuccessHandler.setDefaultTargetUrl(logoutSuccessUrl);
		 }
		 logoutSuccessHandler = urlLogoutSuccessHandler;
	 }
	 
	 public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
     	throws IOException, ServletException {
		 HttpServletRequest request = (HttpServletRequest) req;
		 HttpServletResponse response = (HttpServletResponse) res;
		
		 if (requiresLogout(request, response)) {
		     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		     if (logger.isDebugEnabled()) {
		         logger.debug("Logging out user '" + auth + "' and transferring to logout destination");
		     }
		
		     for (LogoutHandler handler : handlers) {
		         handler.logout(request, response, auth);
		     }
		
		     logoutSuccessHandler.onLogoutSuccess(request, response, auth);
		     boolean isAjaxAuth = AjaxAuthenticationHelper.proccessAjaxRequest(true, "", request, response);
		     return;
		 }
		 chain.doFilter(request, response);
	 }
	 
}
