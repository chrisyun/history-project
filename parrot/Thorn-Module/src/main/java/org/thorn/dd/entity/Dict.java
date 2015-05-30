package org.thorn.dd.entity;

import java.io.Serializable;

/**
 * @ClassName: Dict
 * @Description:
 * @author chenyun
 * @date 2012-5-7 上午09:51:09
 */
public class Dict implements Serializable {

	/** */
	private static final long serialVersionUID = 7558362196715243493L;

	private String dname;

	private String dvalue;

	private int sortNum;

	private String typeId;

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public String getDvalue() {
		return dvalue;
	}

	public void setDvalue(String dvalue) {
		this.dvalue = dvalue;
	}

	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

}
