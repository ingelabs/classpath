package javax.swing.text; 

// too lazy to make a real gapcontent.
// lets just use a stringbuffer instead.

import javax.swing.undo.*;


public class GapContent implements AbstractDocument.Content
{
    StringBuffer buf = new StringBuffer();

    public GapContent()
    {
	this(10);
    }
    
    public GapContent(int size)
    {
    }

    public Position createPosition(final int offset) throws BadLocationException
    {
	return new Position()
	    {
		int off = offset;
		public int getOffset()
		{
		    return off;
		}
	    };
    }

    public int length()
    {
	return buf.length();
    }

    public UndoableEdit insertString(int where, String str) throws BadLocationException
    {
	buf.insert(where, str);
	return null;
    }

    public UndoableEdit remove(int where, int nitems) throws BadLocationException
    {
	buf.delete(where, where + nitems);
	return null;
    }

    public String getString(int where, int len) throws BadLocationException
    {
	return buf.toString();
    }

    public void getChars(int where, int len, Segment txt) throws BadLocationException
    {
	txt.array = new char[len];
		
	System.arraycopy(buf.toString().toCharArray(), where, 
			 txt.array, 0,
			 len);
	
	txt.count  = len;
	txt.offset = 0;
    }
}
