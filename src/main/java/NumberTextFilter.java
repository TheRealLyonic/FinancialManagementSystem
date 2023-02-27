import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.io.Serializable;

public class NumberTextFilter extends DocumentFilter implements Serializable {

    private int maxLength;

    NumberTextFilter(int maxLength){
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(FilterBypass filterBypass, int offset, String string, AttributeSet attributeSet) throws BadLocationException {

        try{
            Double.valueOf(string);
        }catch(NumberFormatException e){
            return;
        }

        if( (filterBypass.getDocument().getLength() + string.length()) <= maxLength){
            super.insertString(filterBypass, offset, string, attributeSet);
        }
    }

    @Override
    public void replace(FilterBypass filterBypass, int offset, int length, String string, AttributeSet attributeSet) throws BadLocationException {

        try{
            Double.valueOf(string);
        }catch(NumberFormatException e){
            return;
        }

        if( ((filterBypass.getDocument().getLength() + string.length() - length) <= maxLength) && !string.contains(".")){
            super.replace(filterBypass, offset, length, string, attributeSet);
        }
    }

}
