import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Deposit extends JFrame implements ActionListener, Colors, Fonts{

    private JLabel newDepositText, emptyDepositWarningText, invalidNumberWarningText;
    private JTextField newDepositTextField;
    private JButton confirmButton;
    private JLabel greenDollarIcon;

    Deposit(){
        this.setTitle("New Deposit");
        this.setSize(650, 430);
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(FINANCIAL_BLUE);
        this.setIconImage(new ImageIcon("resources\\deposit_icon.png").getImage());

        //New Deposit Text stuff
        newDepositText = new JLabel("New Deposit");
        newDepositText.setFont(ROBOTO_MEDIUM);
        newDepositText.setForeground(SUBDUED_WHITE);
        newDepositText.setLocation(150, 15);
        newDepositText.setSize(300, 75);

        //New Deposit Text Field stuff
        newDepositTextField = new JTextField("0.00");
        newDepositTextField.setSize(325, 95);
        newDepositTextField.setFont(ROBOTO_MEDIUM);
        newDepositTextField.setLocation(95, 250);

        //Green Dollar Icon stuff
        greenDollarIcon = new JLabel();
        greenDollarIcon.setIcon(new ImageIcon("resources\\green_dollar_icon.png"));
        greenDollarIcon.setSize(95, 95);
        greenDollarIcon.setLocation(5, 250);

        //Confirm button stuff
        confirmButton = new JButton("Confirm");
        confirmButton.setSize(125, 75);
        confirmButton.setLocation(460, 260);
        confirmButton.addActionListener(this);
        confirmButton.setFont(ROBOTO_BUTTON);
        confirmButton.setFocusable(false);

        //Empty deposit warning text stuff
        emptyDepositWarningText = new JLabel("<html>ERROR: You must enter a dollar amount above 0 to make a new" +
                " deposit.</html>");
        emptyDepositWarningText.setFont(ROBOTO_BUTTON);
        emptyDepositWarningText.setForeground(ERROR_RED);
        emptyDepositWarningText.setLocation(180, 100);
        emptyDepositWarningText.setSize(250, 150);
        emptyDepositWarningText.setVisible(false);

        //Invalid number warning text stuff
        invalidNumberWarningText = new JLabel("<html>ERROR: You must enter a valid number to make a new " +
                "deposit.</html>");
        invalidNumberWarningText.setFont(ROBOTO_BUTTON);
        invalidNumberWarningText.setForeground(ERROR_RED);
        invalidNumberWarningText.setLocation(180, 100);
        invalidNumberWarningText.setSize(250, 150);
        invalidNumberWarningText.setVisible(false);

        //Final JFrame Preparations
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLocation(this.getX(), 85);
        this.add(newDepositText);
        this.add(newDepositTextField);
        this.add(greenDollarIcon);
        this.add(confirmButton);
        this.add(emptyDepositWarningText);
        this.add(invalidNumberWarningText);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == confirmButton){
            try {

                if(Double.valueOf(newDepositTextField.getText()) <= 0.00){
                    invalidNumberWarningText.setVisible(false);
                    emptyDepositWarningText.setVisible(true);
                    return;
                }

                FinancialManagementSystem.addDeposit(newDepositTextField.getText());
                UserInterface.refreshWindow();
                this.dispose();
            } catch(IOException ex){
                emptyDepositWarningText.setVisible(false);
                invalidNumberWarningText.setVisible(true);
            }
        }
    }
}
