/*************************************************************************
 * Main.java -- Implementation of serialver program
 *
 * Copyright (c) 2001 by Free Software Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published 
 * by the Free Software Foundation, version 2. (see COPYING)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 *************************************************************************/
package gnu.classpath.tools;

public class JavahMain
{
  public static void main (String argv[])
  {
    Javap p = parseArguments (argv);
    int ret = p.exec ();
    System.exit (ret);
  }

  /**
   * Parses the arguments to determine what this program
   * should do.
   */
  private static Javap parseArguments (String s[])
  {
    Javap p = new Javap ();

    if (s.length == 0)
      usage ();

    boolean processArguments = true;
    int i = 0;

    p.setJavah (true);

    while (processArguments)
      {
	if (s[i].equals ("-classpath"))
	  p.setClasspath (s[++i]);
	else if (s[i].equals ("-d"))
	  p.setOutputDirectory (s[++i]);
	else if (s[i].equals ("-help"))
	  usage ();
	else if (s[i].equals ("-jni"))
	    p.setOutputJNI (true);
	else if (s[i].equals ("-o"))
	  p.setOutputFile (s[++i]);
	else if (s[i].equals ("-verbose"))
	  p.setOutputVerbose (true);
	else if (s[i].startsWith ("-"))
	  {
	    System.err.println ("Invalid flag: " + s[i]);
	    usage ();
	  }
	else 
	  break;

	i++;
      }
    
    String [] c = new String [s.length - i];
    if (c.length == 0)
      usage ();

    for (int j = i; j < s.length; j++)
	c[j-i] = s[j];

    p.setClasses (c);

    return p;
  }
  
  /**
   * Prints generic usage message to System.out.
   */
  private static void usage ()
  {
    System.out.println ("Usage: javah [OPTION]... [CLASS]...");
    System.out.println ("Generate header files for the given classes.");
    System.out.println ("");
    System.out.println ("   -classpath PATH           Specify where to find user class files");
    System.out.println ("   -d DIR                    Specify an output directory");
    System.out.println ("   -help                     Print this usage message");
    System.out.println ("   -jni                      Generate JNI header file (default)");
    System.out.println ("   -o FILE                   Specify an output file, cannot be used with -d");
    System.out.println ("   -stubs                    Generate an implementation stub file");
    System.out.println ("   -verbose                  Enable verbose output");
    System.exit (0);
  }
}
