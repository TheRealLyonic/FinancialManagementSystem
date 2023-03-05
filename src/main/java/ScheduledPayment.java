import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;

public class ScheduledPayment extends AdditionalWindow implements Colors, Serializable {

    private JComboBox<String> frequencyDropdownBox = new JComboBox(new String[]{
            "Day(s)",
            "Month(s)",
            "Year(s)"
    });
    private JComboBox<String> notifyTypeDropdownBox = new JComboBox(new String[]{
            "Automatically Deduct",
            "Give Reminder"
    });
    private JLabel frequencyDisplayText, notifyTypeDisplayText;
    private JTextField frequencyTextField;
    private LocalDate startDate;
    private double amount;
    private String measureOfFrequency;
    private int quantityOfFrequency;

    ScheduledPayment(){
        this.setTitle("New Scheduled Payment");
        this.setIconImage(new ImageIcon("resources\\scheduled_payment_icon.png").getImage());
        this.setSize(710, 620);
        this.getContentPane().setBackground(FINANCIAL_ORANGE);

        startDate = LocalDate.now();

        //Frequency display text stuff
        frequencyDisplayText = new JLabel("Occurs Every:");
        frequencyDisplayText.setLocation(5, 245);
        frequencyDisplayText.setSize(325, 85);
        frequencyDisplayText.setFont(ROBOTO_MEDIUM);
        frequencyDisplayText.setForeground(DEFAULT_BLACK);
        components.add(frequencyDisplayText);

        //Frequency Text-field stuff
        frequencyTextField = new JTextField();
        ( (AbstractDocument) frequencyTextField.getDocument()).setDocumentFilter(new NumberTextFilter(2));
        frequencyTextField.setSize(90, 75);
        frequencyTextField.setLocation(325, 253);
        frequencyTextField.setFont(ROBOTO_MEDIUM);
        components.add(frequencyTextField);

        //Frequency Dropdown box stuff
        frequencyDropdownBox.setSize(112, 55);
        frequencyDropdownBox.setLocation(425, 261);
        frequencyDropdownBox.setFont(ROBOTO_BUTTON);
        frequencyDropdownBox.setFocusable(false);
        components.add(frequencyDropdownBox);

        //Heading text adjustments
        headingText.setText("New Scheduled Payment");
        headingText.setSize(headingText.getWidth() + 125, headingText.getHeight());
        headingText.setLocation(headingText.getX() - 80, headingText.getY());
        headingText.setForeground(DEFAULT_BLACK);

        //Summary textArea adjustments
        summaryTextArea.setText("Payment Summary");
        summaryTextArea.setLocation(summaryTextArea.getX() - 2, summaryTextArea.getY() + 196);

        //Confirm Button adjustments
        confirmButton.setLocation(confirmButton.getX() + 28, confirmButton.getY() + 207);

        //Notify type display text stuff
        notifyTypeDisplayText = new JLabel("Notify Type:");
        notifyTypeDisplayText.setSize(225, 85);
        notifyTypeDisplayText.setLocation(471, 315);
        notifyTypeDisplayText.setFont(ROBOTO_SMALL);
        notifyTypeDisplayText.setForeground(DEFAULT_BLACK);
        components.add(notifyTypeDisplayText);

        //Notify type dropdown box stuff
        notifyTypeDropdownBox.setSize(227, 55);
        notifyTypeDropdownBox.setLocation(463, 385);
        notifyTypeDropdownBox.setFont(ROBOTO_BUTTON);
        notifyTypeDropdownBox.setFocusable(false);
        components.add(notifyTypeDropdownBox);

        //Currency textField adjustments
        currencyTextField.setLocation(currencyTextField.getX() + 74, currencyTextField.getY() + 34);

        //Dollar-symbol adjustments
        dollarSymbol.setIcon(new ImageIcon("resources\\red_dollar_icon.png"));
        dollarSymbol.setLocation(dollarSymbol.getX() + 74, dollarSymbol.getY() + 34);

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

            quantityOfFrequency = Integer.parseInt(frequencyTextField.getText());
            measureOfFrequency = frequencyDropdownBox.getSelectedItem().toString();
            amount = Double.valueOf(currencyTextField.getText());

            try {
                FinancialManagementSystem.addScheduledPayment(this);
                UserInterface.refreshWindow();
            } catch (IOException ex) {
                UserInterface.showErrorMessage("I/o Exception", "ERROR: An I/o Exception " +
                        "has occurred.");
            } catch (ClassNotFoundException ex) {
                UserInterface.showErrorMessage("Class Not Found", "ERROR: The ScheduledPayment " +
                        "class was not found.");
            }

            this.dispose();
        }
    }

    public LocalDate getStartDate(){
        return startDate;
    }

    public double getAmount(){
        return amount;
    }

    public int getQuantityOfFrequency(){
        return quantityOfFrequency;
    }

    public String getMeasureOfFrequency(){
        return measureOfFrequency;
    }

}