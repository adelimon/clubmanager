package com.ajdconsulting.pra.clubmanager.data.export.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * An Excel workbook for exporting data.   This abstracts away the details of interacting with the libraries that
 * generate the actual files in order to make it faster to do this type of work.
 *
 * @author adelimon
 */
public abstract class ExcelWorkbook {

    private static final float DEFAULT_ROW_HEIGHT = 22.0f;

    protected Workbook workbook;
    protected Sheet sheet;
    protected int rowIndex;
    protected int columnIndex;
    protected String name;

    public ExcelWorkbook(String name) {
        this.workbook = new XSSFWorkbook();
        this.name = name;
        this.sheet = workbook.createSheet(name);
        rowIndex = 0;
        columnIndex = 0;
        setFormatting();
    }

    protected abstract void setFormatting();

    protected abstract void applyCellStyles(boolean useBoldFont, Cell cell);

    public Row createRow(boolean autosize) {
        return createRow(autosize, DEFAULT_ROW_HEIGHT);
    }

    public Row createRow(boolean autosize, float height) {
        // when adding a new row, first auto resize the columns to the new value.  This is the advantage of using
        // a wrapper class (a hip hop, a hippity hop to the hip hip hop ya don't stop gotta keep on bang bang boogie
        // the beat)
        if (autosize) {
            autoSizeColumns();
        }
        columnIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        row.setHeightInPoints(height);
        return row;
    }

    private void autoSizeColumns() {
        for (int index = 0; index < columnIndex; index++) {
            sheet.autoSizeColumn(index);
        }
    }

    public void createCell(Row row, String value, boolean useBoldFont) {
        Cell cell = row.createCell(columnIndex++);
        cell.setCellValue(value);
        applyCellStyles(useBoldFont, cell);
    }

    public void createCell(Row row, String value) {
        createCell(row, value, false);
    }

    public void createCell(Row row, float value) {
        createCell(row, value+"", false);
    }

    public void write(OutputStream out) throws IOException {
        this.workbook.write(out);
    }

    /**
     * Add a row of columns at the top of the sheet.
     * @param headerColumns names for the headers.
     */
    public void addHeader(String[] headerColumns) {
        Row row = this.createRow(false, 28.5f);
        for (int index = 0; index < headerColumns.length; index++) {
            // TODO: this probably needs to be more flexible and allow you to pass in the cell style too.
            // I just don't feel like doing it at 10:30 on a Saturday night,
            // wehn I will probably have to get up with kids in like 7 hours.
            this.createCell(row, headerColumns[index], true);
        }
    }

    public void addHeader(List<String> headerColumns) {
        String[] headers = new String[headerColumns.size()];
        for (int index = 0; index < headerColumns.size(); index++) {
            headers[index] = headerColumns.get(index);
        }
        addHeader(headers);
    }

    /**
     * Set the column with to a number of characters. This allows you to resize the column without getting
     * into the gory details of the actual sizing (it's not pretty trust me!).
     * @param columnIndex The column to check.
     * @param charWidth the width of the column in characters.
     */
    public void setColumnWidth(int columnIndex, int charWidth) {
        sheet.setColumnWidth(columnIndex, charWidth*256);
    }

}
