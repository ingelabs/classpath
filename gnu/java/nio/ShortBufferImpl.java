package gnu.java.nio;
import java.nio.*;
public final class ShortBufferImpl extends java.nio. ShortBuffer
{
    private int array_offset;
    short [] backing_buffer;
    private boolean ro;
  public ShortBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new short[cap];
      this.capacity(cap);
      this.position(off);
      this.limit(lim);
    }
  public ShortBufferImpl(short[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.capacity(array.length);
      this.position(off);
      this.limit(lim);
    }
  public ShortBufferImpl(ShortBufferImpl copy)
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
  private static native short[] nio_cast(byte[]copy);
  private static native short[] nio_cast(char[]copy);
  private static native short[] nio_cast(short[]copy);
  private static native short[] nio_cast(long[]copy);
  private static native short[] nio_cast(int[]copy);
  private static native short[] nio_cast(float[]copy);
  private static native short[] nio_cast(double[]copy);
  ShortBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native byte nio_get_Byte(ShortBufferImpl b, int index); private static native void nio_put_Byte(ShortBufferImpl b, int index, byte value); public java.nio. ByteBuffer asByteBuffer() { return new gnu.java.nio. ByteBufferImpl(backing_buffer); }
  ShortBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native char nio_get_Char(ShortBufferImpl b, int index); private static native void nio_put_Char(ShortBufferImpl b, int index, char value); public java.nio. CharBuffer asCharBuffer() { return new gnu.java.nio. CharBufferImpl(backing_buffer); }
  ShortBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native short nio_get_Short(ShortBufferImpl b, int index); private static native void nio_put_Short(ShortBufferImpl b, int index, short value); public java.nio. ShortBuffer asShortBuffer() { return new gnu.java.nio. ShortBufferImpl(backing_buffer); }
  ShortBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native int nio_get_Int(ShortBufferImpl b, int index); private static native void nio_put_Int(ShortBufferImpl b, int index, int value); public java.nio. IntBuffer asIntBuffer() { return new gnu.java.nio. IntBufferImpl(backing_buffer); }
  ShortBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native long nio_get_Long(ShortBufferImpl b, int index); private static native void nio_put_Long(ShortBufferImpl b, int index, long value); public java.nio. LongBuffer asLongBuffer() { return new gnu.java.nio. LongBufferImpl(backing_buffer); }
  ShortBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native float nio_get_Float(ShortBufferImpl b, int index); private static native void nio_put_Float(ShortBufferImpl b, int index, float value); public java.nio. FloatBuffer asFloatBuffer() { return new gnu.java.nio. FloatBufferImpl(backing_buffer); }
  ShortBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native double nio_get_Double(ShortBufferImpl b, int index); private static native void nio_put_Double(ShortBufferImpl b, int index, double value); public java.nio. DoubleBuffer asDoubleBuffer() { return new gnu.java.nio. DoubleBufferImpl(backing_buffer); }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. ShortBuffer slice()
    {
        ShortBufferImpl A = new ShortBufferImpl(this);
        A.array_offset = position();
        return A;
    }
    public java.nio. ShortBuffer duplicate()
    {
        return new ShortBufferImpl(this);
    }
    public java.nio. ShortBuffer asReadOnlyBuffer()
    {
        ShortBufferImpl a = new ShortBufferImpl(this);
        a.ro = true;
        return a;
    }
    public java.nio. ShortBuffer compact()
    {
        return this;
    }
    public boolean isDirect()
    {
        return backing_buffer != null;
    }
  final public short get()
    {
        short e = backing_buffer[position()];
        position(position()+1);
        return e;
    }
  final public java.nio. ShortBuffer put(short b)
    {
        backing_buffer[position()] = b;
        position(position()+1);
        return this;
    }
  final public short get(int index)
    {
        return backing_buffer[index];
    }
   final public java.nio. ShortBuffer put(int index, short b)
    {
      backing_buffer[index] = b;
      return this;
    }
  final public char getChar() { char a = nio_get_Char(this, position()); inc_pos(2); return a; } final public java.nio. ShortBuffer putChar(char value) { nio_put_Char(this, position(), value); inc_pos(2); return this; } final public char getChar(int index) { char a = nio_get_Char(this, index); return a; } final public java.nio. ShortBuffer putChar(int index, char value) { nio_put_Char(this, index, value); return this; };
  final public short getShort() { return get(); } final public java.nio. ShortBuffer putShort(short value) { return put(value); } final public short getShort(int index) { return get(index); } final public java.nio. ShortBuffer putShort(int index, short value) { return put(index, value); };
  final public int getInt() { int a = nio_get_Int(this, position()); inc_pos(4); return a; } final public java.nio. ShortBuffer putInt(int value) { nio_put_Int(this, position(), value); inc_pos(4); return this; } final public int getInt(int index) { int a = nio_get_Int(this, index); return a; } final public java.nio. ShortBuffer putInt(int index, int value) { nio_put_Int(this, index, value); return this; };
  final public long getLong() { long a = nio_get_Long(this, position()); inc_pos(8); return a; } final public java.nio. ShortBuffer putLong(long value) { nio_put_Long(this, position(), value); inc_pos(8); return this; } final public long getLong(int index) { long a = nio_get_Long(this, index); return a; } final public java.nio. ShortBuffer putLong(int index, long value) { nio_put_Long(this, index, value); return this; };
  final public float getFloat() { float a = nio_get_Float(this, position()); inc_pos(4); return a; } final public java.nio. ShortBuffer putFloat(float value) { nio_put_Float(this, position(), value); inc_pos(4); return this; } final public float getFloat(int index) { float a = nio_get_Float(this, index); return a; } final public java.nio. ShortBuffer putFloat(int index, float value) { nio_put_Float(this, index, value); return this; };
  final public double getDouble() { double a = nio_get_Double(this, position()); inc_pos(8); return a; } final public java.nio. ShortBuffer putDouble(double value) { nio_put_Double(this, position(), value); inc_pos(8); return this; } final public double getDouble(int index) { double a = nio_get_Double(this, index); return a; } final public java.nio. ShortBuffer putDouble(int index, double value) { nio_put_Double(this, index, value); return this; };
}
