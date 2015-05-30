package com.parrot.app.entity;

import java.io.Serializable;

import org.thorn.dao.mybatis.annotation.Mapper;
import org.thorn.dao.mybatis.annotation.MapperNode;
import org.thorn.dao.mybatis.annotation.MethodType;

@Mapper(nameSpace="ProjectFundMapper",node= {
		@MapperNode(id="insert",type=MethodType.INSERT),
		@MapperNode(id="update",type=MethodType.UPDATE),
		@MapperNode(id="delete",type=MethodType.DELETE_BATCH),
		@MapperNode(id="selectList",type=MethodType.QUERY),
		@MapperNode(id="selectList",type=MethodType.QUERY_LIST)
})
public class ProjectFund implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3973094307727835183L;

	private Integer id;
	
	private Integer year;
	
	private Integer pid;
	
	private Double fund;
	
	private String content;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Double getFund() {
		return fund;
	}

	public void setFund(Double fund) {
		this.fund = fund;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
