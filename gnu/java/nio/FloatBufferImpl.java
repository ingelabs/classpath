package gnu.java.nio;
import java.nio.*;
public final class FloatBufferImpl extends java.nio. FloatBuffer
{
    private int array_offset;
    float [] backing_buffer;
    private boolean ro;
  public FloatBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new float[cap];
      this.capacity(cap);
      this.position(off);
      this.limit(lim);
    }
  public FloatBufferImpl(float[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.capacity(array.length);
      this.position(off);
      this.limit(lim);
    }
  public FloatBufferImpl(FloatBufferImpl copy)
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
  private static native float[] nio_cast(byte[]copy);
  private static native float[] nio_cast(char[]copy);
  private static native float[] nio_cast(short[]copy);
  private static native float[] nio_cast(long[]copy);
  private static native float[] nio_cast(int[]copy);
  private static native float[] nio_cast(float[]copy);
  private static native float[] nio_cast(double[]copy);
  FloatBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native byte nio_get_Byte(FloatBufferImpl b, int index); private static native void nio_put_Byte(FloatBufferImpl b, int index, byte value); public java.nio. ByteBuffer asByteBuffer() { return new gnu.java.nio. ByteBufferImpl(backing_buffer); }
  FloatBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native char nio_get_Char(FloatBufferImpl b, int index); private static native void nio_put_Char(FloatBufferImpl b, int index, char value); public java.nio. CharBuffer asCharBuffer() { return new gnu.java.nio. CharBufferImpl(backing_buffer); }
  FloatBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native short nio_get_Short(FloatBufferImpl b, int index); private static native void nio_put_Short(FloatBufferImpl b, int index, short value); public java.nio. ShortBuffer asShortBuffer() { return new gnu.java.nio. ShortBufferImpl(backing_buffer); }
  FloatBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native int nio_get_Int(FloatBufferImpl b, int index); private static native void nio_put_Int(FloatBufferImpl b, int index, int value); public java.nio. IntBuffer asIntBuffer() { return new gnu.java.nio. IntBufferImpl(backing_buffer); }
  FloatBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native long nio_get_Long(FloatBufferImpl b, int index); private static native void nio_put_Long(FloatBufferImpl b, int index, long value); public java.nio. LongBuffer asLongBuffer() { return new gnu.java.nio. LongBufferImpl(backing_buffer); }
  FloatBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native float nio_get_Float(FloatBufferImpl b, int index); private static native void nio_put_Float(FloatBufferImpl b, int index, float value); public java.nio. FloatBuffer asFloatBuffer() { return new gnu.java.nio. FloatBufferImpl(backing_buffer); }
  FloatBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native double nio_get_Double(FloatBufferImpl b, int index); private static native void nio_put_Double(FloatBufferImpl b, int index, double value); public java.nio. DoubleBuffer asDoubleBuffer() { return new gnu.java.nio. DoubleBufferImpl(backing_buffer); }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. FloatBuffer slice()
    {
        FloatBufferImpl A = new FloatBufferImpl(this);
        A.array_offset = position();
        return A;
    }
    public java.nio. FloatBuffer duplicate()
    {
        return new FloatBufferImpl(this);
    }
    public java.nio. FloatBuffer asReadOnlyBuffer()
    {
        FloatBufferImpl a = new FloatBufferImpl(this);
        a.ro = true;
        return a;
    }
    public java.nio. FloatBuffer compact()
    {
        return this;
    }
    public boolean isDirect()
    {
        return backing_buffer != null;
    }
  final public float get()
    {
        float e = backing_buffer[position()];
        position(position()+1);
        return e;
    }
  final public java.nio. FloatBuffer put(float b)
    {
        backing_buffer[position()] = b;
        position(position()+1);
        return this;
    }
  final public float get(int index)
    {
        return backing_buffer[index];
    }
   final public java.nio. FloatBuffer put(int index, float b)
    {
      backing_buffer[index] = b;
      return this;
    }
  final public char getChar() { char a = nio_get_Char(this, position()); inc_pos(2); return a; } final public java.nio. FloatBuffer putChar(char value) { nio_put_Char(this, position(), value); inc_pos(2); return this; } final public char getChar(int index) { char a = nio_get_Char(this, index); return a; } final public java.nio. FloatBuffer putChar(int index, char value) { nio_put_Char(this, index, value); return this; };
  final public short getShort() { short a = nio_get_Short(this, position()); inc_pos(2); return a; } final public java.nio. FloatBuffer putShort(short value) { nio_put_Short(this, position(), value); inc_pos(2); return this; } final public short getShort(int index) { short a = nio_get_Short(this, index); return a; } final public java.nio. FloatBuffer putShort(int index, short value) { nio_put_Short(this, index, value); return this; };
  final public int getInt() { int a = nio_get_Int(this, position()); inc_pos(4); return a; } final public java.nio. FloatBuffer putInt(int value) { nio_put_Int(this, position(), value); inc_pos(4); return this; } final public int getInt(int index) { int a = nio_get_Int(this, index); return a; } final public java.nio. FloatBuffer putInt(int index, int value) { nio_put_Int(this, index, value); return this; };
  final public long getLong() { long a = nio_get_Long(this, position()); inc_pos(8); return a; } final public java.nio. FloatBuffer putLong(long value) { nio_put_Long(this, position(), value); inc_pos(8); return this; } final public long getLong(int index) { long a = nio_get_Long(this, index); return a; } final public java.nio. FloatBuffer putLong(int index, long value) { nio_put_Long(this, index, value); return this; };
  final public float getFloat() { return get(); } final public java.nio. FloatBuffer putFloat(float value) { return put(value); } final public float getFloat(int index) { return get(index); } final public java.nio. FloatBuffer putFloat(int index, float value) { return put(index, value); };
  final public double getDouble() { double a = nio_get_Double(this, position()); inc_pos(8); return a; } final public java.nio. FloatBuffer putDouble(double value) { nio_put_Double(this, position(), value); inc_pos(8); return this; } final public double getDouble(int index) { double a = nio_get_Double(this, index); return a; } final public java.nio. FloatBuffer putDouble(int index, double value) { nio_put_Double(this, index, value); return this; };
}
