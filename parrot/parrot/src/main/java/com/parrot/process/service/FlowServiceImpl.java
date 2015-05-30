package com.parrot.process.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.parrot.app.entity.CostBudget;
import com.parrot.app.entity.ProjectCost;
import com.parrot.app.entity.ReseverCost;
import com.parrot.app.entity.UserExtend;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.core.Configuration;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.dao.mybatis.helper.MyBatisDaoSupport;
import org.thorn.log.NoLogging;
import com.parrot.process.ProcessConfiguration;
import com.parrot.process.entity.FlowMinds;
import com.parrot.process.entity.Process;
import org.thorn.web.entity.Page;

/**
 * @ClassName: FlowServiceImpl
 * @Description:
 * @author chenyun
 * @date 2012-8-10 下午05:18:47
 */
public class FlowServiceImpl implements IFlowService {

	@Autowired
	@Qualifier("myBatisDaoSupport")
	private MyBatisDaoSupport myBatisDaoSupport;

	public List<FlowMinds> queryFlowMinds(Integer flowId)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("flowId", flowId);

		return myBatisDaoSupport.queryList(filter, FlowMinds.class);

	}

	@NoLogging
	public void modifyProcessForm(Object form, List<CostBudget> budgets)
			throws DBAccessException {
		myBatisDaoSupport.modify(form);
		
		Integer pid = null;
		String flowType = "";
		if (form instanceof ProjectCost) {
			pid = ((ProjectCost) form).getId();
			flowType = ProcessConfiguration.PROJECT_KEY;
			
			//修改承担单位信息
			UserExtend ue = new UserExtend();
			try {
				BeanUtils.copyProperties(ue, form);
				ue.setUserId(((ProjectCost) form).getCreater());
				myBatisDaoSupport.modify(ue);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} else if (form instanceof ReseverCost) {
			pid = ((ReseverCost) form).getId();
			flowType = ProcessConfiguration.RESEVER_KEY;
		}
		
		
		// 保存预算明细
		for (CostBudget budget : budgets) {

			budget.setPid(pid);
			budget.setType(flowType);

			if (budget.getId() == null) {
				myBatisDaoSupport.save(budget);
			} else {
				myBatisDaoSupport.modify(budget);
			}
		}
	}

	@NoLogging
	public void dealWithEngine(Process process, FlowMinds flowMinds,
			Object form, List<CostBudget> budgets) throws DBAccessException {
		//true为新增，false为修改
		boolean ueOp = false;
		
		// 先处理表单项
		if (process.getPid() == null) {
			ueOp = true;
			myBatisDaoSupport.save(form);
		} else {
			myBatisDaoSupport.modify(form);
		}

		Integer pid = null;
		if (form instanceof ProjectCost) {
			pid = ((ProjectCost) form).getId();
			
			//修改承担单位信息
			UserExtend ue = new UserExtend();
			try {
				BeanUtils.copyProperties(ue, form);
				ue.setUserId(((ProjectCost) form).getCreater());
				
				if(ueOp && ((ProjectCost) form).getUeId() == null) {
					myBatisDaoSupport.save(ue);
				} else if(StringUtils.isNotBlank(ue.getUserId())) {
					myBatisDaoSupport.modify(ue);
				}

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		} else if (form instanceof ReseverCost) {
			pid = ((ReseverCost) form).getId();
		}

		// 处理流程
		process.setPid(pid);
		if (process.getId() == null) {
			myBatisDaoSupport.save(process);
		} else {
			myBatisDaoSupport.modify(process);
		}

		// 处理流程意见
		flowMinds.setFlowId(process.getId());
		if (flowMinds.getId() == null) {
			myBatisDaoSupport.save(flowMinds);
		} else {
			myBatisDaoSupport.modify(flowMinds);
		}

		// 保存预算明细
		for (CostBudget budget : budgets) {

			budget.setPid(pid);
			budget.setType(process.getFlowType());

			if (budget.getId() == null) {
				myBatisDaoSupport.save(budget);
			} else {
				myBatisDaoSupport.modify(budget);
			}
		}
	}

	public Page<Process> queryPendingProcess(String flowType, String province,
			String userId, Collection<String> roleList, String flowStatus,
			String creater, String createrName, String startTime,
			String endTime, String pName, long start, long limit, String sort, String dir)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("flowType", flowType);
		filter.put("province", province);
		filter.put("userId", userId);
		filter.put("roleList", roleList);
		filter.put("flowStatus", flowStatus);
		filter.put("creater", creater);
		filter.put("createrName", createrName);
		filter.put("startTime", startTime);
		filter.put("endTime", endTime);
		filter.put("pName", pName);

		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);

		if (LocalStringUtils.isEmpty(sort)) {
			filter.put(Configuration.SROT_NAME, "CREATETIME");
			filter.put(Configuration.ORDER_NAME, Configuration.ORDER_ASC);
		} else {
			filter.put(Configuration.SROT_NAME, sort);
			filter.put(Configuration.ORDER_NAME, dir);
		}

		Page<Process> page = new Page<Process>();

		page.setTotal(myBatisDaoSupport.queryCount(filter, Process.class));
		if (page.getTotal() > 0) {
			page.setReslutSet(myBatisDaoSupport
					.queryList(filter, Process.class));
		}

		return page;
	}

	public Process queryProcess(Integer pid, String flowType, Integer id)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("flowType", flowType);
		filter.put("pid", pid);
		filter.put("id", id);

		return (Process) myBatisDaoSupport.queryOne(filter, Process.class);
	}

	public void deleteProcess(String id, String flowType, String pid)
			throws DBAccessException {

		List<String> flowIds = new ArrayList<String>();
		flowIds.add(id);

		List<String> pids = new ArrayList<String>();
		pids.add(pid);

		myBatisDaoSupport.deleteForBatch(flowIds, Process.class);
		myBatisDaoSupport.deleteForBatch(flowIds, FlowMinds.class);

		if (StringUtils.equals(flowType, ProcessConfiguration.PROJECT_KEY)) {
			myBatisDaoSupport.deleteForBatch(pids, ProjectCost.class);
		} else if (StringUtils.equals(flowType,
				ProcessConfiguration.RESEVER_KEY)) {
			myBatisDaoSupport.deleteForBatch(pids, ReseverCost.class);
		}

		// 删除预算明细
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("type", flowType);
		filter.put("pid", pid);
		myBatisDaoSupport.delete(filter, "CostBudgetMapper.delete");
	}

	public List<CostBudget> queryCostBudget(Integer pid, String type)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("type", type);
		filter.put("pid", pid);

		return myBatisDaoSupport.queryList(filter, CostBudget.class);
	}

}
