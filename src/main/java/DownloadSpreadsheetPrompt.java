import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

public class DownloadSpreadsheetPrompt extends JFrame implements Colors, Fonts, ActionListener {

    private JButton yesButton, noButton;

    DownloadSpreadsheetPrompt(){
        this.setTitle("Spreadsheet Downloader");
        this.setSize(710, 510);
        this.setIconImage(new ImageIcon("resources/download_spreadsheet_icon.png").getImage());
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(LOGIN_GRAY);

        //Yes button stuff

        //No button stuff
        noButton = new JButton("No");
        noButton.setLocation(250, 250);
        noButton.setSize(95, 85);
        noButton.setFont(ROBOTO_BUTTON);
        noButton.addActionListener(this);

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == yesButton){
            //Download the spreadsheet
        }else if(e.getSource() == noButton){
            try {
                this.dispose();
                new FinancialManagementSystem();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
