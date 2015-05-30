package org.thorn.auth.service;

import java.util.Collection;
import java.util.List;

import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.role.entity.Role;
import org.thorn.user.entity.User;

/**
 * @ClassName: IAuthService
 * @Description:
 * @author chenyun
 * @date 2012-5-25 上午11:35:13
 */
public interface IAuthService {

	/**
	 * 
	 * @Description：根据角色ID查找授权的资源
	 * @author：chenyun
	 * @date：2012-5-19 下午02:11:02
	 * @param roleId
	 * @return
	 * @throws DBAccessException
	 */
	public List<String> queryResourceByRole(String roleId)
			throws DBAccessException;
	
	/**
	 * 
	 * @Description：根据角色集合查找授权的资源集合
	 * @author：chenyun 	        
	 * @date：2012-6-1 下午04:24:04
	 * @param roleIds
	 * @return
	 * @throws DBAccessException
	 */
	public List<String> queryResourceByRole(List<String> roleIds)
			throws DBAccessException;

	/**
	 * 
	 * @Description：保存角色权限
	 * @author：chenyun
	 * @date：2012-5-25 上午11:49:12
	 * @param roleCode
	 *            角色code
	 * @param sourceIds
	 *            资源的主键集合
	 * @throws DBAccessException
	 */
	public void saveRoleAuth(String roleCode, String sourceIds)
			throws DBAccessException;

	/**
	 * 
	 * @Description：根据用户查找授权的角色
	 * @author：chenyun
	 * @date：2012-5-25 下午01:24:04
	 * @param userId
	 * @return
	 * @throws DBAccessException
	 */
	public List<Role> queryRoleByUser(String userId) throws DBAccessException;

	/**
	 * 
	 * @Description：根据权限ID列表查找对应的角色，返回的角色列表需要去重
	 * @author：chenyun
	 * @date：2012-5-4 下午03:48:01
	 * @param source
	 * @return
	 * @throws DBAccessException
	 */
	public List<Role> queryRoleBySource(List<String> source)
			throws DBAccessException;

	/**
	 * 
	 * @Description：分页查询角色中的用户
	 * @author：chenyun
	 * @date：2012-5-25 下午02:03:45
	 * @param userName
	 *            姓名
	 * @param orgCode
	 *            组织编码
	 * @param roleCode
	 *            角色编码
	 * @param userAccount
	 *            用户ID或账号
	 * @param start
	 * @param limit
	 * @param sort
	 * @param dir
	 * @return
	 * @throws DBAccessException
	 */
	public Page<User> queryPageByRole(String userName, String orgCode,
			String roleCode, String userAccount, long start, long limit,
			String sort, String dir) throws DBAccessException;

	/**
	 * 
	 * @Description：分页查询不在角色中的用户
	 * @author：chenyun
	 * @date：2012-5-25 下午02:04:37
	 * @param orgCode
	 *            组织编码
	 * @param roleCode
	 *            角色编码
	 * @param start
	 * @param limit
	 * @param sort
	 * @param dir
	 * @return
	 * @throws DBAccessException
	 */
	public Page<User> queryPageNotInRole(String orgCode, String roleCode,
			long start, long limit, String sort, String dir)
			throws DBAccessException;

	/**
	 * 
	 * @Description：向角色中添加用户
	 * @author：chenyun
	 * @date：2012-5-25 下午02:05:05
	 * @param roleCode
	 * @param userIds
	 *            多个用户ID分隔字符串
	 * @throws DBAccessException
	 */
	public void saveUserRole(String roleCode, String userIds)
			throws DBAccessException;

	/**
	 * 
	 * @Description：用户绑定角色
	 * @author：chenyun
	 * @date：2012-5-25 下午02:06:02
	 * @param userId
	 * @param roleCodes
	 * @throws DBAccessException
	 */
	public void saveRoleByUser(String userId, String roleCodes)
			throws DBAccessException;

	/**
	 * 
	 * @Description：删除角色中的用户
	 * @author：chenyun
	 * @date：2012-5-25 下午02:06:26
	 * @param roleCode
	 * @param userIds
	 *            多个用户ID分隔字符串
	 * @throws DBAccessException
	 */
	public void deleteUserRole(String roleCode, String userIds)
			throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-6-19 下午09:06:00
	 * @param roleCode
	 * @param orgIds
	 * @return
	 * @throws DBAccessException
	 */
	public List<User> queryListByRole(Collection<String> roleCodes, Collection<String> orgIds)
		throws DBAccessException;
}
