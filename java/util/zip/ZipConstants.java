/* java.util.zip.ZipConstants
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

interface ZipConstants
{
    /* The local file header */
    public final static int L_SIZE    = 30;
    public final static int L_HDR_SIG = 'P'|('K'<<8)|(3<<16)|(4<<24);

    public final static int L_SIGNATURE                      =  0;
    public final static int L_VERSION_NEEDED_TO_EXTRACT      =  4;
    public final static int L_GENERAL_PURPOSE_BIT_FLAG       =  6;
    public final static int L_COMPRESSION_METHOD             =  8;
    public final static int L_LAST_MOD_FILE_TIME             = 10;
    public final static int L_LAST_MOD_FILE_DATE             = 12;
    public final static int L_CRC32                          = 14;
    public final static int L_COMPRESSED_SIZE                = 18;
    public final static int L_UNCOMPRESSED_SIZE              = 22;
    public final static int L_FILENAME_LENGTH                = 26;
    public final static int L_EXTRA_FIELD_LENGTH             = 28;

    /* The Data descriptor */
    public final static int D_HDR_SIG = 'P'|('K'<<8)|(7<<16)|(8<<24);
    public final static int D_SIGNATURE                      =  0;
    public final static int D_CRC32                          =  4;
    public final static int D_COMPRESSED_SIZE                =  8;
    public final static int D_UNCOMPRESSED_SIZE              = 12;
    public final static int D_SIZE                           = 16;

    /* The central directory file header */
    public final static int C_HDR_SIG = 'P'|('K'<<8)|(1<<16)|(2<<24);

    public final static int C_SIGNATURE                      =  0;
    public final static int C_VERSION_MADE_BY                =  4;
    public final static int C_VERSION_NEEDED_TO_EXTRACT      =  6;
    public final static int C_GENERAL_PURPOSE_BIT_FLAG       =  8;
    public final static int C_COMPRESSION_METHOD             = 10;
    public final static int C_LAST_MOD_FILE_TIME             = 12;
    public final static int C_LAST_MOD_FILE_DATE             = 14;
    public final static int C_CRC32                          = 16;
    public final static int C_COMPRESSED_SIZE                = 20;
    public final static int C_UNCOMPRESSED_SIZE              = 24;
    public final static int C_FILENAME_LENGTH                = 28;
    public final static int C_EXTRA_FIELD_LENGTH             = 30;
    public final static int C_FILE_COMMENT_LENGTH            = 32;
    public final static int C_DISK_NUMBER_START              = 34;
    public final static int C_INTERNAL_FILE_ATTRIBUTES       = 36;
    public final static int C_EXTERNAL_FILE_ATTRIBUTES       = 38;
    public final static int C_RELATIVE_OFFSET_LOCAL_HEADER   = 42;
    public final static int C_SIZE                           = 46;

    /* The entries in the end of central directory */
    public final static int EC_HDR_SIG = 'P'|('K'<<8)|(5<<16)|(6<<24);

    public final static int EC_SIGNATURE                        =  0;
    public final static int EC_NUMBER_THIS_DISK                 =  4;
    public final static int EC_NUM_DISK_WITH_START_CENTRAL_DIR  =  6;
    public final static int EC_NUM_ENTRIES_CENTRL_DIR_THS_DISK  =  8;
    public final static int EC_TOTAL_ENTRIES_CENTRAL_DIR        = 10;
    public final static int EC_SIZE_CENTRAL_DIRECTORY           = 12;
    public final static int EC_OFFSET_START_CENTRAL_DIRECTORY   = 16;
    public final static int EC_ZIPFILE_COMMENT_LENGTH           = 20;
    public final static int EC_SIZE                             = 22;
}

