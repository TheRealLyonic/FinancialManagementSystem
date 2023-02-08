import com.dropbox.core.*;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.GetMetadataErrorException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Login extends JFrame implements Colors, Fonts, ActionListener, APIInfo{

    private String firstName, lastName;
    private static String username;
    private char[] password;
    private JLabel usernameText, passwordText;
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JButton loginButton, createNewAccountButton;
    private final Serializer SERIALIZER = new Serializer();
    private static DbxClientV2 dbxClient;
    private DbxCredential dbxCredential;

    Login() throws DbxException {
        dbxCredential = new DbxCredential(DBX_ACCESS_TOKEN, 5000l, DBX_REFRESH_TOKEN, DBX_CLIENT_ID, DBX_APP_SECRET);

        //Gets a new, valid access token
        dbxCredential.refresh(new DbxRequestConfig(DBX_CLIENT_ID));

        dbxClient = new DbxClientV2(new DbxRequestConfig(DBX_CLIENT_ID), dbxCredential);

        //JFrame Stuff
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

                if(usernameTextField.getText().isBlank()){

                    if(passwordTextField.getBackground() == ERROR_RED && passwordTextField.getPassword().length != 0){
                        devisualizeError(passwordTextField);
                    }else if(passwordTextField.getBackground() != ERROR_RED && passwordTextField.getPassword().length == 0){
                        visualizeError(passwordTextField);
                    }

                    visualizeError(usernameTextField);
                }else if(passwordTextField.getPassword().length == 0){

                    if(usernameTextField.getBackground() == ERROR_RED){
                        devisualizeError(usernameTextField);
                    }

                    visualizeError(passwordTextField);
                }else{

                    if(usernameTextField.getBackground() == ERROR_RED){
                        devisualizeError(usernameTextField);
                    }

                    boolean loginSuccessful = authenticateUser(usernameTextField.getText(), passwordTextField.getPassword());

                    if(loginSuccessful){
                        this.dispose();
                        new DownloadSpreadsheetPrompt();
                    }
                }

            } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException |
                     ClassNotFoundException | DbxException ex) {
                throw new RuntimeException(ex);
            }
        }else if(e.getSource() == createNewAccountButton){
            this.dispose();
            new AccountCreator();
        }
    }

    public void signUp() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, ClassNotFoundException, DbxException {

        if(getUser(username) != null){
            JOptionPane.showMessageDialog(this, "I'm sorry, that username is taken. Please try again using a different username.", "Username Taken", JOptionPane.ERROR_MESSAGE);
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

    public boolean authenticateUser(String givenUsername, char[] givenPassword) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, ClassNotFoundException, DbxException {
        User user = getUser(givenUsername);

        if(user == null){

            if(passwordTextField.getPassword().length != 0 && passwordTextField.getBackground() == ERROR_RED){
                devisualizeError(passwordTextField);
            }

            visualizeError(usernameTextField);
            JOptionPane.showMessageDialog(this, "I'm sorry, the username you entered does not exist in our database.", "Invalid Username", JOptionPane.ERROR_MESSAGE);
            return false;
        }else{
            String salt = user.getSalt();
            String calculatedHash = getEncryptedPassword(givenPassword, salt);

            //Verifies that the entered password is correct
            if(calculatedHash.equals(user.getEncryptedPassword())){
                //The username assignment here is so that we can get the current user's username later in other parts
                //of the program. Also important that this is assigned only *AFTER* credentials have been verified.
                username = user.getUsername();
                return true;
            }else{
                visualizeError(passwordTextField);
                JOptionPane.showMessageDialog(this, "I'm sorry, that password is incorrect. Please try again, or reset your password.", "Invalid Password", JOptionPane.ERROR_MESSAGE);
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

    /*
    This method will search the database for a directory with a title that matches the given username, if it finds
    such a directory, it'll retrieve the UserInfo.ser file from that user's directory and use it to make a User
    object with that user's details. This file will promptly be deleted from the user's computer as soon as the
    necessary details are obtained.
    */
    private User getUser(String username) throws IOException, ClassNotFoundException, DbxException {
        checkIfExpired();

        String serverPath = "/" + username + "/UserInfo.ser";

        if(doesFileExist(serverPath)){
            OutputStream downloadFile = new FileOutputStream("resources/UserInfo.ser");

            dbxClient.files().downloadBuilder(serverPath).download(downloadFile);

            downloadFile.close();

            User foundUser = (User) SERIALIZER.deserializeObject("resources/UserInfo.ser");

            new File("resources/UserInfo.ser").delete();

            return foundUser;
        }else{
            return null;
        }
    }

    private boolean doesFileExist(String path) throws DbxException {
        checkIfExpired();

        try{
            dbxClient.files().getMetadata(path);
        } catch (GetMetadataErrorException e) {
            if(e.errorValue.isPath()){
                if(e.errorValue.getPathValue().isNotFound()){
                    return false;
                }
            }
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private void saveUser(User user) throws IOException, DbxException {
        checkIfExpired();

        //Add the user into the database
        String serverPath = "/" + user.getUsername() + "/UserInfo.ser";

        SERIALIZER.SerializeObject(user, "resources/UserInfo.ser");

        InputStream uploadFile = new FileInputStream("resources/UserInfo.ser");

        dbxClient.files().uploadBuilder(serverPath).uploadAndFinish(uploadFile);

        uploadFile.close();
    }

    private void checkIfExpired() throws DbxException {
        if(dbxCredential.aboutToExpire()){
            dbxClient.refreshAccessToken();
        }
    }

    public static void visualizeError(JTextField textField){
        textField.setBackground(ERROR_RED);
        textField.setForeground(STANDARD_WHITE);
    }

    public static void devisualizeError(JTextField textField){
        textField.setBackground(STANDARD_WHITE);
        textField.setForeground(STANDARD_BLACK);
    }

    //Getters + Setters
    public static String getUsername(){
        return username;
    }

    public static DbxClientV2 getDbxClient(){
        return dbxClient;
    }

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