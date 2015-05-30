package com.parrot.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.parrot.app.entity.Resever;
import com.parrot.app.entity.ReseverCost;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.core.Configuration;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.dao.mybatis.helper.MyBatisDaoSupport;
import org.thorn.web.entity.Page;

/**
 * @ClassName: ProjectServiceImpl
 * @Description:
 * @author chenyun
 * @date 2012-8-10 上午10:59:48
 */
public class ReseverServiceImpl implements IReseverService {

	@Autowired
	@Qualifier("myBatisDaoSupport")
	private MyBatisDaoSupport myBatisDaoSupport;

	public void save(Resever resever) throws DBAccessException {
		myBatisDaoSupport.save(resever);
	}

	public void modify(Resever resever) throws DBAccessException {
		myBatisDaoSupport.modify(resever);
	}

	public void delete(String ids) throws DBAccessException {
		List<String> list = LocalStringUtils.splitStr2Array(ids);
		myBatisDaoSupport.deleteForBatch(list, Resever.class);
	}

	public Page<Resever> queryPage(String name, String userName, String userId,
			String province, long start, long limit, String sort, String dir)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("name", name);
		filter.put("userName", userName);
		filter.put("userId", userId);
		filter.put("province", province);
		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);
		filter.put(Configuration.SROT_NAME, sort);
		filter.put(Configuration.ORDER_NAME, dir);

		Page<Resever> page = new Page<Resever>();

		page.setTotal(myBatisDaoSupport.queryCount(filter, Resever.class));
		if (page.getTotal() > 0) {
			page.setReslutSet(myBatisDaoSupport
					.queryList(filter, Resever.class));
		}

		return page;
	}

	public List<Resever> queryReseverList(String province, String userId)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("province", province);
		filter.put("userId", userId);

		return myBatisDaoSupport.queryList(filter, Resever.class);
	}

	public ReseverCost queryReseverCost(Integer id) throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("id", id);

		return (ReseverCost) myBatisDaoSupport.queryOne(filter,
				ReseverCost.class);
	}

	public Page<ReseverCost> queryCostPage(String name, Integer pid,
			String userName, String userId, String province, String startTime,
			String endTime, Integer year, String activity, String flowStatus,
			long start, long limit, String sort, String dir)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("projectName", name);
		filter.put("projectId", pid);
		filter.put("createrName", userName);
		filter.put("creater", userId);
		filter.put("province", province);
		filter.put("startTime", startTime);
		filter.put("endTime", endTime);
		filter.put("year", year);
		filter.put("activity", activity);
		filter.put("flowStatus", flowStatus);
		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);

		if (LocalStringUtils.isEmpty(sort)) {
			filter.put(Configuration.SROT_NAME, "R.APPLYTIME");
			filter.put(Configuration.ORDER_NAME, Configuration.ORDER_ASC);
		} else {
			filter.put(Configuration.SROT_NAME, sort);
			filter.put(Configuration.ORDER_NAME, dir);
		}

		Page<ReseverCost> page = new Page<ReseverCost>();

		page.setTotal(myBatisDaoSupport.queryCount(filter, ReseverCost.class));
		if (page.getTotal() > 0) {
			page.setReslutSet(myBatisDaoSupport.queryList(filter,
					ReseverCost.class));
		}

		return page;
	}

	public Double queryReseverCostSum(Integer year, String province)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("province", province);
		filter.put("year", year);

		long money = myBatisDaoSupport.queryCount(filter,
				"ReseverCostMapper.selectCostSum");

		return (double) money / 10000;
	}

	public ReseverCost queryReseverDW(Integer reseverId)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("reseverId", reseverId);

		return (ReseverCost) myBatisDaoSupport.queryOne(filter,
				"ReseverCostMapper.selectMax");
	}

}
