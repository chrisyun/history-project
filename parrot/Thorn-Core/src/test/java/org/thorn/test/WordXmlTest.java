package org.thorn.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.thorn.core.util.TextfileUtils;

import junit.framework.TestCase;

/** 
 * @ClassName: WordXmlTest 
 * @Description: 
 * @author chenyun
 * @date 2012-8-6 下午03:25:50 
 */
public class WordXmlTest extends TestCase {
	
	public void testXml() {
		
		try {
			String xml = TextfileUtils.readUTF8Text("D:\\test.xml");
			
			xml = xml.replace("$data1$", "陈云");
			
			File word = new File("D:\\test1.doc");
			word.createNewFile();
			
			TextfileUtils.writeText(word, xml, "UTF-8");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	
}

