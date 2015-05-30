package com.talkweb.ncfw.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.talkweb.ncframework.modules.rights.menu.entity.SystemMenu;
import com.talkweb.ncframework.modules.rights.role.rolemanage.entity.Role;
import com.talkweb.ncframework.pub.common.PageBean;
import com.talkweb.ncframework.pub.common.ProccessResultBean;
import com.talkweb.ncframework.pub.dao.IGenericDAO;
import com.talkweb.ncframework.pub.exceptions.DAOException;
import com.talkweb.ncframework.pub.utils.CollectionUtils;
import com.talkweb.ncframework.pub.utils.StringUtils;
import com.talkweb.ncframework.pub.utils.tree.TreeBasic;
import com.talkweb.ncframework.pub.utils.tree.TreeBean;
import com.talkweb.ncframework.pub.utils.tree.TreeUtils;
import com.talkweb.ncframework.pub.web.common.utils.HttpServletUtils;
import com.talkweb.ncframework.pub.web.struts2.BaseAction;
import com.talkweb.ncfw.entity.User;
import com.talkweb.security.SecurityHelper;

/**
 * <p>文件名称: AuthAction.java</p>
 * <p>文件描述: 权限Action</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>公　　司: 拓维信息系统股份有限公司</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2011-5-12</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  Wuqingming
 */
public class AuthAction extends BaseAction {
	private static Logger logger = Logger.getLogger(UserAction.class);
	private IGenericDAO genericDAO;
	
	public IGenericDAO getGenericDAO() {
		return genericDAO;
	}

	public void setGenericDAO(IGenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2011-5-12
	 * @Description：生成菜单树
	 * @return
	 */
	public String createMenuTree () {
		boolean hasSysadminRight = SecurityHelper.hasSysadminRight();
		Map condition = new HashMap();
		if (!hasSysadminRight) {
			List<Role> roleList = SecurityHelper.getCurrentUserRoles();
			if (CollectionUtils.isEmpty(roleList)) {		//无权限
				return null;
			}
			String[] roleidArray = new String[roleList.size()];
			int cursor = 0;
			for (Role role : roleList) {
				roleidArray[cursor++] = role.getRoleId();
			}
			condition.put("roleidinArray", roleidArray);
		}
		condition.put("sort", "DISPLAYORDER");
		condition.put("dir", "ASC");
		List<SystemMenu> menuList = this.getGenericDAO().queryList("RoleMenuMapper.queryMenu", condition);
		if (CollectionUtils.isEmpty(menuList)) {
			return null;
		}
		List<TreeBasic> treeBasicList = new ArrayList(menuList.size());
		for (SystemMenu menu : menuList) {
			TreeBasic treeBasic = new TreeBasic();
			treeBasic.setId(menu.getMenuId());
			treeBasic.setText(menu.getMenuLabelName());
			treeBasic.setParentId(menu.getParentMenuId());
			treeBasic.setHrefTarget(menu.getOpenMode());
			treeBasic.setHref(menu.getMenuEntry());
			treeBasicList.add(treeBasic);
		}
		List<TreeBean> treeBeanList = TreeUtils.onTree(treeBasicList, "", "");
		HttpServletUtils.outJsonArray(this.getResponse(), treeBeanList);
		return null;
	}
	
	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2011-5-12
	 * @Description：保存用户权限
	 * @return
	 */
	public String saveUserAuth () {
		ProccessResultBean resultBean = null;
		String userid 	= this.getParameter("userid");
		if (StringUtils.isEmpty(userid)) {
			resultBean = new ProccessResultBean(false, "用户ID不能为空.");
			HttpServletUtils.outJson(this.getResponse(), resultBean);
			return null;
		}
		String roleid	= this.getParameter("roleid");
		if (StringUtils.isEmpty(roleid)) {		//用户默认角色
			roleid = SecurityHelper.COMMON_USER;
		}
		User user = new User();
		user.setUserid(userid);
		user.setRoleid(roleid);
		try {
			this.getGenericDAO().update("UserMapper.update", user);
			resultBean = new ProccessResultBean(true, "权限保存成功.");
		} catch (DAOException e) {
			String exceptMsg = "修改用户权限时发生异常";
			logger.error(exceptMsg, e);
			resultBean = new ProccessResultBean(false, "保存用户权限异常.");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2011-5-12
	 * @Description：查询菜单
	 * @return
	 */
	public String queryMenu () {
		Map condition = this.getParameterMap();
		List<SystemMenu> menuList = this.getGenericDAO().queryList("RoleMenuMapper.queryMenu", condition);
		HttpServletUtils.outJsonArray(this.getResponse(), menuList);
		return null;
	}
	
	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2011-5-12
	 * @Description：保存角色权限
	 * @return
	 */
	public String saveRoleAuthData () {
		ProccessResultBean resultBean = null;
		String resType 	= this.getParameter("resType");
		String resIds	= this.getParameter("resIds");
		String roleId	= this.getParameter("roleId");
		if (StringUtils.isEmpty(roleId)) {
			resultBean = new ProccessResultBean(false, "角色ID不能为空.");
			HttpServletUtils.outJson(this.getResponse(), resultBean);
			return null;
		}
		if (StringUtils.equals(resType, "menu")) {
			try {
				this.getGenericDAO().deleteForeach("RoleMenuMapper.deleteByRole", roleId);
			} catch (DAOException e) {
				String exceptMsg = "保存角色菜单权限, 删除之前的菜单权限时发生异常";
				logger.error(exceptMsg, e);
				resultBean = new ProccessResultBean(false, "保存角色菜单权限异常.");
			}
			if (StringUtils.isNotEmpty(resIds)) {
				String[] resIdArray = resIds.split(",");
				Map[] roleReses = new HashMap[resIdArray.length];
				int cursor = 0;
				for (String resId : resIdArray) {
					Map roleRes = new HashMap();
					roleRes.put("menuid", resId);
					roleRes.put("roleid", roleId);
					roleReses[cursor++] = roleRes;
				}
				try {
					this.getGenericDAO().insert("RoleMenuMapper.insert", roleReses);
				} catch (DAOException e) {
					String exceptMsg = "保存角色菜单权限, 保存当前角色菜单关系信息时发生异常";
					logger.error(exceptMsg, e);
					resultBean = new ProccessResultBean(false, "保存角色菜单权限异常.");
				}
			}
		}
		if (resultBean == null) {
			resultBean = new ProccessResultBean(true, "权限数据保存成功.");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2011-8-11
	 * @Description：获取角色为授权
	 * @return
	 */
	public String queryRoleForAuth () {
		Map condition 		= this.getParameterMap();
		if (!SecurityHelper.hasSysadminRight()) {
			List<Role> roleList = SecurityHelper.getCurrentUserRoles();
			if (CollectionUtils.isNotEmpty(roleList)) {
				Integer[] currentTransferRoleIdArray = new Integer[roleList.size()];
				int cursor = 0;
				for (Role role : roleList) {
					currentTransferRoleIdArray[cursor++] = new Integer(role.getRoleId());
				}
				condition.put("roleIdArrayIn", currentTransferRoleIdArray);
			}
		}
		List resultList = this.getGenericDAO().queryList("RoleMapper.query", condition);
		HttpServletUtils.outJsonArray(this.getResponse(), resultList);
		return null;
	}
	
	public String listUserByRole () {
		String roleidsIn = this.getParameter("roleidsIn");
		String roleidsOut = this.getParameter("roleidsOut");
		String type = this.getParameter("type");
		Map<String, Object> condition = this.getParameterMap();
		if (StringUtils.isNotEmpty(roleidsIn)) {
			condition.put("roleInArray", roleidsIn.split(","));
		} else if (StringUtils.isNotEmpty(roleidsOut)) {
			condition.put("roleOutArray", roleidsOut.split(","));
		}
		PageBean pageBean = new PageBean();
	    Long count = getGenericDAO().getCount("UserMapper.getCount", condition);
	    pageBean.setTotalProperty(count.longValue());
	    if (count.longValue() > 0L) {
	      List userList = getGenericDAO().queryList("UserMapper.querySupportPagination", condition);
	      pageBean.setResultList(userList);
	    }
	    HttpServletUtils.outJson(getResponse(), pageBean);
	    return null;
	}
	
}

