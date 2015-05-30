package org.thorn.auth.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.role.entity.Role;
import org.thorn.user.entity.User;

/**
 * @ClassName: AuthDaoImpl
 * @Description:
 * @author chenyun
 * @date 2012-5-25 上午11:36:25
 */
public class AuthDaoImpl implements IAuthDao {

	private final static String nameSpace = "AuthMapper.";

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	public List<String> queryResourceByRole(String roleId)
			throws DBAccessException {
		try {
			return (List<String>) sqlSessionTemplate.selectList(nameSpace
					+ "querySourceByRole", roleId);
		} catch (Exception e) {
			throw new DBAccessException("AuthDaoImpl", "queryResourceByRole", e);
		}
	}

	public List<String> queryResourceByRole(List<String> roleIds)
			throws DBAccessException {
		try {
			return (List<String>) sqlSessionTemplate.selectList(nameSpace
					+ "querySourceByRoleList", roleIds);
		} catch (Exception e) {
			throw new DBAccessException("AuthDaoImpl", "queryResourceByRole", e);
		}
	}

	public int deleteRoleSource(String roleCode) throws DBAccessException {
		try {
			return sqlSessionTemplate.delete(nameSpace + "deleteSourceByRole",
					roleCode);
		} catch (Exception e) {
			throw new DBAccessException("AuthDaoImpl", "deleteRoleSource", e);
		}
	}

	public int saveRoleSource(Map<String, String> rs) throws DBAccessException {
		try {
			return sqlSessionTemplate
					.insert(nameSpace + "insertRoleSource", rs);
		} catch (Exception e) {
			throw new DBAccessException("AuthDaoImpl", "saveRoleSource", e);
		}
	}

	public List<Role> queryRoleBySource(List<String> source)
			throws DBAccessException {
		try {
			return (List<Role>) sqlSessionTemplate.selectList(nameSpace
					+ "queryRoleBySource", source);
		} catch (Exception e) {
			throw new DBAccessException("AuthDaoImpl", "queryRolesByResource",
					e);
		}
	}

	public List<Role> queryRoleByUser(String userId) throws DBAccessException {
		try {
			return (List<Role>) sqlSessionTemplate.selectList(nameSpace
					+ "queryRoleByUser", userId);
		} catch (Exception e) {
			throw new DBAccessException("AuthDaoImpl", "queryRoleByUser", e);
		}
	}

	public List<User> queryListByRole(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (List<User>) sqlSessionTemplate.selectList(nameSpace
					+ "selectUserPageByRole", filter);
		} catch (Exception e) {
			throw new DBAccessException("AuthDaoImpl", "queryListByRole", e);
		}
	}

	public long queryPageCountInRole(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (Long) sqlSessionTemplate.selectOne(nameSpace
					+ "selectUserPageByRoleCount", filter);
		} catch (Exception e) {
			throw new DBAccessException("AuthDaoImpl", "queryPageCountInRole", e);
		}
	}

	public int saveUserRole(Map<String, String> ur) throws DBAccessException {
		try {
			return sqlSessionTemplate.insert(nameSpace + "insertUserRole", ur);
		} catch (Exception e) {
			throw new DBAccessException("AuthDaoImpl", "saveUserRole", e);
		}
	}

	public int deleteUserInRole(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return sqlSessionTemplate.delete(nameSpace + "deleteUserRole",
					filter);
		} catch (Exception e) {
			throw new DBAccessException("UserDaoImpl", "deleteUserInRole", e);
		}
	}

	public List<User> queryListNotInRole(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (List<User>) sqlSessionTemplate.selectList(nameSpace
					+ "selectUserPageNotInRole", filter);
		} catch (Exception e) {
			throw new DBAccessException("AuthDaoImpl", "queryListNotInRole", e);
		}
	}

	public long queryPageCountNotInRole(Map<String, Object> filter)
			throws DBAccessException {
		try {

			return (Long) sqlSessionTemplate.selectOne(nameSpace
					+ "selectUserPageNotInRoleCount", filter);
		} catch (Exception e) {
			throw new DBAccessException("AuthDaoImpl",
					"queryPageCountNotInRole", e);
		}
	}

	public int deleteUserAllRole(String userId) throws DBAccessException {
		try {
			return sqlSessionTemplate.delete(nameSpace + "deleteUserAllRole",
					userId);
		} catch (Exception e) {
			throw new DBAccessException("AuthDaoImpl", "deleteUserAllRole", e);
		}
	}

}
