package gnu.java.nio.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

/**
 * UTF-16LE charset.
 *
 * @author Jesse Rosenstock
 */
final class UTF_16LE extends Charset
{
  UTF_16LE ()
  {
    super ("UTF-16LE", null);
  }

  public boolean contains (Charset cs)
  {
    return cs instanceof US_ASCII || cs instanceof ISO_8859_1
      || cs instanceof UTF_8 || cs instanceof UTF_16BE
      || cs instanceof UTF_16LE || cs instanceof UTF_16;
  }

  public CharsetDecoder newDecoder ()
  {
    return new UTF_16Decoder (this, UTF_16Decoder.LITTLE_ENDIAN);
  }

  public CharsetEncoder newEncoder ()
  {
    return new UTF_16Encoder (this, UTF_16Encoder.LITTLE_ENDIAN, true);
  }
}
