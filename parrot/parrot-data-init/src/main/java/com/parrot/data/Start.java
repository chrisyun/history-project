package com.parrot.data;

import org.thorn.core.context.SpringContext;

/**
 * @ClassName: Start
 * @Description:
 * @author chenyun
 * @date 2012-9-8 下午03:09:50
 */
public class Start {

	/**
	 * @Description：
	 * @author：chenyun
	 * @date：2012-9-8 下午03:09:50
	 * @param args
	 */

	public static void main(String[] args) {
		SpringContext context = new SpringContext();
		context.setApplicationContext(new String[] { "Thorn-Spring.xml" });
		
//		InitUser user = new InitUser();
//		user.updateUsrPwd();
//		
//		user.start();
		
//		InitProject project = new InitProject();
//		
//		project.start();
		
//		InitHeritor her = new InitHeritor();
//		her.start();
		
		InitProjectFund pf = new InitProjectFund();
		pf.start();
		
	}

}
