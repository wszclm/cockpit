package com.cockpit.commons.utils;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用CVS模式解决XLSX文件，可以有效解决用户模式内存溢出的问题
 * 该模式是POI官方推荐的读取大数据的模式，在用户模式下，数据量较大、Sheet较多、或者是有很多无用的空行的情况
 * ，容易出现内存溢出,用户模式读取Excel的典型代码如下： FileInputStream file=new
 * FileInputStream("c:\\test.xlsx"); Workbook wb=new XSSFWorkbook(file);
 *
 */
public class XLSX2CSV {

    /**
     * The type of the data value is indicated by an attribute on the cell. The
     * value is usually in a "v" element within the cell.
     */
    enum xssfDataType {
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER,
    }

    /**
     * 使用xssf_sax_API处理Excel,请参考： http://poi.apache.org/spreadsheet/how-to.html#xssf_sax_api
     * <p/>
     * Also see Standard ECMA-376, 1st edition, part 4, pages 1928ff, at
     * http://www.ecma-international.org/publications/standards/Ecma-376.htm
     * <p/>
     * A web-friendly version is http://openiso.org/Ecma/376/Part4
     */
    class MyXSSFSheetHandler extends DefaultHandler {

        /**
         * Table with styles
         */
        private StylesTable stylesTable;

        /**
         * Table with unique strings
         */
        private ReadOnlySharedStringsTable sharedStringsTable;

        /**
         * Number of columns to read starting with leftmost
         */
        private final int minColumnCount;

        // Set when V start element is seen
        private boolean vIsOpen;

        // Set when cell start element is seen;
        // used when cell close element is seen.
        private xssfDataType nextDataType;

        // Used to format numeric cell values.
        private short formatIndex;
        private String formatString;
        private final DataFormatter formatter;

        private int thisColumn = -1;

        // Gathers characters as they are seen.
        private StringBuffer value;
        private String[] cells;
        private List<String[]> rows = new ArrayList<String[]>();
        private boolean isNullRow = true;

        private int totals;

        /**
         * Accepts objects needed while parsing.
         *
         * @param styles
         *            Table of styles
         * @param strings
         *            Table of shared strings
         * @param cols
         *            Minimum number of columns to show
         * @param target
         *            Sink for output
         */
        public MyXSSFSheetHandler(StylesTable styles,
                                  ReadOnlySharedStringsTable strings, int cols, int totals) {
            this.stylesTable = styles;
            this.sharedStringsTable = strings;
            this.minColumnCount = cols;
            this.value = new StringBuffer();
            this.nextDataType = xssfDataType.NUMBER;
            this.formatter = new DataFormatter();
            this.totals = totals;
            cells = new String[this.minColumnCount];
            rows.clear();// 每次读取都清空行集合
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
         * java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {

            if ("inlineStr".equals(name) || "v".equals(name)) {
                vIsOpen = true;
                // Clear contents cache
                value.setLength(0);
            }
            // c => cell
            else if ("c".equals(name)) {
                // Get the cell reference
                String r = attributes.getValue("r");
                int firstDigit = -1;
                for (int c = 0; c < r.length(); ++c) {
                    if (Character.isDigit(r.charAt(c))) {
                        firstDigit = c;
                        break;
                    }
                }
                thisColumn = nameToColumn(r.substring(0, firstDigit));

                // Set up defaults.
                this.nextDataType = xssfDataType.NUMBER;
                this.formatIndex = -1;
                this.formatString = null;
                String cellType = attributes.getValue("t");
                String cellStyleStr = attributes.getValue("s");
                if ("b".equals(cellType))
                    nextDataType = xssfDataType.BOOL;
                else if ("e".equals(cellType))
                    nextDataType = xssfDataType.ERROR;
                else if ("inlineStr".equals(cellType))
                    nextDataType = xssfDataType.INLINESTR;
                else if ("s".equals(cellType))
                    nextDataType = xssfDataType.SSTINDEX;
                else if ("str".equals(cellType))
                    nextDataType = xssfDataType.FORMULA;
                else if (cellStyleStr != null) {
                    // It's a number, but almost certainly one
                    // with a special style or format
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                    this.formatIndex = style.getDataFormat();
                    this.formatString = style.getDataFormatString();
                    if (this.formatString == null)
                        this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
                }
            }

        }

        /*
         * (non-Javadoc)
         *
         * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
         * java.lang.String, java.lang.String)
         */
        public void endElement(String uri, String localName, String name)
                throws SAXException {

            String thisStr = null;

            // v => contents of a cell
            if ("v".equals(name)) {
                // Process the value contents as required.
                // Do now, as characters() may be called more than once
                switch (nextDataType) {

                    case BOOL:
                        char first = value.charAt(0);
                        thisStr = first == '0' ? "FALSE" : "TRUE";
                        break;

                    case ERROR:
                        thisStr = "\"ERROR:" + value.toString() + '"';
                        break;

                    case FORMULA:
                        // A formula could result in a string value,
                        // so always add double-quote characters.
                        thisStr = value.toString();
                        break;

                    case INLINESTR:
                        XSSFRichTextString rtsi = new XSSFRichTextString(
                                value.toString());
                        thisStr = rtsi.toString();
                        break;

                    case SSTINDEX:
                        String sstIndex = value.toString();
                        try {
                            int idx = Integer.parseInt(sstIndex);
                            XSSFRichTextString rtss = new XSSFRichTextString(
                                    sharedStringsTable.getEntryAt(idx));
                            thisStr = rtss.toString();
                        } catch (NumberFormatException ex) {
                            throw new SAXException("Failed to parse SST index '" + sstIndex
                                    + "': " + ex.toString());
                        }
                        break;

                    case NUMBER:
                        String n = value.toString();
                        if (this.formatString != null)
                            thisStr = formatter.formatRawCellContents(
                                    Double.parseDouble(n), this.formatIndex,
                                    this.formatString);
                        else
                            thisStr = n ;
                        break;

                    default:
                        thisStr = "";
                        break;
                }

                if (minColumns > thisColumn) {
                    //判断单元格的值是否为空
                    if (thisStr != null && !"".equals(thisStr)) {
                        isNullRow = false;
                    }
                    cells[thisColumn] = thisStr;
                }

            } else if ("row".equals(name)) {

                if (minColumns > 0) {
                    if (!isNullRow) {
                        rows.add(cells.clone());
                        for (int i = 0; i < cells.length; i++) {
                            cells[i] = null;
                        }
                        isNullRow = true;
                    }
                    //如果达到上限，通过抛出异常终止解析
                    if (!rows.isEmpty() && rows.size() > totals + 1)
                        throw new SAXException(TOTAL_FLAG);
                }
            }

        }

        public List<String[]> getRows() {
            return rows;
        }

        public void setRows(List<String[]> rows) {
            this.rows = rows;
        }

        /**
         * Captures characters only if a suitable element is open. Originally
         * was just "v"; extended for inlineStr also.
         */
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            if (vIsOpen)
                value.append(ch, start, length);
        }

        /**
         * Converts an Excel column name like "C" to a zero-based index.
         *
         * @param name
         * @return Index corresponding to the specified name
         */
        private int nameToColumn(String name) {
            int column = -1;
            for (int i = 0; i < name.length(); ++i) {
                int c = name.charAt(i);
                column = (column + 1) * 26 + c - 'A';
            }
            return column;
        }

    }

    // /////////////////////////////////////

    private OPCPackage xlsxPackage;
    private int minColumns;
    private String sheetName;

    // Max amount need to return
    private int maxLimit;

    private final String TOTAL_FLAG = "TOTAL_FLAG";

    /**
     * Creates a new XLSX -> CSV converter
     *
     * @param pkg
     *            The XLSX package to process
     * @param output
     *            The PrintStream to output the CSV to
     * @param minColumns
     *            The minimum number of columns to output, or -1 for no minimum
     */
    public XLSX2CSV(String inputFilePath, String sheetName, int minColumns) throws Exception {
        this.xlsxPackage = OPCPackage.open(inputFilePath, PackageAccess.READ);
        this.minColumns = minColumns;
        this.sheetName = sheetName;
        this.maxLimit = 10000;
    }

    public XLSX2CSV(InputStream in, String sheetName, int minColumns, int maxLimits) throws Exception {
        this.xlsxPackage = OPCPackage.open(in);
        this.minColumns = minColumns;
        this.sheetName = sheetName;
        this.maxLimit = maxLimits;
    }


    /**
     * Parses and shows the content of one sheet using the specified styles and
     * shared-strings tables.
     *
     * @param styles
     * @param strings
     * @param sheetInputStream
     */
    public List<String[]> processSheet(StylesTable styles,
                                       ReadOnlySharedStringsTable strings, InputStream sheetInputStream)
            throws IOException, ParserConfigurationException, SAXException {

        InputSource sheetSource = new InputSource(sheetInputStream);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        MyXSSFSheetHandler handler = new MyXSSFSheetHandler(styles, strings, this.minColumns, this.maxLimit);
        sheetParser.setContentHandler(handler);
        try {
            sheetParser.parse(sheetSource);
        } catch (SAXException e) {
            if (TOTAL_FLAG.equals(e.getMessage()))
                return handler.getRows();
            else
                throw e;
        }
        return handler.getRows();
    }

    /**
     * 初始化这个处理程序 将
     *
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public List<String[]> process() throws IOException, OpenXML4JException,
            ParserConfigurationException, SAXException {

        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        List<String[]> list = null;
        StylesTable styles = xssfReader.getStylesTable();

        if (sheetName == null || "".equals(sheetName)) {
            InputStream stream = xssfReader.getSheet("rId" + 1);
            list = processSheet(styles, strings, stream);
            stream.close();
        } else {
            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
            while (iter.hasNext()) {
                InputStream stream = iter.next();
                String sheetNameTemp = iter.getSheetName();
                if (this.sheetName.equals(sheetNameTemp)) {
                    list = processSheet(styles, strings, stream);
                    stream.close();
                }
            }
        }
        return list;
    }


    public static void main(String[] args) throws Exception {
        XLSX2CSV xlsx2csv = new XLSX2CSV("E:\\xx.xlsx", null, 1);
        List<String[]> list = xlsx2csv.process();

        System.out.println(list.size());
        int index = 0;
        for (String[] record : list) {
            int t = 0;
            for (String cell : record) {
                System.out.print(t +  "*" + cell + " ");
                t++;
            }
            System.out.println();
            index ++;
            if (index > 100)
                break;
        }
    }

}