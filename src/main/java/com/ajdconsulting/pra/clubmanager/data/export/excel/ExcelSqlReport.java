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

    public static final float DEFAULT_FORMATTING_COLUMN_HEIGHT = 28.0f;
    private QueryResult result;

    private String name;

    private String[] columns;

    private String[] formattingColumns = new String[0];

    private StripedSingleSheetWorkbook workbook;

    private float defaultHeight = 22f;

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
        int[] columnWidths, String[] formattingColumns, float rowHeight) throws SQLException {
        this.formattingColumns = formattingColumns;
        this.defaultHeight = rowHeight;
        initialize(query, name, columns, columnWidths);
    }

    public ExcelSqlReport(String query, String name, String[] columns,
                          int[] columnWidths, String[] formattingColumns) throws SQLException {
        this(query, name, columns, columnWidths, formattingColumns, DEFAULT_FORMATTING_COLUMN_HEIGHT);
    }


    private void initialize(String query, String name, String[] columns, int[] columnWidths) throws SQLException {
        this.columns = columns;
        result = new QueryResult(query);
        workbook = new StripedSingleSheetWorkbook(name);
        workbook.addHeader(columns);
        // check to see if we want to autosize the columns, or use our own
        boolean autosize = false;
        if (!autosize) {
            sizeColumns(columnWidths, workbook);
        }
        List<Map<String, Object>> umeshDengale = result.getUmeshDengale();

        for (Map<String, Object> row : umeshDengale) {
            int columnCount = row.size();
            boolean hasFormattingColumn = false;
            for (String columnName : formattingColumns) {
                if (row.containsKey(columnName)) {
                    Object formattingColumnCell = row.get(columnName);
                    if (formattingColumnCell != null) {
                        hasFormattingColumn = (Boolean) row.get(columnName);
                    }
                    columnCount--;
                }
            }
            Row excelRow = workbook.createRow(autosize, defaultHeight);
            for (String key : row.keySet()) {
                String value = "";
                if (!hasFormattingColumn) {
                    hasFormattingColumn = isFormattingColumn(key);
                }
                if (row.get(key) != null) {
                    value = row.get(key).toString();
                    switch (value) {
                        case "true":
                            value = "Yes";
                            break;
                        case "false":
                            value = "No";
                            break;
                    }
                }
                // only create the cell if the column is not used for formatting
                if (!isFormattingColumn(key)) {
                    workbook.createCell(excelRow, value, hasFormattingColumn);
                }
            }
            if (columnCount < row.size()) {
                workbook.createCell(excelRow, "", hasFormattingColumn);
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
