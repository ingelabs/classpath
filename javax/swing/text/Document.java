package javax.swing.text;


import javax.swing.event.*;

public interface Document
{ 
    void addDocumentListener(DocumentListener listener);
    void addUndoableEditListener(UndoableEditListener listener);
    Position createPosition(int offs);
    Element getDefaultRootElement();
    Position getEndPosition();
    int getLength();
    Object getProperty(Object key);
    Element[] getRootElements();
    Position getStartPosition();
    String getText(int offset, int length);
    void getText(int offset, int length, Segment txt);
    void insertString(int offset, String str, AttributeSet a);
    void putProperty(Object key, Object value);
    void remove(int offs, int len);
    void removeDocumentListener(DocumentListener listener);
    void removeUndoableEditListener(UndoableEditListener listener);
    void render(Runnable r);
}
