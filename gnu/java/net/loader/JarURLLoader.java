package gnu.java.net.loader;

import gnu.java.net.IndexListParser;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * A <code>JarURLLoader</code> is a type of <code>URLLoader</code>
 * only loading from jar url.
 */
public final class JarURLLoader extends URLLoader
{
  final JarFile jarfile; // The jar file for this url
  final URL baseJarURL; // Base jar: url for all resources loaded from jar

  Vector classPath;	// The "Class-Path" attribute of this Jar's manifest

  public JarURLLoader(URLClassLoader classloader, URLStreamHandlerCache cache,
                      URLStreamHandlerFactory factory,
                      URL baseURL, URL absoluteUrl)
  {
    super(classloader, cache, factory, baseURL, absoluteUrl);

    this.classPath = null;
    URL baseJarURL = null;
    JarFile jarfile = null;
    try
      {
        // Cache url prefix for all resources in this jar url.
        String base = baseURL.toExternalForm() + "!/";
        baseJarURL = new URL("jar", "", -1, base, cache.get(factory, "jar"));
        
        jarfile =
          ((JarURLConnection) baseJarURL.openConnection()).getJarFile();
        
        Manifest manifest;
        Attributes attributes;
        String classPathString;

        this.classPath = new Vector();
        
        ArrayList indexListHeaders = new IndexListParser(jarfile, baseJarURL, baseURL).getHeaders();
        if (indexListHeaders.size() > 0)
          this.classPath.addAll(indexListHeaders);
        else if ((manifest = jarfile.getManifest()) != null
            && (attributes = manifest.getMainAttributes()) != null
            && ((classPathString 
      	   = attributes.getValue(Attributes.Name.CLASS_PATH)) 
      	  != null))
          {	      
            StringTokenizer st = new StringTokenizer(classPathString, " ");
            while (st.hasMoreElements ()) 
      	{  
      	  String e = st.nextToken ();
      	  try
      	    {
      	      this.classPath.add(new URL(baseURL, e));
      	    } 
      	  catch (java.net.MalformedURLException xx)
      	    {
      	      // Give up
      	    }
      	}
          }
      }
    catch (IOException ioe)
      {
        /* ignored */
      }

    this.baseJarURL = baseJarURL;
    this.jarfile = jarfile;
  }

  /** get resource with the name "name" in the jar url */
  public Resource getResource(String name)
  {
    if (jarfile == null)
      return null;

    if (name.startsWith("/"))
      name = name.substring(1);

    JarEntry je = jarfile.getJarEntry(name);
    if (je != null)
      return new JarURLResource(this, name, je);
    else
      return null;
  }

  public Manifest getManifest()
  {
    try
      {
        return (jarfile == null) ? null : jarfile.getManifest();
      }
    catch (IOException ioe)
      {
        return null;
      }
  }

  public Vector getClassPath()
  {
    return classPath;
  }
}