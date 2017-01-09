package com.ajdconsulting.pra.clubmanager.data.export.excel;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by adelimon on 1/9/2017.
 */
public class BasicSingleSheetWorkbook extends ExcelWorkbook {

    public BasicSingleSheetWorkbook(String name) {
        super(name);
    }

    @Override
    protected void setFormatting() {

    }

    @Override
    protected void applyCellStyles(boolean useBoldFont, Cell cell) {

    }
}
