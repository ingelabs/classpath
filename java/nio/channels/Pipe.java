package java.nio.channels;

import java.nio.channels.spi.*;

public abstract class Pipe
{
    public abstract static class SinkChannel
	extends AbstractSelectableChannel
	implements WritableByteChannel, GatheringByteChannel
    {
	protected SinkChannel(SelectorProvider provider)
	    {
	    }
    }

    public abstract static class SourceChannel
	extends AbstractSelectableChannel
	implements ReadableByteChannel, ScatteringByteChannel
    {
	protected SourceChannel(SelectorProvider provider)
	{
	}
    }
    

    protected Pipe()
    {
    }
    
    static Pipe open()
    {
	return null;
    }
    
    public abstract  SinkChannel sink();
    public abstract  SourceChannel source();   
}
