package gnu.java.nio;

import java.util.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;


public class SelectorImpl extends AbstractSelector
{
    Set keys, selected, canceled;

    public SelectorImpl(SelectorProvider provider)
    {
	super(provider);
    }

    public Set keys()
    {
	return keys;
    }
    
    public int selectNow()    { return select(1);                }
    public int select()       { return select(Long.MAX_VALUE);   }

    private static native int java_do_select(int []read,
					     int []write,
					     int []except,
					     long timeout);

    public int select(long  timeout)
    {
	if (keys == null)
	    {
		return 0;
	    }

	int [] read   = new int[keys.size()];
	int [] write  = new int[keys.size()];
	int [] except = new int[keys.size()];
	int i = 0;

	Iterator it = keys.iterator();
	while (it.hasNext())
	    {
		SelectionKeyImpl k = (SelectionKeyImpl) it.next();

		read[i]   = k.fd;
		write[i]  = k.fd;
		except[i] = k.fd;

		i++;
	    }

	int ret = java_do_select(read,
				 write,
				 except,
				 timeout);
	it = keys.iterator();
	while (it.hasNext())
	    {
		SelectionKeyImpl k = (SelectionKeyImpl) it.next();

		if (read[i]   != -1 ||
		    write[i]  != -1 ||
		    except[i] != -1)
		    {
			add_selected(k);
		    }

		i++;
	    }
	return ret;
    }
    
    
    public Set selectedKeys()    {	return selected;    }
    public Set cancelledKeys()   {	return canceled;    }

    public Selector wakeup()
    {
	return null;
    }

    public void add(SelectionKeyImpl k)
    {
	if (keys == null)
	    keys = new HashSet();

	keys.add(k);
    }

    void add_selected(SelectionKeyImpl k)
    {
	if (selected == null)
	    selected = new HashSet();

	selected.add(k);
    }


    protected  void implCloseSelector()
    {
    }
    
    protected  SelectionKey register(SelectableChannel  ch, 
				     int  ops, 
				     Object  att)
    {
	return register((AbstractSelectableChannel) ch,
			ops, 
			att);
    }

    protected  SelectionKey register(AbstractSelectableChannel  ch, 
				     int  ops, 
				     Object  att)
    {
	/*
	  // filechannel is not selectable ?
	if (ch instanceof gnu.java.nio.FileChannelImpl)
	    {
		FileChannelImpl fc = (FileChannelImpl) ch;

		SelectionKeyImpl impl = new SelectionKeyImpl(ch,
							     this,
							     fc.fd);

		keys.add(impl);

		return impl;
	    }
	else
	*/
	
	if (ch instanceof gnu.java.nio.SocketChannelImpl)
	    {
		SocketChannelImpl fc = (SocketChannelImpl) ch;

		SelectionKeyImpl impl = new SelectionKeyImpl(ch,
							     this,
							     fc.fd);
		add(impl);

		return impl;
	    }
	else
	    {
		System.err.println("INTERNAL ERROR, no known channel type");
	    }

	return null;
    }

}
