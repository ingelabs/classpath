/*************************************************************************
/* TextComponent.java -- Widgets for entering text
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

import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.peer.TextComponentPeer;

/**
  * This class provides common functionality for widgets than 
  * contain text.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class TextComponent extends Component implements java.io.Serializable
{

/*
 * Static Variables
 */

// Constant for serialization
private static final long serialVersionUID = -2214773872412987419L;

/*
 * Instance Variables
 */

/**
  * @serial Indicates whether or not this component is editable.
  */
private boolean editable;

/**
  * @serial The starting position of the selected text region.
  */
private int selectionStart;

/**
  * @serial The ending position of the selected text region.
  */
private int selectionEnd;

/**
  * @serial The text in the component
  */
private String text;

/**
  * A list of listeners that will receive events from this object.
  */
protected transient TextListener textListener;

/*************************************************************************/

/*
 * Constructors
 */

TextComponent(String text)
{
  this.text = text;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the text in this component
  *
  * @return The text in this component.
  */
public synchronized String
getText()
{
  TextComponentPeer tcp = (TextComponentPeer)getPeer();
  if (tcp != null)
    text = tcp.getText();

  return(text);
}

/*************************************************************************/

/**
  * Sets the text in this component to the specified string.
  *
  * @param text The new text for this component.
  */
public synchronized void
setText(String text)
{
  if (text == null)
    text = "";

  this.text = text;

  TextComponentPeer tcp = (TextComponentPeer)getPeer();
  if (tcp != null)
    tcp.setText(text);
}

/*************************************************************************/

/**
  * Returns a string that contains the text that is currently selected.
  *
  * @return The currently selected text region.
  */
public synchronized String
getSelectedText()
{
  String alltext = getText();
  int start = getSelectionStart();
  int end = getSelectionEnd();
  
  return(alltext.substring(start, end));
}

/*************************************************************************/

/**
  * Returns the starting position of the selected text region.
  * // FIXME: What is returned if there is no selected text?
  *
  * @return The starting position of the selected text region.
  */
public synchronized int
getSelectionStart()
{
  TextComponentPeer tcp = (TextComponentPeer)getPeer();
  if (tcp != null)
    selectionStart = tcp.getSelectionStart();

  return(selectionStart);
}

/*************************************************************************/

/**
  * Sets the starting position of the selected region to the
  * specified value.  If the specified value is out of range, then it
  * will be silently changed to the nearest legal value.
  *
  * @param selectionStart The new start position for selected text.
  */
public synchronized void
setSelectionStart(int selectionStart)
{
  select(selectionStart, getSelectionEnd());
}

/*************************************************************************/

/**
  * Returns the ending position of the selected text region.
  * // FIXME: What is returned if there is no selected text.
  *
  * @return The ending position of the selected text region.
  */
public synchronized int
getSelectionEnd()
{
  TextComponentPeer tcp = (TextComponentPeer)getPeer();
  if (tcp != null)
    selectionEnd = tcp.getSelectionEnd();

  return(selectionEnd);
}

/*************************************************************************/

/**
  * Sets the ending position of the selected region to the
  * specified value.  If the specified value is out of range, then it
  * will be silently changed to the nearest legal value.
  *
  * @param selectionEnd The new start position for selected text.
  */
public synchronized void
setSelectionEnd(int selectionEnd)
{
  select(getSelectionStart(), selectionEnd);
}

/*************************************************************************/

/**
  * This method sets the selected text range to the text between the
  * specified start and end positions.  Illegal values for these
  * positions are silently fixed.
  *
  * @param startSelection The new start position for the selected text.
  * @param endSelection The new end position for the selected text.
  */
public synchronized void
select(int selectionStart, int endSelection)
{
  if (selectionStart < 0)
    selectionStart = 0;

  if (selectionStart > getText().length())
    selectionStart = text.length();

  if (selectionEnd > text.length())
    selectionEnd = text.length();

  if (selectionStart > getSelectionEnd())
    selectionStart = selectionEnd;

  this.selectionStart = selectionStart;
  this.selectionEnd = selectionEnd;

  TextComponentPeer tcp = (TextComponentPeer)getPeer();
  if (tcp != null)
    tcp.select(selectionStart, selectionEnd);
}

/*************************************************************************/

/**
  * Selects all of the text in the component.
  */
public synchronized void
selectAll()
{
  select(0, getText().length());
}

/*************************************************************************/

/**
  * Returns the current caret position in the text.
  *
  * @return The caret position in the text.
  */
public synchronized int
getCaretPosition()
{
  TextComponentPeer tcp = (TextComponentPeer)getPeer();
  if (tcp != null)
    return(tcp.getCaretPosition());
  else
    return(0);
}

/*************************************************************************/

/**
  * Sets the caret position to the specified value.
  *
  * @param caretPosition The new caret position.
  */
public synchronized void
setCaretPosition(int caretPosition)
{
  TextComponentPeer tcp = (TextComponentPeer)getPeer();
  if (tcp != null)
    tcp.setCaretPosition(caretPosition);
}

/*************************************************************************/

/**
  * Tests whether or not this component's text can be edited.
  *
  * @return <code>true</code> if the text can be edited, <code>false</code>
  * otherwise.
  */
public boolean
isEditable()
{
  return(editable);
}

/*************************************************************************/

/**
  * Sets whether or not this component's text can be edited.
  *
  * @param editable <code>true</code> to enable editing of the text,
  * <code>false</code> to disable it.
  */
public synchronized void
setEditable(boolean editable)
{
  this.editable = editable;

  TextComponentPeer tcp = (TextComponentPeer)getPeer();
  if (tcp != null)
    tcp.setEditable(editable);
}

/*************************************************************************/

/**
  * Notifies the component that it should destroy its native peer.
  */
public void
removeNotify()
{
  super.removeNotify();
}

/*************************************************************************/

/**
  * Adds a new listener to the list of text listeners for this
  * component.
  *
  * @param listener The listener to be added.
  */
public synchronized void
addTextListener(TextListener listener)
{
  textListener = AWTEventMulticaster.add(textListener, listener);

  enableEvents(AWTEvent.TEXT_EVENT_MASK);  
}

/*************************************************************************/

/**
  * Removes the specified listener from the list of listeners
  * for this component.
  *
  * @param listener The listener to remove.
  */
public synchronized void
removeTextListener(TextListener listener)
{
  textListener = AWTEventMulticaster.remove(textListener, listener);
}

/*************************************************************************/

/**
  * Processes the specified event for this component.  Text events are
  * processed by calling the <code>processTextEvent()</code> method.
  * All other events are passed to the superclass method.
  * 
  * @param event The event to process.
  */
protected void
processEvent(AWTEvent event)
{
  if (event instanceof TextEvent)
    processTextEvent((TextEvent)event);
}

/*************************************************************************/

/**
  * Processes the specified text event by dispatching it to any listeners
  * that are registered.  Note that this method will only be called
  * if text event's are enabled.  This will be true if there are any
  * registered listeners, or if the event has been specifically
  * enabled using <code>enableEvents()</code>.
  *
  * @param event The text event to process.
  */
protected void
processTextEvent(TextEvent event)
{
  if (textListener != null)
    textListener.textValueChanged(event);
}

/*************************************************************************/

/**
  * Returns a debugging string.
  *
  * @return A debugging string.
  */
protected String
paramString()
{
  return(getClass().getName() + "(text=" + getText() + ")");
}

} // class TextComponent

