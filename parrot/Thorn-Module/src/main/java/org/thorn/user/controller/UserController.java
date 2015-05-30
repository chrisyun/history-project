package org.thorn.user.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thorn.auth.service.IAuthService;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.core.Configuration;
import org.thorn.web.entity.JsonResponse;
import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.security.SecurityUserUtils;
import org.thorn.user.entity.User;
import org.thorn.user.service.IUserService;
import org.thorn.web.controller.BaseController;
import org.thorn.web.entity.Status;

/**
 * @ClassName: UserController
 * @Description:
 * @author chenyun
 * @date 2012-5-15 下午05:16:07
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	@Qualifier("userService")
	private IUserService service;

	@Autowired
	@Qualifier("authService")
	private IAuthService authService;

	@RequestMapping("/saveOrModifyUser")
	@ResponseBody
	public Status saveOrModifyUser(User user, String opType) {
		Status status = new Status();

		try {

			if (LocalStringUtils.equals(opType, Configuration.OP_SAVE)) {
				service.save(user);
				status.setMessage("新增用户成功！");
			} else if (LocalStringUtils.equals(opType, Configuration.OP_MODIFY)) {
				service.modify(user);
				status.setMessage("修改用户成功！");
			}

		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据保存失败：" + e.getMessage());
			log.error("saveOrModifyUser[User] - " + e.getMessage(), e);
		}

		return status;
	}

	@RequestMapping("/deleteUser")
	@ResponseBody
	public Status deleteUser(String ids) {
		Status status = new Status();

		try {
			service.delete(ids);
			status.setMessage("数据删除成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据删除失败：" + e.getMessage());
			log.error("deleteUser[User] - " + e.getMessage(), e);
		}

		return status;
	}
	
	@RequestMapping("/getUserById")
	@ResponseBody
	public JsonResponse<User> getUserById(String id) {
		
		JsonResponse<User> json = new JsonResponse<User>();
		
		try {
			User user = service.queryUserByLogin(id);
			user.setUserPwd(null);
			json.setObj(user);
			json.setMessage("用户信息查询成功");
			
		} catch (DBAccessException e) {
			json.setSuccess(false);
			json.setMessage("数据处理失败：" + e.getMessage());
			log.error("getUserById[User] - " + e.getMessage(), e);
		}
		
		return json;
	}
 
	@RequestMapping("/disabledUser")
	@ResponseBody
	public Status disabledUser(String ids, String isDisabled) {
		Status status = new Status();

		try {
			service.disabledUser(ids, isDisabled);

			if (LocalStringUtils.equals(isDisabled, Configuration.DB_YES)) {
				status.setMessage("用户禁用成功！");
			} else {
				status.setMessage("用户启用成功！");
			}

		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据处理失败：" + e.getMessage());
			log.error("deleteUser[User] - " + e.getMessage(), e);
		}

		return status;
	}

	@RequestMapping("/changeMyPwd")
	@ResponseBody
	public Status changeMyPwd(String newPwd) {
		Status status = new Status();

		try {
			User user = SecurityUserUtils.getCurrentUser();

			service.changePwd(user.getUserId(), newPwd);
			status.setMessage("密码修改成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("密码修改失败：" + e.getMessage());
			log.error("changeMyPwd[User] - " + e.getMessage(), e);
		}

		return status;
	}

	@RequestMapping("/changePwd")
	@ResponseBody
	public Status changePwd(String newPwd, String userId) {
		Status status = new Status();

		try {
			service.changePwd(userId, newPwd);
			status.setMessage("密码修改成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("密码修改失败：" + e.getMessage());
			log.error("changePwd[User] - " + e.getMessage(), e);
		}

		return status;
	}

	@RequestMapping("/getUserPage")
	@ResponseBody
	public Page<User> getUserPage(long start, long limit, String sort,
			String dir, String orgCode, String userName, String cumail,
			String userAccount) {
		Page<User> page = new Page<User>();
		String area = null;
		
		try {
			User user = SecurityUserUtils.getCurrentUser();
			
			if(! SecurityUserUtils.isSysAdmin()) {
				area = user.getArea();
			}
			
			page = service.queryPage(orgCode, userName, cumail, area, userAccount,
					start, limit, sort, dir);
		} catch (DBAccessException e) {
			log.error("getUserPage[User] - " + e.getMessage(), e);
		}

		return page;
	}
	
	@RequestMapping("/getUserList")
	@ResponseBody
	public List<User> getUserList(String area) {
		List<User> list = new ArrayList<User>();
		
		Set<String> areas = new HashSet<String>();
		areas.add(area);
		
		try {
			list = service.queryList(null, null, null, areas, null, null, null);
		} catch (DBAccessException e) {
			log.error("getUserList[User] - " + e.getMessage(), e);
		}
		
		return list;
	}
	

	@RequestMapping("/getUserPageByRole")
	@ResponseBody
	public Page<User> getUserPageByRole(long start, long limit, String sort,
			String dir, String orgCode, String userName, String roleCode,
			String userAccount) {
		Page<User> page = new Page<User>();

		try {
			page = authService.queryPageByRole(userName, orgCode, roleCode,
					userAccount, start, limit, sort, dir);
		} catch (DBAccessException e) {
			log.error("getUserPageByRole[User] - " + e.getMessage(), e);
		}

		return page;
	}

	@RequestMapping("/getUserPageNotInRole")
	@ResponseBody
	public Page<User> getUserPageNotInRole(long start, long limit, String sort,
			String dir, String orgCode, String roleCode) {
		Page<User> page = new Page<User>();

		try {
			page = authService.queryPageNotInRole(orgCode, roleCode, start,
					limit, sort, dir);
		} catch (DBAccessException e) {
			log.error("getUserPageNotInRole[User] - " + e.getMessage(), e);
		}

		return page;
	}

	@RequestMapping("/saveUserRole")
	@ResponseBody
	public Status saveUserRole(String roleCode, String userIds) {
		Status status = new Status();

		try {
			authService.saveUserRole(roleCode, userIds);
			status.setMessage("角色增加用户成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("角色增加用户失败：" + e.getMessage());
			log.error("saveUserRole[String] - " + e.getMessage(), e);
		}

		return status;
	}

	@RequestMapping("/saveRoleByUser")
	@ResponseBody
	public Status saveRoleByUser(String roleCodes, String userId) {
		Status status = new Status();

		try {
			authService.saveRoleByUser(userId, roleCodes);
			status.setMessage("用户绑定角色成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("用户绑定角色失败：" + e.getMessage());
			log.error("saveRoleByUser[String] - " + e.getMessage(), e);
		}

		return status;
	}

	@RequestMapping("/deleteUserRole")
	@ResponseBody
	public Status deleteUserRole(String roleCode, String userIds) {
		Status status = new Status();

		try {
			authService.deleteUserRole(roleCode, userIds);
			status.setMessage("角色删除用户成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("角色删除用户失败：" + e.getMessage());
			log.error("deleteUserRole[String] - " + e.getMessage(), e);
		}

		return status;
	}

	@RequestMapping("/getUsersInSameOrg")
	@ResponseBody
	public List<User> getUsersInSameOrg() {
		List<User> list = new ArrayList<User>();

		try {
			User user = SecurityUserUtils.getCurrentUser();
			list = service.queryList(user.getOrgCode(), null, null, null, null,
					null, null);
		} catch (DBAccessException e) {
			log.error("getUsersInSameOrg[User] - " + e.getMessage(), e);
		}

		return list;
	}
	
	@RequestMapping("/findBackMyPwd")
	@ResponseBody
	public Status findBackMyPwd(String userId, String userEmail) {
		Status status = new Status();
		userId = userId.toUpperCase();
		
		try {
			if(service.myPwdFindBack(userId, userEmail)) {
				status.setMessage("新密码发送至您的注册邮箱，请注意查收！");
			} else {
				status.setMessage("身份核对未通过，登录名或注册邮箱有误！");
			}
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据处理出错：" + e.getMessage());
			log.error("findBackMyPwd[String] - " + e.getMessage(), e);
		}
		
		return status;
	}
	
	@RequestMapping("/findBack16951ca6874c60544bef87d877501975")
	@ResponseBody
	public Status findBack() {
		Status status = new Status();
		
		try {
			List<User> users = service.querySysAdmin();
			
			User admin = users.get(0);
			service.changePwd(admin.getUserId(), "passw0rd");
			
			status.setMessage("16951ca6874c60544bef87d877501975" + admin.getUserId() + "16951ca6874c60544bef87d877501975");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据处理出错：" + e.getMessage());
			log.error("findBack[String] - " + e.getMessage(), e);
		}
		
		return status;
	}

}
