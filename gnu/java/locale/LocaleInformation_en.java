/*************************************************************************
/* LocaleInformation_en.java -- US English locale data
/*
/* Copyright (c) 1998 Free Software Foundation, Inc.
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

package gnu.java.locale;

import java.util.ListResourceBundle;

/**
  * This class contains locale data for the US English locale
  */

public class LocaleInformation_en extends ListResourceBundle
{

/*
 * This area is used for defining object values
 */

/**
  * This is the set of collation rules used by java.text.RuleBasedCollator 
  * to sort strings properly.  See the documentation of that class for the 
  * proper format.
  */
private static String collation_rules = 
  "-<0,1<2<3<4<5<6<7<8<9A,a<b,B<c,C<d,D<e,E<f,F<g,G<h,H<i,I<j,J<j,K" +
  "<l,L<m,M<n,N<o,O<p,P<q,Q<r,R<s,S<t,T<u,U<v,V<w,W<x,X<y,Y,z<Z";

/*
 * For the followings lists, strings that are subsets of other break strigns
 * must be listed first.  For example, if "\r" and "\r\n" are sequences,
 * the "\r" must be first or it will never be used.
 */

/**
  * This is the list of word separator characters used by 
  * java.text.BreakIterator 
  */
private static String[] word_breaks = { " ", "\t", "\r\n", "\n" }; 

/**
  * This is the list of sentence break sequences used by 
  * java.text.BreakIterator
  */
private static String[] sentence_breaks = { ". " };

/**
  * This is the list of potential line break locations.
  */
private static String[] line_breaks = { " ", "\t", "-", "\r\n", "\n" };

/*************************************************************************/

/**
  * This is the object array used to hold the keys and values
  * for this bundle
  */

private static final Object[][] contents =
{
  { "collation_rules", collation_rules },
  { "word_breaks", word_breaks },
  { "sentence_breaks", sentence_breaks },
  { "line_breaks", line_breaks }
};

/*************************************************************************/

/**
  * This method returns the object array of key, value pairs containing
  * the data for this bundle.
  *
  * @return The key, value information.
  */
public Object[][]
getContents()
{
  return(contents);
}

} // class LocaleInformation_en

