package com.parrot.app.entity;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.thorn.dao.mybatis.annotation.Mapper;
import org.thorn.dao.mybatis.annotation.MapperNode;
import org.thorn.dao.mybatis.annotation.MethodType;

/** 
 * @ClassName: ProjectCost 
 * @Description: 
 * @author chenyun
 * @date 2012-8-9 上午10:43:10 
 */
@Mapper(nameSpace="ProjectCostMapper",node= {
		@MapperNode(id="insert",type=MethodType.INSERT),
		@MapperNode(id="update",type=MethodType.UPDATE),
		@MapperNode(id="delete",type=MethodType.DELETE_BATCH),
		@MapperNode(id="selectPageCount",type=MethodType.COUNT),
		@MapperNode(id="selectList",type=MethodType.QUERY),
		@MapperNode(id="selectPage",type=MethodType.QUERY_LIST)
})
public class ProjectCost implements Serializable {

	/** */
	private static final long serialVersionUID = 7749363773185467068L;

	private Integer id;
	
	private Integer projectId;
	
	private String projectName;
	
	private Integer year;
	
	private String creater;
	
	private String createrName;
	
	private String province;
	
	private String attids;
	
	private String address;
	
	private String postalCode;
	
	private String contacts;
	
	private String phone;
	
	private String bankName;
	
	private String bank;
	
	private String bankAccount;
	
	private String companyCtf;

	private String appReason;
	
	private String content;
	
	private String target;
	
	private String usedYear;
	
	private String budget;
	
	private Double money;
	
	private String applyTime;
	
	private String isUnProject;
	
	private String type;
	
	private String code;
	
	private Integer ueId;
	
	private String area;
	
	private String activity;
	
	private String flowStatus;

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getAttids() {
		return attids;
	}

	public void setAttids(String attids) {
		this.attids = attids;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getCompanyCtf() {
		return companyCtf;
	}

	public void setCompanyCtf(String companyCtf) {
		this.companyCtf = companyCtf;
	}

	public String getAppReason() {
		return appReason;
	}

	public void setAppReason(String appReason) {
		this.appReason = appReason;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getUsedYear() {
		return usedYear;
	}

	public void setUsedYear(String usedYear) {
		this.usedYear = usedYear;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		if(StringUtils.isNotBlank(applyTime) 
				&& applyTime.length() > 19) {
			applyTime = applyTime.substring(0, 19);
		}
		
		this.applyTime = applyTime;
	}

	public String getIsUnProject() {
		return isUnProject;
	}

	public void setIsUnProject(String isUnProject) {
		this.isUnProject = isUnProject;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getUeId() {
		return ueId;
	}

	public void setUeId(Integer ueId) {
		this.ueId = ueId;
	}
}

