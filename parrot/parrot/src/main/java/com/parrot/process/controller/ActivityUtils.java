package com.parrot.process.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import com.parrot.app.AppConfiguration;
import com.parrot.process.ProcessConfiguration;
import com.parrot.process.entity.Process;
import org.thorn.user.entity.User;

/**
 * @ClassName: ActivityUtils
 * @Description:
 * @author chenyun
 * @date 2012-8-13 上午10:33:49
 */
public class ActivityUtils {

	private static final String TO_PROVINCE = "送省厅审批";

	private static final String TO_CENTRE = "送非遗司审批";

	private static final String TO_SUCCESS = "审批通过";

	private static final String RETO_PROVINCE = "打回省厅重新审批";

	private static final String RETO_APPLY = "打回申报单位修改重新申报";

	private static final String TO_SAVE = "保存草稿";

	private static final String SP_SUCCESS = "success";

	private static final String SP_FAILURE = "failure";
	
	private static final String SP_APPLY = "apply";

	private static final String HANDLER_ROLE = "role";

	private static final String HANDLER_USER = "user";

	public static Set<String> getNextStep(String province, String curActivity) {

		Set<String> nextStep = new HashSet<String>();

		if (StringUtils.equals(curActivity,
				ProcessConfiguration.ACTIVITY_CREATE)
				|| StringUtils.equals(curActivity,
						ProcessConfiguration.ACTIVITY_SAVE)) {

			if (StringUtils.equals(province,
					ProcessConfiguration.CENTRE_ORG_CODE)) {
				nextStep.add(TO_CENTRE);
			} else {
				nextStep.add(TO_PROVINCE);
			}
		} else if (StringUtils.equals(curActivity,
				ProcessConfiguration.ACTIVITY_PROVINCE)) {
			nextStep.add(TO_CENTRE);
			nextStep.add(RETO_APPLY);
		} else if (StringUtils.equals(curActivity,
				ProcessConfiguration.ACTIVITY_CENTRE)) {
			nextStep.add(TO_SUCCESS);
			
			if (StringUtils.equals(province,
					ProcessConfiguration.CENTRE_ORG_CODE)) {
				nextStep.add(RETO_APPLY);
			} else {
				nextStep.add(RETO_PROVINCE);
			}
		}

		return nextStep;
	}

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-8-14 上午10:22:55
	 * @param flowId
	 * @param flowType
	 * @param user
	 * @param nextStep
	 * @param creater
	 *            创建人ID
	 * @param curActivity
	 * @param pid
	 *            第一次新建时为空，作数据保存时要重新设置值
	 * @return
	 */
	public static Process dealWithActivity(Integer flowId, String flowType,
			User user, String nextStep, String creater, String curActivity,
			Integer pid) {

		Process process = new Process();

		process.setId(flowId);
		process.setFlowType(flowType);
		process.setPid(pid);

		// 新建流程时设置起草人信息
		if (flowId == null) {
			process.setCreater(user.getUserId());
			process.setCreaterName(user.getUserName());
			process.setProvince(user.getArea());

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			process.setCreateTime(df.format(new Date()));
		}

		if (StringUtils.equals(nextStep, TO_PROVINCE)) {
			process.setActivity(ProcessConfiguration.ACTIVITY_PROVINCE);
			process.setFlowStatus(SP_APPLY);
			process.setHandlerType(HANDLER_ROLE);
			process.setHandler(AppConfiguration.ROLE_PROVINCE);

		} else if (StringUtils.equals(nextStep, TO_CENTRE)) {
			process.setActivity(ProcessConfiguration.ACTIVITY_CENTRE);
			
			if(StringUtils.equals(curActivity, ProcessConfiguration.ACTIVITY_CREATE)
					|| StringUtils.equals(curActivity, ProcessConfiguration.ACTIVITY_SAVE)) {
				process.setFlowStatus(SP_APPLY);
			} else {
				process.setFlowStatus(SP_SUCCESS);
			}
			
			process.setHandlerType(HANDLER_ROLE);
			process.setHandler(AppConfiguration.ROLE_CENTRAL);

		} else if (StringUtils.equals(nextStep, TO_SUCCESS)) {
			process.setActivity(ProcessConfiguration.ACTIVITY_FINISH);
			process.setFlowStatus(SP_SUCCESS);
			process.setHandlerType("-");
			process.setHandler("-");

		} else if (StringUtils.equals(nextStep, RETO_PROVINCE)) {
			process.setActivity(ProcessConfiguration.ACTIVITY_PROVINCE);
			process.setFlowStatus(SP_FAILURE);
			process.setHandlerType(HANDLER_ROLE);
			process.setHandler(AppConfiguration.ROLE_PROVINCE);

		} else if (StringUtils.equals(nextStep, RETO_APPLY)) {
			process.setActivity(ProcessConfiguration.ACTIVITY_CREATE);
			process.setFlowStatus(SP_FAILURE);
			process.setHandlerType(HANDLER_USER);
			process.setHandler(creater);

		} else if (StringUtils.equals(nextStep, TO_SAVE)) {
			process.setActivity(ProcessConfiguration.ACTIVITY_SAVE);
			process.setFlowStatus(SP_APPLY);
			process.setHandlerType(HANDLER_USER);
			process.setHandler(creater);
		} else {
			return null;
		}

		return process;

	}

}
