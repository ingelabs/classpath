package javax.swing;

import java.awt.*;

public class JLayeredPane extends JComponent
{
    JLayeredPane()
    {
    }

    
    protected void addImpl(Component comp, Object constraints, int index) 
    {        
        super.addImpl(comp, constraints, index);
	
        comp.validate();
        comp.repaint();
    }
    
  
}
