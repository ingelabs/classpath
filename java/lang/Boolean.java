package java.lang;

import java.io.Serializable;

public final class Boolean implements Serializable
{
  static final long serialVersionUID = -3665804199014368530L;

  public static final Boolean TRUE  = new Boolean(true);
  public static final Boolean FALSE = new Boolean(false);
  public static final Class TYPE = null; /* XXX */

  private boolean value;
    
  public Boolean(boolean value) {
    this.value = value;
  }

  public Boolean(String s) {
    value = (s != null && s.equalsIgnoreCase("true"));
  }

  public boolean booleanValue() {
    return value;
  }

  public static Boolean valueOf(String s) {
    return new Boolean(s);
  }

  public int hashCode() {
    return (value) ? 1231 : 1237;
  }

  public boolean equals(Object obj) {
    if (obj == null || (!(obj instanceof Boolean))) return false;
    return (value == ((Boolean)obj).booleanValue());
  }

  public static boolean getBoolean(String name) {
    String val = System.getProperty(name);
    return (val != null && val.equalsIgnoreCase("true"));
  }
}
