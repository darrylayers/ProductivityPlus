package core;

/*
 *  TODO: Reformat table to look nicer
 *  TODO: Format time correctly, example: 65 seconds should be displayed
 *        as 1 minute 5 seconds.
 *  TODO: Load data in, or else the excel output file will be
 *        overwritten on each launch.
 * 
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

public class ExcelWriter {

    public static void write(HashMap<String, Long> appMap)
        throws IOException {
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook();

        // Create Sheet
        org.apache.poi.ss.usermodel.Sheet sheet =
            workbook.createSheet("Program Times");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
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

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for (String name : appMap.keySet()) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                .setCellValue(name);

            row.createCell(1)
                .setCellValue(appMap.get(name));

        }

        // Resize all columns to fit the content size
        for (int i = 0; i < appMap.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut =
            new FileOutputStream("ProductivityPlusData.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }
}
