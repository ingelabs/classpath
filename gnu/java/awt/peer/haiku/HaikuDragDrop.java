/* HaikuDragDrop.java -- drag and drop for the Haiku AWT peer
   Copyright (C) 2026 Free Software Foundation, Inc.

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

package gnu.java.awt.peer.haiku;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.dnd.peer.DropTargetContextPeer;

/**
 * Drag and drop. All drags, also between Java components, go through
 * Haiku's drag system (BView::DragMessage), so Java can drag to and from
 * Tracker and other applications.
 *
 * A Java drag carries "be:jdnd" (a drag id) and "be:jdnd_vm" (a number
 * for this VM). When it is dropped on a Java window of the same VM, the
 * drop target gets the original Transferable and the drag source learns
 * the result. For drops on other applications the result is not known;
 * the source is told that the copy succeeded.
 */
final class HaikuDragDrop
{

  private static final int B_SHIFT_KEY = 0x1;
  private static final int B_CONTROL_KEY = 0x4;
  private static final int B_OPTION_KEY = 0x40;

  /** Marks the drags of this VM. */
  static final int VM_TOKEN = (int) (System.currentTimeMillis() ^ 0x5a3c96e1);

  // The drag that this VM started, if any.
  private static int nextDragId = 1;
  private static int dragId;
  private static DragSourceContext dragContext;
  private static boolean dragFinished = true;

  // The drop target under the mouse.
  private static DropTarget target;
  private static Component targetComponent;
  private static DropContextPeer targetPeer;

  private HaikuDragDrop()
  {
  }

  // Drag source.

  static DragSourceContextPeer createDragSourceContextPeer()
  {
    return new SourcePeer();
  }

  private static final class SourcePeer
    implements DragSourceContextPeer
  {
    private Cursor cursor;

    public void startDrag(DragSourceContext context, Cursor c, Image image,
                          Point offset)
    {
      cursor = c;
      Component comp = context.getComponent();
      Window w = comp instanceof Window ? (Window) comp
                 : javax.swing.SwingUtilities.getWindowAncestor(comp);
      if (w == null || ! (w.getPeer() instanceof HaikuWindowPeer))
        throw new InvalidDnDOperationException("no Haiku window");
      HaikuWindowPeer wp = (HaikuWindowPeer) w.getPeer();
      int id;
      synchronized (HaikuDragDrop.class)
        {
          id = nextDragId++;
          dragId = id;
          dragContext = context;
          dragFinished = false;
        }
      Transferable t = context.getTransferable();
      boolean files = t.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
      long msg = HaikuTransferable.toMessage(t, files
                                                ? HaikuNative.B_SIMPLE_DATA
                                                : HaikuNative.B_MIME_DATA);
      HaikuNative.msgAddInt(msg, "be:jdnd", id);
      HaikuNative.msgAddInt(msg, "be:jdnd_vm", VM_TOKEN);
      HaikuNative.msgAddInt(msg, "be:jdnd_actions",
                            context.getSourceActions());
      // The drag origin in native window coordinates.
      Point p = context.getTrigger().getDragOrigin();
      Point cs = comp.getLocationOnScreen();
      Point ws = w.getLocationOnScreen();
      Insets in = wp.insets();
      int x = p.x + cs.x - ws.x - in.left;
      int y = p.y + cs.y - ws.y - in.top;
      int[] dims = new int[2];
      int[] argb = image != null ? HaikuTransferable.pixels(image, dims)
                                 : null;
      int ox = offset != null ? -offset.x : 0;
      int oy = offset != null ? -offset.y : 0;
      HaikuNative.dragStart(wp.handle, msg, x - 16, y - 8, 32, 16, argb,
                            dims[0], dims[1], ox, oy);
    }

    public Cursor getCursor()
    {
      return cursor;
    }

    public void setCursor(Cursor c)
    {
      cursor = c;
    }

    public void transferablesFlavorsChanged()
    {
      // The data was put in the drag message when the drag started.
    }
  }

  /**
   * Called by the event pump when a mouse button is released. If a drag
   * of this VM did not end on a Java drop target, it ended elsewhere:
   * tell the source after a short time.
   */
  static void mouseReleased()
  {
    final int id;
    synchronized (HaikuDragDrop.class)
      {
        if (dragFinished)
          return;
        id = dragId;
      }
    Thread t = new Thread("Haiku drag end")
      {
        public void run()
        {
          // Give a drop on a Java window time to arrive. Thread.sleep()
          // can return early on Kaffe, so check the clock.
          long end = System.currentTimeMillis() + 600;
          long now;
          while ((now = System.currentTimeMillis()) < end)
            {
              try
                {
                  Thread.sleep(end - now);
                }
              catch (InterruptedException e)
                {
                  break;
                }
            }
          DragSourceContext ctx;
          synchronized (HaikuDragDrop.class)
            {
              ctx = dragContext;
            }
          int actions = ctx != null ? ctx.getSourceActions() : 0;
          finishDrag(id, true, (actions & DnDConstants.ACTION_COPY) != 0
                               ? DnDConstants.ACTION_COPY : actions);
        }
      };
    t.setDaemon(true);
    t.start();
  }

  /** Tells the drag source how its drag ended (once). */
  static void finishDrag(int id, boolean success, int action)
  {
    final DragSourceContext ctx;
    synchronized (HaikuDragDrop.class)
      {
        if (dragFinished || id != dragId)
          return;
        dragFinished = true;
        ctx = dragContext;
        dragContext = null;
      }
    if (ctx == null)
      return;
    final DragSourceDropEvent e = success
      ? new DragSourceDropEvent(ctx, action, true)
      : new DragSourceDropEvent(ctx);
    EventQueue.invokeLater(new Runnable()
      {
        public void run()
        {
          ctx.dragDropEnd(e);
        }
      });
  }

  // Drop target.

  /** The drag source context if src holds a drag of this VM. */
  private static DragSourceContext localContext(int src, int[] idOut)
  {
    if (HaikuNative.msgInt(src, "be:jdnd_vm", 0) != VM_TOKEN)
      return null;
    int id = HaikuNative.msgInt(src, "be:jdnd", -1);
    synchronized (HaikuDragDrop.class)
      {
        if (id != dragId || dragFinished)
          return null;
        idOut[0] = id;
        return dragContext;
      }
  }

  /** The deepest active drop target at window point (x, y). */
  private static Component targetAt(Window w, int x, int y, Point out)
  {
    Component c = w.findComponentAt(x, y);
    while (c != null)
      {
        DropTarget dt = c.getDropTarget();
        if (dt != null && dt.isActive())
          break;
        c = c.getParent();
      }
    if (c == null)
      return null;
    int tx = x, ty = y;
    for (Component p = c; p != null && p != w; p = p.getParent())
      {
        tx -= p.getX();
        ty -= p.getY();
      }
    out.x = tx;
    out.y = ty;
    return c;
  }

  /** The user's drop action: Ctrl copies, else a move if allowed. */
  private static int userAction(int mods, int sourceActions)
  {
    int a;
    if ((mods & B_CONTROL_KEY) != 0 && (mods & B_SHIFT_KEY) != 0)
      a = DnDConstants.ACTION_LINK;
    else if ((mods & B_CONTROL_KEY) != 0)
      a = DnDConstants.ACTION_COPY;
    else if ((sourceActions & DnDConstants.ACTION_MOVE) != 0)
      a = DnDConstants.ACTION_MOVE;
    else
      a = DnDConstants.ACTION_COPY;
    if ((a & sourceActions) == 0)
      {
        if ((sourceActions & DnDConstants.ACTION_COPY) != 0)
          a = DnDConstants.ACTION_COPY;
        else if ((sourceActions & DnDConstants.ACTION_MOVE) != 0)
          a = DnDConstants.ACTION_MOVE;
        else
          a = sourceActions & DnDConstants.ACTION_LINK;
      }
    return a;
  }

  /**
   * Called by the event pump for a drag over a window: x, y are window
   * coordinates.
   */
  static void drag(Window w, int x, int y, int transit, int mods)
  {
    if (transit == HaikuNative.DRAG_EXIT)
      {
        exitTarget();
        return;
      }
    Point p = new Point();
    Component c = targetAt(w, x, y, p);
    if (c != targetComponent)
      {
        exitTarget();
        if (c == null)
          return;
        int[] id = new int[1];
        DragSourceContext local = localContext(HaikuNative.SRC_DRAG, id);
        int actions = local != null ? local.getSourceActions()
                                    : DnDConstants.ACTION_COPY;
        Transferable t = local != null ? local.getTransferable()
                         : HaikuTransferable.read(HaikuNative.SRC_DRAG);
        targetComponent = c;
        target = c.getDropTarget();
        targetPeer = new DropContextPeer(target, t, local != null, id[0],
                                         actions);
        final DropTargetContext dtc = target.getDropTargetContext();
        dtc.addNotify(targetPeer);
        final DropTarget dt = target;
        final DropTargetDragEvent e =
          dragEvent(dtc, p, userAction(mods, actions), actions);
        EventQueue.invokeLater(new Runnable()
          {
            public void run()
            {
              dt.dragEnter(e);
            }
          });
        return;
      }
    if (c == null)
      return;
    final DropTarget dt = target;
    final DropTargetDragEvent e =
      dragEvent(dt.getDropTargetContext(), p,
                userAction(mods, targetPeer.sourceActions),
                targetPeer.sourceActions);
    EventQueue.invokeLater(new Runnable()
      {
        public void run()
        {
          dt.dragOver(e);
        }
      });
  }

  private static void exitTarget()
  {
    if (target == null)
      return;
    final DropTarget dt = target;
    final DropTargetEvent e = new DropTargetEvent(dt.getDropTargetContext());
    target = null;
    targetComponent = null;
    targetPeer = null;
    EventQueue.invokeLater(new Runnable()
      {
        public void run()
        {
          dt.dragExit(e);
        }
      });
  }

  /** Called by the event pump for a drop on a window. */
  static void drop(Window w, int x, int y, int mods)
  {
    int[] id = new int[1];
    DragSourceContext local = localContext(HaikuNative.SRC_DROP, id);
    Point p = new Point();
    Component c = targetAt(w, x, y, p);
    target = null;
    targetComponent = null;
    targetPeer = null;
    if (c == null)
      {
        if (local != null)
          finishDrag(id[0], false, DnDConstants.ACTION_NONE);
        return;
      }
    int actions = local != null ? local.getSourceActions()
                                : DnDConstants.ACTION_COPY;
    Transferable t = local != null ? local.getTransferable()
                     : HaikuTransferable.read(HaikuNative.SRC_DROP);
    final DropTarget dt = c.getDropTarget();
    final DropContextPeer peer = new DropContextPeer(dt, t, local != null,
                                                     id[0], actions);
    final DropTargetContext dtc = dt.getDropTargetContext();
    dtc.addNotify(peer);
    final DropTargetDropEvent e =
      dropEvent(dtc, p, userAction(mods, actions), actions, local != null);
    EventQueue.invokeLater(new Runnable()
      {
        public void run()
        {
          dt.drop(e);
          peer.dropHandled();
        }
      });
  }

  /**
   * GNU Classpath 0.98's DropTargetDragEvent and DropTargetDropEvent test
   * the source actions wrongly ("~(actions ^ mask) != 0"); only ~mask
   * passes. The Classpath patch fixes the test. Without it, the events
   * are made with ~mask, and the real value is put into the field.
   */
  private static final int BAD_MASK =
    ~(DnDConstants.ACTION_COPY_OR_MOVE | DnDConstants.ACTION_LINK);

  private static void setActions(Object event, String field, int value)
  {
    try
      {
        java.lang.reflect.Field f = event.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.setInt(event, value);
      }
    catch (Exception e)
      {
        // The event keeps the odd value; the drop still works.
      }
  }

  private static DropTargetDragEvent dragEvent(DropTargetContext dtc,
                                               Point p, int action,
                                               int sourceActions)
  {
    try
      {
        return new DropTargetDragEvent(dtc, p, action, sourceActions);
      }
    catch (IllegalArgumentException e)
      {
        DropTargetDragEvent ev =
          new DropTargetDragEvent(dtc, p, action, BAD_MASK);
        setActions(ev, "srcActions", sourceActions);
        return ev;
      }
  }

  private static DropTargetDropEvent dropEvent(DropTargetContext dtc,
                                               Point p, int action,
                                               int sourceActions,
                                               boolean local)
  {
    try
      {
        return new DropTargetDropEvent(dtc, p, action, sourceActions, local);
      }
    catch (IllegalArgumentException e)
      {
        DropTargetDropEvent ev =
          new DropTargetDropEvent(dtc, p, action, BAD_MASK, local);
        setActions(ev, "actions", sourceActions);
        return ev;
      }
  }

  /** The drop target side of one drag or drop. */
  private static final class DropContextPeer
    implements DropTargetContextPeer
  {
    private final DropTarget dropTarget;
    private final Transferable transferable;
    private final boolean local;
    private final int dragId;
    final int sourceActions;
    private int targetActions = DnDConstants.ACTION_COPY_OR_MOVE;
    private int acceptedAction = DnDConstants.ACTION_NONE;
    private boolean completed;

    DropContextPeer(DropTarget dt, Transferable t, boolean local, int id,
                    int sourceActions)
    {
      dropTarget = dt;
      targetActions = dt.getDefaultActions();
      transferable = t;
      this.local = local;
      dragId = id;
      this.sourceActions = sourceActions;
    }

    public void setTargetActions(int actions)
    {
      targetActions = actions;
    }

    public int getTargetActions()
    {
      return targetActions;
    }

    public DropTarget getDropTarget()
    {
      return dropTarget;
    }

    public DataFlavor[] getTransferDataFlavors()
    {
      return transferable != null ? transferable.getTransferDataFlavors()
                                  : new DataFlavor[0];
    }

    public Transferable getTransferable()
    {
      if (transferable == null)
        throw new InvalidDnDOperationException("no data");
      return transferable;
    }

    public boolean isTransferableJVMLocal()
    {
      return local;
    }

    public void acceptDrag(int action)
    {
      // The drag picture does not change.
    }

    public void rejectDrag()
    {
    }

    public void acceptDrop(int action)
    {
      acceptedAction = action;
    }

    public void rejectDrop()
    {
      acceptedAction = DnDConstants.ACTION_NONE;
    }

    public void dropComplete(boolean success)
    {
      completed = true;
      if (local)
        finishDrag(dragId, success, success ? acceptedAction
                                            : DnDConstants.ACTION_NONE);
    }

    /** After drop(): a listener that never called dropComplete. */
    void dropHandled()
    {
      if (! completed && local)
        finishDrag(dragId, acceptedAction != DnDConstants.ACTION_NONE,
                   acceptedAction);
    }
  }
}
