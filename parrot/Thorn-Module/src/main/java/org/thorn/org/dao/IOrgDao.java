package org.thorn.org.dao;

import java.util.List;
import java.util.Map;

import org.thorn.dao.exception.DBAccessException;
import org.thorn.org.entity.Org;

/**
 * @ClassName: IOrgDao
 * @Description:
 * @author chenyun
 * @date 2012-5-10 下午02:41:07
 */
public interface IOrgDao {
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:00:42
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
	 * @date：2012-5-25 上午11:00:46
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public List<Org> queryList(Map<String, Object> filter)
			throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:00:49
	 * @param org
	 * @return
	 * @throws DBAccessException
	 */
	public int save(Org org) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:00:53
	 * @param org
	 * @return
	 * @throws DBAccessException
	 */
	public int modify(Org org) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:00:57
	 * @param ids
	 * @return
	 * @throws DBAccessException
	 */
	public int delete(List<String> ids) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-6-25 下午09:03:18
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public Org queryParent(Map<String, Object> filter) throws DBAccessException;
}
