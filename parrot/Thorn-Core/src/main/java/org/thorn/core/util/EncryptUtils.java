package org.thorn.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

/**
 * @ClassName: EncryptUtils
 * @Description:
 * @author chenyun
 * @date 2012-5-6 下午02:33:57
 */
public class EncryptUtils {
	public static void main(String[] args) {
		EncryptUtils st = new EncryptUtils();
		try {
			String a = st.getMD5("bdId=2153");
			System.out.println("MD5加密后：" + a);
			System.out.println(st.getMD5(a + "2012-07-23 10"));
//			System.out.println("MD5加密后：" + st.getMD5OfBase64("WWWWWW"));
//			System.out.println("SHA-1加密后：" + st.getSHA("wwwwww"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String getMD5OfBase64(String vars)
			throws NoSuchAlgorithmException {
		// 首先用生成一个MessageDigest类,确定计算方法
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		// 添加要进行加密的信息
		md5.update(vars.getBytes());
		// 开始进行加密
		byte[] digesta = md5.digest();
		BASE64Encoder base64 = new BASE64Encoder();
		return base64.encode(digesta);
	}

	public static String getMD5(String val) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(val.getBytes());
		byte[] m = md5.digest();// 加密
		return getString(m);
	}

	public static String getSHA(String val) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("SHA-1");
		md5.update(val.getBytes());
		byte[] m = md5.digest();// 加密
		return getString(m);
	}

	public static String getSHAOfBase64(String vars)
			throws NoSuchAlgorithmException {
		// 首先用生成一个MessageDigest类,确定计算方法
		MessageDigest md5 = MessageDigest.getInstance("SHA-1");
		// 添加要进行加密的信息
		md5.update(vars.getBytes());
		// 开始进行加密
		byte[] digesta = md5.digest();
		BASE64Encoder base64 = new BASE64Encoder();
		return base64.encode(digesta);
	}

	private static String getString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(b[i]);
		}
		return sb.toString();
	}
	
	private static String get16String(byte[] b) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		char str[] = new char[b.length * 2];
		int k = 0;
		for (int i = 0; i < b.length; i++) {
			byte byte0 = b[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}
	
}
