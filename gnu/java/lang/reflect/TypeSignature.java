/*************************************************************************
/* TypeSignature.java -- Class used to compute type signatures
/*
/* Copyright (c) 1998 by Geoffrey C. Berry (gcb@cs.duke.edu)
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

//TODO: comments
//      test suite

package gnu.java.lang.reflect;

import java.lang.reflect.*;

public class TypeSignature
{
  public static String getEncodingOfClass( Class clazz )
  {
    if( clazz.isPrimitive() )
    {
      if( clazz == Boolean.TYPE )
	return "Z";
      if( clazz == Byte.TYPE )
	return "B";
      if( clazz == Character.TYPE )
	return "C";
      if( clazz == Double.TYPE )
	return "D";
      if( clazz == Float.TYPE )
	return "F";
      if( clazz == Integer.TYPE )
	return "I";
      if( clazz == Long.TYPE )
	return "L";
      if( clazz == Short.TYPE )
	return "S";
      if( clazz == Void.TYPE )
	return "V";
      else
	throw new RuntimeException( "Unknown primitive class " + clazz );
    }
    else if( clazz.isArray() )
    {
      return '[' + getEncodingOfClass( clazz.getComponentType() );
    }
    else
    {
      String classname = clazz.getName();
      int name_len = classname.length();
      char[] buf = new char[ name_len + 2 ];
      buf[0] = 'L';
      classname.getChars( 0, name_len, buf, 1 );
      
      int i;
      for( i=1; i <= name_len; i++ )
      {
	if( buf[i] == '.' )
	  buf[i] = '/';
      }
      
      buf[i] = ';';
      return new String( buf );
    }
  }

  
  public static String getEncodingOfMethod( Method m )
  {
    String returnEncoding = getEncodingOfClass( m.getReturnType() );
    Class[] paramTypes = m.getParameterTypes();
    String[] paramEncodings = new String[ paramTypes.length ];

    String paramEncoding;
    int size = 2; // make room for parens
    for( int i=0; i < paramTypes.length; i++ )
    {
      paramEncoding = getEncodingOfClass( paramTypes[i] );
      size += paramEncoding.length();
      paramEncodings[i] = paramEncoding;
    }
    
    size += returnEncoding.length();

    StringBuffer buf = new StringBuffer( size );
    buf.append( '(' );
    
    for( int i=0; i < paramTypes.length; i++ )
    {
      buf.append( paramEncodings[i] );
    }
    
    buf.append( ')' );
    buf.append( returnEncoding );
    
    return buf.toString();
  }


  public static String getEncodingOfConstructor( Constructor c )
  {
    Class[] paramTypes = c.getParameterTypes();
    String[] paramEncodings = new String[ paramTypes.length ];

    String paramEncoding;
    int size = 3; // make room for parens and V for return type
    for( int i=0; i < paramTypes.length; i++ )
    {
      paramEncoding = getEncodingOfClass( paramTypes[i] );
      size += paramEncoding.length();
      paramEncodings[i] = paramEncoding;
    }
    
    StringBuffer buf = new StringBuffer( size );
    buf.append( '(' );
    
    for( int i=0; i < paramTypes.length; i++ )
    {
      buf.append( paramEncodings[i] );
    }
    
    buf.append( ")V" );
    
    return buf.toString();
  }


  public static String getEncodingOfMember( Member mem )
  {
    if( mem instanceof Constructor )
      return getEncodingOfConstructor( (Constructor)mem );
    if( mem instanceof Method )
      return getEncodingOfMethod( (Method)mem );
    else // Field
      return getEncodingOfClass( ((Field)mem).getType() );
  }
}

	

