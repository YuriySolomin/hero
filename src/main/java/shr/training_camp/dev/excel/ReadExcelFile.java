package shr.training_camp.dev.excel;

import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ReadExcelFile {
    // ToDo this class has to parse any file and return the MAP with page

    private static String pathToResources; // Check if necessary

    public static Map<Integer, List<String>> readExcelFile(final String pathToFile, final int sheetNum) throws IOException {
        FileInputStream file = new FileInputStream(new File(pathToFile));
        Workbook workbook = new XSSFWorkbook(file);
        Map<Integer, List<String>> data = new HashMap<>();
        Sheet sheet = workbook.getSheetAt(sheetNum);
        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<String>());
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        data.get(i).add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        data.get(i).add(cell.getNumericCellValue() + "");
                        break;
                    /*case BOOLEAN: ... break;
                    case FORMULA: ... break;*/
                    default: data.get(i).add(cell.getDateCellValue().toString());
                }
            }
            i++;
        }
        return data;
    }

}
