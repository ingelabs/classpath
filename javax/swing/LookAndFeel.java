package javax.swing;

import javax.swing.text.*;

public abstract class LookAndFeel
{
    public UIDefaults getDefaults()
    {
	//This method is called once by UIManager.setLookAndFeel to create the look and feel specific defaults table.
	return null;
    }

    public abstract  String getDescription();  
    public abstract  String getID();
    public abstract  String getName();

    public void initialize()
    {
	//UIManager.setLookAndFeel calls this method before the first call (and typically the only call) to getDefaults(). 
    }

    static void installBorder(JComponent c, String defaultBorderName)
    {
	//Convenience method for installing a component's default Border object on the specified component if either the border is currently null or already an instance of UIResource. 
    }

    public static void installColors(JComponent c, String defaultBgName, String defaultFgName)
    {
	//Convenience method for initializing a component's foreground and background color properties with values from the current defaults table. 
    }

    public static void installColorsAndFont(JComponent c, String defaultBgName, String defaultFgName, String defaultFontName)
    {
	//Convenience method for initializing a components foreground background and font properties with values from the current defaults table. 
    }
  
    public abstract  boolean isNativeLookAndFeel();
    public abstract  boolean isSupportedLookAndFeel();

    public static void loadKeyBindings(InputMap retMap, Object[] keys)
    {
	//Loads the bindings in keys into retMap. 
    }

    public static ComponentInputMap makeComponentInputMap(JComponent c, Object[] keys)
    {
	//    Creates a ComponentInputMap from keys. 
	return null;
    }  

    public static Object makeIcon(Class baseClass, String gifFile)
    {
	//Utility method that creates a UIDefaults.LazyValue that creates an ImageIcon UIResource for the specified gifFile filename. 
	return null;
    }
  
    public static InputMap makeInputMap(Object[] keys)
    {
	//Creates a InputMap from keys. 
	return null;
    }

    public static JTextComponent.KeyBinding[] makeKeyBindings(Object[] keyBindingList)
    {
	//        Convenience method for building lists of KeyBindings.  
	return null;
    }

  
    public String toString()
    {
	//Returns a string that displays and identifies this object's properties. 
	return "LookAndFeel";
    }
  
    public void uninitialize()
    {
	//UIManager.setLookAndFeel calls this method just before we're replaced by a new default look and feel. 
    }

  
    public static void uninstallBorder(JComponent c)
    {
	//Convenience method for un-installing a component's default border on the specified component if the border is currently an instance of UIResource.
    }

}

