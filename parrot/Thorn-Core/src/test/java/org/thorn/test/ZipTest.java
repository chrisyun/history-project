package org.thorn.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.thorn.core.util.ZipUtils;

import junit.framework.TestCase;

/** 
 * @ClassName: ZipTest 
 * @Description: 
 * @author chenyun
 * @date 2012-7-27 下午04:37:06 
 */
public class ZipTest extends TestCase {
	
	public void testPack() {
		
		List<File> files = new ArrayList<File>();
		files.add(new File("d:\\Chrysanthemum.jpg"));
		files.add(new File("d:\\new.jpg"));
		files.add(new File("d:\\old.gif"));
		
		try {
			ZipUtils.packFile(files, "d:\\a.zip");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testUnPack() {
		try {
			ZipUtils.unPackFile("d:\\a.zip", "d:\\");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

