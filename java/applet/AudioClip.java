/*************************************************************************
/* AudioClip.java -- Play an audio clip.
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

/**
  * This interface provides a simple mechanism for playing audio clips.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface AudioClip
{

/**
  * Plays the audio clip starting from the beginning.
  */
public abstract void
play();

/*************************************************************************/

/**
  * Stops playing this audio clip.  There is no mechanism for restarting
  * at the point where the clip is stopped.
  */
public abstract void
stop();

/*************************************************************************/

/**
  * Plays this audio clip in a continuous loop.
  */
public abstract void
loop();

} // interface AudioClip

