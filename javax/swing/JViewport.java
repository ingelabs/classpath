package javax.swing;

import javax.swing.plaf.*;
import java.awt.*;


public class JViewport extends JComponent
{
    Component c;

    JViewport()
    {
	setOpaque(true);
	updateUI();
    }

    void setView(Component c)
    {
	if (this.c != null)
	    remove(c);

	this.c = c;

	add(c);
    }

    public String getUIClassID()
    {
	return "JViewport";
    }

    public void updateUI()
    {
	ViewportUI vp = (ViewportUI) UIManager.getUI(this);
	setUI(vp);
    }

    Container GetHeavy(Container parent)
    {
	if (parent == null)
	    return null;

	while (isLightweightComponent(parent))
	    {
		Container p = parent.getParent();

		if (p == null)
		    {
			System.out.println("GetHeavy FAILED, no heavy weight component found");
			return parent;
		    }
		
		parent = p;
	    }
	return parent;
    }
    
    
    public void paint(Graphics g)
    {
	paintChildren(g);

	System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXX   JViewport -----> paint()");

	Container parent = GetHeavy(getParent());
	
	System.out.println("parent = " + parent + ", " + getParent());

	//parent.paint();

	Graphics wg = parent.getGraphics();
	
	int x = 0;
	int y = 0;
	int w = getWidth();
	int h = getHeight();

	Rectangle r = new Rectangle(x, y, w, h);

	int ox = 0;
	int oy = 0;

	wg.copyArea(r.x,
		    r.y,
		    r.width,
		    r.height,
		    ox,
		    oy);

	wg.dispose();
    }
}







