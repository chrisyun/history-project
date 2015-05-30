package com.parrot.app.entity;

import java.io.Serializable;

/** 
 * @ClassName: CostSummary 
 * @Description: 
 * @author chenyun
 * @date 2012-8-18 下午09:42:26 
 */
public class CostSummary implements Serializable {

	/** */
	private static final long serialVersionUID = -4791944690943005548L;
	
	private String num;
	
	private String name;
	
	private String money;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
}

