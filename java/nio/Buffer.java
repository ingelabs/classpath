package java.nio;


abstract class Buffer
{
    int cap, limit, pos, mark;

    Buffer()
    {
    }

    final int capacity()
    {
	return cap;
    }

    final Buffer clear()
    {
	limit = cap;
	mark = 0;
	pos = 0;
	return this;
    }
    
    final Buffer flip()
    {
	limit = pos;
	pos = 0;

	mark = 0;

	return this;
    }
    
    final boolean hasRemaining()
    {
	return limit > pos;
    }

    abstract  boolean isReadOnly();    
    
    
    final int limit()
    {
	return limit;
    }

    final Buffer limit(int newLimit)
    {
	if (newLimit <= mark)
	    mark = 0;

	if (pos > newLimit)
	    pos = newLimit - 1;

	limit = newLimit;
	return this;
    }

    final Buffer mark()
    {
	mark = pos;
	return this;
    }

    final int position()
    {
	return pos;
    }
    

    final Buffer position(int newPosition)
    {
	/// If the mark is defined and larger than the new 

	if (newPosition <= mark)
	    mark = 0;

	pos = newPosition;
	return this;
    }

    final int remaining()
    {
	return limit - pos;
    }

    final Buffer reset()
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
