import javax.swing.*;
import java.awt.event.ActionListener;

public class MenuOption extends JLabel implements Fonts, Colors{

    private JButton optionButton;
    private JLabel optionText;

    MenuOption(JFrame frame, int xPos, int yPos, String optionName, Icon optionIcon){
        this.setSize(frame.getWidth(), frame.getHeight());
        this.setLocation(0, 0);

        //!Button stuff!
        optionButton = new JButton();
        optionButton.setIcon(optionIcon);
        optionButton.setFocusable(false);
        optionButton.setLocation(xPos, yPos);
        optionButton.setSize(55, 55);
        optionButton.setOpaque(false);
        optionButton.setContentAreaFilled(false);

            //Tries to automatically add the given frame as the action listener to the JButton
        try{
            optionButton.addActionListener((ActionListener) frame);
        }catch(ClassCastException e){
            JOptionPane.showMessageDialog(frame, "ERROR: Class that tried to create a new MenuOption does " +
                    "not implement ActionListener.", "No Action Listener", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        //!Text stuff!
        optionText = new JLabel(optionName);
        optionText.setFont(ROBOTO_SMALL);
        optionText.setForeground(SUBDUED_WHITE);
        optionText.setLocation(xPos + 65, yPos - 2);
        optionText.setSize(optionText.getPreferredSize());
        optionText.setSize(optionText.getWidth() + 15, optionText.getHeight() + 15);

        this.add(optionButton);
        this.add(optionText);
    }

    //Getters + Setters
    public JButton getOptionButton(){
        return optionButton;
    }

}
