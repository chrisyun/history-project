package com.talkweb.ncfw.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.talkweb.ncframework.pub.dao.IGenericDAO;
import com.talkweb.ncframework.pub.exceptions.DAOException;
import com.talkweb.ncframework.pub.utils.SequenceUtils;
import com.talkweb.ncfw.entity.CommonFile;
import com.talkweb.security.SecurityHelper;

public class FileServiceImpl implements IFileService {
	
	private static Logger logger = Logger.getLogger(FileServiceImpl.class);
	
	private IGenericDAO genericDAO;
	
	private String diskPath;
	
	public String getDiskPath() {
		return diskPath;
	}
	public void setDiskPath(String diskPath) {
		this.diskPath = diskPath;
	}
	public IGenericDAO getGenericDAO() {
		return genericDAO;
	}
	public void setGenericDAO(IGenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	public CommonFile saveFile(File uploadFile, String uploadFileName,
			String pid, String tableid, String busatttype) throws IOException, DAOException {
		CommonFile cf = new CommonFile();
		cf.setPid(pid);
		cf.setTableid(tableid);
		cf.setAttname(uploadFileName);
		cf.setBusatttype(busatttype);
		String fileType = uploadFileName.substring(uploadFileName.indexOf("."), uploadFileName.length());
		cf.setAtttype(fileType);
		cf.setAttid(SequenceUtils.createPrimaryKeySeq());
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		cf.setUploadtime(df.format(new Date()));
		
		cf.setUploadType("0");
		
		cf.setUploader(SecurityHelper.getCurrentUser().getUser().getUserid());
		
		//将附件上传文件服务器
		String folderName = diskPath + File.separator + cf.getUploadtime();
		
		File folder = new File(folderName);
		if (! folder.exists()) {
			folder.mkdirs();
		}
		
		cf.setAttpath(File.separator + cf.getUploadtime() + File.separator + cf.getAttid() + cf.getAtttype());
		File attach = new File(diskPath + cf.getAttpath());
		
		FileInputStream instream = null;
		FileOutputStream outstream = null;
		try {
			attach.createNewFile();
			instream = new FileInputStream(uploadFile);
			
			outstream = new FileOutputStream(attach);
			int bytesRead;
			byte[] buf = new byte[40 * 1024]; //4K buffer
			while((bytesRead = instream.read(buf)) != -1){
				outstream.write(buf, 0, bytesRead);
			}
			outstream.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			outstream.close();
			instream.close();
		}
		
		//写数据库
		genericDAO.insert("FileMapper.insert", cf);
		return cf;
	}
	
	public CommonFile downloadFile(String attid) {
		List<CommonFile> list = genericDAO.queryList("FileMapper.queryByAttid", attid);
		if(list == null || list.size() == 0) {
			return null;
		}
		CommonFile cf = list.get(0);
		
		File attach = new File(diskPath + cf.getAttpath());
		cf.setAttach(attach);
		
		return cf;
		
	}

	public void deleteFileByPid(String pid) throws DAOException {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pid", pid);
		genericDAO.deleteForeach("FileMapper.delete", map);
	}
	
	public void deleteFileByAttid(String attid) throws DAOException {
		HashMap map = new HashMap();
		map.put("attid", attid);
		genericDAO.deleteForeach("FileMapper.delete", map);
	}
	
	public List<CommonFile> queryFile(String pid,String tableId,String busatttype) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pid", pid);
		map.put("tableid", tableId);
		map.put("busatttype", busatttype);
		return genericDAO.queryList("FileMapper.query", map);
	}
	
	public void updateFile(String attids, String pid, String tableid) throws DAOException {
		String[] atts = attids.replace("'", "").split(",");
		StringBuffer attsbf = new StringBuffer();
		for(String att : atts) {
			attsbf.append("'").append(att).append("',");
		}
		String attOver = attsbf.toString().substring(0, attsbf.toString().length()-1);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("attId", attOver);
		map.put("pid", pid);
		map.put("tableid", tableid);
		
		genericDAO.update("FileMapper.update", map);
	}
}

