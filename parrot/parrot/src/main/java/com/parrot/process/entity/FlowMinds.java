package com.parrot.process.entity;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.thorn.dao.mybatis.annotation.Mapper;
import org.thorn.dao.mybatis.annotation.MapperNode;
import org.thorn.dao.mybatis.annotation.MethodType;

/** 
 * @ClassName: FlowMinds 
 * @Description: 
 * @author chenyun
 * @date 2012-8-9 上午10:51:14 
 */
@Mapper(nameSpace="FlowMindsMapper",node= {
		@MapperNode(id="insert",type=MethodType.INSERT),
		@MapperNode(id="update",type=MethodType.UPDATE),
		@MapperNode(id="delete",type=MethodType.DELETE_BATCH),
		@MapperNode(id="queryList",type=MethodType.QUERY_LIST)
})
public class FlowMinds implements Serializable {
	
	/** */
	private static final long serialVersionUID = -4856007665108428162L;

	private Integer id;
	
	private Integer flowId;
	
	private String userId;
	
	private String userName;
	
	private String mind;
	
	private String time;
	
	private String activityName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFlowId() {
		return flowId;
	}

	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMind() {
		return mind;
	}

	public void setMind(String mind) {
		this.mind = mind;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		if(StringUtils.isNotBlank(time) 
				&& time.length() > 19) {
			time = time.substring(0, 19);
		}
		
		this.time = time;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
}

