package javax.swing.text;

import java.awt.*;

public class ComponentView extends View
{
    ComponentView(Element elem)
    {
	super(elem);
    }
 

    protected  Component createComponent()
    {
	return null;
    }
    
    float getAlignment(int axis)
    {
	return 0;
    }

    Component getComponent()
    {
	return null;
    }

    float getMaximumSpan(int axis)
    {
	return 0;
    }

    float getMinimumSpan(int axis)
    {
	return 0;
    }

    float getPreferredSpan(int axis)
    {
	return 0;
    }

    Shape modelToView(int pos, Shape a, Position.Bias b)
    {
	return null;
    }
    
    void paint(Graphics g, Shape a)
    {
    }
    
    void setParent(View p)
    {
    }
    
    void setSize(float width, float height)
    {
    }
    
    int viewToModel(float x, float y, Shape a, Position.Bias[] bias)
    {
	return 0;
    }
}
