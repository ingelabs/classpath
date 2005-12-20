/* CSSParser.java --
   Copyright (C) 2005 Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package javax.swing.text.html;

import java.io.*;

/**
 * Parses a CSS document. This works by way of a delegate that implements the
 * CSSParserCallback interface. The delegate is notified of the following
 * events: 
 * - Import statement: handleImport 
 * - Selectors handleSelector. This is invoked for each string. For example if 
 * the Reader contained p, bar , a {}, the delegate would be notified 4 times, 
 * for 'p,' 'bar' ',' and 'a'. 
 * - When a rule starts, startRule 
 * - Properties in the rule via the handleProperty. This
 * is invoked one per property/value key, eg font size: foo;, would cause the
 * delegate to be notified once with a value of 'font size'. 
 * - Values in the rule via the handleValue, this is notified for the total value. 
 * - When a rule ends, endRule
 * 
 * @author Lillian Angel (langel@redhat.com)
 */
class CSSParser
{

  /**
   * Receives all information about the CSS document structure while parsing it.
   * The methods are invoked by parser.
   */
  static interface CSSParserCallback
  {
    /**
     * Handles the import statment in the document.
     * 
     * @param imp - the import string
     */
    public abstract void handleImport(String imp);

    /**
     * Called when the start of a rule is encountered.
     */
    public abstract void startRule();

    /**
     * Called when the end of a rule is encountered.
     */
    public abstract void endRule();

    /**
     * Handles the selector of a rule.
     * 
     * @param selector - the selector in the rule
     */
    public abstract void handleSelector(String selector);

    /**
     * Handles the properties in the document.
     * 
     * @param property - the property in the document.
     */
    public abstract void handleProperty(String property);

    /**
     * Handles the values in the document.
     * 
     * @param value - the value to handle.
     */
    public abstract void handleValue(String value);

  }

  /**
   * The identifier of the rule.
   */
  private static final int IDENTIFIER = 1;

  /**
   * The open bracket.
   */
  private static final int BRACKET_OPEN = 2;

  /**
   * The close bracket.
   */
  private static final int BRACKET_CLOSE = 3;

  /**
   * The open brace.
   */
  private static final int BRACE_OPEN = 4;

  /**
   * The close brace.
   */
  private static final int BRACE_CLOSE = 5;

  /**
   * The open parentheses.
   */
  private static final int PAREN_OPEN = 6;

  /**
   * The close parentheses.
   */
  private static final int PAREN_CLOSE = 7;

  /**
   * The end of the document.
   */
  private static final int END = -1;

  /**
   * The character mapping in the document.
   */
  private static final char[] charMapping = null; // FIXME

  /**
   * Set to true if one character has been read ahead.
   */
  private boolean didPushChar;

  /**
   * The read ahead character.
   */
  private int pushedChar;

  /**
   *  Temporary place to hold identifiers.
   */
  private StringBuffer unitBuffer;

  /**
   * Used to indicate blocks.
   */
  private int[] unitStack;

  /**
   * Number of valid blocks.
   */
  private int stackCount;

  /**
   * Holds the incoming CSS rules.
   */
  private Reader reader;

  /**
   * Set to true when the first non @ rule is encountered.
   */
  private boolean encounteredRuleSet;

  /**
   * The call back used to parse.
   */
  private CSSParser.CSSParserCallback callback;

  /**
   * nextToken() inserts the string here.
   */
  private char[] tokenBuffer;

  /**
   * Current number of chars in tokenBufferLength.
   */
  private int tokenBufferLength;

  /**
   * Set to true if any whitespace is read.
   */
  private boolean readWS;

  /**
   * Constructor
   */
  CSSParser()
  {
    unitBuffer = new StringBuffer();
  }

  /**
   * Appends a character to the token buffer.
   * 
   * @param c - the character to append
   */
  private void append(char c)
  {
    char[] temp = new char[tokenBufferLength + 1];
    if (tokenBuffer != null)
      System.arraycopy(tokenBuffer, 0, temp, 0, tokenBufferLength);
    temp[tokenBufferLength] = c;
    tokenBufferLength++;
    tokenBuffer = temp;
  }

  /**
   * Fetches the next token.
   * 
   * @param c - the character to fetch.
   * @return the location
   * @throws IOException - any i/o error encountered while reading
   */
  private int nextToken(char c) throws IOException
  {
    readWS = false;
    int next = readWS();

    switch (next)
      {
      case '\"':
        // FIXME: Not Implemented
        return IDENTIFIER;
      case '\'':
        // FIXME: Not Implemented
        return IDENTIFIER;
      case '(':
        return PAREN_OPEN;
      case ')':
        return PAREN_CLOSE;
      case '{':
        return BRACE_OPEN;
      case '}':
        return BRACE_CLOSE;
      case '[':
        return BRACKET_OPEN;
      case ']':
        return BRACKET_CLOSE;
      case -1:
        return END;
      default:
        pushChar(next);
        getIdentifier(c);
        return IDENTIFIER;
      }
  }

  /**
   * Reads a character from the stream.
   * 
   * @return the number of characters read or -1 if end of stream is reached.
   * @throws IOException - any i/o encountered while reading
   */
  private int readChar() throws IOException
  {
    if (didPushChar)
      {
        didPushChar = false;
        return pushedChar;
      }
    return reader.read();
  }

  /**
   * Parses the the contents of the reader using the
   * callback.
   * 
   * @param reader - the reader to read from
   * @param callback - the callback instance
   * @param parsingDeclaration - true if parsing a declaration
   * @throws IOException - any i/o error from the reader
   */
  void parse(Reader reader, CSSParser.CSSParserCallback callback, 
             boolean parsingDeclaration)
      throws IOException
  {
    this.reader = reader;
    this.callback = callback;
    // call getNextStatement
    // FIXME: Not fully implemented
  }

  /**
   * Skips any white space, returning the character after the white space.
   * 
   * @return the character after the whitespace
   * @throws IOException - any i/o error from the reader
   */
  private int readWS() throws IOException
  {
    int next = readChar();
    while (Character.isWhitespace((char) next))
      {
        readWS = true;
        int tempNext = readChar();
        if (tempNext == END)
          return next;
        next = tempNext;
      }
    
    // Its all whitespace
    return END;
  }

  /**
   * Gets the next statement, returning false if the end is reached.
   * A statement is either an @ rule, or a ruleset.
   * 
   * @return the next statement
   * @throws IOException - any i/o error from the reader
   */
  private boolean getNextStatement() throws IOException
  {
    // get next set and parseRuleSet
    // FIXME: Not implemented
    return false;
  }

  /**
   * Parses an @ rule, stopping at a matching brace pair, or ;.
   * 
   * @throws IOException - any i/o error from the reader
   */
  private void parseAtRule() throws IOException
  {
    // FIXME: Not Implemented
  }

  /**
   * Parses the next rule set, which is a selector followed by a declaration 
   * block.
   * 
   * @throws IOException - any i/o error from the reader
   */
  private void parseRuleSet() throws IOException
  {
    // call parseDeclarationBlock
    // FIXME: Not Implemented
  }

  /**
   * Parses a set of selectors, returning false if the end of the stream is 
   * reached.
   * 
   * @return false if the end of stream is reached
   * @throws IOException - any i/o error from the reader
   */
  private boolean parseSelectors() throws IOException
  {
    // FIXME: Not Implemented
    return false; 
  }

  /**
   * Parses a declaration block. Which a number of declarations followed by a
   * })].
   * 
   * @throws IOException - any i/o error from the reader
   */
  private void parseDeclarationBlock() throws IOException
  {
    // call parseDeclaration
    // FIXME: Not Implemented
  }

  /**
   * Parses a single declaration, which is an identifier a : and another identifier.
   * This returns the last token seen.
   * 
   * @returns the last token
   * @throws IOException - any i/o error from the reader
   */
  private int parseDeclaration() throws IOException
  {
    // call handleValue
    // FIXME: Not Implemented
    return 0; 
  }

  /**
   * Parses identifiers until c is encountered, returning the ending token,
   * which will be IDENTIFIER if c is found.
   * 
   * @param c - the stop character
   * @param wantsBlocks - true if blocks are wanted
   * @return the ending token
   * @throws IOException - any i/o error from the reader
   */
  private int parseIdentifiers(char c, boolean wantsBlocks) throws IOException
  {
    // FIXME: Not implemented
    return 0;
  }

  /**
   * Parses till a matching block close is encountered. This is only appropriate
   * to be called at the top level (no nesting).
   * 
   * @param i - FIXME
   * @throws IOException - any i/o error from the reader
   */
  private void parseTillClosed(int i) throws IOException
  {
    // FIXME: Not Implemented
  }

  /**
   * Gets an identifier, returning true if the length of the string is greater
   * than 0, stopping when c, whitespace, or one of {}()[] is hit.
   * 
   * @param c - the stop character
   * @return returns true if the length of the string > 0
   * @throws IOException - any i/o error from the reader
   */
  private boolean getIdentifier(char c) throws IOException
  {
    // FIXME: Not Implemented
    return false;
  }

  /**
   * Reads till c is encountered, escaping characters as necessary.
   * 
   * @param c - the stop character
   * @throws IOException - any i/o error from the reader
   */
  private void readTill(char c) throws IOException
  {
    // FIXME: Not Implemented
  }

  /**
   * Parses a comment block.
   * 
   * @throws IOException - any i/o error from the reader
   */
  private void readComment() throws IOException
  {
    // FIXME: Not Implemented
  }

  /**
   * Called when a block start is encountered ({[.
   * 
   * @param start of block
   */
  private void startBlock(int start)
  {
    // FIXME: Not Implemented
  }

  /**
   * Called when an end block is encountered )]}
   * 
   * @param end of block
   */
  private void endBlock(int end)
  {
    // FIXME: Not Implemented
  }

  /**
   * Checks if currently in a block.
   * 
   * @return true if currently in a block.
   */
  private boolean inBlock()
  {
    // FIXME: Not Implemented
    return false; 
  }

  /**
   * Supports one character look ahead, this will throw if called twice in a row.
   * 
   * @param c - the character to push.
   * @throws IOException - if called twice in a row
   */
  private void pushChar(int c) throws IOException
  {
    if (didPushChar)
      throw new IOException("pushChar called twice.");
    didPushChar = true;
    pushedChar = c;
  }
}

 