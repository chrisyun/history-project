package org.thorn.log.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thorn.web.controller.BaseController;
import org.thorn.web.entity.Status;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

/**
 * @ClassName: SysLogController
 * @Description:
 * @author chenyun
 * @date 2012-6-10 下午09:56:17
 */
@Controller
@RequestMapping("/log")
public class SysLogController extends BaseController {

	Logger log = LoggerFactory.getLogger(SysLogController.class);
	
	@RequestMapping("/modifyLogLevel")
	@ResponseBody
	public Status modifyLogLevel(int level) {
		Status status = new Status();

		try {
			LoggerContext context = (LoggerContext) LoggerFactory
					.getILoggerFactory();
			ch.qos.logback.classic.Logger rootLogger = context
					.getLogger(Logger.ROOT_LOGGER_NAME);
			rootLogger.setLevel(Level.toLevel(level));
			status.setMessage("修改日志级别成功！");
		} catch (Exception e) {
			status.setSuccess(false);
			status.setMessage("修改日志级别失败：" + e.getMessage());
			log.error("modifyLogLevel[int] - " + e.getMessage(), e);
		}

		return status;
	}
	
	@RequestMapping("/getLogLevel")
	@ResponseBody
	public List<Map<String, Object>> getLogBackLevel() {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Map<String, Object> all = new HashMap<String, Object>();
		all.put("name", Level.ALL.toString());
		all.put("id", Level.ALL.toInteger());
		list.add(all);
		
		Map<String, Object> trace = new HashMap<String, Object>();
		trace.put("name", Level.TRACE.toString());
		trace.put("id", Level.TRACE.toInteger());
		list.add(trace);
		
		Map<String, Object> debug = new HashMap<String, Object>();
		debug.put("name", Level.DEBUG.toString());
		debug.put("id", Level.DEBUG.toInteger());
		list.add(debug);
		
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("name", Level.INFO.toString());
		info.put("id", Level.INFO.toInteger());
		list.add(info);
		
		Map<String, Object> warn = new HashMap<String, Object>();
		warn.put("name", Level.WARN.toString());
		warn.put("id", Level.WARN.toInteger());
		list.add(warn);
		
		Map<String, Object> error = new HashMap<String, Object>();
		error.put("name", Level.ERROR.toString());
		error.put("id", Level.ERROR.toInteger());
		list.add(error);
		
		Map<String, Object> off = new HashMap<String, Object>();
		off.put("name", Level.OFF.toString());
		off.put("id", Level.OFF.toInteger());
		list.add(off);
		
		return list;
	}
	
}