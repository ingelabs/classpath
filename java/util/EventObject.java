/**
*EventObject.java
*
*@author Daniel Rall (<a href="mailto:dlr@west.net">dlr@west.net</a>, <a href="http://209.146.23.49/">Fine Malt Coding</a>)
*@version 1.0b
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

import java.io.Serializable;

public class EventObject implements Serializable
{
	private static final long serialVersionUID = 5516075349620653480L;
	protected transient Object source;
	
	/**
	*Constructs an EventObject with the specified source.
	*/
	public EventObject( Object source )
	{
		this.source = source;
	}

	/**
	*@return The source of the Event.
	*/
	public Object getSource() { return source; }
	
	/**
	*@return String representation of the Event.
	*@override toString in class Object
	*/
	public String toString() { return source.toString(); }
}
