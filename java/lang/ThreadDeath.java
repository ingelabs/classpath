/*
 * java.lang.ThreadDeath: part of the Java Class Libraries project.
 * Copyright (C) 1998 Free Software Foundation
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package java.lang;

/**
 ** ThreadDeath is thrown in a thread when someone calls <CODE>stop()</CODE> on that thread.
 **
 ** <B>Important:</B> Make sure you rethrow this exception if you catch it.  If you don't, the thread will not die.
 **
 ** @author John Keiser
 ** @version 1.1.0, 5 Feb 1998
 ** @since JDK1.0
 ** @see java.lang.Thread#stop()
 **/

public class ThreadDeath extends Error {
}
