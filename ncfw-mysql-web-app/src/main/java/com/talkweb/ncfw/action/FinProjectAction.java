package com.talkweb.ncfw.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.talkweb.ncframework.pub.common.ProccessResultBean;
import com.talkweb.ncframework.pub.dao.IGenericDAO;
import com.talkweb.ncframework.pub.exceptions.DAOException;
import com.talkweb.ncframework.pub.utils.SequenceUtils;
import com.talkweb.ncframework.pub.utils.StringUtils;
import com.talkweb.ncframework.pub.web.common.utils.HttpServletUtils;
import com.talkweb.ncframework.pub.web.struts2.BaseAction;
import com.talkweb.ncfw.entity.FinProject;
import com.talkweb.ncfw.entity.MedProject;
import com.talkweb.ncfw.entity.Minds;
import com.talkweb.ncfw.entity.Project;
import com.talkweb.ncfw.entity.User;
import com.talkweb.ncfw.file.IFileService;
import com.talkweb.security.SecurityHelper;

/**
 * <p>文件名称: MedProjectAction.java</p>
 * <p>文件描述: 本类描述</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2011-11-16</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  chenyun
 */
public class FinProjectAction extends BaseAction {
	static Logger log = Logger.getLogger(FinProjectAction.class);

	private IGenericDAO genericDAO;
	private IFileService fileService;
	private Project project;
	private FinProject finproject;
	public IGenericDAO getGenericDAO() {
		return genericDAO;
	}
	public void setGenericDAO(IGenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}
	public IFileService getFileService() {
		return fileService;
	}
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public FinProject getFinproject() {
		return finproject;
	}
	public void setFinproject(FinProject finproject) {
		this.finproject = finproject;
	}
	
	public String searchFinProjectIds() {
		String pid = this.getParameter("pid");
		
		List<FinProject> fpids = genericDAO.queryList("FinProjectMapper.selectIdsBypid", pid);
		HttpServletUtils.outJsonArray(this.getResponse(), fpids);
		return null;
	}
	
	public String queryFinProject() {
		String fpid = this.getParameter("fpid");
		
		Object proObj = this.getGenericDAO().load("FinProjectMapper.selectFinProjectById", fpid);
		HttpServletUtils.outJson(this.getResponse(), proObj);
		return null;
	}
	
	
	/**
	 * 
	 * @author：chenyun 	        
	 * @date：2011-11-16
	 * @Description：中期检查新增
	 * @return
	 */
	public String addFinProject() {
		User user = SecurityHelper.getCurrentUser().getUser();
		
		finproject.setFpid(SequenceUtils.createPrimaryKeySeq());
		String attach = this.getParameter("attach");
		if(!StringUtils.isEmpty(attach)) {
			attach = attach.substring(0,attach.length()-1);
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		finproject.setCreattime(df.format(new Date()));
		finproject.setIssp("YES");
		
		flowInfo();
		ProccessResultBean resultBean = null;
		
		String minds = this.getParameter("minds");
		if(StringUtils.isEmpty(minds)) {
			minds = "------------------- " + user.getUsername() + " ";
		} else {
			minds += "------------------- " + user.getUsername() + " ";
		}
		
		try {
			this.genericDAO.insert("FinProjectMapper.insert", finproject);
			this.genericDAO.insert("ProjectMapper.update", project);
			this.fileService.updateFile(attach, finproject.getFpid(), "t_fin_project");
			this.insertMind(finproject.getFpid(), "t_fin_project", minds, "DRAFT");
			resultBean = new ProccessResultBean(true, "项目信息添加成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "项目信息添加不成功!!!");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	/**
	 * 
	 * @author：chenyun 	        
	 * @date：2011-11-17
	 * @Description：中期检查更新
	 * @return
	 */
	public String updateFinProject() {
		User user = SecurityHelper.getCurrentUser().getUser();
		String activity = project.getCurActivityName();
		
		flowInfo();
		ProccessResultBean resultBean = null;
		String minds = this.getParameter("minds");
		if(StringUtils.isEmpty(minds)) {
			minds = "------------------- " + user.getUsername() + " ";
		} else {
			minds += "------------------- " + user.getUsername() + " ";
		}
		try {
			this.genericDAO.update("FinProjectMapper.update", finproject);
			this.genericDAO.update("ProjectMapper.update", project);
			resultBean = new ProccessResultBean(true, "项目信息提交成功.");
			this.insertMind(finproject.getFpid(), "t_fin_project", minds, activity);
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "项目信息提交不成功!!!");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	public String update() {
		ProccessResultBean resultBean = null;
		try {
			this.genericDAO.update("FinProjectMapper.update", finproject);
			resultBean = new ProccessResultBean(true, "项目信息提交成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "项目信息提交不成功!!!");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	private void flowInfo() {
		project.setSpstatus(this.getParameter("status"));
		
		//审批通过
		if(StringUtils.equals(project.getSpstatus(), "SUCCESS")) {
			if(StringUtils.equals(project.getCurActivityName(), "SF_PROCESS")) {
				project.setHandleType("ROLE");
				project.setHandler("projectsp");
				project.setCurActivityName("ZB_PROCESS");
				project.setHandlerlimit("ZB");
			//文化部科技司环节
			} else if(StringUtils.equals(project.getCurActivityName(), "ZB_PROCESS")) {
				project.setHandleType("FINISH");
				project.setHandler("FINISH");
				project.setCurActivityName("FINISH");
				finproject.setIssp("NO");
			}
		} else if(StringUtils.equals(project.getSpstatus(), "NONSP")) {
			//申报人再次申报环节或者中期、结项检查起草
			if(StringUtils.equals(project.getCurActivityName(), "DRAFT")) {
				project.setHandlerlimit(project.getProprovincecode());
				project.setHandleType("ROLE");
				project.setHandler("projectsp");
				if(StringUtils.equals(project.getProprovincecode(), "ZB")) {
					project.setCurActivityName("ZB_PROCESS");
				}else {
					project.setCurActivityName("SF_PROCESS");
				}
			}
		} else {
			//审批不通过
			if(StringUtils.equals(project.getCurActivityName(), "SF_PROCESS")) {
				//省文化厅环节
				project.setHandleType("USER");
				project.setHandler(project.getCreater());
				project.setCurActivityName("DRAFT");
			} else if(StringUtils.equals(project.getCurActivityName(), "ZB_PROCESS")) {
				//文化部科技司环节
				if(StringUtils.equals(project.getProprovincecode(), "ZB")) {
					project.setHandleType("USER");
					project.setHandler(project.getCreater());
					project.setCurActivityName("DRAFT");
				}else {
					project.setHandleType("ROLE");
					project.setHandler("projectsp");
					project.setCurActivityName("SF_PROCESS");
					project.setHandlerlimit(project.getProprovincecode());
				}
			}
		}
	}
	
	private void insertMind(String pid,String tableid,String minds,String activityName) throws DAOException {
		Minds mind = new Minds();
		
		mind.setMid(SequenceUtils.createPrimaryKeySeq());
		mind.setPid(pid);
		mind.setTableid(tableid);
		
		if(StringUtils.equals("DRAFT", activityName)) {
			mind.setActivityname("申报环节");
		} else if(StringUtils.equals("SF_PROCESS", activityName)) {
			mind.setActivityname("省文化厅审批环节");
		} else if(StringUtils.equals("ZB_PROCESS", activityName)) {
			mind.setActivityname("文化部科技司审批环节");
		} 
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mind.setCreattime(df.format(new Date()));
		mind.setMinds(minds + mind.getCreattime());
		
		this.genericDAO.insert("MindsMapper.insert", mind);
	}
	
	
}

