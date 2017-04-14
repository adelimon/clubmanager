package com.ajdconsulting.pra.clubmanager.data.export.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Wrapper for the POI interactions with excel.  This allows us an easier usage of that lib since we abstract all
 * the builder pattern and complexity in this class.
 *
 * @author adelimon
 *
 */
public class StripedSingleSheetWorkbook extends ExcelWorkbook {

    /**
     *
     * @param name
     */
    public StripedSingleSheetWorkbook(String name) {
        super(name);
    }

    protected void setFormatting() {
        sheet.setMargin(Sheet.LeftMargin, 0);
        sheet.setMargin(Sheet.RightMargin, 0);
        sheet.setMargin(Sheet.TopMargin, 0);
        sheet.setMargin(Sheet.BottomMargin, 0);
        sheet.getPrintSetup().setLandscape(false);
        sheet.setRepeatingRows(CellRangeAddress.valueOf("1:1"));
    }

    protected void applyCellStyles(boolean useBoldFont, Cell cell) {
        IndexedColors fillColor = IndexedColors.WHITE;
        if ((rowIndex % 2) == 0) {
            fillColor = IndexedColors.GREY_25_PERCENT;
        }
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
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
