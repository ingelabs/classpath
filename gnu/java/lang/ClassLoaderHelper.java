/* gnu.java.lang.ClassLoaderHelper
   Copyright (C) 1998 Free Software Foundation, Inc.

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
 * Currently getSystemResourceAsFile is only used in java/lang/Character.<clinit>. 
 * Personally I think it's not wise for java/lang/Character to have data stored in 
 * character.uni / block.uni / titlecase.uni files. It'll be more efficient and 
 * convenient to have these data inlined in Character.java.
 * And what's more specific here, if we inlined those data, we don't need 
 * ClassLoaderHelper.getSystemResourceAsFile any more. It's a procedure which could bring
 * trouble.
 * Consider when we add jar support to ClassLoaderHelper.getSystemResourceAsFile, we will 
 * encounter the following call chain:
 *
 * Character.<clinit> -> ClassLoaderHelper.getSystemResourceAsFile() -> ... ->Character.toLowerCase().
 *
 * It's another recursive call when initialization of Character is still on the way, 
 * call to Character happens.
 * Currently I have to use a call of Character.toLowerCase() to verify if initialization 
 * of Character has been finished, if exceptions are throwed, it indicates that the construction 
 * of Character is ongoing, so I abandon further investigation of the jar package. 
 * It's somewhat ugly, and what's important is that it'll degrade performance.
 * Maybe we need to check why we have so many heavyweight <clinit>/<init> which will lead to 
 * call methods/access fields of itself. 
 * 
 * "fileResourceCache" is used to save File instances mapping to specific absolute paths and jar urls.
 *      -- Gansha
 */

/**
 ** ClassLoaderHelper has various methods that ought to have been
 ** in ClassLoader.
 **
 ** @author John Keiser
 ** @version 1.1.0, 29 Jul 1998
 **/
public class ClassLoaderHelper 
{
  private static Hashtable fileResourceCache = new Hashtable();
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
    if (name.startsWith("/"))
      name = name.substring(1);
      
    String path = System.getProperty("java.class.path", ".");
    StringTokenizer st = new StringTokenizer(path,
               			   System.getProperty("path.separator", ":"));
    while (st.hasMoreElements()) {
      String token = st.nextToken();
      File file;
        if (token.endsWith(".zip") ||
            token.endsWith(".jar") ){
            // The following tests whether java/lang/Character 
            //  has been initialized, if no, abort loading this archive to
            //  avoid infinite recursive call
            try{
                token = token.toLowerCase();
            }catch(Exception e){
                continue;
            }
      
            file = new File(token);
    		if(!file.exists())continue;
    		
		   	path = file.getAbsolutePath();
		   	try {
			    if(path.startsWith("/"))
			        path = "jar:file:/"+ path + "!/" + name;
			    else
			        path = "jar:file://"+ path + "!/" + name;
			    file = (File)fileResourceCache.get(path);
			    if(file == null){
			        //load jar/zip entry from the url
    			    URL url = new URL(path);
    			    URLConnection urlconn = url.openConnection();
    			    InputStream is = urlconn.getInputStream();
    			    byte[] buf = new byte[READBUFSIZE];
    			    file = File.createTempFile("tmp", "", new File("."));
    			    FileOutputStream fos = new FileOutputStream(file); 
                    int len = 0;
                    while((len = is.read(buf)) != -1){
                        fos.write(buf);
                    }
                    fos.close();
    			}
			} catch(Exception e) {
				continue;
			} 
      }else{
          if (PlatformHelper.endWithSeparator(token))
            path = token + name;
          else
            path = token + File.separator + name;
          file = (File)fileResourceCache.get(path);
          if(file == null)
            file = new File(path);
      }
      
      if (file != null && file.isFile()){
        fileResourceCache.put(path, file);
	    return file;
	  }
    }
    return null;
  }
}
