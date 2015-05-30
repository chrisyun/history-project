package org.thorn.web.entity;
/** 
 * @ClassName: JsonResponse 
 * @Description: 表示数据结果的实体对象，供给前台使用。除了状态外，还返回了数据
 * @author chenyun
 * @date 2012-5-13 上午11:02:48 
 */
public class JsonResponse<T> extends Status {

	/** */
	private static final long serialVersionUID = 4937313269465119475L;
	
	private T obj;

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}
}

