/*************************************************************************
/* StreamTokenizer.java -- Class to parse streams.
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

package java.io;

/**
  * This class parses streams of characters into tokens.  There are a
  * million-zillion flags that can be set to control the parsing, as 
  * described under the various method headings.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class StreamTokenizer
{
// I'd really like to strangle the person who designed this class

/*************************************************************************/

/*
 * Class Variables
 */

/**
  * This is the increment size we use when allocating our internal token buffer
  */
private static final int BUFFER_INCREMENT_SIZE = 32;

/**
  * Easy to remember lookup table index constants
  */
private static final int WHITE = 0;
private static final int ALPHA = 1;
private static final int NUMERIC = 2;
private static final int QUOTE = 3;
private static final int COMMENT = 4; 

/**
  * Indicates that the end of the stream was reached
  */
public static final int TT_EOF = -1;

/**
  * Indicates that the end of the line has been reached
  */
public static final int TT_EOL = '\n';

/**
  * Indicates that the token encountered was a number
  */
public static final int TT_NUMBER = -2;

/**
  * Indicates that the token encountered was a word
  */
public static final int TT_WORD = -3; 

/**
  * Indicates that we have never read a token (internally used value)
  */
private static final int TT_NONE = -4;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This value contains the type of the token that was last read.  The
  * rules are as follows:
  * <ul>
  * <li>For a token consisting of a single ordinary character, this is the 
  *     value of that character.
  * <li>For a quoted string, this is the value of the quote character
  * <li>For a word, this is TT_WORD
  * <li>For a number, this is TT_NUMBER
  * <li>For the end of the line, this is TT_EOL
  * <li>For the end of the stream, this is TT_EOF
  * </ul>
  */
public int ttype = TT_NONE;

/**
  * For tokens of type TT_WORD or quoted strings, this value contains the
  * token read as a <code>String</code> not including any quote characters.
  */
public String sval;

/**
  * For tokens of type TT_NUMBER, this variable contains the value of that
  * number as a double.
  */
public double nval;

/**
  * This is the internal table of character attribtues.  The flags
  * are:
  * <ul>
  * <li>0 - Whitespace
  * <li>1 - Alphabetic
  * <li>2 - Numeric
  * <li>3 - Quote
  * <li>4 - Comment
  * </ul>
  */
private boolean lookup_table[][] = new boolean[256][5];

/**
  * Indicates whether or not we should handle C style comments
  */
private boolean handle_c_comments;

/**
  * Indicates whether or not we should handle C++ style (//) comments
  */
private boolean handle_cplusplus_comments;

/**
  * Indicates whether words are converted to lower case
  */
private boolean lower_case_mode;

/**
  * Indicates whether or not the EOL charcter is significant
  */
private boolean eol_is_significant;

/**
  * This is the LineNumberReader used for keeping track of line numbers
  */
private LineNumberReader lnr;

/**
  * This is the PushbackReader we use for unreading prefetched bytes
  */
private PushbackReader pbr;

/**
  * This is the buffer that holds the chars that make up the token
  * we just read.
  */
private char[] token_buf;

/**
  * This is the position in the token_buf at which the next character
  * will be stored
  */
private int bufpos = 0;

/**
  * This value indicates whether we have already pushed back the current
  * token so some joker doesn't do it twice in a row.  We also initialize
  * it to <code>true</code> in case that joker is a real sonofabitch who
  * tries to push back without reading anything at all first
  */
private boolean pushed_back = true;

/**
  * Holds the pushed back ttype value
  */
private int pb_ttype;

/**
  * Holds the pushed back sval value
  */
private String pb_sval;

/**
  * Holds the pushed back nval value;
  */
private double pb_nval;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method reads bytes from an <code>InputStream</code> and tokenizes
  * them.  For details on how this method operates by default, see
  * <code>StreamTokenizer(Reader)</code>.
  *
  * @param in The <code>InputStream</code> to read from
  *
  * @deprecated
  */
public 
StreamTokenizer(InputStream in)
{
  this(new InputStreamReader(in));
}

/*************************************************************************/

/**
  * This method initializes a new <code>StreamTokenizer</code> to read 
  * characters from a <code>Reader</code> and parse them.  The char values
  * have their hight bits masked so that the value is treated a character
  * in the range of 0x0000 to 0x00FF.
  * <p>
  * This constructor sets up the parsing table to parse the stream in the
  * following manner:
  * <ul>
  * <li>The values 'A' through 'Z', 'a' through 'z' and 0xA0 through 0xFF
  *     are initialized as alphabetic
  * <li>The values 0x00 through 0x20 are initialized as whitespace
  * <li>The values '\'' and '"' are initialized as quote characters
  * <li>'/' is a comment character
  * <li>Numbers will be parsed
  * <li>EOL is not treated as significant
  * <li>C  and C++ (//) comments are not recognized
  * </ul>
  *
  * @param in The <code>Reader</code> to read chars from
  */
public
StreamTokenizer(Reader in)
{
  // Ok, be lazy and let LineNumberReader and PushbackReader go do that
  // voodoo that they do so well.  We can get away with this since we don't 
  // have to move the line number back ever.
  lnr = new LineNumberReader(in);
  pbr = new PushbackReader(lnr, 512); // Hope that's enough space!

  wordChars('A', 'Z');
  wordChars('a', 'z');
  wordChars(0xA0, 0xFF);

  whitespaceChars(0x00, 0x20);

  quoteChar('\'');
  quoteChar('"');

  commentChar('/');

  parseNumbers(); 
}

/*************************************************************************/

/**
  * This method removes all attributes (whitespace, alphabetic, numeric,
  * quote, and comment) from all characters.  It is equivalent to calling
  * <code>ordinaryChars(0x00, 0xFF)</code>.
  *
  * @see ordinaryChars
  */
public void
resetSyntax()
{
  try
    {
      ordinaryChars(0x00, 0xFF);
    }
  catch(IllegalArgumentException e) { ; }
}

/*************************************************************************/

/**
  * This method sets the alphabetic attribute for all charcters in the
  * specified range, range terminators included.
  *
  * @param low The low end of the range of values to set the alphabetic attribute for
  * @param high The high end of the range of values to set the alphabetic attribute for
  *
  * @exception IllegalArgumentException If low < 0x00 or high > 0xFF or low > high
  */
public synchronized void
wordChars(int low, int high) throws IllegalArgumentException
{
  if ((low < 0) || (high > 255) | (low > high))
    throw new IllegalArgumentException("Bad range: low=" + low + 
                                       " high=" + high);

  for (int i = low; i <= high; i++)
    lookup_table[i][ALPHA] = true;
}

/*************************************************************************/

/**
  * This method sets the whitespace attribute for all charcters in the
  * specified range, range terminators included.
  *
  * @param low The low end of the range of values to set the whitespace attribute for
  * @param high The high end of the range of values to set the whitespace attribute for
  *
  * @exception IllegalArgumentException If low < 0x00 or high > 0xFF or low > high
  */
public synchronized void
whitespaceChars(int low, int high) throws IllegalArgumentException
{
  if ((low < 0) || (high > 255) | (low > high))
    throw new IllegalArgumentException("Bad range: low=" + low + 
                                       " high=" + high);

  for (int i = low; i <= high; i++)
    lookup_table[i][WHITE] = true;
}

/*************************************************************************/

/**
  * This method makes all the characters in the specified range, range
  * terminators included, ordinary.  This means the none of the attributes
  * (whitespace, alphabetic, numeric, quote, or comment) will be set on
  * any of the characters in the range.  This makes each character in this
  * range parse as its own token.
  *
  * @param low The low end of the range of values to set the whitespace attribute for
  * @param high The high end of the range of values to set the whitespace attribute for
  *
  * @exception IllegalArgumentException If low < 0x00 or high > 0xFF or low > high
  */
public synchronized void
ordinaryChars(int low, int high) throws IllegalArgumentException
{
  if ((low < 0) || (high > 255) | (low > high))
    throw new IllegalArgumentException("Bad range: low=" + low + 
                                       " high=" + high);

  for (int i = low; i <= high; i++)
    {
      lookup_table[i][WHITE] = false;
      lookup_table[i][ALPHA] = false;
      lookup_table[i][NUMERIC] = false;
      lookup_table[i][QUOTE] = false;
      lookup_table[i][COMMENT] = false;
    }
}

/*************************************************************************/

/**
  * This method makes the specified character an ordinary character.  This
  * means that none of the attributes (whitespace, alphabetic, numeric,
  * quote, or comment) will be set on this character.  This character will
  * parse as its own token.
  *
  * @param c The charcter to make ordinary, passed as an int
  *
  * @exception IllegalArgumentException If c < 0 or c > 255
  */
public void
ordinaryChar(int c) throws IllegalArgumentException
{
  ordinaryChars(c, c);
}

/*************************************************************************/

/**
  * This method sets the comment attribute on the specified character.
  *
  * @param c The character to set the comment attribute for, passed as an int;
  *
  * @exception IllegalArgumentException If c < 0 or c > 255
  */
public void
commentChar(int c) throws IllegalArgumentException
{
  if ((c < 0) || (c > 0xFF))
    throw new IllegalArgumentException("Bad value: " + c);

  lookup_table[c][COMMENT] = true;
}

/*************************************************************************/

/**
  * This method sets the quote attribute on the specified character.
  *
  * @param c The character to set the quote attribute for, passed as an int;
  *
  * @exception IllegalArgumentException If c < 0 or c > 255
  */
public void
quoteChar(int c) throws IllegalArgumentException
{
  if ((c < 0) || (c > 0xFF))
    throw new IllegalArgumentException("Bad value: " + c);

  lookup_table[c][QUOTE] = true;
}

/*************************************************************************/

/**
  * This method sets the numeric attribute on the characters '0' - '9' and
  * the characters '.' and '-'.
  */
public synchronized void
parseNumbers()
{
  for (int i = '0'; i <= '9'; i++)
    lookup_table[i][NUMERIC] = true;
  
  lookup_table['.'][NUMERIC] = true;
  lookup_table['-'][NUMERIC] = true;
}

/*************************************************************************/

/**
  * This method sets a flag that indicates whether or not the end of line
  * sequence terminates and is a token.  The defaults to <code>false</code>
  *
  * @param flag <code>true</code> if EOF is significant, <code>false</code> otherwise
  */
public void
eolIsSignificant(boolean flag)
{
  eol_is_significant = flag;
}

/*************************************************************************/

/**
  * This method sets a flag that indicates whether or not "C" language style
  * comments (with nesting not allowed) are handled by the parser.
  * If this is <code>true</code> commented out sequences are skipped and
  * ignored by the parser.  This defaults to <code>false</code>.
  *
  * @param flag <code>true</code> to recognized and handle "C" style comments, <code>false</code> otherwise
  */
public void
slashStarComments(boolean flag)
{
  handle_c_comments = flag;
}

/*************************************************************************/

/**
  * This method sets a flag that indicates whether or not "C++" language style
  * comments ("//" comments through EOL ) are handled by the parser.
  * If this is <code>true</code> commented out sequences are skipped and
  * ignored by the parser.  This defaults to <code>false</code>.
  *
  * @param flag <code>true</code> to recognized and handle "C++" style comments, <code>false</code> otherwise
  */
public void
slashSlashComments(boolean flag)
{
  handle_cplusplus_comments = flag;
}

/*************************************************************************/

/**
  * This method sets a flag that indicates whether or not alphabetic
  * tokens that are returned should be converted to lower case.
  * 
  * @param flag <code>true</code> to convert to lower case, <code>false</code> otherwise
  */
public void
lowerCaseMode(boolean flag)
{
  lower_case_mode = flag;
}

/*************************************************************************/

/**
  * This method returns the current line number.  Note that if the 
  * <code>pushBack()</code> method is called, it has no effect on the
  * line number returned by this method.
  *
  * @return The current line number
  */
public int
lineno()
{
  return(lnr.getLineNumber());
}

/*************************************************************************/

public synchronized void
pushBack()
{
  if (pushed_back)
    return;

  pushed_back = true;
  pb_ttype = ttype;
  pb_nval = nval;
  pb_sval = sval;
}

/*************************************************************************/

/**
  * This method reads the next token from the stream.  It sets the 
  * <code>ttype</code> variable to the appropriate token type and 
  * returns it.  It also can set <code>sval</code> or <code>nval</code>
  * as described below.  The parsing strategy is as follows:
  * <ul>
  * <li>Skip any whitespace characters
  * <li>If a numeric character is encountered, attempt to parse a numeric
  * value.  Leading '-' characters indicate a numeric only if followed by
  * another non-'-' numeric.  The value of the numeric token is terminated
  * by either the first non-numeric encountered, or the second occurrence of
  * '-' or '.'.  The token type returned is TT_NUMBER and <code>nval</code>
  * is set to the value parsed.
  * <li>If an alphabetic character is parsed, all subsequent characters
  * are read until the first non-alphabetic or non-numeric character is
  * encountered.  The token type returned is TT_WORD and the value parsed
  * is stored in <code>sval</code>.  If lower case mode is set, the token
  * stored in <code>sval</code> is converted to lower case.  The end of line
  * sequence terminates a word only if EOL signficance has been turned on.
  * The start of a comment also terminates a word.  Any character with a 
  * non-alphabetic and non-numeric attribute (such as white space, a quote,
  * or a commet) are treated as non-alphabetic and terminate the word.
  * <li>If a comment charcters is parsed, then all remaining characters on
  * the current line are skipped and another token is parsed.  Any EOL or EOF's
  * encountered are not discarded, but rather terminate the comment.
  * <li>If a quote character is parsed, then all characters up to the 
  * second occurrence of the same quote character are parsed into a <code>String</code>
  * This <code>String</code> is stored as <code> sval, but is not converted 
  * to lower case, even if lower case mode is enabled.  The token type returned
  * is the value of the quote character encountered.  Any escape sequences
  * (\b (backspace), '\t' (HTAB), \n (linefeed), \f (form feed), 'r' 
  * (carriage return), '\"' (double quote), '\'' (single quote), '\\'
  * (backslash), '\XXX' (octal esacpe) are converted to the appropriate
  * char values.  Invalid esacape sequences are left in untranslated.  
  * Unicode characters like ('\u0000') are not recognized. 
  * <li>If the C++ comment sequence "//" is encountered, and the parser
  * is configured to handle that sequence, then the remainder of the line
  * is skipped and another token is read exactly as if a character with
  * the comment attribute was encountered.
  * <li>If the C comment sequence "/*" is encountered, and the parser
  * is configured to handle that sequence, then all characters up to and
  * including the comment terminator sequence are discarded and another
  * token is parsed.
  * <li>If all cases above are not met, then the character is an ordinary
  * character that is parsed as a token by itself.  The char encountered
  * is returned as the token type.
  *
  * @return The token type
  *
  * @exception IOException If an error occurs
  */
public synchronized int
nextToken() throws IOException
{
  // Check for pushed back token
  if ((pushed_back == true) && (ttype != TT_NONE))
    {
      pushed_back = false; 
      ttype = pb_ttype;
      nval = pb_nval;
      sval = pb_sval;
      return(ttype);
    }

  token_buf = new char[BUFFER_INCREMENT_SIZE];
  bufpos = 0;

  int c;

  // Check for white space or EOF
  for (;;)
    {
      c = readChar();
      if (c == -1)
        {
          ttype = TT_EOF;
          return(ttype);
        }

     // Check end of line 
     if (eol_is_significant && ((c == '\r') || (c == '\n')))
       {
         if (c == '\r')
           {
             c = readChar();
             if ((c != -1) && (c != '\n'))
               pbr.unread(c);
           }
         ttype = TT_EOL;
         return(ttype);
       } 

     if (lookup_table[c][WHITE] == false)
       break;
    }

  // Check for numeric
  if (lookup_table[c][NUMERIC])
    {
      token_buf[bufpos] = (char)c;
      ++bufpos;

      // Read it in
      boolean found_decimal = false;
      for(;;)
        {
          c = readChar();
          if (c == -1)
            break;

          // Is this the end of the number?
          if (!lookup_table[c][NUMERIC] || (c == '-') || 
              ((c == '.') && found_decimal))
            {
              pbr.unread(c);
              break;
            }

          // The second '.' terminates the number so mark the first occurrence
          if (c == '.')
            found_decimal = true;

          token_buf[bufpos] = (char)c;
          ++bufpos;

          // Make sure we don't cream the buffer
          if (bufpos == token_buf.length)
            enlargeBuffer();
        }

      // Now we see if we got a number, or some wacko sequence like
      // "-.-" or "-.."
      try
        {
          nval = Double.valueOf(new String(token_buf, 0, bufpos)).doubleValue();
          ttype = TT_NUMBER;
          return(ttype);
        }
      catch(NumberFormatException e)
       {
         // Unread all data to date and fall through to the next case
         c = token_buf[0];
         pbr.unread(token_buf, 0, bufpos);
         bufpos = 0;
       } 
    }

  // Check for alphabetic word (fall through and catch fake numerics too)
  if (lookup_table[c][ALPHA] || lookup_table[c][NUMERIC])
    {
      // Loop and fill in the rest of the word
      for(;;)
        {
          token_buf[bufpos] = (char)c;
          ++bufpos;

          // Make sure we don't cream the buffer
          if (bufpos == token_buf.length)
            enlargeBuffer();

          c = readChar();
          if (c == -1)
            break;

          // Check for lack of required attributes
          if (!lookup_table[c][ALPHA] && !lookup_table[c][NUMERIC])
            break;
        }

      if (c != -1)
        pbr.unread(c); 
      

      //sval = new String(token_buf, 0, bufpos);

      // Handle lower case mode if enabled
      if (lower_case_mode)
        {
          StringBuffer sb = new StringBuffer("");

          for (int i = 0; i < bufpos; i++)
            if ((token_buf[i] >= 'A') && (token_buf[i] <= 'Z'))
              sb.append((char)(token_buf[i] + ('a' - 'A')));
            else
              sb.append((char)token_buf[i]);

          sval = sb.toString();
        }
      else
        {
          sval = new String(token_buf, 0, bufpos);
        }

      ttype = TT_WORD;
      return(ttype);
    }

  // Handle comments
  if (lookup_table[c][COMMENT])
    {
      for (;;)
        {
          c = readChar();
          if (c == -1)
            {
              ttype = TT_EOF;
              return(ttype);
            }

          if ((c == '\r') || (c == '\n'))
            {
              // If bare CR, check for following LF
              if (c == '\r')
                {
                  c = readChar();
                  if (c == -1)
                    {
                      if (eol_is_significant)
                        ttype = TT_EOL;
                      else
                        ttype = TT_EOF;

                      return(ttype);
                    }
                          
                  if (c != '\n')
                    pbr.unread(c);
                }

              if (eol_is_significant)
                pbr.unread('\n'); 

              return(nextToken());
            }  
        }
    }

  // Handle quoted strings
  if (lookup_table[c][QUOTE])
    {
      char quote_char_found = (char)c;

      for (;;)
        {
          c = readChar();
          if (c == -1)
            break;

          // End of line terminates the quote
          if ((c == '\r') || (c == '\n'))
            {
              // If bare CR, check for following LF
              if (c == '\r')
                {
                  c = readChar();
                          
                  if (c != '\n')
                    pbr.unread(c);
                }

              if (eol_is_significant)
                pbr.unread('\n'); 

              break;
            }

          // Check for end quote
          if (c == quote_char_found)
            break;

          // Handle escape sequences
          if (c == '\\')
            {
              int c2 = readChar();
              switch (c2)
                {
                  case 'b': // Backspace
                    c = '\u0008';
                    break;

                  case 't': // Horizontal tab
                    c = '\u0009';
                    break;

                  case 'n': // Newline
                    /* c = '\\u000a'; */
		    c = '\n';
                    break;

                  case 'f': // Form feed
                    c = '\u000c';
                    break;

                  case 'r': // Carriage return
                    c = '\u000d';
                    break;

                  case '"': // Double quote
                    c = '"';
                    break;

                  case '\'': // Single quote
                    c = '\'';
                    break;

                  case '\\': // Backslash
                    c = '\\';
                    break;

                  // Octal escape
                  case '0':
                  case '1':
                  case '2':
                  case '3':
                  case '4':
                  case '5':
                  case '6':
                  case '7':
                    // Read all the digits up to a max of 3
                    int c3 = readChar();
                    int c4 = -1;
                    if ((c3 >= '0') && (c3 <= '7'))
                      {
                        c4 = readChar();
                        if ((c4 == -1) || ((c4 < '0') && (c4 > '7')))
                          {
                            pbr.unread(c4);
                            c4 = -1;
                          }
                      }
                    else if (c3 != -1)
                      {
                        pbr.unread(c3);
                        c3 = -1;
                      } 

                    // If there are three octal digits, the first one must
                    // be in the range of 0-3
                    if ((c4 != -1) && (c2 > '3'))
                      {
                        pbr.unread(c4);
                        pbr.unread(c3);
                        pbr.unread(c2);
                        break;
                      }

                    // Calculate the octal character code
                    int ovalue = 0;
                    if (c4 != -1)
                       ovalue = (c4 - 0x30) + ((c3 - 0x30) * 8) +
                                ((c2 - 0x30) * 64);
                    else if (c3 != -1)
                       ovalue = (c3 - 0x30) + ((c2 - 0x30) * 8);
                    else
                       ovalue = (c2 - 0x30);

                    c = (char)ovalue; 
                    break;

                  // No escape match.  For lack of anything better to do,
                  // we send the raw charcters back as part of the string
                  default:
                   pbr.unread(c2);
                }
            }
 
          token_buf[bufpos] = (char)c;
          ++bufpos;

          // Make sure we don't cream the buffer
          if (bufpos == token_buf.length)
            enlargeBuffer();
        }

      sval = new String(token_buf, 0, bufpos);
      ttype = quote_char_found;
      return(ttype);
    }

  // Handle C++ "//" style comments
  if (handle_cplusplus_comments && (c == '/'))
    {
      int c2 = readChar();
      if (c2 != '/')
        {
          pbr.unread(c2);
        }
      else
        {
          for (;;)
            {
              c = readChar();
              if (c == -1)
                {
                  ttype = TT_EOF;
                  return(ttype);
                }
    
              if ((c == '\r') || (c == '\n'))
                {
                  // If bare CR, check for following LF
                  if (c == '\r')
                    {
                      c = readChar();
                      if (c == -1)
                        {
                          if (eol_is_significant)
                            ttype = TT_EOL;
                          else
                            ttype = TT_EOF;
    
                          return(ttype);
                        }
                              
                      if (c != '\n')
                        pbr.unread(c);
                    }
    
                  if (eol_is_significant)
                    pbr.unread('\n'); 
    
                  return(nextToken());
                }  
            }
        }
    }

  // Handle C style "/* */" comments
  if (handle_c_comments && (c == '/'))
    {
      int c2 = readChar();
      if (c2 != '*')
        {
          pbr.unread(c2);
        }
      else
        {
          for(;;)
            {
              c = readChar();
              if (c == -1)
                return(TT_EOF);

              if (c == '*')
                {
                  c2 = readChar();
                  if (c2 == '/')
                    return(nextToken());

                  pbr.unread(c2);
                }
            }
        }
    }

  // Finally, it must be an ordinary character
  ttype = c;
  return(c);
}

/*************************************************************************/

/**
  * This convenience method reads a char from the stream and masks off
  * the high bytes
  */
public int
readChar() throws IOException
{
  int c = pbr.read();
  if (c == -1)
    return(-1);
  else
    return(c & 0xFF);
}

/*************************************************************************/

/**
  * Makes the token buffer bigger
  */
public void
enlargeBuffer()
{
  char[] newbuf = new char[token_buf.length + BUFFER_INCREMENT_SIZE];

  System.arraycopy(token_buf, 0, newbuf, 0, token_buf.length);
  token_buf = newbuf;  
}

/*************************************************************************/

/**
  * This method returns the current token value as a <code>String</code> in
  * the form "Token[x], line n", where 'n' is the current line numbers and
  * 'x' is determined as follows:
  * <ul>
  * <li>If no token has been read, then 'x' is "NOTHING" and 'n' is 0
  * <li>If <code>ttype</code> is TT_EOF, then 'x' is "EOF"
  * <li>If <code>ttype</code> is TT_EOL, then 'x' is "EOL"
  * <li>If <code>ttype</code> is TT_WORD, then 'x' is <code>sval</code>
  * <li>If <code>ttype</code> is TT_NUMBER, then 'x' is "n=strnval" where 'strnval' is <code>String.valueOf(nval)</code>.
  * <li>If <code>ttype</code> is a quote character, then 'x' is <code>sval</code>
  * <li>For all other cases, 'x' is <code>ttype</code>
  * </ul>
  */
public String
toString()
{
  switch (ttype)
    {
      case TT_NONE:
        return("Token[NOTHING], line 0"); // Found this format in bug parade

      case TT_EOF:
        return("Token[EOF], line " + lineno());

      case TT_EOL:
        return("Token[EOL], line " + lineno());

      case TT_WORD:
        return("Token[" + sval + "], line " + lineno());

      case TT_NUMBER:
        return("Token[" + String.valueOf(nval) + "], line " + lineno());

      default:
        if (lookup_table[ttype][QUOTE])
          return("Token[" + sval + "], line " + lineno());
        else
          return("Token[" + (char)ttype + "], line " + lineno());
    }
}

} // class StreamTokenizer

