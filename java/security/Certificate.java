/*************************************************************************
/* Certificate.java -- Interface for modeling digital certificates
/*
/* Copyright (c) 1998 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This library is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later verion.
/*
/* This library is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this library; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.security;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
  * This interface models a digital certificate which verifies the 
  * authenticity of a party.  This class simply allows certificate
  * information to be queried, it does not guarantee that the certificate
  * is valid.
  * <p>
  * This class is deprecated in favor of the new java.security.cert package.
  * It exists for backward compatibility only.
  * 
  * @deprecated
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface Certificate
{

/**
  * This method returns the <code>Principal</code> that is guaranteeing
  * this certificate.
  *
  * @return The <code>Principal</code> guaranteeing the certificate
  */
public abstract Principal
getGuarantor();

/*************************************************************************/

/**
  * This method returns the <code>Principal</code> being guaranteed by
  * this certificate.
  *
  * @return The <code>Principal</code> guaranteed by this certificate.
  */
public abstract Principal
getPrincipal();

/*************************************************************************/

/**
  * This method returns the public key for the <code>Principal</code> that
  * is being guaranteed.
  *
  * @return The <code>PublicKey</code> of the <code>Principal</code> being guaranteed
  */
public abstract PublicKey
getPublicKey();

/*************************************************************************/

/**
  * This method returns the encoding format of the certificate (e.g., "PGP",
  * "X.509").  This format is used by the <code>encode</code. and
  * <code>decode</code> methods.
  *
  * @return The encoding format being used
  */
public abstract String
getFormat();

/*************************************************************************/

/**
  * This method writes the certificate to an <code>OutputStream</code> in
  * a format that can be understood by the <code>decode</code> method.
  *
  * @param out The <code>OutputStream</code> to write to.
  *
  * @exception KeyException If there is a problem with the internals of this certificate
  * @exception IOException If an error occurs writing to the stream.
  */
public abstract void
encode(OutputStream out) throws KeyException, IOException;

/*************************************************************************/

/**
  * This method reads an encoded certificate from an <code>InputStream</code>.
  *
  * @param in The <code>InputStream</code> to read from.
  *
  * @param KeyException If there is a problem with the certificate data
  * @param IOException If an error occurs reading from the stream.
  */
public abstract void
decode(InputStream in) throws KeyException, IOException;

/*************************************************************************/

/**
  * This method returns a <code>String</code> representation of the contents
  * of this certificate.
  *
  * @param detail <code>true</code> to provided detailed information about this certificate, <code>false</code> otherwise
  */
public abstract String
toString(boolean detail);

} // interface Certificate

