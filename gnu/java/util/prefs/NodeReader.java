/* NodeReader - Reads and imports preferences nodes from files
   Copyright (C) 2001 Free Software Foundation, Inc.

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

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */

package gnu.java.util.prefs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;

import java.util.prefs.*;

/**
 * Reads and imports preferences nodes from files.
 *
 * @author Mark Wielaard (mark@klomp.org)
 */
public class NodeReader {

    private final BufferedReader br;
    private String line = "";

    private final PreferencesFactory factory;

    public NodeReader(Reader r, PreferencesFactory factory) {
        if(r instanceof BufferedReader) {
            br = (BufferedReader) r;
        } else {
            br = new BufferedReader(r);
        }
        this.factory = factory;
    }

    public NodeReader(InputStream is, PreferencesFactory factory) {
        this(new InputStreamReader(is), factory);
    }

    public void importPreferences()
                    throws InvalidPreferencesFormatException, IOException
    {
        readPreferences();
    }

    private void readPreferences()
                    throws InvalidPreferencesFormatException, IOException
    {
        // Begin starting tag
        skipTill("<preferences");

        readRoot();

        // Ending tag
        skipTill("</preferences>");
    }

    private void readRoot()
                    throws InvalidPreferencesFormatException, IOException
    {
        // Begin starting tag
        skipTill("<root");

        // type attribute
        skipTill("type=\"");
        String type = readTill("\"");
        Preferences root;
        if ("user".equals(type)) {
            root = factory.userRoot();
        } else if ("user".equals(type)) {
            root = factory.systemRoot();
        } else {
            throw new InvalidPreferencesFormatException("Unknown type: "
                                                        + type);
        }

        // Read root map and subnodes
        readMap(root);
        readNodes(root);

        // Ending tag
        skipTill("</root>");
    }

    private void readNodes(Preferences node)
                    throws InvalidPreferencesFormatException, IOException
    {
        while ("node".equals(nextTag())) {
            skipTill("<node");
            skipTill("name=\"");
            String name = readTill("\"");
            Preferences subnode = node.node(name);
            System.out.println("Found subnode: " + subnode.absolutePath());
            readMap(subnode);
            readNodes(subnode);
            skipTill("</node>");
        }
        
    }

    private void readMap(Preferences node)
                    throws InvalidPreferencesFormatException, IOException
    {
        // Begin map tag
        skipTill("<map");

        // Empty map?
        if (line.startsWith("/>")) {
            line = line.substring(2);
            return;
        }

        // Map entries
        readEntries(node);

        // Ending tag
        skipTill("</map>");
    }

    private void readEntries(Preferences node)
                    throws InvalidPreferencesFormatException, IOException
    {
        while ("entry".equals(nextTag())) {
            skipTill("<entry");
            skipTill("key=\"");
            String key = readTill("\"");
            skipTill("value=\"");
            String value = readTill("\"");
            System.out.println("Key: " + key + " Value: " + value);
            node.put(key, value);
        }
    }

    private void skipTill(String s)
                    throws InvalidPreferencesFormatException, IOException
    {
        while(true) {
            if (line == null)
                throw new InvalidPreferencesFormatException(s + " not found");
            
            int index = line.indexOf(s);
            if (index == -1)  {
                line = br.readLine();
            } else {
                line = line.substring(index+s.length());
                return;
            }
        }
    }

    private String readTill(String s)
                    throws InvalidPreferencesFormatException
    {
        int index = line.indexOf(s);
        if (index == -1)
                throw new InvalidPreferencesFormatException(s + " not found");

        String read = line.substring(0, index);
        line = line.substring(index+s.length());

        return read;
    }

    private String nextTag()
                    throws InvalidPreferencesFormatException, IOException
    {
        while(true) {
            if (line == null)
                throw new InvalidPreferencesFormatException("unexpected EOF");
            
            int start = line.indexOf("<");
            if (start == -1)  {
                line = br.readLine();
            } else {
                // Find end of tag
                int end = start+1;
                while (end != line.length()
                       && " \t\r\n".indexOf(line.charAt(end)) == -1) {
                    end++;
                }
                // Line now starts at the found tag
                String tag = line.substring(start+1,end);
                line = line.substring(start);
                return tag;
            }
        }
    }

}
