package java.nio.charset;

import java.io.*;

class CharacterCodingException extends IOException
{
    CharacterCodingException()
    {
    }

    CharacterCodingException(String s)
    {
	super(s);
    }
}
