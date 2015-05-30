package org.thorn.configurator.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.thorn.core.context.ConfigurationContext;
import org.thorn.core.context.SpringContext;

/**
 * @ClassName: ConfiguratorService
 * @Description:
 * @author chenyun
 * @date 2012-5-30 上午11:26:18
 */
public class ConfiguratorService extends Observable {

	static Logger log = LoggerFactory.getLogger(ConfiguratorService.class);

	private volatile Map<String, File> configs = new HashMap<String, File>();
	
	public ConfiguratorService(List<Observer> servers) {
		super();
		for(Observer server : servers) {
			super.addObserver(server);
		}
	}
	
	private synchronized void initConfigs() {
		if (configs == null || configs.size() == 0) {
			ConfigurationContext configurer = SpringContext
					.getBean("propertyConfigurer");
			Resource[] resource = configurer.getResource();

			for (Resource res : resource) {
				try {
					configs.put(res.getFilename(), res.getFile());
				} catch (IOException e) {
					log.warn("get resource file error : {}", res.getFilename());
				}
			}
		}
	}

	public Set<String> getConfigName() {
		if (configs == null || configs.size() == 0) {
			initConfigs();
		}

		Set<String> configNames = configs.keySet();
		return configNames;
	}

	public Map<String, String> getConfigProperty(String name)
			throws DocumentException {
		if (configs == null || configs.size() == 0) {
			initConfigs();
		}

		File config = configs.get(name);
		Map<String, String> property = new HashMap<String, String>();

		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(config);
		Element root = doc.getRootElement();

		Element comment = root.element("comment");
		property.put("comment", comment.getStringValue());

		List<Element> entryList = root.elements("entry");
		for (Element entry : entryList) {
			property.put(entry.attributeValue("key"), entry.getStringValue());
		}

		return property;
	}

	public synchronized void modifyConfig(String name,
			Map<String, String> property) throws DocumentException, IOException {
		if (configs == null || configs.size() == 0) {
			initConfigs();
		}

		SAXReader saxReader = new SAXReader();

		File config = configs.get(name);
		Document doc = saxReader.read(config);
		Element root = doc.getRootElement();

		Element comment = root.element("comment");
		comment.setText(property.get("comment"));

		List<Element> entryList = root.elements("entry");
		for (Element entry : entryList) {
			String key = entry.attributeValue("key");
			entry.setText(property.get(key));
		}

		OutputFormat format = new OutputFormat("    ", true);
		XMLWriter xmlWriter = new XMLWriter(new FileWriter(config), format);

		xmlWriter.write(doc);
		xmlWriter.close();
		
		super.setChanged();
		super.notifyObservers(property);
	}

}
