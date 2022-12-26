import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

public class UserInterface extends JFrame implements ActionListener, Colors{

    private HashMap dataSet;
    private PieChart pieChart;
    private JButton newDepositButton;
    private JLabel newDepositText;

    UserInterface() throws IOException {
        this.setTitle("Financial Management System");
        this.setSize(850, 850);
        this.setIconImage(new ImageIcon("resources/icon.png").getImage());
        this.setLayout(null);
        this.getContentPane().setBackground(FINANCIAL_BLUE);

        //Pie-Chart stuff, the Dataset hashmap is used as reference for what to add to the pie-chart later
        dataSet = new HashMap();
        dataSet.put("Saved (62%)", 62);
        dataSet.put("Spent (38%)", 38);
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
        newDepositText.setFont(new Font("Roboto", 1, 35));
        newDepositText.setForeground(SUBDUED_WHITE);
        newDepositText.setLocation(90, 46);
        newDepositText.setSize(225, 70);

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(pieChart.getChartPanel());
        this.add(newDepositButton);
        this.add(newDepositText);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newDepositButton){
            new Deposit();
        }
    }
}
