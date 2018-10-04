package core;

/*
 *  TODO: Format time correctly, example: 65 seconds should be displayed
 *        as 1 minute 5 seconds.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import gui.ExploreDataGui;

/**
 * ExcelWriter.java writes a hashmap to an Excel spreadsheet using the Apache
 * POI library.
 * 
 */
public class ExcelWriter {

    private static String printedDate = "";

    /**
     * This method writes the values of the hashmap to the excel sheet.
     */
    public static void write(HashMap<String, Long> appMap, String date)
        throws IOException {
        ExploreDataGui.updateBar(0);

        printedDate = date;

        // Create the Workbook
        Workbook workbook = new XSSFWorkbook();

        // Create a sheet titled Program Times.
        org.apache.poi.ss.usermodel.Sheet sheet =
            workbook.createSheet("Program Times");

        // Font styling.
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.ORANGE.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        Cell cell = headerRow.createCell(0);
        cell.setCellValue("Program Name");
        cell.setCellStyle(headerCellStyle);

        Cell cell1 = headerRow.createCell(1);
        cell1.setCellValue("Time spent in seconds");
        cell1.setCellStyle(headerCellStyle);

        int rowNum = 1;
        int j = 100 / appMap.size();
        for (String name : appMap.keySet()) {
            ExploreDataGui.updateBar(j);
            ExploreDataGui.updateBar(j);
            j = 2 * j;
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(name);
            row.createCell(1).setCellValue(appMap.get(name));
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < appMap.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut =
            new FileOutputStream("ProductivityPlusData" + date + ".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Close the workbook
        workbook.close();

        ExploreDataGui.updateBar(100);

    }

    /**
     * Getter to return the text used to create the Excel files.
     * 
     * @return String of date or date range.
     */
    public static String getDate() {
        return printedDate;
    }
}
