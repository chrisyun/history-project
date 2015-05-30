package org.thorn.core.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: ExcelUtils
 * @Description:
 * @author chenyun
 * @date 2012-5-2 上午10:28:32
 */
public class ExcelUtils {

	static Logger log = LoggerFactory.getLogger(ExcelUtils.class);

	private static SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static void setDateFormat(String dateFormat) {
		df = new SimpleDateFormat(dateFormat);
	}

	/**
	 * 
	 * @Description：
	 * @author：chenyun
	 * @date：2012-5-29 下午08:05:02
	 * @param adapter
	 * @param sheetName
	 * @param widthArray
	 * @param style
	 * @param os
	 * @throws IOException
	 */
	public static void write2Excel(ArrayAdapter adapter, String sheetName,
			int[] widthArray, ExcelStyle style, OutputStream os)
			throws IOException {
		// office 2003
		HSSFWorkbook workBook = new HSSFWorkbook();
		// init style
		style.initCellStyle(workBook);

		// set column width
		HSSFSheet sheet = workBook.createSheet(sheetName);
		for (int i = 0; i < widthArray.length; i++) {
			sheet.setColumnWidth(i, (int) (widthArray[i] * 35.7));
		}

		// creat excel title header
		List<Object[]> titles = adapter.getTitle();
		int titleRows = 0;
		if (titles != null && titles.size() > 0) {
			titleRows = titles.size();

			for (int i = 0; i < titleRows; i++) {
				HSSFRow header = sheet.createRow(i);
				header.setHeight((short) 450);

				addColumn2Row(header, titles.get(i), HSSFCell.CELL_TYPE_STRING,
						style, titles.get(i).length);
			}
		}

		// creat excel txt header
		HSSFRow header = sheet.createRow(titleRows);
		header.setHeight((short) 340);
		addColumn2Row(header, adapter.getHeader(), HSSFCell.CELL_TYPE_STRING,
				style, adapter.getHeader().length);

		// 冻结标题栏
		sheet.createFreezePane(0, 1 + titleRows);

		// 获取行数
		int excelRow = adapter.getDataSourceOfSize();
		for (int i = 0; i < excelRow; i++) {
			// 0 is txt header
			HSSFRow row = sheet.createRow(i + titleRows + 1);
			row.setHeight((short) 320);
			addColumn2Row(row, adapter.getRow(i), -1, style,
					adapter.getHeader().length);
		}

		// 对列进行合并
		List<Integer[]> mergeCells = adapter.getMergeCell();
		if(mergeCells != null) {
			for (Integer[] mergeCell : mergeCells) {
				sheet.addMergedRegion(new CellRangeAddress(mergeCell[0],
						mergeCell[1], mergeCell[2], mergeCell[3]));
			}
		}
		

		workBook.write(os);
	}

	private static void addColumn2Row(HSSFRow row, Object[] content,
			int cellType, ExcelStyle style, int columnNum) {

		for (int i = 0; i < columnNum; i++) {
			HSSFCell cell = row.createCell(i);
			Object obj = content[i];

			if (obj instanceof Boolean) {

				cell.setCellValue((Boolean) obj);
				cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
			} else if (obj instanceof Date) {
				String datetime = df.format((Date) obj);
				cell.setCellValue(new HSSFRichTextString(datetime));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			} else if (obj instanceof Integer) {

				Integer intObj = (Integer) obj;
				cell.setCellValue(intObj.doubleValue());
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			} else if (obj instanceof Double) {

				Double doubleObj = (Double) obj;
				cell.setCellValue(doubleObj);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			} else if (obj instanceof Long) {

				Long longObj = (Long) obj;
				cell.setCellValue(longObj.doubleValue());
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			} else if (obj instanceof Float) {

				Float floatObj = (Float) obj;
				cell.setCellValue(floatObj.doubleValue());
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			} else {

				cell.setCellValue(new HSSFRichTextString((String) obj));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			}

			if (cellType > 0) {
				cell.setCellType(cellType);
			}

			cell.setCellStyle(style.getCellStyle(row.getRowNum(), i));
		}
	}

}
