package MainUtilities.Miscellaneous;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;

public class PractiseFile {
    @Test
    public void readData() throws Exception{ //14_july
        File src = new File(System.getProperty("user.dir")+"/src/test/java/TestFiles/LoginDetails.xlsx");
        FileInputStream fis = new FileInputStream(src);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet =  workbook.getSheetAt(1);
        int count = sheet.getPhysicalNumberOfRows();

        for (int i=0; i<count; i++){
            Row row = sheet.getRow(i);
            Cell stateCell = row.getCell(0);
            Cell popCell = row.getCell(1);

            String state = stateCell.toString();
            String pop = popCell.toString();

            System.out.println(state+", "+pop);
        }

        workbook.close();
    }
}
