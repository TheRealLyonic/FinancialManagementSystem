import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;

public class SpreadsheetDownloader extends JFrame implements Colors, Fonts, ActionListener {

    protected JLabel questionText, warningText;
    protected JButton yesButton, noButton;
    protected final DbxClientV2 dbxClient = Login.getDbxClient();

    SpreadsheetDownloader(){
        this.setTitle("Spreadsheet Downloader");
        this.setSize(710, 510);
        this.setIconImage(new ImageIcon("resources/download_spreadsheet_icon.png").getImage());
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(LOGIN_GRAY);

        //Question text stuff
        questionText = new JLabel("<html>Would you like to download the last uploaded version of this year's spreadsheet" +
                " from the database?</html>");
        questionText.setFont(ROBOTO_SMALL);
        questionText.setLocation(100, 0);
        questionText.setSize(565, 185);

        //Warning text stuff
        warningText = new JLabel("<html>WARNING: This will over-write the current version of this year's spreadsheet " +
                "stored on this device.</html>");
        warningText.setFont(ROBOTO_BUTTON);
        warningText.setForeground(ERROR_RED);
        warningText.setLocation(195, 175);
        warningText.setSize(335, 125);

        //Yes button stuff
        yesButton = new JButton("Yes");
        yesButton.setLocation(85, 310);
        yesButton.setSize(185, 125);
        yesButton.setFont(ROBOTO_MEDIUM);
        yesButton.setFocusable(false);
        yesButton.addActionListener(this);

        //No button stuff
        noButton = new JButton("No");
        noButton.setLocation(455, 310);
        noButton.setSize(185, 125);
        noButton.setFont(ROBOTO_MEDIUM);
        noButton.setFocusable(false);
        noButton.addActionListener(this);

        this.add(questionText);
        this.add(warningText);
        this.add(yesButton);
        this.add(noButton);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == yesButton){
            try {

                //Deletes the outdated spreadsheet
                File oldSpreadsheet = new File("data\\" + LocalDate.now().getYear() + ".xlsx");

                if(oldSpreadsheet.exists()){
                    oldSpreadsheet.delete();
                }

                //Verifies that the API credentials are still valid
                Login.checkIfExpired(Login.getDbxCredential(), dbxClient);

                //Assigns the path to the user's spreadsheet on the server and where to download that file to
                String spreadsheetPath = "/" + Login.getUsername() + "/" + LocalDate.now().getYear() + ".xlsx";
                OutputStream downloadSpreadsheet = new FileOutputStream("data\\" + LocalDate.now().getYear() + ".xlsx");

                dbxClient.files().downloadBuilder(spreadsheetPath).download(downloadSpreadsheet);

                this.dispose();
                new FinancialManagementSystem();
            } catch (DbxException | IOException | ParseException ex) {
                Login.displayException(this, "IO");
                throw new RuntimeException(ex);
            }
        }else if(e.getSource() == noButton){
            try {
                this.dispose();
                new FinancialManagementSystem();
            } catch (IOException | ParseException ex) {
                Login.displayException(this, "IO");
                throw new RuntimeException(ex);
            }
        }
    }
}
