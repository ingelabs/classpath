/* java.util.zip.DeflaterConstants
   Copyright (C) 2001 Free Software Foundation, Inc.

This file is part of Jazzlib.

Jazzlib is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

Jazzlib is distributed in the hope that it will be useful, but
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

interface DeflaterConstants {
  final static boolean DEBUGGING = false;

  final static int STORED_BLOCK = 0;
  final static int STATIC_TREES = 1;
  final static int DYN_TREES    = 2;
  final static int PRESET_DICT  = 0x20;

  final static int DEFAULT_MEM_LEVEL = 8;

  final static int MAX_MATCH = 258;
  final static int MIN_MATCH = 3;

  final static int MAX_WBITS = 15;
  final static int WSIZE = 1 << MAX_WBITS;
  final static int WMASK = WSIZE - 1;

  final static int HASH_BITS = DEFAULT_MEM_LEVEL + 7;
  final static int HASH_SIZE = 1 << HASH_BITS;
  final static int HASH_MASK = HASH_SIZE - 1;
  final static int HASH_SHIFT = (HASH_BITS + MIN_MATCH - 1) / MIN_MATCH;

  final static int MIN_LOOKAHEAD = MAX_MATCH + MIN_MATCH + 1;
  final static int MAX_DIST = WSIZE - MIN_LOOKAHEAD;

  final static int PENDING_BUF_SIZE = 1 << (DEFAULT_MEM_LEVEL + 8);
  final static int MAX_BLOCK_SIZE = Math.min(65535, PENDING_BUF_SIZE-5);

  final static int DEFLATE_STORED = 0;
  final static int DEFLATE_FAST   = 1;
  final static int DEFLATE_SLOW   = 2;

  final static int GOOD_LENGTH[] = { 0,4, 4, 4, 4, 8,  8,  8,  32,  32 };
  final static int MAX_LAZY[]    = { 0,4, 5, 6, 4,16, 16, 32, 128, 258 };
  final static int NICE_LENGTH[] = { 0,8,16,32,16,32,128,128, 258, 258 };
  final static int MAX_CHAIN[]   = { 0,4, 8,32,16,32,128,256,1024,4096 };
  final static int COMPR_FUNC[]  = { 0,1, 1, 1, 1, 2,  2,  2,   2,   2 };
}
