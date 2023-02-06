import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class AccountCreator extends JFrame implements Colors, Fonts, ActionListener {

    private JLabel firstNameText, lastNameText, usernameText, passwordText;
    private JTextField firstNameTextField, lastNameTextField, usernameTextField;
    private JPasswordField passwordTextField;
    private JButton confirmButton, backButton;

    AccountCreator(){
        this.setTitle("Account Creator");
        this.setSize(650, 650);
        this.setIconImage(new ImageIcon("resources/account_creator_icon.png").getImage());
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(LOGIN_BLUE);

        //First name text stuff
        firstNameText = new JLabel("First Name:");
        firstNameText.setSize(280, 95);
        firstNameText.setLocation(15, 3);
        firstNameText.setFont(ROBOTO_MEDIUM);

        //First name text field stuff
        firstNameTextField = new JTextField();
        firstNameTextField.setSize(250, 50);
        firstNameTextField.setLocation(265, 28);
        firstNameTextField.setFont(ROBOTO_BUTTON);

        //Last name text stuff
        lastNameText = new JLabel("Last Name:");
        lastNameText.setSize(280, 64);
        lastNameText.setLocation(15, 90);
        lastNameText.setFont(ROBOTO_MEDIUM);

        //Last name text field stuff
        lastNameTextField = new JTextField();
        lastNameTextField.setSize(250, 50);
        lastNameTextField.setLocation(265, 100);
        lastNameTextField.setFont(ROBOTO_BUTTON);

        //Username text stuff
        usernameText = new JLabel("Username:");
        usernameText.setSize(245, 95);
        usernameText.setLocation(15, 150);
        usernameText.setFont(ROBOTO_MEDIUM);

        //Username text field stuff
        usernameTextField = new JTextField();
        usernameTextField.setSize(335, 50);
        usernameTextField.setLocation(255, 176);
        usernameTextField.setFont(ROBOTO_BUTTON);

        //Password text stuff
        passwordText = new JLabel("Password:");
        passwordText.setSize(245, 95);
        passwordText.setLocation(15, 229);
        passwordText.setFont(ROBOTO_MEDIUM);

        //Password text field stuff
        passwordTextField = new JPasswordField();
        passwordTextField.setSize(335, 50);
        passwordTextField.setLocation(255, 255);
        passwordTextField.setFont(ROBOTO_BUTTON);
        passwordTextField.setEchoChar('*');

        //Confirm button stuff
        confirmButton = new JButton("Confirm");
        confirmButton.setSize(125, 90);
        confirmButton.setLocation(325, 325);
        confirmButton.setFont(ROBOTO_BUTTON);
        confirmButton.setFocusable(false);
        confirmButton.addActionListener(this);

        //Back button stuff
        backButton = new JButton("Back");
        backButton.setSize(125, 90);
        backButton.setLocation(10, 505);
        backButton.setFont(ROBOTO_BUTTON);
        backButton.setFocusable(false);
        backButton.setForeground(BACK_RED);
        backButton.addActionListener(this);

        this.add(firstNameText);
        this.add(firstNameTextField);
        this.add(lastNameText);
        this.add(lastNameTextField);
        this.add(usernameText);
        this.add(usernameTextField);
        this.add(passwordText);
        this.add(passwordTextField);
        this.add(confirmButton);
        this.add(backButton);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == confirmButton){
            this.dispose();
            Login login = new Login();

            login.setFirstName(firstNameTextField.getText());
            login.setLastName(lastNameTextField.getText());
            login.setUsername(usernameTextField.getText());
            login.setPassword(passwordTextField.getPassword());

            try {
                login.signUp();
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }else if(e.getSource() == backButton){
            this.dispose();
            new Login();
        }

    }
}
