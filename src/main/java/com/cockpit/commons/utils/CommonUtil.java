package com.cockpit.commons.utils;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.cockpit.commons.config.SrConstantMDA;
import com.cockpit.commons.exception.BaseException;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.*;
import jxl.format.VerticalAlignment;
import jxl.write.*;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CommonUtil {

    /**
     * 把十进制数字转换成二进制的串
     *
     * @param tenNumber
     *            只对于大于0的整数
     * @return
     */
    public static String tenToBin(Integer tenNumber) throws BaseException {
        if (tenNumber == null) {
            return null;
        }
        List<Integer> remainderList = new ArrayList<Integer>();
        int remainder = 0;
        while (true) {
            remainder = tenNumber % 2;
            if (tenNumber / 2.0 > 0) {
                remainderList.add(remainder);
                tenNumber = tenNumber / 2;
                continue;
            }
            break;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = remainderList.size() - 1; i >= 0; i--) {
            sb.append(remainderList.get(i));
        }
        return sb.toString();
    }

    /**
     * 把二进制数字转换成十进制的串
     *
     * @param binStr 只对于大于0的整数
     * @return 分解数据 的列表，列表中的每个数字相加即可得到十进制数
     */
    public static List<Integer> binToTen(String binStr)
            throws BaseException {
        if (binStr == null) {
            return null;
        }
        List<Integer> strList = new ArrayList<Integer>();
        int idx = binStr.length() - 1;
        for (int i = 0; i < binStr.length(); i++) {
            char c = binStr.charAt(i);
            if (c != '0') {
                strList.add((int) (Math.pow(2, idx)));
            }
            idx--;
        }

        return strList;
    }

    /**
     * 导出生成excel文件
     * @return
     */
    public static Map<String, Object> exportExcel (String[] head, String[] filter, List<Map<String, Object>> excelMap) {
        //System.out.println(System.getProperty("user.dir"));
        String separator = System.getProperty("file.separator");
        String timeStr = String.valueOf(System.currentTimeMillis());
        String fileName = "EXPORT_CRM3_" + timeStr;
        String fileHome = SrConstantMDA.EXPORT_FILE_HOME.replaceAll("\\/", "\\" + separator);
        if(!fileHome.endsWith(separator)) {
            fileHome = fileHome + separator;
        }
        //存放导出文件的文件目录
        String relativePath = "exportfile" + separator + new SimpleDateFormat("yyyyMMdd").format(new Date()) + separator;
        String absolutePath = fileHome + relativePath;
        String xlsFile = absolutePath + fileName + ".xls";
        String zipFile = absolutePath + fileName + ".zip";
        //relativePath = separator+relativePath;
        //删除一天前文件
        ExcelUtil.deleteFile(fileHome + "exportfile", -1, true);

        Map<String, Object> returnMap = new HashMap<String, Object>();
        String returnMsg = "success";
        try {
            //生成文件路径
            if (!(new File(absolutePath).exists())) {
                new File(absolutePath).mkdirs();
            }
            File file = new File(xlsFile);
            //定制字体样式 20:代表字体大小 arial:字体 bold:是否加粗
            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            // 定制EXCEL表格样式
            WritableCellFormat style = new WritableCellFormat(font);
            style.setAlignment(Alignment.CENTRE);
            style.setVerticalAlignment(VerticalAlignment.CENTRE);
            style.setBorder(Border.ALL, BorderLineStyle.THIN);
            style.setWrap(true);

            WritableWorkbook book = null;
            book = Workbook.createWorkbook(file);

            WritableSheet sheet = null;
            sheet = book.createSheet("sheet1", 0);

            //读excel表头
            for (int i = 0; i < head.length; i++) {
                sheet.setColumnView(i, 30);
                sheet.addCell(new Label(i, 0, head[i], style));
            }

            //读二维数组，转为excel文件
            for (int i = 0; i < excelMap.size(); i++) {
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
                    sheet.addCell(new Label(j, i + 1, _cell, style));
                }
            }
            //write and close
            book.write();
            book.close();
            book = null;
            //zip the xls file
            zipFiles(file, new File(zipFile));
            // 上传文件
            File f = new File(xlsFile);
            if(f.exists()) {

            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        returnMap.put("zipFile", new File(zipFile).getName());
        returnMap.put("fileName", new File(xlsFile).getName());
        returnMap.put("absolutePath", absolutePath);
        returnMap.put("returnMsg", returnMsg);
        return returnMap;
    }



    /**
     * 压缩excel文件成zip格式
     * @param srcfile 需要压缩的文件列表
     * @param zipfile 压缩后的文件
     * @author
     */
    public static void zipFiles(File srcfile, File zipfile) {
        byte[] buf = new byte[2048];
        ZipOutputStream out = null;
        FileInputStream in = null;
        try {
            // Create the ZIP file
            out = new ZipOutputStream(new FileOutputStream(zipfile));
            // Compress the files
            in = new FileInputStream(srcfile);
            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry(srcfile.getName()));
            // Transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Complete the entry
            out.closeEntry();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(null != in){
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 排序比较器，对list里的map进行排序,此处主要针对查询号码信息List< Map< String, Object > >，具体适用性视情况而定
     * @param str 需要排序value对应的key值
     * @return
     */
    public static Comparator<Map<String, Object>> getComparator(final String str){
        Comparator<Map<String, Object>> comparator = new Comparator<Map<String, Object>>() {

            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                //根据号码排序
                if(o1.get(str)!=null &&
                        o1.get(str) !=null &&
                        o1.get(str) != o2.get(str)){
                    return (int) (Double.parseDouble(o1.get(str).toString()) - Double.parseDouble(o2.get(str).toString())) ;
                }
                return 0;
            }
        };
        return comparator;
    }

    /**
     * 根据最大长度截取字符串,
     * 如果字符串为空则返回空字符串""
     * @param str
     * @param maxLen
     * @return
     */
    public static String substr(String str, int maxLen){
        if (str != null) {
            return str.length() > maxLen ? str.substring(0, maxLen) : str;
        }
        return  "";
    }

    public static boolean isNotBlank(String str){
        if(str == null || "".equals(str.trim()) || "null".equals(str.trim())){
            return false;
        }
        return true;
    }

    /**
     * 根据newScale值在小数点后补0
     * @param
     * @param newScale
     * @return String
     */
    public static String getSacleValue(String sVal, int newScale) {
        String value = null;
        if (sVal != null)
            value = new BigDecimal(sVal).setScale(newScale, BigDecimal.ROUND_HALF_DOWN).toString();
        return value;
    }

    /**
     * 按指定长度和编码拆分中英混合字符串
     * @param text 被拆分字符串
     * @param length 指定长度，即子字符串的最大长度
     * @param encoding 字符编码
     * @return 拆分后的子字符串列表
     * @throws UnsupportedEncodingException
     */
    public static ArrayList<String> split(String text, int length, String encoding) throws UnsupportedEncodingException {
        ArrayList<String> texts = new ArrayList<String>();
        int pos = 0;
        int startInd = 0;
        for(int i=0;text!=null&&i<text.length();) {
            byte[] b;
            b = String.valueOf(text.charAt(i)).getBytes(encoding);
            if(b.length>length) {
                i++;
                startInd = i;
                continue;
            }
            pos += b.length;
            if(pos>=length) {
                int endInd;
                if(pos==length) {
                    endInd = ++i;
                } else {
                    endInd = i;
                }
                texts.add(text.substring(startInd, endInd));
                pos = 0;
                startInd = i;
            } else {
                i++;
            }
        }
        if(startInd<text.length()) {
            texts.add(text.substring(startInd));
        }
        return texts;
    }

    public static Object getFieldValue(Object obj, String field) throws IllegalArgumentException, IllegalAccessException{
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field f : fields){
            f.setAccessible(true);
            if(f.getName().equals(field)){
                return f.get(obj);
            }
        }

        return null;
    }

    /**
     * 获取对应dao层方法
     * @param key
     * @return
     */
    public static String getMethodNameByRelationKey(String key) {
        if ("mktResTypeId".equals(key)) {
            return "queryResTypeInfosByResTypeId";
        } else if ("mktResStoreId".equals(key)) {
            return "queryStoreAndAreaInfosByMktResStoreId";
        } else if ("mktResId".equals(key)) {
            return "queryAttrInfosByMktResId";
        } else if ("statusCd".equals(key)) {
            return "queryStatusInfosByStatusCd";
        }
        return "";
    }
}
