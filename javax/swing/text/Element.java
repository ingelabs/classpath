package javax.swing.text;



public interface Element
{
    AttributeSet getAttributes();
    Document getDocument();
    Element getElement(int index);
    int getElementCount();
    int getElementIndex(int offset);
    int getEndOffset();
    String getName();
    Element getParentElement();
    int getStartOffset();
    boolean isLeaf();
 }
