/*************************************************************************
/* AppletStub.java -- Low level interface to the browser.
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

package java.applet;

import java.net.URL;

/**
  * This interface is the low level interface between the applet and the
  * browser.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface AppletStub
{

/**
  * Returns the URL of the document this applet is embedded in.
  *
  * @return The URL of the document this applet is embedded in.
  */
public abstract URL
getDocumentBase();

/*************************************************************************/

/**
  * Returns the URL of the code base for this applet.
  *
  * @return The URL of the code base for this applet.
  */
public abstract URL
getCodeBase();

/*************************************************************************/

/**
  * Returns the value of the specified parameter that was specified in 
  * the &lt;APPLET&gt; tag for this applet.
  *
  * @param name The parameter name.
  *
  * @param value The parameter value, or <code>null</code> if the parameter
  * does not exist.
  */
public abstract String
getParameter(String name);

/*************************************************************************/

/**
  * Returns the applet context for this applet.
  *
  * @return The applet context for this applet.
  */
public abstract AppletContext
getAppletContext();

/*************************************************************************/

/**
  * Tests whether or not this applet is currently active.
  *
  * @return <code>true</code> if this applet is active, <code>false</code>
  * otherwise.
  */
public abstract boolean
isActive();

/*************************************************************************/

/**
  * Requests that the applet window for this applet be resized.
  *
  * @param width The new width in pixels.
  * @param height The new height in pixels.
  */
public abstract void
appletResize(int width, int height);

} // interface AppletStub

