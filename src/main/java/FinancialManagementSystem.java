import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

public class FinancialManagementSystem{

    private ExcelSpreadsheet spreadsheet;
    private double startingBalance;
    private double balance;
    private double spent;
    private int lastDepositRow;
    private LocalDate localDate;
    private int currentDay, currentYear;
    private Month currentMonth;

    FinancialManagementSystem() throws IOException {
        spreadsheet = new ExcelSpreadsheet("data\\Financial_Data.xlsx", "Balance Sheet");

        updateBalance();
        updateLastDepositRow();
        updateTotalSpent();
        updateCurrentDate();

        System.out.println(localDate);
        System.out.println(spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 0));

//        if( (spreadsheet.readFromSpreadsheet(spreadsheet.getLastRow(), 0))  )

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
        localDate = LocalDate.now();
        currentMonth = localDate.getMonth();
        currentDay = localDate.getDayOfMonth();
        currentYear = localDate.getYear();
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