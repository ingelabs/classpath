/////////////////////////////////////////////////////////////////////////////
// List.java -- An ordered collection which allows indexed access
//
// Copyright (c) 1998 by Stuart Ballard (stuart.ballard@mcmail.com)
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
// ~ Doc comments for everything.

package java.util;

public interface List extends Collection {
  void add(int index, Object o);
  boolean add(Object o);
  boolean addAll(int index, Collection c);
  boolean addAll(Collection c);
  void clear();
  boolean contains(Object o);
  boolean containsAll(Collection c);
  boolean equals(Object o);
  Object get(int index);
  int hashCode();
  int indexOf(Object o);
  int indexOf(Object o, int index);
  boolean isEmpty();
  Iterator iterator();
  int lastIndexOf(Object o);
  int lastIndexOf(Object o, int index);
  ListIterator listIterator();
  ListIterator listIterator(int index);
  Object remove(int index);
  boolean remove(Object o);
  boolean removeAll(Collection c);
  void removeRange(int start, int end);
  boolean retainAll(Collection c);
  Object set(int index, Object o);
  int size();
  Object[] toArray();
}
