package com.parrot.process.entity;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.thorn.dao.mybatis.annotation.Mapper;
import org.thorn.dao.mybatis.annotation.MapperNode;
import org.thorn.dao.mybatis.annotation.MethodType;

/**
 * @ClassName: Process
 * @Description:
 * @author chenyun
 * @date 2012-8-9 上午10:50:56
 */
@Mapper(nameSpace="ProcessMapper",node= {
		@MapperNode(id="insert",type=MethodType.INSERT),
		@MapperNode(id="update",type=MethodType.UPDATE),
		@MapperNode(id="delete",type=MethodType.DELETE_BATCH),
		@MapperNode(id="selectList",type=MethodType.QUERY),
		@MapperNode(id="selectPageCount",type=MethodType.COUNT),
		@MapperNode(id="selectPage",type=MethodType.QUERY_LIST)
})
public class Process implements Serializable {

	/** */
	private static final long serialVersionUID = -5363259355484582696L;

	private Integer id;
	
	private String flowType;
	
	private String activity;
	
	private String flowStatus;
	
	private String creater;
	
	private String createrName;
	
	private String createTime;
	
	private Integer pid;
	
	private String handler;
	
	private String handlerType;
	
	private String province;
	
	private String pName;
	
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFlowType() {
		return flowType;
	}
	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		if(StringUtils.isNotBlank(createTime) 
				&& createTime.length() > 19) {
			createTime = createTime.substring(0, 19);
		}
		this.createTime = createTime;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getHandler() {
		return handler;
	}
	public void setHandler(String handler) {
		this.handler = handler;
	}
	public String getHandlerType() {
		return handlerType;
	}
	public void setHandlerType(String handlerType) {
		this.handlerType = handlerType;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
}
