package javax.swing.plaf.basic;

import javax.swing.text.*;
import javax.swing.plaf.*;
import java.awt.*;
import javax.swing.*;

public class BasicTextUI extends TextUI
{
    int gap = 3;
    View view = new RootView();
    Color textColor, disabledTextColor, normalBackgroundColor;
    EditorKit kit = new DefaultEditorKit();
    
    class RootView extends View
    {
	RootView()
	{
	    super(null);
	}
        public void paint(Graphics g, Shape s)
	{
	    if (view != null)
		{
		    Rectangle r = s.getBounds();

		    view.setSize(r.width,
				 r.height);
		    view.paint(g, s);
		}
        }
    }

    BasicTextUI()
    {
    }

    public static ComponentUI createUI(final JComponent c) 
    {
	return new BasicTextUI();
    }

    
    public void installUI(final JComponent c) 
    {
	super.installUI(c);

	textColor                = new Color(0,0,0);
	disabledTextColor        = new Color(130, 130, 130);
	normalBackgroundColor    = new Color(192,192,192);
    }
    
    public Dimension getPreferredSize(JComponent c) 
    {
	JTextComponent b = (JTextComponent) c;

	View v = getRootView(b);

	float w = v.getPreferredSpan(View.X_AXIS);
	float h = v.getPreferredSpan(View.Y_AXIS);

	return new Dimension(w, h);
    }
    

    void paint(Graphics g, JComponent c)
    {      
	//	view.paint(
    }

    void damageRange(JTextComponent t, int p0, int p1)
    {
	damageRange(t, p0, p1, null, null);
    }    

    void damageRange(JTextComponent t, 
		     int p0, int p1, 
		     Position.Bias firstBias,
		     Position.Bias secondBias)
    {
    }

    EditorKit getEditorKit(JTextComponent t)
    {
	return kit;
    }
    
    int getNextVisualPositionFrom(JTextComponent t, 
				  int pos,
				  Position.Bias b, 
				  int direction,
				  Position.Bias[] biasRet)
    {
	return 0;
    }
    
    View getRootView(JTextComponent t)
    {
	return view;
    }
    
    Rectangle modelToView(JTextComponent t, int pos)
    {
	return modelToView(t, pos, null);
    }
    
    Rectangle modelToView(JTextComponent t, int pos, Position.Bias bias)
    {
	return null;
    }
    
    int viewToModel(JTextComponent t, Point pt)
    {
	return viewToModel(t, pt, null);
    }
    
    int viewToModel(JTextComponent t, Point pt, Position.Bias[] biasReturn)
    {
	return 0;
    } 
}





