package java.nio.channels;



public abstract class SelectionKey
{
    static int OP_ACCEPT  = 1<<0;
    static int OP_CONNECT = 1<<1;
    static int OP_READ    = 1<<2;
    static int OP_WRITE   = 1<<3;
    
    Object attached;
    
protected SelectionKey()
    {
    }
 

    Object attach(Object obj)
    {
	Object old = attached;
	attached = obj;
	return old;
    }
    
    Object attachment()
    {
	return attached;
    }
    
    boolean isAcceptable()
    { 
	return (readyOps() & OP_ACCEPT) != 0;
    }
    boolean isConnectable()
    {
	return (readyOps() & OP_CONNECT) != 0;  
    }        
    boolean isReadable()
    {
	return (readyOps() & OP_READ) != 0; 
    }
    boolean isWritable()
    {
	return (readyOps() & OP_WRITE) != 0;
    }


    abstract  void cancel(); 
    abstract  SelectableChannel channel();
    abstract  int interestOps();
    abstract  SelectionKey interestOps(int ops);
    abstract  boolean isValid();
    abstract  int readyOps();
    abstract  Selector selector();
}
