import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Expenditure extends JFrame implements ActionListener, Colors, Fonts {

    private JLabel newExpenditureText;
    private JTextField newExpenditureTextField;

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

        //New expenditure text field stuff
        newExpenditureTextField = new JTextField("0.00");
        newExpenditureTextField.setSize(325, 95);
        newExpenditureTextField.setFont(ROBOTO_MEDIUM);
        newExpenditureTextField.setLocation(95, 250);


        //Final JFrame Preparations
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLocation(this.getX(), 85);
        this.add(newExpenditureText);
        this.add(newExpenditureTextField);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
