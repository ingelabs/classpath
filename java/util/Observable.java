/* java.util.Observable
   Copyright (C) 1999, 2000 Free Software Foundation, Inc.

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

   As a special exception, if you link this library with other files to
   produce an executable, this library does not by itself cause the
   resulting executable to be covered by the GNU General Public License.
   This exception does not however invalidate any other reasons why the
   executable file might be covered by the GNU General Public License. */


package java.util;

public class Observable
{
  private boolean changed;
  private Vector observers;

  /**
   * Constructs an Observable with zero Observers.
   */
  public Observable()
  {
    changed = false;
    observers = new Vector();
  }

  /**
   * Adds an Observer.
   *
   * @param observer Observer to add.
   */
  public void addObserver(Observer observer)
  {
    observers.add(observer);
  }

  /**
   * Reset this Observable's state to unchanged.
   */
  protected void clearChanged()
  {
    changed = false;
  }

  /**
   * @return Number of Observers for this Observable.
   */
  public int countObservers()
  {
    return observers.size();
  }

  /**
   * Deletes an Observer of this Observable.
   *
   * @param victim Observer to delete.
   */
  public void deleteObserver(Observer victim)
  {
    observers.remove(victim);
  }

  /**
   * Deletes all Observers of this Observable.
   */
  public void deleteObservers()
  {
    observers.removeAllElements();
  }

  /**
   * @return Whether or not this Observable has changed.
   */
  public boolean hasChanged()
  {
    return changed;
  }

  /**
   * Tell Observers that this Observable has changed, then
   * resets state to unchanged.
   */
  public void notifyObservers()
  {
    notifyObservers(null);
  }

  /**
   * @param obj Arguement to Observer's update method.
   */
  public void notifyObservers(Object obj)
  {
    if (!hasChanged())
      return;
    Vector ob1 = (Vector) observers.clone();

    for (int i = 0; i < ob1.size(); i++)
      ((Observer) ob1.elementAt(i)).update(this, obj);

    clearChanged();
  }

  /**
   * Marks this Observable as having changed.
   */
  protected void setChanged()
  {
    changed = true;
  }
}
