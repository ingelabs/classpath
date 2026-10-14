/* HaikuNative.java -- part of the Haiku AWT peer
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

/**
 * Native entry points of the Haiku AWT peer (libhaikupeer).
 *
 * The native side runs a BApplication on its own native thread. Native
 * threads never call into Java; Java polls events with
 * {@link #nextEvent(int[], int)} from the {@link HaikuEventPump} thread.
 */
final class HaikuNative
{
  // Event types. Keep in sync with gnu_java_awt_peer_haiku_HaikuNative.cpp.
  static final int EV_CLOSE = 1;
  static final int EV_RESIZED = 2;
  static final int EV_MOVED = 3;
  static final int EV_MOUSE_DOWN = 4;
  static final int EV_MOUSE_UP = 5;
  static final int EV_MOUSE_MOVED = 6;
  static final int EV_MOUSE_ENTER = 7;
  static final int EV_MOUSE_EXIT = 8;
  static final int EV_KEY_DOWN = 9;
  static final int EV_KEY_UP = 10;
  static final int EV_ACTIVATED = 11;
  static final int EV_WHEEL = 12;
  static final int EV_APP_QUIT = 13;
  static final int EV_MENU = 14;
  static final int EV_CHOICE = 15;
  static final int EV_FILE_PANEL = 16;
  static final int EV_STATE = 17;
  static final int EV_EDIT = 18;
  static final int EV_FILTER = 19;
  static final int EV_DRAG = 20;
  static final int EV_DROP = 21;
  static final int EV_IM = 22;

  // Transits of EV_DRAG.
  static final int DRAG_ENTER = 1;
  static final int DRAG_OVER = 2;
  static final int DRAG_EXIT = 3;

  // Message sources for msgTypes() and the other msg readers.
  static final int SRC_CLIPBOARD = 0;
  static final int SRC_DRAG = 1;
  static final int SRC_DROP = 2;

  // Message codes (AppDefs.h).
  static final int B_SIMPLE_DATA = 0x44415441; // 'DATA'
  static final int B_MIME_DATA = 0x4d494d45;   // 'MIME'

  // Input method opcodes (Input.h).
  static final int B_INPUT_METHOD_STARTED = 0;
  static final int B_INPUT_METHOD_STOPPED = 1;
  static final int B_INPUT_METHOD_CHANGED = 2;
  static final int B_INPUT_METHOD_LOCATION_REQUEST = 3;

  // Window feels (Window.h).
  static final int B_NORMAL_WINDOW_FEEL = 0;
  static final int B_MODAL_APP_WINDOW_FEEL = 1;
  static final int B_FLOATING_ALL_WINDOW_FEEL = 6;

  // Commands of EV_EDIT (the Command-X/C/V/A shortcuts of a window).
  static final int EDIT_CUT = 1;
  static final int EDIT_COPY = 2;
  static final int EDIT_PASTE = 3;
  static final int EDIT_SELECT_ALL = 4;

  // Menu kinds for menuCreate().
  static final int MENU_BAR = 0;
  static final int MENU = 1;
  static final int MENU_POPUP = 2;

  // Haiku modifier bits (InterfaceDefs.h).
  static final int B_SHIFT_KEY = 0x1;
  static final int B_COMMAND_KEY = 0x2;

  /** Number of ints in one event record. */
  static final int EV_INTS = 8;

  // Window kinds for createWindow().
  static final int KIND_FRAME = 0;
  static final int KIND_DIALOG = 1;
  static final int KIND_WINDOW = 2;

  private static boolean initialized;

  private HaikuNative()
  {
  }

  /**
   * Loads libhaikupeer and starts the BApplication thread.
   */
  static synchronized void ensureInitialized()
  {
    if (initialized)
      return;
    String explicit = System.getProperty("gnu.java.awt.peer.haiku.library");
    if (explicit != null)
      System.load(explicit);
    else
      System.loadLibrary("haikupeer");
    if (! init())
      throw new java.awt.AWTError("Cannot start the Haiku BApplication");
    initialized = true;
  }

  private static native boolean init();

  /** out = width, height, dpi. */
  static native void screenInfo(int[] out);

  static native long createWindow(int id, int kind, String title,
                                  int x, int y, int w, int h);

  /** out = left, top, right, bottom. */
  static native void getInsets(long window, int[] out);

  static native void setTitle(long window, String title);

  static native void setVisible(long window, boolean visible);

  /** Bounds of the client area in screen coordinates. */
  static native void setBounds(long window, int x, int y, int w, int h);

  static native void setResizable(long window, boolean resizable);

  static native void toFront(long window);

  static native void toBack(long window);

  static native void dispose(long window);

  static native void blit(long window, int[] pixels, int imgW, int imgH,
                          int x, int y, int w, int h);

  static native int nextEvent(int[] out, int timeoutMs);

  static native void beep();

  // Menus. Handles are native BMenu / BMenuItem pointers.

  static native long menuCreate(int kind, String label);

  static native long menuItemCreate(int id, String label, int shortcut,
                                    int modifiers, boolean separator);

  static native void menuAddItem(long menu, long item, int index);

  static native void menuAddSubmenu(long menu, long submenu, int index);

  static native void menuAddSeparator(long menu);

  static native void menuRemoveItem(long menu, int index);

  static native void menuItemSetLabel(long item, String label);

  static native void menuItemSetEnabled(long item, boolean enabled);

  static native void menuItemSetMarked(long item, boolean marked);

  static native void menuSetLabel(long menu, String label);

  static native void menuSetEnabled(long menu, boolean enabled);

  static native void menuItemDispose(long item);

  static native void menuDispose(long menu);

  /** Attaches a menu bar (0 detaches) and returns its height. */
  static native int setMenuBar(long window, long menuBar);

  static native void popupShow(long menu, int x, int y);

  static native void choicePopup(int id, String[] items, int selected,
                                 int x, int y);

  // File panels.

  /** filtered: ask Java through EV_FILTER which files to show. */
  static native void filePanelShow(int id, boolean save, String title,
                                   String dir, String file,
                                   boolean filtered);

  static native String fileResult(int id);

  /** The next directory a file panel wants filtered, or null. */
  static native String filterNext(int id);

  static native void filterAnswer(int id, String dir, String[] names,
                                  boolean[] accepted);

  // Frame state.

  static native void setFrameState(long window, boolean minimized,
                                   boolean zoomed);

  /** out = minimized, zoomed. */
  static native void getFrameState(long window, boolean[] out);

  /** Largest outer size of a zoomed window; 0 for no limit. */
  static native void setZoomLimits(long window, int w, int h);

  // Cursors.

  /** type is a java.awt.Cursor type; custom is 0 or from cursorCreate. */
  static native void setCursor(long window, int type, long custom);

  /** Makes a cursor from ARGB pixels. Returns 0 on failure. */
  static native long cursorCreate(int[] pixels, int w, int h, int hotX,
                                  int hotY);

  static native void setFeel(long window, int feel);

  // Messages for the clipboard and drag and drop.

  static native long msgNew(int what);

  static native void msgDelete(long msg);

  static native void msgAddData(long msg, String mime, byte[] data);

  static native void msgAddInt(long msg, String name, int value);

  /** Adds a picture as an archived bitmap and as PNG. */
  static native void msgAddImage(long msg, int[] argb, int w, int h);

  /** Adds files as entry_refs ("refs"). */
  static native void msgAddFiles(long msg, String[] paths);

  /** Changes each time any application writes to the clipboard. */
  static native long clipboardCount();

  /** Replaces the clipboard with msg (deleted). Returns the new count. */
  static native long clipboardPut(long msg);

  /** Copies the clipboard into SRC_CLIPBOARD. Returns the count. */
  static native long clipboardSnapshot();

  static native int msgGeneration(int src);

  /** MIME types, "refs" and archived bitmap names in a source. */
  static native String[] msgTypes(int src);

  static native byte[] msgData(int src, String mime);

  /** The picture in a source as ARGB pixels; dims = width, height. */
  static native int[] msgImage(int src, int[] dims);

  static native String[] msgFiles(int src);

  static native int msgInt(int src, String name, int def);

  /**
   * Drags msg (deleted) from a window: with the picture (mouse at offX,
   * offY in it) or else the rectangle (x, y, w, h).
   */
  static native void dragStart(long window, long msg, int x, int y, int w,
                               int h, int[] argb, int iw, int ih, int offX,
                               int offY);

  // Printing.

  /** Returns 0 if the user cancelled a dialog. */
  static native long printCreate(String name, boolean pageDialog,
                                 boolean jobDialog);

  /** page: the page setup dialog, else the print dialog. */
  static native boolean printDialog(long job, boolean page);

  /**
   * out = paper l, t, r, b, printable l, t, r, b (points), first page,
   * last page, x dpi, y dpi.
   */
  static native void printInfo(long job, float[] out);

  static native boolean printBegin(long job);

  /** One page of w x h ARGB pixels that fill the printable area. */
  static native boolean printPage(long job, int[] argb, int w, int h);

  static native void printEnd(long job, boolean commit);

  // Input methods.

  static native String imText(int windowId);

  static native void imLocationReply(long window, int[] xs, int[] ys,
                                     float height);

  // Fonts (BFont). Sizes are pixels (points at 72 dpi).

  /** Opens a font ("" = the plain font); 0 if the family is unknown. */
  static native long fontOpen(String family, int face);

  /** which: 0 all families, 1 the plain font's, 2 the fixed font's. */
  static native String[] fontFamilies(int which);

  /** out = ascent, descent, leading, max advance. */
  static native void fontMetrics(long font, float size, float[] out);

  /** The advance of each UTF-16 unit of text. */
  static native void fontAdvances(long font, float size, String text,
                                  float[] out);

  static native float fontWidth(long font, float size, String text);

  static native void fontHasGlyphs(long font, String text, boolean[] out);

  /**
   * Draws text; returns coverage (0..255) per pixel. dims = width,
   * height, baseline y, origin x.
   */
  static native byte[] fontRender(long font, float size, String text,
                                  int[] dims);

  /**
   * The outline of text: 0 x y = move, 1 x y = line,
   * 2 x1 y1 x2 y2 x3 y3 = cubic, 3 = close. xs: x of each unit, or null.
   */
  static native float[] fontOutline(long font, float size, String text,
                                    float[] xs);

  /** Shows composing text near (x, y) on the screen; null hides it. */
  static native void imShowComposition(String text, int x, int y,
                                       int selStart, int selEnd);
}
