/* java.util.zip.GZIPConstants
   Copyright (C) 2001 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */

package java.util.zip;

interface GZIPConstants {

  /* Magic number found at start of GZIP header */

  public static final int GZIP_MAGIC = 0x1f8b;

  /*    The flag byte is divided into individual bits as follows:
	
	bit 0   FTEXT
	bit 1   FHCRC
	bit 2   FEXTRA
	bit 3   FNAME
	bit 4   FCOMMENT
	bit 5   reserved
	bit 6   reserved
	bit 7   reserved
	*/

  public static final int FTEXT = 0x1;
  public static final int FHCRC = 0x2;
  public static final int FEXTRA = 0x4;
  public static final int FNAME = 0x8;
  public static final int FCOMMENT = 0x10;

}
