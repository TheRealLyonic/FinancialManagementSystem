import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;

public class ViewActiveScheduledPayments extends JFrame implements Colors, Fonts{

    private ArrayList<ScheduledPayment> scheduledPayments;
    private final String[] COLUMN_NAMES = {
            "Cost",
            "Last Payment Date",
            "Next Payment Date",
            "Payment Summary"
    };
    private Object[][] data;
    private JScrollPane tableContainer;
    private JTable scheduledPaymentsTable;

    ViewActiveScheduledPayments(ArrayList<ScheduledPayment> scheduledPayments){
        //!Basic JFrame Stuff!
        this.setTitle("View Active Scheduled Payments");
        this.setIconImage(new ImageIcon("resources\\view_active_scheduled_payments_window_icon.png").getImage());
        this.setSize(710, 620);
        this.getContentPane().setBackground(FINANCIAL_ORANGE);
        this.setLayout(null);

        //!ScheduledPayments stuff!
        this.scheduledPayments = scheduledPayments;
        data = new Object[scheduledPayments.size()][4];
        updateData();

        //!scheduledPaymentsTable stuff!
        scheduledPaymentsTable = new JTable(data, COLUMN_NAMES);
        scheduledPaymentsTable.setBackground(Color.black);
        scheduledPaymentsTable.setForeground(SUBDUED_WHITE);
        scheduledPaymentsTable.setFont(ROBOTO_BUTTON);
        scheduledPaymentsTable.setEnabled(false);
        scheduledPaymentsTable.getTableHeader().setReorderingAllowed(false);

        //!TableContainer stuff!
        tableContainer = new JScrollPane(scheduledPaymentsTable);
        tableContainer.setLocation(30, 10);
        tableContainer.setSize(625, 550);
        scheduledPaymentsTable.setFillsViewportHeight(true);

        //!Table layout stuff!
        TableColumn column;
        TableCellRenderer cellRenderer;
        int width, preferredWidth, maxWidth;
        Component component;

        if(!scheduledPayments.isEmpty()){
            scheduledPaymentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            //Weird algorithm for adjusting column width dynamically to fit the content.
            for(int i = 0; i < scheduledPaymentsTable.getColumnCount(); i++){
                column = scheduledPaymentsTable.getColumnModel().getColumn(i);
                preferredWidth = column.getMinWidth();
                maxWidth = column.getMaxWidth();

                for(int j = 0; j < scheduledPaymentsTable.getRowCount(); j++){
                    cellRenderer = scheduledPaymentsTable.getCellRenderer(j, i);
                    component = scheduledPaymentsTable.prepareRenderer(cellRenderer, j, i);
                    width = component.getPreferredSize().width + scheduledPaymentsTable.getIntercellSpacing().width;
                    preferredWidth = Math.max(preferredWidth, width);

                    if(preferredWidth >= maxWidth){
                        preferredWidth = maxWidth;
                        break;
                    }
                }

                column.setPreferredWidth(preferredWidth + 40);
            }
        }

        this.add(tableContainer);
        this.setLocationRelativeTo(null);
        this.setLocation(this.getX(), 85);
        this.setVisible(true);
    }

    private void updateData(){
        for(int i = 0; i < data.length; i++){
            for(int j = 0; j < data[i].length; j++){
                data[i][j] = scheduledPayments.get(i).getPaymentInformation()[j];
            }
        }
    }

}
