package java.awt.font;

/**
 * @author John Leuner <jewel@debian.org>
 *
 *
 */

public final class TextHitInfo {

  public int getCharIndex()
  {
    return -1;
  }

  public boolean isLeadingEdge()
  {
    return false;
  }

  public int getInsertionIndex()
  {
    return -1;
  }

  public int hashCode()
  {
    return getCharIndex();
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof TextHitInfo)
      return this.equals((TextHitInfo) obj);
    return false;
  }

  public boolean equals(TextHitInfo hitInfo)
  {
    return (getCharIndex() == hitInfo.getCharIndex()) && (isLeadingEdge() == hitInfo.isLeadingEdge());
  }

  public static TextHitInfo leading(int charIndex)
  {
    return new TextHitInfo();
  }

  public static TextHitInfo trailing(int charIndex)
  {
    return new TextHitInfo();
  }

  public static TextHitInfo beforeOffset(int offset)
  {
    return new TextHitInfo();
  }

  public static TextHitInfo afterOffset(int offset)
  {
    return new TextHitInfo();
  }

  public TextHitInfo getOtherHit()
  {
    return new TextHitInfo();
  }

  public TextHitInfo getOffsetHit(int offset)
  {
    return new TextHitInfo();
  }

}
