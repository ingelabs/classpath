package javax.swing.text;

import java.util.*;


public class Segment implements Cloneable, CharacterIterator
{
    char[] array;
    int count;
    int offset;
    
    public Object clone()
    {
	try {
	    return super.clone();
	} catch (Exception e) {
	    System.err.println("Huuuhhh, this class implements cloneable !!!!!!");
	    System.err.println("I think there is a bug in this JVM somewhere");
	}
	return null;
    }
    
    public char current()
    {
	return array[getIndex()];
    }

    public char first()
    {
	offset = getBeginIndex();
	return array[offset];
    }
    
    public int getBeginIndex()
    {
	return offset;
    }
    
    public int getEndIndex()
    {
	return offset + count;
    }
    public int getIndex()
    {
	return offset;
    }
    public char last()
    {
	offset = getEndIndex() - 1;
	return array[offset];
    }
    public char next()
    {
	offset++;
	return array[offset];
    }
    public char previous()
    {
	offset--;
	return array[offset];
    }
    public char setIndex(int position)
    {
	offset = position;
	return array[offset];
    }

    public String toString()
    {
	return new String(array, offset, count);
    }
}

