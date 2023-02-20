import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Deposit extends JFrame implements ActionListener, Colors, Fonts{

    private JLabel newDepositText;
    private JTextField newDepositTextField;
    private JTextArea newDepositTextArea;
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
        newDepositText.setLocation(160, 10);
        newDepositText.setSize(300, 75);

        //New Deposit Text Field stuff
        newDepositTextField = new JTextField("0.00");
        newDepositTextField.setSize(325, 95);
        newDepositTextField.setFont(ROBOTO_MEDIUM);
        newDepositTextField.setLocation(125, 85);

        //New Deposit Text Area Stuff
        newDepositTextArea = new JTextArea("Deposit Summary");
        newDepositTextArea.setLocation(10, 190);
        newDepositTextArea.setSize(450, 185);
        newDepositTextArea.setFont(ROBOTO_BUTTON);
        newDepositTextArea.setLineWrap(true);

        //Green Dollar Icon stuff
        greenDollarIcon = new JLabel();
        greenDollarIcon.setIcon(new ImageIcon("resources\\green_dollar_icon.png"));
        greenDollarIcon.setSize(95, 95);
        greenDollarIcon.setLocation(35, 85);

        //Confirm button stuff
        confirmButton = new JButton("Confirm");
        confirmButton.setSize(125, 75);
        confirmButton.setLocation(485, 295);
        confirmButton.addActionListener(this);
        confirmButton.setFont(ROBOTO_BUTTON);
        confirmButton.setFocusable(false);

        //Final JFrame Preparations
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLocation(this.getX(), 85);
        this.add(newDepositText);
        this.add(newDepositTextField);
        this.add(newDepositTextArea);
        this.add(greenDollarIcon);
        this.add(confirmButton);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == confirmButton){
            try {

                //ERROR: You must enter " +
                //                        "a valid number to make a new deposit.
                if(Double.valueOf(newDepositTextField.getText()) <= 0.00){
                    JOptionPane.showMessageDialog(this, "ERROR: You must enter a dollar " +
                            "amount above 0 to make a new deposit.", "Empty Deposit", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                FinancialManagementSystem.addDeposit(newDepositTextField.getText());
                UserInterface.refreshWindow();
                this.dispose();
            } catch(NumberFormatException | IOException ex){
                JOptionPane.showMessageDialog(this, "ERROR: You must enter a valid number " +
                        "to make a new deposit.", "Format Exception", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
