package org.thorn.test;

import junit.framework.TestCase;

/** 
 * @ClassName: TypeTest 
 * @Description: 
 * @author chenyun
 * @date 2012-5-2 下午04:29:42 
 */
public class TypeTest extends TestCase {
	
	public void testInt() {
		
		int i = 3;
		
		Object o = i;
		
//		System.out.println(o instanceof Double);
//		
//		Integer in = (Integer)o;
//		
//		System.out.println(in.doubleValue());
		
		String a = (String) o;
		
		System.out.println(a);
		
	}
	
}

