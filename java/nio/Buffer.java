package java.nio;


public abstract class Buffer
{
    int cap, limit, pos, mark;

    Buffer()
    {
    }

    public final int capacity()
    {
	return cap;
    }

    public final int capacity(int c)
    {
	int old = cap;
	cap = c;
	return old;
    }

    public final Buffer clear()
    {
	limit = cap;
	mark = 0;
	pos = 0;
	return this;
    }
    
    public final Buffer flip()
    {
	limit = pos;
	pos = 0;

	mark = 0;

	return this;
    }
    
    public final boolean hasRemaining()
    {
	return limit > pos;
    }

    public abstract  boolean isReadOnly();    
    
    
    public final int limit()
    {
	return limit;
    }

    public final Buffer limit(int newLimit)
    {
	if (newLimit <= mark)
	    mark = 0;

	if (pos > newLimit)
	    pos = newLimit - 1;

	limit = newLimit;
	return this;
    }

    public final Buffer mark()
    {
	mark = pos;
	return this;
    }

    public final int position()
    {
	return pos;
    }
    

    public final Buffer position(int newPosition)
    {
	/// If the mark is defined and larger than the new 

	if (newPosition <= mark)
	    mark = 0;

	pos = newPosition;
	return this;
    }

    public final int remaining()
    {
	return limit - pos;
    }

    public final Buffer reset()
    {
	pos = mark;
	return this;
    }

    public final Buffer rewind()
    {
	mark = pos = 0;
	return this;
    }
}
