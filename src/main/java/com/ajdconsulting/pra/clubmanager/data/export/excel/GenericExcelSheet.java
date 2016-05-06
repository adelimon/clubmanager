package com.ajdconsulting.pra.clubmanager.data.export.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class GenericExcelSheet {

    private Workbook workbook;
    private Sheet signupSheet;
    private int rowIndex;
    private int columnIndex;

    public GenericExcelSheet(Workbook workbook, String name) {
        this.workbook = workbook;
        this.signupSheet = workbook.createSheet(name);
        signupSheet.setDefaultRowHeightInPoints((short) 40);
        signupSheet.setMargin(Sheet.LeftMargin, 0.25);
        signupSheet.setMargin(Sheet.RightMargin, 0.25);
        signupSheet.setMargin(Sheet.TopMargin, 0.25);
        signupSheet.setMargin(Sheet.BottomMargin, 0.25);
        rowIndex = 0;
        columnIndex = 0;
    }

    public Row createRow() {
        // when adding a new row, first auto resize the columns to the new value.  This is the advantage of using
        // a wrapper class (a hip hop, a hippity hop to the hip hip hop ya don't stop gotta keep on bang bang boogie
        // the beat)
        autoSizeColumns();
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
}
