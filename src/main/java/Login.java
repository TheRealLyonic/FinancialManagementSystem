import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.ParseException;
import java.util.Base64;

public class Login extends JFrame implements Colors, Fonts, ActionListener {

    private String firstName, lastName;
    private String username;
    private char[] password;
    private JLabel usernameText, passwordText;
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JButton loginButton, createNewAccountButton;
    private final Serializer SERIALIZER = new Serializer();

    Login(){
        this.setTitle("Login");
        this.setSize(625, 500);
        this.setIconImage(new ImageIcon("resources/login_icon.png").getImage());
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(LOGIN_GRAY);

        //Username text stuff
        usernameText = new JLabel("Username:");
        usernameText.setSize(245, 95);
        usernameText.setLocation(8, 4);
        usernameText.setFont(ROBOTO_MEDIUM);

        //Password text stuff
        passwordText = new JLabel("Password:");
        passwordText.setSize(245, 95);
        passwordText.setLocation(8, 105);
        passwordText.setFont(ROBOTO_MEDIUM);

        //Username text field stuff
        usernameTextField = new JTextField();
        usernameTextField.setSize(350, 65);
        usernameTextField.setLocation(250, 20);
        usernameTextField.setFont(ROBOTO_BUTTON);

        //Password text field stuff
        passwordTextField = new JPasswordField();
        passwordTextField.setSize(350, 65);
        passwordTextField.setLocation(250, 121);
        passwordTextField.setFont(ROBOTO_BUTTON);
        passwordTextField.setEchoChar('*');

        //Login Button stuff
        loginButton = new JButton("Login");
        loginButton.setSize(95, 85);
        loginButton.setLocation(505, 200);
        loginButton.setFont(ROBOTO_BUTTON);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        //Create new account button stuff
        createNewAccountButton = new JButton("Create new account");
        createNewAccountButton.setSize(210, 85);
        createNewAccountButton.setLocation(10, 360);
        createNewAccountButton.setFont(ROBOTO_LINK);
        createNewAccountButton.setFocusable(false);
        createNewAccountButton.setForeground(LINK_BLUE);
        createNewAccountButton.addActionListener(this);


        this.add(usernameText);
        this.add(passwordText);
        this.add(usernameTextField);
        this.add(passwordTextField);
        this.add(loginButton);
        this.add(createNewAccountButton);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == loginButton){
            try {
                boolean loginSuccessful = authenticateUser(usernameTextField.getText(), passwordTextField.getPassword());

                if(loginSuccessful){
                    //Download spreadsheet from user's directory
                    new FinancialManagementSystem();
                }

            } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException | ParseException |
                     ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }else if(e.getSource() == createNewAccountButton){
            this.dispose();
            new AccountCreator();
        }
    }

    public void signUp() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, ClassNotFoundException {

        if(getUser(username) != null){
            //Display error saying that username already exists
        }else{
            String salt = getNewSalt();
            String encryptedPassword = getEncryptedPassword(password, salt);

            //Creates a new user with the desired information
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEncryptedPassword(encryptedPassword);
            user.setUsername(username);
            user.setSalt(salt);

            //Creates a new entry for the user in the database
            saveUser(user);
        }

    }

    public boolean authenticateUser(String givenUsername, char[] givenPassword) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, ClassNotFoundException {
        User user = getUser(givenUsername);

        if(user == null){
            //Display GUI for nonexistent user.
            return false;
        }else{
            String salt = user.getSalt();
            String calculatedHash = getEncryptedPassword(givenPassword, salt);

            //Verifies that the entered password is correct
            if(calculatedHash.equals(user.getEncryptedPassword())){
                return true;
            }else{
                //Display GUI for incorrect password.
                return false;
            }

        }

    }

    //Note: If a user wishes to change their password, they must be given a new salt.
    private String getNewSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String getEncryptedPassword(char[] password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String ALGORITHM = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 20000;

        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(password, saltBytes, iterations, derivedKeyLength);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);

        byte[] encodedBytes = secretKeyFactory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(encodedBytes);
    }

    private User getUser(String username) throws IOException, ClassNotFoundException {
        //Get a user from the database using the given username

        return (User) SERIALIZER.deserializeObject("resources/UserInfo.ser");
    }

    private void saveUser(User user) throws IOException {
        //Add the user into the database
        SERIALIZER.SerializeObject(user, "resources/UserInfo.ser");
    }

    //Getters + Setters
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(char[] password){
        this.password = password;
    }

}