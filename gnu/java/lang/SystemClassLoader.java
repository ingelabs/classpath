/* SystemClassLoader.java -- the default system class loader
   Copyright (C) 1998, 1999, 2001, 2002 Free Software Foundation, Inc.

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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

/**
 * The default system class loader. VMs may wish to replace this with a
 * similar class that interacts better with their VM. Also, replacing this
 * class may be necessary when porting to new platforms, like Windows.
 *
 * XXX Should this be merged with ClassLoaderHelper? They both do a similar
 * search of the classpath, including jar/zip files.
 *
 * @author John Keiser
 * @author Mark Wielaard
 * @author Eric Blake <ebb9@email.byu.edu>
 */
public class SystemClassLoader extends ClassLoader
{
  /** A lock for synchronization when reading jars. */
  private static final Object NO_SUCH_ARCHIVE = new Object();

  /** Flag to avoid infinite loops. */
  private static boolean is_trying;

  /**
   * Creates a class loader. Note that the parent may be null, when this is
   * created as the system class loader by ClassLoader.getSystemClassLoader.
   *
   * @param parent the parent class loader
   */
  public SystemClassLoader(ClassLoader parent)
  {
    super(parent);
  }
    
  /**
   * Get the URL to a resource.
   *
   * @param name the name of the resource
   * @return the URL to the resource
   */
  public URL getResource(String name)
  {
    return systemGetResource(name);
  }

  /**
   * Get the URL to a resource.
   *
   * @param name the name of the resource
   * @return the URL to the resource
   */
  public static URL systemGetResource(String name)
  {
    if (name.charAt(0) == '/')
      name = name.substring(1);
    String cp = System.getProperty("java.class.path", ".");
    StringTokenizer st = new StringTokenizer(cp, File.pathSeparator);
    while (st.hasMoreTokens())
      {
        String path = st.nextToken();
        if (path.toLowerCase().endsWith(".zip") ||
            path.toLowerCase().endsWith(".jar"))
          {
            if (is_trying) // Avoid infinite loop.
              continue;
            File f = new File(path);
            if (! f.exists())
              continue;
            path = f.getAbsolutePath();
            ZipFile zf;
            try
              {
                synchronized (NO_SUCH_ARCHIVE)
                  {
                    is_trying = true;
                    zf = gnu.java.net.protocol.jar.JarURLConnection
                      .JarFileCache.get(new URL("file://" + path));
                    is_trying = false;
                  }
                if (zf == null)
                  continue;
              }
            catch (Exception e)
              {
                continue;
              }
            ZipEntry ze = zf.getEntry(name);
            if (ze == null)
              continue;
            try
              {
                if (path.charAt(0) == '/')
                  return new URL("jar:file:/" + path + "!/" + name);
                else
                  return new URL("jar:file://" + path + "!/" + name);
              }
            catch (MalformedURLException e)
              {
                continue;
              }
          }
        File f;
        if (path.endsWith(File.separator))
          f = new File(path + name);
        else
          f = new File(path + File.separator + name);

        if (f.exists())
          try
            {
              return new URL("file://" + f.getAbsolutePath());
            }
          catch (MalformedURLException e)
            {
              continue;
            }
      }
    return null;
  }
}
