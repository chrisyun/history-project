package com.talkweb.ncfw.entity;
/**
 * <p>文件名称: Project.java</p>
 * <p>文件描述: 本类描述</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2011-10-12</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  chenyun
 */
public class Project {
	
	private String pid;//主键ID
	private String pname;//项目名称
	private String pmtype;//项目类型
	private String pstatus;//项目状态
	private String spstatus;//审批状态
	private String creattime;//创建时间
	private String creater;//创建人
	private String pmanager;//项目负责人
	private String tel;//联系电话
	private String email;//联系EMAIL
	private String proprovincecode;//申请人省份code PROPROVINCECODE
	private String handleType;//处理类型
	private String handler;//处理者
	private String handlerlimit;//处理者省份限制
	private String curActivityName;//当前处理环节
	
	private String psecret;//密级
	private String pslnumber;//受理编号
	private String plxnumber;//立项编号
	private String psbtype;//申报类别
	private String ptype;//项目类别
	private String cdorg;//承担单位
	private String sborg;//申报部门
	private String sbdate;//申报日期
	private String gjorg;//共建部门
	private String jsorg;//所属标准化技术委员会
	private String hyorg;//行业主管部门
	private String tjorg;//推荐单位
	private String pwtype;
	
	private String isInput;//是否人工录入，yes为是
	
	public String getIsInput() {
		return isInput;
	}
	public void setIsInput(String isInput) {
		this.isInput = isInput;
	}
	public String getPwtype() {
		return pwtype;
	}
	public void setPwtype(String pwtype) {
		this.pwtype = pwtype;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPmtype() {
		return pmtype;
	}
	public void setPmtype(String pmtype) {
		this.pmtype = pmtype;
	}
	public String getPstatus() {
		return pstatus;
	}
	public void setPstatus(String pstatus) {
		this.pstatus = pstatus;
	}
	public String getSpstatus() {
		return spstatus;
	}
	public void setSpstatus(String spstatus) {
		this.spstatus = spstatus;
	}
	public String getCreattime() {
		return creattime;
	}
	public void setCreattime(String creattime) {
		this.creattime = creattime;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getPmanager() {
		return pmanager;
	}
	public void setPmanager(String pmanager) {
		this.pmanager = pmanager;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPsecret() {
		return psecret;
	}
	public void setPsecret(String psecret) {
		this.psecret = psecret;
	}
	public String getPslnumber() {
		return pslnumber;
	}
	public void setPslnumber(String pslnumber) {
		this.pslnumber = pslnumber;
	}
	public String getPlxnumber() {
		return plxnumber;
	}
	public void setPlxnumber(String plxnumber) {
		this.plxnumber = plxnumber;
	}
	public String getPsbtype() {
		return psbtype;
	}
	public void setPsbtype(String psbtype) {
		this.psbtype = psbtype;
	}
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	public String getCdorg() {
		return cdorg;
	}
	public void setCdorg(String cdorg) {
		this.cdorg = cdorg;
	}
	public String getSborg() {
		return sborg;
	}
	public void setSborg(String sborg) {
		this.sborg = sborg;
	}
	public String getSbdate() {
		return sbdate;
	}
	public void setSbdate(String sbdate) {
		this.sbdate = sbdate;
	}
	public String getGjorg() {
		return gjorg;
	}
	public void setGjorg(String gjorg) {
		this.gjorg = gjorg;
	}
	public String getJsorg() {
		return jsorg;
	}
	public void setJsorg(String jsorg) {
		this.jsorg = jsorg;
	}
	public String getHyorg() {
		return hyorg;
	}
	public void setHyorg(String hyorg) {
		this.hyorg = hyorg;
	}
	public String getTjorg() {
		return tjorg;
	}
	public void setTjorg(String tjorg) {
		this.tjorg = tjorg;
	}
	public String getProprovincecode() {
		return proprovincecode;
	}
	public void setProprovincecode(String proprovincecode) {
		this.proprovincecode = proprovincecode;
	}
	public String getHandleType() {
		return handleType;
	}
	public void setHandleType(String handleType) {
		this.handleType = handleType;
	}
	public String getHandler() {
		return handler;
	}
	public void setHandler(String handler) {
		this.handler = handler;
	}
	public String getCurActivityName() {
		return curActivityName;
	}
	public void setCurActivityName(String curActivityName) {
		this.curActivityName = curActivityName;
	}
	public String getHandlerlimit() {
		return handlerlimit;
	}
	public void setHandlerlimit(String handlerlimit) {
		this.handlerlimit = handlerlimit;
	}
	
}

