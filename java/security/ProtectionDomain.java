/* ProtectionDomain.java -- A security domain
   Copyright (C) 1998, 2003, Free Software Foundation, Inc.

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

package java.security;

/**
 * <p>This <code>ProtectionDomain</code> class encapsulates the characteristics
 * of a domain, which encloses a set of classes whose instances are granted a
 * set of permissions when being executed on behalf of a given set of
 * <i>Principals</i>.
 *
 * <p>A static set of permissions can be bound to a <code>ProtectionDomain</code>
 * when it is constructed; such permissions are granted to the domain regardless
 * of the {@link Policy} in force. However, to support dynamic security
 * policies, a <code>ProtectionDomain</code> can also be constructed such that
 * it is dynamically mapped to a set of permissions by the current {@link
 * Policy} whenever a permission is checked.</p>
 *
 * @author Aaron M. Renn (arenn@urbanophile.com)
 * @version 0.0
 */
public class ProtectionDomain
{
  /** This is the <code>CodeSource</code> for this protection domain. */
  private CodeSource code_source;

  /** This is the set of permissions granted to this domain. */
  private PermissionCollection perms;

  /**
   * Creates a new <code>ProtectionDomain</code> with the given {@link
   * CodeSource} and {@link Permissions}. If the permissions object is not
   * <code>null</code>, then <code>setReadOnly()</code> will be called on the
   * passed in {@link Permissions} object. The only permissions granted to this
   * domain are the ones specified; the current {@link Policy} will not be
   * consulted.
   *
   * @param codesource the codesource associated with this domain.
   * @param permissions the permissions granted to this domain
   */
  public ProtectionDomain(CodeSource codesource, PermissionCollection permissions)
  {
    this.code_source = codesource;
    this.perms = permissions;
    if (permissions != null)
      permissions.setReadOnly();
  }

  /**
   * Returns the {@link CodeSource} of this domain.
   *
   * @return the {@link CodeSource} of this domain which may be <code>null</code>.
   * @since 1.2
   */
  public final CodeSource getCodeSource()
  {
    return code_source;
  }

  /**
   * Returns the static permissions granted to this domain.
   *
   * @return the static set of permissions for this domain which may be
   * <code>null</code>.
   * @see Policy#refresh()
   * @see Policy#getPermissions(ProtectionDomain)
   */
  public final PermissionCollection getPermissions()
  {
    return perms;
  }

  /**
   * <p>Check and see if this <code>ProtectionDomain</code> implies the
   * permissions expressed in the <code>Permission</code> object.</p>
   *
   * <p>The set of permissions evaluated is a function of whether the
   * <code>ProtectionDomain</code> was constructed with a static set of
   * permissions or it was bound to a dynamically mapped set of permissions.</p>
   *
   * <p>If the <code>ProtectionDomain</code> was constructed to a statically
   * bound {@link PermissionCollection} then the permission will only be checked
   * against the {@link PermissionCollection} supplied at construction.</p>
   *
   * <p>However, if the <code>ProtectionDomain</code> was constructed with the
   * constructor variant which supports dynamically binding permissions, then
   * the permission will be checked against the combination of the
   * {@link PermissionCollection} supplied at construction and the current
   * {@link Policy} binding.
   *
   * @param permission the {@link Permission} object to check.
   * @return <code>true</code> if <code>permission</code> is implicit to this
   * <code>ProtectionDomain</code>.
   */
  public boolean implies(Permission permission)
  {
    PermissionCollection pc = getPermissions();
    if (pc == null)
      return (false);

    return pc.implies(permission);
  }

  /**
   * Convert a <code>ProtectionDomain</code> to a String.
   *
   * @return a string representation of the object.
   */
  public String toString()
  {
    String linesep = System.getProperty("line.separator");
    StringBuffer sb = new StringBuffer("");
    sb.append("ProtectionDomain (" + linesep);
    if (code_source == null)
      sb.append("CodeSource:null" + linesep);
    else
      sb.append(code_source + linesep);
    sb.append(perms);
    sb.append(linesep + ")" + linesep);

    return sb.toString();
  }
}
