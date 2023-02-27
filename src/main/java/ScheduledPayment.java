import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.Dictionary;

public class ScheduledPayment extends AdditionalWindow implements Colors{

    private JComboBox<String> frequencyDropdownBox = new JComboBox(new String[]{
            "Day(s)",
            "Month(s)",
            "Year(s)"
    });
    private JTextField frequencyTextField;
    private static LocalDate subscriptionStartDate;
    private Dictionary<Integer, String> frequencyOfPayment; //Key = Number of...; Value = "Days"/"Months"/"Years".

    ScheduledPayment(){
        this.setTitle("Scheduled Payment");
        this.setIconImage(new ImageIcon("resources\\scheduled_payment_icon.png").getImage());
        this.setSize(710, 620);
        this.getContentPane().setBackground(FINANCIAL_ORANGE);

        subscriptionStartDate = LocalDate.now();

        //Frequency Text-field stuff
        frequencyTextField = new JTextField();
        ( (AbstractDocument) frequencyTextField.getDocument()).setDocumentFilter(new NumberTextFilter(2));
        frequencyTextField.setSize(90, 75);
        frequencyTextField.setLocation(20, 245);
        frequencyTextField.setFont(ROBOTO_MEDIUM);
        components.add(frequencyTextField);

        //Frequency Dropdown box stuff
        frequencyDropdownBox.setSize(112, 55);
        frequencyDropdownBox.setLocation(325, 245);
        frequencyDropdownBox.setFont(ROBOTO_BUTTON);
        frequencyDropdownBox.setFocusable(false);
        components.add(frequencyDropdownBox);

        //Heading text adjustments
        headingText.setText("Scheduled Payment");
        headingText.setSize(headingText.getWidth() + 125, headingText.getHeight());
        headingText.setLocation(headingText.getX() - 15, headingText.getY());
        headingText.setForeground(DEFAULT_BLACK);

        //Summary textArea adjustments
        summaryTextArea.setText("Payment Summary");
        summaryTextArea.setLocation(summaryTextArea.getX(), summaryTextArea.getY() + 200);

        //Confirm Button adjustments
        confirmButton.setLocation(confirmButton.getX(), confirmButton.getY() + 200);

        //Dollar-symbol adjustments
        dollarSymbol.setIcon(new ImageIcon("resources\\red_dollar_icon.png"));

        //Add all components to the frame and make it visible
        showFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == confirmButton){

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
        }
    }
}