/* CharsetEncoder.java -- 
   Copyright (C) 2002 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

package java.nio.charset;

import java.nio.*;


public abstract class CharsetEncoder
{
    Charset cs;
    float averageBytesPerChar;
    float maxBytesPerChar;
    byte[] repl;
    
    protected CharsetEncoder(Charset cs,
			     float averageBytesPerChar,
			     float maxBytesPerChar)
    {
	this(cs, averageBytesPerChar, maxBytesPerChar,  null);
    }

    protected CharsetEncoder(Charset cs, 
			     float averageBytesPerChar, 
			     float maxBytesPerChar, 
			     byte[] replacement)
    {
	this.cs = cs;
	this.maxBytesPerChar = maxBytesPerChar;
	this.averageBytesPerChar = averageBytesPerChar;
	this.repl = replacement;
    }
 
    public float averageBytesPerChar()
    {
	return averageBytesPerChar;
    }

    public boolean canEncode(char c)
    {
	return true;
    }
    
    public boolean canEncode(CharSequence cs)
    {
	return true;
    }
    
    public Charset charset()
    {
	return cs;
    }
    
    public ByteBuffer encode(CharBuffer in)
    {
	ByteBuffer x = ByteBuffer.allocate(in.remaining());
	encode(in, x, false);
	return x;
    }
    
    public CoderResult encode(CharBuffer in, ByteBuffer out, boolean endOfInput)
    {   
	return encodeLoop(in, out);
    }
    
    
    protected abstract  CoderResult encodeLoop(CharBuffer in, ByteBuffer out);
    
    public CoderResult flush(ByteBuffer out)
    {
	return implFlush(out);
    }

    protected  CoderResult implFlush(ByteBuffer out)
    {
	return null;
    }

    protected  void implOnMalformedInput(CodingErrorAction newAction)
    {
    }

    protected  void implOnUnmappableCharacter(CodingErrorAction newAction)
    {
    }

    protected  void implReplaceWith(byte[] newReplacement)
    {
    }
    
    protected  void implReset()
    {
    }

 boolean isLegalReplacement(byte[] repl)
    {
	return true;
    }


    public CodingErrorAction malformedInputAction()
    {
	return null;
    }

    public float maxBytesPerChar()
    {
	return  maxBytesPerChar;
    }    

    public CharsetEncoder onMalformedInput(CodingErrorAction newAction)
    {
	return null;
    }

    public CharsetEncoder onUnmappableCharacter(CodingErrorAction newAction)
    {
	return null;
    }
    
    public byte[] replacement()
    {
	return repl;
    }

    public CharsetEncoder replaceWith(byte[] newReplacement)
    {
	repl = newReplacement;
	return null;
    }

    public CharsetEncoder reset()
    {
	return null;
    }

    public CodingErrorAction unmappableCharacterAction()
    {
	return null;
    } 
}
