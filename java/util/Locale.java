package java.util;

import java.io.Serializable;
import java.io.IOException;

/**
 * Locales represent a specific country and culture.
 * <br><br>
 * Classes which can be passed a Locale object tailor their information 
 * for a given locale.  For instance, currency number formatting is 
 * handled differently for the USA and France.
 * <br><br>
 * Locales are made up of a language code, a country code, and an optional
 * set of variant strings.
 * <br><br>
 * Language codes are represented by
 * <a href="http://www.indigo.ie/egt/standards/iso639/iso639-1-en.html">ISO 639:1988</a>
 * w/ additions from ISO 639/RA Newsletter No. 1/1989
 * and a decision of the Advisory Committee of ISO/TC39 on
 * August 8, 1997.
 * <br><br>
 * Country codes are represented by 
 * <a href="ftp://ftp.ripe.net/iso3166-countrycodes">ISO 3166</a>.
 * <br><br>
 * Variant strings are vendor and browser specific.  Standard variant
 * strings include "POSIX" for POSIX, "WIN" for MS-Windows, and "MAC" for
 * Macintosh.  When there is more than one variant string, they must
 * be separated by an underscore (U+005F).
 * <br><br>
 * In order to facilitate easy modification of the locales supported by
 * GNU Classpath, language and country code information is stored in
 * resource files.
 * <br><br>
 * The default locale is determined by the values of the system properties
 * user.language, user.region, and user.variant.
 */
public final class Locale implements Cloneable, Serializable {
  private String country;
  private String language;
  private String variant;

  private transient int hashcode;

  private static Locale defaultL;
  private static Properties ISO639A3, ISO3166A3, new639;

  static {
    try {
      ISO639A3 = new Properties();
      ISO639A3.load(Locale.class.getResourceAsStream("iso639-a3.properties"));
      ISO3166A3 = new Properties();
      ISO3166A3.load(Locale.class.getResourceAsStream("iso3166-a3.properties"));
      new639 = new Properties();
      new639.load(Locale.class.getResourceAsStream("iso639-a2-old.properties"));
    } catch (IOException e) {
      throw new Error("Locale init error: " + e);
    }
    // setup the default locale
    defaultL = new Locale(System.getProperty("user.language", "en"),
			  System.getProperty("user.region", ""),
			  System.getProperty("user.variant", ""));
  }

  /**
   * Locale which represents the English language.
   */
  public static final Locale ENGLISH = new Locale("en", "");
  /**
   * Locale which represents the English language.
   */
  public static final Locale FRENCH = new Locale("fr", "");
  /**
   * Locale which represents the German language.
   */
  public static final Locale GERMAN = new Locale("de", "");
  /**
   * Locale which represents the Italian language.
   */
  public static final Locale ITALIAN = new Locale("it", "");
  /**
   * Locale which represents the Japanese language.
   */
  public static final Locale JAPANESE = new Locale("ja", "");
  /**
   * Locale which represents the Korean language.
   */
  public static final Locale KOREAN = new Locale("ko", "");
  /**
   * Locale which represents the Chinese language.
   */
  public static final Locale CHINESE = new Locale("zh", "");
  /**
   * Locale which represents the Chinese language as used in China.
   */
  public static final Locale SIMPLIFIED_CHINESE = new Locale("zh", "CN");
  /**
   * Locale which represents the Chinese language as used in Taiwan.
   * Same as TAIWAN Locale.
   */
  public static final Locale TRADITIONAL_CHINESE = new Locale("zh", "TW");
  /**
   * Locale which represents France.
   */
  public static final Locale FRANCE = new Locale("fr", "FR");
  /**
   * Locale which represents Germany.
   */
  public static final Locale GERMANY = new Locale("de", "DE");
  /**
   * Locale which represents Italy.
   */
  public static final Locale ITALY = new Locale("it", "IT");
  /**
   * Locale which represents Japan.
   */
  public static final Locale JAPAN = new Locale("ja", "JP");
  /**
   * Locale which represents Korea.
   */
  public static final Locale KOREA = new Locale("ko", "KR");
  /**
   * Locale which represents China.
   * Same as SIMPLIFIED_CHINESE Locale.
   */
  public static final Locale CHINA = SIMPLIFIED_CHINESE;
  /**
   * Locale which represents the People's Republic of China.
   * Same as CHINA Locale.
   */
  public static final Locale PRC = CHINA;
  /**
   * Locale which represents Taiwan.
   * Same as TRADITIONAL_CHINESE Locale.
   */
  public static final Locale TAIWAN = TRADITIONAL_CHINESE;
  /**
   * Locale which represents the United Kingdom.
   */
  public static final Locale UK = new Locale("en", "GB");
  /**
   * Locale which represents the United States.
   */
  public static final Locale US = new Locale("en", "US");
  /**
   * Locale which represents the English speaking portion of Canada.
   */
  public static final Locale CANADA = new Locale("en", "CA");
  /**
   * Locale which represents the French speaking portion of Canada.
   */
  public static final Locale CANADA_FRENCH = new Locale("fr", "CA");

  /**
   * Constructs a new locale using an ISO 639 A2 language code,
   * an ISO 3166 A2 country code, and an uppercase variant string.
   */
  public Locale(String language, String country, String variant) {
    this.language = language.toLowerCase();

    String old639 = new639.getProperty(this.language);
    if (old639 != null)
      language = old639;

    this.country = country.toUpperCase();
    this.variant = variant.toUpperCase();
  }

  /**
   * Constructs a new locale using an ISO 639 A2 language code and
   * an ISO 3166 A2 country code.
   */
  public Locale(String language, String country) {
    this(language, country, "");
  }

  /**
   * Returns the default locale.
   *
   * @return default locale
   */
  public static Locale getDefault() {
    return defaultL;
  }

  /**
   * Sets the default locale.
   * Generally called once at the start of a program.
   *
   * @param newLoc the new default locale
   */
  public static void setDefault(Locale newLoc) {
    defaultL = newLoc;
  }

  /**
   * Clones this locale.
   *
   * @return a deep copy of this locale
   */
  public Object clone() {
    Object o = null;
    try {
      o = super.clone();
    } catch (CloneNotSupportedException e) { }
    return o;
  }

  /**
   * Compares a locale to this locale.
   *
   * @param o Locale to compare to this
   *
   * @return true if o is a Locale and has the same language, country,
   * and variant portions as this locale, else false
   */
  public boolean equals(Object o) {
    if (!(o instanceof Locale)) return false;
    Locale ol = (Locale) o;
    return (language.equals(ol.language) && 
	    country.equals(ol.country) &&
	    variant.equals(ol.variant));
  }

  /**
   * Gets the country code for this locale.
   *
   * @return country code portion of this locale, or an empty String if
   * none exists
   */
  public String getCountry() {
    return country;
  }

  /**
   * Gets the language code for this locale.
   *
   * @return language code portion of this locale, or an empty String if
   * none exists
   */
  public String getLanguage() {
    return language;
  }

  /** 
   * Gets the variant string portion of this locale.
   *
   * @return variant code portion of this locale, or an empty String if
   * none exists
   */
  public String getVariant() {
    return variant;
  }

  /**
   * Gets the country name suitable for display to the user, formatted
   * for a specified locale.
   *
   * @param inLocale locale to use for formatting
   *
   * @return name of the country formatted for inLocale, or
   * if inLocale does not have formatting information for the country,
   * the country formatted for English, or if the English locale
   * does not have information for formatting this country, then
   * the country code is returned, or the empty string if this locale
   * has no country code.
   */
  public String getDisplayCountry(Locale inLocale) {
    ResourceBundle ISO3166 = ResourceBundle.getBundle("java.util.iso3166",
						      inLocale);
    try {
      return ISO3166.getString(country);
    } catch (MissingResourceException e) {
      return country;
    }
  }

  /**
   * Gets the country name suitable for display to the user, formatted
   * for the default locale.
   *
   * @return name of the country formatted for the default locale, or
   * if the default locale does not have formatting information for the 
   * country, the country formatted for English, or if the English locale
   * does not have information for formatting this country, then
   * the country code is returned, or the empty string if this locale
   * has no country code.
   */
  public String getDisplayCountry() {
    return getDisplayCountry(defaultL);
  }

  /**
   * Gets the language name suitable for display to the user, formatted
   * for a specified locale.
   *
   * @param inLocale locale to use for formatting
   *
   * @return name of the language formatted for inLocale, or
   * if inLocale does not have formatting information for the language,
   * the language formatted for English, or if the English locale
   * does not have information for formatting this language, then
   * the language code is returned, or the empty string if this locale
   * has no language code.
   */
  public String getDisplayLanguage(Locale inLocale) {
    ResourceBundle ISO639 = ResourceBundle.getBundle("java.util.iso639",
						     inLocale);
    try {
      return ISO639.getString(language);
    } catch (MissingResourceException e) {
      return language;
    }
  }

  /**
   * Gets the language name suitable for display to the user, formatted
   * for the default locale.
   *
   * @return name of the language formatted for the default locale, or
   * if the default locale does not have formatting information for the 
   * language, the language formatted for English, or if the English locale
   * does not have information for formatting this language, then
   * the language code is returned, or the empty string if this locale
   * has no language code.
   */
  public String getDisplayLanguage() {
    return getDisplayLanguage(defaultL);
  }


  /**
   * Gets the variant names suitable for display to the user, formatted
   * for a specified locale.
   *
   * @param inLocale locale to use for formatting
   *
   * @return name of the variants formatted for inLocale, or
   * if inLocale does not have formatting information for the variants,
   * the variants formatted for English, or the empty string if this locale
   * has no variant names.
   */
  public String getDisplayVariant(Locale inLocale) {
    return variant;
  }

  /**
   * Gets the variant names suitable for display to the user, formatted
   * for the default locale.
   *
   * @return name of the variants formatted for the default locale, or
   * if the default locale does not have formatting information for the 
   * variants, the variants formatted for English, or the empty string 
   * if this locale has no variant names.
   */
  public String getDisplayVariant() {
    return getDisplayVariant(defaultL);
  }

  /**
   * Gets all local components suitable for display to the user, formatted
   * for the default locale.  For the language component, getDisplayLanguage
   * is called.  For the country component, getDisplayCountry is called.
   * For the variant set component, getDisplayVariant is called.
   * <br><br>
   * The returned String will be one of the following forms:<br>
   * <pre>
   * language (country, variant)
   * language (country)
   * language (variant)
   * country (variant)
   * language
   * country
   * variant
   * </pre>
   *
   * @return String version of this locale, suitable for display to the
   * user
   */
  public String getDisplayName() {
    return getDisplayName(defaultL);
  }

  /**
   * Gets all local components suitable for display to the user, formatted
   * for a specified locale.  For the language component, 
   * getDisplayLanguage(Locale) is called.  For the country component, 
   * getDisplayCountry(Locale) is called.  For the variant set component, 
   * getDisplayVariant(Locale) is called.
   * <br><br>
   * The returned String will be one of the following forms:<br>
   * <pre>
   * language (country, variant)
   * language (country)
   * language (variant)
   * country (variant)
   * language
   * country
   * variant
   * </pre>
   *
   * @param inLocale locale to use for formatting
   *
   * @return String version of this locale, suitable for display to the
   * user
   */
  public String getDisplayName(Locale inLocale) {
    String locale[] = new String[3];
    locale[0] = getDisplayLanguage(inLocale);
    locale[1] = getDisplayCountry(inLocale);
    locale[2] = getDisplayVariant(inLocale);

    // determine how many elements are empty
    int numEmpty = 0;
    for (int i = 0; i < 3; i++)
      if (locale[i].length() == 0)
	numEmpty++;

    switch (numEmpty) {
    case 3:
      return "";
    case 2:
      for (int i = 0; i < 3; i++)
	if (locale[i].length() != 0)
	  return locale[i];
    case 1:
      StringBuffer sb = new StringBuffer();
      boolean first = true;
      for (int i = 0; i < 3; i++)
	if (locale[i].length() != 0) {
	  sb.append(locale[i]);
	  if (first) {
	    sb.append(" (");
	    first = false;
	  } else {
	    sb.append(")");
	    break;
	  }
	}
      return sb.toString();
    case 0:
      return locale[0] + " (" + locale[1] + ", " + locale[2] + ")";
    }
    return "";
  }

  /**
   * Gets the local components, formatted for display to a programmer.
   *
   * @return ISO 639 A2 code + "_" + ISO 3166 A2 code + "_" + variant
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(language);
    if (country.length() != 0) {
      if (sb.length() > 0)
	sb.append('_');
      sb.append(country);
    }
    if (variant.length() != 0) {
      if (sb.length() > 0)
	sb.append('_');
      sb.append(variant);
    }
    return sb.toString();
  }

  /**
   * Gets the ISO 639-2/T code for the language of this locale.
   *
   * @return ISO 639-2/T code, or the empty string if this locale
   * does not specify a language
   *
   * @exception MissingResourceException if the 639-1 language code
   * does not have a 639-2/T mapping
   */
  public String getISO3Language() throws MissingResourceException {
    if (language.length() == 0) return language;
    String a3 = ISO639A3.getProperty(language);
    if (a3 == null)
      throw new 
	MissingResourceException("No ISO 639-2/T code for " + language,
				 "java.util.iso639-a3",
				 language);
    return a3;
  }

  /**
   * Gets the ISO 3166 A3 code for the country of this locale.
   *
   * @return ISO 3166 A3 code, or the empty string if this locale
   * does not specify a country
   *
   * @exception MissingResourceException if the 3166 A2 language code
   * does not have a 3166 A3 mapping
   */
  public String getISO3Country() throws MissingResourceException {
    if (country.length() == 0) return country;
    String a3 = ISO3166A3.getProperty(country);
    if (a3 == null)
      throw new 
	MissingResourceException("No ISO 3166 A3 code for " + country,
				 "java.util.iso3166-a3",
				 country);
    return a3;
  }

  /**
   * Calculates the hash code for this locale.
   * Hash codes are cached.
   *
   * @return the hash code
   */
  public int hashCode() {
    if (hashcode != 0) return hashcode;
    hashcode = (language + country + variant).hashCode();
    return hashcode;
  }
}
