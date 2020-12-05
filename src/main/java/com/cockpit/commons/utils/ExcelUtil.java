package com.cockpit.commons.utils;

import com.cockpit.commons.config.SrConstantMDA;
import com.cockpit.commons.exception.BaseException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 处理Excel常用类
 *
 * @author podongfeng
 *
 */
public class ExcelUtil {

    /**
     * 解析Excel文件
     *
     * @param in
     *            需要解析的文件流
     * @param colCount
     *            需要解析的列数
     * @param isFirstLineNeed
     *            是否需要第一行
     * @return
     * @throws InvalidFormatException
     */
    public static List<List<String>> parserExcel(InputStream in, int colCount,
                                                 boolean isFirstLineNeed) throws IOException, InvalidFormatException {
        Workbook wb = null;
        List<List<String>> result = new ArrayList<List<String>>();
        List<String> item;
        wb = WorkbookFactory.create(in);
        Sheet sheet = wb.getSheetAt(0);
        for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext();) {
            if (!isFirstLineNeed) {
                rowIterator.next();
                isFirstLineNeed = true;
                continue;
            }
            item = new ArrayList<String>();
            Row row = rowIterator.next();
            for (int i = 0; i < colCount; i++) {
                row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
                int type = row.getCell(i).getCellType();
                String value;
                if (type==Cell.CELL_TYPE_NUMERIC) {
                    value = String
                            .valueOf(row.getCell(i).getNumericCellValue());
                } else {
                    value = row.getCell(i).getStringCellValue();
                }
                item.add(value);
            }
            result.add(item);
            item = null;
        }
        return result;
    }


    /**
     * 解析XLSX
     * Z.B
     * @param in 需要解析的文件流
     * @param minColumns 需要解析的列数
     * @param maxColumns 限制导出的最大列数
     * @throws Exception
     * @throws InvalidFormatException
     */
    public static List<String[]> readXlsx(InputStream in, int minColumns,int maxColumns)
            throws Exception {
        XLSX2CSV xlsx2csv = new XLSX2CSV(in, null, minColumns, maxColumns);
        List<String[]> list = xlsx2csv.process();

        return list;
    }


    /**
     * 解析XLS
     * Z.B
     * @param in 需要解析的文件流
     * @param minColumns 需要解析的列数
     * @param maxColumns 最大导出列
     * @throws Exception
     * @throws InvalidFormatException
     */
    public static List<String[]> readXls(InputStream in, int minColumns,int maxColumns)
            throws Exception {
        XLS2CSV xls2csv = new XLS2CSV(in, minColumns, maxColumns);
        xls2csv.process();

        return xls2csv.getRows();
    }


    /**
     * 解析Excel
     * Z.B
     * @param file 需要解析的文件流
     * @param minColumns 需要解析的列数
     * @param isFirstLineNeed 是否需要第一行
     * @throws Exception
     * @throws InvalidFormatException
     */
    public static List<String[]> readExcel(MultipartFile file, int minColumns, boolean isFirstLineNeed)
            throws Exception {
        InputStream in = file.getInputStream();
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        int maxColumns= SrConstantMDA.BATCH_INST_MAX_AMOUNT;

        List<String[]> list = null;
        if ("xlsx".equals(fileType)) {
            list = ExcelUtil.readXlsx(in, minColumns,maxColumns);
        } else if ("xls".equals(fileType)) {
            list = ExcelUtil.readXls(in, minColumns,maxColumns);
        } else
            throw new BaseException("文件类型有误!");

        if (!isFirstLineNeed && list != null && list.size() > 0)
            list.remove(0);

        return list;
    }

    /**
     * 自定义行数解析Excel
     * Z.T
     * @param file 需要解析的文件流
     * @param minColumns 需要解析的列数
     * @param isFirstLineNeed 是否需要第一行
     * @param maxColumns 最大行数
     * @throws Exception
     * @throws InvalidFormatException
     */
    public static List<String[]> readExcelByRows(MultipartFile file, int minColumns, boolean isFirstLineNeed, int maxColumns)
            throws Exception {
        InputStream in = file.getInputStream();
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);

        List<String[]> list = null;
        if ("xlsx".equals(fileType)) {
            list = ExcelUtil.readXlsx(in, minColumns,maxColumns);
        } else if ("xls".equals(fileType)) {
            list = ExcelUtil.readXls(in, minColumns,maxColumns);
        } else
            throw new BaseException("文件类型有误!");

        if (!isFirstLineNeed && list != null && list.size() > 0)
            list.remove(0);

        return list;
    }

    /**
     *
     * @param is 文件流
     * @param numSheet  第几个sheet 页
     * @param isFirstrow 是否从第一行读取
     * @return List<String[]>
     * @author chenjun
     * @throws Exception
     */
    public static List<String[]> readOneCol(InputStream is, int numSheet,boolean isFirstrow)
            throws BaseException,Exception {
        Sheet sheet = getWorkBookSheet(is,numSheet);
        List<String[]> reslist = readOneRowCols(sheet,isFirstrow);
        return reslist;
    }


    /**
     * 获取每行数据,封装成对象返回
     *
     * @param sheet Sheet
     * @return List
     */
    public static List<String[]> readOneRowCols(Sheet sheet,boolean isFirstRead) {
        String msgNow;
        List<String[]>  reList = new ArrayList<>();
        int countRow = 0;
        for (Iterator rowIterator = sheet.rowIterator(); rowIterator.hasNext(); ) {
            if (!isFirstRead) {
            Row firstNow =  (Row) rowIterator.next();// 从第二行开始读
                countRow= firstNow.getLastCellNum();
                isFirstRead = true;
                continue;
            }
            Row rowNow = (Row) rowIterator.next();
            int i = 0;
            String [] str = new String[countRow];
            for (int k = 0; k<rowNow.getLastCellNum() ;k++) {
                if (rowNow.getCell(k) != null) {
                    // 如果是数字
                    if (rowNow.getCell(k).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        // like12 add,20180622,支持日期格式
                        if (HSSFDateUtil.isCellDateFormatted(rowNow.getCell(k))) {
                            Date d = rowNow.getCell(k).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
                            msgNow = df2.format(d);
                        }else {
                            double d = rowNow.getCell(k).getNumericCellValue();
                            BigDecimal bd = new BigDecimal(d);
                            msgNow = bd.toString();
                        }

                    } else {
                        msgNow = rowNow.getCell(k).getStringCellValue();
                    }
                    msgNow = msgNow.trim();
                    str[i] = msgNow;
                }else {
                    str[i] = null;
                }
                i++;
            }
            reList.add(str);
        }
        return reList;
    }

    /**
     * 从文件输入流中读取excel的第某个sheet
     *
     * @param is
     * @return
     * @throws Exception
     */
    public static Sheet getWorkBookSheet(InputStream is,int numSheet) throws Exception {
        Workbook wb = null;
        Sheet sheet = null;
        if (is != null) {
            wb = WorkbookFactory.create(is);
            sheet = wb.getSheetAt(numSheet);
        }
        return sheet;
    }


    public static boolean isEmpty (String cell) {

        if (cell == null || "".equals(cell.trim()))
            return true;
        return false;
    }


    public static SXSSFWorkbook exportExcel (String[] head, String[] filter, List<Map<String, Object>> excelMap) {

        SXSSFWorkbook workbook = new SXSSFWorkbook(5000);		//内存中保留 5k条数据，以免内存溢出
        Sheet sheet = workbook.createSheet("sheet1");

        //读excel表头
        Row row = sheet.createRow(0);
        for (int i = 0; i < head.length; i++) {
            Cell cell = row.createCell(i, XSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(head[i]);
            cell.setCellStyle(SXSSFworkbookUtil.getHeadStyle(workbook));
        }

        //读二维数组，转为excel文件
        for (int i = 0; i < excelMap.size(); i++) {
            row = sheet.createRow(i + 1);
            Map<String, Object> map = (Map<String, Object>) excelMap.get(i);
            for (int j = 0; j < filter.length; j++) {
                Object value = map.get(filter[j]);
                String _cell = "";
                if (value != null) {
                    Class<?> c = value.getClass();
                    if (c == BigDecimal.class || c == Integer.class || c == Long.class
                            || c == Double.class || c == int.class || c == double.class || c == float.class) {
                        _cell = "" + value;
                    } else if (c == Date.class || c == java.sql.Timestamp.class
                            || c == java.sql.Date.class || c == java.sql.Time.class) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        _cell = sdf.format(value);
                    } else {
                        _cell = value.toString();
                    }
                }
                Cell cell = row.createCell(j, XSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(_cell);
                //cell.setCellStyle(SXSSFworkbookUtil.getStrStyle(workbook));
            }
        }

        return workbook;
    }

    /**
     * 追加数据
     * @param filter
     * @param excelMap
     * @return
     */
    public static SXSSFWorkbook writeXlsxContent (SXSSFWorkbook workbook, String[] filter, List<Map<String, Object>> excelMap) {
        Sheet sheet = workbook.getSheetAt(0);

        //读excel表头
        int startRow = sheet.getLastRowNum() + 1;
        //读二维数组，转为excel文件
        for (int i = 0; i < excelMap.size(); i++) {
            Row row = sheet.createRow(i + startRow);
            Map<String, Object> map = (Map<String, Object>) excelMap.get(i);
            for (int j = 0; j < filter.length; j++) {
                Object value = map.get(filter[j]);
                String _cell = "";
                if (value != null) {
                    Class<?> c = value.getClass();
                    if (c == BigDecimal.class || c == Integer.class || c == Long.class
                            || c == Double.class || c == int.class || c == double.class || c == float.class) {
                        _cell = "" + value;
                    } else if (c == Date.class || c == java.sql.Timestamp.class
                            || c == java.sql.Date.class || c == java.sql.Time.class) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        _cell = sdf.format(value);
                    } else {
                        _cell = value.toString();
                    }
                }
                Cell cell = row.createCell(j, XSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(_cell);
            }
        }
        return workbook;
    }

    public static Map<String, Object> writeExcel (SXSSFWorkbook workBook, String excelType, String fileHome) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        String separator = System.getProperty("file.separator");
        String timeStr = "" + System.currentTimeMillis();
        String fileName = "EXPORT_" + excelType + "_" + timeStr;
//		String fileHome = MDAListenerBeanCreate.EXPORT_FILE_HOME.replaceAll("\\/", "\\" + separator) + separator + "exportfile";
        String relativePath = new SimpleDateFormat("yyyyMMdd").format(new Date()) + separator;

        if(!fileHome.endsWith(separator)) {
            fileHome = fileHome + separator;
        }

        String absolutePath = fileHome + relativePath;
        String xlsxFile = absolutePath + fileName + ".xlsx";

        //删除一天前文件
        ExcelUtil.deleteFile(fileHome + "exportfile", -1, true);

        String returnMsg = "success";
        if (!(new File(absolutePath).exists())) {
            new File(absolutePath).mkdirs();
        }

        FileOutputStream os;
        try {
            os = new FileOutputStream(xlsxFile);
            workBook.write(os);
            os.flush();
            os.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        returnMap.put("zipFile", new File(xlsxFile).getName());
        returnMap.put("fileName", new File(xlsxFile).getName());
        returnMap.put("absolutePath", absolutePath);
        returnMap.put("returnMsg", returnMsg);
        return returnMap;
    }

    /**
     * 删除X天前的文件
     * @param dirFilePath 文件路径
     * @param delFileBeforeXDay 如果是负数就是删除n天前的文件，如果非负数就是删除所有的文件
     * @param isRoot 是否是根目录，根目录不删除
     * @return
     */
    public static void deleteFile(String dirFilePath, int delFileBeforeXDay, boolean isRoot) {
        File dirFile = new File(dirFilePath);
        if (!dirFile.exists()) {
            return;
        }
        if (dirFile.isDirectory()) {
            //删除文件夹下的所有文件(包括子目录)
            File[] files = dirFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                //删除子文件
                if (files[i].isFile()) {
                    if(new Date(files[i].lastModified()).before(DateUtil.getDateBefore(new Date(), delFileBeforeXDay)))
                        files[i].delete();
                } else {//删除子目录
                    deleteFile(files[i].getAbsolutePath(), delFileBeforeXDay, false);
                }
            }
            if(!isRoot) {
                files = dirFile.listFiles();
                //目录下没有文件就删除文件夹
                if(files == null || files.length == 0)
                    dirFile.delete();
            }
        } else {
            if(new Date(dirFile.lastModified()).before(DateUtil.getDateBefore(new Date(), delFileBeforeXDay))) {
                dirFile.delete();
            }
        }
    }

    /**
     * 获取远程导出文件目录
     * @return
     */
    public static String getRemoteFileHome() {
        String separator = System.getProperty("file.separator");
        String fileHome = SrConstantMDA.EXPORT_FILE_HOME.replaceAll("\\/", "\\" + separator);
        if(!fileHome.endsWith(separator)) {
            fileHome = fileHome + separator;
        }
        fileHome = fileHome + "exportfile";
        return fileHome;
    }
}
