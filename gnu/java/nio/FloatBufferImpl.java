package manta.runtime;
import java.nio.*;
public final class FloatBufferImpl extends java.nio. FloatBuffer
{
    private int array_offset;
    float [] backing_buffer;
    private boolean ro;
    FloatBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new float[cap];
      this.cap = cap;
      this.pos = off;
      this.limit = lim;
    }
    FloatBufferImpl(float[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.cap = array.length;
      this.pos = off;
      this.limit = lim;
    }
    FloatBufferImpl(FloatBufferImpl copy)
    {
        backing_buffer = copy.backing_buffer;
        ro = copy.ro;
        pos = copy.pos;
        limit = copy.limit;
    }
    void inc_pos(int a)
    {
        pos += a;
    }
  private static MantaNative float[] nio_cast(byte[]copy);
  private static MantaNative float[] nio_cast(char[]copy);
  private static MantaNative float[] nio_cast(short[]copy);
  private static MantaNative float[] nio_cast(long[]copy);
  private static MantaNative float[] nio_cast(int[]copy);
  private static MantaNative float[] nio_cast(float[]copy);
  private static MantaNative float[] nio_cast(double[]copy);
  FloatBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative byte nio_get_Byte(FloatBufferImpl b, int index); private static MantaNative void nio_put_Byte(FloatBufferImpl b, int index, byte value); public java.nio. ByteBuffer asByteBuffer() { return new manta.runtime. ByteBufferImpl(backing_buffer); }
  FloatBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative char nio_get_Char(FloatBufferImpl b, int index); private static MantaNative void nio_put_Char(FloatBufferImpl b, int index, char value); public java.nio. CharBuffer asCharBuffer() { return new manta.runtime. CharBufferImpl(backing_buffer); }
  FloatBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative short nio_get_Short(FloatBufferImpl b, int index); private static MantaNative void nio_put_Short(FloatBufferImpl b, int index, short value); public java.nio. ShortBuffer asShortBuffer() { return new manta.runtime. ShortBufferImpl(backing_buffer); }
  FloatBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative int nio_get_Int(FloatBufferImpl b, int index); private static MantaNative void nio_put_Int(FloatBufferImpl b, int index, int value); public java.nio. IntBuffer asIntBuffer() { return new manta.runtime. IntBufferImpl(backing_buffer); }
  FloatBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative long nio_get_Long(FloatBufferImpl b, int index); private static MantaNative void nio_put_Long(FloatBufferImpl b, int index, long value); public java.nio. LongBuffer asLongBuffer() { return new manta.runtime. LongBufferImpl(backing_buffer); }
  FloatBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative float nio_get_Float(FloatBufferImpl b, int index); private static MantaNative void nio_put_Float(FloatBufferImpl b, int index, float value); public java.nio. FloatBuffer asFloatBuffer() { return new manta.runtime. FloatBufferImpl(backing_buffer); }
  FloatBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative double nio_get_Double(FloatBufferImpl b, int index); private static MantaNative void nio_put_Double(FloatBufferImpl b, int index, double value); public java.nio. DoubleBuffer asDoubleBuffer() { return new manta.runtime. DoubleBufferImpl(backing_buffer); }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. FloatBuffer slice()
    {
        FloatBufferImpl A = new FloatBufferImpl(this);
        A.array_offset = pos;
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
    public float get()
    {
        float e = backing_buffer[pos];
        pos++;
        return e;
    }
    public java.nio. FloatBuffer put(float b)
    {
        backing_buffer[pos] = b;
        pos++;
        return this;
    }
    public float get(int index)
    {
        return backing_buffer[index];
    }
    public java.nio. FloatBuffer put(int index, float b)
    {
      backing_buffer[index] = b;
      return this;
    }
  public char getChar() { char a = nio_get_Char(this, pos); inc_pos(2); return a; } public java.nio. FloatBuffer putChar(char value) { nio_put_Char(this, pos, value); inc_pos(2); return this; } public char getChar(int index) { char a = nio_get_Char(this, index); inc_pos(2); return a; } public java.nio. FloatBuffer putChar(int index, char value) { nio_put_Char(this, index, value); inc_pos(2); return this; };
  public short getShort() { short a = nio_get_Short(this, pos); inc_pos(2); return a; } public java.nio. FloatBuffer putShort(short value) { nio_put_Short(this, pos, value); inc_pos(2); return this; } public short getShort(int index) { short a = nio_get_Short(this, index); inc_pos(2); return a; } public java.nio. FloatBuffer putShort(int index, short value) { nio_put_Short(this, index, value); inc_pos(2); return this; };
  public int getInt() { int a = nio_get_Int(this, pos); inc_pos(4); return a; } public java.nio. FloatBuffer putInt(int value) { nio_put_Int(this, pos, value); inc_pos(4); return this; } public int getInt(int index) { int a = nio_get_Int(this, index); inc_pos(4); return a; } public java.nio. FloatBuffer putInt(int index, int value) { nio_put_Int(this, index, value); inc_pos(4); return this; };
  public long getLong() { long a = nio_get_Long(this, pos); inc_pos(8); return a; } public java.nio. FloatBuffer putLong(long value) { nio_put_Long(this, pos, value); inc_pos(8); return this; } public long getLong(int index) { long a = nio_get_Long(this, index); inc_pos(8); return a; } public java.nio. FloatBuffer putLong(int index, long value) { nio_put_Long(this, index, value); inc_pos(8); return this; };
  public float getFloat() { return get(); } public java.nio. FloatBuffer putFloat(float value) { return put(value); } public float getFloat(int index) { return get(index); } public java.nio. FloatBuffer putFloat(int index, float value) { return put(index, value); };
  public double getDouble() { double a = nio_get_Double(this, pos); inc_pos(8); return a; } public java.nio. FloatBuffer putDouble(double value) { nio_put_Double(this, pos, value); inc_pos(8); return this; } public double getDouble(int index) { double a = nio_get_Double(this, index); inc_pos(8); return a; } public java.nio. FloatBuffer putDouble(int index, double value) { nio_put_Double(this, index, value); inc_pos(8); return this; };
}
