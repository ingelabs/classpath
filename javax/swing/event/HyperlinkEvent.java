package javax.swing.event;

import java.util.*;
import java.net.*;

public class HyperlinkEvent extends EventObject
{
    class EventType
    {
    }

    String descr;
    EventType t;

    HyperlinkEvent(Object source, EventType type, URL u)
    {
	this(source, type, u, null);
    }

    HyperlinkEvent(Object source, EventType type, URL u, String desc)
    {
	super(source);

	descr = desc;
	t = type;
    }
}
