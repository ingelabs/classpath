/*************************************************************************
 * GtkLookAndFeel.java
 *
 * Copyright (c) 1999 by Free Software Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package gnu.javax.swing.plaf.gtk;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/**
 *
 * @author Brian Jones
 * @see javax.swing.LookAndFeel
 */
public class GtkLookAndFeel extends BasicLookAndFeel
{
    private UIDefaults uiDefaults;

    /**
     */
    public GtkLookAndFeel()
    {
    }

    /**
     * A short string to identify this look and feel, for example in a 
     * drop down list to choose between several look and feels.
     */
    public String getName() { return "GIMP Toolkit"; }

    /**
     * A much longer description of the look and feel.
     */
    public String getDescription() 
    { 
	return new String("The GIMP Toolkit Look and Feel for Java, " + 
			  "written by Brian Jones (cbj@gnu.org), " + 
			  "(c) 1999 by Free Software Foundation, Inc.  " +
			  "http://www.classpath.org");
    }

    /**
     * Return a unique string identifying this look and feel as different
     * from and not a subclass of any other look and feel.  Usually, a 
     * subclass will return the same <code>String</code> here as the
     * original look and feel if only a few changes are being made rather 
     * than something completely new and different.
     */
    public String getID()
    {
	return "Gtk";
    }

    public boolean isNativeLookAndFeel()
    {
	return false;
    }

    public boolean isSupportedLookAndFeel()
    {
	return true;
    }

    protected void initClassDefaults(UIDefaults table)
    {
	super.initClassDefaults(table);

	String gtkPkgName = "gnu.javax.swing.plaf.gtk.";

	
	Object[] defaults = { 
	    "SliderUI", gtkPkgName + "GtkSliderUI"
	};
	/*
	    "CheckBoxUI", gtkPkgName + "GtkCheckBoxUI",
	    "ButtonUI", gtkPkgName + "GtkButtonUI"
		"ColorChooserUI", "MetalColorChooserUI",
		"MenuBarUI", "MetalMenuBarUI",
		"MenuUI", "MetalMenuUI", 
		"MenuItemUI", "MetalMenuItemUI", 
		"CheckBoxMenuItemUI", "MetalCheckBoxMenuItemUI", 
		"RadioButtonMenuItemUI", "MetalRadioButtonMenuItemUI", 
		"RadioButtonUI", "MetalRadioButtonUI", 
		"ToggleButtonUI", "MetalToggleButtonUI",
		"PopupMenuUI", "MetalPopupMenuUI",
		"ProgressBarUI", "MetalProgressBarUI",
		"ScrollBarUI", "MetalScrollBarUI",
		"ScrollPaneUI", "MetalScrollPaneUI",
		"SplitPaneUI", "MetalSplitPaneUI",
		"SeparatorUI", "MetalSeparatorUI",
		"ToolBarSeparatorUI", "MetalToolBarSeparatorUI",
		"PopupMenuSeparatorUI", "MetalPopupMenuSeparatorUI", 
		"TabbedPaneUI", "MetalTabbedPaneUI",
		"TextAreaUI", "MetalTextAreaUI",
		"TextFieldUI", "MetalTextFieldUI",
		"PasswordFieldUI", "MetalPasswordFieldUI",
		"TextPaneUI", "MetalTextPaneUI",
		"EditorPaneUI", "MetalEditorPaneUI",
		"TreeUI", "MetalTreeUI",
		"LabelUI", "MetalLabelUI",
		"ListUI", "MetalListUI",
		"ToolBarUI", "MetalToolBarUI",
		"ToolTipUI", "MetalToolTipUI",
		"ComboBoxUI", "MetalComboBoxUI",
		"TableUI", "MetalTableUI",
		"TableHeaderUI", "MetalTableHeaderUI",
		"InternalFrameUI", "GtkInternalFrameUI",
		"StandardDialogUI", "GtkStandardDialogUI",
		"DesktopPaneUI", "GtkDesktopPaneUI",
		"DesktopIconUI", "GtkDesktopIconUI",
		"DirectoryPaneUI", "GtkDirectoryPaneUI",
		"FileChooserUI", "GtkFileChooserUI",
		"OptionPaneUI", "GtkOptionPaneUI" }
	*/
	table.putDefaults(defaults);

    }

    protected void initSystemColorDefaults(UIDefaults table)
    {
	String[] colors = {
	    "desktop", "#000000",
	    "activeCaption", "#163555",
	    "activeCaptionText", "#FFFFFF",
	    "activeCaptionBorder", "#000000",
	    "inactiveCaption", "#375676",
	    "inactiveCaptionText", "#999999",
	    "inactiveCaptionBorder", "#000000",
	    "window", "#FFFFFF",
	    "windowBorder", "#969696",
	    "windowText", "#000000",
	    "menu", "#d6d6d6",
	    "menuText", "#000000",
	    "text", "#FFFFFF",
	    "textText", "#000000",
	    "textHighlight", "#00009c",
	    "textHighlightText", "#FFFFFF",
	    "textInactiveText", "#999999",
	    "control", "#d6d6d6",
	    "controlText", "#000000",
	    "controlHighlight", "#eaeaea",
	    "controlLtHighlight", "#eaeaea",
	    "controlShadow", "#c3c3c3",
	    "controlDkShadow", "#888888",
	    "scrollbar", "#c3c3c3",
	    "info", "#d6d6d6",
	    "infoText", "#000000"
	};

	loadSystemColors(table, colors, false);
    }

    protected void initComponentDefaults(UIDefaults table)
    {
	super.initComponentDefaults(table);

	// define common resources
	// fonts
	FontUIResource sansSerifPlain10 = 
	    new FontUIResource("SansSerif", Font.PLAIN, 10);
	FontUIResource serifPlain10 = 
	    new FontUIResource("Serif", Font.PLAIN, 10);
	// insets
	// borders
	// colors
	ColorUIResource controlDkShadow = (ColorUIResource)table.get("controlDkShadow");
	ColorUIResource controlShadow = (ColorUIResource)table.get("controlShadow");
	ColorUIResource control = (ColorUIResource)table.get("control");
	ColorUIResource scrollbar = (ColorUIResource)table.get("scrollbar");
//  	System.out.println((ColorUIResource)table.get("control"));
  	ColorUIResource white = new ColorUIResource(Color.white);
  	ColorUIResource black = new ColorUIResource(Color.black);
	ColorUIResource blue = new ColorUIResource(Color.blue);

	// icons
	Object errorIcon = LookAndFeel.makeIcon(getClass(), "icons/error.gif");
	// any other resources like dimensions and integer values

	// define defaults
	Object[] defaults = 
	{ 
	    "Button.font", sansSerifPlain10,
	    "CheckBox.font", sansSerifPlain10,
  	    "RadioButton.pressed", black,
	    "Slider.focus", blue,
	    "Slider.foreground", control,
  	    "Slider.highlight", white,
  	    "Slider.shadow", black
	};

	table.putDefaults(defaults);
    }
}
