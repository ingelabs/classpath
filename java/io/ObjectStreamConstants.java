/*************************************************************************
/* ObjectStreamConstants.java -- Interface containing constant values
/*                               used in reading and writing
/*				 serialized objects
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

package java.io;

/**
   This interface contains constants that are used in object
   serialization.  This interface is used by ObjectOutputStream,
   ObjectInputStream, ObjectStreamClass, and possibly other classes.
   The values for these constants are specified in Javasoft's "Object
   Serialization Specification" TODO: add reference
*/
interface ObjectStreamConstants
{
  final static short STREAM_MAGIC = (short)0xaced;
  final static short STREAM_VERSION = 5;
  final static byte TC_NULL = (byte)0x70;
  final static byte TC_REFERENCE = (byte)0x71;
  final static byte TC_CLASSDESC = (byte)0x72;
  final static byte TC_OBJECT = (byte)0x73;
  final static byte TC_STRING = (byte)0x74;
  final static byte TC_ARRAY = (byte)0x75;
  final static byte TC_CLASS = (byte)0x76;
  final static byte TC_BLOCKDATA = (byte)0x77;
  final static byte TC_ENDBLOCKDATA = (byte)0x78;
  final static byte TC_RESET = (byte)0x79;
  final static byte TC_BLOCKDATALONG = (byte)0x7A;
  final static byte TC_EXCEPTION = (byte)0x7B;
  final static byte SC_WRITE_METHOD = 0x01;
  final static byte SC_SERIALIZABLE = 0x02;
  final static byte SC_EXTERNALIZABLE = 0x04;
}
