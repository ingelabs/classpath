/*************************************************************************
/* UnsupportedFlavorException.java -- Data flavor is not valid.
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

package java.awt.datatransfer;

/**
  * The data flavor requested is not supported for the transfer data.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class UnsupportedFlavorException extends Exception 
{

/**
  * Initializes a new instance of <code>UnsupportedDataFlavor</code>
  * for the specified data flavor.
  *
  * @param flavor The data flavor that is not supported.
  */
public
UnsupportedFlavorException(DataFlavor flavor)
{
  super(flavor.getHumanPresentableName());
}

} // class UnsupportedFlavorException 

