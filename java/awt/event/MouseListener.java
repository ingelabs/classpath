/*************************************************************************
/* MouseListener.java -- Listen for mouse events other than motion
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
  * This interface is for classes that wish to receive mouse events other
  * than simple motion events.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface MouseListener extends java.util.EventListener
{

/**
  * This method is called when the mouse is clicked on a component.
  *
  * @param event The <code>MouseEvent</code> indicating the click.
  */
public abstract void
mouseClicked(MouseEvent event);

/*************************************************************************/

/**
  * This method is called when the mouse enters a component.
  *
  * @param event The <code>MouseEvent</code> for the entry.
  */
public abstract void
mouseEntered(MouseEvent event);

/*************************************************************************/

/** 
  * This method is called when the mouse exits a component.
  *
  * @param event The <code>MouseEvent</code> for the exit.
  */
public abstract void
mouseExited(MouseEvent event);

/*************************************************************************/

/**
  * This method is called when the mouse is pressed over a component.
  *
  * @param event The <code>MouseEvent</code> for the press.
  */
public abstract void
mousePressed(MouseEvent event);

/*************************************************************************/

/**
  * This method is called when the mouse is released over a component.
  *
  * @param event The <code>MouseEvent</code> for the release.
  */
public abstract void
mouseReleased(MouseEvent event);

} // interface MouseListener

