package javax.swing.text;

import javax.swing.*;
import java.io.*;

public class PlainEditorKit extends EditorKit
{
    public PlainEditorKit()
    {
    }

    public PlainEditorKit(PlainEditorKit kit)
    {
	super(kit);
    }

    protected Object clone()  
    {
	return new PlainEditorKit(this);
    }
    void deinstall(JEditorPane c)
    {
	//      Called when the kit is being removed from the JEditorPane. 
    }
    void install(JEditorPane c)
    {
    }

    Caret createCaret()
    {
	return null;
    }
    Document createDefaultDocument()
    {
	return null;
    }
    Action[] getActions()
    {
	return null;
    }
    String getContentType()
    {
	return null;
    }
    ViewFactory getViewFactory()
    {
	return null;
    }
    void read(InputStream in, Document doc, int pos)
    {	
    }
    void read(Reader in, Document doc, int pos)
    {
    }
    void write(OutputStream out, Document doc, int pos, int len)
    {
    }
    void write(Writer out, Document doc, int pos, int len)
    {
    }
}

