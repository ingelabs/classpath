package java.nio.channels;

import java.util.*;
import java.nio.channels.spi.*;

public abstract class Selector
{
    protected Selector()
    {
    }
 
    static Selector open()
    {
	return SelectorProvider.provider().openSelector();
    }

    public abstract  void close();
    public abstract  boolean isOpen();
    public abstract  Set keys();
    public abstract  SelectorProvider provider();
    public abstract  int select();
    public abstract  int select(long timeout);
    public abstract  Set selectedKeys();
    public abstract  Set cancelledKeys();
    public abstract  int selectNow();
    public abstract  Selector wakeup();
}
