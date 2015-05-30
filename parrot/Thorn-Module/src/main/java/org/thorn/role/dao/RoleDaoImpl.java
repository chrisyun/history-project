package org.thorn.role.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.role.entity.Role;

/**
 * @ClassName: RoleDaoImpl
 * @Description:
 * @author chenyun
 * @date 2012-5-6 上午10:51:05
 */
public class RoleDaoImpl implements IRoleDao {

	private final static String nameSpace = "RoleMapper.";

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	public int save(Role role) throws DBAccessException {
		try {
			return sqlSessionTemplate.insert(nameSpace + "insert", role);
		} catch (Exception e) {
			throw new DBAccessException("RoleDaoImpl", "save", e);
		}
	}

	public int modify(Role role) throws DBAccessException {
		try {
			return sqlSessionTemplate.update(nameSpace + "update", role);
		} catch (Exception e) {
			throw new DBAccessException("RoleDaoImpl", "modify", e);
		}
	}

	public int delete(List<String> ids) throws DBAccessException {
		try {
			return sqlSessionTemplate.delete(nameSpace + "delete", ids);
		} catch (Exception e) {
			throw new DBAccessException("RoleDaoImpl", "delete", e);
		}
	}

	public long queryPageCount(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (Long) sqlSessionTemplate.selectOne(nameSpace
					+ "selectPageCount", filter);
		} catch (Exception e) {
			throw new DBAccessException("RoleDaoImpl", "queryPageCount", e);
		}
	}

	public List<Role> queryList(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (List<Role>) sqlSessionTemplate.selectList(nameSpace
					+ "selectPage", filter);
		} catch (Exception e) {
			throw new DBAccessException("RoleDaoImpl", "query", e);
		}
	}

}
