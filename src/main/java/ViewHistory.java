import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;

public class ViewHistory extends JFrame implements Colors, Fonts{

    private ArrayList<HistoryData> historyData;
    private JTable historyTable;
    private JScrollPane tableContainer;
    private Object[][] data;
    private final String[] COLUMN_NAMES = {
            "Date",
            "Starting Balance",
            "Costs",
            "Deposits",
            "Remaining Balance",
            "Deposit Descriptions",
            "Purchase Descriptions"
    };

    ViewHistory(ArrayList<HistoryData> historyData){
        //!Basic JFrame Stuff!
        this.setTitle("View History");
        this.setIconImage(new ImageIcon("resources\\view_history_icon.png").getImage());
        this.setSize(710, 620);
        this.getContentPane().setBackground(FINANCIAL_GREEN);
        this.setLayout(null);
        this.setResizable(false);

        //!HistoryData Stuff!
        this.historyData = historyData;
        data = new Object[historyData.size()][7];
        updateData();

        //!historyTable Stuff!
        historyTable = new JTable(data, COLUMN_NAMES);
        historyTable.setBackground(DEFAULT_BLACK);
        historyTable.setForeground(SUBDUED_WHITE);
        historyTable.setFont(ROBOTO_BUTTON);
        historyTable.setEnabled(false);
        historyTable.setRowHeight(25);
        historyTable.getTableHeader().setReorderingAllowed(false);

        //!TableContainer Stuff!
        tableContainer = new JScrollPane(historyTable);
        tableContainer.setLocation(5, 5);
        tableContainer.setSize(685, 575);
        historyTable.setFillsViewportHeight(true);

        //!Table Layout Stuff!
        TableColumn column;
        TableCellRenderer cellRenderer;
        int width, preferredWidth, maxWidth, minWidth;
        Component component;

        if(!historyData.isEmpty()){
            historyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            for(int i = 0; i < historyTable.getColumnCount(); i++){
                column = historyTable.getColumnModel().getColumn(i);
                preferredWidth = column.getMinWidth();
                maxWidth = column.getMaxWidth();
                minWidth = 120;

                for(int j = 0; j < historyTable.getRowCount(); j++){
                    cellRenderer = historyTable.getCellRenderer(j, i);
                    component = historyTable.prepareRenderer(cellRenderer, j, i);
                    width = component.getPreferredSize().width + historyTable.getIntercellSpacing().width;
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

                Color columnColor;
                //Normally I'd use a switch statement for this, but the or-conditionals make it more efficient to
                //use an if here for me.
                if(i == 0){
                    columnColor = FINANCIAL_BLUE;
                }else if(i == 1 || i == 4){
                    columnColor = STANDARD_PURPLE;
                }else if(i == 2 || i == 6){
                    columnColor = NEGATIVE_RED;
                }else{
                    columnColor = FINANCIAL_GREEN;
                }

                column.setCellRenderer(new ColoredCellRenderer(columnColor));
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
                data[i][j] = historyData.get(i).getEntryInformation()[j];
            }
        }
    }

}
