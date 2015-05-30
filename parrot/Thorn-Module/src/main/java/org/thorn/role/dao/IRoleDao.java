package org.thorn.role.dao;

import java.util.List;
import java.util.Map;

import org.thorn.dao.exception.DBAccessException;
import org.thorn.role.entity.Role;

/**
 * @ClassName: IRoleDao
 * @Description:
 * @author chenyun
 * @date 2012-5-6 上午10:48:02
 */
public interface IRoleDao {
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 下午01:36:49
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public List<Role> queryList(Map<String, Object> filter) throws DBAccessException;

	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 下午01:37:05
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public long queryPageCount(Map<String, Object> filter) throws DBAccessException;	
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 下午01:36:54
	 * @param role
	 * @return
	 * @throws DBAccessException
	 */
	public int save(Role role) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 下午01:36:57
	 * @param role
	 * @return
	 * @throws DBAccessException
	 */
	public int modify(Role role) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 下午01:37:01
	 * @param ids
	 * @return
	 * @throws DBAccessException
	 */
	public int delete(List<String> ids) throws DBAccessException;
}
