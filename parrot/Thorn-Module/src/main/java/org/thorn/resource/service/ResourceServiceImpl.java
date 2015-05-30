package org.thorn.resource.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.core.Configuration;
import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.resource.dao.IResourceDao;
import org.thorn.resource.entity.Resource;

/**
 * 
 * @ClassName: ResourceService
 * @Description:
 * @author chenyun
 * @date 2012-5-5 下午06:08:54
 * 
 */
public class ResourceServiceImpl implements IResourceService {

	@Autowired
	@Qualifier("resourceDao")
	private IResourceDao resourceDao;

	public List<Resource> queryAllLeaf() throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();

		// 根节点不展示，根节点无URL
		// 不显示的权限可以挂在叶子页面上，是否显示为NO，是否为叶子为YES
		filter.put("isleaf", Configuration.DB_YES);

		return resourceDao.queryByList(filter);
	}

	public List<Resource> queryAllSource() throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();

		filter.put(Configuration.SROT_NAME, "SORTNUM");
		filter.put(Configuration.ORDER_NAME, Configuration.ORDER_ASC);

		return resourceDao.queryByList(filter);
	}

	public List<Resource> queryLeftTree(String pid) throws DBAccessException {

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("parentSource", pid);
		filter.put("isShow", Configuration.DB_YES);
		filter.put(Configuration.SROT_NAME, "SORTNUM");
		filter.put(Configuration.ORDER_NAME, Configuration.ORDER_ASC);

		return resourceDao.queryByList(filter);
	}

	public void save(Resource source) throws DBAccessException {
		resourceDao.save(source);
	}

	public void modify(Resource source) throws DBAccessException {
		resourceDao.modify(source);
	}

	public void delete(String ids) throws DBAccessException {
		List<String> list = LocalStringUtils.splitStr2Array(ids);
		resourceDao.delete(list);
	}

	public Page<Resource> queryPage(String pid, String sourceCode,
			String sourceName, long start, long limit, String sort, String dir)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();

		filter.put("parentSource", pid);
		filter.put("sourceCode", sourceCode);
		filter.put("sourceName", sourceName);

		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);

		if (LocalStringUtils.isEmpty(sort)) {
			filter.put(Configuration.SROT_NAME, "SORTNUM");
			filter.put(Configuration.ORDER_NAME, Configuration.ORDER_ASC);
		} else {
			filter.put(Configuration.SROT_NAME, sort);
			filter.put(Configuration.ORDER_NAME, dir);
		}

		Page<Resource> page = new Page<Resource>();

		page.setTotal(resourceDao.queryPageCount(filter));
		if (page.getTotal() > 0) {
			page.setReslutSet(resourceDao.queryByList(filter));
		}

		return page;
	}

	public Resource queryResource(String sourceCode) throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("sourceCode", sourceCode);

		List<Resource> list = resourceDao.queryByList(filter);

		if (list.size() != 1) {
			throw new DBAccessException("queryOrg find result size:"
					+ list.size());
		}

		return list.get(0);
	}

}
