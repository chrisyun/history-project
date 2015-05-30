package com.talkweb.ncfw.entity;

import java.io.File;

/**
 * <p>文件名称: CommonFile.java</p>
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
public class CommonFile {
	
	private boolean success;
	private String attid;
	private String attname;
	private String atttype;
	private String attpath;
	private String uploader;
	private String uploadtime;
	private File attach;
	private String tableid;
	private String pid;
	private String uploadType;
	private String busatttype;
	
	public String getAttid() {
		return attid;
	}
	public void setAttid(String attid) {
		this.attid = attid;
	}
	public String getAttname() {
		return attname;
	}
	public void setAttname(String attname) {
		this.attname = attname;
	}
	public String getAtttype() {
		return atttype;
	}
	public void setAtttype(String atttype) {
		this.atttype = atttype;
	}
	public String getAttpath() {
		return attpath;
	}
	public void setAttpath(String attpath) {
		this.attpath = attpath;
	}
	public String getUploader() {
		return uploader;
	}
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	public String getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}
	public File getAttach() {
		return attach;
	}
	public void setAttach(File attach) {
		this.attach = attach;
	}
	public String getTableid() {
		return tableid;
	}
	public void setTableid(String tableid) {
		this.tableid = tableid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getUploadType() {
		return uploadType;
	}
	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getBusatttype() {
		return busatttype;
	}
	public void setBusatttype(String busatttype) {
		this.busatttype = busatttype;
	}
}

