package gnu.java.nio.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/**
 * Decoder for UTF-16, UTF-15LE, and UTF-16BE.
 *
 * @author Jesse Rosenstock
 */
final class UTF_16Decoder extends CharsetDecoder
{
  // byte orders
  static final int BIG_ENDIAN = 0;
  static final int LITTLE_ENDIAN = 1;
  static final int UNKNOWN_ENDIAN = 2;

  private static final char BYTE_ORDER_MARK = '\uFEFF';
  private static final char REVERSED_BYTE_ORDER_MARK = '\uFFFE';

  private final int originalByteOrder;
  private int byteOrder;

  UTF_16Decoder (Charset cs, int byteOrder)
  {
    super (cs, 0.5f, 1.0f);
    this.originalByteOrder = byteOrder;
    this.byteOrder = byteOrder;
  }

  protected CoderResult decodeLoop (ByteBuffer in, CharBuffer out)
  {
    // TODO: Optimize this in the case in.hasArray() / out.hasArray()

    int inPos = in.position ();
    try
      {
        while (in.remaining () >= 2)
          {
            byte b1 = in.get ();
            byte b2 = in.get ();

            // handle byte order mark
            if (byteOrder == UNKNOWN_ENDIAN)
              {
                char c = (char) ((b1 << 8) | b2);
                if (c == BYTE_ORDER_MARK)
                  {
                    byteOrder = BIG_ENDIAN;
                    inPos += 2;
                    continue;
                  }
                else if (c == REVERSED_BYTE_ORDER_MARK)
                  {
                    byteOrder = LITTLE_ENDIAN;
                    inPos += 2;
                    continue;
                  }
                else
                  {
                    // assume big endian, do not consume bytes,
                    // continue with normal processing
                    byteOrder = BIG_ENDIAN;
                  }
              }

            char c = byteOrder == BIG_ENDIAN ? (char) ((b1 << 8) | b2)
                                             : (char) ((b2 << 8) | b1);

            if (0xD800 <= c && c <= 0xDFFF)
              {
                // c is a surrogate
                
                // make sure c is a high surrogate
                if (c > 0xDBFF)
                  return CoderResult.malformedForLength (2);
                if (in.remaining () < 2)
                  return CoderResult.UNDERFLOW;
                byte b3 = in.get ();
                byte b4 = in.get ();
                char d = byteOrder == BIG_ENDIAN ? (char) ((b3 << 8) | b4)
                                                 : (char) ((b4 << 8) | b3);
                // make sure d is a low surrogate
                if (d < 0xDC00 || d > 0xDFFF)
                  return CoderResult.malformedForLength (2);
                out.put (c);
                out.put (d);
                inPos += 4;
              }
            else
              {
                if (!out.hasRemaining ())
                  return CoderResult.UNDERFLOW;
                out.put (c);
                inPos += 2;
              }
          }

        return CoderResult.UNDERFLOW;
      }
    finally
      {
        in.position (inPos);
      }
  }

  /**
   * Writes <code>c</code> to <code>out</code> in the byte order
   * specified by <code>byteOrder</code>.
   **/
  private void put (ByteBuffer out, char c)
  {
    if (byteOrder == BIG_ENDIAN)
      {
        out.put ((byte) (c >> 8));
        out.put ((byte) (c & 0xFF));
      }
    else
      {
        out.put ((byte) (c & 0xFF));
        out.put ((byte) (c >> 8));
      }
  }

  protected void implReset ()
  {
    byteOrder = originalByteOrder;
  }
}
