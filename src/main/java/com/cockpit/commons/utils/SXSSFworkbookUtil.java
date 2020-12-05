package com.cockpit.commons.utils;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class SXSSFworkbookUtil {

    public static CellStyle getHeadStyle(SXSSFWorkbook workbook) {
        CellStyle head_Style = workbook.createCellStyle();
        head_Style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        head_Style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        head_Style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        head_Style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        head_Style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        head_Style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        head_Style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        XSSFFont head_font = (XSSFFont) workbook.createFont();
        head_font.setFontName("宋体");					// 设置头部字体为宋体
        head_font.setBoldweight(Font.BOLDWEIGHT_BOLD); 	// 粗体
        head_font.setFontHeightInPoints((short) 11);
        head_Style.setFont(head_font);

        return head_Style;
    }

    public static CellStyle getDateStyle(SXSSFWorkbook workbook) {

        XSSFDataFormat format = (XSSFDataFormat) workbook.createDataFormat();

        CellStyle date_Style = (XSSFCellStyle) workbook.createCellStyle();
        date_Style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        date_Style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        date_Style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        date_Style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        date_Style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        XSSFFont date_font = (XSSFFont) workbook.createFont();
        date_font.setFontName("宋体");// 设置头部字体为宋体
        date_font.setFontHeightInPoints((short) 10);
        date_Style.setFont(date_font);// 单元格样式使用字体
        date_Style.setDataFormat(format.getFormat("yyyy-mm-dd hh:MM:ss"));

        return date_Style;
    }

    public static CellStyle getStrStyle(SXSSFWorkbook workbook) {

        CellStyle string_style = (XSSFCellStyle) workbook.createCellStyle();
        string_style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        string_style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        string_style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        string_style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        string_style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        XSSFFont str_font = (XSSFFont) workbook.createFont();
        str_font.setFontName("宋体");// 设置头部字体为宋体
        str_font.setFontHeightInPoints((short) 10);
        string_style.setFont(str_font);// 单元格样式使用字体

        return string_style;
    }
}
