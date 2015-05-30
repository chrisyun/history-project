package com.parrot.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.parrot.app.entity.Heritor;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.core.Configuration;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.dao.mybatis.helper.MyBatisDaoSupport;
import org.thorn.log.Logging;
import org.thorn.web.entity.Page;

/**
 * @ClassName: ProjectServiceImpl
 * @Description:
 * @author chenyun
 * @date 2012-8-10 上午10:59:48
 */
public class HeritorServiceImpl implements IHeritorService {

	@Autowired
	@Qualifier("myBatisDaoSupport")
	private MyBatisDaoSupport myBatisDaoSupport;

	@Logging("传承人信息新增")
	public void save(Heritor heritor) throws DBAccessException {
		myBatisDaoSupport.save(heritor);
	}

	@Logging("传承人信息修改")
	public void modify(Heritor heritor) throws DBAccessException {
		myBatisDaoSupport.modify(heritor);
	}

	@Logging("设置传承人所在项目")
	public void modifyProject(Integer projectId, String ids)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		List<String> list = LocalStringUtils.splitStr2Array(ids);
		filter.put("projectId", projectId);
		filter.put("list", list);

		myBatisDaoSupport.modify(filter, "HeritorMapper.updateProject");
		myBatisDaoSupport.modify(filter, "HeritorMapper.updateProjectNo");
	}

	@Logging("删除传承人")
	public void delete(String ids) throws DBAccessException {
		List<String> list = LocalStringUtils.splitStr2Array(ids);
		myBatisDaoSupport.deleteForBatch(list, Heritor.class);
	}

	public Page<Heritor> queryPage(String name, String gender, String minority,
			String projectName, String isDie, String province, String batchNum,
			long start, long limit, String sort, String dir)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("name", name);
		filter.put("gender", gender);
		filter.put("projectName", projectName);
		filter.put("isDie", isDie);
		filter.put("batchNum", batchNum);
		filter.put("province", province);
		filter.put("minority", minority);
		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);
		filter.put(Configuration.SROT_NAME, sort);
		filter.put(Configuration.ORDER_NAME, dir);

		Page<Heritor> page = new Page<Heritor>();

		page.setTotal(myBatisDaoSupport.queryCount(filter, Heritor.class));
		if (page.getTotal() > 0) {
			page.setReslutSet(myBatisDaoSupport
					.queryList(filter, Heritor.class));
		}

		return page;
	}

	public List<Heritor> queryList(String name, String province, String isDie,
			Integer projectId, String sort, String dir)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("name", name);
		filter.put("isDie", isDie);
		filter.put("province", province);
		filter.put("projectId", projectId);
		filter.put(Configuration.SROT_NAME, sort);
		filter.put(Configuration.ORDER_NAME, dir);

		return myBatisDaoSupport.queryList(filter, Heritor.class);
	}

	public long queryHeritorCount(String province, String isDie)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("isDie", isDie);
		filter.put("province", province);

		return myBatisDaoSupport.queryCount(filter, Heritor.class);
	}

}
