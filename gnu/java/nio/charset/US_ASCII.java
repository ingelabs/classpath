package gnu.java.nio.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

/**
 * US-ASCII charset.
 *
 * @author Jesse Rosenstock
 */
final class US_ASCII extends Charset
{
  US_ASCII ()
  {
    super ("US-ASCII", new String[]{"ISO646-US"});
  }

  public boolean contains (Charset cs)
  {
    return cs instanceof US_ASCII;
  }

  public CharsetDecoder newDecoder ()
  {
    return new Decoder (this);
  }

  public CharsetEncoder newEncoder ()
  {
    return new Encoder (this);
  }

  private static final class Decoder extends CharsetDecoder
  {
    private Decoder (Charset cs)
    {
      super (cs, 1.0f, 1.0f);
    }

    protected CoderResult decodeLoop (ByteBuffer in, CharBuffer out)
    {
      // TODO: Optimize this in the case in.hasArray() / out.hasArray()
      while (in.hasRemaining ())
        {
          byte b = in.get ();

          if (b < 0)
            {
              in.position (in.position () - 1);
              return CoderResult.malformedForLength (1);
            }
          if (!out.hasRemaining ())
            {
              in.position (in.position () - 1);
              return CoderResult.OVERFLOW;
            }

          out.put ((char) b);
        }

      return CoderResult.UNDERFLOW;
    }
  }

  private static final class Encoder extends CharsetEncoder
  {
    private Encoder (Charset cs)
    {
      super (cs, 1.0f, 1.0f);
    }

    protected CoderResult encodeLoop (CharBuffer in, ByteBuffer out)
    {
      // TODO: Optimize this in the case in.hasArray() / out.hasArray()
      while (in.hasRemaining ())
      {
        char c = in.get ();

        if (c > Byte.MAX_VALUE)
          {
            in.position (in.position () - 1);
            return CoderResult.unmappableForLength (1);
          }
        if (!out.hasRemaining ())
          {
            in.position (in.position () - 1);
            return CoderResult.OVERFLOW;
          }

        out.put ((byte) c);
      }

      return CoderResult.UNDERFLOW;
    }
  }
}
