package org.thorn.resource.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.resource.entity.Resource;

/**
 * @ClassName: ResourceDaoImpl
 * @Description:
 * @author chenyun
 * @date 2012-5-6 上午10:24:53
 */
public class ResourceDaoImpl implements IResourceDao {

	private final static String nameSpace = "ResourceMapper.";

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	public List<Resource> queryByList(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (List<Resource>) sqlSessionTemplate.selectList(nameSpace
					+ "selectPage", filter);
		} catch (Exception e) {
			throw new DBAccessException("ResourceDaoImpl", "queryList", e);
		}
	}

	public long queryPageCount(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (Long) sqlSessionTemplate.selectOne(nameSpace
					+ "selectPageCount", filter);
		} catch (Exception e) {
			throw new DBAccessException("ResourceDaoImpl", "queryPageCount", e);
		}
	}

	public int save(Resource source) throws DBAccessException {
		try {
			return sqlSessionTemplate.insert(nameSpace + "insert", source);
		} catch (Exception e) {
			throw new DBAccessException("ResourceDaoImpl", "save", e);
		}
	}

	public int modify(Resource source) throws DBAccessException {
		try {
			return sqlSessionTemplate.update(nameSpace + "update", source);
		} catch (Exception e) {
			throw new DBAccessException("ResourceDaoImpl", "modify", e);
		}
	}

	public int delete(List<String> ids) throws DBAccessException {
		try {
			return sqlSessionTemplate.delete(nameSpace + "delete", ids);
		} catch (Exception e) {
			throw new DBAccessException("ResourceDaoImpl", "delete", e);
		}
	}

}
