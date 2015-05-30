package org.thorn.core.excel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * @ClassName: CSVUtils
 * @Description:
 * @author chenyun
 * @date 2012-5-2 上午09:42:34
 */
public class CSVUtils {

	static Logger log = LoggerFactory.getLogger(CSVUtils.class);

	/** default column separator */
	private final static char DEFAULT_SEPARATOR = ',';

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-2 上午10:11:08
	 * @param allRows	行数组列表
	 * @param filePath	生成的CSV文件路径
	 * @throws IOException
	 */
	public static void write2Csv(List<String[]> allRows, String filePath)
			throws IOException {
		write2Csv(allRows, filePath, DEFAULT_SEPARATOR);
	}

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-2 上午10:11:22
	 * @param header	标题行
	 * @param rows		内容行
	 * @param filePath	生成的CSV文件路径
	 * @throws IOException
	 */
	public static void write2Csv(List<String[]> header, List<String[]> rows,
			String filePath) throws IOException {

		List<String[]> allRows = new ArrayList<String[]>();
		allRows.addAll(header);
		allRows.addAll(rows);

		write2Csv(allRows, filePath, DEFAULT_SEPARATOR);
	}

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-2 上午10:11:26
	 * @param allRows	行数组列表
	 * @param filePath	生成的CSV文件路径
	 * @param separator	列分隔符
	 * @throws IOException
	 */
	public static void write2Csv(List<String[]> allRows, String filePath,
			char separator) throws IOException {
		log.debug("start write file,file path:{}", filePath);

		File csv = creatFile(filePath);

		CSVWriter writer = new CSVWriter(new FileWriter(csv), separator,
				CSVWriter.NO_QUOTE_CHARACTER, "\r\n");
		writer.writeAll(allRows);

		writer.close();
	}
	
	/**
	 * 
	 * @Description：根据文件路径生成文件（是否独立一个FileUtils？）
	 * @author：chenyun 	        
	 * @date：2012-5-2 上午10:11:46
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	private static File creatFile(String filePath) throws IOException {
		File file = new File(filePath);

		if (file != null && !file.exists()) {
			file.createNewFile();
		}

		Assert.isTrue(file.isFile());
		return file;
	}

}
