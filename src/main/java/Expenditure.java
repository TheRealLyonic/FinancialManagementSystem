import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.ParseException;

public class Expenditure extends AdditionalWindow{

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
                FinancialManagementSystem.addSummary(currencyTextField.getText(), summaryTextArea.getText(), "Purchase");
                UserInterface.refreshWindow();
                this.dispose();
            }catch(IOException ex){
                JOptionPane.showMessageDialog(this, "ERROR: An I/o exception occurred.",
                        "I/o Exception", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "ERROR: A Class Not Found Exception has " +
                        "occurred.", "Class Not Found Exception", JOptionPane.ERROR_MESSAGE);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "ERROR: A Parse Exception has occurred.",
                        "Parse Exception", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
