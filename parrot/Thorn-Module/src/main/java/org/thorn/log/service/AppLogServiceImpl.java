package org.thorn.log.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.core.Configuration;
import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.log.dao.IAppLogDao;
import org.thorn.log.entity.AppLog;

/** 
 * @ClassName: AppLogServiceImpl 
 * @Description: 
 * @author chenyun
 * @date 2012-5-26 下午03:44:56 
 */
public class AppLogServiceImpl implements IAppLogService {
	
	@Autowired
	@Qualifier("logDao")
	private IAppLogDao logDao;
	
	public void save(AppLog log) throws DBAccessException {
		logDao.save(log);
	}

	public Page<AppLog> queryPage(String moduleName, String handleResult,
			String startTime, String endTime, long start, long limit,
			String sort, String dir) throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put("moduleName", moduleName);
		filter.put("handleResult", handleResult);
		filter.put("startTime", startTime);
		filter.put("endTime", endTime);
		
		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);
		
		if(LocalStringUtils.isEmpty(sort)) {
			filter.put(Configuration.SROT_NAME, "EXECUTETIME");
			filter.put(Configuration.ORDER_NAME, Configuration.ORDER_DESC);
		} else {
			filter.put(Configuration.SROT_NAME, sort);
			filter.put(Configuration.ORDER_NAME, dir);
		}
		
		Page<AppLog> page = new Page<AppLog>();
		
		page.setTotal(logDao.queryPageCount(filter));
		if(page.getTotal() > 0) {
			page.setReslutSet(logDao.queryList(filter));
		}
		
		return page;
	}

	public List<AppLog> queryList(String moduleName, String handleResult,
			String startTime, String endTime) throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put("moduleName", moduleName);
		filter.put("handleResult", handleResult);
		filter.put("startTime", startTime);
		filter.put("endTime", endTime);
		
		filter.put(Configuration.SROT_NAME, "EXECUTETIME");
		filter.put(Configuration.ORDER_NAME, Configuration.ORDER_ASC);
		
		return logDao.queryList(filter);
	}

}

