package javax.swing.text;

import javax.swing.*;
import java.io.*;

public abstract class EditorKit implements Cloneable
{
    EditorKit()
    {
    }

    EditorKit(EditorKit kit)
    {
    }

    void deinstall(JEditorPane c)
    {
	//      Called when the kit is being removed from the JEditorPane. 
    }
    void install(JEditorPane c)
    {
    }

    abstract  Caret createCaret();
    abstract  Document createDefaultDocument();
    abstract  Action[] getActions();
    abstract  String getContentType();
    abstract  ViewFactory getViewFactory();
    abstract  void read(InputStream in, Document doc, int pos);
    abstract  void read(Reader in, Document doc, int pos);
    abstract  void write(OutputStream out, Document doc, int pos, int len);
    abstract  void write(Writer out, Document doc, int pos, int len);    
}

