package javax.swing.text;

import java.awt.*;

public class ComponentView extends View
{
    public ComponentView(Element elem)
    {
	super(elem);
    }
 

    protected  Component createComponent()
    {
	return null;
    }
    
    public float getAlignment(int axis)
    {
	return 0;
    }

    public Component getComponent()
    {
	return null;
    }
    
    public float getMaximumSpan(int axis)
    {
	return 0;
    }

    public float getMinimumSpan(int axis)
    {
	return 0;
    }

    public float getPreferredSpan(int axis)
    {
	return 0;
    }

    public Shape modelToView(int pos, Shape a, Position.Bias b)
    {
	return null;
    }
    
    public void paint(Graphics g, Shape a)
    {
    }
    
    public void setParent(View p)
    {
    }
    
    public void setSize(float width, float height)
    {
    }
    
    public int viewToModel(float x, float y, Shape a, Position.Bias[] bias)
    {
	return 0;
    }
}
