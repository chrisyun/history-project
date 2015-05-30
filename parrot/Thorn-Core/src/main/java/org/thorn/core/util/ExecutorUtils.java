package org.thorn.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.thorn.core.context.SpringContext;

/** 
 * @ClassName: ExecutorUtils 
 * @Description: 
 * @author chenyun
 * @date 2012-5-26 上午11:31:38 
 */
public class ExecutorUtils {
	
	static Logger log = LoggerFactory.getLogger(ExecutorUtils.class);
	
	private volatile static TaskExecutor executor;
	
	private static synchronized void initExecutor() {
		if(executor == null) {
			log.debug("init TaskExecutor...");
			executor = SpringContext.getBean("poolExecutor");
		}
	}
	
	public static void executeTask(Runnable task) {
		if(executor == null) {
			initExecutor();
		}
		
		executor.execute(task);
		log.debug("execute task over.");
	}
	
}

