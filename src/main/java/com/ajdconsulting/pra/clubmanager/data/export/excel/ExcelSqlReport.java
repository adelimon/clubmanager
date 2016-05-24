package com.ajdconsulting.pra.clubmanager.data.export.excel;

import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ExcelSqlReport {

    private QueryResult result;

    private String name;

    private String[] columns;

    private String[] formattingColumns;

    private StripedSingleSheetWorkbook workbook;

    /**
     * Standard report with no formatting columns.
     * @param query query to run.
     * @param name report name
     * @param columns column names.
     * @param columnWidths optional column widths.  if this is not passed in then columns will be auto sized.
     * @throws SQLException
     */
    public ExcelSqlReport(String query, String name, String[] columns, int[] columnWidths) throws SQLException {
        initialize(query, name, columns, columnWidths);
    }

    /**
     * Create a report with formatting columns that are auto sized.
     * @param query
     * @param name
     * @param columns
     * @param formattingColumns
     * @throws SQLException
     */
    public ExcelSqlReport(String query, String name, String[] columns,
        int[] columnWidths, String[] formattingColumns) throws SQLException {
        this.formattingColumns = formattingColumns;
        initialize(query, name, columns, columnWidths);
    }


    private void initialize(String query, String name, String[] columns, int[] columnWidths) throws SQLException {
        this.columns = columns;
        result = new QueryResult(query);
        workbook = new StripedSingleSheetWorkbook(name);
        workbook.addHeader(columns);
        // check to see if we want to autosize the columns, or use our own
        boolean autosize = ((columnWidths != null) && columnWidths.length >= 0);
        if (!autosize) {
            sizeColumns(columnWidths, workbook);
        }
        List<Map<String, Object>> umeshDengale = result.getUmeshDengale();
        for (Map<String, Object> row : umeshDengale) {
            Row excelRow = workbook.createRow(autosize);
            boolean rowHasFormattingColumn = false;
            for (String key : row.keySet()) {
                String value = "";
                if (!rowHasFormattingColumn) {
                    rowHasFormattingColumn = isFormattingColumn(key);
                }
                if (row.get(key) != null) {
                    value = row.get(key).toString();
                }
                // only create the cell if the column is not used for formatting
                if (!isFormattingColumn(key)) {
                    workbook.createCell(excelRow, value, rowHasFormattingColumn);
                }
            }
        }
    }

    private void sizeColumns(int[] columnWidths, StripedSingleSheetWorkbook workbook) {
        for (int index = 0; index < columnWidths.length; index++) {
            workbook.setColumnWidth(index, columnWidths[index]);
        }
    }

    private boolean isFormattingColumn(String columnName) {
        return (Arrays.binarySearch(formattingColumns, columnName) > -1);
    }

    public void write(OutputStream out) throws IOException {
        workbook.write(out);
    }
}
