/* Demo.java -- And example of copy/paste datatransfer
   Copyright (C) 2005 Free Software Foundation, Inc.

This file is part of GNU Classpath examples.

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
02110-1301 USA. */

package gnu.classpath.examples.datatransfer;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

class Demo
  extends Frame
  implements ActionListener
{
  public static void main(String args[])
  {
    new Demo();
  }

  private TextArea text;
  private Button copyText;
  private Button pasteText;

  private ImageComponent image;
  private Button copyImage;
  private Button pasteImage;

  private ObjectComponent object;
  private Button copyObject;
  private Button pasteObject;

  private FilesComponent files;
  private Button copyFiles;
  private Button pasteFiles;

  private Demo()
  {
    super("GNU Classpath datatransfer");
    setLayout(new GridLayout(4, 1, 5, 5));
    add(createTextPanel());
    add(createImagePanel());
    add(createObjectPanel());
    add(createFilesPanel());
    addWindowListener(new WindowAdapter ()
      {
	public void windowClosing (WindowEvent e)
	{
	  dispose();
	}
      });
    pack();
    show();
  }

  private Panel createTextPanel()
  {
    Panel textPanel = new Panel();
    textPanel.setLayout(new BorderLayout());
    text = new TextArea("GNU Everywhere!",
			4, 40,
			TextArea.SCROLLBARS_VERTICAL_ONLY);
    text.setEditable(false);
    text.setEnabled(true);
    Panel textButtons = new Panel();
    textButtons.setLayout(new FlowLayout());
    copyText = new Button("Copy text");
    copyText.addActionListener(this);
    pasteText = new Button("Paste text");
    pasteText.addActionListener(this);
    textButtons.add(copyText);
    textButtons.add(pasteText);
    textPanel.add(text, BorderLayout.CENTER);
    textPanel.add(textButtons, BorderLayout.SOUTH);
    return textPanel;
  }

  private Panel createImagePanel()
  {
    Panel imagePanel = new Panel();
    imagePanel.setLayout(new BorderLayout());
    URL imageurl = this.getClass()
      .getResource("/gnu/classpath/examples/icons/big-fullscreen.png");
    Image img = Toolkit.getDefaultToolkit().createImage(imageurl);
    image = new ImageComponent(img);
    Panel imageButtons = new Panel();
    copyImage = new Button("Copy image");
    copyImage.addActionListener(this);
    pasteImage = new Button("Paste image");
    pasteImage.addActionListener(this);
    imageButtons.add(copyImage);
    imageButtons.add(pasteImage);
    imagePanel.add(image, BorderLayout.CENTER);
    imagePanel.add(imageButtons, BorderLayout.SOUTH);
    return imagePanel;
  }

  private Panel createObjectPanel()
  {
    Panel objectPanel = new Panel();
    objectPanel.setLayout(new BorderLayout());
    Random random = new Random();
    int x = (byte) random.nextInt();
    int y = (byte) random.nextInt();
    object = new ObjectComponent(new Point(x, y));
    Panel objectButtons = new Panel();
    copyObject = new Button("Copy object");
    copyObject.addActionListener(this);
    pasteObject = new Button("Paste object");
    pasteObject.addActionListener(this);
    objectButtons.add(copyObject);
    objectButtons.add(pasteObject);
    objectPanel.add(object, BorderLayout.CENTER);
    objectPanel.add(objectButtons, BorderLayout.SOUTH);
    return objectPanel;
  }
  
  private Panel createFilesPanel()
  {
    Panel filesPanel = new Panel();
    filesPanel.setLayout(new BorderLayout());
    files = new FilesComponent(new File(".").listFiles());
    Panel filesButtons = new Panel();
    copyFiles = new Button("Copy files");
    copyFiles.addActionListener(this);
    pasteFiles = new Button("Paste files");
    pasteFiles.addActionListener(this);
    filesButtons.add(copyFiles);
    filesButtons.add(pasteFiles);
    filesPanel.add(files, BorderLayout.CENTER);
    filesPanel.add(filesButtons, BorderLayout.SOUTH);
    return filesPanel;
  }
  
  public void actionPerformed (ActionEvent evt)
  {
    Button b = (Button) evt.getSource();
    Toolkit t = Toolkit.getDefaultToolkit();
    Clipboard c = t.getSystemClipboard();
    if (b == copyText)
      c.setContents(new StringSelection(text.getText()), null);

    if (b == pasteText)
      {
	String s = null;
	try
	  {
	    s = (String) c.getData(DataFlavor.stringFlavor);
	  }
	catch (UnsupportedFlavorException dfnse)
	  {
	  }
	catch (IOException ioe)
	  {
	  }
	catch (ClassCastException cce)
	  {
	  }
	if (s == null)
	  t.beep();
	else
	  text.setText(s);
      }

    if (b == copyImage)
      c.setContents(new ImageSelection(image.getImage()), null);

    if (b == pasteImage)
      {
	Image i = null;
	try
	  {
	    i = (Image) c.getData(DataFlavor.imageFlavor);
	  }
	catch (UnsupportedFlavorException dfnse)
	  {
	  }
	catch (IOException ioe)
	  {
	  }
	catch (ClassCastException cce)
	  {
	  }
	if (i == null)
	  t.beep();
	else
	  image.setImage(i);
      }

    if (b == copyObject)
      c.setContents(new ObjectSelection(object.getObject()), null);

    if (b == pasteObject)
      {
	Serializable o = null;
	try
	  {
	    o = (Serializable) c.getData
	      (new DataFlavor(DataFlavor.javaSerializedObjectMimeType));
	  }
	catch (UnsupportedFlavorException dfnse)
	  {
	  }
	catch (IOException ioe)
	  {
	  }
	catch (ClassCastException cce)
	  {
	  }
	catch (ClassNotFoundException cnfe)
	  {
	  }
	if (o == null)
	  t.beep();
	else
	  object.setObject(o);
      }

    if (b == copyFiles)
      c.setContents(new FilesSelection(files.getFiles()), null);

    if (b == pasteFiles)
      {
	java.util.List fs = null;
	try
	  {
	    fs = (java.util.List) c.getData(DataFlavor.javaFileListFlavor);
	  }
	catch (UnsupportedFlavorException dfnse)
	  {
	  }
	catch (IOException ioe)
	  {
	  }
	catch (ClassCastException cce)
	  {
	  }
	if (fs == null)
	  t.beep();
	else
	  files.setFiles(fs);
      }
  }

  static class ImageComponent extends Component
  {
    private Image image;

    ImageComponent(Image image)
    {
      setSize(120, 120);
      setImage(image);
    }

    Image getImage()
    {
      return image;
    }

    void setImage(Image image)
    {
      this.image = image;
      repaint();
    }

    public void paint(Graphics g)
    {
      g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
  }

  static class ObjectComponent extends TextArea
  {
    private Serializable object;

    ObjectComponent(Serializable object)
    {
      super("", 2, 40, TextArea.SCROLLBARS_NONE);
      setEditable(false);
      setEnabled(false);
      setObject(object);
    }

    Serializable getObject()
    {
      return object;
    }

    void setObject(Serializable object)
    {
      this.object = object;
      setText("Class: " + object.getClass().getName()
	      + "\n"
	      + "toString(): " + object.toString());
      repaint();
    }
  }

  static class FilesComponent extends List
  {
    private File[] files;

    FilesComponent(File[] files)
    {
      super(4, true);
      setFiles(files);
    }

    File[] getFiles()
    {
      String[] strings = getSelectedItems();
      if (strings == null || strings.length == 0)
	return (File[]) files.clone();

      File[] fs = new File[strings.length];
      for (int i = 0; i < strings.length; i++)
	fs[i] = new File(strings[i]);
      return fs;
    }

    void setFiles(File[] files)
    {
      this.files = files;
      removeAll();
      for (int i = 0; i < files.length; i++)
	{
	  addItem(files[i].toString());
	  select(i);
	}
    }

    void setFiles(java.util.List list)
    {
      File[] fs = new File[list.size()];
      int i = 0;
      Iterator it = list.iterator();
      while (it.hasNext())
	fs[i++] = (File) it.next();

      setFiles(fs);
    }
  }
    
  static class ImageSelection implements Transferable
  {
    private final Image img;

    ImageSelection(Image img)
    {
      this.img = img;
    }

    static DataFlavor[] flavors = new DataFlavor[] { DataFlavor.imageFlavor };
    public DataFlavor[] getTransferDataFlavors()
    {
      return (DataFlavor[]) flavors.clone();
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
      return flavor.equals(DataFlavor.imageFlavor);
    }

    public Object getTransferData(DataFlavor flavor)
      throws UnsupportedFlavorException
    {
      if (!isDataFlavorSupported(flavor))
	throw new UnsupportedFlavorException(flavor);

      return img;
    }
  }

  static class ObjectSelection implements Transferable
  {
    private final Serializable obj;

    ObjectSelection(Serializable obj)
    {
      this.obj = obj;
    }

    static DataFlavor objFlavor = new DataFlavor(Serializable.class,
						 "Serialized Object");
    static DataFlavor[] flavors = new DataFlavor[] { objFlavor };
    public DataFlavor[] getTransferDataFlavors()
    {
      return (DataFlavor[]) flavors.clone();
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
      return flavor.equals(objFlavor);
    }

    public Object getTransferData(DataFlavor flavor)
      throws UnsupportedFlavorException
    {
      if (!isDataFlavorSupported(flavor))
	throw new UnsupportedFlavorException(flavor);

      return obj;
    }
  }

  static class FilesSelection implements Transferable
  {
    private final File[] files;

    FilesSelection(File[] files)
    {
      this.files = files;
    }

    static DataFlavor[] flavors = new DataFlavor[]
      { DataFlavor.javaFileListFlavor };
    public DataFlavor[] getTransferDataFlavors()
    {
      return (DataFlavor[]) flavors.clone();
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
      return flavor.equals(DataFlavor.javaFileListFlavor);
    }

    public Object getTransferData(DataFlavor flavor)
      throws UnsupportedFlavorException
    {
      if (!isDataFlavorSupported(flavor))
	throw new UnsupportedFlavorException(flavor);

      return Arrays.asList(files);
    }
  }
}
