package org.thorn.auth.dao;

import java.util.List;
import java.util.Map;

import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.role.entity.Role;
import org.thorn.user.entity.User;

/**
 * @ClassName: IAuthDao
 * @Description:
 * @author chenyun
 * @date 2012-5-25 上午11:35:32
 */
public interface IAuthDao {

	/**
	 * 
	 * @Description：根据角色ID查询授权资源
	 * @author：chenyun
	 * @date：2012-5-25 下午01:56:22
	 * @param roleId
	 * @return
	 * @throws DBAccessException
	 */
	public List<String> queryResourceByRole(String roleId)
			throws DBAccessException;

	/**
	 * 
	 * @Description：根据角色集合查询授权的资源集合，对资源去重
	 * @author：chenyun
	 * @date：2012-6-1 下午04:21:50
	 * @param roleIds
	 * @return
	 * @throws DBAccessException
	 */
	public List<String> queryResourceByRole(List<String> roleIds)
			throws DBAccessException;

	/**
	 * 
	 * @Description：查询资源集合被授权的角色集合，对角色作去重
	 * @author：chenyun
	 * @date：2012-5-25 下午01:56:19
	 * @param source
	 * @return
	 * @throws DBAccessException
	 */
	public List<Role> queryRoleBySource(List<String> source)
			throws DBAccessException;

	/**
	 * 
	 * @Description：取消角色对资源的所有授权
	 * @author：chenyun
	 * @date：2012-5-25 下午01:56:15
	 * @param roleCode
	 * @return
	 * @throws DBAccessException
	 */
	public int deleteRoleSource(String roleCode) throws DBAccessException;

	/**
	 * 
	 * @Description：对角色进行授权
	 * @author：chenyun
	 * @date：2012-5-25 下午01:56:12
	 * @param rs
	 * @return
	 * @throws DBAccessException
	 */
	public int saveRoleSource(Map<String, String> rs) throws DBAccessException;

	/**
	 * 
	 * @Description：查询用户授权的角色，过滤被禁用的角色
	 * @author：chenyun
	 * @date：2012-5-6 上午11:14:15
	 * @param userId
	 * @return
	 * @throws DBAccessException
	 */
	public List<Role> queryRoleByUser(String userId) throws DBAccessException;

	/**
	 * 
	 * @Description：对用户授权
	 * @author：chenyun
	 * @date：2012-5-25 下午01:55:43
	 * @param ur
	 * @return
	 * @throws DBAccessException
	 */
	public int saveUserRole(Map<String, String> ur) throws DBAccessException;

	/**
	 * 
	 * @Description：删除角色下的用户
	 * @author：chenyun
	 * @date：2012-5-25 下午01:55:46
	 * @param filter
	 *            rolecode + user集合
	 * @return
	 * @throws DBAccessException
	 */
	public int deleteUserInRole(Map<String, Object> filter)
			throws DBAccessException;

	/**
	 * 
	 * @Description：取消用户的所有角色授权
	 * @author：chenyun
	 * @date：2012-5-25 下午01:55:50
	 * @param userId
	 * @return
	 * @throws DBAccessException
	 */
	public int deleteUserAllRole(String userId) throws DBAccessException;

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-25 下午01:55:58
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public List<User> queryListNotInRole(Map<String, Object> filter)
			throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-7-2 下午03:43:07
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public long queryPageCountNotInRole(Map<String, Object> filter)
			throws DBAccessException;

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-6-19 下午08:44:18
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public List<User> queryListByRole(Map<String, Object> filter)
			throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-7-2 下午03:43:14
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public long queryPageCountInRole(Map<String, Object> filter)
			throws DBAccessException;

}
