import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ColoredCellRenderer extends DefaultTableCellRenderer {

    private Color cellColor;

    ColoredCellRenderer(Color cellColor){
        super();
        this.cellColor = cellColor;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setForeground(cellColor);
        return cell;
    }

}
