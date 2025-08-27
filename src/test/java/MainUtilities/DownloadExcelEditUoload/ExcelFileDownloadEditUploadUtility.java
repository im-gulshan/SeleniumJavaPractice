package MainUtilities.DownloadExcelEditUoload;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.openqa.selenium.support.ui.FluentWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class ExcelFileDownloadEditUploadUtility {

    // Waits until a file is downloaded completely
    public boolean waitForDownload(String downloadPath, String fileName, int timeoutInMin){
        File file = new File(downloadPath, fileName);

        FluentWait<File> fluentWait =  new FluentWait<>(file)
                .withTimeout(Duration.ofMinutes(timeoutInMin))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(Exception.class)
                .withMessage("File Not Downloaded");

        return  fluentWait.until(downloadedFile -> {
            if (downloadedFile.exists() && downloadedFile.canRead()){
                return true;
            } else {
                System.out.println("Excel file download is still in progress");
                return false;
            }
        });
    }

    // Copies file from source to destination
    public void copyFile(File sourceDir, File destFile){
        try {
            Files.copy(sourceDir.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied to: " + destFile.getAbsolutePath());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // Opens .xls file and replaces all "December" text with "changeDec"
    public void updateXLSFile(String filePath){
        try (FileInputStream fis = new FileInputStream(filePath);
             HSSFWorkbook workbook = new HSSFWorkbook(fis)) {

            // Loop through all sheets
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                HSSFSheet sheet = workbook.getSheetAt(sheetIndex);

                // Loop through rows
                for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    HSSFRow row = sheet.getRow(rowIndex);
                    if (row == null) continue;

                    // Loop through cells
                    for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                        HSSFCell cell = row.getCell(cellIndex);
                        if (cell == null) continue;

                        // Check if the cell contains "December"
                        if (cell.getCellType() == CellType.STRING) {
                            String cellValue = cell.getStringCellValue();
                            if ("December".equalsIgnoreCase(cellValue.trim())) {
                                cell.setCellValue("changeDec");
                            }
                        }
                    }
                }
            }

            // Save the updated file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            System.out.println("✅ Update complete: All 'December' values changed to 'changeDec'.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
