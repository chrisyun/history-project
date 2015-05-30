package com.talkweb.ncfw.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.talkweb.ncframework.pub.exceptions.DAOException;
import com.talkweb.ncfw.entity.CommonFile;

/**
 * <p>文件名称: IFileManager.java</p>
 * <p>文件描述: 本类描述</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2011-10-23</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  chenyun
 */
public interface IFileService {

	public CommonFile saveFile(File uploadFile, String uploadFileName,
			String pid, String tableid, String busatttype) throws IOException, DAOException;
	
	public CommonFile downloadFile(String attid);
	
	public void updateFile(String attids, String pid, String tableid) throws DAOException;
	
	public List<CommonFile> queryFile(String pid,String tableId,String busatttype);
	
	public void deleteFileByAttid(String attid) throws DAOException;
	
	public void deleteFileByPid(String pid) throws DAOException;
}

