/////////////////////////////////////////////////////////////////////////////
// AbstractMap.java -- Abstract implementation of most of Map
//
// Copyright (c) 1998 by Stuart Ballard (stuart.ballard@mcmail.com),
//                       Free Software Foundation, Inc.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU Library General Public License as published
// by the Free Software Foundation, version 2. (see COPYING.LIB)
//
// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU Library General Public License
// along with this program; if not, write to the Free Software Foundation
// Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/////////////////////////////////////////////////////////////////////////////

// TO DO:
// comments
// test suite

package java.util;

public abstract class AbstractMap implements Map {

  public void clear()
  {
    entrySet().clear();
  }

  public boolean containsKey( Object key )
  {
    Object k;
    Iterator entries = entrySet().iterator();
  
    while( entries.hasNext() )
    {
      k = ((Map.Entry)entries.next()).getKey();
      if( key == null ? k == null : key.equals( k ) )
	return true;
    }

    return false;
  }

  public boolean containsValue( Object value )
  {
    Object v;
    Iterator entries = entrySet().iterator();
  
    while( entries.hasNext() )
    {
      v = ((Map.Entry)entries.next()).getValue();
      if( value == null ? v == null : value.equals( v ) )
	return true;
    }

    return false; 
  }

  public abstract Set entrySet();

  public boolean equals( Object o )
  {
    if( this == o )
      return true;
    
    if( o == null || !( o instanceof Map ) )
      return false;
    
    Map m = (Map)o;
    if( m.size() != size() )
      return false;
    
    Object key, value1, value2;
    Map.Entry entry;
    Iterator entries = entrySet().iterator();
    while( entries.hasNext() )
    {
      entry = (Map.Entry)entries.next();
      key = entry.getKey();
      value1 = entry.getValue();
      value2 = m.get( key );
      
      if( !( ( value1 == null && value2 == null )
	     || value1.equals( value2 ) ) )
	return false;
    }

    return true;    
  }

  public Object get( Object key )
  {
    Object k;
    Map.Entry entry;
    Iterator entries = entrySet().iterator();
  
    while( entries.hasNext() )
    {
      entry = (Map.Entry)entries.next();
      k = entry.getKey();
      if( key == null ? k == null : key.equals( k ) )
	return entry.getValue();
    }

    return null;
  }

  public int hashCode()
  {
    int hashcode = 0;
    Iterator entries = entrySet().iterator();
  
    while( entries.hasNext() )
      hashcode += entries.next().hashCode();

    return hashcode;
  }

  public boolean isEmpty()
  {
    return size() == 0;
  }

  public Set keySet()
  {
    if( this.keySet == null )
    {
      this.keySet =
	new AbstractSet()
	{
	  public int size()
          {
	    return AbstractMap.this.size();
	  }
	  
	  public Iterator iterator()
          {
	    return new Iterator()
            {
	      Iterator map_iterator = AbstractMap.this.entrySet().iterator();
	      
	      public boolean hasNext()
              {
		return map_iterator.hasNext();
	      }

	      public Object next()
              {
		return ((Map.Entry)map_iterator.next()).getKey();
	      }
	      
	      public void remove()
              {
		map_iterator.remove();
	      }
	    };
	  }
	};
    }
    
    return this.keySet;    
  }

  public Object put( Object key, Object value )
  {
    throw new UnsupportedOperationException();
  }

  public void putAll( Map m )
  {
    Map.Entry entry;
    Iterator entries = m.entrySet().iterator();
    while( entries.hasNext() )
    {
      entry = (Map.Entry)entries.next();
      put( entry.getKey(), entry.getValue() );
    }
  }

  public Object remove( Object key )
  {
    Object k, value;
    Map.Entry entry;
    Iterator entries = entrySet().iterator();
  
    while( entries.hasNext() )
    {
      entry = (Map.Entry)entries.next();
      k = entry.getKey();
      if( key == null ? k == null : key.equals( k ) )
      {
	value = entry.getValue();
	entries.remove();
	return value;
      }
    }

    return null;    
  }

  public int size()
  {
    return entrySet().size();
  }

  public String toString()
  {
    Map.Entry entry;
    String rep = "AbstractMap< ";
    Iterator entries = entrySet().iterator();
  
    while( entries.hasNext() )
    {
      entry = (Map.Entry)entries.next();
      rep += entry.getKey() + " -> " + entry.getValue() + ", ";
    }

    return rep.substring( 0, rep.length() - 2 ) + " >";
  }

  public Collection values()
  {
    if( this.valueCollection == null )
    {
      this.valueCollection =
	new AbstractCollection()
	{
	  public int size()
          {
	    return AbstractMap.this.size();
	  }
	  
	  public Iterator iterator()
          {
	    return new Iterator()
            {
	      Iterator map_iterator = AbstractMap.this.entrySet().iterator();
	      
	      public boolean hasNext()
              {
		return map_iterator.hasNext();
	      }

	      public Object next()
              {
		return ((Map.Entry)map_iterator.next()).getValue();
	      }
	      
	      public void remove()
              {
		map_iterator.remove();
	      }
	    };
	  }
	};
    }
    
    return this.valueCollection;
  }


  private Collection valueCollection = null;
  private Set keySet = null;
}
