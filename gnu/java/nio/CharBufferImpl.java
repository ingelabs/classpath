package gnu.java.nio;
import java.nio.*;
public final class CharBufferImpl extends java.nio. CharBuffer
{
    private int array_offset;
    char [] backing_buffer;
    private boolean ro;
  public CharBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new char[cap];
      this.capacity(cap);
      this.position(off);
      this.limit(lim);
    }
  public CharBufferImpl(char[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.capacity(array.length);
      this.position(off);
      this.limit(lim);
    }
  public CharBufferImpl(CharBufferImpl copy)
    {
        backing_buffer = copy.backing_buffer;
        ro = copy.ro;
        position(copy.position());
        limit(copy.limit());
    }
    void inc_pos(int a)
    {
      position(position() + a);
    }
  private static native char[] nio_cast(byte[]copy);
  private static native char[] nio_cast(char[]copy);
  private static native char[] nio_cast(short[]copy);
  private static native char[] nio_cast(long[]copy);
  private static native char[] nio_cast(int[]copy);
  private static native char[] nio_cast(float[]copy);
  private static native char[] nio_cast(double[]copy);
  CharBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native byte nio_get_Byte(CharBufferImpl b, int index); private static native void nio_put_Byte(CharBufferImpl b, int index, byte value); public java.nio. ByteBuffer asByteBuffer() { return new gnu.java.nio. ByteBufferImpl(backing_buffer); }
  CharBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native char nio_get_Char(CharBufferImpl b, int index); private static native void nio_put_Char(CharBufferImpl b, int index, char value); public java.nio. CharBuffer asCharBuffer() { return new gnu.java.nio. CharBufferImpl(backing_buffer); }
  CharBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native short nio_get_Short(CharBufferImpl b, int index); private static native void nio_put_Short(CharBufferImpl b, int index, short value); public java.nio. ShortBuffer asShortBuffer() { return new gnu.java.nio. ShortBufferImpl(backing_buffer); }
  CharBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native int nio_get_Int(CharBufferImpl b, int index); private static native void nio_put_Int(CharBufferImpl b, int index, int value); public java.nio. IntBuffer asIntBuffer() { return new gnu.java.nio. IntBufferImpl(backing_buffer); }
  CharBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native long nio_get_Long(CharBufferImpl b, int index); private static native void nio_put_Long(CharBufferImpl b, int index, long value); public java.nio. LongBuffer asLongBuffer() { return new gnu.java.nio. LongBufferImpl(backing_buffer); }
  CharBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native float nio_get_Float(CharBufferImpl b, int index); private static native void nio_put_Float(CharBufferImpl b, int index, float value); public java.nio. FloatBuffer asFloatBuffer() { return new gnu.java.nio. FloatBufferImpl(backing_buffer); }
  CharBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native double nio_get_Double(CharBufferImpl b, int index); private static native void nio_put_Double(CharBufferImpl b, int index, double value); public java.nio. DoubleBuffer asDoubleBuffer() { return new gnu.java.nio. DoubleBufferImpl(backing_buffer); }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. CharBuffer slice()
    {
        CharBufferImpl A = new CharBufferImpl(this);
        A.array_offset = position();
        return A;
    }
    public java.nio. CharBuffer duplicate()
    {
        return new CharBufferImpl(this);
    }
    public java.nio. CharBuffer asReadOnlyBuffer()
    {
        CharBufferImpl a = new CharBufferImpl(this);
        a.ro = true;
        return a;
    }
    public java.nio. CharBuffer compact()
    {
        return this;
    }
    public boolean isDirect()
    {
        return backing_buffer != null;
    }
  final public char get()
    {
        char e = backing_buffer[position()];
        position(position()+1);
        return e;
    }
  final public java.nio. CharBuffer put(char b)
    {
        backing_buffer[position()] = b;
        position(position()+1);
        return this;
    }
  final public char get(int index)
    {
        return backing_buffer[index];
    }
   final public java.nio. CharBuffer put(int index, char b)
    {
      backing_buffer[index] = b;
      return this;
    }
  final public char getChar() { return get(); } final public java.nio. CharBuffer putChar(char value) { return put(value); } final public char getChar(int index) { return get(index); } final public java.nio. CharBuffer putChar(int index, char value) { return put(index, value); };
  final public short getShort() { short a = nio_get_Short(this, position()); inc_pos(2); return a; } final public java.nio. CharBuffer putShort(short value) { nio_put_Short(this, position(), value); inc_pos(2); return this; } final public short getShort(int index) { short a = nio_get_Short(this, index); return a; } final public java.nio. CharBuffer putShort(int index, short value) { nio_put_Short(this, index, value); return this; };
  final public int getInt() { int a = nio_get_Int(this, position()); inc_pos(4); return a; } final public java.nio. CharBuffer putInt(int value) { nio_put_Int(this, position(), value); inc_pos(4); return this; } final public int getInt(int index) { int a = nio_get_Int(this, index); return a; } final public java.nio. CharBuffer putInt(int index, int value) { nio_put_Int(this, index, value); return this; };
  final public long getLong() { long a = nio_get_Long(this, position()); inc_pos(8); return a; } final public java.nio. CharBuffer putLong(long value) { nio_put_Long(this, position(), value); inc_pos(8); return this; } final public long getLong(int index) { long a = nio_get_Long(this, index); return a; } final public java.nio. CharBuffer putLong(int index, long value) { nio_put_Long(this, index, value); return this; };
  final public float getFloat() { float a = nio_get_Float(this, position()); inc_pos(4); return a; } final public java.nio. CharBuffer putFloat(float value) { nio_put_Float(this, position(), value); inc_pos(4); return this; } final public float getFloat(int index) { float a = nio_get_Float(this, index); return a; } final public java.nio. CharBuffer putFloat(int index, float value) { nio_put_Float(this, index, value); return this; };
  final public double getDouble() { double a = nio_get_Double(this, position()); inc_pos(8); return a; } final public java.nio. CharBuffer putDouble(double value) { nio_put_Double(this, position(), value); inc_pos(8); return this; } final public double getDouble(int index) { double a = nio_get_Double(this, index); return a; } final public java.nio. CharBuffer putDouble(int index, double value) { nio_put_Double(this, index, value); return this; };
    public String toString()
    {
      if (backing_buffer != null)
        {
          return new String(backing_buffer, position(), limit());
        }
      return super.toString();
    }
}
