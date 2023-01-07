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
    private JButton newDepositButton, newExpenditureButton;
    private JLabel newDepositText, newExpenditureText;
    private static JLabel balanceText;
    private static int percentSpent, percentSaved;

    UserInterface() throws IOException {
        frame = this;

        frame.setTitle("Financial Management System");
        frame.setSize(850, 850);
        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());
        frame.setResizable(false);
        frame.setLayout(null);
        frame.getContentPane().setBackground(FINANCIAL_BLUE);

        //Pie-Chart stuff, the Dataset hashmap is used as reference for what to add to the pie-chart later
        updatePercentSpentAndSaved();

        dataSet = new HashMap();
        dataSet.put("Spent (" + percentSpent + "%)", percentSpent);
        dataSet.put("Saved (" + percentSaved + "%)", percentSaved);
        pieChart = new PieChart("Weekly Update", dataSet, 490, 0, 350, 300);

        //New Deposit Stuff
            //Deposit Button
        newDepositButton = new JButton();
        newDepositButton.setIcon(new ImageIcon("resources\\new_deposit_icon.png"));
        newDepositButton.setFocusable(false);
        newDepositButton.setLocation(25, 55);
        newDepositButton.setSize(55, 55);
        //Makes the JButton transparent, so that only the selected icon is visible.
        newDepositButton.setOpaque(false);
        newDepositButton.setContentAreaFilled(false);
        newDepositButton.addActionListener(this);
            //Deposit Text
        newDepositText = new JLabel("New Deposit");
        newDepositText.setFont(ROBOTO_SMALL);
        newDepositText.setForeground(SUBDUED_WHITE);
        newDepositText.setLocation(90, 46);
        newDepositText.setSize(225, 70);

        //New Expenditure Stuff
            //Expenditure Button
        newExpenditureButton = new JButton();
        newExpenditureButton.setIcon(new ImageIcon("resources\\new_expenditure_icon.png"));
        newExpenditureButton.setFocusable(false);
        newExpenditureButton.setLocation(25, 165);
        newExpenditureButton.setSize(55, 55);
        //Transparency stuff for the button
        newExpenditureButton.setOpaque(false);
        newExpenditureButton.setContentAreaFilled(false);
        newExpenditureButton.addActionListener(this);
            //Expenditure Text
        newExpenditureText = new JLabel("New Expenditure");
        newExpenditureText.setFont(ROBOTO_SMALL);
        newExpenditureText.setForeground(SUBDUED_WHITE);
        newExpenditureText.setLocation(90, 156);
        newExpenditureText.setSize(350, 70);

        //Balance Display Stuff
        double balance = FinancialManagementSystem.getBalance();

        balanceText = new JLabel("Balance: " + NumberFormat.getCurrencyInstance().format(balance));
        balanceText.setFont(ROBOTO_LARGE);
        balanceText.setForeground(SUBDUED_WHITE);

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

        //Final JFrame preparations
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(pieChart.getChartPanel());
        frame.add(newDepositButton);
        frame.add(newDepositText);
        frame.add(newExpenditureButton);
        frame.add(newExpenditureText);
        frame.add(balanceText);
        frame.setVisible(true);
    }

    public static void refreshWindow() throws IOException {
        frame.dispose();
        new UserInterface();
    }

    public static void updatePercentSpentAndSaved() throws IOException {
        percentSpent = (int) ((FinancialManagementSystem.getSpentSinceLastDeposit() / FinancialManagementSystem.getLastDeposit()) * 100);
        percentSaved = 100 - percentSpent;

        if(FinancialManagementSystem.getSpentSinceLastDeposit() <= 0){
            percentSpent = 0;
            percentSaved = 100;
        }else if(percentSpent >= 100.00){
            percentSpent = 100;
            percentSaved = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newDepositButton){
            new Deposit();
        }else if(e.getSource() == newExpenditureButton){
            new Expenditure();
        }
    }

    //Getters + Setters
    public static int getPercentSpent(){
        return percentSpent;
    }
}
