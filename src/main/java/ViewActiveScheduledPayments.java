import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ViewActiveScheduledPayments extends JFrame implements Colors, Fonts, ActionListener {

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
    private JButton[] buttons;

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
        buttons = new JButton[scheduledPayments.size()];
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
        tableContainer.setLocation(62, 10);
        tableContainer.setSize(625, 550);
        scheduledPaymentsTable.setFillsViewportHeight(true);

        //!Table layout stuff!
        TableColumn column;
        TableCellRenderer cellRenderer;
        int width, preferredWidth, maxWidth, minWidth;
        Component component;

        if(!scheduledPayments.isEmpty()){
            scheduledPaymentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            //Weird algorithm for adjusting column width dynamically to fit the content.
            for(int i = 0; i < scheduledPaymentsTable.getColumnCount(); i++){
                column = scheduledPaymentsTable.getColumnModel().getColumn(i);
                preferredWidth = column.getMinWidth();
                maxWidth = column.getMaxWidth();
                minWidth = 120;

                for(int j = 0; j < scheduledPaymentsTable.getRowCount(); j++){
                    cellRenderer = scheduledPaymentsTable.getCellRenderer(j, i);
                    component = scheduledPaymentsTable.prepareRenderer(cellRenderer, j, i);
                    width = component.getPreferredSize().width + scheduledPaymentsTable.getIntercellSpacing().width;
                    preferredWidth = Math.max(preferredWidth, width);

                    if(preferredWidth >= maxWidth){
                        preferredWidth = maxWidth;
                        break;
                    }else if(preferredWidth <= minWidth){
                        preferredWidth = minWidth;
                        break;
                    }
                }

                column.setPreferredWidth(preferredWidth + 40);
            }
        }

        //!Delete JButtons Stuff!
        for(int i = 0; i < buttons.length; i++){
            buttons[i] = new JButton();
            buttons[i].setIcon(new ImageIcon("resources\\delete_button_icon.png"));
            buttons[i].setFocusable(false);
            buttons[i].setSize(22, 15);
            buttons[i].setOpaque(false);
            buttons[i].setContentAreaFilled(false);
            buttons[i].addActionListener(this);

            if(i == 0){
                buttons[i].setLocation(35, scheduledPaymentsTable.getY() + 30);
            }else{
                buttons[i].setLocation(buttons[i - 1].getX(), buttons[i - 1].getY() + 17);
            }

            this.add(buttons[i]);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        for(int i = 0; i < buttons.length; i++){
            if(e.getSource().equals(buttons[i])){
                try {
                    FinancialManagementSystem.deleteScheduledPayment("serialized\\scheduled_payment_" + i + ".ser");
                    this.dispose();
                    FinancialManagementSystem.updateScheduledPayments();
                    new ViewActiveScheduledPayments(FinancialManagementSystem.getScheduledPayments());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    UserInterface.showErrorMessage("I/o Exception", "ERROR: An I/o exception " +
                            "has occurred.");
                } catch (ClassNotFoundException ex) {
                    UserInterface.showErrorMessage("Class Not Found", "ERROR: A Class Not Found " +
                            "Exception has occurred.");
                }
            }
        }
    }
}
