/*************************************************************************
/* WindowListener.java -- Class for listening for window events.
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

package java.awt.event;

/**
  * This interface is for classes that wish to monitor events for
  * window changes.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface WindowListener extends java.util.EventListener
{

/**
  * This method is called when a window is activated.
  *
  * @param event The <code>WindowEvent</code> indicating the activation.
  */
public abstract void
windowActivated(WindowEvent event);

/*************************************************************************/

/**
  * This method is called when the window is deactivated.
  *
  * @param event The <code>WindowEvent</code> indicating the deactivation.
  */
public abstract void
windowDeactivated(WindowEvent event);

/*************************************************************************/

/**
  * This method is called when the window is made visible.
  *
  * @param event The <code>WindowEvent</code> indicating the visibility change.
  */
public abstract void
windowOpened(WindowEvent event);

/*************************************************************************/

/**
  * This method is called when the user calls the system menu close
  * function.
  *
  * @param event The <code>WindowEvent</code> indicating the close attempt.
  */
public abstract void
windowClosing(WindowEvent event);

/*************************************************************************/

/**
  * This method is called when the window is closed.
  *
  * @param event The <code>WindowEvent</code> indicating the closing.
  */
public abstract void
windowClosed(WindowEvent event);

/*************************************************************************/

/**
  * This method is called when the window is iconified.
  *
  * @param event The <code>WindowEvent</code> indicating the iconification.
  */
public abstract void
windowIconified(WindowEvent event);

/*************************************************************************/

/**
  * This method is called when the window is deiconified.
  *
  * @param event The <code>WindowEvent</code> indicating the deiconification.
  */
public abstract void
windowDeiconified(WindowEvent event);

} // interface WindowListener

