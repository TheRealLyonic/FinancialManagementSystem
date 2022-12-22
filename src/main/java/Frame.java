import javax.swing.*;
import java.util.HashMap;

public class Frame extends JFrame {

    Frame(){
        this.setTitle("Financial Management System");
        this.setSize(650, 650);
        this.setLayout(null);

        //Pie-Chart stuff
        HashMap dataSet = new HashMap();
//        dataSet.put("Saved", 62);
//        dataSet.put("Spent", 38);
//        PieChart pieChart = new PieChart("Financial Data", dataSet);

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setContentPane(pieChart.getChartPanel());
        this.setVisible(true);
    }

}
