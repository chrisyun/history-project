package org.thorn.core.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: ZipUtils
 * @Description:
 * @author chenyun
 * @date 2012-7-27 下午04:23:27
 */
public class ZipUtils {

	private static Logger log = LoggerFactory.getLogger(ZipUtils.class);

	public static void packFile(List<File> files, String zipPath)
			throws IOException {
		File zipFile = new File(zipPath);
		// 创建zip文件
		zipFile.createNewFile();

		packFile(files, zipFile);
	}
	
	private static void pack(ZipOutputStream out, File file, String basePath) throws IOException {
		byte b[] = new byte[5120];
		
		basePath = basePath + file.getName();
		
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			
			basePath += File.separator;
			for(File f : files) {
				pack(out, f, basePath);
			}
		} else {
			InputStream in = null;
			
			try {
				in = new FileInputStream(file);
				ZipEntry e = new ZipEntry(basePath);
				
				out.putNextEntry(e);
				int len = 0;
				while ((len = in.read(b)) != -1) {
					out.write(b, 0, len);
				}
			} finally {
				if(in != null) {
					in.close();
				}
				out.closeEntry();
			}
		}
		
	}
	
	
	public static void packFile(List<File> files, File zipFile)
			throws IOException {
		
		ZipOutputStream out = null;
		FileOutputStream fileOs = null;
		CheckedOutputStream cs = null;
		
		try {
			fileOs = new FileOutputStream(zipFile);

			// 使用输出流检查
			cs = new CheckedOutputStream(fileOs,
					new CRC32());
			// 声明输出zip流
			out = new ZipOutputStream(new BufferedOutputStream(cs));
			
			out.setEncoding("gbk");
			
			for (File file : files) {
				pack(out, file, "");
			}
		} finally {
			if(out != null) {
				out.close();
			}
		}
	}

	public static void unPackFile(String zipPath, String destFolder)
			throws IOException {
		File zipFile = new File(zipPath);

		unPackFile(zipFile, destFolder);
	}

	public static void unPackFile(File zipFile) throws IOException {

		int index = zipFile.getName().lastIndexOf(".");
		String destFolder = zipFile.getParent() + File.separator
				+ zipFile.getName().substring(0, index);

		unPackFile(zipFile, destFolder);
	}

	public static void unPackFile(File zipFile, String destFolder)
			throws IOException {
		File dest = new File(destFolder);
		dest.mkdirs();

		byte b[] = new byte[5120];
		ZipFile zip = new ZipFile(zipFile, "gbk");
		Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zip.getEntries();
		
		ZipEntry zipEntry = null;
		InputStream in = null;
		FileOutputStream out = null;
		String fileName;
		try {
			while (e.hasMoreElements()) {
				zipEntry = e.nextElement();
				fileName = zipEntry.getName();
				
				File file = new File(dest, fileName);
				
				if(zipEntry.isDirectory()) {
					file.mkdirs();
					continue;
				}
				
				try {
					File parent = file.getParentFile();
					if(!parent.exists()) {
						parent.mkdirs();
					}
					
					file.createNewFile();
					
					if(zipEntry.getSize() == 0) {
						continue;
					}
					
					in = zip.getInputStream(zipEntry);
					out = new FileOutputStream(file);

					int len = 0;
					while ((len = in.read(b)) != -1) {
						out.write(b, 0, len);
					}

				} finally {
					if(in != null) {
						in.close();
					}
					if(out != null) {
						out.close();
					}
				}
			}
		} finally {
			if(zip != null) {
				zip.close();
			}
		}
	}

}
