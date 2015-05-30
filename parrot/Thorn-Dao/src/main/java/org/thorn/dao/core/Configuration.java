package org.thorn.dao.core;
/** 
 * @ClassName: Configuration 
 * @Description:
 * @author chenyun
 * @date 2012-4-26 下午04:44:24 
 */
public interface Configuration {
	
	/** 分页查询的start参数 */
	public final static String PAGE_START = "start";
	/** 分页查询的limit参数 */
	public final static String PAGE_LIMIT = "limit";
	/** 排序字段参数 */
	public final static String SROT_NAME = "sort";
	/** 顺序参数 */
	public final static String ORDER_NAME = "dir";
	
	public final static String ORDER_ASC = "asc";
	
	public final static String ORDER_DESC = "desc";
	/** YES **/
	public final static String DB_YES = "YES";
	/** NO **/
	public final static String DB_NO = "NO";
	/** SUCCESS **/
	public final static String DB_SUCCESS = "SUCCESS";
	/** FAILURE **/
	public final static String DB_FAILURE = "FAILURE";
	
	
	public final static String OP_SAVE = "save";
	
	public final static String OP_MODIFY = "modify";
}

