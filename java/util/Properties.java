/*
 * java.util.PropertyResourceBundle: part of the Java Class Libraries project.
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
import java.io.*;

/**
 * An example of a properties file for the german language is given
 * here.  This extends the example given in ListResourceBundle.
 * Create a file MyResource_de.properties with the following contents
 * and put it in the CLASSPATH.  (The char <code>\u00e4<char> is the 
 * german &auml;)
 * 
 * <pre>
 * s1=3
 * s2=MeineDisk
 * s3=3. M\u00e4rz 96
 * s4=Die Diskette ''{1}'' enth\u00e4lt {0} in {2}.
 * s5=0
 * s6=keine Dateien
 * s7=1
 * s8=eine Datei
 * s9=2
 * s10={0,number} Dateien
 * s11=Die Formatierung warf eine Exception: {0}
 * s12=FEHLER
 * s13=Ergebnis
 * s14=Dialog
 * s15=Auswahlkriterium
 * s16=1,3
 * </pre>
 *
 * @see PropertyResourceBundle
 * @author Jochen Hoenicke 
 */
public class Properties extends Hashtable {
    /**
     * The property list that contains default values for any keys not
     * in this property list.  
     */
    private Properties defaults;

    /**
     * Creates a new empty property list.
     */
    public Properties() {
        this.defaults = null;
    }
    
    /**
     * Create a new empty property list with the specified default values.
     * @param defaults a Properties object containing the default values.
     */
    public Properties(Properties defaults) {
        this.defaults = defaults;
    }

    /**
     * Reads a property list from an input stream.  The stream should
     * have the following format:
     *
     * An empty line or a line starting with <code>#</code> or
     * <code>!</code is ignored.  An backslash (<code>\</code>) at the
     * end of the line makes the line continueing on the next line.
     * Otherwise, each line describes a key/value pair.
     *
     * The chars up to the first whitespace, = or : are the key.  You
     * can include this caracters in the key, if you precede them with
     * a backslash (<code>\</code>). The key is followed by optional
     * whitespaces, optionally one <code>=</code> or <code>:</code>,
     * and optionally some more whitespaces.  The rest of the line is
     * the resource belonging to the key.
     *
     * Escape sequences <code>\t, \n, \r, \\, \", \', \ <code>(a
     * space), and unicode characters with the
     * <code>\</code><code>u</code>xxxx notation are detected, and 
     * converted to the corresponding single character.  
     *
     * XXX - examples
     *
     * @param in the input stream
     * @throws IOException if an error occured when reading
     * from the input.  */
    public void load(InputStream inStream) throws IOException {
        BufferedReader reader = 
            new BufferedReader(new InputStreamReader(inStream));
        String line;
        while ((line = reader.readLine()) != null) {
            char c = 0;
            int pos = 0;
            while (pos < line.length()
                   && Character.isWhitespace(c = line.charAt(pos)))
                pos++;

            // If line is empty or begins with a comment character,
            // skip this line.
            if (pos == line.length() || c == '#' || c == '!')
                continue;

            // The characaters up to the next Whitespace, ':', or '='
            // describe the key.  But look for escape sequences.
            StringBuffer key = new StringBuffer();
            while (pos < line.length() 
                   && !Character.isWhitespace(c = line.charAt(pos++))
                   && c != '=' && c != ':') {
                if (c == '\\') {
                    if (pos == line.length()) {
                        // The line continues on the next line.
                        line = reader.readLine();
                        pos = 0;
                        while (pos < line.length()
                               && Character.isWhitespace(c = line.charAt(pos)))
                            pos++;
                    } else {
                        c = line.charAt(pos++);
                        switch (c) {
                        case 'n':
                            key.append('\n');
                            break;
                        case 't':
                            key.append('\t');
                            break;
                        case 'r':
                            key.append('\r');
                            break;
                        case '\\':
                            key.append('\\');
                            break;
                        case '"':
                            key.append('\"');
                            break;
                        case '\'':
                            key.append('\'');
                            break;
                        case ':':
                            key.append(':');
                            break;
                        case '=':
                            key.append('=');
                            break;
                        case ' ':
                            key.append(' ');
                            break;
                        case 'u':
                            if (pos+4 <= line.length()) {
                                char uni = (char) Integer.parseInt
                                    (line.substring(pos, pos+4), 16);
                                key.append(uni);
                            } // else throw exception?
                            break;
                        }
                    }
                } else 
                    key.append(c);
            }
            
            boolean isDelim = (c == ':' || c == '=');
            while (pos < line.length()
                   && Character.isWhitespace(c = line.charAt(pos)))
                pos++;

            if (!isDelim && (c == ':' || c == '=')) {
                pos++;
                while (pos < line.length()
                       && Character.isWhitespace(c = line.charAt(pos)))
                    pos++;
            }

            StringBuffer element = new StringBuffer();
            while (pos < line.length()) {
                c = line.charAt(pos++);
                if (c == '\\') {
                    if (pos == line.length()) {
                        // The line continues on the next line.
                        line = reader.readLine();
                        pos = 0;
                        while (pos < line.length()
                               && Character.isWhitespace(c = line.charAt(pos)))
                            pos++;
                    } else {
                        c = line.charAt(pos++);
                        switch (c) {
                        case 'n':
                            element.append('\n');
                            break;
                        case 't':
                            element.append('\t');
                            break;
                        case 'r':
                            element.append('\r');
                            break;
                        case '\\':
                            element.append('\\');
                            break;
                        case '"':
                            element.append('\"');
                            break;
                        case '\'':
                            element.append('\'');
                            break;
                        case ':':
                            element.append(':');
                            break;
                        case '=':
                            element.append('=');
                            break;
                        case ' ':
                            element.append(' ');
                            break;
                        case 'u':
                            if (pos+4 <= line.length()) {
                                char uni = (char) Integer.parseInt
                                    (line.substring(pos, pos+4), 16);
                                element.append(uni);
                            } // else throw exception?
                            break;
                        }
                    }
                } else 
                    element.append(c);
            }

            put(key.toString(), element.toString());
        }
    }

    /**
     * Calls <code>store(OutputStream out, String header)</code> and
     * ignores the IOException that may be thrown.
     * @deprecated use store instead.
     */
    public void save(OutputStream out, String header) {
        try {
            store(out,header);
        } catch (IOException ex) {
        }
    }
    
    public void store(OutputStream out, String header) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        if (header != null)
            writer.println("#"+header);
        writer.println("#"+new Date().toString());
        list(writer);
    }
    
    /**
     * Adds the given key/value pair to this properties.  This calls
     * the hashtable method put.
     * @param key the key for this property
     * @param value the value for this property
     * @return The old value for the given key.
     * @since JDK1.2 */
    public Object setProperty(String key, String value) {
        return put(key,value);
    }

    /**
     * Gets the property with the specified key in this property list.
     * If the key is not found, the default property list is searched.
     * If the property is not found in default or the default of
     * default, null is returned.
     * @param key The key for this property.
     * @param defaulValue A default value
     * @return The value for the given key, or null if not found.  */
    public String getProperty(String key) {
        return getProperty(key, null);
    }

    /**
     * Gets the property with the specified key in this property list.  If
     * the key is not found, the default property list is searched.  If the
     * property is not found in default or the default of default, the 
     * specified defaultValue is returned.
     * @param key The key for this property.
     * @param defaulValue A default value
     * @return The value for the given key.
     */
    public String getProperty(String key, String defaultValue) {
        Properties prop = this;
        // Eliminate tail recursion.
        do {
            String value = (String) get(key);
            if (value != null)
                return value;
            prop = defaults;
        } while (prop != null);
        return defaultValue;
    }

    /**
     * Returns an enumeration of all keys in this property list, including
     * the keys in the default property list.
     */
    public Enumeration propertyNames() {
        return (defaults == null) ? keys() 
            : new DoubleEnumeration(keys(), defaults.propertyNames());
    }

    /**
     * Formats a key/value pair for output in a properties file.
     * See store for a description of the format.
     * @param key the key.
     * @param value the value.
     */
    private String formatForOutput(String key, String value) {
        StringBuffer result = new StringBuffer();
        boolean head = true;
        for (int i=0; i< key.length(); i++) {
            char c = key.charAt(i);
            switch (c) {
            case '\n':
                result.append("\\n");
                break;
            case '\r':
                result.append("\\r");
                break;
            case '\t':
                result.append("\\t");
                break;
            case '\\':
                result.append("\\\\");
                break;
            case '!':
                result.append("\\!");
                break;
            case '#':
                result.append("\\#");
                break;
            case '=':
                result.append("\\=");
                break;
            case ':':
                result.append("\\:");
                break;
            default:
                if (c < 32 || c > '~') {
                    String hex = Integer.toHexString(c);
                    result.append("\\u0000".substring(0, 6-hex.length()));
                    result.append(hex);
                } else if (c == 32 && head)
                    result.append("\\ ");
                else
                    result.append(c);
            }
            if (c != 32)
                head = false;
        }
        result.append('=');
        head=true;
        for (int i=0; i< value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
            case '\n':
                result.append("\\n");
                break;
            case '\r':
                result.append("\\r");
                break;
            case '\t':
                result.append("\\t");
                break;
            case '\\':
                result.append("\\\\");
                break;
            case '!':
                result.append("\\!");
                break;
            case '#':
                result.append("\\#");
                break;
            default:
                if (c < 32 || c > '~') {
                    String hex = Integer.toHexString(c);
                    result.append("\\u0000".substring(0, 6-hex.length()));
                    result.append(hex);
                } else if (c == 32 && head)
                    result.append("\\ ");
                else
                    result.append(c);
            }
            if (c != 32)
                head = false;
        }
        return result.toString();
    }

    public void list(PrintStream out) {
        Enumeration keys = keys();
        Enumeration elts = elements();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String elt = (String) elts.nextElement();
            String output = formatForOutput(key,elt);
            out.println(output);
        }
    }

    /**
     *
     * @since JDK1.1
     */
    public void list(PrintWriter out) {
        Enumeration keys = keys();
        Enumeration elts = elements();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String elt = (String) elts.nextElement();
            String output = formatForOutput(key,elt);
            out.println(output);
        }
    }
}
