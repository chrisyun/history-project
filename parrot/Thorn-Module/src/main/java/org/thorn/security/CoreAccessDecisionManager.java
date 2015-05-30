package org.thorn.security;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.thorn.core.util.LocalStringUtils;

/**
 * AccessdecisionManager在Spring security中是很重要的。
 * 
 * 在验证部分简略提过了，所有的Authentication实现需要保存在一个GrantedAuthority对象数组中。 这就是赋予给主体的权限。
 * GrantedAuthority对象通过AuthenticationManager 保存到
 * Authentication对象里，然后从AccessDecisionManager读出来，进行授权判断。
 * 
 * Spring Security提供了一些拦截器，来控制对安全对象的访问权限，例如方法调用或web请求。
 * 一个是否允许执行调用的预调用决定，是由AccessDecisionManager实现的。 这个 AccessDecisionManager
 * 被AbstractSecurityInterceptor调用， 它用来作最终访问控制的决定。
 * 这个AccessDecisionManager接口包含三个方法：
 * 
 * void decide(Authentication authentication, Object secureObject,
 * List<ConfigAttributeDefinition> config) throws AccessDeniedException; boolean
 * supports(ConfigAttribute attribute); boolean supports(Class clazz);
 * 
 * 从第一个方法可以看出来，AccessDecisionManager使用方法参数传递所有信息，这好像在认证评估时进行决定。
 * 特别是，在真实的安全方法期望调用的时候，传递安全Object启用那些参数。 比如，让我们假设安全对象是一个MethodInvocation。
 * 很容易为任何Customer参数查询MethodInvocation，
 * 然后在AccessDecisionManager里实现一些有序的安全逻辑，来确认主体是否允许在那个客户上操作。
 * 如果访问被拒绝，实现将抛出一个AccessDeniedException异常。
 * 
 * 这个 supports(ConfigAttribute) 方法在启动的时候被
 * AbstractSecurityInterceptor调用，来决定AccessDecisionManager
 * 是否可以执行传递ConfigAttribute。 supports(Class)方法被安全拦截器实现调用，
 * 包含安全拦截器将显示的AccessDecisionManager支持安全对象的类型。
 */
public class CoreAccessDecisionManager implements AccessDecisionManager {

	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		
		StringBuilder needRoles = new StringBuilder();
		int scaler = 0;
		
		for (GrantedAuthority ga : authentication.getAuthorities()) {
			String userRole = ga.getAuthority().trim();
			
			if (LocalStringUtils.equals(userRole,
					SecurityConfiguration.SYS_ADMIN_ROLE)) {
				return;
			}
			
			for(ConfigAttribute ca : configAttributes) {
				String needRole = ((SecurityConfig) ca).getAttribute().trim();
				
				if (LocalStringUtils.equals(needRole, userRole)) {
					return;
				}
				
				if(scaler == 0) {
					needRoles.append(needRole).append(" ");
				}
			}
			
			// 匿名访问用户需要登录才能访问资源
			if (LocalStringUtils.equals(
					SecurityConfiguration.ANONY_MOUS_ROLE, userRole)
					&& configAttributes.size() == 1) {
				throw new InsufficientAuthenticationException("用户未登录！");
			}
			
			scaler++;
		}
		
		throw new AccessDeniedException("所需要的权限为：" + needRoles.toString());
		
		
//		if (configAttributes == null) {
//			return;
//		}

//		// 访问资源需要的权限集合
//		Iterator<ConfigAttribute> ite = configAttributes.iterator();
//
//		StringBuilder needRoles = new StringBuilder();
//
//		while (ite.hasNext()) {
//
//			ConfigAttribute ca = ite.next();
//			String needRole = ((SecurityConfig) ca).getAttribute().trim();
//
//			needRoles.append(needRole).append(" ");
//
//			// ga 为用户所被赋予的权限。 needRole 为访问相应的资源应该具有的权限。
//			for (GrantedAuthority ga : authentication.getAuthorities()) {
//
//				String userRole = ga.getAuthority().trim();
//
//				if (LocalStringUtils.equals(userRole,
//						SecurityConfiguration.SYS_ADMIN_ROLE)) {
//					return;
//				} else if (LocalStringUtils.equals(needRole, userRole)) {
//					return;
//				}
//
//				// 匿名访问用户需要登录才能访问资源
//				if (LocalStringUtils.equals(
//						SecurityConfiguration.ANONY_MOUS_ROLE, userRole)
//						&& configAttributes.size() == 1) {
//					throw new InsufficientAuthenticationException("用户未登录！");
//				}
//
//			}
//		}
	}

	public boolean supports(ConfigAttribute attribute) {

		return true;

	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

}
