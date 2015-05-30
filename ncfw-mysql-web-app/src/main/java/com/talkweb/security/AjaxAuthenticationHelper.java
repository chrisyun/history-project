package com.talkweb.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

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
public class AjaxAuthenticationHelper {
	private static String ajaxKey = "ajax";

	public static boolean proccessAjaxRequest (boolean ifSuccess, String msg, 
			HttpServletRequest request, HttpServletResponse response) {
		 boolean result = false;
		 if (isAjaxRequest(request)) {
			 result = true;
			 try {
				PrintWriter pw = response.getWriter();
				pw.println("{success: " + ifSuccess + ", msg: '" + msg + "'}");
				pw.flush();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
		 return result;
	 }
	
	private static boolean isAjaxRequest(final HttpServletRequest request) {
		String ajaxRequest = request.getHeader("X-Requested-With");
		if (StringUtils.hasText(ajaxRequest)) {
			return ajaxRequest.equals("XMLHttpRequest");
		}
		ajaxRequest = request.getHeader(ajaxKey);
		if (StringUtils.hasText(ajaxRequest)) {
			return true;
		}

		if (request instanceof HttpServletRequestWrapper) {
			ajaxRequest = ((HttpServletRequestWrapper) request).getRequest().getParameter(ajaxKey);
		} else {
			ajaxRequest = request.getParameter(ajaxKey);
		}
		if (StringUtils.hasText(ajaxRequest)) {
			return true;
		}
		return false;
	}
}

