package manta.runtime;
import java.nio.*;
public final class ShortBufferImpl extends java.nio. ShortBuffer
{
    private int array_offset;
    short [] backing_buffer;
    private boolean ro;
    ShortBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new short[cap];
      this.cap = cap;
      this.pos = off;
      this.limit = lim;
    }
    ShortBufferImpl(short[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.cap = array.length;
      this.pos = off;
      this.limit = lim;
    }
    ShortBufferImpl(ShortBufferImpl copy)
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
  private static MantaNative short[] nio_cast(byte[]copy);
  private static MantaNative short[] nio_cast(char[]copy);
  private static MantaNative short[] nio_cast(short[]copy);
  private static MantaNative short[] nio_cast(long[]copy);
  private static MantaNative short[] nio_cast(int[]copy);
  private static MantaNative short[] nio_cast(float[]copy);
  private static MantaNative short[] nio_cast(double[]copy);
  ShortBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative byte nio_get_Byte(ShortBufferImpl b, int index); private static MantaNative void nio_put_Byte(ShortBufferImpl b, int index, byte value); public java.nio. ByteBuffer asByteBuffer() { return new manta.runtime. ByteBufferImpl(backing_buffer); }
  ShortBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative char nio_get_Char(ShortBufferImpl b, int index); private static MantaNative void nio_put_Char(ShortBufferImpl b, int index, char value); public java.nio. CharBuffer asCharBuffer() { return new manta.runtime. CharBufferImpl(backing_buffer); }
  ShortBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative short nio_get_Short(ShortBufferImpl b, int index); private static MantaNative void nio_put_Short(ShortBufferImpl b, int index, short value); public java.nio. ShortBuffer asShortBuffer() { return new manta.runtime. ShortBufferImpl(backing_buffer); }
  ShortBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative int nio_get_Int(ShortBufferImpl b, int index); private static MantaNative void nio_put_Int(ShortBufferImpl b, int index, int value); public java.nio. IntBuffer asIntBuffer() { return new manta.runtime. IntBufferImpl(backing_buffer); }
  ShortBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative long nio_get_Long(ShortBufferImpl b, int index); private static MantaNative void nio_put_Long(ShortBufferImpl b, int index, long value); public java.nio. LongBuffer asLongBuffer() { return new manta.runtime. LongBufferImpl(backing_buffer); }
  ShortBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative float nio_get_Float(ShortBufferImpl b, int index); private static MantaNative void nio_put_Float(ShortBufferImpl b, int index, float value); public java.nio. FloatBuffer asFloatBuffer() { return new manta.runtime. FloatBufferImpl(backing_buffer); }
  ShortBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative double nio_get_Double(ShortBufferImpl b, int index); private static MantaNative void nio_put_Double(ShortBufferImpl b, int index, double value); public java.nio. DoubleBuffer asDoubleBuffer() { return new manta.runtime. DoubleBufferImpl(backing_buffer); }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. ShortBuffer slice()
    {
        ShortBufferImpl A = new ShortBufferImpl(this);
        A.array_offset = pos;
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
    public short get()
    {
        short e = backing_buffer[pos];
        pos++;
        return e;
    }
    public java.nio. ShortBuffer put(short b)
    {
        backing_buffer[pos] = b;
        pos++;
        return this;
    }
    public short get(int index)
    {
        return backing_buffer[index];
    }
    public java.nio. ShortBuffer put(int index, short b)
    {
      backing_buffer[index] = b;
      return this;
    }
  public char getChar() { char a = nio_get_Char(this, pos); inc_pos(2); return a; } public java.nio. ShortBuffer putChar(char value) { nio_put_Char(this, pos, value); inc_pos(2); return this; } public char getChar(int index) { char a = nio_get_Char(this, index); inc_pos(2); return a; } public java.nio. ShortBuffer putChar(int index, char value) { nio_put_Char(this, index, value); inc_pos(2); return this; };
  public short getShort() { return get(); } public java.nio. ShortBuffer putShort(short value) { return put(value); } public short getShort(int index) { return get(index); } public java.nio. ShortBuffer putShort(int index, short value) { return put(index, value); };
  public int getInt() { int a = nio_get_Int(this, pos); inc_pos(4); return a; } public java.nio. ShortBuffer putInt(int value) { nio_put_Int(this, pos, value); inc_pos(4); return this; } public int getInt(int index) { int a = nio_get_Int(this, index); inc_pos(4); return a; } public java.nio. ShortBuffer putInt(int index, int value) { nio_put_Int(this, index, value); inc_pos(4); return this; };
  public long getLong() { long a = nio_get_Long(this, pos); inc_pos(8); return a; } public java.nio. ShortBuffer putLong(long value) { nio_put_Long(this, pos, value); inc_pos(8); return this; } public long getLong(int index) { long a = nio_get_Long(this, index); inc_pos(8); return a; } public java.nio. ShortBuffer putLong(int index, long value) { nio_put_Long(this, index, value); inc_pos(8); return this; };
  public float getFloat() { float a = nio_get_Float(this, pos); inc_pos(4); return a; } public java.nio. ShortBuffer putFloat(float value) { nio_put_Float(this, pos, value); inc_pos(4); return this; } public float getFloat(int index) { float a = nio_get_Float(this, index); inc_pos(4); return a; } public java.nio. ShortBuffer putFloat(int index, float value) { nio_put_Float(this, index, value); inc_pos(4); return this; };
  public double getDouble() { double a = nio_get_Double(this, pos); inc_pos(8); return a; } public java.nio. ShortBuffer putDouble(double value) { nio_put_Double(this, pos, value); inc_pos(8); return this; } public double getDouble(int index) { double a = nio_get_Double(this, index); inc_pos(8); return a; } public java.nio. ShortBuffer putDouble(int index, double value) { nio_put_Double(this, index, value); inc_pos(8); return this; };
}
