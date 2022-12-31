import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ExcelSpreadsheet{

    private String filePath;
    private String sheetName;
    private XSSFWorkbook workBook;
    private XSSFSheet sheet;

    ExcelSpreadsheet(String filePath, String sheetName){
        this.filePath = filePath;
        this.sheetName = sheetName;
    }

    //Will write data to a specified spreadsheet, at a specific cell within a row in a workbook.
    public void writeToSpreadsheet(int rowNumber, int cellNumber, String cellValue, String valueType) throws IOException{
        workBook = new XSSFWorkbook(new FileInputStream(filePath));

        if(workBook == null){
            System.out.println("File not found.");
            System.exit(-1);
        }

        sheet = workBook.getSheet(sheetName);

        if(sheet == null){
            System.out.println("Sheet is null.");
            System.exit(-1);
        }

        Row row = sheet.getRow(rowNumber);

        if(row == null){
            row = sheet.createRow(rowNumber);
        }

        Cell cell = row.createCell(cellNumber);

        //Made lower-case to account for developer-end errors involving the shift-key.
        valueType = valueType.toLowerCase();
        if(valueType.equals("date")){
            cell.setCellValue(Date.from(LocalDate.parse(cellValue).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            //Formats for a date
            CellStyle cellStyle = workBook.createCellStyle();
            CreationHelper creationHelper = workBook.getCreationHelper();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("m/d/yy"));

            cell.setCellStyle(cellStyle);
        }else if(valueType.equals("number")){
            cell.setCellValue(Double.parseDouble(cellValue));

            CellStyle cellStyle = workBook.createCellStyle();
            CreationHelper creationHelper = workBook.getCreationHelper();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("$0.00"));

            //If a cost is being added to the spreadsheet, the color is set to red, if a deposit is being added,
            //it's green, and if it's neither, the default color of black is applied.
            short index;
            if(cellNumber == 2){
                index = IndexedColors.RED.getIndex();
            }else if(cellNumber == 3){
                index = IndexedColors.GREEN.getIndex();
            }else{
                index = IndexedColors.BLACK.getIndex();
            }

            //Set the color
            Font font = workBook.createFont();
            font.setColor(index);
            cellStyle.setFont(font);

            cell.setCellStyle(cellStyle);
        }else if(valueType.equals("string")){
            cell.setCellValue(cellValue);
        }else if(valueType.equals("starting_value")){
            cell.setCellValue(cellValue);
            sheet.autoSizeColumn(cellNumber);
        }else{
            System.out.println("Invalid data-type parsed");
            System.exit(-1);
        }

        workBook.write(new FileOutputStream(filePath));
        workBook.close();
    }

    //Will read data from a specified spreadsheet, at a specific cell within a row in a workbook.
    public String readFromSpreadsheet(int rowNumber, int cellNumber) throws IOException{
        //Specifies the workbook info and reads from the requested cell
        workBook = new XSSFWorkbook(new FileInputStream(filePath));

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

        //Checks if the cell contains a date, number, or a string.
        if(row.getCell(cellNumber).getCellType() == Cell.CELL_TYPE_STRING){
            cellValue = row.getCell(cellNumber).getStringCellValue();
        }else if(DateUtil.isCellDateFormatted(row.getCell(cellNumber))){
            cellValue = row.getCell(cellNumber).getDateCellValue().toString();
        }else if(row.getCell(cellNumber).getCellType() == Cell.CELL_TYPE_NUMERIC || row.getCell(cellNumber).getCellType() == Cell.CELL_TYPE_FORMULA){
            cellValue = Double.toString(row.getCell(cellNumber).getNumericCellValue());
        }else{
            cellValue = "ERROR";
        }

        workBook.close();
        return cellValue;
    }

    /*
    If this method is returning the wrong value, try the following;
    1. Open Excel file and go to the expected sheet
    2. Select the last row + 1. E.g you have 12 rows with data, then click on row 13.
    3. Select the entire row [Shift]-[Space]
    4. Select all rows to the bottom of the sheet [Ctrl]-[Shift]-[Arrow down]
    5. Clear all selected rows [Ctrl]-[Minus]
    6. Save your workbook
    7. Rerun the code and check returned value
    */
    public int getLastRow() throws IOException {
        workBook = new XSSFWorkbook(new FileInputStream(filePath));

        if(workBook == null){
            System.out.println("File not found.");
            System.exit(-1);
        }

        sheet = workBook.getSheet(sheetName);

        if(sheet == null){
            System.out.println("Sheet not found.");
            System.exit(-1);
        }

        workBook.close();

        return sheet.getLastRowNum();
    }

    public int getLastCell(int rowNumber) throws IOException {
        workBook = new XSSFWorkbook(new FileInputStream(filePath));

        if(workBook == null){
            System.out.println("File not found.");
            System.exit(-1);
        }

        sheet = workBook.getSheet(sheetName);

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

    public void createNewSpreadsheet(String fileName, String sheetName) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet(sheetName);

        String filePath = "data\\" + fileName + ".xlsx";

        //Creates file the at the given path
        workbook.write(new FileOutputStream(filePath));

        this.filePath = filePath;
        this.sheetName = sheetName;
    }

    //Getters + Setters
    public XSSFSheet getSheet(){
        return sheet;
    }

    public void setSheetName(String sheetName){
        this.sheetName = sheetName;
    }

}
