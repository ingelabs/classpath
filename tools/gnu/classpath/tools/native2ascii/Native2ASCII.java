/* Native2ASCII.java - native2ascii program
 Copyright (C) 2003 Free Software Foundation, Inc.

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


package gnu.classpath.tools.native2ascii;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Native2ASCII main program.
 * @author Ito Kazumitsu <kaz@maczuka.gcd.org>
 */
public class Native2ASCII
{
  public static void main(String[] args)
  {
    String encoding = System.getProperty("file.encoding");
    String input = null;
    String output = null;
    for (int i = 0; i < args.length; i++)
      {
        if (args[i].equals("-encoding"))
          {
            i++;
            if (i >= args.length)
              {
                System.err.println("encoding is missing");
              }
            else
              {
                encoding = args[i];
              }
          }
        else if (args[i].equals("-reverse") || args[i].startsWith("-J"))
          {
            System.err.println(args[i] + ": not supported");
          }
        else
          {
            if (input == null)
              {
                input = args[i];
              }
            else if (output == null)
              {
                output = args[i];
              }
            else
              {
                System.err.println(args[i] + ": ignored");
              }
          }
      }
    try
      {
        InputStream is = (input == null ? System.in
            : new FileInputStream(input));
        OutputStream os = (output == null ? (OutputStream) System.out
            : new FileOutputStream(output));
        BufferedReader rdr = new BufferedReader(new InputStreamReader(is,
                                                                      encoding));
        PrintWriter wtr = new PrintWriter(
                                          new BufferedWriter(
                                                             new OutputStreamWriter(
                                                                                    os,
                                                                                    encoding)));
        while (true)
          {
            String s = rdr.readLine();
            if (s == null)
              break;
            StringBuffer sb = new StringBuffer(s.length() + 80);
            for (int i = 0; i < s.length(); i++)
              {
                char c = s.charAt(i);
                if ((int)c <= 127)
                  {
                    sb.append(c);
                  }
                else
                  {
                    sb.append("\\u");
                    if ((int)c <= 0xff)
                      sb.append("00");
                    else if ((int)c <= 0xfff)
                      sb.append("0");
                    sb.append(Integer.toHexString((int) c));
                  }
              }
            wtr.println(sb.toString());
          }
        rdr.close();
        wtr.flush();
        wtr.close();
      }
    catch (Exception e)
      {
        e.printStackTrace();
      }
  }
}
