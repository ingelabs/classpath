package javax.swing.border;

import java.awt.*;

public interface Border
{
    public Insets getBorderInsets(Component c);
    public boolean isBorderOpaque();
    public void paintBorder(Component c, 
			    Graphics g, 
			    int x, 
			    int y,
			    int width,
			    int height);
}
