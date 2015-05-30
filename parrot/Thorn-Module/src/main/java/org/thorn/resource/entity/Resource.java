package org.thorn.resource.entity;

import java.io.Serializable;

/**
 * @ClassName: Resource
 * @Description:
 * @author chenyun
 * @date 2012-5-4 下午03:34:10
 */
public class Resource implements Serializable {

	/** */
	private static final long serialVersionUID = -4442394145188069847L;

	private String sourceCode;

	private String sourceUrl;

	private String sourceName;

	private String isleaf;

	private String isShow;

	private String parentSource;

	private int sortNum;

	private String iconsCls;
	
	public String getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(String isleaf) {
		this.isleaf = isleaf;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getParentSource() {
		return parentSource;
	}

	public void setParentSource(String parentSource) {
		this.parentSource = parentSource;
	}

	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}

	public String getIconsCls() {
		return iconsCls;
	}

	public void setIconsCls(String iconsCls) {
		this.iconsCls = iconsCls;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

}
