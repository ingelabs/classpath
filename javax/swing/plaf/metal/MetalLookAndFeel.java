package javax.swing.plaf.metal;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;


public class MetalLookAndFeel extends LookAndFeel
 {	   
     boolean isNativeLookAndFeel()        { return true; }
     boolean isSupportedLookAndFeel()     { return true; }
     String getDescription()              { return "Metal look and feel"; }
     String getID()                       { return "MetalLookAndFeel"; }
     String getName()                     { return "MetalLookAndFeel"; }
     
     
     UIDefaults LAF_defaults;

     MetalLookAndFeel()
     {
     }

     UIDefaults getDefaults()
	 {
	   if (LAF_defaults == null)
	     LAF_defaults = new BasicDefaults();
	     
	     //      Returns the default values for this look and feel. 
	     return LAF_defaults;
	 }
 };
