import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

public class UserInterface extends JFrame implements ActionListener, Colors, Fonts{

    private HashMap dataSet;
    private PieChart pieChart;
    private JButton newDepositButton;
    private JLabel newDepositText, balanceText;

    UserInterface() throws IOException {
        this.setTitle("Financial Management System");
        this.setSize(850, 850);
        this.setIconImage(new ImageIcon("resources/icon.png").getImage());
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(FINANCIAL_BLUE);

        //Pie-Chart stuff, the Dataset hashmap is used as reference for what to add to the pie-chart later
        int percentSpent = (int) ((FinancialManagementSystem.getSpentSinceLastDeposit() / FinancialManagementSystem.getLastDeposit()) * 100);
        int percentSaved = 100 - percentSpent;

        if(FinancialManagementSystem.getSpentSinceLastDeposit() <= 0){
            percentSpent = 0;
            percentSaved = 100;
        }else if(percentSpent >= 100.00){
            percentSpent = 100;
            percentSaved = 0;
        }

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

        //Balance Stuff
        balanceText = new JLabel("Balance: $" + FinancialManagementSystem.getBalance());
        System.out.println("Balance: $" + FinancialManagementSystem.getBalance());
        balanceText.setFont(ROBOTO_LARGE);
        balanceText.setForeground(SUBDUED_WHITE);
        balanceText.setLocation(155, 225);
        balanceText.setSize(700, 700);

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(pieChart.getChartPanel());
        this.add(newDepositButton);
        this.add(newDepositText);
        this.add(balanceText);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newDepositButton){
            new Deposit();
        }
    }
}
