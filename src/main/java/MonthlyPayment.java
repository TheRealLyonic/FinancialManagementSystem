import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class MonthlyPayment extends AdditionalWindow implements Colors, ActionListener, Fonts {

    private static LocalDate subscriptionStartDate;

    MonthlyPayment(){
        this.setTitle("Monthly Payment");
        this.setIconImage(new ImageIcon("resources\\monthly_payment_icon.png").getImage());

        subscriptionStartDate = LocalDate.now();

        //Heading text adjustments
        headingText.setText("Scheduled Payment");
        headingText.setSize(headingText.getWidth() + 125, headingText.getHeight());
        headingText.setLocation(headingText.getX() - 50, headingText.getY());

        //Summary textArea adjustments
        summaryTextArea.setText("Subscription Summary");

        //Dollar-symbol adjustments
        dollarSymbol.setIcon(new ImageIcon("resources\\red_dollar_icon.png"));

        //Add all components to the frame and make it visible
        showFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == confirmButton){
            if(confirmButton.getText().equals("Subscription Summary")){
                JOptionPane.showMessageDialog(this, "ERROR: You must provide a subscription " +
                        "summary in order to add a new scheduled payment.", "No Subscription Summary", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Add the new monthly payment information to the spreadsheet
            System.out.println("TEMP LOG");
        }
    }
}