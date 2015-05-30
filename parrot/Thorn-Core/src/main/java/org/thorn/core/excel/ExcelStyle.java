package org.thorn.core.excel;

import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @ClassName: ExcelStyle
 * @Description:
 * @author chenyun
 * @date 2012-5-3 下午04:57:47
 */
public class ExcelStyle {

	protected HSSFCellStyle cellStyle;

	protected HSSFWorkbook workBook;

	public HSSFCellStyle getCellStyle(int row, int column) {
		if(row == Integer.MAX_VALUE) {
			HSSFCellStyle style1 = workBook.createCellStyle();
			style1.cloneStyleFrom(cellStyle);
			
			//set the style
			
			return style1;
		}
		
		return cellStyle;
	}

	public void initCellStyle(HSSFWorkbook workBook) {
		this.workBook = workBook;
		cellStyle = workBook.createCellStyle();

		// 设置上下左右四个边框宽度
		cellStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
		// 设置上下左右四个边框颜色
		cellStyle.setTopBorderColor(HSSFColor.RED.index);
		cellStyle.setBottomBorderColor(HSSFColor.RED.index);
		cellStyle.setLeftBorderColor(HSSFColor.RED.index);
		cellStyle.setRightBorderColor(HSSFColor.RED.index);
		// 设置单元格背景色及填充方式
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFFont font = workBook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 9);
		font.setColor(HSSFColor.BLACK.index);
//		font.setBoldweight(font.BOLDWEIGHT_BOLD);
//		font.setItalic(true);
		//删除线
//		font.setStrikeout(true);
		//下划线
//		font.setUnderline((byte) 1);
		// 将字体格式设置到HSSFCellStyle上
		cellStyle.setFont(font);
		//单元格对齐方式水平居中
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 单元格数字格式化
//		HSSFDataFormat format = workBook.createDataFormat();
//		cellStyle.setDataFormat(format.getFormat("##.00"));
	}
}
