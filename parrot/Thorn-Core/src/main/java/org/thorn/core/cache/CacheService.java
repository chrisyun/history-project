package org.thorn.core.cache;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: CacheService
 * @Description:
 * @author chenyun
 * @date 2012-5-3 上午11:56:47
 */
public interface CacheService {

	public boolean set(String key, Object value) throws CacheException;

	public Object get(String key) throws CacheException;

	public Map<String, Object> getByList(List<String> keys)
			throws CacheException;

	public String getString(String key) throws CacheException;

	public boolean delete(String key) throws CacheException;

	public void deleteWithNoReply(String key) throws CacheException;

	public void flush() throws CacheException;
}
