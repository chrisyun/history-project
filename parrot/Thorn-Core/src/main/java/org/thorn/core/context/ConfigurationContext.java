package org.thorn.core.context;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

/**
 * @ClassName: ConfigurationContext
 * @Description:
 * @author chenyun
 * @date 2012-5-30 上午11:44:45
 */
public class ConfigurationContext extends PropertyPlaceholderConfigurer {
	
	private Resource[] locations;
	
	public ConfigurationContext() {
		super();
	}

	public Properties getProperties() throws IOException {
		return this.mergeProperties();
	}
	
	public void setLocation(Resource location) {
		super.setLocation(location);
		this.locations = new Resource[] {location};
	}
	
	public void setLocations(Resource[] locations) {
		super.setLocations(locations);
		this.locations = locations;
	}
	
	public Resource[] getResource() {
		return this.locations;
	}
	
}
