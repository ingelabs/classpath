package java.nio.charset;

class CoderResult
{ 
    boolean err;

    boolean isError()
    {
	return err;
    }

    boolean isMalformed()
    {
	return false;
    }

    boolean isOverflow()
    {
	return false;
    }

    boolean isUnderflow()
    {
	return false;
    }

    boolean isUnmappable()
    {
	return false;
    }

    int length()
    {
	return 0;
    }

    static CoderResult malformedForLength(int length)
    {
	return null;
    }
    
    void throwException()
	throws CharacterCodingException
    {
	throw new CharacterCodingException();
    }

    public String toString()
    {
	return "coder error";
    }

    static CoderResult unmappableForLength(int length)
    {
	return null;
    }    

}
