package javax.swing;

import java.awt.*;
import javax.swing.plaf.*;
import javax.accessibility.*;

public class JTree extends JComponent implements Scrollable, Accessible
{
    JTree()
    {
	updateUI();
    }
    
    public TreeUI getUI()
    {
        return (TreeUI) ui;
    }

    public void setUI(TreeUI ui)
    {
        super.setUI(ui);
    }

    public void updateUI()
    {
        setUI((TreeUI)UIManager.getUI(this));
    }

    
    public String getUIClassID()
    {
	return "JTree";
    }


    public AccessibleContext getAccessibleContext()
    {
      return null;
    }

    public Dimension getPreferredScrollableViewportSize()
    {
	return null;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect,
					  int orientation,
					  int direction)
    {
	return 1;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect,
					   int orientation,
					   int direction)
    {
	return 1;
    }

    public boolean getScrollableTracksViewportWidth()
    {
	return false;
    }

    public boolean getScrollableTracksViewportHeight()
    {
	return false;
    }
}



