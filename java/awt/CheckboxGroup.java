/*************************************************************************
/* CheckboxGroup.java -- A grouping class for checkboxes.
/*
/* Copyright (c) 1999 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This library is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later verion.
/*
/* This library is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this library; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.awt;

/**
  * This class if for combining checkboxes into groups so that only
  * one checkbox in the group can be selected at any one time.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class CheckboxGroup implements java.io.Serializable
{

/*
 * Static Variables
 */

// Serialization constant
private static final long serialVersionUID = 3729780091441768983L;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial The currently selected checkbox.
  */
private Checkbox selectedCheckbox;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>CheckboxGroup</code>.
  */
public
CheckboxGroup()
{
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the currently selected checkbox, or <code>null</code> if none
  * of the checkboxes in this group are selected.
  *
  * @return The selected checkbox.
  */
public Checkbox
getSelectedCheckbox()
{
  return(selectedCheckbox);
} 

/*************************************************************************/

/**
  * Returns the currently selected checkbox, or <code>null</code> if none
  * of the checkboxes in this group are selected.
  *
  * @return The selected checkbox.
  *
  * @deprecated This method is deprecated in favor of 
  * <code>getSelectedCheckbox()</code>.
  */
public Checkbox
getCurrent()
{
  return(selectedCheckbox);
} 

/*************************************************************************/

/**
  * This method sets the specified checkbox to be the selected on in this
  * group, and unsets all others.
  *
  * @param selectedCheckbox The new selected checkbox.
  */
public void
setSelectedCheckbox(Checkbox selectedCheckbox)
{
  if (this.selectedCheckbox != null)
    {
      if (this.selectedCheckbox.getCheckboxGroup() != this)
        return;

      this.selectedCheckbox.setState(false);
    }

  this.selectedCheckbox = selectedCheckbox;
  selectedCheckbox.setState(true);
}

/*************************************************************************/

/**
  * This method sets the specified checkbox to be the selected on in this
  * group, and unsets all others.
  *
  * @param selectedCheckbox The new selected checkbox.
  *
  * @deprecated This method is deprecated in favor of
  * <code>setSelectedCheckbox()</code>.
  */
public void
setCurrent(Checkbox selectedCheckbox)
{
  setSelectedCheckbox(selectedCheckbox);
}

/*************************************************************************/

/**
  * Returns a string representation of this checkbox group.
  *
  * @return A string representation of this checkbox group.
  */
public String
toString()
{
  return(getClass().getName() + "[selectedCheckbox=" + selectedCheckbox + "]");
}

} // class CheckboxGroup 

