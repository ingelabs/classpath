package manta.runtime;
import java.nio.*;
public final class DoubleBufferImpl extends java.nio. DoubleBuffer
{
    private int array_offset;
    double [] backing_buffer;
    private boolean ro;
    DoubleBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new double[cap];
      this.cap = cap;
      this.pos = off;
      this.limit = lim;
    }
    DoubleBufferImpl(double[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.cap = array.length;
      this.pos = off;
      this.limit = lim;
    }
    DoubleBufferImpl(DoubleBufferImpl copy)
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
  private static MantaNative double[] nio_cast(byte[]copy);
  private static MantaNative double[] nio_cast(char[]copy);
  private static MantaNative double[] nio_cast(short[]copy);
  private static MantaNative double[] nio_cast(long[]copy);
  private static MantaNative double[] nio_cast(int[]copy);
  private static MantaNative double[] nio_cast(float[]copy);
  private static MantaNative double[] nio_cast(double[]copy);
  DoubleBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative byte nio_get_Byte(DoubleBufferImpl b, int index); private static MantaNative void nio_put_Byte(DoubleBufferImpl b, int index, byte value); public java.nio. ByteBuffer asByteBuffer() { return new manta.runtime. ByteBufferImpl(backing_buffer); }
  DoubleBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative char nio_get_Char(DoubleBufferImpl b, int index); private static MantaNative void nio_put_Char(DoubleBufferImpl b, int index, char value); public java.nio. CharBuffer asCharBuffer() { return new manta.runtime. CharBufferImpl(backing_buffer); }
  DoubleBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative short nio_get_Short(DoubleBufferImpl b, int index); private static MantaNative void nio_put_Short(DoubleBufferImpl b, int index, short value); public java.nio. ShortBuffer asShortBuffer() { return new manta.runtime. ShortBufferImpl(backing_buffer); }
  DoubleBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative int nio_get_Int(DoubleBufferImpl b, int index); private static MantaNative void nio_put_Int(DoubleBufferImpl b, int index, int value); public java.nio. IntBuffer asIntBuffer() { return new manta.runtime. IntBufferImpl(backing_buffer); }
  DoubleBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative long nio_get_Long(DoubleBufferImpl b, int index); private static MantaNative void nio_put_Long(DoubleBufferImpl b, int index, long value); public java.nio. LongBuffer asLongBuffer() { return new manta.runtime. LongBufferImpl(backing_buffer); }
  DoubleBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative float nio_get_Float(DoubleBufferImpl b, int index); private static MantaNative void nio_put_Float(DoubleBufferImpl b, int index, float value); public java.nio. FloatBuffer asFloatBuffer() { return new manta.runtime. FloatBufferImpl(backing_buffer); }
  DoubleBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative double nio_get_Double(DoubleBufferImpl b, int index); private static MantaNative void nio_put_Double(DoubleBufferImpl b, int index, double value); public java.nio. DoubleBuffer asDoubleBuffer() { return new manta.runtime. DoubleBufferImpl(backing_buffer); }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. DoubleBuffer slice()
    {
        DoubleBufferImpl A = new DoubleBufferImpl(this);
        A.array_offset = pos;
        return A;
    }
    public java.nio. DoubleBuffer duplicate()
    {
        return new DoubleBufferImpl(this);
    }
    public java.nio. DoubleBuffer asReadOnlyBuffer()
    {
        DoubleBufferImpl a = new DoubleBufferImpl(this);
        a.ro = true;
        return a;
    }
    public java.nio. DoubleBuffer compact()
    {
        return this;
    }
    public boolean isDirect()
    {
        return backing_buffer != null;
    }
    public double get()
    {
        double e = backing_buffer[pos];
        pos++;
        return e;
    }
    public java.nio. DoubleBuffer put(double b)
    {
        backing_buffer[pos] = b;
        pos++;
        return this;
    }
    public double get(int index)
    {
        return backing_buffer[index];
    }
    public java.nio. DoubleBuffer put(int index, double b)
    {
      backing_buffer[index] = b;
      return this;
    }
  public char getChar() { char a = nio_get_Char(this, pos); inc_pos(2); return a; } public java.nio. DoubleBuffer putChar(char value) { nio_put_Char(this, pos, value); inc_pos(2); return this; } public char getChar(int index) { char a = nio_get_Char(this, index); inc_pos(2); return a; } public java.nio. DoubleBuffer putChar(int index, char value) { nio_put_Char(this, index, value); inc_pos(2); return this; };
  public short getShort() { short a = nio_get_Short(this, pos); inc_pos(2); return a; } public java.nio. DoubleBuffer putShort(short value) { nio_put_Short(this, pos, value); inc_pos(2); return this; } public short getShort(int index) { short a = nio_get_Short(this, index); inc_pos(2); return a; } public java.nio. DoubleBuffer putShort(int index, short value) { nio_put_Short(this, index, value); inc_pos(2); return this; };
  public int getInt() { int a = nio_get_Int(this, pos); inc_pos(4); return a; } public java.nio. DoubleBuffer putInt(int value) { nio_put_Int(this, pos, value); inc_pos(4); return this; } public int getInt(int index) { int a = nio_get_Int(this, index); inc_pos(4); return a; } public java.nio. DoubleBuffer putInt(int index, int value) { nio_put_Int(this, index, value); inc_pos(4); return this; };
  public long getLong() { long a = nio_get_Long(this, pos); inc_pos(8); return a; } public java.nio. DoubleBuffer putLong(long value) { nio_put_Long(this, pos, value); inc_pos(8); return this; } public long getLong(int index) { long a = nio_get_Long(this, index); inc_pos(8); return a; } public java.nio. DoubleBuffer putLong(int index, long value) { nio_put_Long(this, index, value); inc_pos(8); return this; };
  public float getFloat() { float a = nio_get_Float(this, pos); inc_pos(4); return a; } public java.nio. DoubleBuffer putFloat(float value) { nio_put_Float(this, pos, value); inc_pos(4); return this; } public float getFloat(int index) { float a = nio_get_Float(this, index); inc_pos(4); return a; } public java.nio. DoubleBuffer putFloat(int index, float value) { nio_put_Float(this, index, value); inc_pos(4); return this; };
  public double getDouble() { return get(); } public java.nio. DoubleBuffer putDouble(double value) { return put(value); } public double getDouble(int index) { return get(index); } public java.nio. DoubleBuffer putDouble(int index, double value) { return put(index, value); };
}
