/*************************************************************************
/* FileDialog.java -- A filename selection dialog box
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

import java.awt.peer.FileDialogPeer;
import java.awt.peer.DialogPeer;
import java.awt.peer.WindowPeer;
import java.awt.peer.ContainerPeer;
import java.awt.peer.ComponentPeer;
import java.io.FilenameFilter;

/**
  * This class implements a file selection dialog box widget.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class FileDialog extends Dialog implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * Indicates that the purpose of the dialog is for opening a file.
  */
public static final int LOAD = 0;

/**
  * Indicates that the purpose of the dialog is for saving a file.
  */
public static final int SAVE = 1;

// Serialization constant
private static final long serialVersionUID = 5035145889651310422L;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial The directory for this file dialog.
  */
private String dir;

/**
  * @serial The filename for this file dialog
  */
private String file;

/**
  * @serial The filter for selecting filenames to display
  */
private FilenameFilter filter;

/**
  * @serial The mode of this dialog, either <code>LOAD</code> or 
  * <code>SAVE</code>.
  */
private int mode;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>FileDialog</code> with the 
  * specified parent.  This dialog will have no title and will be for
  * loading a file.
  *
  * @param parent The parent frame for this dialog.
  */
public
FileDialog(Frame parent)
{
  this(parent, "", LOAD);
}

/*************************************************************************/

/**
  * Initialized a new instance of <code>FileDialog</code> with the
  * specified parent and title.  This dialog will be for opening a file.
  *
  * @param parent The parent frame for this dialog.
  * @param title The title for this dialog.
  */
public
FileDialog(Frame parent, String title)
{
  this(parent, title, LOAD);
}

/*************************************************************************/

/**
  * Initialized a new instance of <code>FileDialog</code> with the
  * specified parent, title, and mode.
  *
  * @param parent The parent frame for this dialog.
  * @param title The title for this dialog.
  * @param mode The mode of the dialog, either <code>LOAD</code> or
  * <code>SAVE</code>.
  */
public
FileDialog(Frame parent, String title, int mode)
{
  super(parent, title, true);

  if ((mode != LOAD) && (mode != SAVE))
    throw new IllegalArgumentException("Bad mode: " + mode);

  this.mode = mode;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the mode of this dialog, either <code>LOAD</code> or
  * <code>SAVE</code>.
  *
  * @return The mode of this dialog.
  */
public int
getMode()
{
  return(mode);
}

/*************************************************************************/

/**
  * Sets the mode of this dialog to either <code>LOAD</code> or
  * <code>SAVE</code>.  This method is only effective before the native
  * peer is created.
  *
  * @param mode The new mode of this file dialog.
  */
public void
setMode(int mode)
{
  if ((mode != LOAD) && (mode != SAVE))
    throw new IllegalArgumentException("Bad mode: " + mode);

  this.mode = mode;
}

/*************************************************************************/

/**
  * Returns the directory for this file dialog.
  *
  * @return The directory for this file dialog.
  */
public String
getDirectory()
{
  return(dir);
}

/*************************************************************************/

/**
  * Sets the directory for this file dialog.
  *
  * @param dir The new directory for this file dialog.
  */
public synchronized void
setDirectory(String dir)
{
  this.dir = dir;

  FileDialogPeer fdp = (FileDialogPeer)getPeer();
  if (fdp != null)
    fdp.setDirectory(dir);
}

/*************************************************************************/

/**
  * Returns the file that is selected in this dialog.
  *
  * @return The file that is selected in this dialog.
  */
public String
getFile()
{
  return(file);
}

/*************************************************************************/

/**
  * Sets the selected file for this dialog.
  *
  * @param file The selected file for this dialog.
  */
public synchronized void
setFile(String file)
{
  this.file = file;

  FileDialogPeer fdp = (FileDialogPeer)getPeer();
  if (fdp != null)
    fdp.setFile(file);
}

/*************************************************************************/

/**
  * Returns the filename filter being used by this dialog.
  *
  * @param The filename filter being used by this dialog.
  */
public FilenameFilter
getFilenameFilter()
{
  return(filter);
}

/*************************************************************************/

/**
  * Sets the filename filter used by this dialog.
  *
  * @param filter The new filename filter for this file dialog box.
  */
public synchronized void
setFilenameFilter(FilenameFilter filter)
{
  this.filter = filter;

  FileDialogPeer fdp = (FileDialogPeer)getPeer();
  if (fdp != null)
    fdp.setFilenameFilter(filter);
}

/*************************************************************************/

/**
  * Creates the native peer for this file dialog box.
  */
public void
addNotify()
{
  if (getPeer() != null)
    return;

  setPeer((ComponentPeer)getToolkit().createFileDialog(this));
}

/*************************************************************************/

/**
  * Returns a debugging string for this object.
  *
  * @return A debugging string for this object.
  */
protected String
paramString()
{
  return(getClass().getName() + "(dir=" + dir + ",file=" + file +
         ",mode=" + mode + ")");
}

} // class FileDialog 

