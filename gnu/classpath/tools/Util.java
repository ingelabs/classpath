/* gnu.classpath.tools.Util
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
02111-1307 USA. */

package gnu.classpath.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

public class Util
{
  public static InputStream getInputStream (String filename, String classpath)
  {
    StringTokenizer st = new StringTokenizer (classpath, File.pathSeparator);
    while (st.hasMoreTokens ())
      {
	String path = st.nextToken ();
	File f = new File (path);
	if (f.exists () && f.isDirectory ())
	  {
	    f = new File (f, filename);
	    if (f.exists ())
	      {
		try
		  {
		    FileInputStream fis = new FileInputStream (f);
		    return new BufferedInputStream (fis);
		  }
		catch (FileNotFoundException fe) { }
	      }
	  }	
	else
	  {
	    if (f.exists () && f.isFile ())
	      {
		try
		  {
		    ZipFile zip = new ZipFile (f);
		    ZipEntry entry = zip.getEntry (filename);
		    if (entry != null)
		      return zip.getInputStream (entry);
		  }
		catch (IllegalStateException ise) { }
		catch (ZipException ze) { }
		catch (IOException ioe) { }		
	      }
	  }
      }
    return null;
  }
}
