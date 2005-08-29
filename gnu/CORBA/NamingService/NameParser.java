/* NameParser.java --
 Copyright (C) 2005 Free Software Foundation, Inc.

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
 Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 02110-1301 USA.

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


package gnu.CORBA.NamingService;

import gnu.CORBA.IOR;
import gnu.CORBA.Unexpected;
import gnu.CORBA.Version;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.DATA_CONVERSION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.ORBPackage.InvalidName;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Parses the alternative IOR representations into our IOR structure.
 * 
 * TODO This parser currently supports only one address per target string. A
 * string with the multiple addresses will be accepted, but only the last
 * address will be taken into consideration. The fault tolerance is not yet
 * implemented.
 * 
 * The key string is filtered using {@link java.net.URLDecoder} that replaces
 * the agreed escape sequences by the corresponding non alphanumeric characters.
 * 
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public class NameParser
{
  /**
   * The mandatory prefix.
   */
  public static final String CORBALOC = "corbaloc";

  /**
   * Marks iiop protocol.
   */
  public static final String IIOP = "iiop";

  /**
   * Marks rir protocol.
   */
  public static final String RIR = "rir";

  /**
   * The default port value, as specified in OMG documentation.
   */
  public static final int DEFAULT_PORT = 2809;

  /**
   * Parse CORBALOC.
   * 
   * The expected format is: <br>
   * 1. corbaloc:[iiop][version.subversion@]:host[:port]/key <br>
   * 2. corbaloc:rir:/key <br>
   * 
   * protocol defaults to IOP.
   * 
   * @param corbaloc the string to parse.
   * @param orb the ORB, needed to create IORs and resolve rir references.
   * 
   * @return the constructed IOR.
   */
  public static java.lang.Object corbaloc(String corbaloc, ORB orb)
    throws BAD_PARAM
  {

    // The alternative addresses, if given.
    ArrayList alt_addr = new ArrayList();

    // The version numbers with default values.
    int major = 1;
    int minor = 0;

    // The host address.
    String host;

    // The port.
    int port = DEFAULT_PORT;

    // The object key as string.
    String key;

    StringTokenizer st = new StringTokenizer(corbaloc, ":@/.,", true);

    String[] t = new String[st.countTokens()];

    for (int i = 0; i < t.length; i++)
      {
        t[i] = st.nextToken();
      }

    int p = 0;

    if (!t[p++].equalsIgnoreCase(CORBALOC))
      throw new BAD_PARAM("Must start with corbaloc:");

    if (!t[p++].equals(":"))
      throw new BAD_PARAM("Must start with corbaloc:");

    // Check for rir:
    if (t[p].equals(RIR))
      {
        p++;
        if (!t[p++].equals(":"))
          throw new BAD_PARAM("':' expected after 'rir'");

        key = readKey(p, t);
        
        Object object;
        try
          {
            object = orb.resolve_initial_references(key);
          }
        catch (InvalidName e)
          {
            throw new BAD_PARAM("Unknown initial reference '"+key+"'");
          }
        return object;
      }
    else
    // Check for iiop.
    if (t[p].equals(IIOP) || t[p].equals(":"))
      {
        IOR ior = new IOR();
        
        Addresses: do
          { // Read addresses.
            if (t[p].equals(":"))
              {
                p++;
              }
            else
              {
                p++;
                if (!t[p++].equals(":"))
                  throw new BAD_PARAM("':' expected after 'iiop'");
                // Check if version is present.
                if (t[p + 1].equals("."))
                  if (t[p + 3].equals("@"))
                    {
                      // Version info present.
                      try
                        {
                          major = Integer.parseInt(t[p++]);
                        }
                      catch (NumberFormatException e)
                        {
                          throw new BAD_PARAM("Major version number '"
                            + t[p - 1] + "'");
                        }
                      p++; // '.' at this point.
                      try
                        {
                          minor = Integer.parseInt(t[p++]);
                        }
                      catch (NumberFormatException e)
                        {
                          throw new BAD_PARAM("Major version number '"
                            + t[p - 1] + "'");
                        }
                      p++; // '@' at this point.
                    }
              }
            
            ior.Internet.version = new Version(major, minor);
            
            // Then host data goes till '/' or ':'.
            StringBuffer bhost = new StringBuffer(corbaloc.length());
            while (!t[p].equals(":") && !t[p].equals("/") && !t[p].equals(","))
              bhost.append(t[p++]);

            host = bhost.toString();

            ior.Internet.host = host;

            if (t[p].equals(":"))
              {
                // Port specified.
                p++;
                try
                  {
                    port = Integer.parseInt(t[p++]);
                  }
                catch (NumberFormatException e)
                  {
                    throw new BAD_PARAM("Invalid port '" + t[p - 1] + "'");
                  }
              }
            
            ior.Internet.port = port;
            
            // Id is not listed.
            ior.Id = "";
            
            if (t[p].equals(","))
              p++;
            else
              break Addresses;
          }
        while (true);

        key = readKey(p, t);
        ior.key = key.getBytes();
        
        return ior;
      }
    else
      throw new DATA_CONVERSION("Unsupported protocol '" + t[p] + "'");

  }

  private static String readKey(int p, String[] t)
    throws BAD_PARAM
  {
    if (!t[p].equals("/"))
      throw new BAD_PARAM("'/keyString' expected '" + t[p] + "' found");

    StringBuffer bKey = new StringBuffer();
    p++;

    while (p < t.length)
      bKey.append(t[p++]);

    try
      {
        return URLDecoder.decode(bKey.toString(), "UTF-8");
      }
    catch (UnsupportedEncodingException e)
      {
        throw new Unexpected("URLDecoder does not support UTF-8", e);
      }
  }
  
  static void corbalocT(String ior, ORB orb)
  {
    System.out.println(ior);
    System.out.println(corbaloc(ior, orb));
    System.out.println();
  }

  public static void main(String[] args)
  {
    try
      {
        ORB orb = ORB.init(args, null);
        corbalocT("corbaloc:iiop:1.3@155axyz.com/Prod/aTradingService", orb);
        corbalocT("corbaloc:iiop:2.7@255bxyz.com/Prod/bTradingService", orb);
        corbalocT("corbaloc:iiop:355cxyz.com/Prod/cTradingService", orb);
        corbalocT("corbaloc:iiop:2.7@255bxyz.com/Prod/bTradingService", orb);
        corbalocT("corbaloc:iiop:355cxyz.com:7777/Prod/cTradingService", orb);

        corbalocT("corbaloc::556xyz.com:80/Dev/NameService", orb);
        corbalocT("corbaloc:iiop:1.2@host1:3076/0", orb);

        corbalocT("corbaloc:rir:/NameService", orb);

        corbalocT("corbaloc::555xyz.com,:556xyz.com:80/Dev/NameService", orb);
      }
    catch (BAD_PARAM e)
      {
        e.printStackTrace(System.out);
      }
  }
}
