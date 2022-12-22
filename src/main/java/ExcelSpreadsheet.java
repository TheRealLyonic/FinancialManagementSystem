import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelSpreadsheet{

    private String filePath;
    private String sheetName;

    ExcelSpreadsheet(String filePath, String sheetName){
        this.filePath = filePath;
        this.sheetName = sheetName;
    }

    //Will write data to a specified spreadsheet, at a specific cell within a row in a workbook.
    public void writeToSpreadsheet(int rowNumber, int cellNumber, String cellValue) throws IOException{
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
    public String readFromSpreadsheet(int rowNumber, int cellNumber) throws IOException{
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

        //If the specified row/cell is empty, return a blank string.
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

    public int getLastRow() throws IOException {
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

        return sheet.getLastRowNum();
    }

    public int getLastCell(int rowNumber) throws IOException {
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

        if(row == null){
            return -1;
        }

        //Have to subtract 1 from this value because, for whatever reason, it seems that getLastCellNum() doesn't
        //start its cell count from 0, while getLastRowNum()--As well as pretty much everything else-- does.
        return row.getLastCellNum() - 1;
    }

    //Getters + Setters
    public void setSheetName(String sheetName){
        this.sheetName = sheetName;
    }

}
