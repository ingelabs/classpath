package gnu.java.nio;
import java.nio.*;
public final class IntBufferImpl extends java.nio. IntBuffer
{
    private int array_offset;
    int [] backing_buffer;
    private boolean ro;
  public IntBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new int[cap];
      this.capacity(cap);
      this.position(off);
      this.limit(lim);
    }
  public IntBufferImpl(int[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.capacity(array.length);
      this.position(off);
      this.limit(lim);
    }
  public IntBufferImpl(IntBufferImpl copy)
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
  private static native int[] nio_cast(byte[]copy);
  private static native int[] nio_cast(char[]copy);
  private static native int[] nio_cast(short[]copy);
  private static native int[] nio_cast(long[]copy);
  private static native int[] nio_cast(int[]copy);
  private static native int[] nio_cast(float[]copy);
  private static native int[] nio_cast(double[]copy);
  IntBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native byte nio_get_Byte(IntBufferImpl b, int index); private static native void nio_put_Byte(IntBufferImpl b, int index, byte value); public java.nio. ByteBuffer asByteBuffer() { return new gnu.java.nio. ByteBufferImpl(backing_buffer); }
  IntBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native char nio_get_Char(IntBufferImpl b, int index); private static native void nio_put_Char(IntBufferImpl b, int index, char value); public java.nio. CharBuffer asCharBuffer() { return new gnu.java.nio. CharBufferImpl(backing_buffer); }
  IntBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native short nio_get_Short(IntBufferImpl b, int index); private static native void nio_put_Short(IntBufferImpl b, int index, short value); public java.nio. ShortBuffer asShortBuffer() { return new gnu.java.nio. ShortBufferImpl(backing_buffer); }
  IntBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native int nio_get_Int(IntBufferImpl b, int index); private static native void nio_put_Int(IntBufferImpl b, int index, int value); public java.nio. IntBuffer asIntBuffer() { return new gnu.java.nio. IntBufferImpl(backing_buffer); }
  IntBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native long nio_get_Long(IntBufferImpl b, int index); private static native void nio_put_Long(IntBufferImpl b, int index, long value); public java.nio. LongBuffer asLongBuffer() { return new gnu.java.nio. LongBufferImpl(backing_buffer); }
  IntBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native float nio_get_Float(IntBufferImpl b, int index); private static native void nio_put_Float(IntBufferImpl b, int index, float value); public java.nio. FloatBuffer asFloatBuffer() { return new gnu.java.nio. FloatBufferImpl(backing_buffer); }
  IntBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native double nio_get_Double(IntBufferImpl b, int index); private static native void nio_put_Double(IntBufferImpl b, int index, double value); public java.nio. DoubleBuffer asDoubleBuffer() { return new gnu.java.nio. DoubleBufferImpl(backing_buffer); }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. IntBuffer slice()
    {
        IntBufferImpl A = new IntBufferImpl(this);
        A.array_offset = position();
        return A;
    }
    public java.nio. IntBuffer duplicate()
    {
        return new IntBufferImpl(this);
    }
    public java.nio. IntBuffer asReadOnlyBuffer()
    {
        IntBufferImpl a = new IntBufferImpl(this);
        a.ro = true;
        return a;
    }
    public java.nio. IntBuffer compact()
    {
        return this;
    }
    public boolean isDirect()
    {
        return backing_buffer != null;
    }
  final public int get()
    {
        int e = backing_buffer[position()];
        position(position()+1);
        return e;
    }
  final public java.nio. IntBuffer put(int b)
    {
        backing_buffer[position()] = b;
        position(position()+1);
        return this;
    }
  final public int get(int index)
    {
        return backing_buffer[index];
    }
   final public java.nio. IntBuffer put(int index, int b)
    {
      backing_buffer[index] = b;
      return this;
    }
  final public char getChar() { char a = nio_get_Char(this, position()); inc_pos(2); return a; } final public java.nio. IntBuffer putChar(char value) { nio_put_Char(this, position(), value); inc_pos(2); return this; } final public char getChar(int index) { char a = nio_get_Char(this, index); return a; } final public java.nio. IntBuffer putChar(int index, char value) { nio_put_Char(this, index, value); return this; };
  final public short getShort() { short a = nio_get_Short(this, position()); inc_pos(2); return a; } final public java.nio. IntBuffer putShort(short value) { nio_put_Short(this, position(), value); inc_pos(2); return this; } final public short getShort(int index) { short a = nio_get_Short(this, index); return a; } final public java.nio. IntBuffer putShort(int index, short value) { nio_put_Short(this, index, value); return this; };
  final public int getInt() { return get(); } final public java.nio. IntBuffer putInt(int value) { return put(value); } final public int getInt(int index) { return get(index); } final public java.nio. IntBuffer putInt(int index, int value) { return put(index, value); };
  final public long getLong() { long a = nio_get_Long(this, position()); inc_pos(8); return a; } final public java.nio. IntBuffer putLong(long value) { nio_put_Long(this, position(), value); inc_pos(8); return this; } final public long getLong(int index) { long a = nio_get_Long(this, index); return a; } final public java.nio. IntBuffer putLong(int index, long value) { nio_put_Long(this, index, value); return this; };
  final public float getFloat() { float a = nio_get_Float(this, position()); inc_pos(4); return a; } final public java.nio. IntBuffer putFloat(float value) { nio_put_Float(this, position(), value); inc_pos(4); return this; } final public float getFloat(int index) { float a = nio_get_Float(this, index); return a; } final public java.nio. IntBuffer putFloat(int index, float value) { nio_put_Float(this, index, value); return this; };
  final public double getDouble() { double a = nio_get_Double(this, position()); inc_pos(8); return a; } final public java.nio. IntBuffer putDouble(double value) { nio_put_Double(this, position(), value); inc_pos(8); return this; } final public double getDouble(int index) { double a = nio_get_Double(this, index); return a; } final public java.nio. IntBuffer putDouble(int index, double value) { nio_put_Double(this, index, value); return this; };
}
