/**
 * Observable.java
 *
 * Extended when an object wants to be Observable.
 *
 * @author Daniel Rall (<a href="mailto:dlr@west.net">dlr@west.net</a>, <a href="http://www.finemaltcoding.com/">Fine Malt Coding</a>)
 * @version 1.0b
 */

/////////////////////////////////////////////////////////////////////////////
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

package java.util;

public class Observable
{
	private static final short INITIAL_SIZE = 4;
	
	private boolean changed;
	private Vector observers;
	
	/**
	 * Constructs an Observable with 0 Observers.  A resizable buffer is used to
	 * store Observer references, and is initialized to a small capacity.
	 */
	public Observable()
	{
		changed = false;
		observers = new Vector(INITIAL_SIZE);
	}
	
	/**
	 * Adds an Observer. Does not allow duplicates.
	 *
	 * @param observer Observer to add.
	 */
	public void addObserver( Observer observer )
	{
		if ( !observers.contains(observer) ) { observers.add(observer); }
	}
	
	/**
	 * Reset this Observable's state to unchanged.
	 */
	protected void clearChanged() { changed = false; }
	
	/**
	 * @return Number of Observers for this Observable.
	 */
	public int countObservers() { return observers.size(); }
	
	/**
	 * Deletes an Observer of this Observable.
	 *
	 * @param victim Observer to delete.
	 */
	public void deleteObserver( Observer victim ) { observers.remove(victim); }
	
	/**
	 * Deletes all Observers of this Observable.
	 */
	public void deleteObservers()
	{
		Enumeration enum = observers.elements();
		
		while ( enum.hasMoreElements() )
		{
			deleteObserver( (Observer)enum.nextElement() );
		}
	}

	/**
	 * @return Whether or not this Observable has changed.
	 */
	public boolean hasChanged() { return changed; }
	
	/**
	 * Tell Observers that this Observable has changed, then
	 * resets state to unchanged.
	 */
	public void notifyObservers() { notifyObservers(null); }
	
	/**
	 * @param obj Arguement to Observer's update method.
	 */
	public void notifyObservers( Object obj )
	{
		if ( hasChanged() )
		{
			Enumeration enum = observers.elements();
			
			while ( enum.hasMoreElements() )
			{
				( (Observer)enum.nextElement() ).update(this, obj);;
			}
			
			clearChanged();
		}
	}
	
	/**
	 * Marks this Observable as having changed.
	 */
	protected void setChanged() { changed = true; }
}