import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Deposit extends JFrame implements ActionListener, Colors, Fonts{

    private JLabel newDepositText;
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
        confirmButton.setFont(ROBOTO_BUTTON);
        confirmButton.setFocusable(false);

        //Final JFrame Preparations
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLocation(this.getX(), 85);
        this.add(newDepositText);
        this.add(newDepositTextField);
        this.add(greenDollarIcon);
        this.add(confirmButton);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
