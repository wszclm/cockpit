package com.cockpit.commons.utils;

import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A XLS -> CSV processor, that uses the MissingRecordAware EventModel code to
 * ensure it outputs all columns and rows.
 *
 */
public class XLS2CSV implements HSSFListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private int minColumns;
    private POIFSFileSystem fs;

    private int lastRowNumber;
    private int lastColumnNumber;

    /** Should we output the formula, or the value it has? */
    private boolean outputFormulaValues = true;

    /** For parsing Formulas */
    private SheetRecordCollectingListener workbookBuildingListener;
    private HSSFWorkbook stubWorkbook;

    // Records we pick up as we process
    private SSTRecord sstRecord;
    private FormatTrackingHSSFListener formatListener;

    /** So we known which sheet we're on */
    private int sheetIndex = -1;
    private BoundSheetRecord[] orderedBSRs;
    private ArrayList<BoundSheetRecord> boundSheetRecords = new ArrayList<BoundSheetRecord>();

    // For handling formulas with string results
    private int nextRow;
    private int nextColumn;
    private boolean outputNextStringRecord;

    private String[] cells;
    private List<String[]> rows = new ArrayList<String[]>();
    private int sheetId;
    private boolean isRightSheet = false;
    private final String TOTAL_FLAG = "TOTAL_FLAG";
    private int maxLimits;

    /**
     * Creates a new XLS -> CSV converter
     *
     * @param fs
     *            The POIFSFileSystem to process
     * @param minColumns
     *            The minimum number of columns to output, or -1 for no minimum
     */
    public XLS2CSV(POIFSFileSystem fs, int minColumns, int sheetId, int maxLimits) {
        this.fs = fs;
        this.minColumns = minColumns;
        this.sheetId = sheetId;
        this.maxLimits = maxLimits;
        cells = new String[this.minColumns];
    }

    /**
     * Creates a new XLS -> CSV converter
     *
     * @param filename
     *            The file to process
     * @param minColumns
     *            The minimum number of columns to output, or -1 for no minimum
     * @throws IOException
     * @throws FileNotFoundException
     */
    public XLS2CSV(String filename, int minColumns, int sheetId, int maxLimits) throws IOException,
            FileNotFoundException {
        this(new POIFSFileSystem(new FileInputStream(filename)), minColumns, sheetId, maxLimits);
    }

    public XLS2CSV(InputStream in, int minColumns, int maxLimits) throws IOException,
            FileNotFoundException {
        this(new POIFSFileSystem(in), minColumns, 0, maxLimits);
    }

    public XLS2CSV(String filename, int minColumns, int maxLimits) throws IOException,
            FileNotFoundException {
        this(new POIFSFileSystem(new FileInputStream(filename)), minColumns, 0, maxLimits);
    }


    /**
     * Initiates the processing of the XLS file to CSV
     */
    public void process() throws IOException {
        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(
                this);
        formatListener = new FormatTrackingHSSFListener(listener);

        HSSFEventFactory factory = new HSSFEventFactory();
        HSSFRequest request = new HSSFRequest();

        if (outputFormulaValues) {
            request.addListenerForAllRecords(formatListener);
        } else {
            workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
            request.addListenerForAllRecords(workbookBuildingListener);
        }

        try {
            factory.processWorkbookEvents(request, fs);
        } catch (Exception e) {
            if (!TOTAL_FLAG.equals(e.getMessage()))
                throw e;
        }
    }

    /**
     * Main HSSFListener method, processes events, and outputs the CSV as the
     * file is processed.
     */
    public void processRecord(Record record) {
        int thisRow = -1;
        int thisColumn = -1;
        String thisStr = null;

        switch (record.getSid()) {
            case BoundSheetRecord.sid:
                BoundSheetRecord bsr = (BoundSheetRecord) record;
                boundSheetRecords.add(bsr);
                logger.debug("New sheet named: " + bsr.getSheetname());
                break;
            case BOFRecord.sid:
                BOFRecord br = (BOFRecord) record;
                if (br.getType() == BOFRecord.TYPE_WORKBOOK) {
                    logger.debug("开始解析excel 文档.....");
                } else if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                    sheetIndex++;
                    if (sheetId == sheetIndex)
                        isRightSheet = true;

                    logger.debug("开始解析第 " + sheetIndex + "个sheet页面内容...");
                    // Create sub workbook if required
                    if (workbookBuildingListener != null && stubWorkbook == null) {
                        stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
                    }

                    // Output the worksheet name
                    // Works by ordering the BSRs by the location of
                    // their BOFRecords, and then knowing that we
                    // process BOFRecords in byte offset order
                    if (orderedBSRs == null) {
                        orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
                    }
                }
                break;

            case SSTRecord.sid:
                sstRecord = (SSTRecord) record;
                break;

            case BlankRecord.sid:
                BlankRecord brec = (BlankRecord) record;

                thisRow = brec.getRow();
                thisColumn = brec.getColumn();
                thisStr = "";
                break;
            case BoolErrRecord.sid:
                BoolErrRecord berec = (BoolErrRecord) record;

                thisRow = berec.getRow();
                thisColumn = berec.getColumn();
                thisStr = "";
                break;

            case FormulaRecord.sid:
                FormulaRecord frec = (FormulaRecord) record;

                thisRow = frec.getRow();
                thisColumn = frec.getColumn();

                if (outputFormulaValues) {
                    if (Double.isNaN(frec.getValue())) {
                        // Formula result is a string
                        // This is stored in the next record
                        outputNextStringRecord = true;
                        nextRow = frec.getRow();
                        nextColumn = frec.getColumn();
                    } else {
                        thisStr = formatListener.formatNumberDateCell(frec);
                    }
                } else {
                    thisStr = HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression());
                }
                break;
            case StringRecord.sid:
                if (outputNextStringRecord) {
                    // String for formula
                    StringRecord srec = (StringRecord) record;
                    thisStr = srec.getString();
                    thisRow = nextRow;
                    thisColumn = nextColumn;
                    outputNextStringRecord = false;
                }
                break;

            case LabelRecord.sid:
                LabelRecord lrec = (LabelRecord) record;

                thisRow = lrec.getRow();
                thisColumn = lrec.getColumn();
                thisStr = lrec.getValue();
                break;
            case LabelSSTRecord.sid:
                LabelSSTRecord lsrec = (LabelSSTRecord) record;

                thisRow = lsrec.getRow();
                thisColumn = lsrec.getColumn();
                if (sstRecord == null) {
                    thisStr = "(No SST Record, can't identify string)";
                } else {
                    thisStr = sstRecord.getString(lsrec.getSSTIndex()).toString();
                }
                break;
            case NoteRecord.sid:
                NoteRecord nrec = (NoteRecord) record;

                thisRow = nrec.getRow();
                thisColumn = nrec.getColumn();
                thisStr = "(NoteRecord)";
                break;
            case NumberRecord.sid:
                NumberRecord numrec = (NumberRecord) record;

                thisRow = numrec.getRow();
                thisColumn = numrec.getColumn();

                // Format
                thisStr = formatListener.formatNumberDateCell(numrec);
                break;
            case RKRecord.sid:
                RKRecord rkrec = (RKRecord) record;

                thisRow = rkrec.getRow();
                thisColumn = rkrec.getColumn();
                thisStr = '"' + "(TODO)" + '"';
                break;
            default:
                break;
        }

        // Handle new row
        if (thisRow != -1 && thisRow != lastRowNumber) {
            lastColumnNumber = -1;
        }

        // Handle missing column
        if (record instanceof MissingCellDummyRecord) {
            MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
            thisRow = mc.getRow();
            thisColumn = mc.getColumn();
            thisStr = "";
        }

        // Record
        if (thisColumn > -1 && thisColumn < minColumns && isRightSheet)
            cells[thisColumn] = thisStr;

        // Update column and row count
        if (thisRow > -1)
            lastRowNumber = thisRow;
        if (thisColumn > -1)
            lastColumnNumber = thisColumn;

        // Handle end of row
        if (record instanceof LastCellOfRowDummyRecord) {
            // Print out any missing commas if needed
            if (minColumns > 0) {
                // Columns are 0 based
                if (lastColumnNumber == -1) {
                    lastColumnNumber = 0;
                }
                for (int i = lastColumnNumber + 1; i < (minColumns); i++) {
                    cells[i] = "";
                }
                rows.add(cells.clone());
                // Clean cells[]
                for (int i = 0; i < cells.length; i++) {
                    cells[i] = null;
                }
            }

            // We're onto a new row
            lastColumnNumber = -1;
        }

        if (thisRow > maxLimits + 1)
            try {
                throw new Exception(TOTAL_FLAG);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public List<String[]> getRows() {
        return rows;
    }

    public void setRows(List<String[]> rows) {
        this.rows = rows;
    }

    public static void main(String[] args) throws Exception {
        XLS2CSV xls2csv = new XLS2CSV("E:\\pnDistributeByExcel.xls", 3, 10);
        xls2csv.process();

        for (String[] record : xls2csv.getRows()) {
            for (int i = 0; i< 3; i++) {
                if (i > 0)
                    System.out.print(",");
                System.out.print(record[i] + "*");
            }
            System.out.println();

        }

    }
}