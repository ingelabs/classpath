/* Label.java -- Java label widget
   Copyright (C) 1999 Free Software Foundation, Inc.

This file is part of the non-peer AWT libraries of GNU Classpath.

This library is free software; you can redistribute it and/or modify
it under the terms of the GNU Library General Public License as published 
by the Free Software Foundation, either version 2 of the License, or
(at your option) any later verion.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Library General Public License for more details.

You should have received a copy of the GNU Library General Public License
along with this library; if not, write to the Free Software Foundation
Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA. */


package java.awt;

import java.awt.peer.LabelPeer;
import java.awt.peer.ComponentPeer;

/**
  * This component is used for displaying simple text strings that cannot
  * be edited.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Label extends Component implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * Alignment constant aligning the text to the left of its window.
  */
public static final int LEFT = 0;

/**
  * Alignment constant aligning the text in the center of its window.
  */
public static final int CENTER = 1;

/**
  * Alignment constant aligning the text to the right of its window.
  */
public static final int RIGHT = 2;

// Serialization version constant:
private static final long serialVersionUID = 3094126758329070636L;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial Indicates the alignment of the text within this label's window.
  * This is one of the constants in this class.  The default value is 
  * <code>LEFT</code>.
  */
private int alignment;

/**
  * @serial The text displayed in the label
  */
private String text;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Label</code> with no text.
  */
public
Label()
{
  this("", LEFT);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Label</code> with the specified
  * text that is aligned to the left.
  *
  * @param text The text of the label.
  */
public
Label(String text)
{
  this(text, LEFT);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Label</code> with the specified
  * text and alignment.
  *
  * @param text The text of the label.
  * @param alignment The desired alignment for the text in this label,
  * which must be one of <code>LEFT</code>, <code>CENTER</code>, or
  * <code>RIGHT</code>.
  */
public
Label(String text, int alignment)
{
  this.text = text;
  this.alignment = alignment;
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * Returns the constant indicating the alignment of the text in this
  * label.  The value returned will be one of the alignment constants
  * from this class.
  *
  * @return The alignment of the text in the label.
  */
public int
getAlignment()
{
  return(alignment);
}

/*************************************************************************/

/**
  * Sets the text alignment of this label to the specified value.
  *
  * @param alignment The desired alignment for the text in this label,
  * which must be one of <code>LEFT</code>, <code>CENTER</code>, or
  * <code>RIGHT</code>.
  */
public synchronized void
setAlignment(int alignment)
{
  this.alignment = alignment;

  LabelPeer lp = (LabelPeer)getPeer();
  if (lp != null)
    lp.setAlignment(alignment);
}

/*************************************************************************/

/**
  * Returns the text displayed in this label.
  *
  * @return The text for this label.
  */
public String
getText()
{
  return(text);
}

/*************************************************************************/

/**
  * Sets the text in this label to the specified value.
  *
  * @param text The new text for this label.
  */
public synchronized void
setText(String text)
{
  this.text = text;

  LabelPeer lp = (LabelPeer)getPeer();
  if (lp != null)
    lp.setText(text);
}

/*************************************************************************/

/**
  * Notifies this lable that it has been added to a container, causing
  * the peer to be created.  This method is called internally by the AWT
  * system.
  */
public void
addNotify()
{
  setPeer((ComponentPeer)getToolkit().createLabel(this));
}

/*************************************************************************/

/**
  * Returns a parameter string useful for debugging.
  *
  * @param A debugging string.
  */
protected String
paramString()
{
  return(getClass().getName() + "(text=" + getText() + ",alignment=" +
         getAlignment() + ")");
}

} // class Label

