/* AccessibleText.java -- Java interface for aiding in accessibly rendering 
   other Java components
   Copyright (C) 2000 Free Software Foundation, Inc.

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

package javax.accessibility;

import javax.swing.text.AttributeSet;
import java.awt.Rectangle;
import java.awt.Point;

public abstract interface AccessibleText {
    public static final int CHARACTER = 1;
    public static final int WORD = 2;
    public static final int SENTENCE = 3;
    public abstract String getAfterIndex(int, int);
    public abstract String getAtIndex(int, int);
    public abstract String getBeforeIndex(int, int);
    public abstract int getCaretPosition();
    public abstract int getCharCount();
    public abstract AttributeSet getCharacterAttribute(int);
    public abstract Rectangle getCharacterBounds(int);
    public abstract int getIndexAtPoint(Point);
    public abstract String getSelectedText();
    public abstract int getSelectionEnd();
    public abstract int getSelectionStart();
}
