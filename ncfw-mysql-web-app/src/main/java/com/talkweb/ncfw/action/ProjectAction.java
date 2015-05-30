package com.talkweb.ncfw.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.talkweb.ncframework.modules.datadict.utils.DataDictUtils;
import com.talkweb.ncframework.pub.common.PageBean;
import com.talkweb.ncframework.pub.common.ProccessResultBean;
import com.talkweb.ncframework.pub.dao.IGenericDAO;
import com.talkweb.ncframework.pub.exceptions.DAOException;
import com.talkweb.ncframework.pub.report.poi.ExcelExportBean;
import com.talkweb.ncframework.pub.report.poi.ExportUtils;
import com.talkweb.ncframework.pub.report.poi.ReportBeanUtils;
import com.talkweb.ncframework.pub.utils.SequenceUtils;
import com.talkweb.ncframework.pub.utils.StringUtils;
import com.talkweb.ncframework.pub.web.common.utils.HttpServletUtils;
import com.talkweb.ncframework.pub.web.struts2.BaseAction;
import com.talkweb.ncfw.entity.Minds;
import com.talkweb.ncfw.entity.Org;
import com.talkweb.ncfw.entity.Project;
import com.talkweb.ncfw.entity.User;
import com.talkweb.ncfw.file.IFileService;
import com.talkweb.security.SecurityHelper;

/**
 * <p>文件名称: Project.java</p>
 * <p>文件描述: 本类描述</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2011-10-26</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  chenyun
 */
public class ProjectAction extends BaseAction {
	static Logger log = Logger.getLogger(ProjectAction.class);
	
	private IGenericDAO genericDAO;
	private IFileService fileService;
	private Project project;
	public IGenericDAO getGenericDAO() {
		return genericDAO;
	}
	public void setGenericDAO(IGenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public IFileService getFileService() {
		return fileService;
	}
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}
	/**
	 * 
	 * @author：chenyun 	        
	 * @date：2011-10-26
	 * @Description：待办列表展示
	 * @return
	 */
	public String searchPendingList() {
		User user = SecurityHelper.getCurrentUser().getUser();
		String uid = user.getUserid();
		String roleid = user.getRoleid();
		
		Map condition = this.getParameterMap();
		PageBean<Project> pageBean = new PageBean ();
		
		condition.put("spuserid", uid);
		condition.put("sproleid", roleid);
		condition.put("pendinglimit", user.getProvincenumber());
		
		Long count = this.getGenericDAO().getCount("ProjectMapper.getPendingCount", condition);
		pageBean.setTotalProperty(count);
		if (count > 0) {
			List<Project> projectList = this.getGenericDAO().queryList("ProjectMapper.queryPendingList", condition);
			pageBean.setResultList(projectList);
		}
		HttpServletUtils.outJson(this.getResponse(), pageBean);
		return null;
	}
	
	public String updateProjectStatus() {
		ProccessResultBean resultBean 	= null;
		String pids		= this.getParameter("pids");
		if (StringUtils.isEmpty(pids)) {
			resultBean = new ProccessResultBean(false, "项目编号不能为空.");
			HttpServletUtils.outJson(this.getResponse(), resultBean);
			return null;
		}
		
		String[] pidArray= pids.split(",");
		Map para = new HashMap();
		para.put("pidArray", pidArray);
		para.put("pstatus", "LX");
		try {
			this.getGenericDAO().update("ProjectMapper.updateStatus", para);
			resultBean = new ProccessResultBean(true, "项目立项成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "更新项目状态失败.");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	public String modifyProjectStatus() {
		ProccessResultBean resultBean 	= null;
		String pids		= this.getParameter("pids");
		String status = this.getParameter("pro_status");
		if (StringUtils.isEmpty(pids)) {
			resultBean = new ProccessResultBean(false, "项目编号不能为空.");
			HttpServletUtils.outJson(this.getResponse(), resultBean);
			return null;
		}
		
		String[] pidArray= pids.split(",");
		Map para = new HashMap();
		para.put("pidArray", pidArray);
		para.put("pstatus", status);
		try {
			this.getGenericDAO().update("ProjectMapper.updateStatus", para);
			resultBean = new ProccessResultBean(true, "项目状态修改成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "更新项目状态失败.");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	public String updatePWType() {
		ProccessResultBean resultBean 	= null;
		String pids		= this.getParameter("pids");
		if (StringUtils.isEmpty(pids)) {
			resultBean = new ProccessResultBean(false, "项目编号不能为空.");
			HttpServletUtils.outJson(this.getResponse(), resultBean);
			return null;
		}
		
		String[] pidArray= pids.split(",");
		Map para = new HashMap();
		para.put("pidArray", pidArray);
		para.put("pwtype", this.getParameter("pwtype"));
		try {
			this.getGenericDAO().update("ProjectMapper.updatePWType", para);
			resultBean = new ProccessResultBean(true, "项目分类成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "项目分类失败.");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	public String deleteProject() {
		ProccessResultBean resultBean 	= null;
		String pids		= this.getParameter("pids");
		if (StringUtils.isEmpty(pids)) {
			resultBean = new ProccessResultBean(false, "项目ID不能为空.");
			HttpServletUtils.outJson(this.getResponse(), resultBean);
			return null;
		}
		
		String[] pidArray= pids.split(",");
		try {
			this.getGenericDAO().delete("ProjectMapper.deleteBatch", pidArray);
			resultBean = new ProccessResultBean(true, "项目删除成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "项目删除失败.");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	
	/**
	 * 
	 * @author：chenyun 	        
	 * @date：2011-10-26
	 * @Description：查询所有的项目
	 * 1、直属单位用户：只能看到自己新建的
	 * 2、省份用户：只能看到自己省份的
	 * 3、总部用户：可以看到所有的
	 * @return
	 */
	public String searchAll() {
		User user = SecurityHelper.getCurrentUser().getUser();
		Org org = SecurityHelper.getCurrentUser().getOrg();
		String uid = user.getUserid();
		String provinceCode = user.getProvincenumber();
		String userType = org.getOrgtype();
		
		Map condition = this.getParameterMap();
		
		//直属单位用户
		if(StringUtils.equals("DW", userType)) {
			condition.put("creater", uid);
		} else if(! StringUtils.equals("ZB", provinceCode)) {
			//省份用户
			condition.put("proprovincecode", provinceCode);
		}
		
		String roleId = user.getRoleid();
		if(roleId.indexOf("pw_") >= 0) {
			condition.put("pwtype", roleId);
		}
		
		PageBean<Project> pageBean = new PageBean ();
		Long count = this.getGenericDAO().getCount("ProjectMapper.getAllCount", condition);
		pageBean.setTotalProperty(count);
		if (count > 0) {
			List<Project> projectList = this.getGenericDAO().queryList("ProjectMapper.queryAllList", condition);
			pageBean.setResultList(projectList);
		}
		HttpServletUtils.outJson(this.getResponse(), pageBean);
		return null;
	}
	
	public String searchProjectStore() {
		User user = SecurityHelper.getCurrentUser().getUser();
		Org org = SecurityHelper.getCurrentUser().getOrg();
		String uid = user.getUserid();
		String provinceCode = user.getProvincenumber();
		String userType = org.getOrgtype();
		
		Map condition = this.getParameterMap();
		condition.put("pstatusNot", "SB");
		
		String roleId = user.getRoleid();
		if(roleId.indexOf("pw_") >= 0) {
			condition.put("pwtype", roleId);
		}
		
		//直属单位用户
		if(StringUtils.equals("DW", userType)) {
			condition.put("creater", uid);
		} else if(! StringUtils.equals("ZB", provinceCode)) {
			//省份用户
			condition.put("proprovincecode", provinceCode);
		}
		
		PageBean<Project> pageBean = new PageBean ();
		Long count = this.getGenericDAO().getCount("ProjectMapper.getAllCount", condition);
		pageBean.setTotalProperty(count);
		if (count > 0) {
			List<Project> projectList = this.getGenericDAO().queryList("ProjectMapper.queryAllList", condition);
			pageBean.setResultList(projectList);
		}
		HttpServletUtils.outJson(this.getResponse(), pageBean);
		return null;
	}
	
	/**
	 * 
	 * @author：chenyun 	        
	 * @date：2011-12-5
	 * @Description：查询流程意见
	 * @return
	 */
	public String searchMinds() {
		Map condition = this.getParameterMap();
		
		List<Minds> list = this.getGenericDAO().queryList("MindsMapper.queryMindList", condition);
		HttpServletUtils.outJsonArray(this.getResponse(), list);
		return null;
	}
	
	public String exportProject() {
		Map condition = this.getParameterMap();
		User user = SecurityHelper.getCurrentUser().getUser();
		Org org = SecurityHelper.getCurrentUser().getOrg();
		String uid = user.getUserid();
		String provinceCode = user.getProvincenumber();
		String userType = org.getOrgtype();
		
		String roleId = user.getRoleid();
		if(roleId.indexOf("pw_") >= 0) {
			condition.put("pwtype", roleId);
		}
		
		//直属单位用户
		if(StringUtils.equals("DW", userType)) {
			condition.put("creater", uid);
		} else if(! StringUtils.equals("ZB", provinceCode)) {
			//省份用户
			condition.put("proprovincecode", provinceCode);
		}
		
		List<Project> projectList = this.getGenericDAO().queryList("ProjectMapper.queryList", condition);
		for(Project project : projectList) {
			project.setCurActivityName(DataDictUtils.getDictEntryName("FLOW_ACTIVITYNAME", project.getCurActivityName()));
			project.setPmtype(DataDictUtils.getDictEntryName("PROJECT_TYPE", project.getPmtype()));
			project.setPstatus(DataDictUtils.getDictEntryName("PROJECT_STATUS", project.getPstatus()));
			project.setSpstatus(DataDictUtils.getDictEntryName("FLOW_STATUS", project.getSpstatus()));
			project.setProprovincecode(DataDictUtils.getDictEntryName("COMMON_PROVINCE", project.getProprovincecode()));
		}
		
		String[] dataIndexArray = (new String[] {
				"pname", "pslnumber", "pmtype", "pstatus", "spstatus", "curActivityName", "creater", "proprovincecode", "creattime"
		});
		
		List<String[]> columnHeadList = new ArrayList<String[]>();
		String[] columnHeadArray = (new String[] {
				"项目名称", "项目编号", "项目类别", "项目状态", "审批状态", "当前处理环节", "项目申报人", "申报省份", "申报时间"
		});
		columnHeadList.add(columnHeadArray);
		
		String[] columnWidthArray = (new String[] {
				"150", "150", "150", "80", "80", "120","120","80","80"
		});
		
		ExcelExportBean excel = ReportBeanUtils.createExcelBean(projectList, dataIndexArray, columnHeadList, "申报项目", columnWidthArray, -1);
		try {
			ExportUtils.exportExcel(excel, this.getRequest(), this.getResponse());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	
	
	
	public String exportProjectStore() {
		Map condition = this.getParameterMap();
		condition.put("pstatusNot", "SB");
		User user = SecurityHelper.getCurrentUser().getUser();
		Org org = SecurityHelper.getCurrentUser().getOrg();
		String uid = user.getUserid();
		String provinceCode = user.getProvincenumber();
		String userType = org.getOrgtype();
		
		String roleId = user.getRoleid();
		if(roleId.indexOf("pw_") >= 0) {
			condition.put("pwtype", roleId);
		}
		
		//直属单位用户
		if(StringUtils.equals("DW", userType)) {
			condition.put("creater", uid);
		} else if(! StringUtils.equals("ZB", provinceCode)) {
			//省份用户
			condition.put("proprovincecode", provinceCode);
		}
		
		List<Project> projectList = this.getGenericDAO().queryList("ProjectMapper.queryList", condition);
		for(Project project : projectList) {
			project.setCurActivityName(DataDictUtils.getDictEntryName("FLOW_ACTIVITYNAME", project.getCurActivityName()));
			project.setPmtype(DataDictUtils.getDictEntryName("PROJECT_TYPE", project.getPmtype()));
			project.setPstatus(DataDictUtils.getDictEntryName("PROJECT_STATUS", project.getPstatus()));
			project.setSpstatus(DataDictUtils.getDictEntryName("FLOW_STATUS", project.getSpstatus()));
			project.setProprovincecode(DataDictUtils.getDictEntryName("COMMON_PROVINCE", project.getProprovincecode()));
		}
		
		String[] dataIndexArray = (new String[] {
				"pname", "pslnumber", "pmtype", "pstatus", "spstatus", "curActivityName", "creater", "proprovincecode", "creattime"
		});
		
		List<String[]> columnHeadList = new ArrayList<String[]>();
		String[] columnHeadArray = (new String[] {
				"项目名称", "项目编号", "项目类别", "项目状态", "审批状态", "当前处理环节", "项目申报人", "申报省份", "申报时间"
		});
		columnHeadList.add(columnHeadArray);
		
		String[] columnWidthArray = (new String[] {
				"150", "150", "150", "80", "80", "120","120","80","80"
		});
		
		ExcelExportBean excel = ReportBeanUtils.createExcelBean(projectList, dataIndexArray, columnHeadList, "项目成果库", columnWidthArray, -1);
		try {
			ExportUtils.exportExcel(excel, this.getRequest(), this.getResponse());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String queryProject() {
		Map condition = new HashMap<String, String>();
		String pid = this.getParameter("pid");
		condition.put("pid", pid);
		
		Object proObj = this.getGenericDAO().load("ProjectMapper.selectProjectById", condition);
		HttpServletUtils.outJson(this.getResponse(), proObj);
		return null;
	}
	
	public String addInputProject() {
		User user = SecurityHelper.getCurrentUser().getUser();
		
		project.setCreattime(project.getSbdate());
		project.setPid(SequenceUtils.createPrimaryKeySeq());
		
		project.setPstatus("LX");
		project.setSpstatus("SUCCESS");
		project.setCurActivityName("FINISH");
		
		
		String attach = this.getParameter("attach");
		if(!StringUtils.isEmpty(attach)) {
			attach = attach.substring(0,attach.length()-1);
		}
		
		ProccessResultBean resultBean = null;
		
		try {
			this.genericDAO.insert("ProjectMapper.insert", project);
			this.fileService.updateFile(attach, project.getPid(), "t_project");
			
			resultBean = new ProccessResultBean(true, "项目信息添加成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "项目信息添加不成功!!!");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	
	public String addProject() {
		Org org = SecurityHelper.getCurrentUser().getOrg();
		User user = SecurityHelper.getCurrentUser().getUser();
		
		project.setCreater(user.getUserid());
		project.setProprovincecode(user.getProvincenumber());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		project.setCreattime(df.format(new Date()));
		project.setPstatus("SB");
		project.setPid(SequenceUtils.createPrimaryKeySeq());
		project.setSpstatus("NONSP");
		
		String attach = this.getParameter("attach");
		if(!StringUtils.isEmpty(attach)) {
			attach = attach.substring(0,attach.length()-1);
		}
		
		ProccessResultBean resultBean = null;
		
		project.setHandleType("ROLE");
		project.setHandler("projectsp");
		project.setHandlerlimit(project.getProprovincecode());
		if(StringUtils.equals(project.getProprovincecode(), "ZB")) {
			project.setCurActivityName("ZB_PROCESS");
		}else {
			project.setCurActivityName("SF_PROCESS");
		}
		
		String minds = this.getParameter("minds");
		if(StringUtils.isEmpty(minds)) {
			minds = "------------------- " + user.getUsername() + " ";
		} else {
			minds += "------------------- " + user.getUsername() + " ";
		}
		
		
		try {
			this.genericDAO.insert("ProjectMapper.insert", project);
			this.fileService.updateFile(attach, project.getPid(), "t_project");
			this.insertMind(project.getPid(), "t_project", minds, "DRAFT");
			
			resultBean = new ProccessResultBean(true, "项目信息添加成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "项目信息添加不成功!!!");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	public String updateProject() {
		project.setSpstatus(null);
		project.setCurActivityName(null);
		project.setPstatus(null);
		project.setProprovincecode(null);
		project.setHandler(null);
		project.setHandlerlimit(null);
		
		ProccessResultBean resultBean = null;
		try {
			this.genericDAO.update("ProjectMapper.update", project);
			resultBean = new ProccessResultBean(true, "项目信息提交成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "项目信息提交失败.");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
		
	}
	
	public String submitProject() {
		User user = SecurityHelper.getCurrentUser().getUser();
		
		project.setSpstatus(this.getParameter("status"));
		
		String activity = project.getCurActivityName();
		
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
		
		String minds = this.getParameter("minds");
		if(StringUtils.isEmpty(minds)) {
			minds = "------------------- " + user.getUsername() + " ";
		} else {
			minds += "------------------- " + user.getUsername() + " ";
		}
		
		ProccessResultBean resultBean = null;
		try {
			this.genericDAO.insert("ProjectMapper.update", project);
			this.insertMind(project.getPid(), "t_project", minds, activity);
			resultBean = new ProccessResultBean(true, "项目信息提交成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "项目信息提交失败.");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	public synchronized String getCurNumber() {
		String pinying = this.getParameter("pinying");
		
		ProccessResultBean resultBean = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		String year = df.format(new Date());
		String num = null;
		
		try {
			this.genericDAO.update("ProjectMapper.updateMaxNum", year);
			num = (String) genericDAO.load("ProjectMapper.searchMaxNum", year);
			num = "0000".concat(num);
			num = num.substring(num.length()-4);
			resultBean = new ProccessResultBean(true,pinying + year + num);
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false,"生成项目编号失败");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
	public static void main(String[] args) {
		String num = "7777";
		num = "0000".concat(num);
		num = num.substring(num.length()-4);
		System.out.println(num);
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

