/*************************************************************************
/* Main.java -- Implementation of serialver program
/*
/* Copyright (c) 1998 by Free Software Foundation, Inc.
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, version 2. (see COPYING.LIB)
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU General Public License for more details.
/*
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package gnu.tools.serialver;

import java.io.ObjectStreamClass;

/**
   This class is an implementation of the `serialver' program.  Any
   number of class names can be passed as arguments, and the serial
   version unique identitfier for each class will be printed in a
   manner suitable for cuting and pasting into a Java source file.
*/
public class Main
{
  public static void main( String[] args )
  {
    if( args.length == 0 )
    {
      System.out.println( "Usage: serialver classname(s)" );
      return;
    }

    Class clazz;
    ObjectStreamClass osc;
    for( int i=0; i < args.length; i++ )
    {
      try
      {
	clazz = Class.forName( args[i] );
	osc = ObjectStreamClass.lookup( clazz );
	
	if( osc != null )
	  System.out.println( clazz.getName() + ": " 
			      + "static final long serialVersionUID = " 
			      + osc.getSerialVersionUID() + "L;" );
	else
	  System.err.println( "Class " + args[i] + " is not serializable" );
      }
      catch( ClassNotFoundException e )
      {
	System.err.println( "Class for " + args[i] + " not found" );
      }
    }
  }
}
