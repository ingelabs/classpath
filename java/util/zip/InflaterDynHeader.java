/* java.util.zip.InflaterDynHeader
   Copyright (C) 2001 Free Software Foundation, Inc.

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

package java.util.zip;

class InflaterDynHeader
{
  private static final int LNUM   = 0;
  private static final int DNUM   = 1;
  private static final int BLNUM  = 2;
  private static final int BLLENS = 3;
  private static final int LLENS  = 4;
  private static final int DLENS  = 5;
  private static final int LREPS  = 6;
  private static final int DREPS  = 7;
  private static final int FINISH = 8;
  
  private byte[] blLens;
  private byte[] litlenLens;
  private byte[] distLens;

  private InflaterHuffmanTree blTree;
  
  private int mode;
  private int lnum, dnum, blnum;
  private int repBits;
  private byte repeatedLen;
  private int ptr;

  private static final int[] BL_ORDER =
  { 16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15 };
  
  public InflaterDynHeader()
  {
  }
  
  public boolean decode(StreamManipulator input) throws DataFormatException
  {
  decode_loop:
    for (;;)
      {
	switch (mode)
	  {
	  case LNUM:
	    lnum = input.peekBits(5);
	    if (lnum < 0)
	      return false;
	    lnum += 257;
	    input.dropBits(5);
	    litlenLens = new byte[lnum];
//  	    System.err.println("LNUM: "+lnum);
	    mode = DNUM;
	    /* fall through */
	  case DNUM:
	    dnum = input.peekBits(5);
	    if (dnum < 0)
	      return false;
	    dnum++;
	    input.dropBits(5);
	    distLens = new byte[dnum];
//  	    System.err.println("DNUM: "+dnum);
	    mode = BLNUM;
	    /* fall through */
	  case BLNUM:
	    blnum = input.peekBits(4);
	    if (blnum < 0)
	      return false;
	    blnum += 4;
	    input.dropBits(4);
	    blLens = new byte[19];
	    ptr = 0;
//  	    System.err.println("BLNUM: "+blnum);
	    mode = BLLENS;
	    /* fall through */
	  case BLLENS:
	    while (ptr < blnum)
	      {
		int len = input.peekBits(3);
		if (len < 0)
		  return false;
		input.dropBits(3);
//  		System.err.println("blLens["+BL_ORDER[ptr]+"]: "+len);
		blLens[BL_ORDER[ptr]] = (byte) len;
		ptr++;
	      }
	    blTree = new InflaterHuffmanTree(blLens);
	    blLens = null;
	    ptr = 0;
	    mode = LLENS;
	    /* fall through */
	  case LLENS:
	    while (ptr < lnum)
	      {
		int symbol = blTree.getSymbol(input);
		if (symbol < 0)
		  return false;
		switch (symbol)
		  {
		  default:
//  		    System.err.println("litlenLens["+ptr+"]: "+symbol);
		    litlenLens[ptr++] = (byte) symbol;
		    break;
		  case 16: /* repeat last len 3-6 times */
		    if (ptr == 0)
		      throw new DataFormatException
			("Repeating, but no prev len");
//  		    System.err.println("litlenLens["+ptr+"]: repeat");
		    repeatedLen = litlenLens[ptr-1];
		    repBits = 2;
		    for (int i = 3; i-- > 0; )
		      {
			if (ptr >= lnum)
			  throw new DataFormatException();
			litlenLens[ptr++] = repeatedLen;
		      }
		    mode = LREPS;
		    continue decode_loop;
		  case 17: /* repeat zero 3-10 times */
//  		    System.err.println("litlenLens["+ptr+"]: zero repeat");
		    repeatedLen = 0;
		    repBits = 3;
		    for (int i = 3; i-- > 0; )
		      {
			if (ptr >= lnum)
			  throw new DataFormatException();
			litlenLens[ptr++] = repeatedLen;
		      }
		    mode = LREPS;
		    continue decode_loop;
		  case 18: /* repeat zero 11-138 times */
//  		    System.err.println("litlenLens["+ptr+"]: zero repeat");
		    repeatedLen = 0;
		    repBits = 7;
		    for (int i = 11; i-- > 0; )
		      {
			if (ptr >= lnum)
			  throw new DataFormatException();
			litlenLens[ptr++] = repeatedLen;
		      }
		    mode = LREPS;
		    continue decode_loop;
		  }
	      }
	    ptr = 0;
	    mode = DLENS;
	    /* fall through */
	  case DLENS:
	    while (ptr < dnum)
	      {
		int symbol = blTree.getSymbol(input);
		if (symbol < 0)
		  return false;
		switch (symbol)
		  {
		  default:
		    distLens[ptr++] = (byte) symbol;
//  		    System.err.println("distLens["+ptr+"]: "+symbol);
		    break;
		  case 16: /* repeat last len 3-6 times */
		    if (ptr == 0)
		      throw new DataFormatException
			("Repeating, but no prev len");
//  		    System.err.println("distLens["+ptr+"]: repeat");
		    repeatedLen = distLens[ptr-1];
		    repBits = 2;
		    for (int i = 3; i-- > 0; )
		      {
			if (ptr >= dnum)
			  throw new DataFormatException();
			distLens[ptr++] = repeatedLen;
		      }
		    mode = DREPS;
		    continue decode_loop;
		  case 17: /* repeat zero 3-10 times */
//  		    System.err.println("distLens["+ptr+"]: repeat zero");
		    repeatedLen = 0;
		    repBits = 3;
		    for (int i = 3; i-- > 0; )
		      {
			if (ptr >= dnum)
			  throw new DataFormatException();
			distLens[ptr++] = repeatedLen;
		      }
		    mode = DREPS;
		    continue decode_loop;
		  case 18: /* repeat zero 11-138 times */
//  		    System.err.println("distLens["+ptr+"]: repeat zero");
		    repeatedLen = 0;
		    repBits = 7;
		    for (int i = 11; i-- > 0; )
		      {
			if (ptr >= dnum)
			  throw new DataFormatException();
			distLens[ptr++] = repeatedLen;
		      }
		    mode = DREPS;
		    continue decode_loop;
		  }
	      }
	    mode = FINISH;
	    return true;
	  case LREPS:
	    {
	      int count = input.peekBits(repBits);
	      if (count < 0)
		return false;
	      input.dropBits(repBits);
//  	      System.err.println("litlenLens repeat: "+repBits);
	      while (count-- > 0)
		{
		  if (ptr >= lnum)
		    throw new DataFormatException();
		  litlenLens[ptr++] = repeatedLen;
		}
	    }
	    mode = LLENS;
	    continue decode_loop;
	  case DREPS:
	    {
	      int count = input.peekBits(repBits);
	      if (count < 0)
		return false;
	      input.dropBits(repBits);
	      while (count-- > 0)
		{
		  if (ptr >= dnum)
		    throw new DataFormatException();
		  distLens[ptr++] = repeatedLen;
		}
	    }
	    mode = DLENS;
	    continue decode_loop;
	  }
      }
  }

  public InflaterHuffmanTree buildLitLenTree() throws DataFormatException
  {
    return new InflaterHuffmanTree(litlenLens);
  }

  public InflaterHuffmanTree buildDistTree() throws DataFormatException
  {
    return new InflaterHuffmanTree(distLens);
  }
}
