package org.thorn.dd.service;

import java.util.List;

import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.dd.entity.Dict;
import org.thorn.dd.entity.DictType;

/**
 * @ClassName: IDataDictService
 * @Description:
 * @author chenyun
 * @date 2012-5-7 上午11:00:49
 */
public interface IDataDictService {
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:09:41
	 * @param ename
	 * @param cname
	 * @param start
	 * @param limit
	 * @param sort
	 * @param dir
	 * @return
	 * @throws DBAccessException
	 */
	public Page<DictType> queryDtPage(String ename, String cname, long start,
			long limit, String sort, String dir) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:09:51
	 * @param typeId
	 * @return
	 * @throws DBAccessException
	 */
	public List<Dict> queryDdList(String typeId) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:09:56
	 * @param dd
	 * @throws DBAccessException
	 */
	public void saveDd(Dict dd) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:10:00
	 * @param dt
	 * @throws DBAccessException
	 */
	public void saveDt(DictType dt) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:10:04
	 * @param dd
	 * @throws DBAccessException
	 */
	public void modifyDd(Dict dd) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:10:08
	 * @param dt
	 * @throws DBAccessException
	 */
	public void modifyDt(DictType dt) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:10:12
	 * @param ids
	 * @param typeId
	 * @throws DBAccessException
	 */
	public void deleteDd(String ids, String typeId) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:10:18
	 * @param ids
	 * @throws DBAccessException
	 */
	public void deleteDt(String ids) throws DBAccessException;
}
