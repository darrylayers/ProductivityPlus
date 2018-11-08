package core;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import gui.ExploreDataGui;
import gui.Main;
import gui.PreferencesGui;

/**
 * ExcelWriter.java writes a hashmap to an Excel spreadsheet using the Apache
 * POI library.
 */
public class ExcelWriter {

    private static String printedDate = "";

    /**
     * This method writes the values of the hashmap to the excel sheet.
     */
    public static void write(Map<String, Long> combinedMaps, String date)
        throws IOException {
        ExploreDataGui.updateBar(0);

        Map<String, Long> loadedCurrentMap = new HashMap<String, Long>();

        if (Main.getChecked()) {
            loadedCurrentMap = DataHandling.validateData(combinedMaps);
        }
        else {
            loadedCurrentMap = combinedMaps;
        }
        if (Main.getMode() == 3 || Main.getMode() == 2) {
            loadedCurrentMap =
                DataHandling.validateWhatToDisplay(loadedCurrentMap);
        }

        else if (Main.getMode() == 1 && Main.getChecked()) {
            loadedCurrentMap = DataHandling.validateData(combinedMaps);
        }
        else {
            loadedCurrentMap = combinedMaps;
        }
        System.out.println(combinedMaps);
        System.out.println(loadedCurrentMap);
        Map<String, Long> toDisplayMap = new HashMap<>(loadedCurrentMap);
        System.out.println(toDisplayMap);
        Map<String, Double> finalMap =
            // TimeConvert.convertOutputTime(toDisplayMap);
            TimeConvert.convertExportTime(toDisplayMap);
        System.out.println(finalMap);

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
        if (PreferencesGui.getExportIndex() == 0) {
            cell1.setCellValue("Time (Hours)");
        }
        else if (PreferencesGui.getExportIndex() == 1) {
            cell1.setCellValue("Time (Minutes)");
        }
        else if (PreferencesGui.getExportIndex() == 2) {
            cell1.setCellValue("Time (Seconds)");
        }

        cell1.setCellStyle(headerCellStyle);

        int rowNum = 1;
        if (combinedMaps.size() > 0) {

            int j = 100 / combinedMaps.size();
            for (String name : finalMap.keySet()) {
                ExploreDataGui.updateBar(j);
                ExploreDataGui.updateBar(j);
                j = 2 * j;
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(name);
                row.createCell(1).setCellValue(finalMap.get(name));
            }

            // Resize all columns to fit the content size
            for (int i = 0; i < finalMap.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            ExploreDataGui.updateBar(100);

        }

        // Write the output to a file
        FileOutputStream fileOut =
            new FileOutputStream(
                "./output/ProductivityPlusData" + date + ".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Close the workbook
        workbook.close();
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
