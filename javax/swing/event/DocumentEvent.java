package javax.swing.event;

import javax.swing.text.*;


public interface DocumentEvent
{
    class ElementChange
    {
    }

    class EventType
    {
    }

    ElementChange getChange(Element elem);
    Document getDocument();
    int getLength();
    int getOffset();
    EventType getType();
}
 
