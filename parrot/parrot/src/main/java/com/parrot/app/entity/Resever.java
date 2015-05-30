package com.parrot.app.entity;

import java.io.Serializable;

import org.thorn.dao.mybatis.annotation.Mapper;
import org.thorn.dao.mybatis.annotation.MapperNode;
import org.thorn.dao.mybatis.annotation.MethodType;

/** 
 * @ClassName: Resever 
 * @Description: 
 * @author chenyun
 * @date 2012-8-9 上午10:44:25 
 */
@Mapper(nameSpace="ReseverMapper",node= {
		@MapperNode(id="insert",type=MethodType.INSERT),
		@MapperNode(id="update",type=MethodType.UPDATE),
		@MapperNode(id="delete",type=MethodType.DELETE_BATCH),
		@MapperNode(id="selectPageCount",type=MethodType.COUNT),
		@MapperNode(id="selectPage",type=MethodType.QUERY_LIST)
})
public class Resever implements Serializable {
	
	/** */
	private static final long serialVersionUID = 3799670210897226440L;

	private Integer id;
	
	private String name;
	
	private String buildTime;
	
	private String area;
	
	private String userId;
	
	private String province;
	
	private String userName;
	
	private String replyTime;
	
	private Integer serNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(String buildTime) {
		this.buildTime = buildTime;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	public Integer getSerNo() {
		return serNo;
	}

	public void setSerNo(Integer serNo) {
		this.serNo = serNo;
	}
}

