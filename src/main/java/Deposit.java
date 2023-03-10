import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.ParseException;

public class Deposit extends AdditionalWindow{

    Deposit(){
        this.setTitle("New Deposit");
        this.setIconImage(new ImageIcon("resources\\deposit_icon.png").getImage());

        //Heading text adjustments
        headingText.setText("New Deposit");

        //Summary textArea adjustments
        summaryTextArea.setText("Deposit Summary");

        //Dollar-symbol adjustments
        dollarSymbol.setIcon(new ImageIcon("resources\\green_dollar_icon.png"));

        //Add all the components to the frame and make it visible
        showFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == confirmButton){
            try {
                if(Double.valueOf(currencyTextField.getText()) == 0.00){
                    JOptionPane.showMessageDialog(this, "ERROR: You must enter a dollar " +
                            "amount above 0 to make a new deposit.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                    return;
                }else if(summaryTextArea.getText().equals("Deposit Summary")){
                    JOptionPane.showMessageDialog(this, "ERROR: You must provide a deposit " +
                            "summary in order to make a new deposit.", "No Deposit Summary", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                FinancialManagementSystem.addDeposit(currencyTextField.getText());
                FinancialManagementSystem.addSummary(currencyTextField.getText(), summaryTextArea.getText(), "Deposit");
                this.dispose();
                UserInterface.refreshWindow();
            } catch(IOException ex){
                JOptionPane.showMessageDialog(this, "ERROR: An I/o exception occurred.",
                        "I/o Exception", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "ERROR: A Class Not Found Exception has " +
                        "occurred.", "Class Not Found Exception", JOptionPane.ERROR_MESSAGE);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "ERROR: A ParseException has occurred.",
                        "Parse Exception", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
