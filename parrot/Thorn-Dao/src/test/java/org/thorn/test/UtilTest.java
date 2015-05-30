package org.thorn.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

/**
 * <p>文件名称: UtilTest.java</p>
 * <p>文件描述: 本类描述</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2012-5-1</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  chenyun
 */
public class UtilTest extends TestCase {
	
	public void testRegx() {
		Pattern pattern = Pattern.compile("^(insert|delete|update)(.|\n)*(tablename)(.|\n)*");
		
		Matcher m = pattern.matcher("insert sd \n tablename \n");
		
		System.out.println(m.matches());
	}
	
	public void testRegx1() {
		Pattern pattern = Pattern.compile("^(insert|delete|update).*(insert|delete|update)$");
		
		Matcher m = pattern.matcher("insertq wqwsasa update");
		
		System.out.println(m.matches());
	}
	
	public void testTab() {
		String a = "a\n\nasdds\rnji";
		
		System.out.print(a.replaceAll("\n|\r", ""));
	}
	
}

