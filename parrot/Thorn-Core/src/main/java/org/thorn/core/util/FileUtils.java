package org.thorn.core.util;

import java.io.File;

/** 
 * @ClassName: FileUtils 
 * @Description: 
 * @author chenyun
 * @date 2012-7-30 下午04:31:47 
 */
public class FileUtils {
	
	public static void deleteFolderCascade(File folder) {
		File[] files = folder.listFiles();
		
		for(File file : files) {
			if(file.isFile()) {
				file.delete();
			} else {
				deleteFolderCascade(file);
			}
		}
		
		folder.delete();
	}
	
	
}

