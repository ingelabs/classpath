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
import java.util.Vector;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import gnu.java.io.PlatformHelper;
import gnu.java.net.protocol.jar.JarURLConnection.JarFileCache;

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

  /** value of classpath property */
  private static String classpath = null;
  
  /** A vector contains information for each path in classpath,
    * the item of the vector can be:
    *   null:           if corresponding path isn't valid;
    *   ZipFile object: if corresponding path is a zip/jar file;
    *   String object:  if corresponding path is a directory.
    */
  private static Vector pathinfos = new Vector();

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
   * Find the URL to a resource. Called by ClassLoader.getResource().
   *
   * @param name the name of the resource
   * @return the URL to the resource
   */
  protected URL findResource(String name)
  {
    return systemFindResource(name);
  }

  /**
   * Get the URL to a resource.
   *
   * @param name the name of the resource
   * @return the URL to the resource
   */
  private static URL systemFindResource(String name)
  {
    if (name.charAt(0) == '/')
      name = name.substring(1);
    
    String cp = System.getProperty("java.class.path", ".");
    
    Vector bak_pathinfos = null; // the backup of pathinfos
    
    /* if is_trying is true, it's called recursively and in 
     * a transient status, so we backup pathinfos into a temp
     * variable, and when all are done, restore pathinfos to 
     * backup state. That means, this call won't persistent 
     * its state, it's just a fall through to get out of 
     * transient state. And when next time it's called again
     * and is_trying is false, pathinfos will be re-calculated.
     */
    if (is_trying)
      {
	bak_pathinfos = pathinfos;
	pathinfos = new Vector();
      }
    
    // if classpath property has been changed
    if (!cp.equals(classpath))
      {
	pathinfos.clear();
	StringTokenizer st = new StringTokenizer(cp, File.pathSeparator);
	while (st.hasMoreTokens())
	  {
	    String path = st.nextToken();
	    // check if path exists, if not, cache failure 
	    File f = new File(path);
	    if(!f.exists())
	      {
		pathinfos.add(null);
		continue;
	      }
	    
	    String lc_path = path.toLowerCase();
	    path = f.getAbsolutePath();
	    
	    if (lc_path.endsWith(".zip") ||
		lc_path.endsWith(".jar")) //whether it's zip/jar file
	      {
		if (is_trying) // Avoid infinite loop.
		  continue;
		
		path = f.getAbsolutePath();
		ZipFile zf;
		try
		  {
		    // Construct URL object for the parth
		    StringBuffer sb = 
		      new StringBuffer(PlatformHelper.INITIAL_MAX_PATH);
		    sb.append("file://");
		    sb.append(path);
		    URL url = new URL(sb.toString());
		    // class-level critical section
		    synchronized (NO_SUCH_ARCHIVE)
		      {
			is_trying = true;
			zf = JarFileCache.get(url);
			is_trying = false;
		      }
		  }
		catch (Exception e)
		  {
		    zf = null;
		  }
		pathinfos.add(zf);
	      }
	    else //not zip/jar file
	      {
		if ( !PlatformHelper.endWithSeparator(path) )
		  pathinfos.add(path + File.separator);
		else
		  pathinfos.add(path);      
	      }
	  } // while more paths
      } // if classpath property has been changed
    
    URL result = null;
    for (int i = 0; i < pathinfos.size(); i++)
      {
	Object o = pathinfos.elementAt(i);
	if (o == null )
	  continue; //it's not a valid path
	
	if (o instanceof ZipFile)
	  { //it's a zip/jar file
	    ZipFile zf = (ZipFile)o;
            ZipEntry ze = zf.getEntry(name);

	    // if the resource doesn't reside in this zip/jar file
            if (ze == null)
              continue;

            try
              {
		StringBuffer sb = 
		  new StringBuffer(PlatformHelper.INITIAL_MAX_PATH);
		sb.append("jar:file://");
		sb.append(zf.getName());
		sb.append("!/");
		sb.append(name);
		result = new URL(sb.toString());        
              }
            catch (MalformedURLException e)
              {
		result = null;
              }
	    break;
          }
	// otherwise o is string
	String path = (String)o;
	File f = new File(path + name);
        if (f.exists())
	  {
	    try
	      {
		result = new URL("file://" + f.getAbsolutePath());
	      }
	    catch (MalformedURLException e)
	      {
		result = null;
	      }
	    break;
	  }
      } //for each paths
    
    // Restore pathinfos
    if (is_trying)
      {
	pathinfos = bak_pathinfos;
      }
    else
      {
	/* update classpath, hopefully next time classpath is
	 * not changed, so pathinfos will not be re-calcualted.
	 */
	classpath = cp;
      }
    
    return result;
    
  } //End of systemFindResource
  
}
