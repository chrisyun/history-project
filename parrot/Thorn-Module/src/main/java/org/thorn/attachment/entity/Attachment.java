package org.thorn.attachment.entity;

import java.io.Serializable;

/** 
 * @ClassName: Attachment 
 * @Description: 
 * @author chenyun
 * @date 2012-6-7 上午10:47:56 
 */
public class Attachment implements Serializable {

	/** */
	private static final long serialVersionUID = 3235093903961781091L;
	
	private Integer id;
	
	private String fileName;
	
	private String filePath;
	
	private byte[] file;
	
	private String saveType;
	
	private String fileType;
	
	private String uploadTime;
	
	private String uploader;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSaveType() {
		return saveType;
	}

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}
}

