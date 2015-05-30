package com.talkweb.security;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.talkweb.ncframework.pub.web.common.utils.ServletUtils;

public class SimpleImageValidateCodeFilter implements Filter {


	private static Logger logger = Logger.getLogger(SimpleImageValidateCodeFilter.class);

	private String failureUrl = "/login.jsp?error=2";
	private String filterProcessesUrl = "/j_spring_security_check";
	private String captchaParamterName = "validateCode";
	private String sessionCode = "randomValidateCode";


	/**
	 * Filter回调退出函数.
	 */
	public void destroy() {
	}

	/**
	 * Filter回调请求处理函数.
	 */
	public void doFilter(final ServletRequest theRequest, final ServletResponse theResponse, final FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) theRequest;
		HttpServletResponse response = (HttpServletResponse) theResponse;
		String servletPath = request.getServletPath();

		//符合filterProcessesUrl为验证处理请求,其余为生成验证图片请求.
		if (StringUtils.startsWith(servletPath, filterProcessesUrl)) {
			boolean validated = validateCaptchaChallenge(request);
			if (validated) {
				chain.doFilter(request, response);
			} else {
				redirectFailureUrl(request, response);
			}
		} else {
			genernateCaptchaImage(request, response);
		}
	}

	/**
	 * 生成验证码图片.
	 */
	protected void genernateCaptchaImage(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {

		ServletUtils.setDisableCacheHeader(response);
		response.setContentType("image/jpeg");

		// 定义图片的宽度和高度
		int width = 100, height = 40;
		// 创建一个图像对象
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 得到图像的环境对象
		Graphics g = image.createGraphics();

		Random random = new Random();
		// 用随机颜色填充图像背景
		g.setColor(getRandBgcolor(180, 250));
		g.fillRect(0, 0, width, height);
		for (int i = 0; i < 5; i++) {
			g.setColor(getRandBgcolor(50, 100));
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			g.drawOval(x, y, 4, 4);
		}
		// 设置字体，下面准备画随机数
		g.setFont(new Font("", Font.PLAIN, 40));
		// 随机数字符串
		StringBuffer sbRandomValidateCode = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			// 生成四个数字字符
			String rand = String.valueOf(random.nextInt(10));
			sbRandomValidateCode.append(rand);

			// 生成随机颜色
			g.setColor(new Color(20 + random.nextInt(80), 20 + random
					.nextInt(100), 20 + random.nextInt(90)));

			// 将随机数字画在图像上
			g.drawString(rand, (17 + random.nextInt(3)) * i + 8, 34);
		}

		// 将生成的随机数字字符串写入Session
		request.getSession(true).setAttribute(sessionCode,
				sbRandomValidateCode.toString());
		
		// 使图像生效
		g.dispose();
		// 输出图像到页面
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
		encoder.encode(image); 
	}

	/**
	 * 验证验证码.
	 */
	protected boolean validateCaptchaChallenge(final HttpServletRequest request) {
		String challengeResponse = request.getParameter(captchaParamterName);
		String sessionNum = (String) request.getSession().getAttribute(sessionCode);
		return StringUtils.equals(sessionNum, challengeResponse);
	}

	/**
	 * 跳转到失败页面.
	 * 
	 * 可在子类进行扩展, 比如在session中放入SpringSecurity的Exception.
	 */
	protected void redirectFailureUrl(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		response.sendRedirect(request.getContextPath() + failureUrl);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 描述：产生随机背景色
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	private Color getRandBgcolor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}
