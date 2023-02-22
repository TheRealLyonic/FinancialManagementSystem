import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class ScheduledPayment extends AdditionalWindow implements Colors, ActionListener, Fonts {

    private static LocalDate subscriptionStartDate;

    ScheduledPayment(){
        this.setTitle("Scheduled Payment");
        this.setIconImage(new ImageIcon("resources\\scheduled_payment_icon.png").getImage());

        subscriptionStartDate = LocalDate.now();

        //Heading text adjustments
        headingText.setText("Scheduled Payment");
        headingText.setSize(headingText.getWidth() + 125, headingText.getHeight());
        headingText.setLocation(headingText.getX() - 50, headingText.getY());

        //Summary textArea adjustments
        summaryTextArea.setText("Payment Summary");

        //Dollar-symbol adjustments
        dollarSymbol.setIcon(new ImageIcon("resources\\red_dollar_icon.png"));

        //Add all components to the frame and make it visible
        showFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == confirmButton){

            try{
                if(Double.valueOf(currencyTextField.getText()) == 0.00){
                    JOptionPane.showMessageDialog(this, "ERROR: You must enter a dollar amount " +
                            "above zero to create a new scheduled payment.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                    return;
                }else if(summaryTextArea.getText().equals("Payment Summary")){
                    JOptionPane.showMessageDialog(this, "ERROR: You must provide a payment " +
                            "summary in order to add a new scheduled payment.", "No Payment Summary", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //Add the new monthly payment information to the spreadsheet
                //DO NOT FORGET TO ACCOUNT FOR IF A NEGATIVE NUMBER IS GIVEN. SINCE WE'RE LOSING MONEY, THAT SHOULD
                //BE AN ACCEPTED FORMAT, BUT MUST BE ACCOUNTED FOR LATER IN THE PROGRAM.
                System.out.println("TEMP LOG");
                this.dispose();
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "ERROR: You must enter a valid number " +
                        "to create a new scheduled payment.", "Format Exception", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}