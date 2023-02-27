import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AdditionalWindow extends JFrame implements Colors, Fonts, ActionListener{

    protected JLabel headingText;
    protected JTextField currencyTextField;
    protected JTextArea summaryTextArea;
    protected JButton confirmButton;
    protected JLabel dollarSymbol;
    protected ArrayList<Component> components = new ArrayList<>();

    AdditionalWindow(){
        this.setSize(650, 430);
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(FINANCIAL_BLUE);

        //Default headingText stuff
        headingText = new JLabel("Default Title");
        headingText.setFont(ROBOTO_MEDIUM);
        headingText.setForeground(SUBDUED_WHITE);
        headingText.setLocation(160, 10);
        headingText.setSize(410, 75);

        //Default currencyTextField stuff
        currencyTextField = new JTextField("0.00");
        currencyTextField.setSize(325, 95);
        currencyTextField.setFont(ROBOTO_MEDIUM);
        currencyTextField.setLocation(125, 85);
            //Limits the amount of characters that may be entered in the TextField.
        ( (AbstractDocument) currencyTextField.getDocument()).setDocumentFilter(new CurrencyTextFilter());

        //Default summaryTextArea stuff
        summaryTextArea = new JTextArea("Default Summary");
        summaryTextArea.setLocation(10, 190);
        summaryTextArea.setSize(450, 185);
        summaryTextArea.setFont(ROBOTO_BUTTON);
        summaryTextArea.setLineWrap(true);

        //Default confirmButton stuff
        confirmButton = new JButton("Confirm");
        confirmButton.setSize(125, 75);
        confirmButton.setLocation(485, 295);
        confirmButton.addActionListener(this);
        confirmButton.setFont(ROBOTO_BUTTON);
        confirmButton.setFocusable(false);

        //Default dollarSymbol stuff
        dollarSymbol = new JLabel();
        dollarSymbol.setIcon(new ImageIcon("resources\\default_icon.png"));
        dollarSymbol.setSize(95, 95);
        dollarSymbol.setLocation(35, 85);

        /*
        Adds the default components to the array list, the array list approach is taken so that other classes which
        extend this one may easily add components without needing to explicitly add them to the frame, instead a
        class merely has to register the component, and everything else is taken care of later in the showFrame()
        method.
        */
        components.add(headingText);
        components.add(currencyTextField);
        components.add(summaryTextArea);
        components.add(confirmButton);
        components.add(dollarSymbol);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLocation(this.getX(), 85);
    }

    //Any class that extends this one should call this method just before the frame is to be drawn to the screen, so
    //that any and all components are added before the frame is made visible to the user.
    protected void showFrame(){
        for(Component component : components){
            this.add(component);
        }

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
