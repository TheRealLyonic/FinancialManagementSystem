import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class MonthlyPayment extends JFrame implements Colors, ActionListener, Fonts {

    private JButton confirmButton;
    private JTextArea subscriptionDescription;
    private static LocalDate subscriptionStartDate;

    MonthlyPayment(){
        this.setTitle("Monthly Payment");
        this.setSize(650, 430);
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(FINANCIAL_BLUE);
        this.setIconImage(new ImageIcon("resources\\monthly_payment_icon.png").getImage());

        subscriptionStartDate = LocalDate.now();

        //Subscription Description Text Area
        subscriptionDescription = new JTextArea("Subscription Description");

        //Confirm Button Stuff
        confirmButton = new JButton("Confirm");
        confirmButton.setFocusable(false);
        confirmButton.addActionListener(this);
        confirmButton.setLocation(490, 300);
        confirmButton.setSize(125, 75);
        confirmButton.setFont(ROBOTO_BUTTON);

        //Finishing Preparations
        this.add(confirmButton);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocation(this.getX(), 85);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == confirmButton){
            if(confirmButton.getText().toLowerCase().equals("subscription description")){

            }

            //Add the new monthly payment information to the spreadsheet
        }
    }

    //Getters + Setters
    public LocalDate getSubscriptionStartDate(){
        return subscriptionStartDate;
    }
}