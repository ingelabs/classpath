/* gnu.classpath.tools.Javap
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
02111-1307 USA. */

package gnu.classpath.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.StringTokenizer;

import gnu.bytecode.*;

public class Javap
{
  private boolean javap = false;
  private boolean javah = false;

  private boolean disassemble = false;
  private boolean print_line_numbers = false;
  private boolean print_local_variables = false;
  private boolean print_signatures = false;
  private boolean print_stacksize = false;
  private boolean print_number_locals = false;
  private boolean print_method_args = false;
  
  private boolean show_public = true;
  private boolean show_protected = true;
  private boolean show_private = false;

  private boolean output_jni = false;
  private boolean output_verbose = false;
  private boolean output_stubs = false;

  private boolean print_compiled_from = false;

  private static String classpath = null;
  private static String user_classpath = null;
  private static String boot_classpath = null;
  private static final String TAB = "    ";
  private static String TABBING = TAB;
  private static String CLASS_TABBING = "";
  private static final String COMMENT_START = "/*   ";
  private static final String COMMENT_END = "   */";

  private static final short MODIFIERS  = 0x01;
  private static final short EXCEPTIONS = 0x02;
  private static final short WITHSEMICOLON = 0x04;

  private String [] classes = null;
  private String output_directory = null;
  private String output_file = null;
  
  private StringWriter swriter = null;

  static
  {
    classpath = System.getProperty ("java.class.path");
    boot_classpath = System.getProperty ("sun.boot.class.path");
  }

  public int exec ()
  {
    int error = 0;

    if (classes == null)
      return 1;

    if (javah && output_file != null && output_directory != null)
      {
	System.err.println ("Error: Can't mix options -d and -o.  Try -help.");
	return 1;
      }
    if (javah)
      {
	if (output_directory != null)
	  {
	    File dir = new File (output_directory);
	    if (!dir.exists () || (dir.exists () && !dir.isDirectory ()))
	      if (!dir.mkdirs ())
		{
		  System.err.println ("Error: The directory " + dir.getPath () + " could not be created for output.");
		  return 1;
		}
	  }
	if (output_file != null)
	  {
	    File f = new File (output_file);
	    if (f.exists ())
	      if (!f.delete ())
		{
		  System.err.println ("Error: The file " + f.getPath () + " could not be deleted.");
		  return 1;
		}
	  }
      }
    
    for (int i = 0; i < classes.length; i++)
      {
	InputStream is = getInputStream (classes[i]);
	if (is == null)
	  {
	    System.err.println ("Class '" + classes[i] + "' not found");
	    error = 1;
	    continue;	  
	  }

	try 
	  {
	    ClassType classType = ClassFileInput.readClassType (is);
	    print_compiled_from = true;
	    generateOutput (classType, System.out);
	  }
	catch (ClassFormatError cfe) 
	  {
	    System.err.println ("Error: " + classes[i] + ": " + cfe.getMessage ());
	    error = 1;
	  }
	catch (IOException ioe) 
	  {
	    System.err.println ("Error: " + classes[i] + ": " + ioe.getMessage ());
	    error = 1;
	  }
      }

    return error;
  }

  public void setJavap (boolean value)
  {
    javap = value;
  }

  public void setJavah (boolean value)
  {
    javah = value;
  }

  public void setOutputDirectory (String s)
  {
    output_directory = s;
  }

  public void setOutputFile (String s)
  {
    output_file = s;
  }

  public void setOutputJNI (boolean value)
  {
    output_jni = value;
  }

  public void setOutputVerbose (boolean value)
  {
    output_verbose = value;
  }

  public void setClasses (String [] s)
  {
    classes = s;
  }

  public void setDisassemble (boolean value)
  {
    disassemble = value;
  }

  public void setPrintLineNumbers (boolean value)
  {
    print_line_numbers = value;
  }

  public void setPrintLocalVariables (boolean value)
  {
    print_local_variables = value;
  }

  public void setPrintSignatures (boolean value)
  {
    print_signatures = value;
  }

  public void setPrintStackSize (boolean value)
  {
    print_stacksize = value;
  }

  public void setPrintNumberLocals (boolean value)
  {
    print_number_locals = value;
  }

  public void setPrintMethodArguments (boolean value)
  {
    print_method_args = value;
  }

  public void setShowPublic (boolean value)
  {
    show_public = value;
    show_protected = false;
    show_private = false;
  }

  public void setShowProtected (boolean value)
  {
    show_protected = value;
    if (value)
      {
	show_public = true;
	show_private = false;
      }
  }

  public void setShowPrivate (boolean value)
  {
    show_private = value;
    if (value)
      {
	show_public = true;
	show_protected = true;
      }
  }

  public void setClasspath (String s)
  {
    user_classpath = s;
  }

  private InputStream getInputStream (String s)
  {
    InputStream is = null;

    File f = new File (s);
    if (f.exists ())
      {
	try 
	  {
	    FileInputStream fis = new FileInputStream (f);
	    return new BufferedInputStream (fis);
	  }
	catch (FileNotFoundException fe) { }
      }

    // obtain absolute path name for resource
    if (s.endsWith (".class"))
      {
	int x = s.lastIndexOf ('.');
        s = s.substring (0, x);
      }
    String classPath = s.replace('.', '/');
    classPath = classPath + ".class";
    String absClassPath = "/" + classPath;

    if (user_classpath == null)
      {
	// Class.forName requires package.class format
	String className = s.replace('/', '.');
	Class refClass;
	try 
	  {
	    refClass = Class.forName (className);
	    is =  refClass.getResourceAsStream (absClassPath);
	  } 
	catch (ClassNotFoundException e) 
	  { 
	    return null; 
	  }
	catch (Throwable le)
	  {
	    StringBuffer sbuf = new StringBuffer ();
	    if (boot_classpath != null)
	      sbuf.append (boot_classpath);
	    if (classpath != null)
	      {
		if (boot_classpath != null)
		  sbuf.append (File.pathSeparator);
		sbuf.append (classpath);
	      }
	    is = Util.getInputStream (classPath, sbuf.toString ());
	  }
      }
    else
      is = Util.getInputStream (classPath, user_classpath);
    
    return is;
      
  }

  private void generateOutput (ClassType classType, PrintStream out)
  {
    if (javap)
      {
	generateJavapOutput (classType, out);
      }
    else if (javah)
      {
	generateJavahOutput (classType, out);
      }
  }

  private void generateJavahOutput (ClassType classType, PrintStream out)
  {
    StringBuffer buf;

    if (output_verbose)
      {
	buf = new StringBuffer ();
	buf.append ("[Search path = ");
	if (user_classpath != null)
	  buf.append (user_classpath);
	else
	  {
	    if (boot_classpath != null)
	      {
		buf.append (boot_classpath);
		buf.append (File.pathSeparator);
	      }
	    buf.append (classpath);
	  }
	buf.append ("]");
	out.println (buf.toString ());
      }

    if (output_directory != null)
      {
	String className = classType.getName ();
	className = className.replace ('.', '_');
	className = className.replace ('$', '_');
	className = className + ".h";
	File dir = new File (output_directory);

	// fatal error
	if (!dir.exists ())
	  return;

	File f = new File (dir, className);
	if (f.exists ())
	  if (!f.delete ())
	    {
	      System.err.println ("Error: The file " + f.getPath () + " could not be deleted.");
	      return;
	    }

	if (output_verbose)
	  {
	    buf = new StringBuffer ();
	    buf.append ("[Creating ");
	    buf.append (f.getPath ());
	    buf.append ("]");
	    out.println (buf.toString ());
	  }
	writeHeader (classType, f);
      }
    else if (output_file != null)
      {
	File f = new File (output_file);
	if (output_verbose)
	  {
	    buf = new StringBuffer ();
	    buf.append ("[Creating ");
	    buf.append (f.getPath ());
	    buf.append ("]");
	    out.println (buf.toString ());
	  }
	writeHeader (classType, f);
      }
  }

  private void writeHeader (ClassType classType, File f)
  {
    StringBuffer buf;
    String linesep = System.getProperty ("line.separator");

    boolean newfile = f.exists ();
    try 
      {
	RandomAccessFile raf = new RandomAccessFile (f, "rw");
	raf.seek (raf.length ());
	FileWriter writer = new FileWriter (raf.getFD ());

	if (!newfile)
	  {
	    buf = new StringBuffer ();
	    buf.append ("/* DO NOT EDIT THIS FILE - it is machine generated */");
	    buf.append (linesep);
	    buf.append ("#include <jni.h>");
	    buf.append (linesep);
	    writer.write (buf.toString ());
	  }

	buf = new StringBuffer ();
	buf.append ("/* Header for class ");
	String className = classType.getName ();
	className = getUnicodeName (className);
	className = escapeUnicode (className);
	className = className.replace ('$', '_');
	buf.append (className.replace ('.', '_'));
	buf.append (" */");
	buf.append (linesep);
	buf.append (linesep);
	writer.write (buf.toString ());

	String includedef = "_Included_" + className.replace ('.', '_');
	buf = new StringBuffer ();
	buf.append ("#ifndef ");
	buf.append (includedef);
	buf.append (linesep);
	buf.append ("#define ");
	buf.append (includedef);
	buf.append (linesep);
	buf.append ("#ifdef __cplusplus");
	buf.append (linesep);
	buf.append ("extern \"C\" {");
	buf.append (linesep);
	buf.append ("#endif");
	buf.append (linesep);
	writer.write (buf.toString ());

	// write any static fields
	Field field = classType.getFields ();
	while (field != null)
	  {
	    int flags = field.getModifiers ();
	    if ((flags & Access.STATIC) != 0)
	      {
		buf = new StringBuffer ();
		String fieldname = field.getSourceName ();
		fieldname = getUnicodeName (fieldname);
		fieldname = escapeUnicode (fieldname);
		if ((flags & Access.FINAL) != 0)
		  {
		    ConstantValueAttr constval = (ConstantValueAttr)Attribute.get (field, "ConstantValue");
		    if (constval == null)
		      {
//  			System.out.println ("DBG1: " + fieldname);
			buf.append ("/* Inaccessible static: ");
			buf.append (escapeUnicodeInner (fieldname));
			buf.append (" */");
			buf.append (linesep);
			writer.write (buf.toString ());
			field = field.getNext ();
			continue;
		      }
		    Object val = constval.getValue (classType.getConstants ());
		    if (val instanceof String)
		      {
			System.out.println ("DBG2: " + fieldname);
			field = field.getNext ();
			continue;
		      }

		    fieldname = className.replace ('.', '_') + "_" + fieldname;
		    buf.append ("#undef ");
		    buf.append (escapeUnicodeInner (fieldname));
		    buf.append (linesep);
		    buf.append ("#define ");
		    buf.append (fieldname);
		    buf.append (" ");

		    if (val instanceof Integer)
		      {
			Integer valint = (Integer)val;
			buf.append (valint.toString ());
			buf.append ("L");
		      }
		    else if (val instanceof Long)
		      {
			Long vallong = (Long)val;
			buf.append (vallong.toString ());
			buf.append ("LL");
		      }
		    else if (val instanceof Float)
		      {
			Float valfloat = (Float)val;
			buf.append (valfloat.toString ());
			buf.append ("f");
		      }
		    else if (val instanceof Double)
		      {
			Double valdouble = (Double)val;
			buf.append (valdouble.toString ());
			buf.append ("D");
		      }
		    else
		      System.err.println ("Unknown constant value " + val);

		    buf.append (linesep);
		  }
		else
		  {
		    buf.append ("/* Inaccessible static: ");
		    buf.append (escapeUnicodeInner (fieldname));
		    buf.append (" */");
		    buf.append (linesep);
		  }
		writer.write (buf.toString ());
	      }
	    field = field.getNext ();
	  }

	// write any native methods out
	Method method = classType.getMethods ();
	while (method != null)
	  {
	    int flags = method.getModifiers ();
	    if ((flags & Access.NATIVE) != 0)
	      {
		buf = new StringBuffer ();
		buf.append ("/*");
		buf.append (linesep);
		buf.append (" * Class:     ");
		buf.append (className.replace ('.', '_'));
		buf.append (linesep);
		buf.append (" * Method:    ");
		String methodname = getUnicodeName (method.getName ());
		methodname = escapeUnderscore (methodname);
		methodname = escapeUnicode (methodname);
		buf.append (methodname);
		buf.append (linesep);
		buf.append (" * Signature: ");
		buf.append (method.getSignature ());
		buf.append (linesep);
		buf.append (" */");
		buf.append (linesep);

		buf.append ("JNIEXPORT ");
		String returnval = getJNIType (method.getReturnType ());
		buf.append (returnval);
		buf.append (" JNICALL ");
		String methodname2 = getJNIMethodName (classType, method);
		methodname2 = escapeUnicodeInner (methodname2);
		buf.append (methodname2);
		buf.append (linesep);
		writer.write (buf.toString ());

		// begin printing JNI arguments
		buf = new StringBuffer ();
		buf.append ("  (JNIEnv *, ");
		if ((flags & Access.STATIC) != 0)
		  buf.append ("jclass");
		else
		  buf.append ("jobject");

		// if there are arguments, include those
		Type[] paramTypes = method.getParameterTypes ();
		if (paramTypes.length > 0)
		  buf.append (", ");
		for (int i = 0; i < paramTypes.length; i++)
		  {
		    String jniname = getJNIType (paramTypes[i]);
		    if (buf.length () + jniname.length () > 76)
		      {
			buf.append (linesep);
			writer.write (buf.toString ());
			buf = new StringBuffer ();
			buf.append ("   ");
		      }
		    buf.append (jniname);
		    if ((i+1) < paramTypes.length)
		      buf.append (", ");
		  }
		buf.append (");");
		buf.append (linesep);

		buf.append (linesep);
		writer.write (buf.toString ());
	      }
	    
	    method = method.getNext ();
	  }

	buf = new StringBuffer ();
	buf.append ("#ifdef __cplusplus");
	buf.append (linesep);
	buf.append ("}");
	buf.append (linesep);
	buf.append ("#endif");
	buf.append (linesep);
	buf.append ("#endif");
	buf.append (linesep);
	writer.write (buf.toString ());
	
	writer.close ();
	raf.close ();
      }
    catch (IOException ioe) 
      {
	System.err.println ("Error: Can't recover from an I/O error with the following message: " 
			    +  ioe.getMessage ());
	return;
      }
  }

  /**
   * Copied largely from gnu.bytecode.ClassTypeWriter
   */
  private String getUnicodeName (String s)
  {
    StringBuffer namebuf = new StringBuffer ();
    int len = s.length ();
    for (int i = 0; i < len; i++)
      {
	char ch = s.charAt (i);
	if (ch >= ' ' && ch < 127)
	  namebuf.append (ch);
	else
	  {
	    namebuf.append ("\\u");
	    for (int j = 4; --j >= 0; )
	      namebuf.append (Character.forDigit((ch >> (j * 4)) & 15, 16));
	  }
      }
    return namebuf.toString ();
  }
  
  private String getJNIType (Type t)
  {
    String jtype = t.getName ();
    String ntype = null;
    boolean isArray = false;

    if (jtype.indexOf ("[]") != -1)
      {
	jtype = jtype.substring (0, jtype.indexOf ("[]"));
	isArray = true;
      }

    if (jtype.equals ("void"))
      ntype = "void";
    else if (jtype.equals ("boolean"))
      ntype = "jboolean";
    else if (jtype.equals ("byte"))
      ntype = "jbyte";
    else if (jtype.equals ("char"))
      ntype = "jchar";
    else if (jtype.equals ("short"))
      ntype = "jshort";
    else if (jtype.equals ("int"))
      ntype = "jint";
    else if (jtype.equals ("long"))
      ntype = "jlong";
    else if (jtype.equals ("float"))
      ntype = "jfloat";
    else if (jtype.equals ("double"))
      ntype = "jdouble";

    if (isArray)
      {
	if (ntype == null)
	  ntype = "jobject";
	ntype = ntype + "Array";
      }

    if (ntype != null)
      return ntype;

    ntype = "jobject";

    Type stringtype = Type.getType ("java.lang.String");
    if (t.isSubtype (stringtype))
      return "jstring";
    Type throwtype = Type.getType ("java.lang.Throwable");
    if (t.isSubtype (throwtype))
      return "jthrowable";
    Type classtype = Type.getType ("java.lang.Class");
    if (t.isSubtype (classtype))
      return "jclass";

    return ntype;
  }

  private String getJNIMethodName (ClassType classType, Method m)
  {
    StringBuffer result = new StringBuffer ();

    String classname = classType.getName ();
    classname = classname.replace ('.', '_');
    result.append ("Java_");
    result.append (classname);
    result.append ("_");

    Method method = classType.getMethods ();
    int overload = 0;
    while (method != null)
      {
	int flags = method.getModifiers ();
	if ((flags & Access.NATIVE) != 0)
	  {
	    if (method.getName ().equals (m.getName ()))
	      overload++;
	  }
	method = method.getNext ();
      }

    String methodname = getUnicodeName (m.getName ());
    methodname = escapeUnderscore (methodname);
    methodname = escapeUnicode (methodname);

    result.append (methodname);

    if (overload > 1)
      {
	result.append ("__");

	StringBuffer sig = new StringBuffer (m.getSignature ());

	String signature = sig.toString ();
	int idx = signature.indexOf ('(');
	if (idx != -1)
	  sig.deleteCharAt (idx);

	signature = sig.toString ();
	idx = signature.indexOf (')');
	if (idx != -1)
	  sig.delete (idx, sig.length ());

	signature = sig.toString ();
	signature = escapeUnderscore (signature);
	signature = escapeSemicolon (signature);
	signature = escapeArray (signature);
	signature = signature.replace ('/', '_');

	result.append (signature);
      }

    return result.toString ();
  }

  /**
   * Escapes any '_' character with '_1'
   */
  private String escapeUnderscore (String s)
  {
    StringBuffer buf = new StringBuffer (s);
    int start = -1;
    while ((start = s.indexOf ('_', start+1)) != -1)
      {
	buf.replace (start, start+1, "_1");
	s = buf.toString ();
      }
    s = buf.toString ();
    return s;
  }

  /**
   * Escapes any Unicode character XXXX with '_0XXXX'
   */
  private String escapeUnicode (String s)
  {
    StringBuffer buf = new StringBuffer (s);
    int start = -1;
    while ((start = s.indexOf ("\\u", start+1)) != -1)
      {
	if (s.length () > start+5)
	  {
	    buf.replace (start, start+2, "_0");
	    s = buf.toString ();
	  }
      }
    s = buf.toString ();
    return s;
  }
  
  /**
   * Escapes any '$' with _00024
   */
  private String escapeUnicodeInner (String s)
  {
    StringBuffer buf = new StringBuffer (s);
    int start = -1;
    while ((start = s.indexOf ("$", start+1)) != -1)
      {
	buf.replace (start, start+1, "_00024");
	s = buf.toString ();
      }
    s = buf.toString ();
    return s;
  }

  /**
   * Escapes the ';' character with '_2'.
   * Useful only for descriptors, as in method signatures.
   */
  private String escapeSemicolon (String s)
  {
    StringBuffer buf = new StringBuffer (s);
    int start = -1;
    while ((start = s.indexOf (';', start+1)) != -1)
      {
	buf.replace (start, start+1, "_2");
	s = buf.toString ();
      }
    s = buf.toString ();
    return s;
  }

  /**
   * Escapes the '[' character with '_3'.
   * Useful only for descriptors, as in method signatures
   */
  private String escapeArray (String s)
  {
    StringBuffer buf = new StringBuffer (s);
    int start = -1;
    while ((start = s.indexOf ('[', start+1)) != -1)
      {
	buf.replace (start, start+1, "_3");
	s = buf.toString ();
      }
    s = buf.toString ();
    return s;
  }

  private void generateJavapOutput (ClassType classType, PrintStream out)
  {
    StringBuffer buf = new StringBuffer ();

    try 
      {
	SourceFileAttr sourceFile = (SourceFileAttr)Attribute.get (classType, "SourceFile");
	if (sourceFile != null)
	  {
	    if (print_compiled_from)
	      {
		print_compiled_from = false;
		buf.append ("Compiled from ");
		buf.append (sourceFile.getSourceFile ());
		out.println (buf.toString ());
	      }
	  }
      }
    catch (ClassCastException cce) { }
    
    buf = new StringBuffer ();

    buf.append (CLASS_TABBING);
    int flags = classType.getModifiers ();
    if ((flags & Access.PUBLIC) != 0)      buf.append("public ");
    if ((flags & Access.PRIVATE) != 0)     buf.append("private ");
    if ((flags & Access.PROTECTED) != 0)   buf.append("protected ");
    if ((flags & Access.STATIC) != 0)      buf.append("static ");
    if ((flags & Access.FINAL) != 0)       buf.append("final ");
    /* synchronized flag here is for super bit... */
    //      if ((flags & Access.SYNCHRONIZED) != 0)buf.append(" synchronized");
    if ((flags & Access.VOLATILE) != 0)    buf.append("volatile ");
    if ((flags & Access.TRANSIENT) != 0)   buf.append("transient ");
    if ((flags & Access.NATIVE) != 0)      buf.append("native ");
    if (!classType.isInterface ())
      if ((flags & Access.ABSTRACT) != 0)    buf.append("abstract ");
    if ((flags & Access.INTERFACE) != 0)   buf.append("interface ");
    
    if (!classType.isInterface ())
      buf.append ("class ");

    String classtypename = classType.getName ();
    classtypename = escapeInnerClass (classtypename);
    buf.append (classtypename);
    
    boolean show_super = false;
    ClassType superType = classType.getSuperclass ();
    if (superType != null)
      {
	if (classType.isInterface ())
	  if (superType.getName ().equals ("java.lang.Object"))
	    show_super = true;

	if (! show_super)
	  {
	    buf.append (" extends ");
	    String supertypename = superType.getName ();
	    supertypename = escapeInnerClass (supertypename);
	    buf.append (supertypename);
	  }
      }

    ClassType[] interfaces = classType.getInterfaces ();
    if (interfaces != null)
      {
	if (interfaces.length > 0)
	  {
	  if (classType.isInterface ())
	    buf.append (" extends ");
	  else
	    buf.append (" implements ");
	  }
	for (int i = 0; i < interfaces.length; i++)
	  {
	    String interfacename = interfaces[i].getName ();
	    interfacename = escapeInnerClass (interfacename);
	    buf.append (interfacename);
	    if ((i+1) < interfaces.length)
	      buf.append (", ");
	  }
      }
    if (!classType.isInterface ())
      buf.append (" {");
    else
      buf.append (" "); // done only to match jdk javap on diff
    out.println (buf.toString());
    
    if ((flags & Access.SYNCHRONIZED) == 0)
      out.println ("    /* ACC_SUPER bit NOT set */");

    if (classType.isInterface ())
      out.println (CLASS_TABBING + "{");
    
    // output fields
    buf = null;
    Field field = classType.getFields ();
    while (field != null)
      {
	buf = new StringBuffer();	
	buf.append (TABBING);
	flags = field.getModifiers ();
	if ((flags & Access.PUBLIC) != 0)
	  {
	    buf.append("public ");
	    if (!show_public)
	      {
		field = field.getNext ();
		continue;
	      }
	  }
	if ((flags & Access.PRIVATE) != 0)     
	  {
	    buf.append("private ");
	    if (!show_private)
	      {
		field = field.getNext ();
		continue;
	      }
	  }
	if ((flags & Access.PROTECTED) != 0)   
	  {
	    buf.append("protected ");
	    if (!show_protected)
	      {
		field = field.getNext ();
		continue;
	      }
	  }
	if ((flags & Access.STATIC) != 0)      buf.append("static ");
	if ((flags & Access.FINAL) != 0)       buf.append("final ");
	if ((flags & Access.SYNCHRONIZED) != 0)buf.append("synchronized ");
	if ((flags & Access.VOLATILE) != 0)    buf.append("volatile ");
	if ((flags & Access.TRANSIENT) != 0)   buf.append("transient ");
	if ((flags & Access.NATIVE) != 0)      buf.append("native ");
	if ((flags & Access.ABSTRACT) != 0)    buf.append("abstract ");
	if ((flags & Access.INTERFACE) != 0)   buf.append("interface ");
	String fieldtypename = field.getType ().getName ();
	fieldtypename = escapeInnerClass (fieldtypename);
	buf.append (fieldtypename);
	buf.append (" ");
	buf.append (field.getSourceName ());
	buf.append (";");
	out.println (buf.toString ());
	
	if (print_signatures)
	  {
	    buf = new StringBuffer ();
	    buf.append (TABBING);
	    buf.append (TAB);
	    buf.append (COMMENT_START);
	    buf.append (field.getSignature ());
	    buf.append (COMMENT_END);
	    out.println (buf.toString ());
	  }
	field = field.getNext ();
      }
    
    // output methods
    buf = null;
    Method method = classType.getMethods ();
    while (method != null)
      {
	buf = new StringBuffer();
	buf.append (TABBING);
	
	flags = 1;
	flags |= MODIFIERS | EXCEPTIONS | WITHSEMICOLON;
	String methodStr = getMethod (classType, method, flags);
	if (methodStr == null)
	  {
	    method = method.getNext ();
	    continue;
	  }
	buf.append (getMethod (classType, method, flags));
	out.println (buf.toString ());
	buf = null;

	if (print_signatures)
	  {
	    buf = new StringBuffer ();
	    buf.append (TABBING);
	    buf.append (TAB);
	    buf.append (COMMENT_START);
	    buf.append (method.getSignature ());
	    buf.append (COMMENT_END);
	    out.println (buf.toString ());
	    buf = null;
	  }
	
	if (print_stacksize || print_number_locals || print_method_args)
	  {
	    CodeAttr code = method.getCode ();
	    if (code != null)
	      {
		buf = new StringBuffer ();
		buf.append (TABBING);
		buf.append (TAB);
		buf.append (COMMENT_START);
		if (print_stacksize)
		  {
		    buf.append ("Stack=");
		    buf.append (code.getMaxStack ());
		  }
		if (print_number_locals)
		  {
		    if (print_stacksize)
		      buf.append (", ");
		    buf.append ("Locals=");
		    buf.append (code.getMaxLocals ());
		  }
		if (print_method_args)
		  {
		    if (print_stacksize || print_number_locals)
		      buf.append (", ");
		    buf.append ("Args_size=");
		    Type[] paramTypes = method.getParameterTypes ();
		    String methodName = method.getName ();
		    int add = 1;
		    if (methodName.equals ("<clinit>"))
		      add = 0;
		    int args = paramTypes.length + add;
		    buf.append (args);
		  }
		buf.append (COMMENT_END);
		out.println (buf.toString ());
		buf = null;
	      }
	  }
	
	method = method.getNext ();
      }
    
    InnerClassesAttr innerClassAttr = (InnerClassesAttr)Attribute.get (classType, "InnerClasses");
    if (innerClassAttr != null)
      {
	String[][] innerClassNames = innerClassAttr.getClassNames ();
//  	System.out.println (TABBING + "DBG: " + classType.getName () + " THIS");
//  	for (int i = 0; i < innerClassNames.length; i++)
//  	  {
//  	    System.out.println (TABBING + "DBG: " + innerClassNames[i][0]);
//  	  }

	TABBING = TABBING + TAB;
	CLASS_TABBING = CLASS_TABBING + TAB;
	for (int i = 0; i < innerClassNames.length; i++)
	  {
	    String tclassName = innerClassNames[i][0];
	    tclassName = tclassName.replace ('/', '.');
	    if (tclassName.indexOf (classType.getName ()) == -1)
	      continue;
	    if (tclassName.length () <= classType.getName ().length ())
	      continue;
	    String test = escapeInnerClass (tclassName);
	    if (test.indexOf ('$') != -1)
		continue;

	    InputStream is = getInputStream (innerClassNames[i][0]);
	    if (is == null)
	      {
		System.err.println ("Class '" + innerClassNames[i][0] + "' not found");
		continue;	  
	      }
	    
	    try 
	      {
		ClassType innerclassType = ClassFileInput.readClassType (is);
		flags = innerclassType.getModifiers ();

		StringTokenizer st = new StringTokenizer (innerClassNames[i][1]);
		while (st.hasMoreTokens ())
		  {
		    String token = st.nextToken ();
		    if (token.equals ("public"))
		      flags |= Access.PUBLIC;
		    else if (token.equals ("private"))
		      flags |= Access.PRIVATE;
		    else if (token.equals ("protected"))
		      flags |= Access.PROTECTED;
		    else if (token.equals ("static"))
		      flags |= Access.STATIC;
		    else if (token.equals ("final"))
		      flags |= Access.FINAL;
		    else if (token.equals ("abstract"))
		      flags |= Access.ABSTRACT;
		    else if (token.equals ("interface"))
		      flags |= Access.INTERFACE;
		  }
		innerclassType.setModifiers (flags);		
		generateOutput (innerclassType, out);
	      }
	    catch (ClassFormatError cfe) 
	      {
		System.err.println ("Error: " + innerClassNames[i][0] + ": " + cfe.getMessage ());
	      }
	    catch (IOException ioe) 
	      {
		System.err.println ("Error: " + innerClassNames[i][0] + ": " + ioe.getMessage ());
	      }
	  }
	TABBING = TABBING.substring (0, TABBING.length () - TAB.length ());
	CLASS_TABBING = CLASS_TABBING.substring (0, CLASS_TABBING.length () - TAB.length ());
      }

    // end of class
    out.println (CLASS_TABBING + "}");
    
    method = classType.getMethods ();
    while (method != null)
      {
	if (disassemble || print_line_numbers || print_local_variables)
	  {
	    try
	      {
		CodeAttr code = (CodeAttr)Attribute.get (method, "Code");
		if (code != null)
		  {
		    if (disassemble)
		      {
			buf = new StringBuffer ();
			String methodStr = getMethod (classType, method, 0);
			if (methodStr != null)
			  {
			    buf.append ("Method ");
			    buf.append (methodStr);
			    out.println ("");
			    out.println (buf.toString ());
			  }
			int offset = 0;
			int length = code.getCodeLength ();

			buf = new StringBuffer ();
			ClassTypeWriter writer = getClassTypeWriter (buf, classType);
			code.disAssemble (writer, offset, length);
			writer.flush ();
			writer.close ();
			// swriter is set in getClasTypeWriter
			if (swriter != null)
			  out.print (swriter.toString ());
		      }
		    
		    if (print_line_numbers)
		      {
			LineNumbersAttr lineNumbers = (LineNumbersAttr)Attribute.get (code, "LineNumberTable");
			if (lineNumbers != null)
			  {
			    buf = new StringBuffer ();
			    String methodStr = getMethod (classType, method, 0);
			    if (methodStr != null)
			      {
				buf.append ("Line numbers for method ");
				buf.append (methodStr);
				out.println ("");
				out.println (buf.toString ());
				int linenumber_count = lineNumbers.getLineCount ();
				short[] linenumber_table = lineNumbers.getLineNumberTable ();
				for (int i = 0; i < linenumber_count; i++)
				  {
				    out.print ("   line ");
				    out.print (linenumber_table[2 * i + 1] & 0xFFFF);
				    out.print (": ");
				    out.println (linenumber_table[2 * i] & 0xFFFF);
				  }
			      }
			  }
		      }

		    if (print_local_variables)
		      {
			try 
			  {
			    LocalVarsAttr vars = (LocalVarsAttr)Attribute.get (code, "LocalVariableTable");
			    if (vars != null)
			      {
				buf = new StringBuffer ();
				String methodStr = getMethod (classType, method, 0);
				if (methodStr != null)
				  {
				    buf.append ("Local variables for method ");
				    buf.append (methodStr);
				    out.println ("");
				    out.println (buf.toString ());
				    VarEnumerator varEnum = vars.allVars ();
				    Variable var = varEnum.nextVar ();
				    while (var != null)
				      {
					buf = new StringBuffer ();
					buf.append ("   ");
					buf.append (var.getType ().getName ());
					buf.append (" ");
					buf.append (var.getName ());
					buf.append ("  pc=");
					buf.append (var.getStartPC ());
					buf.append (", length=");
					buf.append (var.getEndPC () - var.getStartPC ());
					buf.append (", slot=");
					buf.append (var.getOffset ());
					out.println (buf.toString ());
					var = varEnum.nextVar ();
				      }
				  }
			      }
			  }
			catch (ClassCastException cce) { }
		      }

		  }  // if (code != null)
	      }
	    catch (ClassCastException cce) { }
	  }
	
	method = method.getNext ();
      }
  }
  
  private String getMethod (ClassType classType, Method method, int modifiers)
  {
    StringBuffer buf = new StringBuffer ();

    int flags = method.getModifiers ();

    if ((modifiers & MODIFIERS) != 0)
      {
	if ((flags & Access.PUBLIC) != 0)
	  {
	    buf.append("public ");
	    if (!show_public)
	      {
		return null;
	      }
	  }
	if ((flags & Access.PRIVATE) != 0)
	  {
	    buf.append("private ");
	    if (!show_private)
	      {
		return null;
	      }
	  }
	if ((flags & Access.PROTECTED) != 0)
	  {
	    buf.append("protected ");
	    if (!show_protected)
	      {
		return null;
	      }
	  }
	if ((flags & Access.STATIC) != 0)      buf.append("static ");
	if ((flags & Access.FINAL) != 0)       buf.append("final ");
	if ((flags & Access.SYNCHRONIZED) != 0)buf.append("synchronized ");
	if ((flags & Access.VOLATILE) != 0)    buf.append("volatile ");
	if ((flags & Access.TRANSIENT) != 0)   buf.append("transient ");
	if ((flags & Access.NATIVE) != 0)      buf.append("native ");
	if ((flags & Access.ABSTRACT) != 0)    buf.append("abstract ");
	if ((flags & Access.INTERFACE) != 0)   buf.append("interface ");
      }

    String methodName = method.getName ();
    if (methodName.indexOf ('$') != methodName.length () - 1)
      methodName = escapeInnerClass (methodName);

    // if a constructor
    if (methodName.equals ("<init>"))
      {
	String classtypename = classType.getName ();
	classtypename = escapeInnerClass (classtypename);
	buf.append (classtypename);
      }
    else if (methodName.equals ("<clinit>"))
      {
	if ((modifiers & MODIFIERS) != 0)
	  buf.append ("{}");
	else
	  buf.append ("static {}");
      }
    else
      {
	Type returnType = method.getReturnType ();
	String returntypename = returnType.getName ();
	returntypename = escapeInnerClass (returntypename);
	buf.append (returntypename);
	buf.append (" ");
	buf.append (methodName);
      }
    
    if (!methodName.equals ("<clinit>"))
      {
	buf.append ("(");
	Type[] paramTypes = method.getParameterTypes ();
	for (int i = 0; i < paramTypes.length; i++)
	  {
	    String paramtypename = paramTypes[i].getName ();
	    paramtypename = escapeInnerClass (paramtypename);
	    buf.append (paramtypename);
	    
	    if ((i+1) < paramTypes.length)
	      buf.append (", ");
	  }
	buf.append (")");
      }

    if ((modifiers & EXCEPTIONS) != 0)
      {
	ClassType[] exceptions = method.getExceptions ();
	if (exceptions != null && exceptions.length > 0)
	  {
	    buf.append (" throws ");
	    for (int i = 0; i < exceptions.length; i++)
	      {
		String exceptionname = exceptions[i].getName ();
		exceptionname = escapeInnerClass (exceptionname);
		buf.append (exceptionname);
		if ((i+1) < exceptions.length)
		  buf.append (", ");
	      }
	  }
      }

    if ((modifiers & WITHSEMICOLON) != 0)
      buf.append (";");

    return buf.toString ();
  }

  private ClassTypeWriter getClassTypeWriter (StringBuffer buf, ClassType ctype)
  {
    swriter = new StringWriter ();
    PrintWriter pwriter = new PrintWriter (swriter, true);
    ClassTypeWriter cwriter = new ClassTypeWriter (ctype, pwriter, 0);
    return cwriter;
  }

  /**
   * Output for class names of the form foo$1 should
   * not be made foo.1.
   */
  private String escapeInnerClass (String s)
  {
    int idx = s.lastIndexOf ('$');
    String anon = s.substring (idx+1, s.length ());
    boolean is_anon = false;
    try 
      {
	Integer.parseInt (anon);
	is_anon = true;
        s = s.substring (0, idx);
      }
    catch (NumberFormatException nfe) { }
    s = s.replace ('$', '.');
    if (is_anon)
      s = s + "$" + anon;

    return s;
  } 
}
