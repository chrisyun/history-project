package com.parrot.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.parrot.app.AppConfiguration;
import com.parrot.app.entity.Project;
import com.parrot.app.entity.ProjectCost;
import com.parrot.app.entity.ProjectFund;
import com.parrot.app.entity.UserExtend;
import com.parrot.app.service.IProjectService;
import com.parrot.process.ProcessConfiguration;

import org.thorn.core.excel.ArrayAdapter;
import org.thorn.core.excel.ExcelStyle;
import org.thorn.core.excel.ExcelUtils;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.core.util.ReflectUtils;
import org.thorn.dao.core.Configuration;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.security.SecurityUserUtils;
import org.thorn.user.entity.User;
import org.thorn.web.controller.BaseController;
import org.thorn.web.entity.JsonResponse;
import org.thorn.web.entity.Page;
import org.thorn.web.entity.Status;
import org.thorn.web.util.DDUtils;
import org.thorn.web.util.ResponseHeaderUtils;

/**
 * @ClassName: ProjectController
 * @Description:
 * @author chenyun
 * @date 2012-8-10 上午11:10:06
 */
@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {

	static Logger log = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	@Qualifier("projectService")
	private IProjectService projectService;

	@RequestMapping("/saveOrModifyFund")
	@ResponseBody
	public Status saveOrModifyFund(String fundJson) {
		Status status = new Status();

		try {
			if (StringUtils.isNotBlank(fundJson)) {
				ObjectMapper mapper = new ObjectMapper();

				ProjectFund[] array = mapper.readValue(fundJson,
						ProjectFund[].class);
				List<ProjectFund> list = Arrays.asList(array);
				projectService.saveOrModify(list);
			}
			status.setMessage("数据更新成功！");
		} catch (Exception e) {
			status.setSuccess(false);
			status.setMessage("数据保存失败：" + e.getMessage());
			log.error("saveOrModifyFund[ProjectFund] - " + e.getMessage(),
					e);
		}

		return status;
	}

	@RequestMapping("/getProjectDW")
	@ResponseBody
	public JsonResponse<UserExtend> getProjectDW() {
		JsonResponse<UserExtend> json = new JsonResponse<UserExtend>();

		try {
			User user = SecurityUserUtils.getCurrentUser();
			json.setObj(projectService.queryUserExtend(user.getUserId()));
		} catch (DBAccessException e) {
			json.setSuccess(false);
			json.setMessage("数据获取失败：" + e.getMessage());
			log.error("getProjectDW[UserExtend] - " + e.getMessage(), e);
		}

		return json;
	}

	@RequestMapping("/dw/getProjectDWPage")
	@ResponseBody
	public Page<UserExtend> getProjectDWPage(long start, long limit,
			String sort, String dir, String orgCode, String userName,
			String cumail, String userId) {
		Page<UserExtend> page = new Page<UserExtend>();

		try {
			User user = SecurityUserUtils.getCurrentUser();
			List<String> roles = SecurityUserUtils.getRoleList();

			if (!SecurityUserUtils.isSysAdmin()
					&& !roles.contains(AppConfiguration.ROLE_CENTRAL)) {
				orgCode = user.getOrgCode();
			}

			page = projectService.queryPage(orgCode, userName, cumail, userId,
					start, limit, sort, dir);
		} catch (DBAccessException e) {
			log.error("getProjectDWPage[UserExtend] - " + e.getMessage(), e);
		}

		return page;
	}

	@RequestMapping("/dw/saveOrModifyProjectDW")
	@ResponseBody
	public Status saveOrModifyProjectDW(UserExtend ue, String opType) {
		Status status = new Status();

		try {
			if (LocalStringUtils.equals(opType, Configuration.OP_SAVE)) {
				projectService.save(ue);
			} else if (LocalStringUtils.equals(opType, Configuration.OP_MODIFY)) {
				projectService.modify(ue);
			}
			status.setMessage("修改项目保护单位基本信息成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据保存失败：" + e.getMessage());
			log.error("saveOrModifyProjectDW[UserExtend] - " + e.getMessage(),
					e);
		}

		return status;
	}

	@RequestMapping("/dw/getProjectDWExcel")
	public void exportProjectDWExcel(String orgCode, String userName,
			String cumail, String userId, HttpServletResponse response) {

		try {
			User user = SecurityUserUtils.getCurrentUser();
			List<String> roles = SecurityUserUtils.getRoleList();

			if (!SecurityUserUtils.isSysAdmin()
					&& !roles.contains(AppConfiguration.ROLE_CENTRAL)) {
				orgCode = user.getOrgCode();
			}

			List<UserExtend> list = projectService.queryList(orgCode, userName,
					cumail, userId);

			String title = "项目承担单位列表";
			ResponseHeaderUtils.setExcelResponse(response, title);

			String[] header = new String[] { "单位编码", "承担单位名称", "联系地址", "单位负责人",
					"联系电话", "开户名称", "开户银行", "银行账号" };
			int[] columnWidth = new int[] { 150, 250, 250, 150, 150, 150, 150,
					250 };

			String[] orderArray = new String[] { "userId", "userName",
					"address", "contacts", "phone", "bankName", "bank",
					"bankAccount" };
			List<Object[]> dataSource = ReflectUtils.object2Array(list);

			ExcelStyle style = new ExcelStyle();

			ArrayAdapter adapter = new ArrayAdapter(header, orderArray,
					dataSource);

			ExcelUtils.write2Excel(adapter, "项目承担单位", columnWidth, style,
					response.getOutputStream());
			response.getOutputStream().flush();
		} catch (DBAccessException e) {
			log.error("exportProjectDWExcel[UserExtend] - " + e.getMessage(), e);
		} catch (IOException e) {
			log.error("exportProjectDWExcel[UserExtend] - " + e.getMessage(), e);
		}
	}

	@RequestMapping("/saveOrModifyProject")
	@ResponseBody
	public Status saveOrModifyProject(Project project, String opType) {
		Status status = new Status();

		try {

			if (LocalStringUtils.equals(opType, Configuration.OP_SAVE)) {
				projectService.save(project);
				status.setMessage("新增非遗项目成功！");
			} else if (LocalStringUtils.equals(opType, Configuration.OP_MODIFY)) {
				projectService.modify(project);
				status.setMessage("修改非遗项目成功！");
			}

		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据保存失败：" + e.getMessage());
			log.error("saveOrModifyProject[Project] - " + e.getMessage(), e);
		}

		return status;
	}

	@RequestMapping("/deleteProject")
	@ResponseBody
	public Status deleteProject(String ids) {
		Status status = new Status();

		try {
			projectService.delete(ids);
			status.setMessage("数据删除成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据删除失败：" + e.getMessage());
			log.error("deleteProject[Project] - " + e.getMessage(), e);
		}

		return status;
	}

	@RequestMapping("/getProjectByProvince")
	@ResponseBody
	public List<Project> getProjectByProvince(String province) {
		List<Project> list = new ArrayList<Project>();

		if (StringUtils.isBlank(province)) {
			return list;
		}

		try {
			list = projectService.queryProjectList(province, null);
		} catch (DBAccessException e) {
			log.error("getProjectByProvince[Project] - " + e.getMessage(), e);
		}

		return list;
	}

	@RequestMapping("/getProjectById")
	@ResponseBody
	public JsonResponse<Project> getProjectById(Integer projectId) {
		JsonResponse<Project> json = new JsonResponse<Project>();

		if (projectId == null) {
			return json;
		}

		try {
			json.setObj(projectService.queryProject(projectId));
		} catch (DBAccessException e) {
			json.setSuccess(false);
			json.setMessage("查询项目信息失败");
			log.error("getProjectById[Project] - " + e.getMessage(), e);
		}

		return json;
	}

	@RequestMapping("/getProjectByUser")
	@ResponseBody
	public List<Project> getProjectByUser() {
		List<Project> list = new ArrayList<Project>();

		try {
			User user = SecurityUserUtils.getCurrentUser();
			list = projectService.queryProjectList(null, user.getUserId());
		} catch (DBAccessException e) {
			log.error("getProjectByUser[Project] - " + e.getMessage(), e);
		}

		return list;
	}

	@RequestMapping("/getProjectOfDW")
	@ResponseBody
	public Page<Project> getProjectByUser(String userId) {
		Page<Project> page = new Page<Project>();

		try {
			page.setReslutSet(projectService.queryProjectList(null, userId));
		} catch (DBAccessException e) {
			log.error("getProjectByUser[Project] - " + e.getMessage(), e);
		}

		return page;
	}

	@RequestMapping("/getProjectPage")
	@ResponseBody
	public Page<Project> getProjectPage(long start, long limit, String sort,
			String dir, String name, String code, String userName, String type,
			String isUnProject, String province, String provinceArea,
			String isJhxm, String isWhxm, String minority, String batchNum) {
		Page<Project> page = new Page<Project>();
		String userId = null;

		try {
			User user = SecurityUserUtils.getCurrentUser();
			List<String> roleList = SecurityUserUtils.getRoleList();

			if (!SecurityUserUtils.isSysAdmin()
					&& !roleList.contains(AppConfiguration.ROLE_CENTRAL)) {

				if (roleList.contains(AppConfiguration.ROLE_PROVINCE)) {
					province = user.getArea();
				} else {
					province = user.getArea();
					userId = user.getUserId();
				}
			}

			page = projectService.queryPage(name, code, userName, userId, type,
					isUnProject, province, provinceArea, isJhxm, isWhxm,
					minority, batchNum, start, limit, sort, dir);
		} catch (DBAccessException e) {
			log.error("getProjectPage[Project] - " + e.getMessage(), e);
		}

		return page;
	}

	@RequestMapping("/getProjectCostById")
	@ResponseBody
	public JsonResponse<ProjectCost> getProjectCostById(Integer id) {
		JsonResponse<ProjectCost> json = new JsonResponse<ProjectCost>();

		try {
			json.setObj(projectService.queryProjectCost(id));
		} catch (DBAccessException e) {
			json.setSuccess(false);
			json.setMessage("项目费用获取失败：" + e.getMessage());
			log.error("getProjectCostById[ProjectCost] - " + e.getMessage(), e);
		}

		return json;
	}

	@RequestMapping("/getProjectCostPage")
	@ResponseBody
	public Page<ProjectCost> getProjectCostPage(long start, long limit,
			String sort, String dir, String name, Integer pid, String userName,
			String userId, String isUnProject, String province,
			String activity, String flowStatus, String projectType,
			String startTime, String endTime, Integer year) {
		Page<ProjectCost> page = new Page<ProjectCost>();

		try {
			User user = SecurityUserUtils.getCurrentUser();
			List<String> roleList = SecurityUserUtils.getRoleList();

			if (!SecurityUserUtils.isSysAdmin()
					&& !roleList.contains(AppConfiguration.ROLE_CENTRAL)) {

				if (roleList.contains(AppConfiguration.ROLE_PROVINCE)) {
					province = user.getArea();
				} else {
					province = user.getArea();
					userId = user.getUserId();
				}
			}

			page = projectService.queryCostPage(name, pid, userName, userId,
					isUnProject, province, projectType, startTime, endTime,
					year, null, activity, flowStatus, start, limit, sort, dir);
		} catch (DBAccessException e) {
			log.error("getProjectCostPage[ProjectCost] - " + e.getMessage(), e);
		}

		return page;
	}

	@RequestMapping("/getProjectCostSummary")
	@ResponseBody
	public Page<ProjectCost> getProjectCostSummary(Integer year,
			String province, String provinceArea, String sort, String dir) {
		Page<ProjectCost> page = new Page<ProjectCost>();

		try {
			User user = SecurityUserUtils.getCurrentUser();
			List<String> roleList = SecurityUserUtils.getRoleList();

			if (!SecurityUserUtils.isSysAdmin()
					&& !roleList.contains(AppConfiguration.ROLE_CENTRAL)) {

				province = user.getArea();
			}

			page = projectService.queryCostPage(null, null, null, null, null,
					province, null, null, null, year, provinceArea,
					ProcessConfiguration.ACTIVITY_FINISH, "success", null,
					null, sort, dir);
		} catch (DBAccessException e) {
			log.error("getProjectCostSummary[ProjectCost] - " + e.getMessage(),
					e);
		}

		return page;
	}

	@RequestMapping("/exportProjectCostExcel")
	public void exportProjectCostExcel(Integer year, String province,
			String provinceArea, HttpServletResponse response) {

		try {
			User user = SecurityUserUtils.getCurrentUser();
			List<String> roleList = SecurityUserUtils.getRoleList();

			if (!SecurityUserUtils.isSysAdmin()
					&& !roleList.contains(AppConfiguration.ROLE_CENTRAL)) {

				province = user.getArea();
			}

			String provinceName = DDUtils.queryDdById("AREA", province);

			List<ProjectCost> list = projectService.queryCostPage(null, null,
					null, null, null, province, null, null, null, year,
					provinceArea, "审批完成已归档", "success", null, null, null, null)
					.getReslutSet();

			StringBuilder title = new StringBuilder(String.valueOf(year));
			title.append("年度");

			if (StringUtils.isBlank(provinceName)) {
				String areaName = DDUtils.queryDdById("PROVINCE_AREA",
						provinceArea);
				title.append(areaName);
			} else {
				title.append(provinceName);
			}

			title.append("国家级非物质文化遗产代表性项目补助费申报汇总表");

			ResponseHeaderUtils.setExcelResponse(response, title.toString());

			String[] header = new String[] { "序号", "项目编号", "项目名称", "申报地区或单位",
					"资金申报单位", "金额（万元）", "备注" };
			int[] columnWidth = new int[] { 100, 150, 250, 250, 250, 100, 500 };

			int i = 1;
			Double money = 0.0;
			for (ProjectCost pc : list) {
				pc.setAddress(String.valueOf(i));

				if (StringUtils.equals(pc.getIsUnProject(),
						Configuration.DB_YES)) {
					pc.setProjectName("★" + pc.getProjectName());
				}

				money += pc.getMoney();
				i++;
			}

			ProjectCost pc = new ProjectCost();
			pc.setAddress("合计");
			pc.setCode("-");
			pc.setProjectName("-");
			pc.setCreaterName("-");
			pc.setMoney(money);
			list.add(pc);

			String[] orderArray = new String[] { "address", "code",
					"projectName", "area", "createrName", "money", "appReason" };
			List<Object[]> dataSource = ReflectUtils.object2Array(list);

			ExcelStyle style = new ExcelStyle();

			List<Object[]> titles = new ArrayList<Object[]>();
			titles.add(new Object[] { title.toString() });

			List<Integer[]> merCells = new ArrayList<Integer[]>();
			merCells.add(new Integer[] { 0, 0, 0, 6 });

			ArrayAdapter adapter = new ArrayAdapter(header, orderArray,
					dataSource, titles, merCells);

			ExcelUtils.write2Excel(adapter, "汇总表", columnWidth, style,
					response.getOutputStream());
			response.getOutputStream().flush();
		} catch (DBAccessException e) {
			log.error(
					"exportProjectCostExcel[ProjectCost] - " + e.getMessage(),
					e);
		} catch (IOException e) {
			log.error(
					"exportProjectCostExcel[ProjectCost] - " + e.getMessage(),
					e);
		}
	}

	@RequestMapping("/getProjectFundById")
	@ResponseBody
	public Page<ProjectFund> getProjectFundById(Integer pid) {
		Page<ProjectFund> page = new Page<ProjectFund>();

		try {
			page.setReslutSet(projectService.queryProjectFund(pid));
		} catch (DBAccessException e) {
			log.error("getProjectFundById[ProjectFund] - " + e.getMessage(), e);
		}

		return page;
	}

}
