/*************************************************************************
/* Font.java -- Font object
/*
/* Copyright (c) 1999 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This library is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later verion.
/*
/* This library is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this library; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.awt;

import java.awt.peer.FontPeer;
import java.util.StringTokenizer;

/**
  * This class represents a windowing system font.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Font implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * Constant indicating a "plain" font.
  */
public static final int PLAIN = 0;

/**
  * Constant indicating a "bold" font.
  */
public static final int BOLD = 1;

/**
  * Constant indicating an "italic" font.
  */
public static final int ITALIC = 2;

// Serialization constant
private static final long serialVersionUID = -4206021311591459213L;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The name of this font
  */
protected String name;

/**
  * The font style, which is a combination (by summing, not OR-ing) of
  * the font style constants in this class.
  */
protected int style;

/**
  * The font point size.
  */
protected int size;

// The native peer for this font
private FontPeer peer;

/*************************************************************************/

/*
 * Static Methods
 */

/**
  * Creates a <code>Font</code> object from the specified string, which
  * is in one of the following formats:
  * <p>
  * <ul>
  * <li>fontname-style-pointsize
  * <li>fontname-style
  * <li>fontname-pointsize
  * <li>fontname
  * </ul>
  * <p>
  * The style should be one of BOLD, ITALIC, or BOLDITALIC.  The default
  * style if none is specified is PLAIN.  The default size if none
  * is specified is 12.
  */
public static Font
decode(String fontspec)
{
  String name = null;
  int style = PLAIN;
  int size = 12;

  StringTokenizer st = new StringTokenizer(fontspec, "-");
  while (st.hasMoreTokens())
    {
      String token = st.nextToken();
      if (name == null)
        {
          name = token;
          continue;
        }

      if (token.toUpperCase().equals("BOLD"))
        {
          style = BOLD;
          continue;
        }
      if (token.toUpperCase().equals("ITALIC"))
        {
          style = ITALIC;
          continue;
        }
      if (token.toUpperCase().equals("BOLDITALIC"))
        {
          style = BOLD + ITALIC;
          continue;
        }

      int tokenval = 0;
      try
        {
          tokenval = Integer.parseInt(token);
        }
      catch(Exception e) { ; }

      if (tokenval != 0)
        size = tokenval;
    }

  return(new Font(name, style, size));
}

/*************************************************************************/

/**
  * Returns a <code>Font</code> object from the passed property name.
  *
  * @param propname The name of the system property.
  * @param default Value to use if the property is not found.
  *
  * @return The requested font, or <code>default</code> if the property 
  * not exist or is malformed.
  */
public static Font
getFont(String propname, Font defval)
{
  String propval = System.getProperty(propname);
  if (propval != null)
    return(decode(propval));

  return(defval);
}

/*************************************************************************/

/**
  * Returns a <code>Font</code> object from the passed property name.
  *
  * @param propname The name of the system property.
  *
  * @return The requested font, or <code>null</code> if the property 
  * not exist or is malformed.
  */
public static Font
getFont(String propname)
{
  return(getFont(propname, null));
}

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Font</code> with the specified
  * attributes.
  *
  * @param name The name of the font.
  * @param style The font style.
  * @param size The font point size.
  */
public 
Font(String name, int style, int size)
{
  this.name = name;
  this.style = style;
  this.size = size;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the name of the font.
  *
  * @return The name of the font.
  */
public String
getName()
{
  return(name);
}

/*************************************************************************/

/**
  * Returns the style of the font.
  * 
  * @return The font style.
  */
public int
getSize()
{
  return(size);
}

/*************************************************************************/

/**
  * Tests whether or not this is a plain font.  This will be true if
  * and only if neither the bold nor the italics style is set.
  *
  * @return <code>true</code> if this is a plain font, <code>false</code>
  * otherwise.
  */
public boolean
isPlain()
{
  if (style == PLAIN)
    return(true);
  else
    return(false);
}

/*************************************************************************/

/**
  * Tests whether or not this font is bold.
  *
  * @return <code>true</code> if this font is bold, <code>false</code>
  * otherwise.
  */
public boolean
isBold()
{
  if ((style == BOLD) || (style == (BOLD+ITALIC)))
    return(true);
  else
    return(false);
}

/*************************************************************************/

/**
  * Tests whether or not this font is italic.
  *
  * @return <code>true</code> if this font is italic, <code>false</code>
  * otherwise.
  */
public boolean
isItalic()
{
  if ((style == ITALIC) || (style == (BOLD+ITALIC)))
    return(true);
  else
    return(false);
}

/*************************************************************************/

/**
  * Returns the system specific font family name.
  *
  * @return The system specific font family name.
  */
public String
getFamily()
{
  // FIXME: How do I implement this?
  return(name);
}

/*************************************************************************/

/**
  * Returns a native peer object for this font.
  *
  * @return A native peer object for this font.
  */
public FontPeer
getPeer()
{
  if (peer != null)
    return(peer);

  peer = Toolkit.getDefaultToolkit().getFontPeer(name, style);
  return(peer);
}

/*************************************************************************/

/**
  * Returns a hash value for this font.
  * 
  * @return A hash for this font.
  */
public int
hashCode()
{
  return((new String(name + size + style)).hashCode());
}

/*************************************************************************/

/**
  * Tests whether or not the specified object is equal to this font.  This
  * will be true if and only if:
  * <P>
  * <ul>
  * <li>The object is not <code>null</code>.
  * <li>The object is an instance of <code>Font</code>.
  * <li>The object has the same name, style, and size as this object.
  * </ul>
  *
  * @return <code>true</code> if the specified object is equal to this
  * object, <code>false</code> otherwise.
  */
public boolean
equals(Object obj)
{
  if (obj == null)
    return(false);

  if (!(obj instanceof Font))
    return(false);

  Font f = (Font)obj;

  if (!f.name.equals(name))
    return(false);

  if (f.size != size)
    return(false);

  if (f.style != style)
    return(false);

  return(true);
} 

/*************************************************************************/

/**
  * Returns a string representation of this font.
  *
  * @return A string representation of this font.
  */
public String
toString()
{
  return(getClass().getName() + "(name=" + name + ",style=" + style +
         ",size=" + size + ")");
}

} // class Font 

