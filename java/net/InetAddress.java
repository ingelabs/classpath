/*************************************************************************
/* InetAddress.java -- Class to model an Internet address
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

package java.net;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
  * This class models an Internet address.  It does not have a public
  * constructor.  Instead, new instances of this objects are created 
  * using the static methods getLocalHost(), getByName(), and
  * getAllByName().
  * <p>
  * This class fulfills the function of the C style functions gethostname(), 
  * gethostbyname(), and gethostbyaddr().  It resolves Internet DNS names
  * into their corresponding numeric addresses and vice versa.
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public final class InetAddress implements Serializable
{

/*************************************************************************/

/*
 * Static Variables
 */

// Static Initializer to load the shared library needed for name resolution
static
{
  System.loadLibrary("javanet");
}

/**
  * The default DNS hash table size
  */
private static final int DEFAULT_CACHE_SIZE = 400;

/**
  * The default caching period in minutes
  */
private static final int DEFAULT_CACHE_PERIOD = (4 * 60);

/**
  * Percentage of cache entries to purge when the table gets full
  */
private static final int DEFAULT_CACHE_PURGE_PCT = 30;

/**
  * The special IP address INADDR_ANY
  */
private static InetAddress inaddr_any;

/**
  * The size of the cache
  */
private static int cache_size = 0;

/**
  * The length of time we will continue to read the address from cache
  * before forcing another lookup
  */
private static int cache_period = 0;

/**
  * What percentage of the cache we will purge if it gets full
  */
private static int cache_purge_pct = 0;

/**
  * Hashtable to use as DNS lookup cache
  */
private static Hashtable cache;

// Static initializer for the cache
static
{
  // Look for properties that override default caching behavior
  cache_size = Integer.getInteger("gnu.java.net.dns_cache_size",
                                  DEFAULT_CACHE_SIZE).intValue();
  cache_period = Integer.getInteger("gnu.java.net.dns_cache_period",
                                  DEFAULT_CACHE_PERIOD * 60 * 1000).intValue();

  cache_purge_pct =  Integer.getInteger("gnu.java.net.dns_cache_purge_pct",
                                        DEFAULT_CACHE_PURGE_PCT).intValue();

  // Fallback to  defaults if necessary
  if ((cache_purge_pct < 1) || (cache_purge_pct > 100))
      cache_purge_pct = DEFAULT_CACHE_PURGE_PCT;

  // Create the cache
  if (cache_size != 0)
      cache = new Hashtable(cache_size);
}      

/*************************************************************************/

/*
 * Instance variables
 */

/**
  * An array of octets representing an IP address
  */
int[] my_ip;

/**
  * The name of the host for this address
  */
String hostname;

/**
  * Backup hostname alias for this address.
  */
String hostname_alias;

/**
  * The time this address was looked up
  */
long lookup_time;

/*************************************************************************/

/*
 * Class Methods
 */

/**
  * This method checks the DNS cache to see if we have looked this hostname
  * up before. If so, we return the cached addresses unless it has been in the
  * cache too long.
  *
  * @param hostname The hostname to check for
  *
  * @return The InetAddress for this hostname or null if not available
  */
private static synchronized InetAddress[]
checkCacheFor(String hostname)
{
  InetAddress[] addrs = null;

  if (cache_size == 0)
    return(null);

  Object obj = cache.get(hostname);
  if (obj == null)
    return(null);

  if (obj instanceof InetAddress[])
    addrs = (InetAddress[])obj;

  if (addrs == null)
    return(null);

  if (cache_period != -1)
    if ((System.currentTimeMillis() - addrs[0].lookup_time) > cache_period)
      {
        cache.remove(hostname);
        return(null);
      }

  return(addrs);
}

/*************************************************************************/

/**
  * This method adds an InetAddress object to our DNS cache.  Note that
  * if the cache is full, then we run a purge to get rid of old entries.
  * This will cause a performance hit, thus applications using lots of
  * lookups should set the cache size to be very large.
  *
  * @param hostname The hostname to cache this address under
  * @param addr The InetAddress or InetAddress array to store
  */  
private static synchronized void
addToCache(String hostname, Object addr)
{
  if (cache_size == 0)
    return;

  // Check to see if hash table is full
  if (cache_size != -1)
    if (cache.size() == cache_size)
      {
        ; //*** Add code to purge later.
      } 

  cache.put(hostname, addr);
} 

/*************************************************************************/

/**
  * Returns the special address INADDR_ANY used for binding to a local
  * port on all IP addresses hosted by a the local host.
  *
  * @return An InetAddress object representing INDADDR_ANY
  *
  * @exception UnknownHostException If an error occurs
  */
static InetAddress
getInaddrAny() throws UnknownHostException
{
  if (inaddr_any == null)
    {
      int[] addr = lookupInaddrAny();
      inaddr_any = new InetAddress(addr);
    }

  return(inaddr_any);
}

/*************************************************************************/

/**
  * Returns an array of InetAddress objects representing all the host/ip
  * addresses of a given host, given the host's name.  This name can be
  * either a hostname such as "www.urbanophile.com" or an IP address in
  * dotted decimal format such as "127.0.0.1".  If the value is null, the
  * hostname of the local machine is supplied by default.
  *
  * @param hostname The name of the desired host, or null for the local machine
  *
  * @return All addresses of the host as an array of InetAddress's
  *
  * @exception UnknownHostException If no IP address can be found for the given hostname
  */
public static InetAddress[]
getAllByName(String hostname) throws UnknownHostException
{
  // Default to current host if necessary
  if (hostname == null)
    {
      InetAddress addr = getLocalHost();
      return(getAllByName(addr.getHostName()));
    }

  // Check the cache for this host before doing a lookup
  InetAddress[] addrs = checkCacheFor(hostname);
  if (addrs != null)
    return(addrs);

  // Not in cache, try the lookup
  int[][] iplist = getHostByName(hostname);
  if (iplist.length == 0)
    throw new UnknownHostException(hostname);

  addrs = new InetAddress[iplist.length];

  for (int i = 0; i < iplist.length; i++)
    {
      if (iplist[i].length != 4)
        throw new UnknownHostException(hostname);

      // Don't store the hostname in order to force resolution of the
      // canonical names of these ip's when the user asks for the hostname
      // But do specify the host alias so if the IP returned won't
      // reverse lookup we don't throw an exception.
      addrs[i] = new InetAddress(iplist[i], null, hostname);
    } 

  addToCache(hostname, addrs);

  return(addrs);      
}

/*************************************************************************/

/**
  * Returns an InetAddress object representing the IP address of the given
  * hostname.  This name can be either a hostname such as "www.urbanophile.com"
  * or an IP address in dotted decimal format such as "127.0.0.1".  If the
  * hostname is null, the hostname of the local machine is supplied by
  * default.  This method is equivalent to returning the first element in
  * the InetAddress array returned from GetAllByName.
  *
  * @param hostname The name of the desired host, or null for the local machine
  *
  * @return The address of the host as an InetAddress
  *
  * @exception UnknownHostException If no IP address can be found for the given hostname
  */
public static InetAddress
getByName(String hostname) throws UnknownHostException
{
  // Default to current host if necessary
  if (hostname == null)
    return(getLocalHost());

  // First, check to see if it is an IP address.  If so, then don't 
  // do a DNS lookup.
  StringTokenizer st = new StringTokenizer(hostname, ".");
  if (st.countTokens() == 4)
   {
     int i;
     int[] ip = new int[4];
     for (i = 0; i < 4; i++)
       {
         try
           {
             ip[i] = Integer.parseInt(st.nextToken());
             if ((ip[i] < 0) || (ip[1] > 255))
               break;
           }
         catch (NumberFormatException e)
           {
             break;
           }
       }
     if (i == 4)
       {
         return(new InetAddress(ip));
       }
   }

  // Wasn't and IP, so try the lookup
  InetAddress[] addrs = getAllByName(hostname);
  
  return(addrs[0]);
}

/*************************************************************************/

/**
  * Returns an InetAddress object representing the address of the current
  * host.
  *
  * @return The local host's address
  *
  * @exception UnknownHostException If an error occurs
  */
public static InetAddress
getLocalHost() throws UnknownHostException
{
  String hostname = getLocalHostName();

  return(getByName(hostname));
}

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes this object's my_ip instance variable from the passed in
  * int array.  Note that this constructor is protected and is called
  * only by static methods in this class.
  *
  * @param addr The IP number of this address as an array of bytes
  */
private
InetAddress(int[] addr)
{
  this(addr, null, null);
}

/*************************************************************************/

/**
  * Initializes this object's my_ip instance variable from the passed in
  * int array.  Note that this constructor is protected and is called
  * only by static methods in this class.
  *
  * @param addr The IP number of this address as an array of bytes
  * @param hostname The hostname of this IP address.
  */
private
InetAddress(int[] addr, String hostname)
{
  this(addr, hostname, null);
}

/*************************************************************************/

/**
  * Initializes this object's my_ip instance variable from the passed in
  * int array.  Note that this constructor is protected and is called
  * only by static methods in this class.
  *
  * @param addr The IP number of this address as an array of bytes
  * @param hostname The hostname of this IP address.
  * @param hostname_alias A backup hostname to use if hostname is null to prevent reverse lookup failures
  */
private
InetAddress(int[] addr, String hostname, String hostname_alias)
{
  my_ip = new int[addr.length];

  for (int i = 0; i < addr.length; i++)
    my_ip[i] = addr[i];    

  this.hostname = hostname;
  this.hostname_alias = hostname_alias;
  lookup_time = System.currentTimeMillis();
}

/*************************************************************************/

/**
  * Instance Methods
  */

/**
  * Tests this address for equality against another InetAddress.  The two
  * addresses are considered equal if they contain the exact same octets.
  * This implementation overrides Object.equals()
  *
  * @param addr The address to test for equality
  *
  * @return true if the passed in object's address is equal to this one's, false otherwise
  */
public boolean
equals(Object addr)
{
  if (!(addr instanceof InetAddress))
    return(false);

  byte[] test_ip = ((InetAddress)addr).getAddress();

  if (test_ip.length != my_ip.length)
    return(false);

  for (int i = 0; i < my_ip.length; i++)
    if (test_ip[i] != (byte)my_ip[i])
       return(false);

  return(true);
}

/*************************************************************************/

/**
  * Returns the IP address of this object as a int array.
  *
  * @return IP address
  */
public byte[]
getAddress()
{
  byte[] addr = new byte[my_ip.length];

  for (int i = 0; i < my_ip.length; i++)
    {
      addr[i] = (byte)my_ip[i];
    }

  return(addr);
}

/*************************************************************************/

/**
  * Returns the IP address of this object as a String.  The address is in 
  * the dotted octet notation, for example, "127.0.0.1".
  *
  * @return The IP address of this object in String form
  */
public String
getHostAddress()
{
  StringBuffer addr = new StringBuffer();

  for (int i = 0; i < my_ip.length; i++)
    {
      addr.append((int)my_ip[i]);
      if (i < (my_ip.length - 1))
        addr.append(".");
    }

  return(addr.toString());
}

/*************************************************************************/

/**
  * Returns the hostname for this address.  This will return the IP address
  * as a String if there is no hostname available for this address
  *
  * @return The hostname for this address
  */
public String
getHostName()
{
  if (hostname != null)
    return(hostname);

  try
    {
      hostname = getHostByAddr(my_ip);  
      return(hostname);
    }
  catch (UnknownHostException e)
    {
      if (hostname_alias != null)
         return(hostname_alias);
      else
         return(getHostAddress());
    }
}

/*************************************************************************/

/**
  * Returns a hash value for this address.  Useful for creating hash
  * tables.  Overrides Object.hashCode()
  *
  * @return A hash value for this address.
  */
public int
hashCode()
{
  long val1 = 0, val2 = 0;

  // Its obvious here that I have no idea how to generate a good
  // hash key
  for (int i = 0; i < my_ip.length; i++)
    val1 = val1 + (my_ip[i] << ((my_ip.length - i) / 8));

  for (int i = 0; i < my_ip.length; i++)
    val2 = val2 + (my_ip[i] * 10 * i);

  val1 = (val1 >> 1) ^ val2;

  return((int)val1);
}

/*************************************************************************/

/**
  * Returns true if this address is a multicast address, false otherwise.
  * An address is multicast if the high four bits are "1110".  These are
  * also known as "Class D" addresses.
  *
  * @return true if mulitcast, false if not
  */
public boolean
isMulticastAddress()
{
  if (my_ip.length == 0)
    return(false);

  // Mask against high order bits of 1110
  if ((my_ip[0] & 0xF0) == 224)
    return(true);

  return(false);
}

/*************************************************************************/

/**
  * Converts this address to a String.  This string contains the IP in
  * dotted decimal form. For example: "127.0.0.1"  This method is equivalent
  * to getHostAddress() and overrides Object.toString()
  *
  * @return This address in String form
  */
public String
toString()
{
  return(getHostAddress());
}

/*************************************************************************/

/*
 * Native Methods
 */

/**
  * This native method looks up the hostname of the local machine
  * we are on.  If the actual hostname cannot be determined, then the
  * value "localhost" we be used.  This native method wrappers the
  * "gethostname" function.
  *
  * @return The local hostname.
  */
private static native String getLocalHostName();

/*************************************************************************/

/**
  * Returns the value of the special address INADDR_ANY
  */
private static native int[] lookupInaddrAny() throws UnknownHostException;

/*************************************************************************/

/**
  * This method returns the hostname for a given IP address.  It will
  * throw an UnknownHostException if the hostname cannot be determined.
  *
  * @param ip The IP address as a int arrary
  * 
  * @return The hostname
  *
  * @exception UnknownHostException If the reverse lookup fails
  */
private static native String getHostByAddr(int[] ip) throws UnknownHostException;

/*************************************************************************/

/**
  * Returns a list of all IP addresses for a given hostname.  Will throw
  * an UnknownHostException if the hostname cannot be resolved.
  */
private static native int[][] getHostByName(String hostname) throws UnknownHostException;

} // class InetAddress


