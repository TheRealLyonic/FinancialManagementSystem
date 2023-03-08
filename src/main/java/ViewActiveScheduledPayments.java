import javax.swing.*;
import java.util.ArrayList;

public class ViewActiveScheduledPayments extends JFrame implements Colors, Fonts{

    private ArrayList<ScheduledPayment> scheduledPayments;

    ViewActiveScheduledPayments(ArrayList<ScheduledPayment> scheduledPayments){
        //!Basic JFrame Stuff!
        this.setTitle("Active Scheduled Payments");
        this.setIconImage(new ImageIcon("resources\\").getImage());
        this.setSize(710, 620);
        this.getContentPane().setBackground(FINANCIAL_ORANGE);

        this.scheduledPayments = scheduledPayments;


        this.setVisible(true);
    }

}
