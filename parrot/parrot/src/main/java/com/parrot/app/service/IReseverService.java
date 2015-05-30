package com.parrot.app.service;

import java.util.List;

import com.parrot.app.entity.Resever;
import com.parrot.app.entity.ReseverCost;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.web.entity.Page;

/**
 * @ClassName: IProjectServiceImpl
 * @Description:
 * @author chenyun
 * @date 2012-8-10 上午10:59:16
 */
public interface IReseverService {

	public void save(Resever resever) throws DBAccessException;

	public void modify(Resever resever) throws DBAccessException;

	public void delete(String ids) throws DBAccessException;

	public Page<Resever> queryPage(String name, String userName, String userId,
			String province, long start, long limit, String sort, String dir)
			throws DBAccessException;

	public List<Resever> queryReseverList(String province, String userId)
			throws DBAccessException;

	public ReseverCost queryReseverCost(Integer id) throws DBAccessException;

	public Page<ReseverCost> queryCostPage(String name, Integer pid,
			String userName, String userId, String province, String startTime,
			String endTime, Integer year, String activity, String flowStatus,
			long start, long limit, String sort, String dir)
			throws DBAccessException;

	public Double queryReseverCostSum(Integer year, String province)
			throws DBAccessException;

	public ReseverCost queryReseverDW(Integer reseverId)
			throws DBAccessException;
}
