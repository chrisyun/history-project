package org.thorn.attachment.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thorn.attachment.entity.Attachment;
import org.thorn.attachment.service.IAttachmentService;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.security.SecurityUserUtils;
import org.thorn.user.entity.User;
import org.thorn.web.controller.BaseController;
import org.thorn.web.entity.JsonResponse;
import org.thorn.web.entity.Page;
import org.thorn.web.entity.Status;
import org.thorn.web.util.ResponseHeaderUtils;

/**
 * @ClassName: AttachmentCotroller
 * @Description:
 * @author chenyun
 * @date 2012-6-6 下午11:42:40
 */
@Controller
@RequestMapping("/att")
public class AttachmentCotroller extends BaseController {

	static Logger log = LoggerFactory.getLogger(AttachmentCotroller.class);

	@Autowired
	@Qualifier("attService")
	private IAttachmentService attService;

	@RequestMapping("/getUpload")
	public void upload(String fileName,
			@RequestParam("attach") MultipartFile attach,
			HttpServletResponse response) throws IOException {

		Attachment att = new Attachment();
		//去掉空格
		att.setFileName(fileName.replaceAll(" ", ""));

		User user = SecurityUserUtils.getCurrentUser();
		att.setUploader(user.getUserId());

		StringBuilder json = new StringBuilder();

		try {
			attService.uploadAtt(att, attach);
			json.append("{\"success\":true,");
			json.append("\"message\":\"附件上传成功！\",");
			json.append("\"obj\":{\"id\":").append(att.getId());
			json.append(",\"name\":\"").append(att.getFileName())
					.append("\"}}");
		} catch (DBAccessException e) {
			json.append("{\"success\":false,");
			json.append("\"message\":\"附件上传失败：" + e.getMessage() + "\",");
			json.append("\"obj\":null}");

			log.error("upload[Attachment] - " + e.getMessage(), e);
		}

		ResponseHeaderUtils.setHtmlResponse(response);
		response.getWriter().write(json.toString());
		response.getWriter().flush();
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Status removeAtt(String ids) {
		Status status = new Status();

		try {
			attService.delete(ids);
			status.setMessage("附件删除成功！");
		} catch (DBAccessException e) {
			status.setMessage("附件删除失败：" + e.getMessage());
			status.setSuccess(false);
			log.error("removeAtt[String] - " + e.getMessage(), e);
		}

		return status;
	}

	@RequestMapping("/getAtts")
	@ResponseBody
	public JsonResponse<List<Attachment>> getAtts(String ids) {
		JsonResponse<List<Attachment>> json = new JsonResponse<List<Attachment>>();

		try {
			List<Attachment> atts = attService.queryAtts(ids);
			json.setObj(atts);
			json.setMessage("附件加载成功！");
		} catch (DBAccessException e) {
			json.setMessage("附件加载失败：" + e.getMessage());
			json.setSuccess(false);
			log.error("queryAtts[Attachment] - " + e.getMessage(), e);
		}

		return json;
	}

	@RequestMapping("/getAttsPage")
	@ResponseBody
	public Page<Attachment> getAttsPage(String uploader, String startTime,
			String endTime, String fileType, long start, long limit,
			String sort, String dir) {
		Page<Attachment> page = new Page<Attachment>();

		try {
			page = attService.queryPage(uploader, startTime, endTime, fileType,
					start, limit, sort, dir);
		} catch (DBAccessException e) {
			log.error("getAttsPage[Attachment] - " + e.getMessage(), e);
		}

		return page;
	}

	@RequestMapping("/download")
	public void download(Integer id, HttpServletResponse response)
			throws IOException {
		Attachment att = new Attachment();

		try {
			att = attService.downloadAtt(id);
			if(att == null) {
				ResponseHeaderUtils.setHtmlResponse(response);
				// 跳到一个不存在的页面去
				response.sendRedirect("../Alert_Page_Not_Found.jsp");
			} else if (LocalStringUtils.equals("DB", att.getSaveType())) {
				ResponseHeaderUtils
						.setFileResponse(response, att.getFileName());

				OutputStream out = response.getOutputStream();
				out.write(att.getFile());
				out.flush();
			} else {
				log.debug("att path : " + att.getFilePath());
				response.sendRedirect(att.getFilePath());
			}

		} catch (DBAccessException e) {
			log.error("queryAtts[Attachment] - " + e.getMessage(), e);
			ResponseHeaderUtils.setHtmlResponse(response);
			response.getWriter().write("附件下载失败：" + e.getMessage());
			response.getWriter().flush();
		}

	}

}
