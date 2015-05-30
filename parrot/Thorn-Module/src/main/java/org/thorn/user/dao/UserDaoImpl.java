package org.thorn.user.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.user.entity.User;

/**
 * @ClassName: UserDaoImpl
 * @Description:
 * @author chenyun
 * @date 2012-5-6 上午11:15:55
 */
public class UserDaoImpl implements IUserDao {

	private final static String nameSpace = "UserMapper.";

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	public User queryUser(Map<String, Object> filter) throws DBAccessException {
		try {
			return (User) sqlSessionTemplate.selectOne(
					nameSpace + "selectPage", filter);
		} catch (Exception e) {
			throw new DBAccessException("UserDaoImpl", "queryUser", e);
		}
	}

	public List<User> queryList(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (List<User>) sqlSessionTemplate.selectList(nameSpace
					+ "selectPage", filter);
		} catch (Exception e) {
			throw new DBAccessException("UserDaoImpl", "selectUserList", e);
		}
	}

	public int save(User user) throws DBAccessException {
		try {
			return sqlSessionTemplate.insert(nameSpace + "insert", user);
		} catch (Exception e) {
			throw new DBAccessException("UserDaoImpl", "save", e);
		}
	}

	public int modify(User user) throws DBAccessException {
		try {
			return sqlSessionTemplate.update(nameSpace + "update", user);
		} catch (Exception e) {
			throw new DBAccessException("UserDaoImpl", "modify", e);
		}
	}

	public int delete(List<String> ids) throws DBAccessException {
		try {
			return sqlSessionTemplate.delete(nameSpace + "delete", ids);
		} catch (Exception e) {
			throw new DBAccessException("UserDaoImpl", "delete", e);
		}
	}

	public long queryPageCount(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (Long) sqlSessionTemplate.selectOne(nameSpace
					+ "selectPageCount", filter);
		} catch (Exception e) {
			throw new DBAccessException("UserDaoImpl", "queryPageCount", e);
		}
	}

	public int disabled(Map<String, Object> filter) throws DBAccessException {
		try {
			return sqlSessionTemplate.update(nameSpace + "disabled", filter);
		} catch (Exception e) {
			throw new DBAccessException("UserDaoImpl", "disabled", e);
		}
	}
}
