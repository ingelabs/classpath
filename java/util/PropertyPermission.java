/*
 * java.util.PropertyPermission: part of the Java Class Libraries project.
 * Copyright (C) 1999 Jochen Hoenicke
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
import java.security.Permission;
import java.security.BasicPermission;
import java.security.PermissionCollection;
import java.io.ObjectStreamField;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * This class represents the permission to access and modify a property.<br>
 *
 * The name is the name of the property, e.g. xxx.  You can also
 * use an asterisk "*" as described in BasicPermission <br>
 *
 * The action string is a comma-separated list if keywords.  There are
 * two possible actions:
 * <dl>
 * <dt>read</dt> 
 * <dd>Allows to read the property via <code>System.getProperty</code>.</dd>
 * <dt>write</dt> 
 * <dd>Allows to write the property via <code>System.setProperty</code>.</dd>
 * </dl>
 * 
 * The action string is case insensitive (it is converted to lower case).
 *
 * @see Permission
 * @see BasicPermission
 * @author Jochen Hoenicke 
 */
public final class PropertyPermission extends BasicPermission
{
  /**
   * @serialField action String
   *   The action string.
   */
  private static final ObjectStreamField[] serialPersistentFields =
  {
    new ObjectStreamField("action", String.class)
  };

  private static final long serialVersionUID = 885438825399942851L;

  private static final int READ  = 1;
  private static final int WRITE = 2;
  private transient int actions;

  private static String actionStrings[] = 
  { 
    "", "read", "write", "read,write" 
  };

  /**
   * Constructs a PropertyPermission witha he specified property.  Possible
   * actions are read and write.
   * @param name the name of the property.
   * @param actions the action string.
   * @exception IllegalArgumentException if name string contains an
   * illegal wildcard or actions string contains an illegal action
   */
  public PropertyPermission(String name, String actions)
  {
    super(name);
    setActions(actions.toLowerCase());
  }

  /**
   * Parse the action string and convert actions from external to internal
   * form.  This will set the internal actions field.
   * @param actions the action string.
   * @exception IllegalArgumentException if actions string contains an
   * illegal action */
  private void setActions(String actions)
  {
    this.actions = 0;
    StringTokenizer actionTokenizer = new StringTokenizer(actions, ",");
    while (actionTokenizer.hasMoreElements())
      {
	String anAction = actionTokenizer.nextToken();
	if ("read".equals(anAction))
	  this.actions |= READ;
	else if ("write".equals(anAction))
	  this.actions |= WRITE;
	else
	  throw new IllegalArgumentException("illegal action "+anAction);
      }
  }

  /**
   * Check if this permission implies p.  This returns true iff all of
   * the following conditions are true:
   * <ul>
   * <li> p is a PropertyPermission </li>
   * <li> this.getName() implies p.getName(),  
   *  e.g. <code>java.*</code> implies <code>java.home</code> </li>
   * <li> this.getActions is a subset of p.getActions </li>
   * </ul>
   */
  public boolean implies(Permission p)
  {
    if (!(p instanceof PropertyPermission))
      return false;
    
    // We have to check the actions.
    PropertyPermission pp = (PropertyPermission) p;
    if ((pp.actions & ~actions) != 0)
      return false;

    // BasicPermission checks for name.
    if (!super.implies(p))
      return false;

    return true;
  }

  /**
   * Returns the action string.  Note that this may differ from the string
   * given at the constructor:  The actions are converted to lowercase and
   * may be reordered.
   */
  public String getActions()
  {
    return actionStrings[actions];
  }

  /**
   * Reads an object from the stream. This converts the external to the
   * internal representation.
   */
  private void readObject(ObjectInputStream s) 
    throws IOException, ClassNotFoundException
  {
    ObjectInputStream.GetField fields = s.readFields();
    setActions((String) fields.get("actions", null));
  }

  /**
   * Writes an object to the stream. This converts the internal to the
   * external representation.
   */
  private void writeObject(ObjectOutputStream s) 
    throws IOException
  {
    ObjectOutputStream.PutField fields = s.putFields();
    fields.put("actions", getActions());
    s.writeFields();
  }

  /**
   * Returns a permission collection suitable to take
   * PropertyPermission objects.
   * @return a new empty PermissionCollection.  
   */
  public PermissionCollection newPermissionCollection() 
  {
    return new PermissionCollection() 
      {
	Vector permissions;
	boolean readOnly;
	
	public void add(Permission permission) 
	  {
	    if (readOnly)
	      throw new IllegalStateException("readonly");
	    // also check that permission is of correct type.
	    permissions.add((PropertyPermission) permission);
	  }
	
	public boolean implies(Permission permission)
	  {
	    PropertyPermission[] perms = (PropertyPermission[]) 
	      permissions.toArray(new PropertyPermission[permissions.size()]);
	    for (int i=0; i< perms.length; i++)
	      {
		if (perms[i].implies(permission))
		  return true;
	      }
	    return false;
	  }
	
	public Enumeration elements()
	  {
	    return permissions.elements();
	  }
      };
  }
}
