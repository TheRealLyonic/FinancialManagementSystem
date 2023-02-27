import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;

public class UserInterface extends JFrame implements ActionListener, Colors, Fonts{

    private static JFrame frame;
    private HashMap dataSet;
    private static PieChart pieChart;
    private MenuOption newDepositOption, newExpenditureOption, newScheduledPaymentOption;
    private static JLabel balanceText;
    private static int percentSpent, percentSaved;

    UserInterface() throws IOException {
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
        newDepositOption = new MenuOption(this, 25, 55, "New Deposit", new ImageIcon("resources\\new_deposit_icon.png"));
        newExpenditureOption = new MenuOption(this, 25, 165, "New Expenditure", new ImageIcon("resources\\new_expenditure_icon.png"));
        newScheduledPaymentOption = new MenuOption(this, 25, 340, "New Scheduled Payment", new ImageIcon("resources\\new_scheduled_payment_icon.png"));

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
        frame.add(newScheduledPaymentOption);
            //Other
        frame.add(pieChart.getChartPanel());

        frame.setVisible(true);


        if(FinancialManagementSystem.getScheduledPayments().size() != 0){
            for(ScheduledPayment scheduledPayment : FinancialManagementSystem.getScheduledPayments()){
                System.out.println(scheduledPayment.getStartDate());
                System.out.println(scheduledPayment.getQuantityOfFrequency());
                System.out.println(scheduledPayment.getMeasureOfFrequency());
            }
        }
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

    public static void refreshWindow() throws IOException {
        frame.dispose();
        new UserInterface();
    }

    public static void showErrorMessage(String errorTitle, String errorMessage){
        JOptionPane.showMessageDialog(frame, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newDepositOption.getOptionButton()){
            new Deposit();
        }else if(e.getSource() == newExpenditureOption.getOptionButton()){
            new Expenditure();
        }else if(e.getSource() == newScheduledPaymentOption.getOptionButton()){
            new ScheduledPayment();
        }
    }

    //Getters + Setters
    public static int getPercentSpent(){
        return percentSpent;
    }
}
