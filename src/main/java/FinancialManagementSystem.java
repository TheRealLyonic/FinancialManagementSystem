import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class FinancialManagementSystem{

    private static ExcelSpreadsheet spreadsheet;
    private double startingBalance;
    private static double balance;
    private static double spentSinceLastDeposit;
    private static int lastDepositRow;
    private LocalDate currentLocalDate;
    private LocalDate lastSpreadsheetDate;

    FinancialManagementSystem() throws IOException, ParseException {
        updateCurrentDate();

        //Finds the correct spreadsheet for the program to first begin operation.
        findSpreadsheet();

        //Necessary for successfully running the lastEntryIsCurrent() and addMissingEntries() methods.
        updateInformation();

        checkForMissingEntries();

        //Once we're sure all missing dates are accounted for, we can update all the class' variables again.
        updateInformation();

        new UserInterface();
    }

    //Will loop through all the years (Starting at the system's current year) until 2000 checking for a spreadsheet
    //with the name of that given year.
    private void findSpreadsheet(){
        for(int spreadsheetYear = currentLocalDate.getYear(); spreadsheetYear > 2000; spreadsheetYear--){
            File spreadsheetFile = new File("data\\" + spreadsheetYear + ".xlsx");

            if(spreadsheetFile.exists()){
                spreadsheet = new ExcelSpreadsheet("data\\" + spreadsheetYear + ".xlsx", "Balance Sheet");
                return;
            }
        }
    }

    //Updates all the basic information for the program
    private void updateInformation() throws IOException, ParseException {
        updateLastSpreadsheetDate();
        updateBalance();
        updateStartingBalance();
        updateLastDepositRow();
        updateTotalSpentSinceLastDeposit();
        updateCurrentDate();
    }

    //Checks to see if there is more than a one-day gap between the last entry in the spreadsheet and the current
    //day. If there is, the addMissingEntries() method is called.
    private void checkForMissingEntries() throws IOException, ParseException {
        if(!lastEntryIsCurrent() && lastSpreadsheetDate.plusDays(1) != currentLocalDate){
            addMissingEntries();
        }
    }

    public void updateBalance() throws IOException {
        updateStartingBalance();

        double costs = new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 2))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        double deposits = new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 3))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

        balance = new BigDecimal(startingBalance + costs + deposits).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    public void updateStartingBalance() throws IOException{
        startingBalance = new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 1))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    public void updateLastDepositRow() throws IOException{
        for(int i = spreadsheet.getLastRow(); i >= 1; i--){
            if(Double.parseDouble(spreadsheet.readFromSpreadsheet(i, 3)) > 0.00){
                lastDepositRow = i;
                break;
            }else{
                lastDepositRow = 1;
            }
        }
    }

    //Updates the spentSinceLastDeposit variable, by adding up all of the costs since your last deposit. Useful pretty
    //much exclusively for the PieChart to be generated later.
    public void updateTotalSpentSinceLastDeposit() throws IOException {
        spentSinceLastDeposit = 0.00;

        for(int i = lastDepositRow; i < lastDepositRow + 7; i++){

            //Ensures that if less than 6 days has passed since your last deposit, a blank cell value is not
            //attempted to be added to the spentSinceLastDeposit variable.
            if(i > spreadsheet.getLastRow()){
                return;
            }

            //Get the value from the spreadsheet, and times it by negative one, since all costs are negative values.
            //Then add the new, positive value to the spentSinceLastDeposit variable to get a clear, positive
            //representation of how much you have spent.
            spentSinceLastDeposit += (new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(i, 2))).setScale(2, RoundingMode.HALF_EVEN).doubleValue()) * -1;
        }
    }

    public void updateCurrentDate(){
        currentLocalDate = LocalDate.now();
    }

    private void updateLastSpreadsheetDate() throws IOException, ParseException {
        //This little work around is needed to essentially convert the given instance of the Date class to an instance
        //of the localDate class, which is far easier to work with and read dates from.

        DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        Date rawSpreadsheetDate = formatter.parse(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 0));
        lastSpreadsheetDate = LocalDate.ofInstant(rawSpreadsheetDate.toInstant(), ZoneId.systemDefault());
    }

    private boolean lastEntryIsCurrent(){
        boolean yearMatch = lastSpreadsheetDate.getYear() == currentLocalDate.getYear();
        boolean monthMatch = lastSpreadsheetDate.getMonthValue() == currentLocalDate.getMonthValue();
        boolean dayMatch = lastSpreadsheetDate.getDayOfMonth() == currentLocalDate.getDayOfMonth();

        if(!yearMatch || !monthMatch || !dayMatch){
            return false;
        }else{
            return true;
        }
    }

    private void addMissingEntries() throws IOException, ParseException {
        if(lastSpreadsheetDate.plusDays(1).equals(currentLocalDate) && lastSpreadsheetDate.getYear() == currentLocalDate.getYear()){
            createNewEntry();
        }else{
            if(lastSpreadsheetDate.getYear() != currentLocalDate.getYear()){

                //Edge-case for if the last date in the spreadsheet was the last day of that year. Without this
                //the program would fill the old spreadsheet with the following year's dates.
                if(lastSpreadsheetDate.equals(LocalDate.of(lastSpreadsheetDate.getYear(), 1, 1).with(TemporalAdjusters.lastDayOfYear()))){
                    enterNewYear(String.valueOf(lastSpreadsheetDate.getYear() + 1));
                    return;
                }

                //Fill all the entries for the rest of the year, create and set up a new spreadsheet, then call this
                //method again to check for more missing entries.
                fillRestOfYear();
                enterNewYear( String.valueOf(lastSpreadsheetDate.getYear() + 1) );
            }else{
                //Fill up to the current date
                int row = spreadsheet.getLastRow() + 1;
                lastSpreadsheetDate = lastSpreadsheetDate.plusDays(1);

                while(!lastSpreadsheetDate.equals(currentLocalDate)){
                    enterDefaultInformation(row, lastSpreadsheetDate);
                    row++;
                    lastSpreadsheetDate = lastSpreadsheetDate.plusDays(1);
                }

                //Creates a new entry for today's date.
                createNewEntry();
            }
        }
    }

    //Completes the entries for the rest of the year in the currently accessed spreadsheet.
    private void fillRestOfYear() throws IOException{
        LocalDate lastDayOfYear = LocalDate.of(lastSpreadsheetDate.getYear(), 1, 1).with(TemporalAdjusters.lastDayOfYear());
        int row = spreadsheet.getLastRow() + 1;
        lastSpreadsheetDate = lastSpreadsheetDate.plusDays(1);

        //Loops so long as the spreadsheet's current date is not equal to the last day of the year
        while(!lastSpreadsheetDate.equals(lastDayOfYear)){
            enterDefaultInformation(row, lastSpreadsheetDate);
            row++;
            lastSpreadsheetDate = lastSpreadsheetDate.plusDays(1);
        }

        //Enters the default information for the final day of the year.
        enterDefaultInformation(row, lastSpreadsheetDate);
    }

    private void enterDefaultInformation(int row, LocalDate entryDate) throws IOException {
        //The Date of the Entry
        spreadsheet.writeToSpreadsheet(row, 0, String.valueOf(entryDate), "Date");
        //Starting Balance
        spreadsheet.writeToSpreadsheet(row, 1, String.valueOf(balance), "Number");
        //Costs
        spreadsheet.writeToSpreadsheet(row, 2, "0", "Number");
        //Deposits
        spreadsheet.writeToSpreadsheet(row, 3, "0", "Number");
        //Remaining Balance
        spreadsheet.writeToSpreadsheet(row, 4, String.valueOf(balance), "Number");
        //Purchase Descriptions
        spreadsheet.writeToSpreadsheet(row, 5, "No purchases for this day.", "String");
    }

    //Handles all of the necessary actions for entering a new year, including creating a new spreadsheet.
    private void enterNewYear(String newYear) throws IOException, ParseException {
        LocalDate firstDayOfYear = LocalDate.of(Integer.parseInt(newYear), 1, 1);

        spreadsheet.createNewSpreadsheet(newYear, "Balance Sheet");
        setupSheet();
        enterDefaultInformation(1, firstDayOfYear);
        updateInformation();
        checkForMissingEntries();
    }

    //Creates a new entry for today's date, to be used if there is not currently an entry.
    private void createNewEntry() throws IOException {
        int row = spreadsheet.getLastRow() + 1;

        //Date
        spreadsheet.writeToSpreadsheet(row, 0, currentLocalDate.toString(), "Date");
        //Starting Balance
        spreadsheet.writeToSpreadsheet(row, 1, spreadsheet.readFromSpreadsheet(row - 1, 4), "Number");
        //Costs
        spreadsheet.writeToSpreadsheet(row, 2, "0", "Number");
        //Deposits
        spreadsheet.writeToSpreadsheet(row, 3, "0", "Number");
        //Remaining Balance
        updateBalance();
        spreadsheet.writeToSpreadsheet(row, 4, String.valueOf(balance), "Number");
        //Purchase Descriptions
        spreadsheet.writeToSpreadsheet(row, 5, "No purchases for this day.", "String");
    }

    private void setupSheet() throws IOException {
        //Every entry here has two writeToSpreadsheet calls so that the column width is properly set, didn't like
        //cluttering up the titles of each row with the actual formatting specifications, so this approach was taken
        //instead.
        spreadsheet.writeToSpreadsheet(0, 0, "12/31/9999", "Starting_Value");
        spreadsheet.writeToSpreadsheet(0, 0, "Date", "String");

        spreadsheet.writeToSpreadsheet(0, 1, "Starting BalanceX", "Starting_Value");
        spreadsheet.writeToSpreadsheet(0, 1, "Starting Balance", "String");

        spreadsheet.writeToSpreadsheet(0, 2, "CostsXXXXXXXXXXXX", "Starting_Value");
        spreadsheet.writeToSpreadsheet(0, 2, "Costs", "String");

        spreadsheet.writeToSpreadsheet(0, 3, "DepositsXXXXXXXXX", "Starting_Value");
        spreadsheet.writeToSpreadsheet(0, 3, "Deposits", "String");

        spreadsheet.writeToSpreadsheet(0, 4, "Remaining Balance", "Starting_Value");
        spreadsheet.writeToSpreadsheet(0, 4, "Remaining Balance", "String");

        spreadsheet.writeToSpreadsheet(0, 5, "Purchase Descriptions DEFAULT TEXT FOR EXPANDING THE CURRENT WIDTH OF THE COLUMN.", "Starting_Value");
        spreadsheet.writeToSpreadsheet(0, 5, "Purchase Descriptions", "String");
    }

    //Getters + Setters
    public static double getBalance(){
        return balance;
    }

    public static double getSpentSinceLastDeposit(){
        return spentSinceLastDeposit;
    }

    public static double getLastDeposit() throws IOException {
        return new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(lastDepositRow, 3))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

}