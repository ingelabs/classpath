package javax.swing;

import java.awt.*;


// this is what SUN basically told us to do so:
// no icon though as that's not implemented yet....

public class DefaultCellRenderer extends JLabel implements ListCellRenderer 
{
    public Component getListCellRendererComponent(JList list,
						  Object value,           
						  int index,            
						  boolean isSelected,     
						  boolean cellHasFocus)   
    {
	String s = value.toString();
	setText(s);	

	//	System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=" + s);


	if (isSelected) 
	    {
		setBackground(list.getSelectionBackground());
		setForeground(list.getSelectionForeground());
	    }
	else 
	    {
		setBackground(list.getBackground());
		setForeground(list.getForeground());
	    }

	setEnabled(list.isEnabled());
	setFont(list.getFont());
	return this;
    }
}
