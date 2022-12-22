import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FinancialManagementSystem{

    private double balance;
    private double spent;
    private int lastDepositRow;
    private ExcelSpreadsheet spreadsheet;

    FinancialManagementSystem() throws IOException {
        spreadsheet = new ExcelSpreadsheet("D:\\Documents\\Java Projects\\FinancialManagementSystem\\data\\2022.xlsx", "Balance Sheet");

        updateBalance();
        updateLastDepositRow();
        //Take the week's paycheck, subtract how much you have left, you get how much you spent
        spent = new BigDecimal(((new BigDecimal(Double.parseDouble(spreadsheet.readFromSpreadsheet(lastDepositRow, 3))).setScale(2, RoundingMode.HALF_EVEN).doubleValue() - balance))).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

        System.out.println(balance);
        System.out.println(lastDepositRow);
        System.out.println(spent);
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

}