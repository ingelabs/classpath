package javax.swing.text;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;


import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;

public class JTextComponent extends JComponent
{
    public class KeyBinding
    {
	char from, to;
    }
    
    int icon_gap;
    Icon icon;
    int align;
    Document doc;

    public JTextComponent()
    {
	this("", null, 0);
    }

    public JTextComponent(Icon image)
    {
	this("", image, 0);
    }

    public JTextComponent(Icon image, int horizontalAlignment)
    {
	this("", image, horizontalAlignment);
    }

    public JTextComponent(String text)
    {
	this(text, null, 0);
    }

    public JTextComponent(String text, int horizontalAlignment)
    {
	this(text, null, horizontalAlignment);
    }

    public JTextComponent(String text, Icon icon, int horizontalAlignment)
    {
	setDocument(new PlainDocument());

	// do the work.....
	setText(text);
	this.icon  = icon;
	this.align     = horizontalAlignment;
	
        // its an editor, so:
        enableEvents(AWTEvent.KEY_EVENT_MASK);
        updateUI();
    }

    public void setDocument(Document s)
    {
	doc = s;
	revalidate();
	repaint();
    }

    public Document getDocument()
    {
	if (doc == null)
	    System.out.println("doc == null !!!");
	return doc;
    }

    protected  int checkHorizontalKey(int key, String message)
    {
	//    Verify that key is a legal value for the horizontalAlignment properties. 
	return 0;
    }
    protected  int checkVerticalKey(int key, String message)
    {
	//      Verify that key is a legal value for the verticalAlignment or verticalTextPosition properties.  
	return 0;
    }
    public AccessibleContext getAccessibleContext()
    {
	//          Get the AccessibleContext of this object 
	return null;
    }
    public Icon getDisabledIcon()
    {
	return null;
    }
    public int getDisplayedMnemonic()
    {
	//          Return the keycode that indicates a mnemonic key.   
	return 0;
    }
    public int getHorizontalAlignment()
    {
	//          Returns the alignment of the label's contents along the X axis.   
	return 0;
    }
    public int getHorizontalTextPosition()
    {
	//          Returns the horizontal position of the label's text, relative to its image.    
	return 0;
    }

    public Icon getIcon()
    {	return icon;    }
    public int getIconTextGap()
    {	return icon_gap;    }


    Component getLabelFor()
    {
	//          Get the component this is labelling.  
	return null;
    }

    public void setText(String text)
    {
	getDocument().remove(0,doc.getLength());
	getDocument().insertString(0, text, null);
    }
  
    public String getText()
    {
	return getDocument().getText(0, 
				     getDocument().getLength());
    }

    public String getUIClassID()
    {
	//          Returns a string that specifies the name of the l&f class that renders this component.  
	return "JTextComponent";
    }
    public int getVerticalAlignment()
    {
	//          Returns the alignment of the label's contents along the Y axis. 
	return 0;
    }
    public int getVerticalTextPosition()
    {
	//          Returns the vertical position of the label's text, relative to its image. 
	return 0;
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h)
    {
	//          This is overriden to return false if the current Icon's Image is not equal to the passed in Image img. 
	return (img == icon);
    }
    protected  String paramString()
    {
	//          Returns a string representation of this JTextComponent.  
	return "JTextComponent";
    }
    void setDisabledIcon(Icon disabledIcon)
    {
	//          Set the icon to be displayed if this JTextComponent is "disabled" (JTextComponent.setEnabled(false)).  
    }
    void setDisplayedMnemonic(char aChar)
    {
	//          Specifies the displayedMnemonic as a char value.  
    }
    void setDisplayedMnemonic(int key)
    {
	//          Specify a keycode that indicates a mnemonic key.  
    }
    void setHorizontalAlignment(int alignment)
    {
	//          Sets the alignment of the label's contents along the X axis.  
    }
    void setHorizontalTextPosition(int textPosition)
    {
	//          Sets the horizontal position of the label's text, relative to its image.  
    }
    void setIcon(Icon icon)
    {
	//          Defines the icon this component will display.  
    }
    public void setIconTextGap(int iconTextGap)
    {
	//          If both the icon and text properties are set, this property defines the space between them.  
    }
  
    public void setLabelFor(Component c)
    {
	//          Set the component this is labelling.  
    }
    
    public void setVerticalAlignment(int alignment)
    {
	//          Sets the alignment of the label's contents along the Y axis.  
    }
    public void setVerticalTextPosition(int textPosition)
    {
	//          Sets the vertical position of the label's text, relative to its image.  
    }

    public TextUI getUI()
    {	return (TextUI) ui;
    }

    public void updateUI()
    {
	TextUI b = (TextUI)UIManager.getUI(this);
	setUI(b);
    }
}












