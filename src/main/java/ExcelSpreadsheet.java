import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelSpreadsheet{

    private String filePath;

    ExcelSpreadsheet(String filePath){
        this.filePath = filePath;
    }

    //Will write data to a specified spreadsheet, at a specific cell within a row in a workbook.
    public void writeToSpreadsheet(String sheetName, int rowNumber, int cellNumber, String cellValue) throws IOException{
        XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream(filePath));

        if(workBook == null){
            System.out.println("File not found.");
            System.exit(-1);
        }

        Sheet sheet = workBook.getSheet(sheetName);

        if(sheet == null){
            System.out.println("Sheet is null.");
            System.exit(-1);
        }

        Row row = sheet.getRow(rowNumber);

        if(row == null){
            row = sheet.createRow(rowNumber);
        }

        Cell cell = row.createCell(cellNumber);

        try{
            cell.setCellValue(Double.parseDouble(cellValue));
        }catch(NumberFormatException e){
            cell.setCellValue(cellValue);
        }

        workBook.write(new FileOutputStream(filePath));
        workBook.close();
    }

    //Will read data from a specified spreadsheet, at a specific cell within a row in a workbook.
    public String readFromSpreadsheet(String sheetName, int rowNumber, int cellNumber) throws IOException{
        //Specifies the workbook info and reads from the requested cell
        XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream(filePath));

        if(workBook == null){
            System.out.println("File not found.");
            System.exit(-1);
        }

        XSSFSheet sheet = workBook.getSheet(sheetName);

        if(sheet == null){
            System.out.println("Sheet not found.");
            System.exit(-1);
        }

        XSSFRow row = sheet.getRow(rowNumber);

        if(row == null || row.getCell(cellNumber) == null){
            return "";
        }

        String cellValue;
        try{
            cellValue = row.getCell(cellNumber).getStringCellValue();
        }catch(IllegalStateException e){
            cellValue = Double.toString(row.getCell(cellNumber).getNumericCellValue());
        }

        workBook.close();
        return cellValue;
    }

}
