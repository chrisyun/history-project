package com.parrot.app.entity;

import java.io.Serializable;

import org.thorn.dao.mybatis.annotation.Mapper;
import org.thorn.dao.mybatis.annotation.MapperNode;
import org.thorn.dao.mybatis.annotation.MethodType;

/** 
 * @ClassName: Heritor 
 * @Description: 
 * @author chenyun
 * @date 2012-8-9 上午10:43:46 
 */
@Mapper(nameSpace="HeritorMapper",node= {
		@MapperNode(id="insert",type=MethodType.INSERT),
		@MapperNode(id="update",type=MethodType.UPDATE),
		@MapperNode(id="delete",type=MethodType.DELETE_BATCH),
		@MapperNode(id="selectPageCount",type=MethodType.COUNT),
		@MapperNode(id="selectPage",type=MethodType.QUERY_LIST)
})
public class Heritor implements Serializable {
	
	/** */
	private static final long serialVersionUID = 7080576894216697092L;

	private Integer id;
	
	private String name;
	
	private String idCard;
	
	private String gender;
	
	private String batchNum;
	
	private String remark;
	
	private String isDie;
	
	private String dieDate;
	
	private Integer projectId;
	
	private String projectName;
	
	private String projectCode;
	
	private String province;
	
	private String birthDate;
	
	private String minority;
	
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

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsDie() {
		return isDie;
	}

	public void setIsDie(String isDie) {
		this.isDie = isDie;
	}

	public String getDieDate() {
		return dieDate;
	}

	public void setDieDate(String dieDate) {
		this.dieDate = dieDate;
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

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getMinority() {
		return minority;
	}

	public void setMinority(String minority) {
		this.minority = minority;
	}
}

