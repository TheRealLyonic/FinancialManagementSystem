import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;

public class Main implements APIInfo{

    public static void main(String[] args) throws IOException, ParseException, DbxException {



        //Logic + GUI for getting username and password goes here

        new Login();

        //We can use the startUp file to see if this is the program's first time running, and if it is, we can prompt
        //the user to enter a username and password to associate their spreadsheet with on the server. We should also
        // check to see if this username already exists, and if it does, prompt them to enter the password and login
        //as opposed to creating a new account.

        //Also provide the option to opt-out of using the server, if they plan to only use the program on one device,
        //or don't care about having it linked between devices

        //SIMPLE BUTTON ASKING THE USER IF THEY WOULD LIKE TO DOWNLOAD THE MOST RECENT VERSION OF THE SPREADSHEET
        //FROM THE SERVER, *UPLOAD THE CURRENT VERSION OF THE SPREADSHEET, OR NEITHER
        //*ADD VERY OBVIOUS WARNING THAT ONCE THIS IS DONE, THE PREVIOUS VERSION OF THE SPREADSHEET THAT WAS ON THE
        //SERVER CAN NOT BE RECOVERED

//        OutputStream downloadFile = new FileOutputStream("C:\\Users\\rwbyf\\Desktop\\ruby.png");
//
//        DbxClientV2 client = new DbxClientV2(new DbxRequestConfig(DBX_CLIENT_ID), new DbxCredential(DBX_ACCESS_TOKEN));
//
//        client.files().downloadBuilder("/Qrow.png").download(downloadFile);
//
//        downloadFile.close();
    }

}