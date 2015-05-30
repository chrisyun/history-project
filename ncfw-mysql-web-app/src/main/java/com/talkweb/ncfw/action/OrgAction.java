package com.talkweb.ncfw.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.talkweb.ncframework.pub.common.PageBean;
import com.talkweb.ncframework.pub.common.ProccessResultBean;
import com.talkweb.ncframework.pub.dao.IGenericDAO;
import com.talkweb.ncframework.pub.exceptions.DAOException;
import com.talkweb.ncframework.pub.utils.CollectionUtils;
import com.talkweb.ncframework.pub.utils.ObjectUtils;
import com.talkweb.ncframework.pub.utils.StringUtils;
import com.talkweb.ncframework.pub.utils.tree.TreeBasic;
import com.talkweb.ncframework.pub.web.common.utils.HttpServletUtils;
import com.talkweb.ncframework.pub.web.struts2.BaseAction;
import com.talkweb.ncfw.entity.Org;

/**
 * <p>文件名称: OrgAction.java</p>
 * <p>文件描述: 本类描述</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2011-10-21</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  chenyun
 */
public class OrgAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(OrgAction.class);
	public static final String ALLORGROOT			= "0";			//组织根节点	
	private IGenericDAO genericDAO;
	private Org org;
	public IGenericDAO getGenericDAO() {
		return genericDAO;
	}
	public void setGenericDAO(IGenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}
	public Org getOrg() {
		return org;
	}
	public void setOrg(Org org) {
		this.org = org;
	}
	
	/**
	 * 
	 * @author：chenyun 	        
	 * @date：2011-10-21
	 * @Description：组织机构数
	 * @return
	 */
	public String createOrgTree () {
		Map condition = new HashMap();
		condition.put("isshow", "SHOW");
		condition.put("orgtype", this.getParameter("orgtype"));
		String parentId	= this.getParameter("parentId");
		if (StringUtils.isEmpty(parentId)) {
			parentId	= ALLORGROOT;
			condition.put("orgLevel", "DEPT");
		}
		
		String notIn = this.getParameter("notShow");
		if (!StringUtils.isEmpty(notIn)) {
			condition.put("notIn", "notIn");
		}
		
		condition.put("parentorg", parentId);
		condition.put("sort", "sort");
		condition.put("dir", "asc");
		List<Org> orgList = this.getGenericDAO().queryList("OrgMapper.query", condition);
		if (CollectionUtils.isEmpty(orgList)) {
			return null;
		}
		List<TreeBasic> treeBasicList = new ArrayList(orgList.size());
		for (Org org : orgList) {
			TreeBasic treeBasic = new TreeBasic();
			treeBasic.setId(ObjectUtils.toString(org.getOrgid()));
			treeBasic.setText(org.getOrgname());
			treeBasic.setParentId(ObjectUtils.toString(org.getParentorg()));
			Map attributes = new HashMap();
			attributes.put("provinceCode", org.getProvinceNumber());
			treeBasic.setAttributes(attributes);
			treeBasicList.add(treeBasic);
		}
		//List<TreeBean> treeBeanList = TreeUtils.onTree(treeBasicList, "", "");
		HttpServletUtils.outJsonArray(this.getResponse(), treeBasicList);
		return null;
	}
	
	public String editOrg() {
		ProccessResultBean resultBean = null;
		String opType = this.getParameter("opType");
		//System.out.println("=============================="+opType);
		if (opType.equals("update")) {
			try {
				this.genericDAO.update("OrgMapper.update", org);
				resultBean = new ProccessResultBean(true, "组织修改成功.");
			} catch (DAOException e) {
				resultBean = new ProccessResultBean(false, "组织修改不成功!!!");
	
			}
		}else if (opType.equals("add")) {
			try {
				this.genericDAO.insert("OrgMapper.insert", org);
				resultBean = new ProccessResultBean(true, "组织添加成功.");
			} catch (DAOException e) {
				resultBean = new ProccessResultBean(false, "组织添加不成功!!!");
			}
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	public String queryOrg() {
		Map map = this.getParameterMap();
		
		PageBean<Map> pageBean = new PageBean<Map>();
		Long count = this.genericDAO.getCount("OrgMapper.getCount", map);
		List<Map> list = (List<Map>) this.genericDAO.queryList("OrgMapper.querySupportPagination", map);
		pageBean.setResultList(list);
		pageBean.setTotalProperty(count.longValue());
		HttpServletUtils.outJson(this.getResponse(), pageBean);
		return null;
	}
	
	public String deleteOrg() {
		ProccessResultBean resultBean = null;
		String orgid = this.getParameter("orgid");
		Map para = new HashMap();
		para.put("orgid", new Integer(orgid));
		try {
			this.genericDAO.deleteForeach("OrgMapper.delete", para);
			resultBean = new ProccessResultBean(true, "组织删除成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "组织删除不成功!!!");

		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	public String searchOrg() {
		ProccessResultBean resultBean = null;
		Map map = this.getParameterMap();
		Map para = new HashMap();
		para.put("orgid", map.get("orgid"));
		Object qstObj = this.getGenericDAO().load("OrgMapper.query", para);
		HttpServletUtils.outJson(this.getResponse(), qstObj);
		return null;
	}
	
	
}

