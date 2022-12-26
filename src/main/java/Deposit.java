import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Deposit extends JFrame implements ActionListener, Colors{

    private JLabel newDepositText;

    Deposit(){
        this.setTitle("New Deposit");
        this.setSize(650, 430);
        this.setLayout(null);
        this.getContentPane().setBackground(FINANCIAL_BLUE);
        this.setIconImage(new ImageIcon("resources\\deposit_icon.png").getImage());

        //New Deposit Text stuff
        newDepositText = new JLabel("New Deposit");
        newDepositText.setFont(new Font("Roboto", 1, 45));
        newDepositText.setForeground(SUBDUED_WHITE);
        newDepositText.setLocation(130, 15);
        newDepositText.setSize(300, 75);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLocation(this.getX(), 85);
        this.add(newDepositText);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
