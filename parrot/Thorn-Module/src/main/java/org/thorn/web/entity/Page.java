package org.thorn.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 
 * @ClassName: Page 
 * @Description:
 * @author chenyun
 * @date 2012-4-26 下午04:55:47 
 */
public class Page<T> implements Serializable {

	/** */
	private static final long serialVersionUID = 5951452024298265919L;
	/** 结果集总数 */
	private long total;
	/** 结果集 */
	private List<T> reslutSet = new ArrayList<T>();

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getReslutSet() {
		return reslutSet;
	}

	public void setReslutSet(List<T> reslutSet) {
		this.reslutSet = reslutSet;
	}
	
}

