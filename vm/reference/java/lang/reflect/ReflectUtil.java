/* ReflectUtil.java --
   Copyright (C) 2003 David Belanger <dbelan2@cs.mcgill.ca>
 
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

package java.lang.reflect;

/**
 * Common methods needed by several reflection classes.
 *
 * @author David Bélanger
 *
 */
class ReflectUtil
{

  /**
   * Returns the <code>Class</code> that represents the type
   * specificied by <code>name</name>. (ex: "I", "V", "Ljava/lang/String;")
   *
   */
  public static Class typeToClass(String name)
  {
    char c;

    c = name.charAt(0);

    switch (c)
    {
    case 'Z':
      return boolean.class;
    case 'B':
      return byte.class;
    case 'S':
      return short.class;
    case 'C':
      return char.class;
    case 'I':
      return int.class;
    case 'J':
      return long.class;
    case 'F':
      return float.class;
    case 'D':
      return double.class;
    case 'V':  // got also that type for return types
      return void.class;
    case 'L':
      {
        String formalName;
        // check needed?
        if (name.charAt(name.length() - 1) != ';')
        {
          throw new InternalError("Invalid class name format");
        }
        formalName = name.substring(1, name.length() - 1);  // remove ';'
        formalName = formalName.replace('/', '.');
        try
        {
          return Class.forName(formalName);
        }
        catch (ClassNotFoundException e)
        {
          throw new InternalError(e.toString());
        }
      }
    case '[':
      // formal name for arrays example: "[Ljava.lang.String;"
      {
        String formalName;

        formalName = name.replace('/', '.');

        /*
        int indexL;

        formalName = name;
        indexL = formalName.lastIndexOf('[') + 1;
        if (formalName.charAt(indexL) == 'L') {
          // remove L and ;
          formalName = formalName.substring(0, indexL) +
            formalName.substring(indexL + 1, formalName.length() - 1);
          // '/' => '.'
          formalName = formalName.replace('/', '.');
        }
        */
        try
        {
          return Class.forName(formalName);
        }
        catch (ClassNotFoundException e)
        {
          throw new InternalError(e.toString());
        }
      }
    default:
      throw new InternalError("Unknown type:" + name);
    }
  }

  /**
   * Creates an array of <code>Class</code> that represents the
   * type described by <code>names</code>.  Names have same format
   * as method signature.
   */
  public static Class[] namesToClasses(String[] names)
  {
    Class[] classes;

    classes = new Class[names.length];
    for (int i = 0; i < names.length; i++)
    {
      classes[i] = typeToClass(names[i]);
    }
    return classes;
  }

  public static Class getReturnType(String descriptor)
  {
    return typeToClass(descriptor.substring(descriptor.indexOf(')') + 1));
  }

  public static Class[] getParameterTypes(String desc)
  {
    String[] names;
    int count;
    int i;
    String descriptor;

    // count them
    count = 0;
    i = 0;
    descriptor = desc.substring(desc.indexOf('(') + 1, desc.indexOf(')'));
    while (i < descriptor.length())
    {
      if (descriptor.charAt(i) == '[')
      {
        i++;
        continue;
      }
      if (descriptor.charAt(i) == 'L')
      {
        while (descriptor.charAt(++i) != ';')
        {
          // skip char
        }
      }
      count++;
      i++;
    }

    // parse them
    names = new String[count];
    for (i = 0; i < count; i++)
    {
      names[i] = "";
    }
    int index = 0;
    i = 0;
    while (i < descriptor.length())
    {
      if (descriptor.charAt(i) == '[')
      {
        names[index] += "[";
        i++;
        continue;
      }
      if (descriptor.charAt(i) == 'L')
      {
        String s;
        s = descriptor.substring(i, descriptor.indexOf(';', i) + 1);
        names[index] += s;
        i += s.length();
      }
      else
      {
        names[index] += descriptor.charAt(i);
        i++;
      }
      index++;
    }
    return namesToClasses(names);
  }

}
