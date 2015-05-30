package org.thorn.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.TextEscapeUtils;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.security.SecurityConfiguration;

public class LoginAuthWithCodeFilter extends
		UsernamePasswordAuthenticationFilter {

	private boolean allowEmptyValidateCode = false;
	private String sessionvalidateCodeField = SecurityConfiguration.AUTHCODE_SESSION_ID;
	private String validateCodeParameter = SecurityConfiguration.AUTHCODE_FROM_ID;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		
		String username = obtainUsername(request);
		if (username == null) {
            username = "";
        }
		
		username = username.trim();
		 
        HttpSession session = request.getSession(false);

        if (session != null || getAllowSessionCreation()) {
            request.getSession().setAttribute(SPRING_SECURITY_LAST_USERNAME_KEY, TextEscapeUtils.escapeEntities(username));
        }
		
		if (!isAllowEmptyValidateCode())
			checkValidateCode(request);
		
		return super.attemptAuthentication(request, response);
		
	}

	/**
	 * 
	 * @Description：比较session中的验证码和用户输入的验证码是否相等
	 * @author：chenyun 	        
	 * @date：2012-5-6 下午04:06:50
	 * @param request
	 */
	private void checkValidateCode(HttpServletRequest request) {
		String sessionValidateCode = obtainSessionValidateCode(request);
		String validateCodeParameter = obtainValidateCodeParameter(request);
		if (LocalStringUtils.isEmpty(validateCodeParameter)
				|| !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
			throw new AuthenticationServiceException(
					messages.getMessage("validateCode.notEquals"));
		}
	}

	private String obtainValidateCodeParameter(HttpServletRequest request) {
		String code = request.getParameter(validateCodeParameter);
		if (LocalStringUtils.isNotEmpty(code)) {
			code = code.toUpperCase();
		}
		return code;
	}

	private String obtainSessionValidateCode(HttpServletRequest request) {
		Object obj = request.getSession()
				.getAttribute(sessionvalidateCodeField);
		return null == obj ? "" : obj.toString().toUpperCase();
	}

	public String getValidateCodeName() {
		return sessionvalidateCodeField;
	}

	public void setValidateCodeName(String validateCodeName) {
		this.sessionvalidateCodeField = validateCodeName;
	}

	public boolean isAllowEmptyValidateCode() {
		return allowEmptyValidateCode;
	}

	public void setAllowEmptyValidateCode(boolean allowEmptyValidateCode) {
		this.allowEmptyValidateCode = allowEmptyValidateCode;
	}

}
