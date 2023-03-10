import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.io.Serializable;

public class CurrencyTextFilter extends DocumentFilter implements Serializable {

    private int maxLength = 0;

    @Override
    public void insertString(FilterBypass filterBypass, int offset, String string, AttributeSet attributeSet) throws BadLocationException {

        boolean formatted;
        try{
            Double.valueOf(string);
            formatted = true;
        }catch(NumberFormatException e){
            formatted = false;
        }

        if( !string.equals(" ") && formatted || (((string.equals("."))) && !filterBypass.getDocument().getText(0, filterBypass.getDocument().getLength()).contains(".")) ){

            if(string.equals(".")){
                maxLength = filterBypass.getDocument().getLength() + 3;
            }else if(!filterBypass.getDocument().getText(0, filterBypass.getDocument().getLength()).contains(".") && maxLength != 0){
                maxLength = 0;
            }else if(maxLength > 0){
                if( (filterBypass.getDocument().getLength() + string.length()) > maxLength){
                    return;
                }
            }

            super.insertString(filterBypass, offset, string, attributeSet);
        }
    }

    @Override
    public void replace(FilterBypass filterBypass, int offset, int length, String string, AttributeSet attributeSet) throws BadLocationException{

        boolean formatted;

        try{
            Double.valueOf(string);
            formatted = true;
        }catch(NumberFormatException e){
            formatted = false;
        }

        if( !string.equals(" ") && formatted || ((string.equals("."))) && !filterBypass.getDocument().getText(0, filterBypass.getDocument().getLength()).contains(".") ){

            if(string.equals(".")){
                maxLength = filterBypass.getDocument().getLength() + 3;
            }else if(!filterBypass.getDocument().getText(0, filterBypass.getDocument().getLength()).contains(".") && maxLength != 0){
                maxLength = 0;
            }else if(maxLength > 0){
                if( (filterBypass.getDocument().getLength() + string.length()) > maxLength){
                    return;
                }
            }

            //Fixes a small bug encountered where you could past in decimals, resulting in more than one '.' in the
            //text field, which would obviously throw a NumberFormatException.
            if(string.contains(".") && filterBypass.getDocument().getText(0, filterBypass.getDocument().getLength()).contains(".")){
                return;
            }

            super.replace(filterBypass, offset, length, string, attributeSet);
        }
    }

}
