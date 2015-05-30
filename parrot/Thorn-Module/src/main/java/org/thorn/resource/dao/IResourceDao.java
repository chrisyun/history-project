package org.thorn.resource.dao;

import java.util.List;
import java.util.Map;

import org.thorn.dao.exception.DBAccessException;
import org.thorn.resource.entity.Resource;

/**
 * @ClassName: IResourceDao
 * @Description:
 * @author chenyun
 * @date 2012-5-6 上午10:22:39
 */
public interface IResourceDao {
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:43:23
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public List<Resource> queryByList(Map<String, Object> filter)
			throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:43:27
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
	 * @date：2012-5-25 上午11:43:31
	 * @param source
	 * @return
	 * @throws DBAccessException
	 */
	public int save(Resource source) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:43:35
	 * @param source
	 * @return
	 * @throws DBAccessException
	 */
	public int modify(Resource source) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:43:38
	 * @param ids
	 * @return
	 * @throws DBAccessException
	 */
	public int delete(List<String> ids) throws DBAccessException;
}
