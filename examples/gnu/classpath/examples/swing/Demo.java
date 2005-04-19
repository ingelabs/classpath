/* SwingDemo.java -- An example of using the javax.swing UI.
   Copyright (C) 2003, 2004  Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.
*/


package gnu.classpath.examples.swing;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.border.*;

import java.net.URL;
import java.util.*;

public class Demo
{
  JFrame frame;
  static Color blueGray = new Color(0xdc, 0xda, 0xd5);

  static
  {
    try
      {
        if (System.getProperty("swing.defaultlaf") == null)
          {
            StringBuffer text = new StringBuffer();
            text.append("\tYou may change the Look and Feel of this\n");
            text.append("\tDemo by setting the system property\n");
            text.append("\t-Dswing.defaultlaf=<LAFClassName>\n\n");
            text.append("\tPossible values for <LAFClassName> are:\n");
            text.append("\t  * javax.swing.plaf.metal.MetalLookAndFeel\n");
            text.append("\t\tthe default Java L&F\n");
            text.append("\t  * gnu.classpath.examples.swing.GNULookAndFeel\n");
            text.append("\tthe GNU Look and Feel\n");
            text.append("\t(derived from javax.swing.plaf.basic.BasicLookAndFeel\n\n");
            text.append("\tthe default is gnu.classpath.examples.swing.GNULookAndFeel\n");
            JEditorPane textPane = new JEditorPane();
            // temporary hack, preferred size should be computed by the
            // component
            textPane.setPreferredSize(new Dimension(400, 300));
            textPane.setText(text.toString());
            JOptionPane.showMessageDialog(null, textPane,
                                          "Look and Feel notice",
                                          JOptionPane.INFORMATION_MESSAGE);
            
            UIManager.setLookAndFeel(new GNULookAndFeel());
          }
      }
    catch (UnsupportedLookAndFeelException e)
      {
        System.err.println("Cannot install GNULookAndFeel, exiting");
        System.exit(1);
      }
  }

  static Icon stockIcon(String s)
  {
    return getIcon("/gnu/classpath/examples/icons/stock-" + s + ".png", s);
  }

  static Icon bigStockIcon(String s)
  {
    return getIcon("/gnu/classpath/examples/icons/big-" + s + ".png", s);
  }

  static Icon getIcon(String location, String name)
  {
    URL url = Demo.class.getResource(location);
    if (url == null) System.err.println("WARNING " + location + " not found.");
    return new ImageIcon(url, name);
  }

  static JMenuBar mkMenuBar()
  {
    JMenuBar bar = new JMenuBar();
    
    JMenu file = new JMenu("File");
    JMenu edit = new JMenu("Edit");
    JMenu help = new JMenu("Help");

    file.setMnemonic(KeyEvent.VK_F);
    edit.setMnemonic(KeyEvent.VK_E);
    help.setMnemonic(KeyEvent.VK_H);

    file.add(new JMenuItem("New", stockIcon("new")));
    file.add(new JMenuItem("Open", stockIcon("open")));

    JMenu recent = new JMenu("Recent Files...");
    recent.add(new JMenuItem("war-and-peace.txt"));
    recent.add(new JMenuItem("taming-of-shrew.txt"));
    recent.add(new JMenuItem("sun-also-rises.txt"));
    file.add(recent);
    file.add(new JMenuItem("Save", stockIcon("save")));
    file.add(new JMenuItem("Save as...", stockIcon("save-as")));

    JMenuItem exit = new JMenuItem("Exit", stockIcon("quit"));
    exit.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          System.exit(1);
        }
      });

    file.add(exit);

    edit.add(new JMenuItem("Cut", stockIcon("cut")));
    edit.add(new JMenuItem("Copy", stockIcon("copy")));
    edit.add(new JMenuItem("Paste", stockIcon("paste")));

    JMenu preferences = new JMenu("Preferences...");
    preferences.add(new JCheckBoxMenuItem("Microphone Active",
		    stockIcon("mic")));
    preferences.add(new JCheckBoxMenuItem("Check Spelling",
		    stockIcon("spell-check")));
    preferences.add(new JCheckBoxMenuItem("World Peace"));
    edit.add(preferences);

    help.add(new JMenuItem("just play with the widgets"));
    help.add(new JMenuItem("and enjoy the sensation of"));
    help.add(new JMenuItem("your neural connections growing"));

    bar.add(file);
    bar.add(edit);
    bar.add(help);
    return bar;
  }

  static void triggerDialog(final JButton but, final String dir)
  {
    but.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JOptionPane.showConfirmDialog(but, 
                                        "Sure you want to go " + dir + "?",
                                        "Confirm",
                                        JOptionPane.OK_CANCEL_OPTION, 
                                        JOptionPane.QUESTION_MESSAGE,
                                        bigStockIcon("warning"));
        }
      });
  }

  static JToolBar mkToolBar()
  {
    JToolBar bar = new JToolBar();

    JButton b = mkButton(stockIcon("go-back"));
    triggerDialog(b, "back");
    bar.add(b);

    b = mkButton(stockIcon("go-down"));
    triggerDialog(b, "down");
    bar.add(b);

    b = mkButton(stockIcon("go-forward"));
    triggerDialog(b, "forward");
    bar.add(b);
    return bar;
  }

  static String valign2str(int a)
  {
    switch (a)
      {
      case SwingConstants.CENTER:
        return "Center";
      case SwingConstants.TOP:
        return "Top";
      case SwingConstants.BOTTOM:
        return "Bottom";
      default:
        return "Unknown";
      }
  }

  static String halign2str(int a)
  {
    switch (a)
      {
      case SwingConstants.CENTER:
        return "Center";
      case SwingConstants.RIGHT:
        return "Right";
      case SwingConstants.LEFT:
        return "Left";
      default:
        return "Unknown";
      }
  }

  static JButton mkButton(String title, Icon icon, 
                          int hAlign, int vAlign,
                          int hPos, int vPos)
  {    
    JButton b;
    if (icon == null)
      b = new JButton(title);
    else if (title == null)
      b = new JButton(icon);
    else
      b = new JButton(title, icon);
    
    if (hAlign != -1) b.setHorizontalAlignment(hAlign);
    if (vAlign != -1) b.setVerticalAlignment(vAlign);
    if (hPos != -1) b.setHorizontalTextPosition(hPos);
    if (vPos != -1) b.setVerticalTextPosition(vPos);      
    return b;
  }

  static JButton mkButton(String title)
  {
    return mkButton(title, null, -1, -1, -1, -1);
  }

  static JButton mkButton(Icon i)
  {
    return mkButton(null, i, -1, -1, -1, -1);
  }


  static JPanel mkButtonWorld()
  {
    Icon ii = bigStockIcon("home");
    int CENTER = SwingConstants.CENTER;
    int TOP = SwingConstants.TOP;
    int BOTTOM = SwingConstants.BOTTOM;

    int[] valigns = new int[] {SwingConstants.CENTER,
                               SwingConstants.TOP,
                               SwingConstants.BOTTOM};

    int[] haligns = new int[] {SwingConstants.CENTER,
                               SwingConstants.RIGHT,
                               SwingConstants.LEFT};

    Border[] borders = new Border[] { 
      new SoftBevelBorder(BevelBorder.RAISED),
      new SoftBevelBorder(BevelBorder.LOWERED),
      new BevelBorder(BevelBorder.RAISED),
      
      LineBorder.createBlackLineBorder(),
      new MatteBorder(2, 2, 2, 2, Color.GREEN),
      LineBorder.createGrayLineBorder(),
      
      new BevelBorder(BevelBorder.LOWERED),
      new EtchedBorder(EtchedBorder.RAISED),
      new EtchedBorder(EtchedBorder.LOWERED)      
    };

    JComponent[] comps = new JComponent[3*3];

    int q = 0;

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 3));

    for (int i = 0; i < 3; ++i)
      for (int j = 0; j < 3; ++j)
        {
          JButton b = mkButton(halign2str(haligns[i])
                               + valign2str(valigns[j]),
                               ii,
                               -1, -1, haligns[i], valigns[j]);
          b.setBorder(borders[q++]);
          JPanel tmp = new JPanel();
          tmp.setBorder(new MatteBorder(5, 5, 5, 5, blueGray));
          tmp.add(b);
          panel.add(tmp);
        }
    
    return panel;
  }

  private static class CheckCellRenderer 
    extends JCheckBox implements ListCellRenderer
  {
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus)
    {
      setSelected(isSelected);
      setText(value.toString());
      
      return this;
    }
  }

  private static class LabelCellRenderer 
    extends DefaultListCellRenderer
  {
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus)
    {
      Component c = super.getListCellRendererComponent(list, value, index, 
                                                       isSelected,
						       cellHasFocus);
      
      return c;
    }
  }

  public static JScrollPane mkScrollPane(JComponent inner)
  {
    JScrollPane jsp;
    jsp = new JScrollPane(inner,
			  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
			  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    
    return jsp;
  }

  public static JPanel mkListWorld()
  {

    String foo[] = new String[] { 
      "non alcoholic ",
      "carbonated ",
      "malted ",
      "fresh squeezed ",
      "imported ",
      "high fructose ",
      "enriched "
    };
    
    String bar[] = new String[] { 
      "orange juice",
      "ginger beer",
      "yak milk",
      "corn syrup",
      "herbal remedy"
    };

    final DefaultListModel mod = new DefaultListModel();
    final JList list1 = new JList(mod);
    final JList list2 = new JList(mod);

    list2.setSelectionModel(list1.getSelectionModel());
    for (int i = 0; i < bar.length; ++i)
      for (int j = 0; j < foo.length; ++j)
        mod.addElement(foo[j] + bar[i]);

    list1.setCellRenderer(new LabelCellRenderer());
    list2.setCellRenderer(new CheckCellRenderer());

    JButton add = mkButton("add element");
    add.addActionListener(new ActionListener()
      {
        int i = 0;
        public void actionPerformed(ActionEvent e)
        {
          mod.addElement("new element " + i);
          ++i;
        }
      });

    JButton del = mkButton("delete selected");
    del.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          for (int i = 0; i < mod.getSize(); ++i)
            if (list1.isSelectedIndex(i))
              mod.remove(i);
        }
      });


    JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitter.add(mkScrollPane(list1), JSplitPane.LEFT);
    splitter.add(mkScrollPane(list2), JSplitPane.RIGHT);

    JPanel p1 = new JPanel(); 
    p1.setLayout(new BorderLayout());

    JPanel p2 = new JPanel(); 
    p2.setLayout(new GridLayout(1, 2));
    p2.add(add);
    p2.add(del);

    p1.add(p2, BorderLayout.NORTH);
    p1.add(splitter, BorderLayout.CENTER);
    return p1;
  }


  static JPanel mkDesktopWorld()
  {
    
    final JDesktopPane desk = new JDesktopPane();
    desk.setDesktopManager(new DefaultDesktopManager());
    desk.setPreferredSize(new Dimension(300,300));
    desk.setMinimumSize(new Dimension(300,300));
    JButton but = mkButton("add frame");
    but.addActionListener(new ActionListener()
      {
        int i = 10;
        public void actionPerformed(ActionEvent e)
        {
          JInternalFrame f;
	  f = new JInternalFrame("internal", true, true, true, true);
          f.getContentPane().setLayout(new BorderLayout());
          f.getContentPane().add(mkToolBar(), BorderLayout.NORTH);
          f.getContentPane().add(mkButton(bigStockIcon("fullscreen")),
				 BorderLayout.CENTER);
          desk.add(f);
          f.setBounds(i, i, 250, 200);
          i += 30;
        }
      });
    
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(desk, BorderLayout.CENTER);
    panel.add(but, BorderLayout.NORTH);
    but.doClick();
    but.doClick();
    return panel;
  }

  static JTabbedPane mkTabbedPane()
  {
    JTabbedPane tabs = new JTabbedPane();
    
    tabs.add("Button world!", mkButtonWorld());
    tabs.add("List world!", mkListWorld());
    tabs.add("Desktop world!", mkDesktopWorld());
    return tabs;
  }

  static JComponent mkSliders()
  {
    JSlider slider = new JSlider();
    slider.setPaintTrack(true);
    slider.setPaintTicks(true);
    slider.setMajorTickSpacing(30);
    slider.setInverted(false);
    JProgressBar progress = new JProgressBar();
    BoundedRangeModel model = new DefaultBoundedRangeModel(10, 1, 0, 100);
    progress.setModel(model);
    slider.setModel(model);
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1, 2));
    panel.add(slider);
    panel.add(progress);
    return panel;
  }

  public Demo()
  {
    frame = new JFrame("Swing Activity Board");
    frame.setJMenuBar(mkMenuBar());
    JComponent component = (JComponent) frame.getContentPane();
    component.setLayout(new BorderLayout());
    component.add(mkToolBar(), BorderLayout.NORTH);
    component.add(mkTabbedPane(), BorderLayout.CENTER);
    component.add(mkButtonBar(), BorderLayout.SOUTH);
    
    frame.pack();
    frame.show();
  }

  public static class LaterMain
    implements Runnable
  {
    public void run()
    {
      Demo demo = new Demo();      
    }
  }

  public static void main(String args[])
  {
    SwingUtilities.invokeLater(new LaterMain());
  }

  public static JCheckBox mkCheckbox(String label)
  {
    JCheckBox c = new JCheckBox(label);
    c.setFont(new Font("Luxi", Font.PLAIN, 14));
    return c;
  }

  public static JRadioButton mkRadio(String label)
  {
    JRadioButton c = new JRadioButton(label);
    c.setFont(new Font("Luxi", Font.PLAIN, 14));
    return c;
  }

  public static JList mkList(Object[] elts)
  {
    JList list = new JList(elts);
    list.setFont(new Font("Luxi", Font.PLAIN, 14));
    return list;
  }

  public static JTabbedPane mkTabs(String[] names)
  {
    JTabbedPane tabs = new JTabbedPane();
    for (int i = 0; i < names.length; ++i)
      {
        tabs.addTab(names[i], mkButton(names[i]));
      }
    return tabs;
  }

  public static JComboBox mkComboBox(String[] names)
  {
    JComboBox box = new JComboBox(names);
    return box;
  }

  public static JSpinner mkSpinner()
  {
    JSpinner spinner = new JSpinner();
    return spinner;
  }

  public static JButton mkBigButton(String title)
  {
    JButton b = new JButton(title);
    b.setMargin(new Insets(5,5,5,5));
    //b.setFont(new Font("Luxi", Font.PLAIN, 14));
    return b;
  }

  public static JToggleButton mkToggle(String title)
  {
    JToggleButton b = new JToggleButton(title);
    b.setMargin(new Insets(5,5,5,5));
    b.setFont(new Font("Luxi", Font.PLAIN, 14));
    return b;    
  }

  public static JPanel mkPanel(JComponent[] inners)
  {
    JPanel p = new JPanel();
    for (int i = 0; i < inners.length; ++i)
      {
        p.add(inners[i]);
      }
    return p;
  }

  public static JScrollBar mkScrollBar()
  {
    JScrollBar scrollbar = new JScrollBar();
    return scrollbar;
  }

  public static JPanel mkViewportBox(final JComponent inner)
  {
    final JViewport port = new JViewport();
    port.setView(inner);
    JButton left = mkBigButton("left");
    JButton right = mkBigButton("right");
    
    left.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          Point p = port.getViewPosition();
          port.setViewPosition(new Point(p.x - 10, p.y));
        }
      });

    right.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          Point p = port.getViewPosition();
          port.setViewPosition(new Point(p.x + 10, p.y));
        }
      });
 
    return mkPanel(new JComponent[] {port, left, right});
  }

  public static JPanel mkListPanel(Object[] elts)
  {
    final DefaultListModel mod = new DefaultListModel();
    final JList list1 = new JList(mod);
    list1.setLayoutOrientation(JList.VERTICAL_WRAP);
    list1.setVisibleRowCount(4);
    final JList list2 = new JList(mod);
    list2.setLayoutOrientation(JList.VERTICAL_WRAP);
    list2.setVisibleRowCount(4);

    list2.setSelectionModel(list1.getSelectionModel());
    for (int i = 0; i < elts.length; ++i)
      mod.addElement(elts[i]);
    list1.setCellRenderer(new LabelCellRenderer());
    list2.setCellRenderer(new CheckCellRenderer());

    JButton add = mkBigButton("add element");
    add.addActionListener(new ActionListener()
      {
        int i = 0;
        public void actionPerformed(ActionEvent e)
        {
          mod.addElement("new element " + i);
          ++i;
        }
      });

    JButton del = mkBigButton("delete selected");
    del.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          for (int i = 0; i < mod.getSize(); ++i)
            if (list1.isSelectedIndex(i))
              mod.remove(i);
        }
      });

    return mkPanel(new JComponent[] {list1, //mkScrollPane(list1), 
                                     list2, //mkScrollPane(list2), 
                                     mkPanel(new JComponent[] {add, del})});
  }


  public static JButton mkDisposerButton(final JFrame c)
  {
    JButton close = mkBigButton("Close");
    close.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          c.dispose();
        }
      });
    return close;
  }

  public static JColorChooser mkColorChooser()
  {
    return new JColorChooser();
  }

  private static class PopUpAction
    implements ActionListener
  {
    private JComponent inner;
    private String name;

    PopUpAction(String n, JComponent i, JPanel p)
    {
      name = n;
      inner = i;

      JButton b = mkBigButton(name);
      b.addActionListener(this);
      p.add(b);
    }

    public void actionPerformed(ActionEvent e)
    {
      JFrame frame = new JFrame(name);
      frame.getContentPane().setLayout(new BorderLayout());
      frame.getContentPane().add(inner, BorderLayout.CENTER);
      frame.getContentPane().add(mkDisposerButton(frame), BorderLayout.SOUTH);
      frame.pack();
      frame.show();
    }
  }

  private JPanel mkButtonBar()
  {    
    JPanel panel = new JPanel ();
    panel.setLayout(new FlowLayout());

    new PopUpAction("Buttons",
		    mkPanel(new JComponent[]
			{mkBigButton("mango"), 
			 mkBigButton("guava"),
			 mkBigButton("lemon")}),
		    panel);
    
    new PopUpAction("Toggles",
		    mkToggle("cool and refreshing"),
		    panel);

    new PopUpAction("Checkbox",
		    mkCheckbox("ice cold"),
		    panel);

    new PopUpAction("Radio",
		    mkRadio("delicious"),
		    panel);

    new PopUpAction("Slider",
		    mkSliders(),
		    panel);

    new PopUpAction("List",
		    mkListPanel(new String[] { "hello",
					       "this",
					       "is",
					       "a",
					       "list",
                                               "that",
                                               "wraps",
                                               "over"}),
		    panel);

    new PopUpAction("Scrollbar",
		    mkScrollBar(),
		    panel);

    new PopUpAction("Viewport",
		    mkViewportBox(mkBigButton("View Me!")),
		    panel);

    new PopUpAction("ScrollPane",
		    mkScrollPane(mkBigButton("Scroll Me!")),
		    panel);

    new PopUpAction("TabPane",
		    mkTabs(new String[] {"happy",
					 "sad",
					 "indifferent"}),
		    panel);

    new PopUpAction("Spinner",
		    mkSpinner(),
		    panel);

    new PopUpAction("TextField",
		    mkTextField("Hello, World!"),
		    panel);

    new PopUpAction("ColorChooser",
		    mkColorChooser(),
		    panel);

    new PopUpAction("ComboBox",
		    mkComboBox(new String[] {"Stop",
					     "Software",
					     "Hoarders",
					     "Support",
					     "GNU!"}),
		    panel);

    JButton exitDisposer = mkDisposerButton(frame);
    panel.add(exitDisposer);

    return panel;
  }

  public static JTextField mkTextField(String sometext)
  {
    return new JTextField(sometext, 40);
  }
}
