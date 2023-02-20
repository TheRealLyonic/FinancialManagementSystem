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
    private JLabel purchaseSummaryWarningText, emptyExpenditureWarningText, invalidNumberWarningText;

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

        //Purchase summary warning text stuff
        //Adding the HTML tags here because it makes the text wrap. Why is this not a default feature for the JLabel
        //class? Beats me.
        purchaseSummaryWarningText = new JLabel("<html>ERROR: You must enter a purchase summary in order to create a" +
                " new expenditure.</html>");
        purchaseSummaryWarningText.setFont(ROBOTO_BUTTON);
        purchaseSummaryWarningText.setForeground(ERROR_RED);
        purchaseSummaryWarningText.setLocation(480, 50);
        purchaseSummaryWarningText.setSize(150, 250);
        purchaseSummaryWarningText.setVisible(false);

        //Empty expenditure warning text stuff
        emptyExpenditureWarningText = new JLabel("<html>ERROR: You must enter a dollar amount above 0" +
                " to create a new expenditure.</html>");
        emptyExpenditureWarningText.setFont(ROBOTO_BUTTON);
        emptyExpenditureWarningText.setForeground(ERROR_RED);
        emptyExpenditureWarningText.setLocation(480, 50);
        emptyExpenditureWarningText.setSize(150, 250);
        emptyExpenditureWarningText.setVisible(false);

        //Invalid number warning text stuff
        invalidNumberWarningText = new JLabel("<html>ERROR: You must enter a valid number to make a new " +
                "expenditure.</html>");
        invalidNumberWarningText.setFont(ROBOTO_BUTTON);
        invalidNumberWarningText.setForeground(ERROR_RED);
        invalidNumberWarningText.setLocation(480, 50);
        invalidNumberWarningText.setSize(150, 250);
        invalidNumberWarningText.setVisible(false);

        //Final JFrame Preparations
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLocation(this.getX(), 85);
        this.add(newExpenditureText);
        this.add(newExpenditureTextField);
        this.add(confirmButton);
        this.add(redDollarIcon);
        this.add(purchaseSummaryTextArea);
        this.add(purchaseSummaryWarningText);
        this.add(emptyExpenditureWarningText);
        this.add(invalidNumberWarningText);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == confirmButton){
            try {

                if(Double.valueOf(newExpenditureTextField.getText()) == 0.00){
                    purchaseSummaryWarningText.setVisible(false);
                    invalidNumberWarningText.setVisible(false);
                    emptyExpenditureWarningText.setVisible(true);
                    return;
                }else if(purchaseSummaryTextArea.getText().equals("Purchase Summary")){
                    emptyExpenditureWarningText.setVisible(false);
                    invalidNumberWarningText.setVisible(false);
                    purchaseSummaryWarningText.setVisible(true);
                    return;
                }

                FinancialManagementSystem.addExpenditure(newExpenditureTextField.getText());
                FinancialManagementSystem.addPurchaseSummary(newExpenditureTextField.getText(), purchaseSummaryTextArea.getText());
                UserInterface.refreshWindow();
                this.dispose();
            }catch(IOException ex){
                throw new RuntimeException(ex);
            }catch(NumberFormatException ex){
                purchaseSummaryWarningText.setVisible(false);
                emptyExpenditureWarningText.setVisible(false);
                invalidNumberWarningText.setVisible(true);
            }
        }
    }
}
