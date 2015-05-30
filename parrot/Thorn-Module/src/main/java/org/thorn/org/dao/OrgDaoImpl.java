package org.thorn.org.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.org.entity.Org;

/**
 * @ClassName: OrgDaoImpl
 * @Description:
 * @author chenyun
 * @date 2012-5-10 下午02:43:57
 */
public class OrgDaoImpl implements IOrgDao {

	private final static String nameSpace = "OrgMapper.";

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	public long queryPageCount(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (Long) sqlSessionTemplate.selectOne(nameSpace
					+ "selectPageCount", filter);
		} catch (Exception e) {
			throw new DBAccessException("OrgDaoImpl", "queryPage", e);
		}
	}

	public List<Org> queryList(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (List<Org>) sqlSessionTemplate.selectList(nameSpace
					+ "selectPage", filter);
		} catch (Exception e) {
			throw new DBAccessException("OrgDaoImpl", "queryPage", e);
		}
	}

	public int save(Org org) throws DBAccessException {
		try {
			return sqlSessionTemplate.insert(nameSpace + "insert", org);
		} catch (Exception e) {
			throw new DBAccessException("OrgDaoImpl", "save", e);
		}
	}

	public int modify(Org org) throws DBAccessException {
		try {
			return sqlSessionTemplate.update(nameSpace + "update", org);
		} catch (Exception e) {
			throw new DBAccessException("OrgDaoImpl", "modify", e);
		}
	}

	public int delete(List<String> ids) throws DBAccessException {
		try {
			return sqlSessionTemplate.delete(nameSpace + "delete", ids);
		} catch (Exception e) {
			throw new DBAccessException("OrgDaoImpl", "delete", e);
		}
	}

	public Org queryParent(Map<String, Object> filter) throws DBAccessException {
		try {
			return (Org) sqlSessionTemplate.selectOne(nameSpace
					+ "selectParent", filter);
		} catch (Exception e) {
			throw new DBAccessException("OrgDaoImpl", "queryParent", e);
		}
	}

}
