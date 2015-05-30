package com.parrot.app.entity;

import java.io.Serializable;

import org.thorn.dao.mybatis.annotation.Mapper;
import org.thorn.dao.mybatis.annotation.MapperNode;
import org.thorn.dao.mybatis.annotation.MethodType;

/** 
 * @ClassName: CostBudget 
 * @Description: 
 * @author chenyun
 * @date 2012-8-9 上午10:44:08 
 */
@Mapper(nameSpace="CostBudgetMapper",node= {
		@MapperNode(id="insert",type=MethodType.INSERT),
		@MapperNode(id="update",type=MethodType.UPDATE),
		@MapperNode(id="selectList",type=MethodType.QUERY_LIST)
})
public class CostBudget implements Serializable {
	
	/** */
	private static final long serialVersionUID = 8691006689017041036L;

	private Integer id;
	
	private Integer pid;
	
	private String type;
	
	private String detail;
	
	private String remark;
	
	private double money;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if(o instanceof CostBudget) {
			return ((CostBudget) o).getDetail().equals(this.detail);
		}
		
		return o.hashCode() == this.hashCode();
	}
	
}

