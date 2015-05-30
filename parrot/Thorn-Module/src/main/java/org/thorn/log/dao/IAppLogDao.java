package org.thorn.log.dao;

import java.util.List;
import java.util.Map;

import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.log.entity.AppLog;

/**
 * @ClassName: IAppLogDao
 * @Description:
 * @author chenyun
 * @date 2012-5-26 下午03:36:52
 */
public interface IAppLogDao {
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-29 下午07:50:23
	 * @param log
	 * @return
	 * @throws DBAccessException
	 */
	public int save(AppLog log) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-29 下午07:50:27
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public long queryPageCount(Map<String, Object> filter)
			throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-29 下午07:50:31
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public List<AppLog> queryList(Map<String, Object> filter)
		throws DBAccessException;
	
}
