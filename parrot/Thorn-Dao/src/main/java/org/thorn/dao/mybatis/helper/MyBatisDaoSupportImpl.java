package org.thorn.dao.mybatis.helper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.dao.mybatis.annotation.MapperUtils;
import org.thorn.dao.mybatis.annotation.MethodType;

public class MyBatisDaoSupportImpl implements MyBatisDaoSupport {

	static Logger log = LoggerFactory.getLogger(MyBatisDaoSupportImpl.class);

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	public int save(Object obj) throws DBAccessException {
		String mapper = MapperUtils.getMapperSource(obj.getClass(),
				MethodType.INSERT);

		return save(obj, mapper);
	}

	public int save(Object obj, String mapperId) throws DBAccessException {
		try {
			return sqlSessionTemplate.insert(mapperId, obj);
		} catch (Exception e) {
			throw new DBAccessException("MyBatisDaoSupport", "save", "Object["
					+ obj.getClass().getName() + "],mapper[" + mapperId + "]",
					e);
		}
	}

	public int modify(Object obj) throws DBAccessException {
		String mapper = MapperUtils.getMapperSource(obj.getClass(),
				MethodType.UPDATE);

		return modify(obj, mapper);
	}

	public int modify(Object obj, String mapperId) throws DBAccessException {
		try {
			return sqlSessionTemplate.update(mapperId, obj);
		} catch (Exception e) {
			throw new DBAccessException("MyBatisDaoSupport", "modify",
					"Object[" + obj.getClass().getName() + "],mapper["
							+ mapperId + "]", e);
		}
	}

	public int delete(Object obj) throws DBAccessException {
		String mapper = MapperUtils.getMapperSource(obj.getClass(),
				MethodType.DELETE);

		return delete(obj, mapper);
	}

	public int delete(Object obj, String mapperId) throws DBAccessException {
		try {
			return sqlSessionTemplate.delete(mapperId, obj);
		} catch (Exception e) {
			throw new DBAccessException("MyBatisDaoSupport", "delete",
					"Object[" + obj.getClass().getName() + "],mapper["
							+ mapperId + "]", e);
		}
	}

	public Object queryOne(Map<String, Object> filter, String mapperId)
			throws DBAccessException {
		try {
			return sqlSessionTemplate.selectOne(mapperId, filter);
		} catch (Exception e) {
			throw new DBAccessException("MyBatisDaoSupport", "queryOne",
					"mapper[" + mapperId + "]", e);
		}
	}

	public <T> Object queryOne(Map<String, Object> filter, Class<T> bean)
			throws DBAccessException {
		String mapper = MapperUtils.getMapperSource(bean, MethodType.QUERY);

		return queryOne(filter, mapper);
	}

	public <T> int deleteForBatch(Collection<String> ids, Class<T> bean)
			throws DBAccessException {
		String mapper = MapperUtils.getMapperSource(bean,
				MethodType.DELETE_BATCH);

		return delete(ids, mapper);
	}

	public <T> int deleteForBatch(Collection<String> ids, String mapperId)
			throws DBAccessException {
		try {
			return sqlSessionTemplate.delete(mapperId, ids);
		} catch (Exception e) {
			throw new DBAccessException("MyBatisDaoSupport", "deleteForBatch",
					"mapper[" + mapperId + "]", e);
		}
	}

	public <T> long queryCount(Map<String, Object> filter, Class<T> bean)
			throws DBAccessException {
		String pageCountMapper = MapperUtils.getMapperSource(bean,
				MethodType.COUNT);

		return queryCount(filter, pageCountMapper);
	}

	public <T> long queryCount(Map<String, Object> filter, String mapperId)
			throws DBAccessException {
		try {
			Object num = sqlSessionTemplate.selectOne(mapperId, filter);
			if(num == null) {
				return 0;
			} else {
				return (Long) num;
			}
		} catch (Exception e) {
			throw new DBAccessException("MyBatisDaoSupport", "queryCount",
					"mapper[" + mapperId + "]", e);
		}
	}

	public <T> List<T> queryList(Map<String, Object> filter, Class<T> bean)
			throws DBAccessException {
		String mapper = MapperUtils
				.getMapperSource(bean, MethodType.QUERY_LIST);

		return queryList(filter, mapper);
	}

	public <T> List<T> queryList(Map<String, Object> filter, String mapperId)
			throws DBAccessException {
		try {
			return (List<T>) sqlSessionTemplate.selectList(mapperId, filter);
		} catch (Exception e) {
			throw new DBAccessException("MyBatisDaoSupport", "queryList",
					"mapper[" + mapperId + "]", e);
		}
	}

}
