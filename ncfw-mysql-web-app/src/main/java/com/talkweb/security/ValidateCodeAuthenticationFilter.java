package com.talkweb.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.TextEscapeUtils;
import org.springframework.util.Assert;

import com.octo.captcha.service.CaptchaService;

public class ValidateCodeAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {
	private boolean postOnly = true;
	private CaptchaService captchaService;
	private boolean allowEmptyValidateCode = false;
	private String sessionvalidateCodeField = DEFAULT_SESSION_VALIDATE_CODE_FIELD;
	private String validateCodeParameter = DEFAULT_VALIDATE_CODE_PARAMETER;
	public static final String DEFAULT_SESSION_VALIDATE_CODE_FIELD = "validateCode";
	public static final String DEFAULT_VALIDATE_CODE_PARAMETER = "validateCode";

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}
		username = username.trim();
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		HttpSession session = request.getSession(false);
		if (session != null || getAllowSessionCreation()) {
			request.getSession().setAttribute(	SPRING_SECURITY_LAST_USERNAME_KEY,
												TextEscapeUtils.escapeEntities(username));
		}
		setDetails(request, authRequest);
		// check validate code
		if (!isAllowEmptyValidateCode()) {
			checkValidateCode(request, response);
		}
		return this.getAuthenticationManager().authenticate(authRequest);
	}
	
    protected void checkValidateCode(HttpServletRequest request, HttpServletResponse response) { 
    	/*
    	Assert.notNull(captchaService, "captchaService can not be null");
    	String captchaID 	= request.getSession(true).getId();
    	String captchaValue	= request.getParameter(DEFAULT_VALIDATE_CODE_PARAMETER);
    	boolean flag 		= false;
    	try {
    		flag = captchaService.validateResponseForID(captchaID, captchaValue);
    	} catch (Exception e) {
    		e.printStackTrace();
    		flag = false;
		}*/
    	
    	String challengeResponse = request.getParameter("validateCode");
		String sessionNum = (String) request.getSession().getAttribute("randomValidateCode");
		boolean flag = StringUtils.equals(sessionNum, challengeResponse);
    	
        if (!flag) {
        	AjaxAuthenticationHelper.proccessAjaxRequest(false, "验证码输入错误.", request, response);
        	request.getSession().setAttribute("msg", "验证码输入错误.");
            throw new AuthenticationServiceException("验证码错误.");  
        } 
        
        request.getSession().removeAttribute("msg");
    }


	public boolean isPostOnly() {
		return postOnly;
	}
	@Override
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public boolean isAllowEmptyValidateCode() {
		return allowEmptyValidateCode;
	}

	public void setAllowEmptyValidateCode(boolean allowEmptyValidateCode) {
		this.allowEmptyValidateCode = allowEmptyValidateCode;
	}

	public CaptchaService getCaptchaService() {
		return captchaService;
	}

	public void setCaptchaService(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	public String getSessionvalidateCodeField() {
		return sessionvalidateCodeField;
	}

	public void setSessionvalidateCodeField(String sessionvalidateCodeField) {
		this.sessionvalidateCodeField = sessionvalidateCodeField;
	}

	public String getValidateCodeParameter() {
		return validateCodeParameter;
	}

	public void setValidateCodeParameter(String validateCodeParameter) {
		this.validateCodeParameter = validateCodeParameter;
	}

}
