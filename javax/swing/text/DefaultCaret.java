package javax.swing.text;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;


public class DefaultCaret extends Rectangle implements Caret, FocusListener, MouseListener, MouseMotionListener
{
    Color color = new Color(0,0,0);
    JTextComponent parent;
    
    public void mouseDragged(java.awt.event.MouseEvent  evt)
    {
    }

    public void mouseMoved(java.awt.event.MouseEvent  evt)
    {
    }

    public void mouseClicked(java.awt.event.MouseEvent  evt)
    {
    }

    public void mouseEntered(java.awt.event.MouseEvent  evt)
    {
    }

    public void mouseExited(java.awt.event.MouseEvent  evt)
    {
    }

    public void mousePressed(java.awt.event.MouseEvent  evt)
    {
    }

    public void mouseReleased(java.awt.event.MouseEvent  evt)
    {
    }

    public void focusGained(java.awt.event.FocusEvent  evt)
    {
    }

    public void focusLost(java.awt.event.FocusEvent  evt)
    {
    }

    // caret methods:

    public void deinstall(JTextComponent c)
    {
	parent.removeFocusListener(this);
	parent.removeMouseListener(this);

	parent = null;    
    }
    public void install(JTextComponent c)
    {
	parent.addFocusListener(this);
	parent.addMouseListener(this);
	parent = c;
	repaint();
    }
    
    Point magic = null;
    public void setMagicCaretPosition(Point p)
    {	magic = p;    }
    public Point getMagicaretPosition()
    {	return magic;    }

    
    int mark = 0;
    public int getMark()
    {	return mark;    }

    boolean vis_sel = true;
    public void setSelectionVisible(boolean v)
    {  vis_sel = v;  repaint();  }
    public boolean isSelectionVisible()
    {  return vis_sel;    }

    private void repaint()
    {	
	if (parent != null)
	    {
		parent.repaint();
	    }
    }

    public void paint(Graphics g)
    {
	g.setColor(color);
	g.drawLine(x,y,
		   x,y+height);
    }

    
    Vector changes = new Vector();
    public void addChangeListener(ChangeListener l)
    {	changes.addElement(l);    }
    public void removeChangeListener(ChangeListener l)
    {   changes.removeElement(l);    }


    int blink = 500;
    public int getBlinkRate()
    { return blink;    }
    public void setBlinkRate(int rate)
    { blink = rate;    }

    int dot = 0;
    public int getDot()
    {  return dot;     }
    public void moveDot(int dot)
    {   setDot(dot);    }
    public void setDot(int dot)
    {
	this.dot = dot;   
	repaint();
    }

    boolean vis = true;
    public boolean isVisible()
    {	return vis;    }
    public void setVisible(boolean v)
    {
	vis = v; 
	repaint();
    }
}



