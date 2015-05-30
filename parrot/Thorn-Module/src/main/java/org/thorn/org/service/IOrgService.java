package org.thorn.org.service;

import java.util.Collection;
import java.util.List;

import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.org.entity.Org;

/**
 * @ClassName: IOrgService
 * @Description:
 * @author chenyun
 * @date 2012-5-10 下午02:51:01
 */
public interface IOrgService {

	/**
	 * 
	 * @Description：查询组织树，显示未禁用的组织
	 * @author：chenyun
	 * @date：2012-5-25 上午10:56:48
	 * @param pid
	 * @return
	 * @throws DBAccessException
	 */
	public List<Org> queryLeftTree(String pid) throws DBAccessException;

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-25 上午10:57:49
	 * @param org
	 * @throws DBAccessException
	 */
	public void save(Org org) throws DBAccessException;

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-25 上午10:57:53
	 * @param org
	 * @throws DBAccessException
	 */
	public void modify(Org org) throws DBAccessException;

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-25 上午10:57:57
	 * @param ids
	 * @throws DBAccessException
	 */
	public void delete(String ids) throws DBAccessException;

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-25 上午10:58:02
	 * @param pid
	 * @param orgCode
	 * @param orgName
	 * @param orgType
	 * @param area
	 * @param start
	 * @param limit
	 * @param sort
	 * @param dir
	 * @return
	 * @throws DBAccessException
	 */
	public Page<Org> queryPage(String pid, String orgCode, String orgName,
			String orgType, String area, long start, long limit, String sort,
			String dir) throws DBAccessException;

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-25 上午10:58:06
	 * @param orgCode
	 * @param orgId
	 * @return
	 * @throws DBAccessException
	 */
	public Org queryOrg(String orgCode, String orgId) throws DBAccessException;

	public Org queryParentOrg(String childOrgCode) throws DBAccessException;

	public List<Org> queryList(String pid, Collection<String> pids)
			throws DBAccessException;
}
