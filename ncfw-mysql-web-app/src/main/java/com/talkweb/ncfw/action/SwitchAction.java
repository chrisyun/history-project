package com.talkweb.ncfw.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.talkweb.ncframework.pub.common.PageBean;
import com.talkweb.ncframework.pub.common.ProccessResultBean;
import com.talkweb.ncframework.pub.dao.IGenericDAO;
import com.talkweb.ncframework.pub.exceptions.DAOException;
import com.talkweb.ncframework.pub.utils.StringUtils;
import com.talkweb.ncframework.pub.web.common.utils.HttpServletUtils;
import com.talkweb.ncframework.pub.web.struts2.BaseAction;
import com.talkweb.ncfw.entity.FlowSwitch;
import com.talkweb.ncfw.entity.Project;

/**
 * <p>文件名称: SwitchAction.java</p>
 * <p>文件描述: 本类描述</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2012-2-3</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  chenyun
 */
public class SwitchAction extends BaseAction {
	static Logger log = Logger.getLogger(SwitchAction.class);

	private IGenericDAO genericDAO;
	private FlowSwitch flowSwitch;
	public IGenericDAO getGenericDAO() {
		return genericDAO;
	}
	public void setGenericDAO(IGenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}
	public FlowSwitch getFlowSwitch() {
		return flowSwitch;
	}
	public void setFlowSwitch(FlowSwitch flowSwitch) {
		this.flowSwitch = flowSwitch;
	}
	
	/**
	 * 查询所有的流程开关
	 * @author：chenyun 	        
	 * @date：2012-2-3
	 * @Description：
	 * @return
	 */
	public String searchSwitchList() {
		PageBean<FlowSwitch> pageBean = new PageBean ();
		pageBean.setTotalProperty(100);
		List<FlowSwitch> list = this.getGenericDAO().queryList("SwitchMapper.queryList",null);
		pageBean.setResultList(list);
		HttpServletUtils.outJson(this.getResponse(), pageBean);
		return null;
	}
	
	public String updateStatus() {
		ProccessResultBean resultBean 	= null;
		String flowids		= this.getParameter("flowids");
		String status		= this.getParameter("status");
		if (StringUtils.isEmpty(flowids)) {
			resultBean = new ProccessResultBean(false, "未选择项目类别.");
			HttpServletUtils.outJson(this.getResponse(), resultBean);
			return null;
		}
		
		String[] flowidArray= flowids.split(",");
		Map para = new HashMap();
		para.put("flowidArray", flowidArray);
		para.put("status", status);
		try {
			this.getGenericDAO().update("SwitchMapper.updateStatus", para);
			resultBean = new ProccessResultBean(true, "请求处理成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "请求处理失败.");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	public String searchStatus() {
		ProccessResultBean resultBean = null;
		String flowid = this.getParameter("flowid");
		try {
			String status = (String) genericDAO.load("SwitchMapper.queryById", flowid);
			resultBean = new ProccessResultBean(true,status);
		} catch (Exception e) {
			resultBean = new ProccessResultBean(false,"查询项目状态失败");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
		
	}
	
	
}

