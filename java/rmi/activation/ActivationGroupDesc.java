/* ActivationGroupDesc.java -- the RMI activation group descriptor
   Copyright (c) 1996, 1997, 1998, 1999, 2004, 2006
   Free Software Foundation, Inc.

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


package java.rmi.activation;

import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.util.Properties;

/**
 * Contains information, necessary to create of recreate the activation objects.
 * The group descriptor contains:
 * <ul>
 * <li>The name of the group's class. This class is derived from the
 * {@link ActivationGroup}.</li>
 * <li>The group class code location.</li>
 * <li>The marshalled object that contains the group specific initialization
 * information</li>
 * </ul>
 * The groups are created by the {@link ActivationGroup#createGroup} method that
 * expectes the group class to have the two parameter constructor, the first
 * parameter being the {@link ActivationGroupID} and the second the
 * {@link MarshalledObject}.
 */
public final class ActivationGroupDesc
    implements Serializable
{
  /**
   * Use the SVUID for interoperability.
   */
  static final long serialVersionUID = - 4936225423168276595L;

  public static class CommandEnvironment
      implements Serializable
  {

    /**
     * Use the SVUID for interoperability.
     */
    static final long serialVersionUID = 6165754737887770191L;

    private String cmdpath;

    private String[] argv;

    public CommandEnvironment(String commandPatch, String[] args)
    {
      cmdpath = commandPatch;
      argv = args;
    }

    public String getCommandPath()
    {
      return cmdpath;
    }

    public String[] getCommandOptions()
    {
      return argv;
    }

    public boolean equals(Object obj)
    {
      if (! (obj instanceof CommandEnvironment))
        {
          return (false);
        }
      CommandEnvironment that = (CommandEnvironment) obj;

      if (! this.cmdpath.equals(that.cmdpath))
        {
          return (false);
        }

      int len = this.argv.length;
      if (len != that.argv.length)
        {
          return (false);
        }
      for (int i = 0; i < len; i++)
        {
          if (! this.argv[i].equals(that.argv[i]))
            {
              return (false);
            }
        }
      return (true);
    }

    public int hashCode()
    {
      return cmdpath.hashCode(); // Not a very good hash code.
    }

  }

  public ActivationGroupDesc(Properties overrides,
                             ActivationGroupDesc.CommandEnvironment cmd)
  {
    throw new Error("Not implemented");
  }

  public ActivationGroupDesc(String className, String location,
                             MarshalledObject data, Properties overrides,
                             ActivationGroupDesc.CommandEnvironment cmd)
  {
    throw new Error("Not implemented");
  }

  public String getClassName()
  {
    throw new Error("Not implemented");
  }

  public String getLocation()
  {
    throw new Error("Not implemented");
  }

  public MarshalledObject getData()
  {
    throw new Error("Not implemented");
  }

  public Properties getPropertyOverrides()
  {
    throw new Error("Not implemented");
  }

  public ActivationGroupDesc.CommandEnvironment getCommandEnvironment()
  {
    throw new Error("Not implemented");
  }

  public boolean equals(Object obj)
  {
    throw new Error("Not implemented");
  }

  public int hashCode()
  {
    throw new Error("Not implemented");
  }

}
