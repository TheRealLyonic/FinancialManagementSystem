import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Deposit extends JFrame implements ActionListener, Colors, Fonts{

    private JLabel newDepositText;
    private JTextField newDepositTextField;

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

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLocation(this.getX(), 85);
        this.add(newDepositText);
        this.add(newDepositTextField);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
