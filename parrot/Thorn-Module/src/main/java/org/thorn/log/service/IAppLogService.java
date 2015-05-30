package org.thorn.log.service;

import java.util.List;

import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.log.entity.AppLog;

/**
 * @ClassName: IAppLogService
 * @Description:
 * @author chenyun
 * @date 2012-5-26 下午03:41:16
 */
public interface IAppLogService {

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-26 下午03:41:56
	 * @param log
	 * @throws DBAccessException
	 */
	public void save(AppLog log) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-26 下午03:43:41
	 * @param moduleName	模块名称
	 * @param handleResult	操作结果
	 * @param startTime		开始日期
	 * @param endTime		结束日期
	 * @param start
	 * @param limit
	 * @param sort
	 * @param dir
	 * @return
	 * @throws DBAccessException
	 */
	public Page<AppLog> queryPage(String moduleName, String handleResult,
			String startTime, String endTime, long start, long limit,
			String sort, String dir) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-29 下午07:47:26
	 * @param moduleName
	 * @param handleResult
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws DBAccessException
	 */
	public List<AppLog> queryList(String moduleName, String handleResult,
			String startTime, String endTime) throws DBAccessException;
}
