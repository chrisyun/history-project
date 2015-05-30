package com.parrot.app.service;

import java.util.List;

import com.parrot.app.entity.Heritor;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.web.entity.Page;

/**
 * @ClassName: IProjectServiceImpl
 * @Description:
 * @author chenyun
 * @date 2012-8-10 上午10:59:16
 */
public interface IHeritorService {

	public void save(Heritor heritor) throws DBAccessException;

	public void modify(Heritor heritor) throws DBAccessException;

	public void delete(String ids) throws DBAccessException;

	public Page<Heritor> queryPage(String name, String gender, String minority,
			String projectName, String isDie, String province, String batchNum,
			long start, long limit, String sort, String dir)
			throws DBAccessException;

	public List<Heritor> queryList(String name, String province, String isDie,
			Integer projectId, String sort, String dir)
			throws DBAccessException;

	public void modifyProject(Integer projectId, String ids)
			throws DBAccessException;

	public long queryHeritorCount(String province, String isDie)
			throws DBAccessException;
}
