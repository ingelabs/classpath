/* ListPeer.java -- Interface for list box peer
   Copyright (C) 1999 Free Software Foundation, Inc.

This file is part of the non-peer AWT libraries of GNU Classpath.

This library is free software; you can redistribute it and/or modify
it under the terms of the GNU Library General Public License as published 
by the Free Software Foundation, either version 2 of the License, or
(at your option) any later verion.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Library General Public License for more details.

You should have received a copy of the GNU Library General Public License
along with this library; if not, write to the Free Software Foundation
Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA. */


package java.awt.peer;

import java.awt.Dimension;

public interface ListPeer extends ComponentPeer
{

public abstract void add(String item, int index);
public abstract void addItem(String item, int index);
public abstract void clear();
public abstract void delItems(int start_index, int end_index);
public abstract void deselect(int index);
public abstract int[] getSelectedIndexes();
public abstract void makeVisible(int index);
public abstract Dimension minimumSize(int s);
public abstract Dimension preferredSize(int s);
public abstract void removeAll();
public abstract void select(int index);
public abstract void setMultipleMode(boolean multi);
public abstract void setMultipleSelections(boolean multi);

} // interface ListPeer 

