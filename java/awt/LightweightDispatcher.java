/* LightweightDispatcher.java -- Dispatches mouse events to lightweights
   Copyright (C) 2006 Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package java.awt;

import gnu.java.awt.AWTUtilities;

import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

/**
 * Redispatches mouse events to lightweight components. The native peers know
 * nothing about the lightweight components and thus mouse events are always
 * targetted at Windows or heavyweight components. This class listenes directly
 * on the eventqueue and dispatches mouse events to lightweight components.
 *
 * @author Roman Kennke (kennke@aicas.com)
 */
class LightweightDispatcher
  implements AWTEventListener
{

  /**
   * The component that is the start of a mouse dragging. All MOUSE_DRAGGED
   * events that follow the initial press must have the source set to this,
   * as well as the MOUSE_RELEASED event following the dragging.
   */
  private Component dragTarget;

  /**
   * The last mouse event target. If the target changes, additional
   * MOUSE_ENTERED and MOUSE_EXITED events must be dispatched.
   */
  private Component lastTarget;

  /**
   * Receives notification if a mouse event passes along the eventqueue.
   *
   * @param event the event
   */
  public void eventDispatched(AWTEvent event)
  {
    if (event instanceof MouseEvent && event.getSource() instanceof Window)
      {
        MouseEvent mouseEvent = (MouseEvent) event;
        handleMouseEvent(mouseEvent);
      }
  }

  /**
   * Handles all mouse events that are targetted at toplevel containers
   * (Window instances) and dispatches them to the correct lightweight child.
   *
   * @param ev the mouse event
   */
  private void handleMouseEvent(MouseEvent ev)
  {
    Window window = (Window) ev.getSource();
    Component target = window.findComponentAt(ev.getX(), ev.getY());
    if (target != null && target.isLightweight())
      {
        // Dispatch additional MOUSE_EXITED and MOUSE_ENTERED if event target
        // is different from the last event target.
        if (target != lastTarget)
          {
            if (lastTarget != null)
              {
                Point p1 = AWTUtilities.convertPoint(window, ev.getX(),
                                                     ev.getY(), lastTarget);
                MouseEvent mouseExited =
                  new MouseEvent(lastTarget, MouseEvent.MOUSE_EXITED,
                                 ev.getWhen(), ev.getModifiers(), p1.x, p1.y,
                                 ev.getClickCount(), ev.isPopupTrigger());
                lastTarget.dispatchEvent(mouseExited);
              }
            Point p = AWTUtilities.convertPoint(window, ev.getX(), ev.getY(),
                                                target);
            MouseEvent mouseEntered =
              new MouseEvent(target, MouseEvent.MOUSE_ENTERED, ev.getWhen(),
                             ev.getModifiers(), p.x, p.y, ev.getClickCount(),
                             ev.isPopupTrigger());
            target.dispatchEvent(mouseEntered);
          }
        lastTarget = target;

        
        switch (ev.getID())
        {
          case MouseEvent.MOUSE_PRESSED:
            dragTarget = target;
            break;
          case MouseEvent.MOUSE_RELEASED:
            if (dragTarget != null)
              target = dragTarget;
            dragTarget = null;
            break;
          case MouseEvent.MOUSE_DRAGGED:
            target = dragTarget;
            break;
          default:
            // Do nothing in other cases.
            break;
        }

        Point targetCoordinates =
          AWTUtilities.convertPoint(window, ev.getX(), ev.getY(), target);
        int dx = targetCoordinates.x - ev.getX();
        int dy = targetCoordinates.y - ev.getY();
        ev.translatePoint(dx, dy);
        ev.setSource(target);
        target.dispatchEvent(ev);

        // We reset the event, so that the normal event dispatching is not
        // influenced by this modified event.
        ev.setSource(window);
        ev.translatePoint(-dx, -dy);
      }
  }
}
