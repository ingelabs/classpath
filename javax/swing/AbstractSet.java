package javax.swing;

import java.util.*;


/**
 * Empty
 *
 * @author Ronald Veldema (rveldema@cs.vu.nl)
 */

public abstract class AbstractSet extends AbstractCollection implements Set
{
	boolean contained(Object []a1, Object b)
	{
		for (int i=0;i<a1.length;i++)
		{
			if (a1[i] == b)
				return true;
		}
		return false;
	}

	public boolean equals(Object o)
	{
		if (! (o instanceof AbstractSet))
			return false;
		AbstractSet s = (AbstractSet) o;

		if (s == this)
			return true;

		if (s.size() != size())
			return false;

		Object[] a1 = s.toArray();
		Object[] a2 = toArray();

		for (int i=0;i<a1.length;i++)
		{
			if (! contained(a2, a1[i]))
				return false;
		}
		return true;
	}

	public int hashCode()
	{
		int hash = 0;
		Object[] a1 = toArray();

		if (a1 == null)
			return 0;

		for (int i=0; i<a1.length; i++)
		{
			hash += a1[i].hashCode();
		}
		return hash;
	}

	public boolean removeAll(Collection c)
	{
		return false;
	}
}



