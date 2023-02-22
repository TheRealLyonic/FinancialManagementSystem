import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Expenditure extends AdditionalWindow implements ActionListener{

    Expenditure(){
        this.setTitle("New Expenditure");
        this.setIconImage(new ImageIcon("resources\\expenditure_icon.png").getImage());

        //Heading text adjustments
        headingText.setText("New Expenditure");
        headingText.setLocation(headingText.getX() - 30, headingText.getY());

        //Summary textArea adjustments
        summaryTextArea.setText("Purchase Summary");

        //Dollar-symbol adjustments
        dollarSymbol.setIcon(new ImageIcon("resources\\red_dollar_icon.png"));

        //Add all components to the frame and make it visible
        showFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == confirmButton){
            try {

                if(Double.valueOf(currencyTextField.getText()) == 0.00){
                    JOptionPane.showMessageDialog(this, "ERROR: You must enter a dollar amount " +
                            "above 0 to create a new expenditure.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                    return;
                }else if(summaryTextArea.getText().equals("Purchase Summary")){
                    JOptionPane.showMessageDialog(this, "ERROR: You must provide a purchase " +
                            "summary in order to create a new expenditure.", "No Purchase Summary", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                FinancialManagementSystem.addExpenditure(currencyTextField.getText());
                FinancialManagementSystem.addPurchaseSummary(currencyTextField.getText(), summaryTextArea.getText());
                UserInterface.refreshWindow();
                this.dispose();
            }catch(IOException ex){
                throw new RuntimeException(ex);
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "ERROR: You must enter a valid number " +
                        "to make a new expenditure.", "Invalid Number", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
