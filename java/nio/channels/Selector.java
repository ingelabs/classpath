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

    abstract  void close();
    abstract  boolean isOpen();
    abstract  Set keys();
    abstract  SelectorProvider provider();
    abstract  int select();
    abstract  int select(long timeout);
    abstract  Set selectedKeys();
    abstract  Set cancelledKeys();
    abstract  int selectNow();
    abstract  Selector wakeup();
}
