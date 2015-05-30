package com.talkweb.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

/**
 * <p>文件名称: MyAuthenticationSuccessHandler.java</p>
 * <p>文件描述: 认证成功回调类</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>公　　司: 拓维信息系统股份有限公司</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2011-5-23</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  Wuqingming
 */
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	 protected final static Logger logger = Logger.getLogger(MyAuthenticationSuccessHandler.class);

	 private RequestCache requestCache = new HttpSessionRequestCache();

	 @Override
	 public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
		boolean isAjaxAuth = AjaxAuthenticationHelper.proccessAjaxRequest(true, "", request, response);
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null && !isAjaxAuth) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        if (	!isAjaxAuth &&
        		(isAlwaysUseDefaultTargetUrl() || StringUtils.hasText(request.getParameter(getTargetUrlParameter())))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }

        clearAuthenticationAttributes(request);
        
        if (isAjaxAuth) {
        	return;
        }
        // Use the DefaultSavedRequest URL
        String targetUrl = savedRequest.getRedirectUrl();
        logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
	 }

	 public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
	 }
}

