/*
 * gnu.java.lang.ClassLoaderHelper: part of the Java Class Libraries project.
 * Copyright (C) 1998 Free Software Foundation
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package gnu.java.lang;

import java.io.*;
import java.util.*;

/**
 ** ClassLoaderHelper has various methods that ought to have been
 ** in ClassLoader.
 **
 ** @author John Keiser
 ** @version 1.1.0, 29 Jul 1998
 **/
public class ClassLoaderHelper 
{
  /**
   * Searches the CLASSPATH for a resource and returns it as a File.
   * Currently ignores ZIP and JAR archives.
   *
   * @param name name of resource to locate
   *
   * @return a File object representing the resource, or null
   * if the resource could not be found
   */
  public static final File getSystemResourceAsFile(String name) {
    if (name.startsWith("/"))
      name = name.substring(1);
    String path = System.getProperty("java.class.path", ".");
    StringTokenizer st = new StringTokenizer(path,
               			   System.getProperty("path.separator", ":"));
    while (st.hasMoreElements()) {
      String token = st.nextToken();
      File file;
      if (token.endsWith(File.separator))
        file = new File(token, name);
      else
        file = new File(token + File.separator + name);
      if (file.isFile())
	return file;
    }
    return null;
  }
}
