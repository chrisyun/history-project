package org.thorn.core.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.transcoders.StringTranscoder;

/**
 * @ClassName: XMemcacheClient
 * @Description:
 * @author chenyun
 * @date 2012-5-3 上午10:32:03
 */
public class XMemcacheService implements CacheService {

	static Logger log = LoggerFactory.getLogger(XMemcacheService.class);

	private static long defaultTimeOut = 3000;

	private MemcachedClient client;

	public boolean set(String key, Object value) throws CacheException {
		try {
			log.debug("memcache do set,the key:{}", key);

			return client.set(key, 0, value, defaultTimeOut);
		} catch (TimeoutException e) {
			throw new CacheException("xmemcache set method timeOut", e);
		} catch (InterruptedException e) {
			throw new CacheException("xmemcache set method interrupt", e);
		} catch (MemcachedException e) {
			throw new CacheException("xmemcache inside exception", e);
		}
	}

	public Object get(String key) throws CacheException {
		try {
			return client.get(key, defaultTimeOut);
		} catch (TimeoutException e) {
			throw new CacheException("xmemcache get method timeOut", e);
		} catch (InterruptedException e) {
			throw new CacheException("xmemcache get method interrupt", e);
		} catch (MemcachedException e) {
			throw new CacheException("xmemcache inside exception", e);
		}
	}

	public Map<String, Object> getByList(List<String> keys)
			throws CacheException {
		try {
			return client.get(keys, defaultTimeOut);
		} catch (TimeoutException e) {
			throw new CacheException("xmemcache getByList method timeOut", e);
		} catch (InterruptedException e) {
			throw new CacheException("xmemcache getByList method interrupt", e);
		} catch (MemcachedException e) {
			throw new CacheException("xmemcache inside exception", e);
		}
	}

	public String getString(String key) throws CacheException {
		try {
			return client.get(key, defaultTimeOut, new StringTranscoder());
		} catch (TimeoutException e) {
			throw new CacheException("xmemcache getString method timeOut", e);
		} catch (InterruptedException e) {
			throw new CacheException("xmemcache getString method interrupt", e);
		} catch (MemcachedException e) {
			throw new CacheException("xmemcache inside exception", e);
		}
	}

	public boolean delete(String key) throws CacheException {
		try {
			log.debug("memcache do delete,the key:{}", key);

			return client.delete(key, defaultTimeOut);
		} catch (TimeoutException e) {
			throw new CacheException("xmemcache delete method timeOut", e);
		} catch (InterruptedException e) {
			throw new CacheException("xmemcache delete method interrupt", e);
		} catch (MemcachedException e) {
			throw new CacheException("xmemcache inside exception", e);
		}
	}

	public void deleteWithNoReply(String key) throws CacheException {
		try {
			log.debug("memcache do deleteWithNoReply,the key:{}", key);

			client.deleteWithNoReply(key);
		} catch (InterruptedException e) {
			throw new CacheException(
					"xmemcache deleteWithNoReply method interrupt", e);
		} catch (MemcachedException e) {
			throw new CacheException("xmemcache inside exception", e);
		}
	}

	public void flush() throws CacheException {
		try {
			log.debug("memcache do flushAllWithNoReply");

			client.flushAllWithNoReply(0);
		} catch (InterruptedException e) {
			throw new CacheException("xmemcache flush method interrupt", e);
		} catch (MemcachedException e) {
			throw new CacheException("xmemcache inside exception", e);
		}
	}

	public static long getDefaultTimeOut() {
		return defaultTimeOut;
	}

	public static void setDefaultTimeOut(long defaultTimeOut) {
		XMemcacheService.defaultTimeOut = defaultTimeOut;
	}

	public MemcachedClient getClient() {
		return client;
	}

	public void setClient(MemcachedClient client) {
		this.client = client;
	}
}
