/* DocFlavor.java --
   Copyright (C) 2004 Free Software Foundation, Inc.

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


package javax.print;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Michael Koch (konqueror@gmx.de)
 */
public class DocFlavor implements Cloneable, Serializable
{
  private static final long serialVersionUID = -4512080796965449721L;
  
  public static final String hostEncoding = "US-ASCII";

  private String mediaSubtype;
  private String mediaType;
  private String className;
  private HashMap params = new HashMap();
  
  public DocFlavor(String mimeType, String className)
  {
    if (mimeType == null || className == null)
      throw new NullPointerException();

    parseMimeType(mimeType);
    this.className = className;
  }

  private void parseMimeType(String mimeType)
  {
    // FIXME: This method is know to be not completely correct, but it works for now.
    
    int pos = mimeType.indexOf(';');

    if (pos != -1)
      {
	String tmp = mimeType.substring(pos + 2);
	mimeType = mimeType.substring(0, pos);
	pos = tmp.indexOf('=');
	params.put(tmp.substring(0, pos), tmp.substring(pos + 1));
      }

    pos = mimeType.indexOf('/');

    if (pos == -1)
      throw new IllegalArgumentException();

    mediaType = mimeType.substring(0, pos);
    mediaSubtype = mimeType.substring(pos + 1);
  }
  
  public boolean equals(Object obj)
  {
    if (! (obj instanceof DocFlavor))
      return false;

    DocFlavor tmp = (DocFlavor) obj;

    return (getMimeType().equals(tmp.getMimeType())
	    && getRepresentationClassName().equals(tmp.getRepresentationClassName()));
  }

  public String getMediaSubtype()
  {
    return mediaSubtype;
  }

  public String getMediaType()
  {
    return mediaType;
  }

  public String getMimeType()
  {
    // FIXME: Check if this algorithm is correct.
    
    String mimeType = getMediaType() + "/" + getMediaSubtype();
    Iterator it = params.entrySet().iterator();

    while (it.hasNext())
      {
	Map.Entry entry = (Map.Entry) it.next();
	mimeType += "; " + entry.getKey() + "=\"" + entry.getValue() + "\"";
      }

    return mimeType;
  }

  public String getParameter(String paramName)
  {
    if (paramName == null)
      throw new NullPointerException();
    
    return (String) params.get(paramName); 
  }

  public String getRepresentationClassName()
  {
    return className;
  }

  public int hashCode()
  {
    return ((mediaType.hashCode()
	     * mediaSubtype.hashCode()
	     * className.hashCode()) ^ params.hashCode());
  }

  public String toString()
  {
    return getMimeType();
  }
}
