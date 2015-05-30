package org.thorn.core.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * @ClassName: EhCacheService
 * @Description:
 * @author chenyun
 * @date 2012-5-3 下午02:02:00
 */
public class EhCacheService implements CacheService {

	static Logger log = LoggerFactory.getLogger(EhCacheService.class);

	private Ehcache cache;

	public boolean set(String key, Object value) throws CacheException {
		try {
			log.debug("ehcache do set,the key:{}", key);

			Element e = new Element(key, value);
			cache.put(e);

			return true;
		} catch (Exception e) {
			throw new CacheException("Ehcache do set runtimeException", e);
		}
	}

	public Object get(String key) throws CacheException {
		try {
			Element e = cache.get(key);
			
			if(e != null) {
				return e.getValue();
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new CacheException("Ehcache do get runtimeException", e);
		}
	}

	public Map<String, Object> getByList(List<String> keys)
			throws CacheException {

		Map<String, Object> map = new HashMap<String, Object>();

		Element el = null;

		try {
			for (String key : keys) {
				el = cache.get(key);
				map.put(key, el.getValue());
			}

			return map;
		} catch (Exception e) {
			throw new CacheException("Ehcache do get runtimeException", e);
		}

	}

	public String getString(String key) throws CacheException {

		Object obj = get(key);

		if (obj != null && obj instanceof String) {
			return (String) obj;
		}

		return null;
	}

	public boolean delete(String key) throws CacheException {
		try {
			log.debug("ehcache do delete,the key:{}", key);

			cache.remove(key);
			return true;
		} catch (Exception e) {
			throw new CacheException("Ehcache do remove runtimeException", e);
		}
	}

	public void deleteWithNoReply(String key) throws CacheException {
		delete(key);
	}

	public void flush() throws CacheException {
		try {
			log.debug("ehcache do removeAll");

			cache.removeAll();
		} catch (Exception e) {
			throw new CacheException("Ehcache do removeAll runtimeException", e);
		}
	}

	public Ehcache getCache() {
		return cache;
	}

	public void setCache(Ehcache cache) {
		this.cache = cache;
	}
}
