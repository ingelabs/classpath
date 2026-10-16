/* HaikuToolkit.java -- part of the Haiku AWT peer
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

import gnu.classpath.SystemProperties;
import gnu.java.awt.ClasspathToolkit;
import gnu.java.awt.EmbeddedWindow;
import gnu.java.awt.font.OpenTypeFontPeer;
import gnu.java.awt.image.ImageConverter;
import gnu.java.awt.peer.ClasspathFontPeer;
import gnu.java.awt.peer.EmbeddedWindowPeer;
import gnu.java.awt.peer.swing.SwingCanvasPeer;
import gnu.java.awt.peer.swing.SwingTextAreaPeer;

import java.awt.AWTException;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxMenuItem;
import java.awt.Choice;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.PrintJob;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Window;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import java.awt.datatransfer.Clipboard;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.im.InputMethodHighlight;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.peer.ButtonPeer;
import java.awt.peer.CanvasPeer;
import java.awt.peer.CheckboxMenuItemPeer;
import java.awt.peer.CheckboxPeer;
import java.awt.peer.ChoicePeer;
import java.awt.peer.DialogPeer;
import java.awt.peer.FileDialogPeer;
import java.awt.peer.FontPeer;
import java.awt.peer.FramePeer;
import java.awt.peer.LabelPeer;
import java.awt.peer.ListPeer;
import java.awt.peer.MenuBarPeer;
import java.awt.peer.MenuItemPeer;
import java.awt.peer.MenuPeer;
import java.awt.peer.PanelPeer;
import java.awt.peer.PopupMenuPeer;
import java.awt.peer.RobotPeer;
import java.awt.peer.ScrollPanePeer;
import java.awt.peer.ScrollbarPeer;
import java.awt.peer.TextAreaPeer;
import java.awt.peer.TextFieldPeer;
import java.awt.peer.WindowPeer;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

/**
 * AWT toolkit for Haiku.
 *
 * Top-level windows are native BWindows (see {@link HaikuWindowPeer});
 * widgets inside them use the Swing based peers from
 * gnu.java.awt.peer.swing. All drawing is done in Java.
 *
 * Select it with -Dawt.toolkit=gnu.java.awt.peer.haiku.HaikuToolkit.
 */
public class HaikuToolkit
  extends ClasspathToolkit
{

  private EventQueue eventQueue;

  private ColorModel colorModel;

  private Clipboard clipboard;

  private final HashMap<URL,Image> imageCache = new HashMap<URL,Image>();

  private final HashMap<String,ClasspathFontPeer> fontCache =
    new HashMap<String,ClasspathFontPeer>();

  private static final boolean DEBUG =
    Boolean.getBoolean("gnu.java.awt.peer.haiku.debug");

  public HaikuToolkit()
  {
    SystemProperties.setProperty("java.awt.graphicsenv",
                                 "gnu.java.awt.peer.haiku.HaikuGraphicsEnvironment");
    // PrinterJob.getPrinterJob() uses this class, as in the JDK.
    if (SystemProperties.getProperty("java.awt.printerjob") == null)
      SystemProperties.setProperty("java.awt.printerjob",
                                   "gnu.java.awt.peer.haiku.HaikuPrinterJob");
    HaikuNative.ensureInitialized();
  }

  public GraphicsEnvironment getLocalGraphicsEnvironment()
  {
    return new HaikuGraphicsEnvironment();
  }

  private static HaikuGraphicsConfiguration getConfig()
  {
    return (HaikuGraphicsConfiguration)
      GraphicsEnvironment.getLocalGraphicsEnvironment()
      .getDefaultScreenDevice().getDefaultConfiguration();
  }

  public ClasspathFontPeer getClasspathFontPeer(String name, Map attrs)
  {
    String canonical = OpenTypeFontPeer.encodeFont(name, attrs);
    synchronized (fontCache)
      {
        ClasspathFontPeer font = fontCache.get(canonical);
        if (font == null)
          {
            font = new HaikuFontPeer(name, attrs);
            fontCache.put(canonical, font);
          }
        return font;
      }
  }

  public Font createFont(int format, InputStream stream)
  {
    return null;
  }

  public RobotPeer createRobot(GraphicsDevice screen) throws AWTException
  {
    throw new AWTException("Robot is not supported on Haiku yet");
  }

  public EmbeddedWindowPeer createEmbeddedWindow(EmbeddedWindow w)
  {
    throw new UnsupportedOperationException("Not yet implemented.");
  }

  // Top-level windows.

  protected FramePeer createFrame(Frame target)
  {
    return new HaikuFramePeer(target);
  }

  protected WindowPeer createWindow(Window target)
  {
    return new HaikuWindowPeer(target);
  }

  protected DialogPeer createDialog(Dialog target)
  {
    return new HaikuDialogPeer(target);
  }

  // Widgets, implemented with Swing.

  protected ButtonPeer createButton(Button target)
  {
    return new HaikuButtonPeer(target);
  }

  protected TextFieldPeer createTextField(TextField target)
  {
    return new HaikuTextFieldPeer(target);
  }

  protected LabelPeer createLabel(Label target)
  {
    return new HaikuLabelPeer(target);
  }

  protected ListPeer createList(List target)
  {
    return new HaikuListPeer(target);
  }

  protected CheckboxPeer createCheckbox(Checkbox target)
  {
    return new HaikuCheckboxPeer(target);
  }

  protected TextAreaPeer createTextArea(TextArea target)
  {
    return new SwingTextAreaPeer(target);
  }

  protected CanvasPeer createCanvas(Canvas target)
  {
    return new SwingCanvasPeer(target);
  }

  protected PanelPeer createPanel(Panel target)
  {
    return new HaikuPanelPeer(target);
  }

  protected ScrollbarPeer createScrollbar(Scrollbar target)
  {
    return new HaikuScrollbarPeer(target);
  }

  protected ScrollPanePeer createScrollPane(ScrollPane target)
  {
    return new HaikuScrollPanePeer(target);
  }

  protected ChoicePeer createChoice(Choice target)
  {
    return new HaikuChoicePeer(target);
  }

  // Menus and file dialogs are native.

  protected MenuBarPeer createMenuBar(MenuBar target)
  {
    return new HaikuMenuBarPeer(target);
  }

  protected MenuPeer createMenu(Menu target)
  {
    return new HaikuMenuPeer(target);
  }

  protected PopupMenuPeer createPopupMenu(PopupMenu target)
  {
    return new HaikuPopupMenuPeer(target);
  }

  protected MenuItemPeer createMenuItem(MenuItem target)
  {
    return new HaikuMenuItemPeer(target);
  }

  protected FileDialogPeer createFileDialog(FileDialog target)
  {
    return new HaikuFileDialogPeer(target);
  }

  protected CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem target)
  {
    return new HaikuCheckboxMenuItemPeer(target);
  }

  protected FontPeer getFontPeer(String name, int style)
  {
    HashMap<Object,Object> attrs = new HashMap<Object,Object>();
    ClasspathFontPeer.copyStyleToAttrs(style, attrs);
    return getClasspathFontPeer(name, attrs);
  }

  // Screen.

  public Dimension getScreenSize()
  {
    java.awt.Rectangle r = getConfig().getBounds();
    return new Dimension(r.width, r.height);
  }

  public int getScreenResolution()
  {
    return getConfig().getResolution();
  }

  public ColorModel getColorModel()
  {
    if (colorModel == null)
      colorModel = new DirectColorModel(24, 0xFF0000, 0xFF00, 0xFF);
    return colorModel;
  }

  // Fonts.

  public String[] getFontList()
  {
    return GraphicsEnvironment.getLocalGraphicsEnvironment()
           .getAvailableFontFamilyNames();
  }

  public FontMetrics getFontMetrics(Font font)
  {
    ClasspathFontPeer peer = (ClasspathFontPeer) font.getPeer();
    return peer.getFontMetrics(font);
  }

  public void sync()
  {
    // Blits are synchronous.
  }

  // Images. Decoding is done in Java with javax.imageio.

  public Image getImage(String name)
  {
    try
      {
        return getImage(new File(name).toURL());
      }
    catch (MalformedURLException ex)
      {
        return createErrorImage();
      }
  }

  public Image getImage(URL url)
  {
    synchronized (imageCache)
      {
        Image image = imageCache.get(url);
        if (image == null)
          {
            image = createImage(url);
            imageCache.put(url, image);
          }
        return image;
      }
  }

  public Image createImage(String filename)
  {
    try
      {
        return createImage(new File(filename).toURL());
      }
    catch (MalformedURLException ex)
      {
        return createErrorImage();
      }
  }

  public Image createImage(URL url)
  {
    try
      {
        InputStream in = url.openStream();
        try
          {
            return createImage(in);
          }
        finally
          {
            in.close();
          }
      }
    catch (IOException ex)
      {
        return createErrorImage();
      }
  }

  public Image createImage(byte[] data, int offset, int len)
  {
    try
      {
        return createImage(new ByteArrayInputStream(data, offset, len));
      }
    catch (IOException ex)
      {
        return createErrorImage();
      }
  }

  private Image createImage(InputStream in)
    throws IOException
  {
    if (DEBUG)
      System.err.println("haiku: decoding image");
    BufferedImage img = ImageIO.read(in);
    if (DEBUG)
      System.err.println("haiku: decoded " + img);
    return img != null ? toDirect(img) : createErrorImage();
  }

  /**
   * Converts an image to INT_RGB or INT_ARGB. HaikuGraphics2D draws those
   * directly; other types (such as the indexed images of GIF files) take
   * a slow generic path that does not draw them correctly.
   */
  static BufferedImage toDirect(BufferedImage img)
  {
    int type = img.getType();
    if (type == BufferedImage.TYPE_INT_RGB
        || type == BufferedImage.TYPE_INT_ARGB)
      return img;
    int w = img.getWidth();
    int h = img.getHeight();
    boolean alpha = img.getColorModel().hasAlpha();
    BufferedImage out =
      new BufferedImage(w, h, alpha ? BufferedImage.TYPE_INT_ARGB
                                    : BufferedImage.TYPE_INT_RGB);
    int[] dst = ((java.awt.image.DataBufferInt) out.getRaster()
                 .getDataBuffer()).getData();
    if (img.getColorModel() instanceof java.awt.image.IndexColorModel
        && img.getRaster().getNumBands() == 1)
      {
        // Map the palette indices; much faster than getRGB() per pixel.
        java.awt.image.IndexColorModel icm =
          (java.awt.image.IndexColorModel) img.getColorModel();
        int[] palette = new int[icm.getMapSize()];
        icm.getRGBs(palette);
        java.awt.image.Raster r = img.getRaster();
        int[] idx = new int[w];
        for (int y = 0; y < h; y++)
          {
            r.getSamples(r.getMinX(), r.getMinY() + y, w, 1, 0, idx);
            int off = y * w;
            for (int x = 0; x < w; x++)
              {
                int i = idx[x];
                dst[off + x] = i < palette.length ? palette[i] : 0;
              }
          }
        return out;
      }
    for (int y = 0; y < h; y++)
      img.getRGB(0, y, w, 1, dst, y * w, w);
    return out;
  }

  private static Image createErrorImage()
  {
    return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
  }

  public Image createImage(ImageProducer producer)
  {
    ImageConverter conv = new ImageConverter();
    producer.startProduction(conv);
    Image img = conv.getImage();
    if (img instanceof BufferedImage)
      img = toDirect((BufferedImage) img);
    return img;
  }

  public boolean prepareImage(Image image, int width, int height,
                              ImageObserver observer)
  {
    // Images are loaded synchronously.
    return true;
  }

  public int checkImage(Image image, int width, int height,
                        ImageObserver observer)
  {
    return ImageObserver.ALLBITS | ImageObserver.WIDTH
           | ImageObserver.HEIGHT;
  }

  // Misc.

  /** Shows Haiku's page setup and print dialogs; null if cancelled. */
  public PrintJob getPrintJob(Frame frame, String title, Properties props)
  {
    SecurityManager sm = System.getSecurityManager();
    if (sm != null)
      sm.checkPrintJobAccess();
    return HaikuPrintJob.create(title);
  }

  public void beep()
  {
    HaikuNative.beep();
  }

  /** The system clipboard, shared with other Haiku applications. */
  public synchronized Clipboard getSystemClipboard()
  {
    if (clipboard == null)
      {
        HaikuNative.ensureInitialized();
        clipboard = new HaikuClipboard();
      }
    return clipboard;
  }

  public Cursor createCustomCursor(Image cursor, Point hotSpot, String name)
  {
    HaikuNative.ensureInitialized();
    return new HaikuCursor(cursor, hotSpot, name);
  }

  /** Haiku cursors can have any size; keep them reasonable. */
  public Dimension getBestCursorSize(int w, int h)
  {
    return new Dimension(Math.max(1, Math.min(w, 64)),
                         Math.max(1, Math.min(h, 64)));
  }

  public int getMaximumCursorColors()
  {
    return 1 << 24;
  }

  /** Haiku windows can be minimized and zoomed. */
  public boolean isFrameStateSupported(int state)
  {
    return state == Frame.NORMAL || state == Frame.ICONIFIED
           || state == Frame.MAXIMIZED_BOTH
           || state == (Frame.ICONIFIED | Frame.MAXIMIZED_BOTH);
  }

  protected synchronized EventQueue getSystemEventQueueImpl()
  {
    if (eventQueue == null)
      eventQueue = new EventQueue();
    return eventQueue;
  }

  public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent e)
  {
    return HaikuDragDrop.createDragSourceContextPeer();
  }

  /** Mouse drags start drag gestures (GNU Classpath's generic recognizer). */
  public <T extends java.awt.dnd.DragGestureRecognizer> T
    createDragGestureRecognizer(Class<T> recognizer,
                                java.awt.dnd.DragSource ds,
                                java.awt.Component comp, int actions,
                                java.awt.dnd.DragGestureListener l)
  {
    if (! recognizer.isAssignableFrom(
            gnu.java.awt.dnd.GtkMouseDragGestureRecognizer.class))
      return null;
    gnu.java.awt.dnd.GtkMouseDragGestureRecognizer r =
      new gnu.java.awt.dnd.GtkMouseDragGestureRecognizer(ds, comp, actions, l);
    // GNU Classpath's DragGestureRecognizer constructor does not do this.
    if (comp != null)
      r.registerListeners();
    return recognizer.cast(r);
  }

  /**
   * Composed text: dotted underline for raw text, inverse colours for the
   * part being converted, a solid underline for converted text.
   */
  public Map mapInputMethodHighlight(InputMethodHighlight highlight)
  {
    java.util.HashMap<java.awt.font.TextAttribute,Object> m =
      new java.util.HashMap<java.awt.font.TextAttribute,Object>();
    if (highlight.isSelected())
      m.put(java.awt.font.TextAttribute.SWAP_COLORS,
            java.awt.font.TextAttribute.SWAP_COLORS_ON);
    else if (highlight.getState() == InputMethodHighlight.RAW_TEXT)
      m.put(java.awt.font.TextAttribute.INPUT_METHOD_UNDERLINE,
            java.awt.font.TextAttribute.UNDERLINE_LOW_DOTTED);
    else
      m.put(java.awt.font.TextAttribute.INPUT_METHOD_UNDERLINE,
            java.awt.font.TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
    return m;
  }

  public boolean isModalExclusionTypeSupported(ModalExclusionType type)
  {
    return false;
  }

  /** Modal dialogs block the whole application. */
  public boolean isModalityTypeSupported(ModalityType type)
  {
    return type == ModalityType.MODELESS
           || type == ModalityType.APPLICATION_MODAL;
  }
}
