package javax.swing;

import java.util.*;
import javax.swing.plaf.*;
import java.awt.*;

public class JTabbedPane extends JComponent
{
    class Tab
    {
	Icon icon;
	String name, descr;
	Component tab;

	Tab(String name,
	    Icon icon,
	    Component tab,
	    String descr)
	{
	    this.name = name;
	    this.icon = icon;
	    this.tab  = tab;
	    this.descr = descr;
	}
    }
    
    private Vector tabs = new Vector();

    public JTabbedPane()
    {
    }

    public void addTab(String name,
		Component panel)		
    {
	addTab(name, null, panel, null);
    }
    public void addTab(String name,
		Icon icon,
		Component panel)		
    {
	addTab(name, icon, panel, null);
    }
    public void addTab(String name,
		Icon icon,
		Component panel,
		String descr)
    {
	tabs.addElement(new Tab(name, icon, panel, descr));
    }

    public int getTabCount()
    {
	return tabs.size();
    }
    public Component getComponentAt(int i)
    {
	Tab t = (Tab) tabs.elementAt(i);
	return t.tab;
    }
    
    public String getUIClassID()
    {	return "JTabbedPane";    }


    public void setUI(TabbedPaneUI ui) {
        super.setUI(ui);
    }
    
    public TabbedPaneUI getUI() {
        return (TabbedPaneUI)ui;
    }
    
    public void updateUI()
    {
        setUI((TabbedPaneUI)UIManager.getUI(this));
    }
    
    public AccessibleContext getAccessibleContext()
    {
	return null;
    }
    
   protected  String paramString()
    {
	return "JTabbedPane";
    }
}
