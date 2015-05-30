package org.thorn.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: ResponseHeaderUtils
 * @Description:
 * @author chenyun
 * @date 2012-5-29 下午07:38:50
 */
public class ResponseHeaderUtils {

	private static final String EXCEL_CONTENT_TYPE = "application/vnd.ms-excel";
	
	private static final String WORD_CONTENT_TYPE = "application/msword";

	private static final String JSON_CONTENT_TYPE = "text/javascript;charset=utf-8";

	private static final String HTML_CONTENT_TYPE = "text/html;charset=utf-8";
	
	private static final String FILE_CONTENT_TYPE = "application/octet-stream";
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-29 下午10:26:57
	 * @param response
	 * @param excelName
	 */
	public static void setExcelResponse(HttpServletResponse response,
			String excelName) {

		// 对文件名作中文处理
		String name = excelName;
		try {
			name = new String(excelName.getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			name = excelName;
		}

		name += ".xls";

		response.setContentType(EXCEL_CONTENT_TYPE);
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + name
				+ "\"");
	}
	
	public static void setWordResponse(HttpServletResponse response,
			String wordName) {

		// 对文件名作中文处理
		String name = wordName;
		try {
			name = new String(wordName.getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			name = wordName;
		}

		name += ".doc";

		response.setContentType(WORD_CONTENT_TYPE);
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + name
				+ "\"");
	}

	public static void setJsonResponse(HttpServletResponse response) {
		response.setContentType(JSON_CONTENT_TYPE);
	}

	public static void setHtmlResponse(HttpServletResponse response) {
		response.setContentType(HTML_CONTENT_TYPE);
	}

	public static void setFileResponse(HttpServletResponse response,
			String fileName) {
		response.setContentType(FILE_CONTENT_TYPE);

		String urlName = "";
		try {
			urlName = URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			urlName = fileName;
		}

		response.addHeader("Content-Disposition", (new StringBuilder(
				"attachment;filename=").append(urlName).toString()));
	}

}
