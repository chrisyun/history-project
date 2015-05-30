package com.parrot.process.service;

import java.util.Collection;
import java.util.List;

import com.parrot.app.entity.CostBudget;
import org.thorn.dao.exception.DBAccessException;
import com.parrot.process.entity.FlowMinds;
import com.parrot.process.entity.Process;
import org.thorn.web.entity.Page;

/**
 * @ClassName: IFlowService
 * @Description:
 * @author chenyun
 * @date 2012-8-10 下午05:18:25
 */
public interface IFlowService {

	public List<FlowMinds> queryFlowMinds(Integer flowId)
			throws DBAccessException;

	public void dealWithEngine(Process process, FlowMinds flowMinds,
			Object form, List<CostBudget> budgets) throws DBAccessException;

	public void modifyProcessForm(Object form, List<CostBudget> budgets)
			throws DBAccessException;

	public Page<Process> queryPendingProcess(String flowType, String province,
			String userId, Collection<String> roleList, String flowStatus,
			String creater, String createrName, String startTime,
			String endTime, String pName, long start, long limit, String sort, String dir)
			throws DBAccessException;

	public Process queryProcess(Integer pid, String flowType, Integer id)
			throws DBAccessException;

	public void deleteProcess(String id, String flowType, String pid)
			throws DBAccessException;

	public List<CostBudget> queryCostBudget(Integer pid, String type)
			throws DBAccessException;
}
