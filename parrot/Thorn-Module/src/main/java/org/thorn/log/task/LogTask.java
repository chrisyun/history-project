package org.thorn.log.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thorn.core.context.SpringContext;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.log.entity.AppLog;
import org.thorn.log.service.IAppLogService;

/** 
 * @ClassName: LogTask 
 * @Description: 
 * @author chenyun
 * @date 2012-5-26 上午11:39:15 
 */
public class LogTask implements Runnable {
	
	static Logger logger = LoggerFactory.getLogger(LogTask.class);
	
	private AppLog log;
	
	public LogTask(AppLog log) {
		this.log = log;
	}
	
	public void run() {
		IAppLogService service = SpringContext.getBean("logService");
		try {
			service.save(log);
		} catch (DBAccessException e) {
			logger.error("logTask do save [AppLog] - " + e.getMessage(), e);
		}
	}

}

