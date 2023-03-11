import java.io.File;
import java.io.IOException;
import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;

public class FinancialManagementSystem{

    private static final Serializer SERIALIZER = new Serializer();
    private static final int NUM_OF_COLUMNS = 7; //The number of used columns in the spreadsheet
    private static ExcelSpreadsheet spreadsheet;
    private static int lastDepositRow;
    private static double startingBalance, balance, spentSinceLastDeposit;
    private static LocalDate currentLocalDate, lastSpreadsheetDate;
    private static ArrayList<ScheduledPayment> scheduledPayments = new ArrayList();
    private static ArrayList<HistoryData> historyData = new ArrayList();

    FinancialManagementSystem() throws IOException, ParseException, ClassNotFoundException {
        //Basic variable assignments and operations needed for the program to begin, keep in mind that the order of
        //these is important, because some of these methods require variables assigned in earlier methods to work.
        updateCurrentDate();
        findSpreadsheet();
        updateInformation(); //Update info. is called twice here so that all relevant information is updated after
        checkForMissingEntries(); //Missing entries are accounted for.
        updateInformation();

        //Ensures that all column widths are correct when the program begins, doesn't actually effect anything program
        //side, but it does make sure that column widths inside the spreadsheet will always be correct, even if the
        //user manually edits a purchase or deposit summary.
        autoSizeColumns();

        addHistoryData();

        new UserInterface();
    }

    //Will loop through all the years (Starting at the system's current year) until 2000 checking for a spreadsheet
    //with the name of that given year.
    private void findSpreadsheet(){
        for(int spreadsheetYear = currentLocalDate.getYear(); spreadsheetYear >= 2000; spreadsheetYear--){
            File spreadsheetFile = new File("data\\" + spreadsheetYear + ".xlsx");

            if(spreadsheetFile.exists()){
                spreadsheet = new ExcelSpreadsheet("data\\" + spreadsheetYear + ".xlsx", "Balance Sheet");
                return;
            }
        }
    }

    //!Update information stuff!
    private static void updateInformation() throws IOException, ParseException, ClassNotFoundException {
        //Worth noting here that the order of these should not be changed for any reason, as some of these functions
        //are dependent on the proper values from the earlier ones.
        updateLastSpreadsheetDate();
        updateBalance();
        updateStartingBalance();
        updateLastDepositRow();
        updateTotalSpentSinceLastDeposit();
        updateCurrentDate();
        updateScheduledPayments();
    }

    private static void updateLastSpreadsheetDate() throws IOException, ParseException {
        lastSpreadsheetDate = convertToLocalDate(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 0));
    }

    public static void updateBalance() throws IOException {
        updateStartingBalance();

        //BigDecimal class is used to avoid losing precision with decimal point math.
        double costs = new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 2))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        double deposits = new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 3))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

        balance = new BigDecimal(startingBalance + costs + deposits).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

        spreadsheet.writeToSpreadsheet(spreadsheet.getLastRow(), 4, String.valueOf(balance), "Number");
    }

    public static void updateStartingBalance() throws IOException{
        startingBalance = new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 1))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    public static void updateLastDepositRow() throws IOException{
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
    public static void updateTotalSpentSinceLastDeposit() throws IOException {
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

    public static void updateCurrentDate(){
        currentLocalDate = LocalDate.now();
    }

    public static void updateScheduledPayments() throws IOException, ClassNotFoundException {
        scheduledPayments.clear(); //Zeros out the scheduledPayments array list.

        File directory = new File("serialized");
        File[] directoryFiles = directory.listFiles();

        if(directoryFiles == null){
            UserInterface.showErrorMessage("Missing Directory", "ERROR: serialized folder does " +
                    "not exist in project directory.");
            return; //This shouldn't be necessary, since showErrorMessage ends the program, but just in-case.
        }

        //Keep this for ensuring all files are correctly named in terms of numbering, error occurs with deleting
        //serialized files if we don't have this check.
        for(int i = 0; i < directoryFiles.length; i++){
            directoryFiles[i].renameTo(new File("serialized\\scheduled_payment_" + i + ".ser"));
        }

        try{
            for(int i = 0; i < directoryFiles.length; i++){
                scheduledPayments.add( (ScheduledPayment) SERIALIZER.deserializeObject("serialized\\scheduled_payment_" + i + ".ser") );
            }
        }catch(InvalidClassException e){
            UserInterface.showErrorMessage("Invalid Class Exception", "ERROR: Cannot " +
                    "deserialize the provided .ser file into a ScheduledPayment instance because the classes " +
                    "do not match.");
        }
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

    //!Missing entries stuff!
        //Checks to see if there is more than a one-day gap between the last entry in the spreadsheet and the current
        //day. If there is, the addMissingEntries() method is called.
    private void checkForMissingEntries() throws IOException, ParseException, ClassNotFoundException {
        if(!lastEntryIsCurrent() && lastSpreadsheetDate.plusDays(1) != currentLocalDate){
            addMissingEntries();
        }
    }

    /*
    Adds any missing entries in the spreadsheet from the last time it was run. So if the program was last run on
    March 4th, and the currentLocalDate is the 15th of the same month and year, then the 'Missing Entries' would
    be those from March 5th-14th.
    */
    private void addMissingEntries() throws IOException, ParseException, ClassNotFoundException {
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
                    enterDefaultInformation(row, lastSpreadsheetDate, spreadsheet);
                    row++;

                    //Ensures that any scheduled payments are not missed while the rest of the year is being filled.
                    checkIfPaymentDue(lastSpreadsheetDate);

                    lastSpreadsheetDate = lastSpreadsheetDate.plusDays(1);
                }

                //Creates a new entry for today's date.
                createNewEntry();
            }
        }
    }

        //Completes the entries for the rest of the year in the currently accessed spreadsheet.
    private void fillRestOfYear() throws IOException, ClassNotFoundException {
        LocalDate lastDayOfYear = LocalDate.of(lastSpreadsheetDate.getYear(), 1, 1).with(TemporalAdjusters.lastDayOfYear());
        int row = spreadsheet.getLastRow() + 1;
        lastSpreadsheetDate = lastSpreadsheetDate.plusDays(1);

        //Loops so long as the spreadsheet's current date is not equal to the last day of the year
        while(!lastSpreadsheetDate.equals(lastDayOfYear)){
            enterDefaultInformation(row, lastSpreadsheetDate, spreadsheet);
            row++;

            checkIfPaymentDue(lastSpreadsheetDate);

            lastSpreadsheetDate = lastSpreadsheetDate.plusDays(1);
        }

        //Enters the default information for the final day of the year.
        enterDefaultInformation(row, lastSpreadsheetDate, spreadsheet);
    }

    //Standard method for evaluating a given date for any due scheduled payments. This includes handling the payment
    //if it is due on the given date, and informing the user of the activity via JOptionPanes.
    public static void checkIfPaymentDue(LocalDate date) throws IOException, ClassNotFoundException {
        for(int i = 0; i < scheduledPayments.size(); i++){
            scheduledPayments.get(i).updateNextPaymentDate();

            if(date.equals(scheduledPayments.get(i).getNextPaymentDate())){
                String paymentDate = scheduledPayments.get(i).getNextPaymentDate().format(DateTimeFormatter.ISO_LOCAL_DATE);

                //Response to the scheduled payment varies depending on the notification-type set by the user when
                //they created the Scheduled Payment.
                if(scheduledPayments.get(i).getReminderType().equals("Give Reminder")){
                    scheduledPayments.get(i).remindPayment(date, paymentDate);
                }else{
                    UserInterface.showPaymentMessage(paymentDate, "Deduct");
                    scheduledPayments.get(i).reoccurPayment(date);
                }

                File file = new File("serialized\\scheduled_payment_" + i + ".ser");
                file.delete();
                SERIALIZER.serializeObject(scheduledPayments.get(i), "serialized\\scheduled_payment_" + i + ".ser");

                updateScheduledPayments();
            }
        }
    }

    public static void enterDefaultInformation(int row, LocalDate entryDate, ExcelSpreadsheet spreadsheet) throws IOException {
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
        //Deposit Descriptions
        spreadsheet.writeToSpreadsheet(row, 5, "No deposits for this day.", "String");
        //Purchase Descriptions
        spreadsheet.writeToSpreadsheet(row, 6, "No purchases for this day.", "String");
    }

        //Handles all of the necessary actions for entering a new year, including creating a new spreadsheet.
    private void enterNewYear(String newYear) throws IOException, ParseException, ClassNotFoundException {
        LocalDate firstDayOfYear = LocalDate.of(Integer.parseInt(newYear), 1, 1);

        spreadsheet.createNewSpreadsheet("data\\" + newYear + ".xlsx", "Balance Sheet");
        setupSheet(spreadsheet);
        enterDefaultInformation(1, firstDayOfYear, spreadsheet);
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
        //Deposit Descriptions
        spreadsheet.writeToSpreadsheet(row, 5, "No deposits for this day.", "String");
        //Purchase Descriptions
        spreadsheet.writeToSpreadsheet(row, 6, "No purchases for this day.", "String");
    }

    public static void addHistoryData() throws IOException, ParseException {
        historyData.clear();

        for(int i = 1; i <= spreadsheet.getLastRow(); i++){
            LocalDate date = convertToLocalDate(spreadsheet.readFromSpreadsheet(i, 0));
            double startingBalance = new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(i, 1))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            double costTotal = new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(i, 2))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

            if(costTotal < 0.00){
                costTotal *= -1; //Makes the costTotal appear as a non-negative value in the table.
            }

            double depositTotal = new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(i, 3))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            double remainingBalance = new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(i, 4))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            String depositDescriptions = spreadsheet.readFromSpreadsheet(i, 5);
            String purchaseDescriptions = spreadsheet.readFromSpreadsheet(i, 6);

            if(costTotal == 0.00 && depositTotal == 0.00 && !date.equals(LocalDate.of(date.getYear(), 1, 1))){
                continue;
            }

            HistoryData historyDataInstance = new HistoryData(date, startingBalance, costTotal, depositTotal, remainingBalance, depositDescriptions, purchaseDescriptions);

            historyData.add(historyDataInstance);
        }
    }

    //!Formatting stuff!
    private static LocalDate convertToLocalDate(String spreadsheetDate) throws IOException, ParseException {
        //This little work around is needed to essentially convert the given instance of the Date class to an instance
        //of the localDate class, which is far easier to work with and read dates from.
        DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date rawSpreadsheetDate = formatter.parse(spreadsheetDate);
        return LocalDate.ofInstant(rawSpreadsheetDate.toInstant(), ZoneId.systemDefault());
    }

        //Formats a newly created spreadsheet with all of the correct columns, to be used when a new spreadsheet is
        //first created.
    public static void setupSheet(ExcelSpreadsheet spreadsheet) throws IOException {
        spreadsheet.writeToSpreadsheet(0, 0, "Date", "String");
        spreadsheet.writeToSpreadsheet(0, 1, "Starting Balance", "String");
        spreadsheet.writeToSpreadsheet(0, 2, "Costs", "String");
        spreadsheet.writeToSpreadsheet(0, 3, "Deposits", "String");
        spreadsheet.writeToSpreadsheet(0, 4, "Remaining Balance", "String");
        spreadsheet.writeToSpreadsheet(0, 5, "Deposit Descriptions", "String");
        spreadsheet.writeToSpreadsheet(0, 6, "Purchase Descriptions", "String");

        autoSizeColumns();
    }

        //Automatically formats the width of each column to perfectly fit the entered text.
    public static void autoSizeColumns() throws IOException {
        for(int i = 0; i < NUM_OF_COLUMNS; i++){
            //Adds the value at row 0 for each column (The heading row) back into the spreadsheet as itself, adding
            //some buffer-space for extra formatting purposes, also 'trims' the result from reading the spreadsheet
            //to avoid duplicating buffer-space.
            spreadsheet.writeToSpreadsheet(0, i, spreadsheet.readFromSpreadsheet(0, i).trim() + "            ", "Format");
        }
    }

    //!Add new information stuff!
    public static void addDeposit(String amount) throws IOException{
        if(Double.parseDouble(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 3)) == 0.00){
            spreadsheet.writeToSpreadsheet(spreadsheet.getLastRow(), 3, amount, "Number");
        }else{
            double total = Double.parseDouble(amount) + Double.parseDouble(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 3));
            spreadsheet.writeToSpreadsheet(spreadsheet.getLastRow(), 3, String.valueOf(total), "Number");
        }

        updateBalance();
    }

    public static void addExpenditure(String amount) throws IOException{
        //Makes the value of 'cost' negative, so that when it's added to the spreadsheet, the amount is deducted from
        //our total balance instead of added.
        double cost = (Double.parseDouble(amount) * -1);

        if(Double.parseDouble(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 2)) != 0.00){
            cost += Double.parseDouble(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 2));
        }

        spreadsheet.writeToSpreadsheet(spreadsheet.getLastRow(), 2, String.valueOf(cost), "Number");

        updateTotalSpentSinceLastDeposit();
        updateBalance();
    }

    public static void addScheduledPayment(ScheduledPayment scheduledPayment) throws IOException, ClassNotFoundException {
        SERIALIZER.serializeObject(scheduledPayment, "serialized\\scheduled_payment_" + scheduledPayments.size() + ".ser");
        updateScheduledPayments();
    }

    public static void deleteScheduledPayment(String path){
        File scheduledPaymentFile = new File(path);

        if(scheduledPaymentFile.exists()){
            scheduledPaymentFile.delete();
        }else{
            UserInterface.showErrorMessage("Nonexistent File", "ERROR: Tried to delete a file" +
                    " that does not exist.");
        }

    }

    //General method to be used for adding a summary to the spreadsheet. Works for both purchase and deposit summaries.
    public static void addSummary(String amount, String description, String summaryType) throws IOException {
        String summaryDefault;
        int cellNumber;

        switch(summaryType.toLowerCase()){
            case "purchase":
                summaryDefault = "No purchases for this day.";
                cellNumber = 6;
                break;
            case "deposit":
                summaryDefault = "No deposits for this day.";
                cellNumber = 5;
                break;
            default:
                summaryDefault = "ERROR";
                cellNumber = 9999;
                UserInterface.showErrorMessage("Invalid summaryType", "ERROR: Class gave invalid " +
                        "summaryType when trying to create a new summary in the spreadsheet.");
        }

        String fullString = "";

        if(!spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), cellNumber).equals(summaryDefault)){
            fullString = spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), cellNumber);
        }

        fullString += "$" + amount + " - " + description + " || ";

        spreadsheet.writeToSpreadsheet(spreadsheet.getLastRow(), cellNumber, fullString, "String");
        spreadsheet.writeToSpreadsheet(spreadsheet.getLastRow(), cellNumber, spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), cellNumber), "Format");
    }

    //Getters + Setters
    public static double getBalance() throws IOException {
        updateBalance();
        return balance;
    }

    public static double getSpentSinceLastDeposit() throws IOException {
        updateTotalSpentSinceLastDeposit();
        return spentSinceLastDeposit;
    }

    public static double getLastDeposit() throws IOException {
        updateLastDepositRow();
        return new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(lastDepositRow, 3))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    public static ArrayList<ScheduledPayment> getScheduledPayments(){
        return scheduledPayments;
    }

    public static ArrayList<HistoryData> getHistoryData(){
        return historyData;
    }

}