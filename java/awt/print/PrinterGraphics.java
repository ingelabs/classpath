/*************************************************************************
/* PrinterGraphics.java -- Hook to return print job controller.
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
  * This interface is implemented by the <code>Graphics</code> instance
  * that is used for rendering pages.  It provides a hook to return the
  * object that is controlling the print job.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface PrinterGraphics
{

/**
  * This method returns the instance of <code>PrinterJob</code> that is
  * controlling this print job.
  *
  * @return The <code>PrinterJob</code> that is controlling this print job.
  */
public abstract PrinterJob
getPrinterJob();

} // interface PrinterGraphics

