package java.nio.channels.spi;

import java.nio.channels.*;
import java.util.*;


public abstract class AbstractSelectionKey
    extends SelectionKey
{
    boolean ok = true;

    protected AbstractSelectionKey()
    {
    }
 
    public void cancel()
    {
	if (ok)
	    {
		selector().cancelledKeys().add(this);
	    }
	ok = false;
    }

    public boolean isValid()
    {
	return ok;
    }
}

