package com.talkweb.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * <p>文件名称: 题目名称</p>
 * <p>文件描述: 本类描述</p>
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
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	 public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
	            AuthenticationException exception) throws IOException, ServletException {
		 boolean isAjaxAuth = AjaxAuthenticationHelper.proccessAjaxRequest(false, "用户名与密码错误.", request, response);
		 if (isAjaxAuth) {
			 return;
		 }
		 super.onAuthenticationFailure(request, response, exception);
	 }
}

