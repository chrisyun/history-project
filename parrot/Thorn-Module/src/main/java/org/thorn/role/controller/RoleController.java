package org.thorn.role.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.role.entity.Role;
import org.thorn.role.service.IRoleService;
import org.thorn.security.SecurityConfiguration;
import org.thorn.web.controller.BaseController;
import org.thorn.web.entity.JsonResponse;
import org.thorn.web.entity.Relation;
import org.thorn.web.entity.Status;

/**
 * @ClassName: RoleController
 * @Description:
 * @author chenyun
 * @date 2012-5-17 下午09:22:41
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

	static Logger log = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	@Qualifier("roleService")
	private IRoleService service;

	@Autowired
	@Qualifier("authService")
	private IAuthService authService;

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-25 上午11:54:08
	 * @param ids
	 * @return
	 */
	@RequestMapping("/deleteRole")
	@ResponseBody
	public Status deleteRole(String ids) {
		Status status = new Status();

		try {
			service.delete(ids);
			status.setMessage("数据删除成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据删除失败：" + e.getMessage());
			log.error("deleteResource[Role] - " + e.getMessage(), e);
		}

		return status;
	}

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-25 上午11:54:21
	 * @param start
	 * @param limit
	 * @param sort
	 * @param dir
	 * @param roleCode
	 *            角色编码
	 * @param roleName
	 *            角色名称
	 * @return
	 */
	@RequestMapping("/getRolePage")
	@ResponseBody
	public Page<Role> getRolePage(long start, long limit, String sort,
			String dir, String roleCode, String roleName) {
		Page<Role> page = new Page<Role>();

		try {
			page = service.queryPage(roleCode, roleName, start, limit, sort,
					dir);
		} catch (DBAccessException e) {
			log.error("getRolePage[Role] - " + e.getMessage(), e);
		}

		return page;
	}

	/**
	 * 
	 * @Description：获取所有的角色
	 * @author：chenyun
	 * @date：2012-5-25 上午11:54:37
	 * @return
	 */
	@RequestMapping("/getAllRole")
	@ResponseBody
	public List<Role> getAllRole() {
		List<Role> list = new ArrayList<Role>();

		try {
			list = service.queryAllRoles();
		} catch (DBAccessException e) {
			log.error("getRolePage[Role] - " + e.getMessage(), e);
		}

		return list;
	}

	/**
	 * 
	 * @Description：获取用户与所有角色的关系
	 * @author：chenyun
	 * @date：2012-5-25 上午11:54:53
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getUserRole")
	@ResponseBody
	public JsonResponse<List<Relation>> getUserRoles(String userId) {
		JsonResponse<List<Relation>> json = new JsonResponse<List<Relation>>();

		List<Relation> list = new ArrayList<Relation>();

		try {
			// 只有rolecode
			List<Role> userRole = authService.queryRoleByUser(userId);

			List<Role> role = service.queryAllRoles();
			for (Role r : role) {
				//不显示默认角色
				if (LocalStringUtils.equals(r.getRoleCode(),
						SecurityConfiguration.COMMON_USER_ROLE)
						|| LocalStringUtils.equals(r.getRoleCode(),
								SecurityConfiguration.SYS_ADMIN_ROLE)) {
					continue;
				}

				Relation relation = new Relation();
				relation.setSubject(userId);
				relation.setObject(r);
				relation.setRelevance(false);
				list.add(relation);
			}

			for (Role ur : userRole) {

				for (Relation r : list) {

					if (LocalStringUtils.equals(ur.getRoleCode(),
							((Role) r.getObject()).getRoleCode())) {
						r.setRelevance(true);
						break;
					}
				}
			}

			json.setObj(list);
		} catch (DBAccessException e) {
			json.setSuccess(false);
			json.setMessage("数据加载失败：" + e.getMessage());
			log.error("getUserRoles[Relation] - " + e.getMessage(), e);
		}

		return json;
	}

	/**
	 * 
	 * @Description：授权资源给角色
	 * @author：chenyun
	 * @date：2012-5-25 上午11:55:19
	 * @param roleCode
	 *            角色编码
	 * @param ids
	 *            资源主键id
	 * @return
	 */
	@RequestMapping("/saveAuth")
	@ResponseBody
	public Status saveAuth(String roleCode, String ids) {
		Status status = new Status();

		try {
			authService.saveRoleAuth(roleCode, ids);
			status.setMessage("角色授权成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据保存失败：" + e.getMessage());
			log.error("saveOrModifyOrg[Role] - " + e.getMessage(), e);
		}

		return status;
	}

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-25 上午11:56:21
	 * @param role
	 * @param opType
	 * @return
	 */
	@RequestMapping("/saveOrModifyRole")
	@ResponseBody
	public Status saveOrModifyRole(Role role, String opType) {
		Status status = new Status();

		try {

			if (LocalStringUtils.equals(opType, Configuration.OP_SAVE)) {
				service.save(role);
				status.setMessage("新增角色成功！");
			} else if (LocalStringUtils.equals(opType, Configuration.OP_MODIFY)) {
				service.modify(role);
				status.setMessage("修改角色成功！");
			}

		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据保存失败：" + e.getMessage());
			log.error("saveOrModifyOrg[Role] - " + e.getMessage(), e);
		}

		return status;
	}
}
