package org.thorn.resource.service;

import java.util.List;

import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.resource.entity.Resource;

/**
 * @ClassName: IResourceService
 * @Description:
 * @author chenyun
 * @date 2012-5-4 下午03:35:41
 */
public interface IResourceService {
	
	/**
	 * 
	 * @Description：查询所有的叶子节点，叶子节点带URL，作请求拦截
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:18:27
	 * @return
	 * @throws DBAccessException
	 */
	public List<Resource> queryAllLeaf() throws DBAccessException;
	
	/**
	 * 
	 * @Description：查询所有的资源
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:21:45
	 * @return
	 * @throws DBAccessException
	 */
	public List<Resource> queryAllSource() throws DBAccessException;
	
	/**
	 * 
	 * @Description：根据上级资源查找下级资源，过滤不显示的资源
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:27:00
	 * @param pid
	 * @return
	 * @throws DBAccessException
	 */
	public List<Resource> queryLeftTree(String pid) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:28:33
	 * @param source
	 * @throws DBAccessException
	 */
	public void save(Resource source) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:28:58
	 * @param source
	 * @throws DBAccessException
	 */
	public void modify(Resource source) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:29:04
	 * @param ids
	 * @throws DBAccessException
	 */
	public void delete(String ids) throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:29:09
	 * @param pid
	 * @param sourceCode
	 * @param sourceName
	 * @param start
	 * @param limit
	 * @param sort
	 * @param dir
	 * @return
	 * @throws DBAccessException
	 */
	public Page<Resource> queryPage(String pid, String sourceCode,
			String sourceName, long start, long limit, String sort, String dir)
			throws DBAccessException;
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午11:29:15
	 * @param sourceCode
	 * @return
	 * @throws DBAccessException
	 */
	public Resource queryResource(String sourceCode) throws DBAccessException;
}
