/*************************************************************************
/* ObjectStreamConstants.java -- Interface containing constant values
/*                               used in reading and writing
/*				 serialized objects
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

package java.io;

/**
   This interface contains constants that are used in object
   serialization.  This interface is used by ObjectOutputStream,
   ObjectInputStream, ObjectStreamClass, and possibly other classes.
   The values for these constants are specified in Javasoft's "Object
   Serialization Specification" TODO: add reference
*/
public interface ObjectStreamConstants
{
  public final static short STREAM_MAGIC = (short)0xaced;
  public final static short STREAM_VERSION = 5;
  public final static byte TC_NULL = (byte)112;
  public final static byte TC_REFERENCE = (byte)113;
  public final static byte TC_CLASSDESC = (byte)114;
  public final static byte TC_OBJECT = (byte)115;
  public final static byte TC_STRING = (byte)116;
  public final static byte TC_ARRAY = (byte)117;
  public final static byte TC_CLASS = (byte)118;
  public final static byte TC_BLOCKDATA = (byte)119;
  public final static byte TC_ENDBLOCKDATA = (byte)120;
  public final static byte TC_RESET = (byte)121;
  public final static byte TC_BLOCKDATALONG = (byte)122;
  public final static byte TC_EXCEPTION = (byte)123;
  public final static int baseWireHandle = 0x7e0000;
  public final static byte SC_WRITE_METHOD = (byte)1;
  public final static byte SC_BLOCK_DATA = (byte)1;
  public final static byte SC_SERIALIZABLE = (byte)2;
  public final static byte SC_EXTERNALIZABLE = (byte)4;
}
