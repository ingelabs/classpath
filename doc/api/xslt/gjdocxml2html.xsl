<?xml version="1.0" encoding="utf-8"?>

<!-- gjdocxml2html.xsl
     Copyright (C) 2001 Free Software Foundation, Inc.
     
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
     Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
     02111-1307 USA.
     -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:gjdoc="http://www.gnu.org/software/cp-tools/gjdocxml"
  xmlns="http://www.w3.org/TR/xhtml1/strict">

  <xsl:variable name="version">0.2.1</xsl:variable>
  
  <xsl:output method="xml"
    encoding="ISO-8859-1"
    indent="no"/>

  <xsl:strip-space elements="*"/>
  <xsl:preserve-space elements="tag tag//*"/>

  <!-- <xsl:exclude-result-prefixes abc="..."/> -->

  <xsl:param name="verbose" select="0"/>

  <xsl:param name="allowimages" select="0"/>

  <xsl:param name="copyrightfile" select="'copyright.xml'"/>

  <xsl:param name="windowtitle" select="'Generated Documentation'"/>

  <xsl:param name="targetdir" select="'.'"/>

  <xsl:param name="refdocs1" select="''"/>

  <xsl:template name="tree_indentation_text_empty">
    <xsl:param name="p_indent"/>
    <xsl:choose>
      <xsl:when test="$p_indent&gt;1">
        <xsl:text>   </xsl:text>
        <xsl:call-template name="tree_indentation_text_empty">
          <xsl:with-param name="p_indent"><xsl:value-of select="$p_indent+-1"/></xsl:with-param>
        </xsl:call-template>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- Recursively insert indentation for heritage tree. -->
  <!-- This one uses images for nice, visual indentation. -->
  <!-- p_indent selects level of indentation. -->
  
  <xsl:template name="tree_indentation">
    <xsl:param name="p_indent"/>
    <xsl:choose>
      <xsl:when test="$p_indent&gt;1">
        <xsl:choose>
          <xsl:when test="$allowimages=1">
            <img width="26" height="21" border="0" src="common/images/tree-empty.png" class="classdoc-tree-image"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>   </xsl:text>
          </xsl:otherwise>
        </xsl:choose>

        <xsl:call-template name="tree_indentation">
          <xsl:with-param name="p_indent"><xsl:value-of select="$p_indent+-1"/></xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:choose>
          <xsl:when test="$allowimages=1">
            <img width="26" height="21" border="0" src="common/images/tree-branch.png" class="classdoc-tree-image"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>+--</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <!-- Recursively create the tree showing the heritage of the -->
  <!-- given class -->
  
  <xsl:template name="create_tree">
    <xsl:param name="p_superclass"/>
    <xsl:param name="p_indent"/>	
    <xsl:param name="p_final" select="0"/>

    <xsl:variable name="v_superclass" select="/gjdoc:rootdoc/gjdoc:classdoc[attribute::qualifiedtypename=$p_superclass]/gjdoc:superclass"/>

    <xsl:if test="$v_superclass">
      <xsl:call-template name="create_tree">
        <xsl:with-param name="p_indent"><xsl:value-of select="$p_indent+-1"/></xsl:with-param>
        <xsl:with-param name="p_superclass"><xsl:value-of select="/gjdoc:rootdoc/gjdoc:classdoc[attribute::qualifiedtypename=$p_superclass]/gjdoc:superclass/@qualifiedtypename"/></xsl:with-param>
      </xsl:call-template>

      <xsl:call-template name="tree_indentation_text_empty">
        <xsl:with-param name="p_indent"><xsl:value-of select="$p_indent"/></xsl:with-param>
      </xsl:call-template>
      <xsl:text>|
</xsl:text>
    </xsl:if>	

    <xsl:if test="$p_indent>0">	
      <xsl:call-template name="tree_indentation">
        <xsl:with-param name="p_indent"><xsl:value-of select="$p_indent"/></xsl:with-param>
      </xsl:call-template>
    </xsl:if>	
        
    <xsl:choose>
      <xsl:when test="$p_final=1">
        <xsl:value-of select="$p_superclass"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="link_to_class_full">
          <xsl:with-param name="p_qualifiedname">
            <xsl:value-of select="$p_superclass"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <!-- Recursively create the tree showing the heritage of the -->
  <!-- given class, use bitmaps -->
  
  <xsl:template name="create_tree_gfx">
    <xsl:param name="p_superclass"/>
    <xsl:param name="p_indent"/>	
    <xsl:param name="p_final" select="0"/>
    <xsl:if test="/gjdoc:rootdoc/gjdoc:classdoc[attribute::qualifiedtypename=$p_superclass]/gjdoc:superclass">
      <xsl:call-template name="create_tree">
        <xsl:with-param name="p_indent"><xsl:value-of select="$p_indent+-1"/></xsl:with-param>
        <xsl:with-param name="p_superclass"><xsl:value-of select="/gjdoc:rootdoc/gjdoc:classdoc[attribute::qualifiedtypename=$p_superclass]/gjdoc:superclass/@qualifiedtypename"/></xsl:with-param>
      </xsl:call-template>
    </xsl:if>
    <tr><td>
    <table cellspacing="0" cellpadding="0" border="0">
      <tr>
        <xsl:variable name="imagename">
          <xsl:choose>
            <xsl:when test="$p_final=1">
              <xsl:text>common/images/tree-final-node.png</xsl:text>
            </xsl:when>
            <xsl:otherwise>
              <xsl:text>common/images/tree-node.png</xsl:text>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>

        <td>
          <xsl:if test="$p_indent>0">	
            <xsl:call-template name="tree_indentation">
              <xsl:with-param name="p_indent"><xsl:value-of select="$p_indent"/></xsl:with-param>
            </xsl:call-template>
          </xsl:if>	
          <img width="10" height="21" class="classdoc-tree-image" src="{$imagename}"/>
        </td>

        <xsl:choose>
          <xsl:when test="$p_final=1">
            <td class="classdoc-tree-label"><xsl:value-of select="$p_superclass"/></td>
          </xsl:when>
          <xsl:otherwise>
            <td class="classdoc-tree-label">
              <xsl:call-template name="link_to_class_full">
                <xsl:with-param name="p_qualifiedname">
                  <xsl:value-of select="$p_superclass"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
          </xsl:otherwise>
        </xsl:choose>
      </tr></table>
    </td></tr>
  </xsl:template>

  <!-- Recursively output one character for each superclass found for the given -->
  <!-- class. The length of the resulting string tells how many levels of -->
  <!-- indentation are required for creating the heritage tree.  -->
  
  <xsl:template name="output_base_markers">
    <xsl:param name="p_superclass"/>
    <xsl:if test="/gjdoc:rootdoc/gjdoc:classdoc[attribute::qualifiedtypename=$p_superclass]/gjdoc:superclass">
      <xsl:call-template name="output_base_markers">
        <xsl:with-param name="p_superclass"><xsl:value-of select="/gjdoc:rootdoc/gjdoc:classdoc[attribute::qualifiedtypename=$p_superclass]/gjdoc:superclass/@qualifiedtypename"/></xsl:with-param>
      </xsl:call-template>
    </xsl:if>
    <xsl:text>*</xsl:text>
  </xsl:template>
  
  <!-- Output header for Class documentation  -->
  
  <xsl:template name="classdoc_header">
    
    <div class="header">
      <a href="index.html" target="_top">Index (Frames)</a> |
      <a href="index_noframes.html" target="_top">Index (No Frames)</a> |
      <a href="{concat(gjdoc:containingPackage/@name,'.html')}">Package</a> |
      <a href="{concat(gjdoc:containingPackage/@name,'-tree.html')}">Package Tree</a> |
      <a href="full-tree.html">Tree</a>
    </div>

    <div class="classdoc-head">

      <b class="classdoc-head-packagename">
        <xsl:value-of select="gjdoc:containingPackage/@name"/>
      </b>
      <br/>
    
      <h1 class="classdoc-head-classname">
        <xsl:choose>
          <xsl:when test="gjdoc:isInterface">
            <xsl:text>Interface </xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>Class </xsl:text>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:value-of select="@name"/>
      </h1>

    </div>
    
    <xsl:call-template name="classdoc_heritage_tree"/>
    
    <xsl:if test="gjdoc:implements">
      <p/>
      <b>All Implemented Interfaces:</b><br/>
      <xsl:for-each select="gjdoc:implements|gjdoc:superimplements">
        <xsl:call-template name="link_to_class">
          <xsl:with-param name="p_qualifiedname">
            <xsl:value-of select="@qualifiedtypename"/>
          </xsl:with-param>
          <xsl:with-param name="p_name">
            <xsl:value-of select="@typename"/>
          </xsl:with-param>
        </xsl:call-template>
        <xsl:if test="position() != last()">
          <xsl:text>, </xsl:text>
        </xsl:if>
      </xsl:for-each>
    </xsl:if>
    <p/>
    <hr/>
    
    <div class="classdoc-prototype">

      <!-- 'public final class Byte' -->
      
      <xsl:value-of select="gjdoc:access/@scope"/><xsl:text> </xsl:text>
      <xsl:if test="gjdoc:isStatic"><xsl:text>static </xsl:text></xsl:if>
      <xsl:if test="gjdoc:isFinal"><xsl:text>final </xsl:text></xsl:if>
      <xsl:choose>
        <xsl:when test="gjdoc:isInterface">
          <xsl:text>interface </xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:if test="gjdoc:isAbstract"><xsl:text>abstract </xsl:text></xsl:if>
          <xsl:text>class </xsl:text>
        </xsl:otherwise>
      </xsl:choose>
      <b><xsl:value-of select="@name"/></b><br/>
    
      <!-- 'extends Number' -->
      
      <xsl:if test="gjdoc:isClass">
        <xsl:text>extends </xsl:text>
        <xsl:call-template name="link_to_class">
          <xsl:with-param name="p_qualifiedname">
            <xsl:value-of select="gjdoc:superclass/@qualifiedtypename"/>
          </xsl:with-param>
          <xsl:with-param name="p_name">
            <xsl:value-of select="gjdoc:superclass/@typename"/>
          </xsl:with-param>
        </xsl:call-template>
        <br/>
      </xsl:if>
      
      <!-- 'implements Comparable' -->
      
      <xsl:if test="gjdoc:implements">
        <xsl:text>implements </xsl:text>
      </xsl:if>
      <xsl:for-each select="gjdoc:implements">
        <xsl:call-template name="link_to_class">
          <xsl:with-param name="p_qualifiedname">
            <xsl:value-of select="@qualifiedtypename"/>
          </xsl:with-param>
          <xsl:with-param name="p_name">
            <xsl:value-of select="@typename"/>
          </xsl:with-param>
        </xsl:call-template>
        <xsl:if test="position() != last()">
          <xsl:text>, </xsl:text>
        </xsl:if>
      </xsl:for-each>
      
    </div>
    
    <!-- Documentation -->
    
    <p/>
    
    <div class="classdoc-comment-body">
      <xsl:for-each select="gjdoc:inlineTags/node()">
        <xsl:copy-of select="."/>
      </xsl:for-each>
    </div>

    <p/>

    <xsl:call-template name="output_since_tags"/>
    <xsl:call-template name="output_author_tags"/>
    <xsl:call-template name="output_see_tags"/>

  </xsl:template>

  <xsl:template name="output_since_tags">

    <xsl:if test="gjdoc:tags/gjdoc:tag[@kind='@since']">
      <b>Since:</b> 
      <ul>
        <li><xsl:copy-of select="gjdoc:tags/gjdoc:tag[@kind='@since']"/></li>
      </ul>
    </xsl:if>

  </xsl:template>

  <xsl:template name="output_author_tags">

    <xsl:if test="gjdoc:tags/gjdoc:tag[@kind='@author']">
      <xsl:choose>
        <xsl:when test="gjdoc:tags/gjdoc:tag[@kind='@author'][position()=2]">
          <b>Authors:</b> 
        </xsl:when>
        <xsl:otherwise>
          <b>Author:</b>           
        </xsl:otherwise>
      </xsl:choose>

      <ul>
        <xsl:for-each select="gjdoc:tags/gjdoc:tag[@kind='@author']">
          <li><xsl:copy-of select="."/></li>
        </xsl:for-each>
      </ul>
    </xsl:if>
  </xsl:template>
  
  <!-- Heritage Tree -->
  
  <xsl:template name="classdoc_heritage_tree">
    
    <xsl:variable name="p_qualifiedtypename" select="@qualifiedtypename"/>

    <xsl:for-each select="document('index.xml', /)//gjdoc:classdoc[@qualifiedtypename=$p_qualifiedtypename]">

      <xsl:variable name="superclass_markers">
        <xsl:call-template name="output_base_markers">
          <xsl:with-param name="p_superclass"><xsl:value-of select="gjdoc:superclass/@qualifiedtypename"/></xsl:with-param>
        </xsl:call-template>
      </xsl:variable>
      
      <xsl:choose>
        <xsl:when test="$allowimages=1">
          <table border="0" cellpadding="0" cellspacing="0" style="margin:0px; padding:0px; line-height:1px">
            <xsl:call-template name="create_tree_gfx">
              <xsl:with-param name="p_indent"><xsl:value-of select="string-length($superclass_markers)"/></xsl:with-param>
              <xsl:with-param name="p_superclass"><xsl:value-of select="@qualifiedtypename"/></xsl:with-param>
              <xsl:with-param name="p_final">1</xsl:with-param>
            </xsl:call-template>
          </table>
        </xsl:when>
        <xsl:otherwise>
          <pre>
            <xsl:call-template name="create_tree">
              <xsl:with-param name="p_indent"><xsl:value-of select="string-length($superclass_markers)"/></xsl:with-param>
              <xsl:with-param name="p_superclass"><xsl:value-of select="@qualifiedtypename"/></xsl:with-param>
              <xsl:with-param name="p_final">1</xsl:with-param>
            </xsl:call-template>
          </pre>
        </xsl:otherwise>
      </xsl:choose>
      
    </xsl:for-each>
  </xsl:template>
  
  <!-- Output summary of all fields in this class -->

  <xsl:template name="classdoc_all_field_summary">

    <xsl:if test="gjdoc:fielddoc">    
      <h1 class="classdoc-header">Field Summary</h1>
    
      <table border="1" cellspacing="0" width="100%" class="classdoc-table">
        <xsl:for-each select="gjdoc:fielddoc">
          <xsl:sort select="@name" order="ascending"/>
          <xsl:call-template name="classdoc_field_summary_tr"/>  
        </xsl:for-each>
      </table>
    </xsl:if>

    <xsl:variable name="v_superclass" select="gjdoc:superclass/@qualifiedtypename"/>
    
    <xsl:for-each select="/gjdoc:rootdoc/gjdoc:classdoc[attribute::qualifiedtypename=$v_superclass]">
      <xsl:call-template name="output_superclass_fields"/>
    </xsl:for-each>

  </xsl:template>
 
  <!-- Output summary of all methods in this class -->
  
  <xsl:template name="classdoc_all_method_summary">
    
    <xsl:if test="gjdoc:methoddoc">
      <h1 class="classdoc-header">Method Summary</h1>
    
      <table border="1" cellspacing="0" width="100%" class="classdoc-table">
        <xsl:for-each select="gjdoc:methoddoc">
          <xsl:sort select="@name" order="ascending"/>
          <xsl:call-template name="classdoc_method_summary_tr"/>  
        </xsl:for-each>
      </table>
    </xsl:if>

    <xsl:variable name="v_superclass">
      <xsl:value-of select="gjdoc:superclass/@qualifiedtypename"/>
    </xsl:variable>
    
    <xsl:for-each select="/gjdoc:rootdoc/gjdoc:classdoc[attribute::qualifiedtypename=$v_superclass]">
      <xsl:call-template name="output_superclass_methods"/>
    </xsl:for-each>
    
  </xsl:template>
  
  <!-- Output summary of all constructors in this class -->
  
  <xsl:template name="classdoc_all_constructor_summary">
    
    <xsl:if test="gjdoc:constructordoc">
      <h1 class="classdoc-header">Constructor Summary</h1>
    
      <table border="1" cellspacing="0" width="100%" class="classdoc-table">
        <xsl:for-each select="gjdoc:constructordoc">
          <xsl:sort select="@name" order="ascending"/>
          <xsl:call-template name="classdoc_method_summary_tr"/>  
        </xsl:for-each>
      </table>
    </xsl:if>

  </xsl:template>
  
  <!-- Output summary of a single field -->
  
  <xsl:template name="classdoc_field_summary_tr">
    
    <tr>

      <!-- Left table cell: Modifiers and Return Type  -->
      
      <td width="1%" align="right" valign="top" class="no-border-r">
        <code>
          <xsl:call-template name="output_modifiers_summary"/>
          <xsl:call-template name="link_to_class_full">
            <xsl:with-param name="p_qualifiedname">
              <xsl:value-of select="gjdoc:type/@qualifiedtypename"/>
            </xsl:with-param>
          </xsl:call-template>
          <xsl:value-of select="gjdoc:type/@dimension"/>
        </code>
      </td>

      <!-- Right table cell: name and short description  -->
      
      <td align="left" valign="top" class="with-border">
        <!-- Method signature -->
        
        <!-- Link to field definition -->
          
        <code>
          <a href="{concat('#',@name)}"><xsl:value-of select="@name"/></a>
        </code>

        <!-- Brief description of field -->
        
        <br/>
        <blockquote class="classdoc-summary-comment">
          <xsl:for-each select="gjdoc:firstSentenceTags/node()">
            <xsl:copy-of select="."/>
          </xsl:for-each>
        </blockquote>
      </td>

    </tr>

  </xsl:template>
  
  <xsl:template name="output_modifiers">
    <xsl:if test="gjdoc:isNative">
      <xsl:text>native </xsl:text>
    </xsl:if>
    <xsl:if test="gjdoc:isStatic">
      <xsl:text>static </xsl:text>
    </xsl:if>
    <xsl:if test="gjdoc:isFinal">
      <xsl:text>final </xsl:text>
    </xsl:if>
    <xsl:if test="gjdoc:isAbstract">
      <xsl:text>abstract </xsl:text>
    </xsl:if>
    <xsl:if test="gjdoc:isTransient">
      <xsl:text>transient </xsl:text>
    </xsl:if>
    <xsl:if test="gjdoc:isVolatile">
      <xsl:text>volatile </xsl:text>
    </xsl:if>
    <xsl:if test="gjdoc:isSynchronized">
      <xsl:text>synchronized </xsl:text>
    </xsl:if>
  </xsl:template>
  
  <xsl:template name="output_modifiers_summary">
    <xsl:if test="gjdoc:isStatic">
      <xsl:text>static </xsl:text>
    </xsl:if>
    <xsl:if test="gjdoc:isAbstract">
      <xsl:text>abstract </xsl:text>
    </xsl:if>
    <xsl:if test="gjdoc:isTransient">
      <xsl:text>transient </xsl:text>
    </xsl:if>
    <xsl:if test="gjdoc:isVolatile">
      <xsl:text>volatile </xsl:text>
    </xsl:if>
    <xsl:if test="gjdoc:isSynchronized">
      <xsl:text>synchronized </xsl:text>
    </xsl:if>
  </xsl:template>
  
  <!-- Output summary of a single method or constructor -->
  
  <xsl:template name="classdoc_method_summary_tr">
    
    <tr>
      
      <!-- Left table cell: Modifiers and Return Type  -->
      
      <xsl:if test="gjdoc:isMethod">
        <td width="1%" align="right" valign="top" class="no-border-r">
          <code>
            <xsl:call-template name="output_modifiers_summary"/>
            <xsl:text> </xsl:text>
            <xsl:call-template name="link_to_class_full">
              <xsl:with-param name="p_qualifiedname">
                <xsl:value-of select="gjdoc:returns/@qualifiedtypename"/>
              </xsl:with-param>
            </xsl:call-template>
            <xsl:value-of select="gjdoc:returns/@dimension"/>
          </code>
        </td>
      </xsl:if>
      
      <!-- Right table cell: signature and short description  -->
      
      <td align="left" valign="top" class="with-border">
        
        <!-- Method signature -->
        
        <code>
          
          <!-- Link to method definition -->
          
          <a href="{concat('#',@name,gjdoc:signature/@full)}"><xsl:value-of select="@name"/></a>
          
          <!-- Parameter List -->
          
          <xsl:text>(</xsl:text>
          <xsl:call-template name="list_parameters"/>
          <xsl:text>)</xsl:text>
        </code>
        
        <!-- Brief description of Method -->
        
        <br/>
        <blockquote class="classdoc-summary-comment">
          <xsl:for-each select="gjdoc:firstSentenceTags/node()">
            <xsl:copy-of select="."/>
          </xsl:for-each>
        </blockquote>
      </td>
    </tr>
    
  </xsl:template>
  
  <!-- Output a list of all parameters of the current methoddoc  -->
  <xsl:template name="list_parameters">
    <xsl:for-each select="gjdoc:parameter">
      <xsl:call-template name="link_to_class_full">
        <xsl:with-param name="p_qualifiedname">
          <xsl:value-of select="@qualifiedtypename"/>
        </xsl:with-param>
      </xsl:call-template>
      <xsl:value-of select="@dimension"/>
      <xsl:text>&#160;</xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:if test="position() != last()">
        <xsl:text>, </xsl:text>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="list_parameter_details">
    <ul class="classdoc-param-comment">
      <xsl:for-each select="gjdoc:parameter">
        <li>
          <code>
            <xsl:value-of select="@name"/>
          </code>
          <xsl:text> - </xsl:text>
          <xsl:variable name="param_position">
            <xsl:value-of select="position()"/>
          </xsl:variable>
          <xsl:for-each select="../gjdoc:tags/gjdoc:tag[attribute::kind='@param'][position()=$param_position]/gjdoc:inlineTags/node()">
            <xsl:copy-of select="."/>
          </xsl:for-each>
        </li>
      </xsl:for-each>
    </ul>
  </xsl:template>
  
  <xsl:template name="output_see_tags">
    <xsl:if test="gjdoc:tags/gjdoc:tag[attribute::kind='@see']">
      <b>See Also:</b><br/>

      <ul class="classdoc-see-comment">
        <xsl:for-each select="gjdoc:tags/gjdoc:tag[attribute::kind='@see']">
          <xsl:variable name="v_see" select="normalize-space(.)"/>
          <xsl:variable name="v_class" select="normalize-space(substring-before(., '#'))"/>
          <xsl:variable name="v_anchor" select="normalize-space(substring-after(., '#'))"/>
          <xsl:variable name="v_plain" select="document('index.xml', /)/gjdoc:rootdoc/gjdoc:classdoc[attribute::name=$v_see]/@qualifiedtypename"/>
          <xsl:variable name="v_plainclass" select="document('index.xml', /)/gjdoc:rootdoc/gjdoc:classdoc[attribute::name=$v_class]/@qualifiedtypename"/>
          <li>
            <code>
              <xsl:choose>
                <xsl:when test="starts-with(., '#')">
                  <a href="{$v_see}"><xsl:value-of select="$v_anchor"/></a>
                </xsl:when>
                <xsl:when test="document('index.xml', /)/gjdoc:rootdoc/gjdoc:classdoc[attribute::qualifiedtypename=$v_class]">
                  <a href="{concat($v_class, '.html#', $v_anchor)}"><xsl:value-of select="concat($v_class, '.', $v_anchor)"/></a>
                </xsl:when>
                <xsl:when test="$v_plain">
                  <a href="{concat($v_plain, '.html#', $v_anchor)}"><xsl:value-of select="$v_see"/></a>
                </xsl:when>
                <xsl:when test="$v_plainclass">
                  <a href="{concat($v_plainclass, '.html#', $v_anchor)}"><xsl:value-of select="$v_see"/></a>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:copy-of select="."/>
                </xsl:otherwise>
              </xsl:choose>
            </code>
          </li>
        </xsl:for-each>
      </ul>
    </xsl:if>
  </xsl:template>

  <!-- Output details of all fields in this class -->
  
  <xsl:template name="classdoc_field_details">

    <a name="{@name}"/>

    <h3><xsl:value-of select="@name"/></h3>
    
    <code>
      <xsl:value-of select="gjdoc:access/@scope"/>
      <xsl:text> </xsl:text>
      <xsl:call-template name="output_modifiers"/>
      <xsl:value-of select="gjdoc:type/@typename"/>
      <xsl:value-of select="gjdoc:type/@dimension"/>
      <xsl:text> </xsl:text>
      <b><xsl:value-of select="@name"/></b>
    </code>

    <p/>
    
    <!-- Full comment text -->
    
    <div class="classdoc-comment-body">
      <xsl:for-each select="gjdoc:inlineTags/node()">
        <xsl:copy-of select="."/>
      </xsl:for-each>
    </div>

    <p/>

    <xsl:call-template name="output_since_tags"/>
    <xsl:call-template name="output_author_tags"/>

    <!-- See Also -->

    <xsl:call-template name="output_see_tags"/>

    <hr/>

  </xsl:template>

  
  <!-- Output details of all methods in this class -->
  
  <xsl:template name="classdoc_method_details">
    
    <a name="{concat(@name,gjdoc:signature/@full)}"/>
    
    <h3><xsl:value-of select="@name"/></h3>
    
    <code>
      <xsl:value-of select="gjdoc:access/@scope"/>
      <xsl:text> </xsl:text>
      <xsl:call-template name="output_modifiers"/>
      <xsl:value-of select="gjdoc:returns/@typename"/><xsl:value-of select="gjdoc:returns/@dimension"/>
      <xsl:text> </xsl:text>
      <b><xsl:value-of select="@name"/></b>
      <xsl:text>(</xsl:text>
      <xsl:call-template name="list_parameters"/>
      <xsl:text>)</xsl:text>
    </code>
    
    <p/>
    
    <!-- Full comment text -->
    
    <div class="classdoc-comment-body">
      <xsl:for-each select="gjdoc:inlineTags/node()">
        <xsl:copy-of select="."/>
      </xsl:for-each>
    </div>

    <p/>

    <xsl:call-template name="output_since_tags"/>
    <xsl:call-template name="output_author_tags"/>
    
    <xsl:if test="gjdoc:parameter">
      <b>Parameters:</b><br/>
      <xsl:call-template name="list_parameter_details"/>
    </xsl:if>
    
    <xsl:if test="gjdoc:tags/gjdoc:tag[attribute::kind='@return']">
      <b>Returns:</b><br/>
      <ul>
        <li>
          <xsl:for-each select="gjdoc:tags/gjdoc:tag[attribute::kind='@return']">
            <xsl:copy-of select="."/>
          </xsl:for-each>
        </li>
      </ul>
    </xsl:if>
    
    <xsl:if test="gjdoc:tags/gjdoc:tag[attribute::kind='@throws']">
      <b>Throws:</b><br/>
      <ul>
        <xsl:for-each select="gjdoc:tags/gjdoc:tag[attribute::kind='@throws']">
          <li>
            <code>
              <xsl:call-template name="link_to_class">
                <xsl:with-param name="p_qualifiedname">
                  <xsl:value-of select="gjdoc:exception/@qualifiedtypename"/>
                </xsl:with-param>
                <xsl:with-param name="p_name">
                  <xsl:value-of select="gjdoc:exception/@typename"/>
                </xsl:with-param>
              </xsl:call-template>
            </code>
            <xsl:text> - </xsl:text>
            <xsl:for-each select="gjdoc:inlineTags/node()">
              <xsl:copy-of select="."/>
            </xsl:for-each>
          </li>
        </xsl:for-each>
      </ul>
    </xsl:if>
    
    <xsl:call-template name="output_see_tags"/>

    <hr/>
    
  </xsl:template>
  
  <!-- If the given class is also included, create a link to it. -->
  <!-- Otherwise output the qualified name in plain text. -->
  
  <xsl:template name="link_to_class_full">
    <xsl:param name="p_qualifiedname"/>
    <xsl:choose>
      <xsl:when test="document('index.xml', /)/gjdoc:rootdoc/gjdoc:classdoc[attribute::qualifiedtypename=$p_qualifiedname]">
        <a href="{concat($p_qualifiedname,'.html')}">
          <xsl:value-of select="$p_qualifiedname"/>
        </a>
      </xsl:when>
      <xsl:when test="$refdocs1 and document(concat($refdocs1, '/descriptor.xml'), /)//gjdoc:class[attribute::qualifiedtypename=$p_qualifiedname]">
        <a href="{concat($refdocs1, '/', $p_qualifiedname, '.html')}">
          <xsl:value-of select="$p_qualifiedname"/>
        </a>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$p_qualifiedname"/>
      </xsl:otherwise>
    </xsl:choose> 
  </xsl:template>
  
  <!-- If the given class is also included, create a link to it. -->
  <!-- Otherwise output the qualified name in plain text. -->
  
  <xsl:template name="link_to_class">
    <xsl:param name="p_qualifiedname"/>
    <xsl:param name="p_name"/>
    <xsl:choose>
      <xsl:when test="document('index.xml', /)/gjdoc:rootdoc/gjdoc:classdoc[attribute::qualifiedtypename=$p_qualifiedname]">
        <a href="{concat($p_qualifiedname,'.html')}" title="{$p_qualifiedname}"><xsl:value-of select="$p_name"/></a>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$p_qualifiedname"/>
      </xsl:otherwise>
    </xsl:choose> 
  </xsl:template>
  
  <!-- For every classdoc node found in the source XML, create a corresponding -->
  <!-- class rootdoc HTML file. -->
  
  <xsl:template name="create_classdoc">

    <xsl:if test="$verbose=1">
      <xsl:message>
        <xsl:text>  </xsl:text>
        <xsl:value-of select="@qualifiedtypename"/>
        <xsl:text>...</xsl:text>
      </xsl:message>
    </xsl:if>

    <xsl:variable name="filename">
      <xsl:value-of select="concat(@qualifiedtypename,'.html')"/>
    </xsl:variable>

    <xsl:variable name="v_sub_xml_filename">
      <xsl:value-of select="concat(@qualifiedtypename,'.xml')"/>
    </xsl:variable>  
    
    <xsl:document href="{concat($targetdir,'/',$filename)}"
      method="html"
      encoding="ISO-8859-1"
      doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
      doctype-system="http://www.w3.org/TR/html4/loose.dtd"
      indent="no">
      
      <html>
        <head>
          <xsl:call-template name="include_common"/>
          <title>
            <xsl:value-of select="concat($windowtitle, ' - ', @name)"/>
          </title>
        </head>
        <body bgcolor="white" onload="top.document.title = document.title;">

          <xsl:call-template name="classdoc_header"/>
          <xsl:call-template name="classdoc_all_field_summary">
            <xsl:with-param name="v_sub_xml_filename">
              <xsl:value-of select="$v_sub_xml_filename"/>
            </xsl:with-param>
          </xsl:call-template>
          <xsl:call-template name="classdoc_all_constructor_summary">
            <xsl:with-param name="v_sub_xml_filename">
              <xsl:value-of select="$v_sub_xml_filename"/>
            </xsl:with-param>
          </xsl:call-template>
          <xsl:call-template name="classdoc_all_method_summary">
            <xsl:with-param name="v_sub_xml_filename">
              <xsl:value-of select="$v_sub_xml_filename"/>
            </xsl:with-param>
          </xsl:call-template>

          <xsl:if test="gjdoc:fielddoc">
            <h1 class="classdoc-header">Field Details</h1>
          
            <xsl:for-each select="gjdoc:fielddoc">
              <xsl:sort select="@name" order="ascending"/>
              <xsl:call-template name="classdoc_field_details"/>
            </xsl:for-each>
          </xsl:if>
                   
          <xsl:if test="gjdoc:constructordoc">
            <h1 class="classdoc-header">Constructor Details</h1>
          
            <xsl:for-each select="gjdoc:constructordoc">
              <xsl:sort select="gjdoc:signature/@full" order="ascending"/>
              <xsl:call-template name="classdoc_method_details"/>
            </xsl:for-each>
          </xsl:if>

          <xsl:if test="gjdoc:methoddoc">
            <h1 class="classdoc-header">Method Details</h1>
          
            <xsl:for-each select="gjdoc:methoddoc">
              <xsl:sort select="@name" order="ascending"/>
              <xsl:sort select="gjdoc:signature/@full" order="ascending"/>
              <xsl:call-template name="classdoc_method_details"/>
            </xsl:for-each>
          </xsl:if>

          <xsl:call-template name="output_copyright_footer"/>

        </body>
      </html>
    </xsl:document>
  </xsl:template>

  <xsl:template name="output_superclass_fields">
    <xsl:if test="gjdoc:fielddoc">
      
      <h3 class="classdoc-subheader">Fields inherited from class <xsl:value-of select="@name"/></h3>

      <xsl:for-each select="gjdoc:fielddoc">
        <xsl:sort select="@name" order="ascending"/>
        <a href="{concat(../@qualifiedtypename,'.html#',name)}"><xsl:value-of select="@name"/></a>
        <xsl:if test="position() != last()">
          <xsl:text>, </xsl:text>
        </xsl:if>
      </xsl:for-each>
      
      <p/>
    </xsl:if>

    <xsl:variable name="v_superclass">
      <xsl:value-of select="gjdoc:superclass/@qualifiedtypename"/>
    </xsl:variable>
    <xsl:for-each select="/gjdoc:rootdoc/gjdoc:classdoc[@qualifiedtypename=$v_superclass]">
      <xsl:call-template name="output_superclass_fields"/>
    </xsl:for-each>

  </xsl:template>

  <xsl:template name="output_superclass_methods">
    <xsl:if test="gjdoc:methoddoc">
      
      <h3 class="classdoc-subheader">Methods inherited from class <xsl:value-of select="@name"/></h3>

      <xsl:for-each select="gjdoc:methoddoc">
        <xsl:sort select="@name" order="ascending"/>
        <a href="{concat(../@qualifiedtypename,'.html#',@name,gjdoc:signature/@full)}"><xsl:value-of select="@name"/></a>
        <xsl:if test="position() != last()">
          <xsl:text>, </xsl:text>
        </xsl:if>
      </xsl:for-each>
      
      <p/>
    </xsl:if>

    <xsl:variable name="v_superclass">
      <xsl:value-of select="gjdoc:superclass/@qualifiedtypename"/>
    </xsl:variable>
    <xsl:for-each select="/gjdoc:rootdoc/gjdoc:classdoc[@qualifiedtypename=$v_superclass]">
      <xsl:call-template name="output_superclass_methods"/>
    </xsl:for-each>

  </xsl:template>

  <xsl:template name="create_allclasses">
    <xsl:variable name="filename">
      <xsl:value-of select="'allclasses.html'"/>
    </xsl:variable>
    <xsl:document href="{concat($targetdir,'/',$filename)}"
      method="html"
      encoding="ISO-8859-1"
      doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
      doctype-system="http://www.w3.org/TR/html4/loose.dtd">
      <html>
        <head>
          <title>
            <xsl:value-of select="concat($windowtitle, ' - All Classes')"/>
          </title>
          <xsl:call-template name="include_common"/>
        </head>
        <body>
          <h3>All Classes</h3>
          <xsl:for-each select="gjdoc:rootdoc/gjdoc:classdoc/@name">
            <xsl:sort select="." order="ascending"/>
            <a href="{concat(../@qualifiedtypename, '.html')}" target="content">
              <xsl:value-of select="."/>
            </a>
            <br/>
          </xsl:for-each>
        </body>
      </html>
    </xsl:document>
  </xsl:template>

  <xsl:template name="create_packages">
    <xsl:for-each select="gjdoc:rootdoc/gjdoc:specifiedpackage/@name">
      <xsl:sort select="." order="ascending"/>
      <xsl:variable name="filename">
        <xsl:value-of select="concat(.,'.html')"/>
      </xsl:variable>
      <xsl:message>
        <xsl:text>  </xsl:text>
        <xsl:value-of select="."/>        
        <xsl:text>...</xsl:text>
      </xsl:message>

      <xsl:document href="{concat($targetdir, '/', . ,'-summary.html')}"
        method="html"
        encoding="ISO-8859-1"
        doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
        doctype-system="http://www.w3.org/TR/html4/loose.dtd">
        
        <html>
          <head>
            <title>
              <xsl:value-of select="concat($windowtitle, ' - ', .)"/>
            </title>
            <xsl:call-template name="include_common"/>
          </head>
          <body>
            <h3><xsl:value-of select="."/></h3>
            <xsl:variable name="v_currentpackage" select="."/>
            <xsl:for-each select="/gjdoc:rootdoc/gjdoc:classdoc[gjdoc:containingPackage/@name=$v_currentpackage]">
              <xsl:sort select="@name" order="ascending"/>
              <a href="{concat(@qualifiedtypename, '.html')}" target="content">
                <xsl:value-of select="@name"/>
              </a><br/>
            </xsl:for-each>
          </body>
        </html>
      </xsl:document>

      <xsl:document href="{concat($targetdir, '/', . ,'.html')}"
        method="html"
        encoding="ISO-8859-1"
        doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
        doctype-system="http://www.w3.org/TR/html4/loose.dtd">
        
        <html>
          <head>
            <title>
              <xsl:value-of select="concat($windowtitle, ' - ', .)"/>
            </title>
            <xsl:call-template name="include_common"/>
          </head>
          <body>
            <div class="header">
              <a href="index.html" target="_top">Index (Frames)</a> |
              <a href="index_noframes.html" target="_top">Index (No Frames)</a> |
              Package |
              <a href="{concat(.,'-tree.html')}">Package Tree</a> |
              <a href="full-tree.html">Tree</a>
            </div>
            <xsl:variable name="v_currentpackage">
              <xsl:value-of select="."/>
            </xsl:variable>
            <h1 class="classdoc-header">Package <xsl:value-of select="."/></h1>
            <table border="1">
              <xsl:for-each select="/gjdoc:rootdoc/gjdoc:classdoc[gjdoc:containingPackage/@name=$v_currentpackage]">
                <xsl:sort select="@name" order="ascending"/>
                <xsl:variable name="v_currentclass">
                  <xsl:value-of select="@qualifiedtypename"/>
                </xsl:variable>
                <xsl:variable name="v_sub_xml_filename">
                  <xsl:value-of select="concat(@qualifiedtypename,'.xml')"/>
                </xsl:variable>  
                <tr>
                  <td>
                    <a href="{concat(@qualifiedtypename, '.html')}" target="content">
                      <xsl:value-of select="@name"/>
                    </a>
                  </td>
                  <td>
                    <xsl:copy-of select="document($v_sub_xml_filename,/gjdoc:rootdoc)//gjdoc:classdoc[@qualifiedtypename=$v_currentclass]/gjdoc:firstSentenceTags/node()"/>
                  </td>
                </tr>
              </xsl:for-each>
            </table>
            <xsl:call-template name="output_copyright_footer"/>
          </body>
        </html>

      </xsl:document>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="create_allpackages">
    <xsl:variable name="filename">
      <xsl:value-of select="'allpackages.html'"/>
    </xsl:variable>
    <xsl:document href="{concat($targetdir, '/', $filename)}"
      method="html"
      encoding="ISO-8859-1"
      doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
      doctype-system="http://www.w3.org/TR/html4/loose.dtd">
      <html>
        <head>
          <title>
            <xsl:value-of select="concat($windowtitle, ' - Packages')"/>
          </title>
          <xsl:call-template name="include_common"/>
        </head>
        <body>
          <h3>Packages</h3>
          <a href="allclasses.html" target="classes">All Classes</a><br/><br/>
          <xsl:for-each select="gjdoc:rootdoc/gjdoc:specifiedpackage/@name">
            <xsl:sort select="." order="ascending"/>
            <a href="{concat(., '-summary.html')}" target="classes">
              <xsl:value-of select="."/>
            </a>
            <br/>
          </xsl:for-each>
        </body>
      </html>
    </xsl:document>
  </xsl:template>

  <xsl:template name="create_index_noframes">
    <xsl:variable name="filename">
      <xsl:value-of select="'index_noframes.html'"/>
    </xsl:variable>
    <xsl:document href="{concat($targetdir, '/', $filename)}"
      method="html"
      encoding="ISO-8859-1"
      doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
      doctype-system="http://www.w3.org/TR/html4/loose.dtd">
      <html>
        <head>
          <title>
            <xsl:value-of select="concat($windowtitle, ' - Index (No Frames)')"/>
          </title>
          <xsl:call-template name="include_common"/>
        </head>
        <body>

          <h1 class="classdoc-header">All Packages</h1>
          <table border="1" width="100%">
            <xsl:for-each select="gjdoc:rootdoc/gjdoc:packagedoc">
              <tr>
                <td>
                  <a href="{concat(@name, '.html')}">
                    <xsl:value-of select="@name"/>
                  </a>
                </td>
                <td>
                  <i><xsl:copy-of select="./gjdoc:firstSentenceTags/node()"/></i>
                </td>
              </tr>
            </xsl:for-each>
          </table>
          <xsl:call-template name="output_copyright_footer"/>
        </body>
      </html>
    </xsl:document>
  </xsl:template>

  <xsl:template name="output_copyright_footer">
    <xsl:copy-of select="document($copyrightfile)"/>
    <div class="footer">
      <i>Generated on <xsl:value-of select="$now"/> by <a href="http://www.gnu.org/software/cp-tools">GNU Classpath Tools</a> (using gjdocxml2html.xsl <xsl:value-of select="$version"/>)</i>
    </div>
  </xsl:template>

  <xsl:template name="create_full_tree">
    <xsl:variable name="filename">
      <xsl:value-of select="'full-tree.html'"/>
    </xsl:variable>
    <xsl:document href="{concat($targetdir, '/', $filename)}"
      method="html"
      encoding="ISO-8859-1"
      doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
      doctype-system="http://www.w3.org/TR/html4/loose.dtd">
      <html>
        <head>
          <title>
            <xsl:value-of select="concat($windowtitle, ' - Full Tree')"/>
          </title>
          <xsl:call-template name="include_common"/>
        </head>
        <body>
          <div class="header">
            <a href="index.html" target="_top">Index (Frames)</a> |
            <a href="index_noframes.html" target="_top">Index (No Frames)</a> |
            Package |
            Package Tree |
            Tree
          </div>
          <code>
            <xsl:for-each select="/gjdoc:rootdoc/gjdoc:classdoc[@qualifiedtypename='java.lang.Object']">
              <xsl:call-template name="output_tree_recursive"/>
            </xsl:for-each>
          </code>
          <xsl:call-template name="output_copyright_footer"/>
        </body>
      </html>
    </xsl:document>
  </xsl:template>

  <xsl:template name="output_tree_indent_rec">
    <xsl:param name="p_level"/>
    <xsl:if test="$p_level&gt;0">
      <xsl:text>&#160;&#160;&#160;</xsl:text>
      <xsl:call-template name="output_tree_indent_rec">
        <xsl:with-param name="p_level"><xsl:value-of select="$p_level+-1"/></xsl:with-param>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <xsl:template name="output_tree_recursive">
    <xsl:param name="p_level" select="0"/>
    <xsl:call-template name="output_tree_indent_rec">
      <xsl:with-param name="p_level"><xsl:value-of select="$p_level"/></xsl:with-param>
    </xsl:call-template>
    <a href="{concat(@qualifiedtypename,'.html')}"><xsl:value-of select="@qualifiedtypename"/></a>
    <br/>
    <xsl:variable name="v_qualifiedname">
      <xsl:value-of select="@qualifiedtypename"/>
    </xsl:variable>
    <xsl:for-each select="/gjdoc:rootdoc/gjdoc:classdoc[gjdoc:superclass/@qualifiedtypename=$v_qualifiedname]">
      <xsl:sort select="@gjdoc:qualifiedtypename" order="ascending"/>
      <xsl:call-template name="output_tree_recursive">
        <xsl:with-param name="p_level"><xsl:value-of select="$p_level+1"/></xsl:with-param>
      </xsl:call-template>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="create_descriptor">
    <xsl:document href="{concat($targetdir, '/descriptor.xml')}"
      method="xml"
      encoding="ISO-8859-1"
      indent="no">
      <gjdoc:descriptor>
        <gjdoc:about>
          This documentation was automatically generated using gjdoc
          and gjdocxml2html.xsl <xsl:value-of select="$version"/>
          on <xsl:value-of select="$now"/>.

          <xsl:copy-of select="document($copyrightfile)"/>
        </gjdoc:about>
        <xsl:for-each select="/gjdoc:rootdoc/gjdoc:packagedoc">
          <gjdoc:package name="{@name}"/>
        </xsl:for-each>
        <xsl:for-each select="/gjdoc:rootdoc/gjdoc:classdoc">
          <gjdoc:class name="{@name}" qualifiedtypename="{@qualifiedtypename}"/>
        </xsl:for-each>
      </gjdoc:descriptor>
    </xsl:document>
  </xsl:template>

  <xsl:template name="create_index">
    <xsl:document href="{concat($targetdir, '/index.html')}"
      method="html"
      encoding="ISO-8859-1"
      doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
      doctype-system="http://www.w3.org/TR/html4/loose.dtd"
      indent="no">
      <html>
        <head>
          <title>
            <xsl:value-of select="$windowtitle"/>
          </title>
        </head>
        <frameset cols="25%,75%">
          <frameset rows="25%,75%">
            <frame src="allpackages.html" name="packages"/>
            <frame src="allclasses.html" name="classes"/>
          </frameset>
          <frame src="index_noframes.html" name="content"/>
        </frameset>
      </html>
    </xsl:document>
  </xsl:template>

  <xsl:template name="create_allclassdocs">
    <xsl:for-each select="/gjdoc:rootdoc/gjdoc:classdoc">
      <xsl:sort select="@qualifiedtypename"/>
      <xsl:variable name="v_xml_filename">
        <xsl:value-of select="concat(@qualifiedtypename, '.xml')"/>
      </xsl:variable>  
      <xsl:for-each select="document($v_xml_filename,/gjdoc:rootdoc)/gjdoc:classdoc">
        <xsl:call-template name="create_classdoc"/>
      </xsl:for-each>
    </xsl:for-each>
    <xsl:message>Done.</xsl:message>
  </xsl:template>

  <xsl:template name="include_common">
    <link rel="stylesheet" type="text/css" href="common/gjdochtml.css"/>
  </xsl:template>

  <xsl:template match="/">
    <xsl:message>Generating Index file...</xsl:message>
    <xsl:call-template name="create_index"/>
    <xsl:message>Generating Index file (No Frames)...</xsl:message>
    <xsl:call-template name="create_index_noframes"/>
    <xsl:message>Generating Descriptor file...</xsl:message>
    <xsl:call-template name="create_descriptor"/>
    <xsl:message>Generating All Classes file...</xsl:message>
    <xsl:call-template name="create_allclasses"/>
    <xsl:message>Generating All Packages file...</xsl:message>
    <xsl:call-template name="create_allpackages"/>
    <xsl:message>Generating Tree file...</xsl:message>
    <xsl:call-template name="create_full_tree"/>
    <xsl:message>Generating Package documentation...</xsl:message>
    <xsl:call-template name="create_packages"/>
    <xsl:message>Generating Class documentation...</xsl:message>
    <xsl:call-template name="create_allclassdocs"/>
  </xsl:template>

</xsl:stylesheet>
