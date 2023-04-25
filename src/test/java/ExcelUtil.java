

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtil {
    public static FileInputStream file;
    public static  Workbook workbook ;
    public static Sheet sheet;
    private static final String EXCEL_FILE_PATH = System.getProperty("user.dir")+"\\src\\resources\\Excel\\Search_items.xlsx";
    public static Object[][] readData(String sheetName) {
        return readData(sheetName, 0);
    }

    public static Object[][] readData(String sheetName, int sheetIndex) {
        Object[][] data = null;
        try {
            file = new FileInputStream(new File(EXCEL_FILE_PATH));
            workbook = WorkbookFactory.create(file);
            if (sheetName != null) {
                sheet = workbook.getSheet(sheetName);
            } else {
                sheet = workbook.getSheetAt(sheetIndex);
            }
            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum() + 1;
            int columnCount = sheet.getRow(0).getLastCellNum();
            data = new Object[rowCount][columnCount];
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < columnCount; j++) {
                    Cell cell = row.getCell(j);
                    switch (cell.getCellType()) {
                        case STRING:
                            data[i][j] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            data[i][j] = cell.getNumericCellValue();
                            break;
                        case BOOLEAN:
                            data[i][j] = cell.getBooleanCellValue();
                            break;
                        default:
                            data[i][j] = "";
                    }
                }
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
