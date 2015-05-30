package org.thorn.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntUrlPathMatcher;
import org.springframework.security.web.util.UrlMatcher;
import org.thorn.auth.service.IAuthService;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.resource.entity.Resource;
import org.thorn.resource.service.IResourceService;
import org.thorn.role.entity.Role;

/**
 * 
 * @ClassName: InvocationSecurityMetadataSource
 * @Description: 最核心的地方，就是提供某个资源对应的权限定义，即getAttributes方法返回的结果。
 *               此类在初始化时，应该取到所有资源及其对应角色的定义。
 * @author chenyun
 * @date 2012-5-5 下午08:40:08
 * 
 */
public class InvocationSecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {

	static Logger log = LoggerFactory
			.getLogger(InvocationSecurityMetadataSource.class);

	private UrlMatcher urlMatcher = new AntUrlPathMatcher();

	/**
	 * 资源ID与资源URL关系map，key为ID,URL为value
	 */
	private static Map<String, String> resourceMap = null;
	
	/**
	 * 上次加载资源的时间
	 */
	private volatile long lastReloadDate = 0L;
	
	/**
	 * 重新加载资源的时间，默认为10分钟
	 */
	private long raloadTime = 1000*60*10;
	
	private boolean needReload = false;
	
	private IAuthService authService;

	private IResourceService resourceService;

	/**
	 * 初始化该类时，加载资源列表URL与资源ID
	 * 
	 * @throws DBAccessException
	 */
	public InvocationSecurityMetadataSource(IResourceService resourceService,
			IAuthService authService) throws DBAccessException {

		resourceMap = new HashMap<String, String>();
		this.resourceService = resourceService;
		this.authService = authService;

		loadSource();
	}
	
	/**
	 * 
	 * @Description：加载资源到map中
	 * @author：chenyun 	        
	 * @date：2012-6-1 下午05:06:29
	 * @throws DBAccessException
	 */
	private synchronized void loadSource() throws DBAccessException {
		
		if(System.currentTimeMillis() - lastReloadDate < raloadTime) {
			return ;
		}
		// 刷新修改时间
		lastReloadDate = System.currentTimeMillis();
		
		List<Resource> sources = this.resourceService.queryAllLeaf();
		
		resourceMap.clear();
		for (Resource source : sources) {
			if(LocalStringUtils.isNotEmpty(source.getSourceUrl())) {
				resourceMap.put(source.getSourceCode(), source.getSourceUrl());
			}
		}
	}
	
	/**
	 * 
	 * @Description：根据url在map中找到对应的sourceCode
	 * @author：chenyun 	        
	 * @date：2012-6-1 下午05:06:52
	 * @param url
	 * @return
	 */
	public List<String> getSourceCodeByUrl(String url) {
		List<String> source = new ArrayList<String>();
		for (String id : resourceMap.keySet()) {
			if (urlMatcher.pathMatchesUrl(resourceMap.get(id), url)) {
				source.add(id);
			}
		}
		
		return source;
	}
	
	
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	/**
	 * 根据URL，找到相关的角色。
	 */
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {

		// object 是一个URL，被用户请求的url。
		String url = ((FilterInvocation) object).getRequestUrl();

		int firstQuestionMarkIndex = url.indexOf("?");

		if (firstQuestionMarkIndex != -1) {
			url = url.substring(0, firstQuestionMarkIndex);
		}
		
		// 判断是否需要重新加载资源，默认启动加载即可
		if(needReload && System.currentTimeMillis() - lastReloadDate >= raloadTime) {
			try {
				loadSource();
			} catch (DBAccessException e) {
				log.error("reload source exception", e);
			}
		}
		
		Collection<ConfigAttribute> collection = new ArrayList<ConfigAttribute>();
		List<String> source = getSourceCodeByUrl(url);
		
		if (source.size() > 0) {
			try {
				List<Role> roles = authService.queryRoleBySource(source);

				for (Role role : roles) {
					ConfigAttribute ca = new SecurityConfig(role.getRoleCode());
					collection.add(ca);
				}
			} catch (DBAccessException e) {
				log.error("Role Query Exception", e);
			}
		}

		return collection;
	}
	
	public void setRaloadTime(long raloadTime) {
		this.raloadTime = raloadTime;
	}
	
	public void setNeedReload(boolean needReload) {
		this.needReload = needReload;
	}

	public boolean supports(Class<?> arg0) {
		return true;
	}
}
