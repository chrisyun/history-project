package org.thorn.core.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/** 
 * @ClassName: SpringContext 
 * @Description:
 * @author chenyun
 * @date 2012-4-26 上午10:57:30 
 */
public class SpringContext implements ApplicationContextAware {
	
	static Logger log = LoggerFactory.getLogger(SpringContext.class);
	
	private static ApplicationContext applicationContext = null;
	
	public synchronized void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		log.debug("注入ApplicationContext到SpringContext:" + applicationContext);
		
		if(this.applicationContext != null) {
			log.warn("SpringContext中的ApplicationContext被覆盖");
		}
		
		if(applicationContext == null) {
			log.warn("the args applicationContext is null");
		} else {
			this.applicationContext = applicationContext;
		}
	}
	
	public synchronized void setApplicationContext(String... configLocations) {
		
		log.debug("注入ApplicationContext到SpringContext:" + applicationContext);

		if (this.applicationContext != null) {
			log.warn("SpringContext中的ApplicationContext被覆盖");
		}

		this.applicationContext = new ClassPathXmlApplicationContext(configLocations);
	}
	
	public static ApplicationContext getApplicationContext() {
		
		return applicationContext;
	}
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-4-26 上午11:12:16
	 * @param <T>
	 * @param name bean name
	 * @return
	 */
	public static <T> T getBean(String name) {
		Assert.notNull(applicationContext);
		return (T) applicationContext.getBean(name);
	}

}

