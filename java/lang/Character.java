package java.lang;

import java.util.*;
import java.io.*;
import gnu.java.lang.ClassLoaderHelper;

/**
 * Wrapper class for the primitive char data type.  In addition,
 * this class allows one to retrieve property information and
 * perform transformations on the 38,000+ characters in the
 * Unicode Standard, Version 2.  java.lang.Character is designed 
 * to be very dynamic, and as such, it retrieves information on
 * the Unicode character set from a separate database, which
 * can be easily upgraded.<br>
 * <br>
 * For predicates, boundaries are used to describe
 * the set of characters for which the method will return true.
 * This syntax uses fairly normal regular expression notation.
 * See §5.13 of the Unicode Standard, Version 2.0, for the
 * boundary specification.<br>
 * <br>
 * See http://www.unicode.org for more information on the Unicode Standard.
 *
 * @author Paul N. Fisher
 */
public final class Character implements Serializable/*, Comparable */
{
  /**
   * Constants for character blocks.
   * All character blocks are part of the Unicode 2 standard, unless 
   * otherwise noted.
   */
  public static class Subset {
    private char start;
    private char end;
    private static final int NUM_SUBSETS = 66;
    private static final Subset sets[] = new Subset[NUM_SUBSETS];

    /**
     * Returns the Unicode character block which a character belongs to.
     */
    private static Subset getBlock(char ch) {
      // do a simple binary search for the correct block
      int low = 0; int hi = sets.length-1; int mid = 0;
      while (low <= hi) {
	mid = (low + hi) >> 1;
	Subset b = sets[mid];
	if (ch < b.start)
	  hi = mid - 1;
	else if (ch > b.end)
	  low = mid + 1;
	else return b;
      }
      return null;
    }

    /**
     * Constructor for strictly defined normative blocks
     */
    private Subset(int num, char start, char end) {
      this.start = start;
      this.end = end;
      sets[num] = this;
    }

    /**
     * Constructor for strictly defined blocks.
     */
    private Subset(char start, char end) {
      this.start = start;
      this.end = end;
    }

    /**
     * Constructor for loosely defined CJK blocks
     */
    private Subset() { }
    
    /**
     * U+0000 - U+007F
     */
    public final static Subset BASIC_LATIN = new Subset(0, '\u0000', '\u007F');
    /**
     * U+0080 - U+00FF
     */
    public final static Subset LATIN_1_SUPPLEMENT = new Subset(1, '\u0080', '\u00FF');
    /**
     * U+0100 - U+017F
     */
    public final static Subset LATIN_EXTENDED_A = new Subset(2, '\u0100', '\u017F');
    /**
     * U+0180 - U+024F
     */
    public final static Subset LATIN_EXTENDED_B = new Subset(3, '\u0180', '\u024F');
    /**
     * U+0250 - U+02AF
     */
    public final static Subset IPA_EXTENSIONS = new Subset(4,'\u0250', '\u02AF');
    /**
     * U+02B0 - U+02FF
     */
    public final static Subset SPACING_MODIFIER_LETTERS = new Subset(5, '\u02B0', '\u02FF');
    /**
     * U+0300 - U+036F
     */
    public final static Subset COMBINING_DIACRITICAL_MARKS = new Subset(6, '\u0300', '\u036F');
    /**
     * U+0370 - U+03FF
     */
    public final static Subset GREEK = new Subset(7, '\u0370', '\u03FF');
    /**
     * U+0400 - U+04FF
     */
    public final static Subset CYRILLIC = new Subset(8, '\u0400', '\u04FF');
    /**
     * U+0530 - U+058F
     */
    public final static Subset ARMENIAN = new Subset(9, '\u0530', '\u058F');
    /**
     * U+0590 - U+05FF
     */
    public final static Subset HEBREW = new Subset(10, '\u0590', '\u05FF');
    /** 
     * U+0600 - U+06FF
     */
    public final static Subset ARABIC = new Subset(11, '\u0600', '\u06FF');
    /**
     * U+0900 - U+097F
     */
    public final static Subset DEVANAGARI = new Subset(12, '\u0900', '\u097F');
    /**
     * U+0980 - U+09FF
     */
    public final static Subset BENGALI = new Subset(13, '\u0980', '\u09FF');
    /**
     * U+0A00 - U+0A7F
     */
    public final static Subset GURMUKHI = new Subset(14, '\u0A00', '\u0A7F');
    /**
     * U+0A80 - U+0AFF
     */
    public final static Subset GUJARATI = new Subset(15, '\u0A80', '\u0AFF');
    /**
     * U+0B00 - U+0B7F
     */
    public final static Subset ORIYA = new Subset(16, '\u0B00', '\u0B7F');
    /**
     * U+0B80 - U+0BFF
     */
    public final static Subset TAMIL = new Subset(17, '\u0B80', '\u0BFF');
    /**
     * U+0C00 - U+0C7F
     */
    public final static Subset TELUGU = new Subset(18, '\u0C00', '\u0C7F');
    /**
     * U+0C80 - U+0CFF
     */
    public final static Subset KANNADA = new Subset(19, '\u0C80', '\u0CFF');
    /**
     * U+0D00 - U+0D7F
     */
    public final static Subset MALAYALAM = new Subset(20, '\u0D00', '\u0D7F');
    /**
     * U+0E00 - U+0E7F
     */
    public final static Subset THAI = new Subset(21, '\u0E00', '\u0E7F');
    /**
     * U+0E80 - U+0EFF
     */
    public final static Subset LAO = new Subset(22, '\u0E80', '\u0EFF');
    /**
     * U+0F00 - U+0FBF
     */
    public final static Subset TIBETAN = new Subset(23, '\u0F00', '\u0FBF');
    /**
     * U+10A0 - U+10FF
     */
    public final static Subset GEORGIAN = new Subset(24, '\u10A0', '\u10FF');
    /**
     * U+1100 - U+11FF
     */
    public final static Subset HANGUL_JAMO = new Subset(25, '\u1100', '\u11FF');
    /**
     * U+1E00 - U+1EFF
     */
    public final static Subset LATIN_EXTENDED_ADDITIONAL = new Subset(26, '\u1E00', '\u1EFF');
    /**
     * U+1F00 - U+1FFF
     */
    public final static Subset GREEK_EXTENDED = new Subset(27, '\u1F00', '\u1FFF');
    /**
     * U+2000 - U+206F
     */
    public final static Subset GENERAL_PUNCTUATION = new Subset(28, '\u2000', '\u206F');
    /**
     * U+2070 - U+209F
     */
    public final static Subset SUPERSCRIPTS_AND_SUBSCRIPTS = new Subset(29, '\u2070', '\u209F');
    /**
     * U+20A0 - U+20CF
     */
    public final static Subset CURRENCY_SYMBOLS = new Subset(30, '\u20A0', '\u20CF');
    /**
     * U+20D0 - U+20FF
     */
    public final static Subset COMBINING_MARKS_FOR_SYMBOLS = new Subset(31, '\u20D0', '\u20FF');
    /**
     * U+2100 - U+214F
     */
    public final static Subset LETTERLIKE_SYMBOLS = new Subset(32, '\u2100', '\u214F');
    /**
     * U+2150 - U+218F
     */
    public final static Subset NUMBER_FORMS = new Subset(33, '\u2150', '\u218F');
    /**
     * U+2190 - U+21FF
     */
    public final static Subset ARROWS = new Subset(34, '\u2190', '\u21FF');
    /**
     * U+2200 - U+22FF
     */
    public final static Subset MATHEMATICAL_OPERATORS = new Subset(35, '\u2200', '\u22FF');
    /**
     * U+2300 - U+23FF
     */
    public final static Subset MISCELLANEOUS_TECHNICAL = new Subset(36, '\u2300', '\u23FF');
    /**
     * U+2400 - U+243F
     */
    public final static Subset CONTROL_PICTURES = new Subset(37, '\u2400', '\u243F');
    /**
     * U+2440 - U+245F
     */
    public final static Subset OPTICAL_CHARACTER_RECOGNITION = new Subset(38, '\u2440', '\u245F');
    /**
     * U+2460 - U+24FF
     */
    public final static Subset ENCLOSED_ALPHANUMERICS = new Subset(39, '\u2460', '\u24FF');
    /**
     * U+2500 - U+257F
     */
    public final static Subset BOX_DRAWING = new Subset(40, '\u2500', '\u257F');
    /**
     * U+2580 - U+259F
     */
    public final static Subset BLOCK_ELEMENTS = new Subset(41, '\u2580', '\u259F');
    /**
     * U+25A0 - U+25FF
     */
    public final static Subset GEOMETRIC_SHAPES = new Subset(42, '\u25A0', '\u25FF');
    /**
     * U+2600 - U+26FF
     */
    public final static Subset MISCELLANEOUS_SYMBOLS = new Subset(43, '\u2600', '\u26FF');
    /**
     * U+2700 - U+27BF
     */
    public final static Subset DINGBATS = new Subset(44, '\u2700', '\u27BF');
    /**
     * U+3000 - U+303F
     */
    public final static Subset CJK_SYMBOLS_AND_PUNCTUATION = new Subset(45, '\u3000', '\u303F');
    /**
     * U+3040 - U+309F
     */
    public final static Subset HIRAGANA = new Subset(46, '\u3040', '\u309F');
    /**
     * U+30A0 - U+30FF
     */
    public final static Subset KATAKANA = new Subset(47, '\u30A0', '\u30FF');
    /**
     * U+3100 - U+312F
     */
    public final static Subset BOPOMOFO = new Subset(48, '\u3100', '\u312F');
    /**
     * U+3130 - U+318F
     */
    public final static Subset HANGUL_COMPATIBILITY_JAMO = new Subset(49, '\u3130', '\u318F');
    /**
     * U+3190 - U+319F
     */
    public final static Subset KANBUN = new Subset(50, '\u3190', '\u319F');
    /**
     * U+3200 - U+32FF
     */
    public final static Subset ENCLOSED_CJK_LETTERS_AND_MONTHS = new Subset(51, '\u3200', '\u32FF');
    /**
     * U+3300 - U+33FF
     */
    public final static Subset CJK_COMPATIBILITY = new Subset(52, '\u3300', '\u33FF');
    /**
     * U+4E00 - U+9FFF
     */
    public final static Subset CJK_UNIFIED_IDEOGRAPHS = new Subset(53, '\u4E00', '\u9FFF');
    /**
     * U+AC00 - U+D7A3
     */
    public final static Subset HANGUL_SYLLABLES = new Subset(54, '\uAC00', '\uD7A3');

    /**
     * SURROGATES combines the following 3 Unicode character blocks:<br>
     * High Surrogates: U+D800 - U+DB7F<br>
     * High Private Use Surrogates: U+DB80 - U+DBFF<br>
     * Low Surrogates: U+DC00 - U+DFFF
     */
    public final static Subset SURROGATES = new Subset(55, '\uD800', '\uDFFF');

    /**
     * U+E000 - U+F8FF
     */
    public final static Subset PRIVATE_USE = new Subset(56, '\uE000', '\uF8FF');
    /**
     * U+F900 - U+FAFF
     */
    public final static Subset CJK_COMPATIBILITY_IDEOGRAPHS = new Subset(57, '\uF900', '\uFAFF');
    /**
     * U+FB00 - U+FB4F
     */
    public final static Subset ALPHABETIC_PRESENTATION_FORMS = new Subset(58, '\uFB00', '\uFB4F');
    /**
     * U+FB50 - U+FDFF
     */
    public final static Subset ARABIC_PRESENTATION_FORMS_A = new Subset(59, '\uFB50', '\uFDFF');
    /**
     * U+FE20 - U+FE2F
     */
    public final static Subset COMBINING_HALF_MARKS = new Subset(60, '\uFE20', '\uFE2F');
    /**
     * U+FE30 - U+FE4F
     */
    public final static Subset CJK_COMPATIBILITY_FORMS = new Subset(61, '\uFE30', '\uFE4F');
    /**
     * U+FE50 - U+FE6F
     */
    public final static Subset SMALL_FORM_VARIANTS = new Subset(62, '\uFE50', '\uFE6F');
    /**
     * U+FE70 - U+FEFF
     */
    public final static Subset ARABIC_PRESENTATION_FORMS_B = new Subset(63, '\uFE70', '\uFEFF');
    /**
     * U+FF00 - U+FFEF
     */
    public final static Subset HALFWIDTH_AND_FULLWIDTH_FORMS = new Subset(64, '\uFF00', '\uFFEF');

    /**
     * SPECIALS combine the following two Unicode character blocks:<br>
     * SPECIALS: U+FEFF - U+FEFF<br>
     * SPECIALS: U+FFF0 - U+FFFF
     */
    public final static Subset SPECIALS = new Subset(65, '\uFEFF', '\uFFFF');


    /**
     * All Latin character blocks combined into one.
     * Informative block added by Sun.
     * U+0000 - U+024F
     */
    public final static Subset LATIN = new Subset('\u0000', '\u024F');
    /**
     * All Latin digits.
     * Informative block added by Sun.
     * U+0030 - U+0039
     */
    public final static Subset LATIN_DIGITS = new Subset('\u0030', '\u0039');
    /**
     * Halfwidth Katakana characters.  
     * Informative block added by Sun.
     * U+FF60 - U+FF9F
     */
    public final static Subset HALFWIDTH_KATAKANA = new Subset('\uFF60', '\uFF9F');
    /**
     * All Han characters used for writing traditional Chinese.
     * Loosely defined as Unicode characters which
     * have mappings to CNS 11643 or BigFive.
     * Informative block added by Sun.
     */
    public final static Subset TRADITIONAL_HANZI = new Subset();
    /**
     * All Han characters used for writing simplified Chinese.
     * Loosely defined as Unicode characters which
     * have mappings to GB 2312.
     * Informative block added by Sun.
     */
    public final static Subset SIMPLIFIED_HANZI = new Subset();
    /**
     * All Han characters used for writing Japanese.
     * Loosely defined as Unicode characters which
     * have mappings to JIS X 0208 or JIS X 0212.
     * Informative block added by Sun.
     */
    public final static Subset KANJI = new Subset();
    /**
     * All Han characters used for writing Korean.
     * Loosely defined as Unicode characters which
     * have mappings to KS C 5601.
     * Informative block added by Sun.
     */
    public final static Subset HANJA = new Subset();
  };

  private char value;		// the character represented by this class
  static final long serialVersionUID = 3786198910865385080L;

  /**
   * The minimum value the char data type can hold.
   * This value is U+0000.
   */
  public static final char MIN_VALUE = '\u0000';

  /**
   * The maximum value the char data type can hold.
   * This value is U+FFFF.
   */
  public static final char MAX_VALUE = '\uFFFF';

  /**
   * Smallest value allowed for radix arguments in Java.
   * This value is 2.
   */
  public static final int MIN_RADIX = 2;

  /**
   * Largest value allowed for radix arguments in Java.
   * This value is 36.
   */
  public static final int MAX_RADIX = 36;

  /**
   * Class object representing the primitive char data type.
   */
//    public static final Class TYPE = VMClassLoader.getPrimitiveClass("char");

  /**
   * Cn = Other, Not Assigned (Normative)
   */
  public static final byte UNASSIGNED = 0;
  /**
   * Lu = Letter, Uppercase (Informative)
   */
  public static final byte UPPERCASE_LETTER = 1;
  /**
   * Ll = Letter, Lowercase (Informative)
   */
  public static final byte LOWERCASE_LETTER = 2;
  /**
   * Lt = Letter, Titlecase (Informative)
   */
  public static final byte TITLECASE_LETTER = 3;
  /**
   * Lm = Letter, Modifier (Informative)
   */
  public static final byte MODIFIER_LETTER = 4;
  /**
   * Lo = Letter, Other (Informative)
   */
  public static final byte OTHER_LETTER = 5;
  /**
   * Mn = Mark, Non-Spacing (Normative)
   */
  public static final byte NON_SPACING_MARK = 6;
  /**
   * Me = Mark, Enclosing (Normative)
   */
  public static final byte ENCLOSING_MARK = 7;
  /**
   * Mc = Mark, Spacing Combining (Normative)
   */
  public static final byte COMBINING_SPACING_MARK = 8;
  /**
   * Nd = Number, Decimal Digit (Normative)
   */
  public static final byte DECIMAL_DIGIT_NUMBER = 9;
  /**
   * Nl = Number, Letter (Normative)
   */
  public static final byte LETTER_NUMBER = 10;
  /**
   * No = Number, Other (Normative)
   */
  public static final byte OTHER_NUMBER = 11;
  /**
   * Zs = Separator, Space (Normative)
   */
  public static final byte SPACE_SEPARATOR = 12;
  /**
   * Zl = Separator, Line (Normative)
   */
  public static final byte LINE_SEPARATOR = 13;
  /**
   * Zp = Separator, Paragraph (Normative)
   */
  public static final byte PARAGRAPH_SEPARATOR = 14;
  /**
   * Cc = Other, Control (Normative)
   */
  public static final byte CONTROL = 15;
  /**
   * Cf = Other, Format (Normative)
   */
  public static final byte FORMAT = 16;

  // 17 is skipped (don't ask me why)

  /**
   * Co = Other, Private Use (Normative)
   */
  public static final byte PRIVATE_USE = 18;
  /**
   * Cs = Other, Surrogate (Normative)
   */
  public static final byte SURROGATE = 19;
  /**
   * Pd = Punctuation, Dash (Informative)
   */
  public static final byte DASH_PUNCTUATION = 20;
  /**
   * Ps = Punctuation, Open (Informative)
   */
  public static final byte START_PUNCTUATION = 21;
  /**
   * Pe = Punctuation, Close (Informative)
   */
  public static final byte END_PUNCTUATION = 22;
  /**
   * Pc = Punctuation, Connector (Informative)
   */
  public static final byte CONNECTOR_PUNCTUATION = 23;
  /**
   * Po = Punctuation, Other (Informative)
   */
  public static final byte OTHER_PUNCTUATION = 24;
  /**
   * Sm = Symbol, Math (Informative)
   */
  public static final byte MATH_SYMBOL = 25;
  /**
   * Sc = Symbol, Currency (Informative)
   */
  public static final byte CURRENCY_SYMBOL = 26;
  /**
   * Sk = Symbol, Modifier (Informative)
   */
  public static final byte MODIFIER_SYMBOL = 27;
  /**
   * So = Symbol, Other (Informative)
   */
  public static final byte OTHER_SYMBOL = 28;

  static Block blocks[];	// block.uni
  static char tcs[][];		// titlecase.uni
  static byte[] unicodeData; // character.uni

  static CharAttr cachedCharAttr;

  // open up the Unicode attribute database and read the index into memory
  static {
    File cFile = ClassLoaderHelper.getSystemResourceAsFile(
                   "/gnu/java/locale/character.uni");
    File blockFile = ClassLoaderHelper.getSystemResourceAsFile(
                   "/gnu/java/locale/block.uni");
    File tcFile = ClassLoaderHelper.getSystemResourceAsFile(
                   "/gnu/java/locale/titlecase.uni");
    if (cFile == null || blockFile == null || tcFile == null)
      throw new Error("Cannot locate Unicode attribute database.");

    blocks = new Block[(int) blockFile.length() / 9];
    try {
      DataInputStream blockIS = 
	new DataInputStream(new FileInputStream(blockFile));
      for (int i = 0; i < blocks.length; i++) {
	char start = blockIS.readChar();
	char end = blockIS.readChar();
	boolean compressed = (blockIS.readUnsignedByte() != 0);
	int offset = blockIS.readInt();
	blocks[i] = new Block(start, end, compressed, offset);
      }
    } catch (IOException e) {
      throw new Error("Error reading block file: " + e);
    }

    tcs = new char[(int) tcFile.length() / 4][2];
    try {
      DataInputStream tcIS = new DataInputStream(new FileInputStream(tcFile));
      for (int i = 0; i < tcs.length; i++) {
	tcs[i][0] = tcIS.readChar();
	tcs[i][1] = tcIS.readChar();
      }
    } catch (IOException e) {
      throw new Error("Error reading titlecase file: " + e);
    }

    try {
      RandomAccessFile charFile = new RandomAccessFile(cFile, "r");
      unicodeData = new byte[(int)charFile.length()];
      charFile.readFully(unicodeData, 0, unicodeData.length);
    } catch (IOException e) {
      throw new Error("Unable to open Unicode character attribute file: " + e);
    }

    cachedCharAttr = new CharAttr((char)0, false, CONTROL, 65535, 
				  (char)0, (char)0);
  }

  /**
   * Grabs a character out of the Unicode attribute database.
   */
  private static CharAttr readChar(char ch) {
    CharAttr cached = cachedCharAttr;
    if (cached.ch == ch)
      return cached;

    int i = getBlock(ch);
    if (i == -1) return new CharAttr(ch, false, UNASSIGNED, 65535, ch, ch);
    Block b = blocks[i];
    int offset = (b.compressed) ? b.offset : (b.offset + (ch-b.start)*7);
    int byte7 = unicodeData[offset] & 0xFF;
    boolean noBreakSpace = ((byte7 >> 5 & 0x1) == 1);
    int category = byte7 & 0x1F;
    int numericalDecimalValue = (((unicodeData[offset+1] & 0xFF) << 8) + 
				 (unicodeData[offset+2] & 0xFF));
    char uppercase = (char)(((unicodeData[offset+3] & 0xFF) << 8) + 
			    (unicodeData[offset+4] & 0xFF));
    char lowercase = (char)(((unicodeData[offset+5] & 0xFF) << 8) + 
			    (unicodeData[offset+6] & 0xFF));

    CharAttr ca = new CharAttr(ch, noBreakSpace, category, 
			       numericalDecimalValue, uppercase, lowercase);
    cachedCharAttr = ca;
    return ca;
  }

  /**
   * Locates which block a character's information resides in
   */
  private static int getBlock(char ch) {
    // simple binary search
    int low = 0;
    int hi = blocks.length-1;
    int mid = 0;
    while (low <= hi) {
      mid = (low + hi) >> 1;
      Block b = blocks[mid];
      if (ch < b.start)
	hi = mid - 1;
      else if (ch > b.end)
	low = mid + 1;
      else return mid;
    }
    return -1;
  }

  /**
   * Wraps up a character.
   *
   * @param value the character to wrap
   */
  public Character(char value) {
    this.value = value;
  }

  /**
   * Returns the character which has been wrapped by this class.
   *
   * @return the character wrapped
   */
  public char charValue() {
    return value;
  }

  /**
   * Returns the numerical value (unsigned) of the wrapped character.
   * Range of returned values: 0x0000-0xFFFF.
   *
   * @return an integer representing the numerical value of the
   * wrapped character
   */
  public int hashCode() {
    return (int) value;
  }

  /**
   * Determines if an object is equal to this object.
   *
   * @param o object to compare
   *
   * @return true if o is an instance of Character and has the
   * same wrapped character, else false
   */
  public boolean equals(Object o) {
    return (o instanceof Character && value == ((Character)o).value);
  }

  /**
   * Converts the wrapped character into a String.
   *
   * @return a String containing one character -- the wrapped character
   * of this class
   */
  public String toString() {
    return String.valueOf(value);
  }

  /**
   * Returns the Unicode general category property of a character.
   *
   * @param ch character from which the general category property will
   * be retrieved
   *
   * @return the character category property of ch as an integer
   *
   * @see java.lang.Character#UNASSIGNED
   * @see java.lang.Character#UPPERCASE_LETTER
   * @see java.lang.Character#LOWERCASE_LETTER
   * @see java.lang.Character#TITLECASE_LETTER
   * @see java.lang.Character#MODIFIER_LETTER
   * @see java.lang.Character#OTHER_LETTER
   * @see java.lang.Character#NON_SPACING_MARK
   * @see java.lang.Character#ENCLOSING_MARK
   * @see java.lang.Character#COMBINING_SPACING_MARK
   * @see java.lang.Character#DECIMAL_DIGIT_NUMBER
   * @see java.lang.Character#LETTER_NUMBER
   * @see java.lang.Character#OTHER_NUMBER
   * @see java.lang.Character#SPACE_SEPARATOR
   * @see java.lang.Character#LINE_SEPARATOR
   * @see java.lang.Character#PARAGRAPH_SEPARATOR
   * @see java.lang.Character#CONTROL
   * @see java.lang.Character#FORMAT
   * @see java.lang.Character#PRIVATE_USE
   * @see java.lang.Character#SURROGATE
   * @see java.lang.Character#DASH_PUNCTUATION
   * @see java.lang.Character#START_PUNCTUATION
   * @see java.lang.Character#END_PUNCTUATION
   * @see java.lang.Character#CONNECTOR_PUNCTUATION
   * @see java.lang.Character#OTHER_PUNCTUATION
   * @see java.lang.Character#MATH_SYMBOL
   * @see java.lang.Character#CURRENCY_SYMBOL
   * @see java.lang.Character#MODIFIER_SYMBOL
   */
  public static int getType(char ch) {
    return readChar(ch).getType();
  }
  
  /**
   * Returns the Unicode numeric value property of a character.
   * If the character lacks a numeric value property, -1 is returned.
   * If the character has a numeric value property which is not
   * representable as a nonnegative integer, -2 is returned.
   *
   * @param ch character from which the numeric value property will
   * be retrieved
   *
   * @return the numeric value property of ch, or -1 if it does not exist, or
   * -2 if it is not representable as a nonnegative integer
   */
  public static int getNumericValue(char ch) {
    return readChar(ch).getNumericValue();
  }

  /**
   * Determines if a character is a Unicode decimal digit.
   * <br>
   * Unicode decimal digit = [Nd]
   *
   * @param ch character to test
   *
   * @return true if ch is a Unicode decimal digit, else false
   */
  public static boolean isDigit(char ch) {
    return (getType(ch) == DECIMAL_DIGIT_NUMBER);
  }

  /**
   * Converts a character into a digit of the specified radix.
   * <br>
   * character argument boundary = [Nd]|U+0041-U+005A|U+0061-007A
   *
   * @param ch character to convert into a digit
   * @param radix radix in which ch is a digit
   *
   * @return digit which ch represents in radix, or -1
   * if radix < MIN_RADIX or radix > MAX_RADIX or ch is not
   * a valid digit in radix
   *
   * @see java.lang.Character#MIN_RADIX
   * @see java.lang.Character#MAX_RADIX
   */
  public static int digit(char ch, int radix) {
    if (radix < MIN_RADIX || radix > MAX_RADIX)
      return -1;
    CharAttr attr = readChar(ch);
    if (attr.getType() == DECIMAL_DIGIT_NUMBER &&
	attr.getNumericValue() < radix)
      return attr.getNumericValue();
    if (ch >= 'A' && ch <= 'Z' && ch < radix+'A'-10)
      return ch-'A'+10;
    if (ch >= 'a' && ch <= 'z' && ch < radix+'a'-10)
      return ch-'a'+10;
    return -1;
  }

  /**
   * Converts a digit into a character which represents that digit
   * in a specified radix.
   * <br>
   * return value boundary = U+0030-U+0039|U+0061-U+007A
   * 
   * @param digit digit to be converted into a character
   * @param radix radix of digit
   *
   * @return character representing digit in radix, or U+0000 if
   * radix < MIN_RADIX or radix > MAX_RADIX or digit < 0 or
   * digit >= radix
   *
   * @see java.lang.Character#MIN_RADIX
   * @see java.lang.Character#MAX_RADIX
   */
  public static char forDigit(int digit, int radix) {
    if (radix < MIN_RADIX || radix > MAX_RADIX ||
	digit < 0 || digit >= radix)
      return '\u0000';
    return (char) ((digit < 10) ? ('0' + digit) : ('a'+digit-10));
  }

  /**
   * Determines if a character is a Unicode space character.
   * <br>
   * Unicode space = [Zs]
   *
   * @param ch character to test
   *
   * @return true if ch is a Unicode space, else false
   */
  public static boolean isSpaceChar(char ch) {
    return (getType(ch) == SPACE_SEPARATOR);
  }

  /**
   * Determines if a character is a Java space, per JLS 20.5.19.
   * <br>
   * Java space = U+0020|U+0009|U+000A|U+000C|U+000D
   *
   * @param ch character to test
   *
   * @return true if ch is a Java space, else false
   * 
   * @deprecated Replaced by isWhitespace()
   */
  public static boolean isSpace(char ch) {
    return ((ch == ' ') || (ch == '\t') || (ch == '\n') || (ch == '\f') || 
	    (ch == '\r'));
  }

  /**
   * Determines if a character is Java whitespace.
   * <br>
   * Java whitespace = ([Zs]¬Nb)|[Zl]|[Zp]|U+0009-U+000D|U+001C-U+001F
   *
   * @param ch character to test
   *
   * @return true if ch is Java whitespace, else false
   */
  public static boolean isWhitespace(char ch) {
    CharAttr attr = readChar(ch);
    int category = attr.getType();
    return ((category == SPACE_SEPARATOR && !attr.isNoBreakSpace()) ||
	    category == LINE_SEPARATOR ||
	    category == PARAGRAPH_SEPARATOR ||
	    //(ch >= '\u0009' && ch <= '\ u000D') ||
	    (ch >= '\u0009' && ch <= '\r') || // Change to make javadep work
	    (ch >= '\u001C' && ch <= '\u001F'));
  }
    
  /**
   * Determines if a character has the ISO Control property.
   * <br>
   * ISO Control = U+0000-U+001F|U+0074-U+0094
   *
   * @param ch character to test
   *
   * @return true if ch is an ISO Control character, else false
   */
  public static boolean isISOControl(char ch) {
    return ((ch >= '\u0000' && ch <= '\u001F') ||
	    (ch >= '\u007F' && ch <= '\u009F'));
  }

  /**
   * Determines if a character is part of the Unicode Standard.
   * <br>
   * defined = ¬[Cn]
   *
   * @param ch character to test
   *
   * @return true if ch is a Unicode character, else false
   */
  public static boolean isDefined(char ch) {
    return (getBlock(ch) != -1);
  }

  /**
   * Determines if a character is a Unicode letter.
   * <br>
   * letter = [Lu]|[Ll]|[Lt]|[Lm]|[Lo]|[Nl]
   *
   * @param ch character to test
   *
   * @return true if ch is a Unicode letter, else false
   */
  public static boolean isLetter(char ch) {
    int category = getType(ch);
    return ((category >= UPPERCASE_LETTER && category <= OTHER_LETTER) ||
	    (category == LETTER_NUMBER));
  }

  /**
   * Determines if a character is a Unicode letter or a Unicode digit.
   * <br>
   * letter or digit = [Lu]|[Ll]|[Lt]|[Lm]|[Lo]|[Nl]|[Nd]
   *
   * @param ch character to test
   *
   * @return true if ch is a Unicode letter or a Unicode digit, else false
   */
  public static boolean isLetterOrDigit(char ch) {
    int category = getType(ch);
    return ((category >= UPPERCASE_LETTER && category <= OTHER_LETTER) || 
	    (category == LETTER_NUMBER) ||
	    (category == DECIMAL_DIGIT_NUMBER));
  }

  /**
   * Determines if a character is a Unicode lowercase letter.
   * <br>
   * lowercase = [Ll]
   *
   * @param ch character to test
   *
   * @return true if ch is a Unicode lowercase letter, else false
   */
  public static boolean isLowerCase(char ch) {
    return (getType(ch) == LOWERCASE_LETTER);
  }

  /**
   * Converts a Unicode character into its lowercase equivalent mapping.
   * If a mapping does not exist, then the character passed is returned.
   *
   * @param ch character to convert to lowercase
   *
   * @return lowercase mapping of ch, or ch if lowercase mapping does
   * not exist.
   */
  public static char toLowerCase(char ch) {
    return readChar(ch).toLowerCase();
  }
  
  /**
   * Determines if a character is a Unicode uppercase letter.
   * <br>
   * uppercase = [Lu]
   *
   * @param ch character to test
   *
   * @return true if ch is a Unicode uppercase letter, else false
   */
  public static boolean isUpperCase(char ch) {
    return (getType(ch) == UPPERCASE_LETTER);
  }

  /**
   * Converts a Unicode character into its uppercase equivalent mapping.
   * If a mapping does not exist, then the character passed is returned.
   *
   * @param ch character to convert to uppercase
   *
   * @return uppercase mapping of ch, or ch if uppercase mapping does
   * not exist.
   */
  public static char toUpperCase(char ch) {
    return readChar(ch).toUpperCase();
  }

  /**
   * Determines if a character is a Unicode titlecase letter.
   * <br>
   * titlecase = [Lt]
   *
   * @param ch character to test
   *
   * @return true if ch is a Unicode titlecase letter, else false
   */
  public static boolean isTitleCase(char ch) {
    return (getType(ch) == TITLECASE_LETTER);
  }

  /**
   * Converts a Unicode character into its titlecase equivalent mapping.
   * If a mapping does not exist, then the character passed is returned.
   *
   * @param ch character to convert to titlecase
   *
   * @return titlecase mapping of ch, or ch if titlecase mapping does
   * not exist.
   */
  public static char toTitleCase(char ch) {
    for (int i = 0; i < tcs.length; i++)
      if (tcs[i][0] == ch)
	return tcs[i][1];
    return toUpperCase(ch);
  }

  /**
   * Determines if a character can start a Java identifier.
   * This method does not implement JLS 20.5.17, but instead
   * implements the algorithm used by isJavaIdentifierStart().
   * 
   * @param ch character to test
   *
   * @return true if ch can start a Java identifier, else false
   *
   * @deprecated Replaced by isJavaIdentifierStart()
   *
   * @see java.lang.Character#isJavaIdentifierStart
   */
  public static boolean isJavaLetter(char ch) {
    return isJavaIdentifierStart(ch);
  }

  /**
   * Determines if a character can follow the first letter in
   * a Java identifier.  This method does not implement JLS 20.5.18,
   * but instead implements the algorithm used by isJavaIdentifierPart().
   *
   * @param ch character to test
   *
   * @return true if ch can follow the first letter in a Java identifier,
   * else false
   *
   * @deprecated Replaced by isJavaIdentifierPart()
   *
   * @see java.lang.Character#isJavaIdentifierPart
   */
  public static boolean isJavaLetterOrDigit(char ch) {
    return isJavaIdentifierPart(ch);
  }

  /**
   * Determines if a character can start a Java identifier.
   * <br>
   * Java identifier start = [Lu]|[Ll]|[Lt]|[Lm]|[Lo]|[Nl]|[Sc]|[Pc]
   * 
   * @param ch character to test
   *
   * @return true if ch can start a Java identifier, else false
   */
  public static boolean isJavaIdentifierStart(char ch) {
    int category = getType(ch);
    return ((category >= UPPERCASE_LETTER && category <= OTHER_LETTER) ||
	    (category == CURRENCY_SYMBOL) ||
	    (category == CONNECTOR_PUNCTUATION));
  }

  /**
   * Determines if a character can follow the first letter in
   * a Java identifier.
   * <br>
   * Java identifier extender = 
   *   [Lu]|[Ll]|[Lt]|[Lm]|[Lo]|[Nl]|[Sc]|[Pc][Mn]|[Mc]|[Nd]|[Cf]
   *
   * @param ch character to test
   *
   * @return true if ch can follow the first letter in a Java identifier,
   * else false
   */
  public static boolean isJavaIdentifierPart(char ch) {
    int category = getType(ch);
    return ((category >= UPPERCASE_LETTER && category <= OTHER_LETTER) ||
	    (category == LETTER_NUMBER) ||
	    (category == CURRENCY_SYMBOL) ||
	    (category == NON_SPACING_MARK) ||
	    (category == COMBINING_SPACING_MARK) ||
	    (category == DECIMAL_DIGIT_NUMBER) ||
	    (category == CONNECTOR_PUNCTUATION) ||
	    (category == FORMAT));
  }  
  
  /**
   * Determines if a character can start a Unicode identifier.  Only
   * letters can start a Unicode identifier.
   * <br>
   * Unicode identifier start = [Lu]|[Ll]|[Lt]|[Lm]|[Lo]|[Nl]
   * 
   * @param ch character to test
   *
   * @return true if ch can start a Unicode identifier, else false
   */
  public static boolean isUnicodeIdentifierStart(char ch) {
    return isLetter(ch);
  }

  /**
   * Determines if a character can follow the first letter in
   * a Unicode identifier.
   * <br>
   * Unicode identifier extender = 
   *   [Lu]|[Ll]|[Lt]|[Lm]|[Lo]|[Nl]|[Mn]|[Mc]|[Nd]|[Pc]|[Cf]
   *
   * @param ch character to test
   *
   * @return true if ch can follow the first letter in a Unicode identifier,
   * else false
   */
  public static boolean isUnicodeIdentifierPart(char ch) {
    int category = getType(ch);
    return ((category >= UPPERCASE_LETTER && category <= OTHER_LETTER) ||
	    (category == LETTER_NUMBER) ||
	    (category == NON_SPACING_MARK) ||
	    (category == COMBINING_SPACING_MARK) ||
	    (category == DECIMAL_DIGIT_NUMBER) ||
	    (category == CONNECTOR_PUNCTUATION) ||
	    (category == FORMAT));
  }

  /**
   * Determines if a character is ignorable in a Unicode identifier.
   * <br>
   * Unicode identifier ignorable = [Cf]
   *
   * @param ch character to test
   *
   * @return true if ch is ignorable in a Unicode identifier,
   * else false
   */
  public static boolean isIdentifierIgnorable(char ch) {
    return (getType(ch) == FORMAT);
  }

  /**
   * Determines the Unicode character block that a character belongs to.
   *
   * @param ch character for which the Unicode character block will
   * be retrieved
   *
   * @return the Unicode character block which ch belongs to, or null
   * if ch does not belong to any Unicode character block
   *
   * @see java.lang.Character.Subset
   */
  public static Character.Subset getUnicodeBlock(char ch) {
    return Character.Subset.getBlock(ch);
  }

  /**
   * Compares another Character to this Character, numerically.
   *
   * @param anotherCharacter Character to compare with this Character
   *
   * @return a negative integer if this Character is less than
   * anotherCharacter, zero if this Character is equal to anotherCharacter, or
   * a positive integer if this Character is greater than anotherCharacter
   *
   * @exception NullPointerException if anotherCharacter is null
   */
  public int compareTo(Character anotherCharacter)
    throws NullPointerException {
    return value - anotherCharacter.value;
  }

  /**
   * Compares an object to this Character.  Assuming the object is a
   * Character object, this method performs the same comparison as
   * compareTo(Character).
   *
   * @param o object to compare
   *
   * @return a negative integer if this Character is less than o,
   * zero if this Character is equal to o, or a positive integer if this
   * Character is greater than o
   *
   * @exception ClassCastException if o is not a Character object
   * @exception NullPointerException if o is null
   *
   * @see java.lang.Character#compareTo(Character)
   */
  public int compareTo(Object o) 
    throws ClassCastException, NullPointerException {
    return compareTo((Character)o);
  }
}

/**
 * Represents an entry in block.uni
 */
final class Block {
  char start;
  char end;
  boolean compressed;
  int offset;

  Block(char start, char end, boolean compressed, int offset) {
    this.start = start;
    this.end = end;
    this.compressed = compressed;
    this.offset = offset;
  }
}

/**
 * Represents all the attributes stored for a character.
 */
final class CharAttr {
  boolean noBreakSpace;
  int category;
  int numericalDecimalValue;
  char uppercase;
  char lowercase;
  char ch;

  CharAttr(char ch, boolean noBreakSpace, int category, 
	   int numericalDecimalValue,
	   char uppercase, char lowercase) {
    this.noBreakSpace = noBreakSpace;
    this.category = category;
    this.numericalDecimalValue = numericalDecimalValue;
    this.uppercase = uppercase;
    this.lowercase = lowercase;
    this.ch = ch;
  }

  boolean isNoBreakSpace() {
    return noBreakSpace;
  }

  int getNumericValue() {
    if (numericalDecimalValue == 65535) return -1;
    if (numericalDecimalValue == 65534) return -2;
    return numericalDecimalValue;
  }

  int getType() {
    return category;
  }

  char toUpperCase() {
    return (uppercase != 0) ? uppercase : ch;
  }

  char toLowerCase() {
    return (lowercase != 0) ? lowercase : ch;
  }
}
