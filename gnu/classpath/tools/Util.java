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
