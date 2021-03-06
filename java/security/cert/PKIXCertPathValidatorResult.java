/* PKIXCertPathValidatorResult.java -- PKIX cert path builder result
   Copyright (C) 2003, 2015 Free Software Foundation, Inc.

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


package java.security.cert;

import java.security.PublicKey;

/**
 * Results returned by the {@link
 * CertPathValidator#validate(java.security.cert.CertPath,java.security.cert.CertPathParameters)}
 * method for PKIX {@link CertPathValidator}s.
 *
 * @see CertPathValidator
 */
public class PKIXCertPathValidatorResult implements CertPathValidatorResult
{

  // Fields.
  // ------------------------------------------------------------------------

  /** The trust anchor. */
  private final TrustAnchor trustAnchor;

  /** The root node of the policy tree. */
  private final PolicyNode policyTree;

  /** The subject's public key. */
  private final PublicKey subjectPublicKey;

  // Constructor.
  // ------------------------------------------------------------------------

  /**
   * Creates a new PKIXCertPathValidatorResult.
   *
   * @param trustAnchor      The trust anchor.
   * @param policyTree       The root node of the policy tree.
   * @param subjectPublicKey The public key.
   * @throws NullPointerException If either <i>trustAnchor</i> or
   *         <i>subjectPublicKey</i> is null.
   */
  public PKIXCertPathValidatorResult(TrustAnchor trustAnchor,
                                     PolicyNode policyTree,
                                     PublicKey subjectPublicKey)
  {
    if (trustAnchor == null || subjectPublicKey == null)
      throw new NullPointerException();
    this.trustAnchor = trustAnchor;
    this.policyTree = policyTree;
    this.subjectPublicKey = subjectPublicKey;
  }

  // Instance methods.
  // ------------------------------------------------------------------------

  /**
   * Returns the trust anchor.
   *
   * @return The trust anchor.
   */
  public TrustAnchor getTrustAnchor()
  {
    return trustAnchor;
  }

  /**
   * Returns the root node of the policy tree.
   *
   * @return The root node of the policy tree.
   */
  public PolicyNode getPolicyTree()
  {
    return policyTree;
  }

  /**
   * Returns the subject public key.
   *
   * @return The subject public key.
   */
  public PublicKey getPublicKey()
  {
    return subjectPublicKey;
  }

  /**
   * Returns a copy of this object.
   *
   * @return The copy.
   */
  @Override
  public Object clone()
  {
    return new PKIXCertPathValidatorResult(trustAnchor, policyTree,
                                           subjectPublicKey);
  }

  /**
   * Returns a printable string representation of this result.
   *
   * @return A printable string representation of this result.
   */
  @Override
  public String toString()
  {
    return "[ Trust Anchor=" + trustAnchor + "; Policy Tree="
      + policyTree + "; Subject Public Key=" + subjectPublicKey + " ]";
  }
}
