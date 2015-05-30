package org.thorn.configurator.listeners;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.thorn.core.util.LocalStringUtils;

/** 
 * @ClassName: MailConfiguratorListener 
 * @Description: 
 * @author chenyun
 * @date 2012-6-5 下午11:46:20 
 */
public class MailConfiguratorListener implements Observer {
	
	private String configName = "mail-config.xml";
	
	public void update(Observable o, Object arg) {
		if(!(arg instanceof Map)) {
			return ;
		}
		
		Map<String, String> map = (Map<String, String>) arg;
		
		String name = map.get("name");
		
		if(LocalStringUtils.equals(configName, name)) {
			// do update bean
			System.out.println(" hello,i'm must update");
		}
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}
}

