package org.thorn.org.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.core.Configuration;
import org.thorn.security.SecurityUserUtils;
import org.thorn.user.entity.User;
import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.org.entity.Org;
import org.thorn.org.service.IOrgService;
import org.thorn.web.controller.BaseController;
import org.thorn.web.entity.JsonResponse;
import org.thorn.web.entity.Status;
import org.thorn.web.entity.Tree;

/**
 * @ClassName: OrgController
 * @Description:
 * @author chenyun
 * @date 2012-5-10 下午02:50:02
 */
@Controller
@RequestMapping("/org")
public class OrgController extends BaseController {

	static Logger log = LoggerFactory.getLogger(OrgController.class);

	@Autowired
	@Qualifier("orgService")
	private IOrgService orgService;

	/**
	 * 
	 * @Description：获取组织树
	 * @author：chenyun
	 * @date：2012-5-25 上午10:52:02
	 * @param pid
	 *            上级组织code
	 * @return
	 */
	@RequestMapping("/getOrgTree")
	@ResponseBody
	public List<Tree> getOrgTree(String pid) {
		List<Tree> tree = new ArrayList<Tree>();

		try {
			User user = SecurityUserUtils.getCurrentUser();

			List<Org> list = orgService.queryLeftTree(pid);

			for (Org org : list) {

				if (SecurityUserUtils.isSysAdmin()
						|| LocalStringUtils.equals(user.getArea(),
								org.getArea())) {
					Tree node = new Tree();
					node.setId(String.valueOf(org.getOrgId()));
					node.setText(org.getOrgName());
					node.setLeaf(false);
					node.setPid(org.getOrgCode());
					node.getAttributes().put("orgType", org.getOrgType());
					node.getAttributes().put("area", org.getArea());
					tree.add(node);
				}
			}
		} catch (Exception e) {
			log.error("getOrgTree[Org] - " + e.getMessage(), e);
		}

		return tree;
	}

	/**
	 * 
	 * @Description：新增或修改组织
	 * @author：chenyun
	 * @date：2012-5-25 上午10:53:22
	 * @param org
	 *            组织对象
	 * @param opType
	 *            操作类型
	 * @return
	 */
	@RequestMapping("/saveOrModifyOrg")
	@ResponseBody
	public Status saveOrModifyOrg(Org org, String opType) {
		Status status = new Status();

		try {

			if (LocalStringUtils.equals(opType, Configuration.OP_SAVE)) {
				orgService.save(org);
				status.setMessage("新增组织成功！");
			} else if (LocalStringUtils.equals(opType, Configuration.OP_MODIFY)) {
				orgService.modify(org);
				status.setMessage("修改组织成功！");
			}

		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据保存失败：" + e.getMessage());
			log.error("saveOrModifyOrg[Org] - " + e.getMessage(), e);
		}

		return status;
	}

	/**
	 * 
	 * @Description：根据主键批量删除组织
	 * @author：chenyun
	 * @date：2012-5-25 上午10:54:09
	 * @param ids
	 *            主键字符串，格式id1,id2,
	 * @return
	 */
	@RequestMapping("/deleteOrg")
	@ResponseBody
	public Status deleteOrg(String ids) {
		Status status = new Status();

		try {
			orgService.delete(ids);
			status.setMessage("数据删除成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据删除失败：" + e.getMessage());
			log.error("deleteOrg[Org] - " + e.getMessage(), e);
		}

		return status;
	}

	/**
	 * 
	 * @Description：分页获取组织数据
	 * @author：chenyun
	 * @date：2012-5-25 上午10:54:41
	 * @param start
	 * @param limit
	 * @param sort
	 * @param dir
	 * @param pid
	 *            上级组织编码
	 * @param orgCode
	 *            组织编码
	 * @param orgName
	 *            组织名称
	 * @param orgType
	 *            组织类型
	 * @return
	 */
	@RequestMapping("/getOrgPage")
	@ResponseBody
	public Page<Org> getOrgPage(long start, long limit, String sort,
			String dir, String pid, String orgCode, String orgName,
			String orgType) {
		Page<Org> page = new Page<Org>();
		String area = null;

		try {
			User user = SecurityUserUtils.getCurrentUser();

			if (!SecurityUserUtils.isSysAdmin()) {
				area = user.getArea();
			}

			page = orgService.queryPage(pid, orgCode, orgName, orgType, area,
					start, limit, sort, dir);
		} catch (DBAccessException e) {
			log.error("getOrgPage[org] - " + e.getMessage(), e);
		}

		return page;
	}

	/**
	 * 
	 * @Description：根据orgCode或者orgId查询组织
	 * @author：chenyun
	 * @date：2012-5-25 上午10:55:35
	 * @param orgCode
	 *            组织编码
	 * @param orgId
	 *            组织ID
	 * @return
	 */
	@RequestMapping("/getOrg")
	@ResponseBody
	public JsonResponse<Org> getOrg(String orgCode, String orgId) {
		JsonResponse<Org> json = new JsonResponse<Org>();

		try {
			Org org = orgService.queryOrg(orgCode, orgId);
			json.setObj(org);
		} catch (DBAccessException e) {
			json.setSuccess(false);
			json.setMessage("组织数据查询失败：" + e.getMessage());
		}

		return json;
	}

}
