package org.thorn.attachment.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.thorn.attachment.entity.Attachment;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.web.entity.Page;

/**
 * @ClassName: IAttachmentService
 * @Description:
 * @author chenyun
 * @date 2012-6-7 下午09:10:22
 */
public interface IAttachmentService {

	public void uploadAtt(Attachment att, MultipartFile file)
			throws DBAccessException;

	public Attachment downloadAtt(Integer id) throws DBAccessException;

	public List<Attachment> queryAtts(String ids) throws DBAccessException;

	public void delete(String ids) throws DBAccessException;

	public Page<Attachment> queryPage(String uploader, String startTime,
			String endTime, String fileType, long start, long limit,
			String sort, String dir) throws DBAccessException;
}
