package com.parrot.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.parrot.app.entity.Project;
import com.parrot.app.entity.ProjectCost;
import com.parrot.app.entity.ProjectFund;
import com.parrot.app.entity.UserExtend;
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
public class ProjectServiceImpl implements IProjectService {

	@Autowired
	@Qualifier("myBatisDaoSupport")
	private MyBatisDaoSupport myBatisDaoSupport;

	@Logging("项目信息保存")
	public void save(Project project) throws DBAccessException {
		myBatisDaoSupport.save(project);
	}

	@Logging("项目信息修改")
	public void modify(Project project) throws DBAccessException {
		myBatisDaoSupport.modify(project);
	}

	@Logging("项目信息删除")
	public void delete(String ids) throws DBAccessException {
		List<String> list = LocalStringUtils.splitStr2Array(ids);
		myBatisDaoSupport.deleteForBatch(list, Project.class);
	}

	public Page<Project> queryPage(String name, String code, String userName,
			String userId, String type, String isUnProject, String province,
			String provinceArea, String isJhxm, String isWhxm, String minority,
			String batchNum, long start, long limit, String sort, String dir)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("name", name);
		filter.put("code", code);
		filter.put("userName", userName);
		filter.put("type", type);
		filter.put("isUnProject", isUnProject);
		filter.put("userId", userId);
		filter.put("province", province);
		filter.put("provinceArea", provinceArea);
		filter.put("isJhxm", isJhxm);
		filter.put("isWhxm", isWhxm);
		filter.put("minority", minority);
		filter.put("batchNum", batchNum);
		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);
		
		if(StringUtils.isEmpty(sort)) {
			filter.put(Configuration.SROT_NAME, "P.PROVINCE");
			filter.put(Configuration.ORDER_NAME, "asc");
		} else {
			filter.put(Configuration.SROT_NAME, sort);
			filter.put(Configuration.ORDER_NAME, dir);
		}
		
		Page<Project> page = new Page<Project>();

		page.setTotal(myBatisDaoSupport.queryCount(filter, Project.class));
		if (page.getTotal() > 0) {
			page.setReslutSet(myBatisDaoSupport
					.queryList(filter, Project.class));
		}

		return page;
	}

	public Project queryProject(String name, String area)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("name", name);
		filter.put("area", area);

		return (Project) myBatisDaoSupport.queryOne(filter, Project.class);

	}

	public List<Project> queryProjectList(String province, String userId)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("province", province);
		filter.put("userId", userId);

		return myBatisDaoSupport.queryList(filter, Project.class);
	}

	public ProjectCost queryProjectCost(Integer id) throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("id", id);

		return (ProjectCost) myBatisDaoSupport.queryOne(filter,
				ProjectCost.class);
	}

	public Page<ProjectCost> queryCostPage(String name, Integer pid,
			String userName, String userId, String isUnProject,
			String province, String projectType, String startTime,
			String endTime, Integer year, String provinceArea, String activity,
			String flowStatus, Long start, Long limit, String sort, String dir)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("projectName", name);
		filter.put("projectId", pid);
		filter.put("createrName", userName);
		filter.put("type", projectType);
		filter.put("isUnProject", isUnProject);
		filter.put("creater", userId);
		filter.put("province", province);
		filter.put("startTime", startTime);
		filter.put("endTime", endTime);
		filter.put("year", year);
		filter.put("endTime", endTime);
		filter.put("activity", activity);
		filter.put("flowStatus", flowStatus);
		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);

		if (LocalStringUtils.isEmpty(sort)) {
			filter.put(Configuration.SROT_NAME, "P.APPLYTIME");
			filter.put(Configuration.ORDER_NAME, Configuration.ORDER_ASC);
		} else {
			filter.put(Configuration.SROT_NAME, sort);
			filter.put(Configuration.ORDER_NAME, dir);
		}

		Page<ProjectCost> page = new Page<ProjectCost>();

		page.setTotal(myBatisDaoSupport.queryCount(filter, ProjectCost.class));
		if (page.getTotal() > 0) {
			page.setReslutSet(myBatisDaoSupport.queryList(filter,
					ProjectCost.class));
		}

		return page;
	}

	public Double queryProjectCostSum(Integer year, String province)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("province", province);
		filter.put("year", year);

		long money = myBatisDaoSupport.queryCount(filter,
				"ProjectCostMapper.selectCostSum");

		return (double) money / 10000;
	}

	public UserExtend queryUserExtend(String userId) throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("userId", userId);
		return (UserExtend) myBatisDaoSupport
				.queryOne(filter, UserExtend.class);
	}

	@Logging("项目承担单位初始化")
	public void save(UserExtend ue) throws DBAccessException {
		myBatisDaoSupport.save(ue);
	}

	@Logging("项目承担单位修改")
	public void modify(UserExtend ue) throws DBAccessException {
		myBatisDaoSupport.modify(ue);
	}

	public Page<UserExtend> queryPage(String orgCode, String userName,
			String cumail, String userId, long start, long limit, String sort,
			String dir) throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("orgCode", orgCode);
		filter.put("userName", userName);
		filter.put("cumail", cumail);
		filter.put("userId", userId);

		filter.put("isShow", Configuration.DB_YES);
		filter.put("isDisabled", Configuration.DB_NO);

		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);

		if (LocalStringUtils.isEmpty(sort)) {
			filter.put(Configuration.SROT_NAME, "U.SORTNUM");
			filter.put(Configuration.ORDER_NAME, Configuration.ORDER_ASC);
		} else {
			filter.put(Configuration.SROT_NAME, sort);
			filter.put(Configuration.ORDER_NAME, dir);
		}

		Page<UserExtend> page = new Page<UserExtend>();

		page.setTotal(myBatisDaoSupport.queryCount(filter, UserExtend.class));
		if (page.getTotal() > 0) {
			page.setReslutSet(myBatisDaoSupport.queryList(filter,
					UserExtend.class));
		}

		return page;
	}

	public List<UserExtend> queryList(String orgCode, String userName,
			String cumail, String userId) throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("orgCode", orgCode);
		filter.put("userName", userName);
		filter.put("cumail", cumail);
		filter.put("userId", userId);

		filter.put("isShow", Configuration.DB_YES);
		filter.put("isDisabled", Configuration.DB_NO);

		return myBatisDaoSupport.queryList(filter, UserExtend.class);
	}

	public Project queryProject(Integer projectId) throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("id", projectId);

		return (Project) myBatisDaoSupport.queryOne(filter, Project.class);
	}

	public void save(ProjectFund pFund) throws DBAccessException {
		myBatisDaoSupport.save(pFund);
	}
	
	public void saveOrModify(List<ProjectFund> list) throws DBAccessException {
		for (ProjectFund pFund : list) {
			if(pFund.getId() == null) {
				myBatisDaoSupport.save(pFund);
			} else {
				myBatisDaoSupport.modify(pFund);
			}
		}
	}

	public List<ProjectFund> queryProjectFund(Integer pid)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("pid", pid);

		return myBatisDaoSupport.queryList(filter, ProjectFund.class);
	}

}
