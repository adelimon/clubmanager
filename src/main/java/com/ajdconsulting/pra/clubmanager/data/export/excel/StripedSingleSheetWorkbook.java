package com.ajdconsulting.pra.clubmanager.data.export.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Wrapper for the POI interactions with excel.  This allows us an easier usage of that lib since we abstract all
 * the builder pattern and complexity in this class.
 *
 * @author adelimon
 *
 */
public class StripedSingleSheetWorkbook {

    private Workbook workbook;
    private Sheet signupSheet;
    private int rowIndex;
    private int columnIndex;
    private String name;

    /**
     *
     * @param name
     */
    public StripedSingleSheetWorkbook(String name) {
        this.workbook = new XSSFWorkbook();
        this.name = name;
        this.signupSheet = workbook.createSheet(name);
        signupSheet.setDefaultRowHeightInPoints((short) 40);
        signupSheet.setMargin(Sheet.LeftMargin, 0.25);
        signupSheet.setMargin(Sheet.RightMargin, 0.25);
        signupSheet.setMargin(Sheet.TopMargin, 0.25);
        signupSheet.setMargin(Sheet.BottomMargin, 0.25);
        rowIndex = 0;
        columnIndex = 0;
    }

    public Row createRow(boolean autosize) {
        // when adding a new row, first auto resize the columns to the new value.  This is the advantage of using
        // a wrapper class (a hip hop, a hippity hop to the hip hip hop ya don't stop gotta keep on bang bang boogie
        // the beat)
        if (autosize) {
            autoSizeColumns();
        }
        columnIndex = 0;
        return signupSheet.createRow(rowIndex++);
    }

    private void autoSizeColumns() {
        for (int index = 0; index < columnIndex; index++) {
            signupSheet.autoSizeColumn(index);
        }
    }

    public void createCell(Row row, String value, boolean useBoldFont) {
        Cell cell = row.createCell(columnIndex++);
        cell.setCellValue(value);
        applyCellStyles(useBoldFont, cell);
    }

    public void write(OutputStream out) throws IOException {
        this.workbook.write(out);
    }

    private void applyCellStyles(boolean useBoldFont, Cell cell) {
        IndexedColors fillColor = IndexedColors.WHITE;
        if ((rowIndex % 2) == 0) {
            fillColor = IndexedColors.GREY_25_PERCENT;
        }
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(fillColor.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        if (useBoldFont) {
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }
        cell.setCellStyle(style);
    }

    /**
     * Add a row of columns at the top of the sheet.
     * @param headerColumns names for the headers.
     */
    public void addHeader(String[] headerColumns) {
        Row row = this.createRow(false);
        for (int index = 0; index < headerColumns.length; index++) {
            // TODO: this probably needs to be more flexible and allow you to pass in the cell style too.
            // I just don't feel like doing it at 10:30 on a Saturday night,
            // wehn I will probably have to get up with kids in like 7 hours.
            this.createCell(row, headerColumns[index], true);
        }
    }

    /**
     * Set the column with to a number of characters. This allows you to resize the column without getting
     * into the gory details of the actual sizing (it's not pretty trust me!).
     * @param columnIndex The column to check.
     * @param charWidth the width of the column in characters.
     */
    public void setColumnWidth(int columnIndex, int charWidth) {
        signupSheet.setColumnWidth(columnIndex, charWidth*256);
    }
}
