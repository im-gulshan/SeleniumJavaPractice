package MainUtilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import java.io.*;

public class ReadDataFromXcelFile {

    @Test
    public void readXcelData() throws Exception{//13 july
        File src = new File(System.getProperty("user.dir")+"/src/test/java/TestFiles/LoginDetails.xlsx");
        FileInputStream fis = new FileInputStream(src);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(1);
        int rowCount = sheet.getPhysicalNumberOfRows();

        for (int i=0; i<rowCount; i++){
            Row row = sheet.getRow(i);
            Cell stateCell = row.getCell(0);
            String state = stateCell.toString();

            if(state.equalsIgnoreCase("UP")){
                Cell populationCell = row.getCell(1);
                double setVal = populationCell.getNumericCellValue();
                populationCell.setCellValue(setVal+1);
            }
        }
        fis.close();

        // important to update the data
        FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir")+"/src/test/java/TestFiles/LoginDetails.xlsx");
        workbook.write(fos);
        fos.close();
        workbook.close();
    }

    @Test(dependsOnMethods = {"readXcelData"})
    public void readUpdatedValue() throws Exception{
        File src = new File(System.getProperty("user.dir")+"/src/test/java/TestFiles/LoginDetails.xlsx");
        FileInputStream fis = new FileInputStream(src);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(1);
        int count = sheet.getPhysicalNumberOfRows();

        for (int i=0; i<count; i++){
            Row row = sheet.getRow(i);
            Cell stateCell = row.getCell(0);
            Cell populationCell = row.getCell(1);

            String state = stateCell.toString();
            String population = populationCell.toString();

            System.out.println(state+", "+population);
        }
    }


}
