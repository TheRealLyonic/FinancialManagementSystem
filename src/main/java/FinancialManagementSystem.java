import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class FinancialManagementSystem{

    private ExcelSpreadsheet spreadsheet;
    private double startingBalance;
    private double balance;
    private double spent;
    private int lastDepositRow;
    private LocalDate currentLocalDate;
    private LocalDate lastSpreadsheetDate;

    FinancialManagementSystem() throws IOException {
        spreadsheet = new ExcelSpreadsheet("data\\Financial_Data.xlsx", "Balance Sheet");

        //This little work around is needed to essentially convert the given instance of the Date class to an instance
        //of the localDate class, which is far easier to work with and read dates from.
        Date rawSpreadsheetDate = new Date(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 0));
        lastSpreadsheetDate = LocalDate.ofInstant(rawSpreadsheetDate.toInstant(), ZoneId.systemDefault());

        //Updates all the basic information for the program
        updateBalance();
        updateLastDepositRow();
        updateTotalSpent();
        updateCurrentDate();

        if(!lastEntryIsCurrent()){
            addMissingEntries();
        }

        new UserInterface();
    }

    public void updateBalance() throws IOException {
        balance = new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 4))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    public void updateLastDepositRow() throws IOException{
        for(int i = spreadsheet.getLastRow(); i >= 0; i--){
            if(Double.parseDouble(spreadsheet.readFromSpreadsheet(i, 3)) > 0.00){
                lastDepositRow = i;
                break;
            }
        }
    }

    public void updateTotalSpent() throws IOException {
        //Take the week's paycheck, subtract how much you have left, you get how much you spent
        spent = new BigDecimal(((new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(lastDepositRow, 3))).setScale(2, RoundingMode.HALF_EVEN).doubleValue() - balance))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    public void updateCurrentDate() throws IOException{
        currentLocalDate = LocalDate.now();
    }

    private boolean lastEntryIsCurrent() throws IOException {
        boolean yearMatch = lastSpreadsheetDate.getYear() == currentLocalDate.getYear();
        boolean monthMatch = lastSpreadsheetDate.getMonthValue() == currentLocalDate.getMonthValue();
        boolean dayMatch = lastSpreadsheetDate.getDayOfMonth() == currentLocalDate.getDayOfMonth();

        if(!yearMatch || !monthMatch || !dayMatch){
            return false;
        }else{
            return true;
        }
    }

    private void addMissingEntries() throws IOException{
        int numberOfMissingEntries = currentLocalDate.getDayOfMonth() - lastSpreadsheetDate.getDayOfMonth();

        int currentRow = spreadsheet.getLastRow() + 1;
        //Start at the first missing entry, go until it matches with the current day.
        for(int i = lastSpreadsheetDate.getDayOfMonth() + 1; i < currentLocalDate.getDayOfMonth(); i++){

        }
    }

    //Getters + Setters
    public double getBalance(){
        return balance;
    }

    public double getSpent(){
        return spent;
    }

    public int getLastDepositRow(){
        return lastDepositRow;
    }

}