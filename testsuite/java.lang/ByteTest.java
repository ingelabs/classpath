/**
 * Test the Byte object wrapper class.
 *
 * @author Brian Jones (brian.jones@oryxsoft.com)
 */
public class ByteTest
{
  public static void main(String[] argv)
    {
      ByteTest test = new ByteTest();
      test.constructorsTest();
      test.byteValueTest();
      test.decodeTest();
      test.doubleValueTest();
      test.equalsTest();
      test.floatValueTest();
      test.hashCodeTest();
      test.intValueTest();
      test.longValueTest();
      test.parseByteTest();
      test.shortValueTest();
      test.toStringTest();
      test.valueOfTest();
      test.variables();
      test.typeInstance();
    }

  public void constructorsTest()
    {
      byte b = 1;
      Byte obj1 = null, obj2 = null;
      String y;

      obj1 = new Byte(b);
      y = obj1.toString();
      if (y.equals("1") != true)
	failed("Byte(byte)");
      else
	passed("Byte(byte)");

      try 
	{
	  obj2 = new Byte("1");
	  y = obj2.toString();
	  if (y.equals("1") != true)
	    failed("Byte(String)");
	  else
	    passed("Byte(String)");
	}
      catch (NumberFormatException nfe)
	{
	  failed("Byte(String)");
	}
    }

  public void byteValueTest()
    {
      byte b = 1;
      Byte obj = new Byte(b);
      if (obj.byteValue() == b)
	passed("Byte.byteValue()");
      else
	failed("Byte.byteValue()");
    }

  public void decodeTest()
    {
      try
	{
	  Byte obj = Byte.decode("1");
	  if (obj.byteValue() == 1)
	    passed("Byte.decode(String)");
	  else
	    failed("Byte.decode(String)");
	}
      catch (NumberFormatException nfe)
	{
	  failed("Byte.decode(String) threw NumberFormatException");
	}
    }

  public void doubleValueTest()
    {
      byte b = 4;
      double d = b;
      Byte obj = new Byte(b);
      if (obj.doubleValue() == d)
	passed("Byte.doubleValue()");
      else
	failed("Byte.doubleValue()");
    }

  public void equalsTest()
    {
      Byte obj1 = null, obj2 = null;
      obj1 = new Byte((byte)1);
      obj2 = new Byte((byte)2);
      if (obj1.equals(obj2))
	failed("Byte.equals() 1 != 2");
      else
	passed("Byte.equals() 1 != 2");

      obj2 = obj1;
      if (obj1.equals(obj2))
	passed("Byte.equals() 1 == 1");
      else
	failed("Byte.equals() 1 == 1");
    }

  public void floatValueTest()
    {
      byte b = 4;
      float f = b;
      Byte obj = new Byte(b);
      if (obj.floatValue() == f)
	passed("Byte.floatValue()");
      else
	failed("Byte.floatValue()");
    }

  public void hashCodeTest()
    {
      boolean caught = false;
      Byte obj = new Byte((byte)1);
      try
	{
	  int i = obj.hashCode();
	}
      catch (Exception e)
	{
	  caught = true;
	  failed("Byte.hashCode()");
	}
      if (!caught)
	passed("Byte.hashCode()");
    }

  public void intValueTest()
    {
      byte b = 4;
      int i = b;
      Byte obj = new Byte(b);
      if (obj.intValue() == i)
	passed("Byte.intValue()");
      else
	failed("Byte.intValue()");
    }

  public void longValueTest()
    {
      byte b = 4;
      long l = b;
      Byte obj = new Byte(b);
      if (obj.longValue() == l)
	passed("Byte.longValue()");
      else
	failed("Byte.longValue()");
    }

  public void parseByteTest()
    {
      byte b = Byte.parseByte("1");
      if (b == (byte)1)
	passed("Byte.parseByte(String)");
      else
	failed("Byte.parseByte(String)");

      b = Byte.parseByte("-4", 10);
      if (b == (byte)-4)
	passed("Byte.parseByte(String, int)");
      else
	failed("Byte.parseByte(String, int)");
    }

  public void shortValueTest()
    {
      byte b = 4;
      short s = b;
      Byte obj = new Byte(b);
      if (obj.shortValue() == s)
	passed("Byte.shortValue()");
      else
	failed("Byte.shortValue()");
    }

  public void toStringTest()
    {
      Byte obj = new Byte((byte)-2);
      String x = obj.toString();
      if (x.equals("-2"))
	passed("Byte.toString()");
      else
	failed("Byte.toString()");

      x = Byte.toString((byte)-2);
      if (x.equals("-2"))
	passed("Byte.toString(byte)");
      else
	failed("Byte.toString(byte)");
    }

  public void valueOfTest()
    {
      Byte obj1 = Byte.valueOf("2",10);
      Byte obj2 = new Byte((byte)2);
      if (obj1.intValue() == obj2.intValue())
	passed("Byte.valueOf(String,int)");
      else
	failed("Byte.valueOf(String,int)");

      obj1 = Byte.valueOf("2");
      if (obj1.intValue() == obj2.intValue())
	passed("Byte.valueOf(String)");
      else
	failed("Byte.valueOf(String)");
    }

  public void variables()
    {
      byte min = Byte.MIN_VALUE;
      byte max = Byte.MAX_VALUE;
      
      if (min == (byte)-128)
	passed("Byte.MIN_VALUE is -128");
      else
	failed("Byte.MIN_VALUE is " + min + " != -128");

      if (max == (byte)127)
	passed("Byte.MIN_VALUE is 127");
      else
	failed("Byte.MIN_VALUE is " + min + " != 127");

      String x = Byte.TYPE.getName();
      if (x.equals("byte") != true)
        failed("Byte.TYPE.getName() is " + x + " != byte");
      else
        passed("Byte.TYPE.getName() is byte");
    }

  public void typeInstance()
    {
      try {
        Object b = Byte.TYPE.newInstance();

        failed("Byte.TYPE.newInstance succeeded.");
      }
      catch (InstantiationException e) {
	passed("Byte.TYPE.newInstance failed with exception '" + e.toString() + "'");
      }
      catch (Exception e) {
        failed("Byte.TYPE.newInstance threw incorrect exception '" + e.toString() + "'");
      }
    }

  public void failed(String s)
    {
      if (s != null)
	System.out.println("FAILED: " + s);
      else
	System.out.println("FAILED: ");
    }

  public void passed(String s)
    {
      if (s != null)
	System.out.println("PASSED: " + s);
      else
	System.out.println("PASSED: ");
    }
}
