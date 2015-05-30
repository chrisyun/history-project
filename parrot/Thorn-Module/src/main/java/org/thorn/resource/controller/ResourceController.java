package org.thorn.resource.controller;

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
import org.thorn.resource.entity.Resource;
import org.thorn.resource.service.IResourceService;
import org.thorn.security.SecurityConfiguration;
import org.thorn.security.SecurityUserUtils;
import org.thorn.web.controller.BaseController;
import org.thorn.web.entity.FullTree;
import org.thorn.web.entity.JsonResponse;
import org.thorn.web.entity.Status;
import org.thorn.web.entity.Tree;

/**
 * @ClassName: ResourceController
 * @Description:
 * @author chenyun
 * @date 2012-5-6 下午10:51:39
 */
@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController {

	static Logger log = LoggerFactory.getLogger(ResourceController.class);

	@Autowired
	@Qualifier("resourceService")
	private IResourceService service;
	
	@Autowired
	@Qualifier("authService")
	private IAuthService authService;
	
	/**
	 * 
	 * @Description：获取系统左边菜单树，带权限控制
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:03:59
	 * @param pid	上级资源ID
	 * @param isSourcePanel 请求是否来自资源面板	YES
	 * @return
	 */
	@RequestMapping("/getLeftTree")
	@ResponseBody
	public List<Tree> getLeftTree(String pid, String isSourcePanel) {
		List<Tree> tree = new ArrayList<Tree>();
		
		List<String> userSource = SecurityUserUtils.getSoucrceList();
		
		try {
			List<Resource> source = service.queryLeftTree(pid);
			
			List<String> roles = SecurityUserUtils.getRoleList();
			for (Resource res : source) {
				
				if(! LocalStringUtils.equals("YES", isSourcePanel)) {
					//if(! SecurityUserUtils.isSysAdmin() 
					if(! roles.contains(SecurityConfiguration.SYS_ADMIN_ROLE)
							&& !userSource.contains(res.getSourceCode())) {
						continue;
					}
				}
				
				Tree node = new Tree();
				node.setId(res.getSourceCode());
				node.setText(res.getSourceName());
				node.setPid(res.getParentSource());

				node.setTargetUrl(res.getSourceUrl());
				node.setIconCls(res.getIconsCls());

				if (LocalStringUtils.equals(res.getIsleaf(),
						Configuration.DB_YES)) {
					node.setLeaf(true);
				} else {
					node.setLeaf(false);
				}
				tree.add(node);
			}
		} catch (Exception e) {
			log.error("getLeftTree[Resource] - " + e.getMessage(), e);
		}

		return tree;
	}
	
	/**
	 * 
	 * @Description：一次性获取整个资源树，无需层级加载
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:05:00
	 * @param pid
	 * @return
	 */
	@RequestMapping("/getSourceTree")
	@ResponseBody
	public List<FullTree> getSourceTree(String pid) {
		List<FullTree> ft = new ArrayList<FullTree>();
		
		FullTree tree = new FullTree();

		try {
			List<Resource> allSource = service.queryAllSource();
			
			//第一次遍历，找到当前节点的根
			for (Resource res : allSource) {
				if(LocalStringUtils.equals(res.getSourceCode(), pid)) {
					tree.setId(res.getSourceCode());
					tree.setText(res.getSourceName());
					tree.setPid(res.getParentSource());
					tree.setIconCls(res.getIconsCls());
					tree.setLeaf(false);
					
					tree.setUiProvider("checkBox");
					tree.setExpanded(true);
					break;
				}
			}
			
			//递归向下加子节点
			sortTree(allSource, tree);
			
		} catch (Exception e) {
			log.error("getSourceTree[Resource] - " + e.getMessage(), e);
		}
		
		ft.add(tree);
		return ft;
	}
	
	/**
	 * 
	 * @Description：对资源进行递归排序，按照顺序放到fullTree上
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:05:56
	 * @param source	待排序的资源
	 * @param tree		非叶子节点树
	 */
	private void sortTree(List<Resource> source, FullTree tree) {
		
		for(Resource res : source) {
			if(LocalStringUtils.equals(res.getParentSource(), tree.getId())) {
				
				FullTree node = new FullTree();
				
				node.setId(res.getSourceCode());
				node.setText(res.getSourceName());
				node.setPid(res.getParentSource());
				node.setIconCls(res.getIconsCls());
				node.setTargetUrl(res.getSourceUrl());
				
				node.setUiProvider("checkBox");
				node.setExpanded(true);
				
				tree.getChildren().add(node);
				
				sortTree(source, node);
				
				if(node.getChildren().size() == 0) {
					node.setLeaf(true);
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @Description：获取角色授权的资源列表
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:07:59
	 * @param roleCode 角色编码
	 * @return
	 */
	@RequestMapping("/getSourceCodeByRole")
	@ResponseBody
	public JsonResponse<List> getSourceCodeByRole(String roleCode) {
		JsonResponse<List> json = new JsonResponse<List>();
		
		try {
			List<String> list = authService.queryResourceByRole(roleCode);
			json.setObj(list);
			
		} catch (DBAccessException e) {
			json.setSuccess(false);
			json.setMessage("获取数据失败：" + e.getMessage());
			log.error("getSourceCodeByRole[String] - " + e.getMessage(), e);
		}
		
		return json;
	}
	
	/**
	 * 
	 * @Description：新增或修改资源
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:15:10
	 * @param source	资源对象
	 * @param opType	操作类型
	 * @return
	 */
	@RequestMapping("/saveOrModifySource")
	@ResponseBody
	public Status saveOrModifySource(Resource source, String opType) {
		Status status = new Status();

		try {

			if (LocalStringUtils.equals(opType, Configuration.OP_SAVE)) {
				service.save(source);
				status.setMessage("新增资源成功！");
			} else if (LocalStringUtils.equals(opType, Configuration.OP_MODIFY)) {
				service.modify(source);
				status.setMessage("修改资源成功！");
			}

		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据保存失败：" + e.getMessage());
			log.error("saveOrModifyOrg[Resource] - " + e.getMessage(), e);
		}

		return status;
	}
	
	/**
	 * 
	 * @Description：根据主键批量删除资源
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:16:09
	 * @param ids	主键字符串，格式id1,id2,
	 * @return
	 */
	@RequestMapping("/deleteSource")
	@ResponseBody
	public Status deleteSource(String ids) {
		Status status = new Status();

		try {
			service.delete(ids);
			status.setMessage("数据删除成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据删除失败：" + e.getMessage());
			log.error("deleteResource[Resource] - " + e.getMessage(), e);
		}

		return status;
	}
	
	/**
	 * 
	 * @Description：分页获取资源数据
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:16:50
	 * @param start
	 * @param limit
	 * @param sort
	 * @param dir
	 * @param pid		上级资源code
	 * @param sourceCode资源code
	 * @param sourceName资源名称
	 * @return
	 */
	@RequestMapping("/getSourcePage")
	@ResponseBody
	public Page<Resource> getSourcePage(long start, long limit, String sort,
			String dir, String pid, String sourceCode, String sourceName) {
		Page<Resource> page = new Page<Resource>();

		try {
			page = service.queryPage(pid, sourceCode, sourceName, start,
					limit, sort, dir);
		} catch (DBAccessException e) {
			log.error("getSourcePage[Resource] - " + e.getMessage(), e);
		}

		return page;
	}
	
	/**
	 * 
	 * @Description：根据资源code获取单个资源对象
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:17:25
	 * @param sourceCode
	 * @return
	 */
	@RequestMapping("/getResource")
	@ResponseBody
	public JsonResponse<Resource> getResource(String sourceCode) {
		JsonResponse<Resource> json = new JsonResponse<Resource>();
		
		try {
			Resource source = service.queryResource(sourceCode);
			json.setObj(source);
		} catch (DBAccessException e) {
			json.setSuccess(false);
			json.setMessage("资源数据查询失败：" + e.getMessage());
		}
		
		return json;
	}
	
	
}
