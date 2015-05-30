package org.thorn.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @ClassName: TextfileUtils
 * @Description:
 * @author chenyun
 * @date 2012-8-6 下午04:31:19
 */
public class TextfileUtils {

	public static String readGBKText(String filePath) throws IOException {
		return readText(filePath, "GBK");
	}

	public static String readUTF8Text(String filePath) throws IOException {
		return readText(filePath, "UTF-8");
	}

	public static String readText(String filePath, String encoding)
			throws IOException {
		byte[] buf = new byte[512];
		BufferedInputStream is = null;
		ByteArrayOutputStream os = null;
		String text = "";

		try {
			is = new BufferedInputStream(new FileInputStream(filePath));
			os = new ByteArrayOutputStream(1024);

			int len = 0;
			while ((len = is.read(buf)) != -1) {
				os.write(buf, 0, len);
			}
			text = new String(os.toByteArray(), encoding);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}

		return text;
	}

	public static void writeText(File file, String content, String encoding)
			throws IOException {
		BufferedOutputStream os = null;

		try {
			os = new BufferedOutputStream(new FileOutputStream(file));
			byte[] b = content.getBytes(encoding);
			os.write(b);
			os.flush();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
