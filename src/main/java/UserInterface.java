import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;

public class UserInterface extends JFrame implements ActionListener, Colors, Fonts{

    private static JFrame frame;
    private HashMap dataSet;
    private static PieChart pieChart;
    private MenuOption newDepositOption, newExpenditureOption, viewHistoryOption, newScheduledPaymentOption, viewActiveScheduledPaymentsOption;
    private static JLabel balanceText;
    private static int percentSpent, percentSaved;

    UserInterface() throws IOException, ClassNotFoundException {

        FinancialManagementSystem.checkIfPaymentDue(LocalDate.now());

        frame = this;

        //!Basic JFrame stuff!
        frame.setTitle("Financial Management System");
        frame.setSize(850, 850);
        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());
        frame.setResizable(false);
        frame.setLayout(null);
        frame.getContentPane().setBackground(FINANCIAL_BLUE);

        //!Pie-Chart stuff!
        updatePercentSpentAndSaved();

            //The 'dataSet' hashmap is used as reference for what to add to the pie-chart later.
        dataSet = new HashMap();
        dataSet.put("Spent (" + percentSpent + "%)", percentSpent);
        dataSet.put("Saved (" + percentSaved + "%)", percentSaved);
        pieChart = new PieChart("Weekly Update", dataSet, 490, 0, 350, 300);

        //!Menu Option stuff!
            //Plus 110 to the y each time.
        newDepositOption = new MenuOption(this, 25, 55, "New Deposit", new ImageIcon("resources\\new_deposit_icon.png"));
        newExpenditureOption = new MenuOption(this, 25, 165, "New Expenditure", new ImageIcon("resources\\new_expenditure_icon.png"));
        viewHistoryOption = new MenuOption(this, 25, 275, "View Financial History", new ImageIcon("resources\\view_history_option_icon.png"));
        newScheduledPaymentOption = new MenuOption(this, 25, 385, "New Scheduled Payment", new ImageIcon("resources\\new_scheduled_payment_icon.png"));
        viewActiveScheduledPaymentsOption = new MenuOption(this, 25, 495, "View Active Scheduled Payments", new ImageIcon("resources\\view_active_scheduled_payments_icon.png"));

        //!Balance Display Stuff!
        double balance = FinancialManagementSystem.getBalance();

        balanceText = new JLabel("Balance: " + NumberFormat.getCurrencyInstance().format(balance));
        balanceText.setFont(ROBOTO_LARGE);

        if(balance < 0.00){
            balanceText.setForeground(NEGATIVE_RED);
        }else{
            balanceText.setForeground(SUBDUED_WHITE);
        }

            //Changes the size and location of the balanceText object depending on the rough dollar amount. This is so that
            //regardless of what your current balance is (within reasonable standard), it will still appear relatively
            //centered within the JFrame.
        if(balance < 1000.00){
            balanceText.setSize(700, 700);
            balanceText.setLocation(155, 295);
        }else if(balance < 1000000.00){
            balanceText.setSize(700, 700);
            balanceText.setLocation(125, 295);
        }else{
            balanceText.setSize(825, 700);
            balanceText.setLocation(75, 295);
        }

        //!Final JFrame stuff!
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

            //Primary text elements
        frame.add(balanceText);
            //Menu Options
        frame.add(newDepositOption);
        frame.add(newExpenditureOption);
        frame.add(viewHistoryOption);
        frame.add(newScheduledPaymentOption);
        frame.add(viewActiveScheduledPaymentsOption);
            //Other
        frame.add(pieChart.getChartPanel());

        frame.setVisible(true);
    }

    //Updates the variables necessary for the creation of the Pie-chart.
    public static void updatePercentSpentAndSaved() throws IOException {
        percentSpent = (int) ((FinancialManagementSystem.getSpentSinceLastDeposit() / FinancialManagementSystem.getLastDeposit()) * 100);
        percentSaved = 100 - percentSpent;

        //Check ensuring the if either value is over 100, it will max out at 100%
        if(FinancialManagementSystem.getSpentSinceLastDeposit() <= 0){
            percentSpent = 0;
            percentSaved = 100;
        }else if(percentSpent >= 100.00){
            percentSpent = 100;
            percentSaved = 0;
        }
    }

    public static void refreshWindow() throws IOException, ClassNotFoundException, ParseException {
        frame.dispose();
        FinancialManagementSystem.addHistoryData();
        new UserInterface();
    }

    public static void showErrorMessage(String errorTitle, String errorMessage){
        JOptionPane.showMessageDialog(frame, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    public static void showPaymentMessage(String date, String paymentType){
        if(paymentType.equalsIgnoreCase("Remind")){
            JOptionPane.showMessageDialog(frame, "A subscription due on " + date + " should be accounted for " +
                    "in the spreadsheet, as the program has not been told to automatically deduct the set amount.",
                    "Payment Renewal Notification", JOptionPane.INFORMATION_MESSAGE);
        }else if(paymentType.equalsIgnoreCase("Deduct")){
            JOptionPane.showMessageDialog(frame, "A subscription due on " + date + " has been renewed, and " +
                    "deducted from your balance accordingly.", "Payment Renewal Notification", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(frame, "Non-Critical ERROR: The method showPaymentMessage() has been " +
                    "passed an illegal parameter, and the scheduled notification could not be displayed. Just know that " +
                    "a scheduled payment is due and should be accounted for manually.", "Invalid parameter",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newDepositOption.getOptionButton()){
            new Deposit();
        }else if(e.getSource() == newExpenditureOption.getOptionButton()){
            new Expenditure();
        }else if(e.getSource() == viewHistoryOption.getOptionButton()){
            new ViewHistory(FinancialManagementSystem.getHistoryData());
        }else if(e.getSource() == newScheduledPaymentOption.getOptionButton()){
            new ScheduledPayment();
        }else if(e.getSource() == viewActiveScheduledPaymentsOption.getOptionButton()){
            new ViewActiveScheduledPayments(FinancialManagementSystem.getScheduledPayments());
        }
    }

    //Getters + Setters
    public static int getPercentSpent(){
        return percentSpent;
    }
}
