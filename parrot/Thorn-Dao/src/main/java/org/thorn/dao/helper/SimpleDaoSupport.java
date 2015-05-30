package org.thorn.dao.helper;

import org.thorn.dao.exception.DBAccessException;

/** 
 * @ClassName: SimpleDaoSupport 
 * @Description: 定义简单DAO操作方法，只能进行简单的单对象的处理
 * @author chenyun
 * @date 2012-4-26 下午01:33:46 
 */
public interface SimpleDaoSupport {
	
	public int save(Object obj) throws DBAccessException;
	
	public int modify(Object obj) throws DBAccessException;
	
	public int delete(Object obj) throws DBAccessException;
}

