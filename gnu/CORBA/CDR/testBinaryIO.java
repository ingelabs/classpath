

package gnu.CORBA.CDR;

import junit.framework.TestCase;

import java.io.IOException;

import java.util.Arrays;
import java.util.Random;

import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.ORB;

/**
 * Test the low level (binary) io routines.
 * 
 * In some ORBs, provided by different vendors, this may not be implemented.
 */
public class testBinaryIO
  extends TestCase
{
  ORB orb = ORB.init();
  Random r = new Random();
  private org.omg.CORBA.portable.InputStream in;
  private org.omg.CORBA.portable.OutputStream out;

  public void testBinary()
                  throws IOException
  {
    byte wByte = (byte) (r.nextInt(Byte.MAX_VALUE));
    byte[] wBytes = new byte[ 24 + r.nextInt(24) ];
    byte[] rBytes = new byte[ wBytes.length ];

    for (int i = 0; i < wBytes.length; i++)
      {
        wBytes [ i ] = (byte) (r.nextInt(Byte.MAX_VALUE) - Byte.MAX_VALUE / 2);
      }
    ;

    int ofs = r.nextInt(7);
    int len = 4;

    try
      {
        out = orb.create_any().create_output_stream();
        out.write(wBytes);
        in = out.create_input_stream();        
        in.read(rBytes);
        assertTrue(Arrays.equals(rBytes, wBytes));
      }
    catch (NO_IMPLEMENT ex)
      {
        fail("write(byte []) not implemented by this vendor.");
      }

    try
      {
        out = orb.create_any().create_output_stream();
        out.write(wBytes, ofs, len);
        in = out.create_input_stream();        
        rBytes = new byte[ wBytes.length ];
        in.read(rBytes, ofs, len);

        boolean eq = true;
        for (int i = ofs; i < ofs; i++)
          {
            if (rBytes [ i ] != wBytes [ i ])
              eq = false;
          }
        assertTrue(eq);
      }
    catch (NO_IMPLEMENT ex)
      {
        fail("write(byte[], ofs, len) not implemented by this vendor.");
      }

    try
      {
        out = orb.create_any().create_output_stream();
        out.write(wByte);
        in = out.create_input_stream();
        assertEquals(in.read(), wByte);
      }
    catch (NO_IMPLEMENT ex)
      {
        fail("write(int) not implemented by this vendor.");
      }
  }
}
