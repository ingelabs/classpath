/* ClassLoaderHelper.java -- aids ClassLoader in finding resources
   Copyright (C) 1998, 2002 Free Software Foundation, Inc.

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

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package gnu.java.lang;

import java.io.*;
import java.util.*;
import java.net.*;
import gnu.java.io.PlatformHelper;

/**
 * ClassLoaderHelper has various methods that ought to have been
 * in ClassLoader.
 *
 * @author John Keiser
 * @author Eric Blake <ebb9@email.byu.edu>
 */
public class ClassLoaderHelper
{
  /**
   * Saves File instances mapping to specific absolute paths and jar urls.
   */
  private static final Hashtable fileResourceCache = new Hashtable();

  private static final int READBUFSIZE = 1024;

  /**
   * Searches the CLASSPATH for a resource and returns it as a File.
   *
   * @param name name of resource to locate
   *
   * @return a File object representing the resource, or null
   * if the resource could not be found
   */
  public static final File getSystemResourceAsFile(String name) {
    if (name.charAt(0) == '/')
      name = name.substring(1);

    String path = System.getProperty("java.class.path", ".");
    StringTokenizer st
      = new StringTokenizer(path, System.getProperty("path.separator", ":"));
    while (st.hasMoreElements())
      {
        String token = st.nextToken();
        File file;
        if (token.endsWith(".zip") || token.endsWith(".jar"))
          {
            file = new File(token);
            if (! file.exists())
              continue;
            path = file.getAbsolutePath();
            try
              {
                if (path.charAt(0) == '/')
                  path = "jar:file:/" + path + "!/" + name;
                else
                  path = "jar:file://" + path + "!/" + name;
                file = (File) fileResourceCache.get(path);
                if (file == null)
                  {
                    //load jar/zip entry from the url
                    URL url = new URL(path);
                    URLConnection urlconn = url.openConnection();
                    InputStream is = urlconn.getInputStream();
                    byte[] buf = new byte[READBUFSIZE];
                    file = File.createTempFile("tmp", "", new File("."));
                    FileOutputStream fos = new FileOutputStream(file);
                    int len = 0;
                    while ((len = is.read(buf)) != -1)
                      fos.write(buf);
                    fos.close();
                  }
              }
            catch(Exception e)
              {
                continue;
              }
          }
        else
          {
            if (PlatformHelper.endWithSeparator(token))
              path = token + name;
            else
              path = token + File.separator + name;
            file = (File) fileResourceCache.get(path);
            if (file == null)
              file = new File(path);
          }
        if (file != null && file.isFile())
          {
            fileResourceCache.put(path, file);
            return file;
          }
      }
    return null;
  }
} // class ClassLoaderHelper
