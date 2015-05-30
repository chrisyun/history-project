package org.thorn.auth.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserCache;
import org.thorn.auth.dao.IAuthDao;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.core.Configuration;
import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.role.entity.Role;
import org.thorn.user.entity.User;

/**
 * @ClassName: AuthServiceImpl
 * @Description:
 * @author chenyun
 * @date 2012-5-25 上午11:35:58
 */
public class AuthServiceImpl implements IAuthService {

	@Autowired
	@Qualifier("authDao")
	private IAuthDao authDao;
	
	@Autowired
	@Qualifier("userSecurityCache")
	private UserCache userCache;
	
	public List<String> queryResourceByRole(String roleId)
			throws DBAccessException {
		return authDao.queryResourceByRole(roleId);
	}
	
	public List<User> queryListByRole(Collection<String> roleCodes, Collection<String> orgIds)
		throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("roleCodes", roleCodes);
		filter.put("orgs", orgIds);
		filter.put("isDisabled", Configuration.DB_NO);
		
		return authDao.queryListByRole(filter);
	}

	public void saveRoleAuth(String roleCode, String sourceIds)
			throws DBAccessException {
		List<String> list = LocalStringUtils.splitStr2Array(sourceIds);

		authDao.deleteRoleSource(roleCode);

		if (list != null && list.size() > 0) {
			Map<String, String> rs = new HashMap<String, String>();
			for (String sourceCode : list) {
				rs.put("roleCode", roleCode);
				rs.put("sourceCode", sourceCode);

				authDao.saveRoleSource(rs);
			}
		}
	}

	public List<Role> queryRoleBySource(List<String> source)
			throws DBAccessException {
		return authDao.queryRoleBySource(source);
	}

	public List<Role> queryRoleByUser(String userId) throws DBAccessException {
		return authDao.queryRoleByUser(userId);
	}
	
	public Page<User> queryPageByRole(String userName, String orgCode,
			String roleCode, String userAccount, long start, long limit,
			String sort, String dir) throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();

		filter.put("idOrAccount", userAccount);
		filter.put("roleCode", roleCode);
		filter.put("orgCode", orgCode);
		filter.put("userName", userName);

		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);

		if (LocalStringUtils.isEmpty(sort)) {
			filter.put(Configuration.SROT_NAME, "SORTNUM");
			filter.put(Configuration.ORDER_NAME, Configuration.ORDER_ASC);
		} else {
			filter.put(Configuration.SROT_NAME, sort);
			filter.put(Configuration.ORDER_NAME, dir);
		}
		
		Page<User> page = new Page<User>();
		
		page.setTotal(authDao.queryPageCountInRole(filter));
		if(page.getTotal() > 0) {
			page.setReslutSet(authDao.queryListByRole(filter));
		}
		
		return page;
	}

	public void saveUserRole(String roleCode, String userIds)
			throws DBAccessException {
		Map<String, String> filter = new HashMap<String, String>();
		filter.put("roleCode", roleCode);
		
		List<String> list = LocalStringUtils.splitStr2Array(userIds);
		
		if(list != null && list.size() > 0) {
			for (String id : list) {
				filter.put("userId", id);
				authDao.saveUserRole(filter);
				
				// 角色发生改变从缓存中清除该用户对象
				userCache.removeUserFromCache(id);
			}
		}
	}

	public void deleteUserRole(String roleCode, String userIds)
			throws DBAccessException {
		List<String> list = LocalStringUtils.splitStr2Array(userIds);
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("roleCode", roleCode);
		filter.put("list", list);
		
		authDao.deleteUserInRole(filter);
		
		for(String id : list) {
			// 角色发生改变从缓存中清除该用户对象
			userCache.removeUserFromCache(id);
		}
		
	}

	public Page<User> queryPageNotInRole(String orgCode, String roleCode,
			long start, long limit, String sort, String dir)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();

		filter.put("roleCode", roleCode);
		filter.put("orgCode", orgCode);

		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);

		if (LocalStringUtils.isEmpty(sort)) {
			filter.put(Configuration.SROT_NAME, "SORTNUM");
			filter.put(Configuration.ORDER_NAME, Configuration.ORDER_ASC);
		} else {
			filter.put(Configuration.SROT_NAME, sort);
			filter.put(Configuration.ORDER_NAME, dir);
		}

		Page<User> page = new Page<User>();
		
		page.setTotal(authDao.queryPageCountNotInRole(filter));
		if(page.getTotal() > 0) {
			page.setReslutSet(authDao.queryListNotInRole(filter));
		}
		
		return page;
	}

	public void saveRoleByUser(String userId, String roleCodes)
			throws DBAccessException {
		
		authDao.deleteUserAllRole(userId);
		Map<String, String> filter = new HashMap<String, String>();
		filter.put("userId", userId);
		
		List<String> list = LocalStringUtils.splitStr2Array(roleCodes);
		
		if(list != null && list.size() > 0) {
			for (String id : list) {
				filter.put("roleCode", id);
				authDao.saveUserRole(filter);
			}
		}
		
		// 角色发生改变从缓存中清除该用户对象
		userCache.removeUserFromCache(userId);
	}

	public List<String> queryResourceByRole(List<String> roleIds)
			throws DBAccessException {
		return authDao.queryResourceByRole(roleIds);
	}

}
