package javax.swing;

import java.awt.*;

public interface ListCellRenderer
{
    public Component getListCellRendererComponent(JList list,
						  Object value,
						  int index,
						  boolean isSelected,
						  boolean cellHasFocus);
}
