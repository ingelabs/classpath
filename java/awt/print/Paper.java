/*************************************************************************
/* Paper.java -- Information about a paper type.
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

package java.awt.print;

/**
  * This class describes a particular type of paper.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Paper
{

/*
 * Instance Variables
 */

// Height of the paper
private double height;

// Width of the paper
private double width;

// Upper left imageable X coordinate
private double imageableX;

// Upper left imageable Y coordinate
private double imageableY;

// Imageable width of the page
private double imageableWidth;

// Imageable height of the page
private double imageableHeight;

/*************************************************************************/

/*
 * Constructor
 */

/**
  * This method creates a letter sized paper with one inch margins
  */
public
Paper()
{
  width = 8.5 * 72;
  height = 11 * 72;
  imageableX = 72;
  imageableY = 72;
  imageableWidth = width - (2 * 72);
  imageableHeight = height - (2 * 72);
}

/*************************************************************************/

/**
  * This method returns the height of the paper in 1/72nds of an inch.
  *
  * @return The height of the paper in 1/72nds of an inch.
  */
public double
getHeight()
{
  return(height);
}

/*************************************************************************/

/**
  * Returns the width of the paper in 1/72nds of an inch.
  *
  * @return The width of the paper in 1/72nds of an inch.
  */
public double
getWidth()
{
  return(width);
}

/*************************************************************************/

/**
  * This method returns the X coordinate of the upper left hand corner
  * of the imageable area of the paper.
  *
  * @return The X coordinate of the upper left hand corner of the imageable
  * area of the paper.
  */
public double
getImageableX()
{
  return(imageableX);
}

/*************************************************************************/

/**
  * This method returns the Y coordinate of the upper left hand corner
  * of the imageable area of the paper.
  *
  * @return The Y coordinate of the upper left hand corner of the imageable
  * area of the paper.
  */
public double
getImageableY()
{
  return(imageableY);
}

/*************************************************************************/

/**
  * Returns the width of the imageable area of the paper.
  *
  * @return The width of the imageable area of the paper.
  */
public double
getImageableWidth()
{
  return(imageableWidth);
}

/*************************************************************************/

/**
  * Returns the height of the imageable area of the paper.
  *
  * @return The height of the imageable area of the paper.
  */
public double
getImageableHeight()
{
  return(imageableHeight);
}

/*************************************************************************/

/**
  * This method sets the size of the paper to the specified width and
  * height, which are specified in 1/72nds of an inch.
  *
  * @param width The width of the paper in 1/72nds of an inch.
  * @param height The height of the paper in 1/72nds of an inch.
  */
public void
setSize(double width, double height)
{
  this.width = width;
  this.height = height;
}

/*************************************************************************/

/**
  * This method sets the imageable area of the paper by specifying the
  * coordinates of the upper left hand corner of that area, and its
  * length and height.  All values are in 1/72nds of an inch.
  *
  * @param imageableX The X coordinate of the upper left hand corner of
  * the imageable area, in 1/72nds of an inch.
  * @param imageableY The Y coordinate of the upper left hand corner of
  * the imageable area, in 1/72nds of an inch.
  * @param imageableWidth The width of the imageable area of the paper, 
  * in 1/72nds of an inch.
  * @param imageableHeight The heigth of the imageable area of the paper, 
  * in 1/72nds of an inch.
  */
public void
setImageableArea(double imageableX, double imageableY, 
                 double imageableWidth, double imageableHeight)
{
  this.imageableX = imageableX;
  this.imageableY = imageableY;
  this.imageableWidth = imageableWidth;
  this.imageableHeight = imageableHeight;
}

/*************************************************************************/

/**
  * This method creates a copy of this object.
  *
  * @return A copy of this object.
  */
public Object
clone()
{
  try
    {
      return(super.clone());
    }
  catch(CloneNotSupportedException e)
    {
      return(null);
    }
}

} // class Paper

