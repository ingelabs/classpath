/*
 * java.util.StringTokenizer: part of the Java Class Libraries project.
 * Copyright (C) 1998 Jochen Hoenicke
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package java.util;

/**
 * blah blah
 *
 * @author Jochen Hoenicke
 */
public class StringTokenizer implements Enumeration {

    private int pos;
    private String str;
    private String delim;
    private boolean retDelims;

    public StringTokenizer(String str) {
        this(str, " \t\n\r\f", false);
    }

    public StringTokenizer(String str, String delim) {
        this(str, delim, false);
    }

    public StringTokenizer(String str, String delim, boolean returnDelims) {
        this.str = str;
        this.delim = delim;
        this.retDelims = returnDelims;
        this.pos = 0;
    }

    public boolean hasMoreTokens() {
        if (!retDelims) {
            while (pos < str.length()
                   && delim.indexOf(str.charAt(pos)) > -1) {
                pos++;
            }
        }
        return pos < str.length();
    }

    public String nextToken(String delim) throws NoSuchElementException {
        this.delim = delim;
        return nextToken();
    }

    public String nextToken() throws NoSuchElementException {
        if (pos < str.length() 
            && delim.indexOf(str.charAt(pos)) > -1) {
            if (retDelims)
                return str.substring(pos, ++pos);

            while (++pos < str.length() 
                   && delim.indexOf(str.charAt(pos)) > -1) {
                /* empty */
            }
        }
        if (pos < str.length()) {
            int start = pos;
            while (++pos < str.length()
                   && delim.indexOf(str.charAt(pos)) == -1) {
                /* empty */
            }
            return str.substring(start, pos);
        }
        throw new NoSuchElementException();
    }

    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    public Object nextElement() throws NoSuchElementException {
        return nextToken();
    }

    public int countTokens() {
        int count = 0;
        while (pos < str.length()) {
            if (delim.indexOf(str.charAt(pos)) > -1) {
                if (retDelims) {
                    count++;
                    continue;
                }
                
                while (++pos < str.length() 
                       && delim.indexOf(str.charAt(pos)) > -1) {
                    /* empty */
                }
            }
            if (pos < str.length()) {
                int start = pos;
                while (++pos < str.length()
                       && delim.indexOf(str.charAt(pos)) == -1) {
                    /* empty */
                }
                count++;
            }
        }
        return count;
    }
}
