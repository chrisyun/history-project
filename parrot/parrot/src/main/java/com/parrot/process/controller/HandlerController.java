package com.parrot.process.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.parrot.app.entity.CostBudget;
import com.parrot.app.entity.ProjectCost;
import com.parrot.app.entity.ReseverCost;
import org.thorn.dao.exception.DBAccessException;
import com.parrot.process.ProcessConfiguration;
import com.parrot.process.entity.FlowMinds;
import com.parrot.process.entity.Process;
import com.parrot.process.service.IFlowService;
import org.thorn.security.SecurityUserUtils;
import org.thorn.user.entity.User;
import org.thorn.web.controller.BaseController;
import org.thorn.web.entity.Status;

/**
 * @ClassName: HandlerController
 * @Description:
 * @author chenyun
 * @date 2012-8-14 上午09:50:18
 */
@Controller
@RequestMapping("/wf")
public class HandlerController extends BaseController {

	static Logger log = LoggerFactory.getLogger(HandlerController.class);

	@Autowired
	@Qualifier("flowService")
	private IFlowService flowService;

	@RequestMapping("/modifyProcess")
	@ResponseBody
	public Status modifyProcess(HttpServletRequest request, String flowType,
			String budgetJson, Integer pid, String flowAtts, String creater) {
		Status status = new Status();

		User user = SecurityUserUtils.getCurrentUser();

		// 表单项处理
		Object form = null;
		if (StringUtils.equals(flowType, ProcessConfiguration.PROJECT_KEY)) {
			form = getProjectCost(request, flowAtts, user, pid, creater);
		}

		try {
			List<CostBudget> list = getCostBudget(budgetJson);

			flowService.modifyProcessForm(form, list);
			status.setMessage("数据修改成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据保存失败：" + e.getMessage());
			log.error("modifyProcess[Workflow] - " + e.getMessage(), e);
		} catch (Exception e) {
			status.setSuccess(false);
			status.setMessage("数据解析失败：" + e.getMessage());
			log.error("modifyProcess[Workflow] - " + e.getMessage(), e);
		}

		return status;

	}

	@RequestMapping("/cm/handlerProcess")
	@ResponseBody
	public Status handlerProcess(HttpServletRequest request, Integer mindsId,
			String minds, Integer flowId, String flowType, String nextStep,
			String budgetJson, String creater, String curActivity, Integer pid,
			String flowAtts) {
		Status status = new Status();

		User user = SecurityUserUtils.getCurrentUser();

		// 表单项处理
		Object form = null;
		if (StringUtils.equals(flowType, ProcessConfiguration.PROJECT_KEY)) {
			form = getProjectCost(request, flowAtts, user, pid, creater);
		} else if (StringUtils.equals(flowType,
				ProcessConfiguration.RESEVER_KEY)) {
			form = getReseverCost(request, flowAtts, user, pid, creater);
		}

		// 流程环节处理
		Process process = ActivityUtils.dealWithActivity(flowId, flowType,
				user, nextStep, creater, curActivity, pid);

		// 处理流程意见
		FlowMinds flowMinds = new FlowMinds();
		flowMinds.setActivityName(curActivity);
		flowMinds.setFlowId(flowId);
		flowMinds.setId(mindsId);
		flowMinds.setUserId(user.getUserId());
		flowMinds.setUserName(user.getUserName());
		flowMinds.setMind(minds);

		try {

			List<CostBudget> list = getCostBudget(budgetJson);

			flowService.dealWithEngine(process, flowMinds, form, list);
			status.setMessage("流程处理成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据保存失败：" + e.getMessage());
			log.error("handlerProcess[Workflow] - " + e.getMessage(), e);
		} catch (Exception e) {
			status.setSuccess(false);
			status.setMessage("数据解析失败：" + e.getMessage());
			log.error("handlerProcess[Workflow] - " + e.getMessage(), e);
		}

		return status;
	}

	private ProjectCost getProjectCost(HttpServletRequest request,
			String flowAtts, User user, Integer pid, String creater) {

		ProjectCost pc = new ProjectCost();

		if (pid == null) {
			pc.setCreater(user.getUserId());
			pc.setCreaterName(user.getUserName());
			pc.setProvince(user.getArea());

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			pc.setApplyTime(df.format(new Date()));
		} else {
			pc.setId(pid);
			pc.setCreater(creater);
		}
		pc.setAttids(flowAtts);

		String projectId = request.getParameter("pc_projectId");
		if (StringUtils.isNotBlank(projectId)) {
			pc.setProjectId(Integer.parseInt(projectId));
		}

		String projectName = request.getParameter("pc_projectName");
		pc.setProjectName(projectName);

		String year = request.getParameter("pc_year");
		if (StringUtils.isNotBlank(year)) {
			pc.setYear(Integer.parseInt(year));
		}

		String address = request.getParameter("pc_address");
		pc.setAddress(address);

		String postalCode = request.getParameter("pc_postalCode");
		pc.setPostalCode(postalCode);

		String contacts = request.getParameter("pc_contacts");
		pc.setContacts(contacts);

		String phone = request.getParameter("pc_phone");
		pc.setPhone(phone);

		String bankName = request.getParameter("pc_bankName");
		pc.setBankName(bankName);

		String bank = request.getParameter("pc_bank");
		pc.setBank(bank);

		String bankAccount = request.getParameter("pc_bankAccount");
		pc.setBankAccount(bankAccount);

		String companyCtf = request.getParameter("pc_companyCtf");
		pc.setCompanyCtf(companyCtf);

		String appReason = request.getParameter("pc_appReason");
		pc.setAppReason(appReason);

		String content = request.getParameter("pc_content");
		pc.setContent(content);

		String target = request.getParameter("pc_target");
		pc.setTarget(target);

		String usedYear = request.getParameter("pc_usedYear");
		pc.setUsedYear(usedYear);

		String budget = request.getParameter("pc_budget");
		pc.setBudget(budget);

		String money = request.getParameter("pc_money");
		if (StringUtils.isNotBlank(money)) {
			pc.setMoney(Double.parseDouble(money));
		}

		String ueId = request.getParameter("pc_id");
		if (StringUtils.isNotBlank(ueId)) {
			pc.setUeId(Integer.parseInt(ueId));
		}

		return pc;
	}

	private ReseverCost getReseverCost(HttpServletRequest request,
			String flowAtts, User user, Integer pid, String creater) {

		ReseverCost rc = new ReseverCost();

		if (pid == null) {
			rc.setCreater(user.getUserId());
			rc.setCreaterName(user.getUserName());
			rc.setProvince(user.getArea());

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			rc.setApplyTime(df.format(new Date()));
		} else {
			rc.setId(pid);
		}
		rc.setAttids(flowAtts);

		String reseverId = request.getParameter("rc_reseverId");
		if (StringUtils.isNotBlank(reseverId)) {
			rc.setReseverId(Integer.parseInt(reseverId));
		}

		String reseverName = request.getParameter("rc_reseverName");
		rc.setReseverName(reseverName);

		String year = request.getParameter("rc_year");
		if (StringUtils.isNotBlank(year)) {
			rc.setYear(Integer.parseInt(year));
		}

		String address = request.getParameter("rc_address");
		rc.setAddress(address);

		String postalCode = request.getParameter("rc_postalCode");
		rc.setPostalCode(postalCode);

		String contacts = request.getParameter("rc_contacts");
		rc.setContacts(contacts);

		String phone = request.getParameter("rc_phone");
		rc.setPhone(phone);

		String appReason = request.getParameter("rc_appReason");
		rc.setAppReason(appReason);

		String content = request.getParameter("rc_content");
		rc.setContent(content);

		String target = request.getParameter("rc_target");
		rc.setTarget(target);

		String budget = request.getParameter("rc_budget");
		rc.setBudget(budget);

		String applyMoney = request.getParameter("rc_applyMoney");
		if (StringUtils.isNotBlank(applyMoney)) {
			rc.setApplyMoney(Double.parseDouble(applyMoney));
		}
		String givenMoney = request.getParameter("rc_givenMoney");
		if (StringUtils.isNotBlank(givenMoney)) {
			rc.setGivenMoney(Double.parseDouble(givenMoney));
		}

		return rc;
	}

	private List<CostBudget> getCostBudget(String budgetJson)
			throws JsonParseException, JsonMappingException, IOException {
		if (StringUtils.isEmpty(budgetJson)) {
			return new ArrayList<CostBudget>();
		}

		ObjectMapper mapper = new ObjectMapper();

		CostBudget[] budgets = mapper.readValue(budgetJson, CostBudget[].class);

		return Arrays.asList(budgets);
	}

}
