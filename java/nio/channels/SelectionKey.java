package java.nio.channels;



public abstract class SelectionKey
{
    public static int OP_ACCEPT  = 1<<0;
    public static int OP_CONNECT = 1<<1;
    public static int OP_READ    = 1<<2;
    public static int OP_WRITE   = 1<<3;
    
    Object attached;
    
    protected SelectionKey()
    {
    }
 

    public Object attach(Object obj)
    {
	Object old = attached;
	attached = obj;
	return old;
    }
    
    public Object attachment()
    {
	return attached;
    }    
    public boolean isAcceptable()
    { 
	return (readyOps() & OP_ACCEPT) != 0;
    }
    public boolean isConnectable()
    {
	return (readyOps() & OP_CONNECT) != 0;  
    }        
    public boolean isReadable()
    {
	return (readyOps() & OP_READ) != 0; 
    }
    public boolean isWritable()
    {
	return (readyOps() & OP_WRITE) != 0;
    }


    public abstract  void cancel(); 
    public abstract  SelectableChannel channel();
    public abstract  int interestOps();
    public abstract  SelectionKey interestOps(int ops);
    public abstract  boolean isValid();
    public abstract  int readyOps();
    public abstract  Selector selector();
}
