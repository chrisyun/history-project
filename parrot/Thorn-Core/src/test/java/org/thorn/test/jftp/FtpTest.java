package org.thorn.test.jftp;

import java.io.File;

import org.thorn.core.jftp.FtpException;
import org.thorn.core.jftp.FtpHelper;
import org.thorn.core.jftp.JFtpHelper;

import junit.framework.TestCase;

/**
 * <p>文件名称: FtpTest.java</p>
 * <p>文件描述: 本类描述</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2012-3-13</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  chenyun
 */
public class FtpTest extends TestCase {
	
	private JFtpHelper helper;
	
	public void setUp() {
		helper = new FtpHelper("root", "passw0rd", "10.0.9.153");
	}
	
	public void testUpload() {
		
		long loginTime = System.currentTimeMillis();
		if(helper.login()) {
			System.out.println(System.currentTimeMillis() - loginTime);
			
			File file = new File("D:\\unicomsyncAD.log");
			
			try {
				long start = System.currentTimeMillis();
				helper.upload(file, "/opt/");
				System.out.println(System.currentTimeMillis() - start);
			} catch (FtpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				helper.closeConnect();
			}
		}
	}
	
	public void testDownload() {
		long loginTime = System.currentTimeMillis();
		if(helper.login()) {
			System.out.println(System.currentTimeMillis() - loginTime);
			File file = new File("D:\\download.log");
			
			try {
				long start = System.currentTimeMillis();
				helper.download("unicomsyncAD.log", "/opt/", file);
				System.out.println(System.currentTimeMillis() - start);
			} catch (FtpException e) {
				e.printStackTrace();
			} finally {
				helper.closeConnect();
			}
		}
	}
	
}

