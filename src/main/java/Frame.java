import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Frame extends JFrame {

    Frame(){
        this.setTitle("Financial Management System");
        this.setSize(650, 650);
        this.setIconImage(new ImageIcon("resources/icon.png").getImage());
        this.setLayout(null);
        this.getContentPane().setBackground(Color.gray);

        //Pie-Chart stuff, the Dataset hashmap is used as reference for what to add to the pie-chart later
        HashMap dataSet = new HashMap();
        dataSet.put("Saved (62%)", 62);
        dataSet.put("Spent (38%)", 38);
        PieChart pieChart = new PieChart("Weekly Update", dataSet, 50, 50, 350, 350);

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(pieChart.getChartPanel());
        this.setVisible(true);
    }

}
