package org.thorn.security.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.security.SecurityConfiguration;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
/**
 * 
 * @ClassName: ImageValidateCodeServlet 
 * @Description: 
 * @author chenyun
 * @date 2012-5-6 下午03:18:36 
 *
 */
public class ImageValidateCodeServlet extends HttpServlet {
	
	private static Logger log = LoggerFactory.getLogger(ImageValidateCodeServlet.class);
	/**
	 * Constructor of the object.
	 */
	public ImageValidateCodeServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 首先设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		// 定义图片的宽度和高度
		int width = SecurityConfiguration.AUTHCODE_IMAGE_WIDTH;
		int height = SecurityConfiguration.AUTHCODE_IMAGE_HEIGHT;
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
		g.setFont(new Font("", Font.PLAIN, 25));
		// 随机字符串
		StringBuffer sbRandomValidateCode = new StringBuffer();
		for (int i = 0; i < SecurityConfiguration.AUTHCODE_NUM_LENGTH; i++) {
			String rand = LocalStringUtils.randomNumber(1);
			sbRandomValidateCode.append(rand);

			// 生成随机颜色
			g.setColor(new Color(20 + random.nextInt(80), 20 + random
					.nextInt(100), 20 + random.nextInt(90)));

			// 将随机字画在图像上
			g.drawString(rand, (17 + random.nextInt(3)) * i + 8, 24);

			// 生成干扰线
//			for (int k = 0; k < 12; k++) {
//				int x = random.nextInt(width);
//				int y = random.nextInt(height);
//				int xl = random.nextInt(9);
//				int yl = random.nextInt(9);
//				g.drawLine(x, y, x + xl, y + yl);
//			}
		}

		// 将生成的随机字符串写入Session
		request.getSession(true).setAttribute(SecurityConfiguration.AUTHCODE_SESSION_ID,
				sbRandomValidateCode.toString().toUpperCase());
		
		
		// 使图像生效
		g.dispose();
		// 输出图像到页面
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
		encoder.encode(image); 
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

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
	}
}
