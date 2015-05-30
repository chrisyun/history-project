package org.thorn.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 
 * @ClassName: FullTree 
 * @Description: 能够完整表示tree的实体对象
 * @author chenyun
 * @date 2012-5-18 下午06:11:09 
 */
public class FullTree extends Tree implements Serializable {

	/** */
	private static final long serialVersionUID = 5097710969654517743L;
	
	private String uiProvider;
	
	private List<FullTree> children = new ArrayList<FullTree>(); 
	
	private boolean expanded;

	public String getUiProvider() {
		return uiProvider;
	}

	public void setUiProvider(String uiProvider) {
		this.uiProvider = uiProvider;
	}

	public List<FullTree> getChildren() {
		return children;
	}

	public void setChildren(List<FullTree> children) {
		this.children = children;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
}

