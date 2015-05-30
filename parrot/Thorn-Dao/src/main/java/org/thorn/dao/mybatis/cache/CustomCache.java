package org.thorn.dao.mybatis.cache;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.thorn.core.cache.CacheException;
import org.thorn.core.cache.CacheService;
import org.thorn.core.context.SpringContext;

/**
 * @ClassName: MemCache
 * @Description:
 * @author chenyun
 * @date 2012-5-3 下午02:32:49
 */
public class CustomCache implements Cache {

	static Logger log = LoggerFactory.getLogger(CustomCache.class);

	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	private String id;

	private CacheService cacheService;

	private String springBean;

	private Set<Integer> cacheKeys = new HashSet<Integer>();

	public CustomCache(String id) {
		this.id = id;
	}

	public String getSpringBean() {
		return springBean;
	}

	public void setSpringBean(String springBean) {
		this.springBean = springBean;
		cacheService = SpringContext.getBean(springBean);

		Assert.notNull(cacheService);
	}

	public String getId() {
		return id;
	}

	public int getSize() {
		return cacheKeys.size();
	}

	public void putObject(Object key, Object value) {
		int hashKye = key.hashCode();

		try {
			cacheService.set(id + "-" + hashKye, value);
			cacheKeys.add(hashKye);
		} catch (CacheException e) {
			log.error("myBatis cache put exception", e);
		}
	}

	public Object getObject(Object key) {
		int hashKye = key.hashCode();

		if (cacheKeys.contains(hashKye)) {

			try {
				return cacheService.get(id + "-" + hashKye);
			} catch (CacheException e) {
				log.error("myBatis cache get exception", e);
			}
		}

		return null;
	}

	public Object removeObject(Object key) {
		int hashKye = key.hashCode();

//		try {
//			cacheService.delete(id + "-" + hashKye);
//		} catch (CacheException e) {
//			log.error("myBatis cache remove exception", e);
//		}

		cacheKeys.remove(hashKye);
		return null;
	}

	public void clear() {
		// 不需要真正去刷新缓存
		cacheKeys.clear();
	}

	public ReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}

}
