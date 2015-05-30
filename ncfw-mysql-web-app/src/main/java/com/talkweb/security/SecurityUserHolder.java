package com.talkweb.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUserHolder {
	public static UserSession getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal == null || !(principal instanceof UserSession) ) {
			return null;
		}
		return (UserSession) principal;
	}
}
