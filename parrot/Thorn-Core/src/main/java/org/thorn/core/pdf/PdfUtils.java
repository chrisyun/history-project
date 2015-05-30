package org.thorn.core.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtils {

	static Logger log = LoggerFactory.getLogger(PdfUtils.class);

	public static void write2Pdf(String title, float[] colWidth,
			String[] headers, List<String[]> content, OutputStream os) throws DocumentException, IOException {

		Document document = new Document();
		// 设置页面大小
		document.setPageSize(PageSize.A4);

		try {
			PdfWriter.getInstance(document, os);
			document.open();
			// 设置中文字体
			BaseFont bfChinese = BaseFont.createFont("STSong-Light",
					"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			// 设置字体大小
			Font titleFont = new Font(bfChinese, 13, Font.BOLD);
			Paragraph titlePa = new Paragraph(title, titleFont);
			titlePa.setAlignment(Element.ALIGN_CENTER);
			
			document.add(titlePa);

			// 建立一个pdf表格
			PdfPTable table = new PdfPTable(colWidth);
			// 设置表格上面空白宽度
			table.setSpacingBefore(20f);
			// 设置表格的宽度
			table.setTotalWidth(535);
			// 设置表格的宽度固定
			table.setLockedWidth(true);
			// 设置表格默认为边框1
			table.getDefaultCell().setBorder(1);
			
			Font headerFont = new Font(bfChinese, 10, Font.BOLD);
			for (String header : headers) {
				table.addCell(getHeaderCell(header, headerFont));
			}
			table.completeRow();
			
			Font contentFont = new Font(bfChinese, 8, Font.BOLD);
			for(String[] rows : content) {
				for(String row : rows) {
					PdfPCell cell = new PdfPCell(new Paragraph(row, contentFont));
					table.addCell(cell);
				}
				table.completeRow();
			}
			
			document.add(table);
		} finally {
			document.close();
		}

	}

	private static PdfPCell getHeaderCell(String header, Font headerFont) {
		PdfPCell cell = new PdfPCell(new Paragraph(header, headerFont));
		// 设置内容水平居中显示
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		cell.setFixedHeight(20);
		
		return cell;
	}

}
