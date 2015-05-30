package org.thorn.role.service;

import java.util.List;

import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.role.entity.Role;

/**
 * @ClassName: IRoleService
 * @Description:
 * @author chenyun
 * @date 2012-5-4 下午03:17:35
 */
public interface IRoleService {
	
	/**
	 * 
	 * @Description：查询所有的角色，过滤禁用的角色
	 * @author：chenyun 	        
	 * @date：2012-5-25 下午01:35:17
	 * @return
	 * @throws DBAccessException
	 */
	public List<Role> queryAllRoles() throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 下午01:35:36
	 * @param role
	 * @throws DBAccessException
	 */
	public void save(Role role) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 下午01:35:40
	 * @param role
	 * @throws DBAccessException
	 */
	public void modify(Role role) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 下午01:35:43
	 * @param ids
	 * @throws DBAccessException
	 */
	public void delete(String ids) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 下午01:35:46
	 * @param roleCode
	 * @param roleName
	 * @param start
	 * @param limit
	 * @param sort
	 * @param dir
	 * @return
	 * @throws DBAccessException
	 */
	public Page<Role> queryPage(String roleCode, String roleName, long start,
			long limit, String sort, String dir) throws DBAccessException;
	
}
