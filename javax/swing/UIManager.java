package javax.swing;

import java.io.*;
import java.awt.*;

import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;


import java.beans.*;


public class UIManager implements Serializable
{
    static class LookAndFeelInfo
    {
	String name, clazz;
	
	LookAndFeelInfo(String name, 
			String clazz)
	{
	    this.name  = name;
	    this.clazz = clazz;
	}
	String getName()      { return name;  }
	String getClassName() { return clazz; }
    }

    
    static LookAndFeelInfo [] installed = {
	new LookAndFeelInfo("Metal",
			    "javax.swing.plaf.metal.MetalLookAndFeel")
    };

    
    static LookAndFeel[] aux_installed;
    
    static LookAndFeel look_and_feel  = new MetalLookAndFeel();
    

    UIManager()
    {
    }

    static  void addPropertyChangeListener(PropertyChangeListener listener)
    {
	//      Add a PropertyChangeListener to the listener list. 
    }

    static  void addAuxiliaryLookAndFeel(LookAndFeel l)
    {
	//          Add a LookAndFeel to the list of auxiliary look and feels. 
	if (aux_installed == null)
	    {
		aux_installed = new LookAndFeel[1];
		aux_installed[0] = l;
		return;
	    }
	
	LookAndFeel[] T = new LookAndFeel[ aux_installed.length+1 ];
	System.arraycopy(aux_installed, 0,
			 T,             0,
			 aux_installed.length);			 
	aux_installed = T;
	aux_installed[aux_installed.length-1] = l;
    }
    
    static  boolean removeAuxiliaryLookAndFeel(LookAndFeel laf)
    {
	if (aux_installed == null)
	    return false;

	for (int i=0;i<aux_installed.length;i++)
	    {
		if (aux_installed[i] == laf)
		    {
			aux_installed[ i ] = aux_installed[aux_installed.length-1];
			
			LookAndFeel[] T = new LookAndFeel[ aux_installed.length-1 ];
			System.arraycopy(aux_installed, 0,
					 T,             0,
					 aux_installed.length-1);			 
			aux_installed = T;
			return true;
		    }		
	    }
	return false;
    }

    static  LookAndFeel[] getAuxiliaryLookAndFeels()
    {	return aux_installed;    }


    static  Object get(Object key)
    {	return getLookAndFeel().getDefaults().get(key);    }
    
    static  Border getBorder(Object key)
    //      Returns a border from the defaults table. 
    {
	return (Border) getLookAndFeel().getDefaults().get(key);
    }

    static  Color getColor(Object key)
    //      Returns a drawing color from the defaults table. 
    {
	return (Color) getLookAndFeel().getDefaults().get(key);
    }
    static  String getCrossPlatformLookAndFeelClassName()
    {
	// this string can be passed to Class.forName()
	return "javax.swing.plaf.metal.MetalLookAndFeel";
    }

    static  UIDefaults getDefaults()
    {
	//      Returns the default values for this look and feel. 
	return getLookAndFeel().getDefaults();
    }

    static  Dimension getDimension(Object key)
    //      Returns a dimension from the defaults table. 
    {
	System.out.println("UIManager.getDim");
	return new Dimension(200,100);
    }
    static  Font getFont(Object key)
    //      Returns a drawing font from the defaults table. 
    {
	return (Font) getLookAndFeel().getDefaults().get(key);
    }
    static  Icon getIcon(Object key)
    //      Returns an Icon from the defaults table. 
    {
	return (Icon) getLookAndFeel().getDefaults().get(key);
    }
    static  Insets getInsets(Object key)
    //      Returns an Insets object from the defaults table. 
    {
	return (Insets) getLookAndFeel().getDefaults().getInsets(key);
    }

    static LookAndFeelInfo[] getInstalledLookAndFeels()
    {
	return installed;
    }

    static  int getInt(Object key)
    {
	Integer x = (Integer) getLookAndFeel().getDefaults().get(key);
	if (x == null)
	    return 0;
	return x.intValue();
    }
    static  LookAndFeel getLookAndFeel()
    {
	return look_and_feel;
    }

    static  UIDefaults getLookAndFeelDefaults()
    //      Returns the default values for this look and feel. 
    {
	return getLookAndFeel().getDefaults();
    }
    static  String getString(Object key)
    //      Returns a string from the defaults table. 
    {
	return (String) getLookAndFeel().getDefaults().get(key);
    }
    static  String getSystemLookAndFeelClassName()
    //      Returns the name of the LookAndFeel class that implements the native systems look and feel if there is one, otherwise the name of the default cross platform LookAndFeel class. 
    {
	return getCrossPlatformLookAndFeelClassName();
    }


    static  ComponentUI getUI(JComponent target)
    //      Returns the L&F object that renders the target component. 
    {
	ComponentUI ui = getDefaults().getUI(target);
	//System.out.println("GET-UI-> " + ui + ", for " + target);
	return ui;
    }


    static  void installLookAndFeel(String name, String className)
    //      Creates a new look and feel and adds it to the current array. 
    {
    }
    static  void installLookAndFeel(LookAndFeelInfo info)
    //      Adds the specified look and feel to the current array and then calls setInstalledLookAndFeels(javax.swing.UIManager.LookAndFeelInfo[]). 
    {
    }
    static  Object put(Object key, Object value)
    //      Stores an object in the defaults table. 
    {
	return getLookAndFeel().getDefaults().put(key,value);
    }
    static  void removePropertyChangeListener(PropertyChangeListener listener)
    //      Remove a PropertyChangeListener from the listener list. 
    {
    }
    static  void setInstalledLookAndFeels(UIManager.LookAndFeelInfo[] infos)
    //      Replaces the current array of installed LookAndFeelInfos. 
    {
    }
    static  void setLookAndFeel(LookAndFeel newLookAndFeel)
    {
	if (look_and_feel != null)
	    look_and_feel.uninitialize();

	//      Set the current default look and feel using a LookAndFeel object. 
	look_and_feel = newLookAndFeel;
	look_and_feel.initialize();
	
	//	revalidate();
	//	repaint();
    }
    static  void setLookAndFeel(String className)
        throws ClassNotFoundException, 
               InstantiationException, 
               IllegalAccessException,
	       UnsupportedLookAndFeelException
    {
	//          Set the current default look and feel using a class name.
	Class c = Class.forName(className);
	LookAndFeel a = (LookAndFeel) c.newInstance(); // throws class-cast-exception
	setLookAndFeel(a);
    }


}
