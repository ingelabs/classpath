package javax.swing;

import java.io.*;
import java.net.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;

public class JEditorPane extends JTextComponent
{
    URL page_url;
    EditorKit kit;
    String ctype = "text/plain";
    boolean focus_root;
    boolean manages_focus;


    JEditorPane()
    {
    }

    JEditorPane(String url)
    {
	this();
	setPage(url);
    }
    
    JEditorPane(String type, String text)
    {
	ctype = text;
	setText(text);
    }
    
    JEditorPane(URL url)
    {
	setPage(url);
    }

  void addHyperlinkListener(HyperlinkListener listener)
    {  }
    
    protected  EditorKit createDefaultEditorKit()
    {	return new PlainEditorKit();    }
    
    static EditorKit createEditorKitForContentType(String type)
    {	return new PlainEditorKit();     }
    
  void fireHyperlinkUpdate(HyperlinkEvent e)
  {
  }

  AccessibleContext getAccessibleContext()
  {      return null;  }

  String getContentType()
    {  return ctype;   }

  EditorKit getEditorKit()
    {  return kit;    }
    
  static String getEditorKitClassNameForContentType(String type)
    { return "text/plain";  }
  
  EditorKit getEditorKitForContentType(String type)
    { return kit;  }
    
    public Dimension getPreferredSize()
    {
	//Returns the preferred size for the JEditorPane.  
	return super.getPreferredSize();
    }

  boolean getScrollableTracksViewportHeight()
    {  return false;  }
  boolean getScrollableTracksViewportWidth()
    {  return false;  }

  URL getPage()
    { return page_url;  }

  protected  InputStream getStream(URL page)
    {	
	try {
	    return page.openStream();    
	} catch (Exception e) {
	    System.out.println("Hhmmm, failed to open stream: " + e);
	}	
	return null;
    }

    public String getText()
    { return super.getText();    }
    
    String getUIClassID()
  {    return "JEditorPane";  }

  boolean isFocusCycleRoot()
    { return focus_root;
  }
  boolean isManagingFocus()
    { return manages_focus;  }

  protected  String paramString()
    { return "JEditorPane";  }
    
  protected  void processComponentKeyEvent(KeyEvent e)
    {
	//Overridden to handle processing of tab/shift tab. 
    }
    
  protected void processKeyEvent(KeyEvent e)
    {
	//Make sure that TAB and Shift-TAB events get consumed, so that awt doesn't attempt focus traversal.  
    }
    
    void read(InputStream in, Object desc)
    {
	//This method initializes from a stream. 
    }
    
    static void registerEditorKitForContentType(String type, String classname)
    {
	//Establishes the default bindings of type to classname. 
    }
    
    static void registerEditorKitForContentType(String type, String classname, ClassLoader loader)
    {
	//Establishes the default bindings of type to classname.  
    }
    
    void removeHyperlinkListener(HyperlinkListener listener)
    {
	//Removes a hyperlink listener.  
    }
    
    void replaceSelection(String content)
    {
	//Replaces the currently selected content with new content represented by the given string. 
    }
    
    protected  void scrollToReference(String reference)
    {
	//Scrolls the view to the given reference location (that is, the value returned by the UL.getRef method for the URL being displayed).  
    }
    
    void setContentType(String type)
    {
	ctype = type;
	invalidate();
	repaint();
    }
    
    void setEditorKit(EditorKit kit)
    {
	this.kit = kit;
	invalidate();
	repaint();
    }
    
    void setEditorKitForContentType(String type, EditorKit k)
    {
	ctype = type;
	setEditorKit(k);
    }
  
  void setPage(String url)
    {
	//  Sets the current URL being displayed.  
    }
    
    void setPage(URL page)
    {
	//    Sets the current URL being displayed.  
    }
    
    void setText(String t)
    {	
	super.setText(t);
    }
}






