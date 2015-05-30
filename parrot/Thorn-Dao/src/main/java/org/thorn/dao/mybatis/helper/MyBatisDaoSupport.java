package org.thorn.dao.mybatis.helper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.thorn.dao.exception.DBAccessException;
import org.thorn.dao.helper.SimpleDaoSupport;

public interface MyBatisDaoSupport extends SimpleDaoSupport {

	public int save(Object obj, String mapperId) throws DBAccessException;

	public int modify(Object obj, String mapperId) throws DBAccessException;

	public <T> Object queryOne(Map<String, Object> filter, Class<T> bean)
			throws DBAccessException;

	public Object queryOne(Map<String, Object> filter, String mapperId)
			throws DBAccessException;

	public <T> List<T> queryList(Map<String, Object> filter, Class<T> bean)
			throws DBAccessException;

	public <T> List<T> queryList(Map<String, Object> filter, String mapperId)
			throws DBAccessException;

	public <T> long queryCount(Map<String, Object> filter, Class<T> bean)
			throws DBAccessException;

	public <T> long queryCount(Map<String, Object> filter, String mapperId)
			throws DBAccessException;

	public <T> int deleteForBatch(Collection<String> ids, Class<T> bean)
			throws DBAccessException;

	public <T> int deleteForBatch(Collection<String> ids, String mapperId)
			throws DBAccessException;

	public int delete(Object obj, String mapperId) throws DBAccessException;

}
