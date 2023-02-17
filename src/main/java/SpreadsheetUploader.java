import com.dropbox.core.DbxException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

public class SpreadsheetUploader extends SpreadsheetDownloader {

    SpreadsheetUploader(){
        this.setTitle("Spreadsheet Uploader");
        this.setIconImage(new ImageIcon("resources\\upload_spreadsheet_icon.png").getImage());
        questionText.setText("<html>Would you like to upload the current version of the program's spreadsheet" +
                " to the database?</html>");
        warningText.setText("<html>WARNING: This will over-write the spreadsheet which is currently stored in " +
                "the database.</html>");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == yesButton){
            try {
                String spreadsheetServerPath = "/" + Login.getUsername() + "/" + LocalDate.now().getYear() + ".xlsx";

                Login.checkIfExpired(Login.getDbxCredential(), dbxClient);

                InputStream uploadSpreadsheet = new FileInputStream("data\\" + LocalDate.now().getYear() + ".xlsx");

                if(Login.doesFileExist(spreadsheetServerPath)){
                    dbxClient.files().deleteV2(spreadsheetServerPath);
                }

                dbxClient.files().uploadBuilder(spreadsheetServerPath).uploadAndFinish(uploadSpreadsheet);

                uploadSpreadsheet.close();

                System.exit(0);
            } catch (DbxException ex) {
                Login.displayException(this, "Dbx");
                throw new RuntimeException(ex);
            }catch (IOException ex) {
                Login.displayException(this, "IO");
                throw new RuntimeException(ex);
            }

        }else if(e.getSource() == noButton){
            System.exit(0);
        }
    }
}
