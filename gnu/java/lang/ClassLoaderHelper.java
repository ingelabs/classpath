package gnu.java.lang;

import java.io.*;
import java.util.*;

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
