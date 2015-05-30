package org.thorn.dd.dao;

import java.util.List;
import java.util.Map;

import org.thorn.dao.exception.DBAccessException;
import org.thorn.dd.entity.Dict;
import org.thorn.dd.entity.DictType;

/** 
 * @ClassName: IDataDictDao 
 * @Description: 
 * @author chenyun
 * @date 2012-5-7 上午10:39:54 
 */
public interface IDataDictDao {
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:50:56
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public List<DictType> queryDtList(Map<String, Object> filter) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-7-2 下午03:54:57
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public long queryDtPageCount(Map<String, Object> filter) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:51:00
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public List<Dict> queryDdList(Map<String, Object> filter) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:51:03
	 * @param dd
	 * @return
	 * @throws DBAccessException
	 */
	public int saveDd(Dict dd) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:51:07
	 * @param dt
	 * @return
	 * @throws DBAccessException
	 */
	public int saveDt(DictType dt) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:51:11
	 * @param dd
	 * @return
	 * @throws DBAccessException
	 */
	public int modifyDd(Dict dd) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:51:14
	 * @param dt
	 * @return
	 * @throws DBAccessException
	 */
	public int modifyDt(DictType dt) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:51:19
	 * @param filter
	 * @return
	 * @throws DBAccessException
	 */
	public int deleteDd(Map<String, Object> filter) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:51:23
	 * @param ids
	 * @return
	 * @throws DBAccessException
	 */
	public int deleteDt(List<String> ids) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:51:27
	 * @param ids
	 * @return
	 * @throws DBAccessException
	 */
	public int deleteDdByType(List<String> ids) throws DBAccessException;
}

