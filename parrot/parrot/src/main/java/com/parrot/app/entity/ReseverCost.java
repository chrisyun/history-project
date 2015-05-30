package com.parrot.app.entity;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.thorn.dao.mybatis.annotation.Mapper;
import org.thorn.dao.mybatis.annotation.MapperNode;
import org.thorn.dao.mybatis.annotation.MethodType;

/** 
 * @ClassName: ReseverCost 
 * @Description: 
 * @author chenyun
 * @date 2012-8-9 上午10:44:41 
 */
@Mapper(nameSpace="ReseverCostMapper",node= {
		@MapperNode(id="insert",type=MethodType.INSERT),
		@MapperNode(id="update",type=MethodType.UPDATE),
		@MapperNode(id="delete",type=MethodType.DELETE_BATCH),
		@MapperNode(id="selectPageCount",type=MethodType.COUNT),
		@MapperNode(id="selectList",type=MethodType.QUERY),
		@MapperNode(id="selectPage",type=MethodType.QUERY_LIST)
})
public class ReseverCost implements Serializable {
	
	/** */
	private static final long serialVersionUID = -3568906545691069048L;

	private Integer id;
	
	private Integer reseverId;
	
	private String reseverName;
	
	private Integer year;
	
	private String creater;
	
	private String createrName;
	
	private String province;
	
	private String attids;
	
	private String address;
	
	private String postalCode;
	
	private String contacts;
	
	private String phone;
	
	private String appReason;
	
	private String content;
	
	private String target;
	
	private String budget;
	
	private Double applyMoney;
	
	private Double givenMoney;
	
	private String applyTime;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReseverId() {
		return reseverId;
	}

	public void setReseverId(Integer reseverId) {
		this.reseverId = reseverId;
	}

	public String getReseverName() {
		return reseverName;
	}

	public void setReseverName(String reseverName) {
		this.reseverName = reseverName;
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

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public Double getApplyMoney() {
		return applyMoney;
	}

	public void setApplyMoney(Double applyMoney) {
		this.applyMoney = applyMoney;
	}

	public Double getGivenMoney() {
		return givenMoney;
	}

	public void setGivenMoney(Double givenMoney) {
		this.givenMoney = givenMoney;
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
}

