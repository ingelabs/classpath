/* gnu.java.net.protocol.jar.JarURLConnection - jar url connection for java.net
   Copyright (C) 2002 Free Software Foundation, Inc.

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


package gnu.java.net.protocol.jar;

import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.jar.*;

/**
  * This subclass of java.net.JarURLConnection models a URLConnection via
  * the "jar" protocol.
  *
  */
public class JarURLConnection extends java.net.JarURLConnection
{

  private JarFile jar_file;
  private JarEntry jar_entry;
  private URL jar_url;
  
  public static class JarFileCache
  {
    private static Hashtable cache = new Hashtable();
    private static final int READBUFSIZE = 1024;
    private static boolean is_trying = false;
    
    public static synchronized JarFile get(URL url) throws IOException{
      JarFile jf = (JarFile)cache.get(url);
      if(jf != null)return jf; 
      if(is_trying)return null;
      try{
	is_trying = true;
	if("file".equals(url.getProtocol())){
	  jf = new JarFile(url.getFile());
	}else{
	  URLConnection urlconn = url.openConnection();
	  InputStream is = urlconn.getInputStream();
	  byte[] buf = new byte[READBUFSIZE];
	  File f;
	  FileOutputStream fos = new FileOutputStream(f = File.createTempFile("cache", "jar")); 
	  int len = 0;
	  while((len = is.read(buf)) != -1){
	    fos.write(buf);
	  }
	  fos.close();
	  jf = new JarFile(f);
	}
	cache.put(url, jf);
      }finally{
	is_trying = false;
      }
      return jf;
    }
    
  }
  
  public 
    JarURLConnection(URL url) throws MalformedURLException, IOException
  {
    super(url);
    jar_url = getJarFileURL();
  }
  
  public void 
    connect() throws IOException
  {
    if(connected)return;
    
    jar_file = JarFileCache.get(jar_url);
    String entry_name = getEntryName();
    if(entry_name != null && !entry_name.equals("")){
      jar_entry = (JarEntry)jar_file.getEntry(entry_name);
      //wgs
      if(jar_entry == null)
	throw new IOException("No entry for " + entry_name + " exists.");
    }
    
    connected = true;
  }
  
  public JarFile 
    getJarFile() throws IOException
  {
    if(!connected)
      connect();
    return jar_file;
  }
  
  public InputStream
    getInputStream() throws IOException
  {
    if(!connected)
      connect();
    if(jar_entry == null)
      throw new IOException(jar_url + " couldn't be found.");
    return jar_file.getInputStream(jar_entry);
    
  }
  
} // class JarURLConnection

