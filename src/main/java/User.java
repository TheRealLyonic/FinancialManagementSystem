import java.io.Serializable;

public class User implements Serializable {

    private String firstName, lastName;
    private String encryptedPassword;
    private String salt;
    private String username;

    //Getters + Setters
    public String getEncryptedPassword(){
        return encryptedPassword;
    }

    public String getSalt(){
        return salt;
    }

    public String getUsername(){
        return username;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setEncryptedPassword(String encryptedPassword){
        this.encryptedPassword = encryptedPassword;
    }

    public void setSalt(String salt){
        this.salt = salt;
    }

    public void setUsername(String username){
        this.username = username;
    }

}