import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Expenditure extends JFrame implements ActionListener, Colors, Fonts {

    private JLabel newExpenditureText;
    private JTextField newExpenditureTextField;
    private JTextArea purchaseSummaryTextArea;
    private JButton confirmButton;
    private JLabel redDollarIcon;

    Expenditure(){
        this.setTitle("New Expenditure");
        this.setSize(650, 430);
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(FINANCIAL_BLUE);
        this.setIconImage(new ImageIcon("resources\\expenditure_icon.png").getImage());

        //New expenditure text stuff
        newExpenditureText = new JLabel("New Expenditure");
        newExpenditureText.setFont(ROBOTO_MEDIUM);
        newExpenditureText.setForeground(SUBDUED_WHITE);
        newExpenditureText.setLocation(125, 15);
        newExpenditureText.setSize(425, 75);

        //Red dollar icon stuff
        redDollarIcon = new JLabel();
        redDollarIcon.setIcon(new ImageIcon("resources\\red_dollar_icon.png"));
        redDollarIcon.setSize(95, 95);
        redDollarIcon.setLocation(35, 85);

        //New expenditure text field stuff
        newExpenditureTextField = new JTextField("0.00");
        newExpenditureTextField.setSize(325, 95);
        newExpenditureTextField.setFont(ROBOTO_MEDIUM);
        newExpenditureTextField.setLocation(125, 85);

        //Confirm button
        confirmButton = new JButton("Confirm");
        confirmButton.setSize(125, 75);
        confirmButton.setLocation(485, 295);
        confirmButton.setFont(ROBOTO_BUTTON);
        confirmButton.addActionListener(this);
        confirmButton.setFocusable(false);

        //Purchase summary text field stuff
        purchaseSummaryTextArea = new JTextArea("Purchase Summary");
        purchaseSummaryTextArea.setFont(ROBOTO_BUTTON);
        purchaseSummaryTextArea.setSize(450, 185);
        purchaseSummaryTextArea.setLocation(10, 190);
        purchaseSummaryTextArea.setLineWrap(true);


        //Final JFrame Preparations
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLocation(this.getX(), 85);
        this.add(newExpenditureText);
        this.add(newExpenditureTextField);
        this.add(confirmButton);
        this.add(redDollarIcon);
        this.add(purchaseSummaryTextArea);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == confirmButton){
            try {
                FinancialManagementSystem.addExpenditure(newExpenditureTextField.getText());
                UserInterface.updateBalanceText();
                this.dispose();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
