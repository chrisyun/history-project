package org.thorn.configurator.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thorn.configurator.service.ConfiguratorService;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.web.controller.BaseController;
import org.thorn.web.entity.JsonResponse;
import org.thorn.web.entity.Status;

/** 
 * @ClassName: ConfigController 
 * @Description: 
 * @author chenyun
 * @date 2012-6-4 下午09:11:49 
 */
@Controller
@RequestMapping("/cf")
public class ConfigController extends BaseController {
	
	static Logger log = LoggerFactory.getLogger(ConfigController.class);
	
	@Autowired
	@Qualifier("configuratorService")
	private ConfiguratorService service;
	
	@RequestMapping("/getConfigName")
	@ResponseBody
	public List<Map<String,String>> getAvailableConfig() {
		Set<String> names = service.getConfigName();
		List<Map<String,String>> json = new ArrayList<Map<String,String>>();
		
		for(String name : names) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", name);
			json.add(map);
		}
		
		return json;
	}

	@RequestMapping("/getConfig")
	@ResponseBody
	public JsonResponse<Map<String,String>> getConfig(String name) {
		JsonResponse<Map<String,String>> json = new JsonResponse<Map<String,String>>();
		
		if(LocalStringUtils.isEmpty(name)) {
			json.setMessage("配置文件名称为空！");
			json.setSuccess(false);
		} else {
			try {
				Map<String, String> cf = service.getConfigProperty(name);
				json.setObj(cf);
			} catch (DocumentException e) {
				json.setMessage("获取配置文件内容发生错误："+ e.getMessage());
				json.setSuccess(false);
				log.error("getConfig[Map] - " + e.getMessage(), e);
			}
		}
		
		return json;
	}
	
	@RequestMapping("/modifyConfig")
	@ResponseBody
	public Status modifyConfig(String name, HttpServletRequest request) {
		Enumeration<String> parames = request.getParameterNames();
		Map<String, String> parameters = new HashMap<String, String>();
		
		while(parames.hasMoreElements()) {
			String key = parames.nextElement();
			parameters.put(key, request.getParameter(key));
		}
		
		Status status = new Status();
		
		try {
			service.modifyConfig(name, parameters);
			status.setMessage("更新配置文件成功！");
		} catch (Exception e) {
			status.setMessage("更新配置文件内容发生错误："+ e.getMessage());
			status.setSuccess(false);
			log.error("modifyConfig[Map] - " + e.getMessage(), e);
		}
		
		return status;
	}
	
	
}

