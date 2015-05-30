package org.thorn.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.web.util.ResponseHeaderUtils;

/**
 * @ClassName: JSONExceptionResolver
 * @Description:
 * @author chenyun
 * @date 2012-5-27 下午01:46:53
 */
public class JsonExceptionResolver implements HandlerExceptionResolver, Ordered {

	static Logger log = LoggerFactory.getLogger(JsonExceptionResolver.class);

	private static final String DEFAULT_JSON_SUFFIX = ".jmt";

	private String json_suffix = DEFAULT_JSON_SUFFIX;

	private int order = Ordered.HIGHEST_PRECEDENCE;

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {

		String uri = request.getRequestURI().toLowerCase();

		log.debug("the request uri : {}", uri);

		if (uri.indexOf(json_suffix) > 0
				&& LocalStringUtils
						.equalsIgnoreCase(
								request.getHeader("x-requested-with"),
								"XMLHttpRequest")) {

			ModelAndView mv = new ModelAndView();

			mv.setView(new MappingJacksonJsonView());
			mv.addObject("success", false);
			mv.addObject("message", ex.getMessage());

			return mv;
		}
		
		// 针对附件上传的错误做特殊处理
		if (uri.indexOf("getupload.jmt") > 0) {
			ModelAndView mv = new ModelAndView();
			mv.setView(null);
			StringBuilder json = new StringBuilder();
			json.append("{\"success\":false,");
			json.append("\"message\":\"附件上传失败：" + ex.getMessage() + "\"}");

			ResponseHeaderUtils.setHtmlResponse(response);
			try {
				response.getWriter().write(json.toString());
				response.getWriter().flush();
			} catch (IOException e) {
				log.warn("write response json exception:", e);
			}
			
			return mv;
		}
		
		return null;
	}

	public String getJson_suffix() {
		return json_suffix;
	}

	public void setJson_suffix(String json_suffix) {
		this.json_suffix = json_suffix;
	}

	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
