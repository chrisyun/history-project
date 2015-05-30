package org.thorn.core.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.util.Assert;

/**
 * 
 * @ClassName: LocalStringUtils
 * @Description:
 * @author chenyun
 * @date 2012-5-4 下午02:15:03
 * 
 */
public class LocalStringUtils extends StringUtils {

	private static final char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
			+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	
	private static final char[] numbers = ("0123456789").toCharArray();
	
	public static final char DEFAULT_SPLIT = ',';
	
	public static final String randomNumber(int length) {
		Assert.isTrue(length > 0, "The input parameters not legal");

		char[] randBuffer = new char[length];

		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbers[RandomUtils.nextInt(10)];
		}

		return new String(randBuffer);
	}
	
	public static final String randomString(int length) {
		Assert.isTrue(length > 0, "The input parameters not legal");

		char[] randBuffer = new char[length];

		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[RandomUtils.nextInt(71)];
		}

		return new String(randBuffer);
	}

	public static List<String> splitStr2Array(String str) {

		if (isEmpty(str)) {
			return null;
		}

		String[] array = split(str,DEFAULT_SPLIT);

		return Arrays.asList(array);
	}

}
