package manta.runtime;
import java.nio.*;
public final class LongBufferImpl extends java.nio. LongBuffer
{
    private int array_offset;
    long [] backing_buffer;
    private boolean ro;
    LongBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new long[cap];
      this.cap = cap;
      this.pos = off;
      this.limit = lim;
    }
    LongBufferImpl(long[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.cap = array.length;
      this.pos = off;
      this.limit = lim;
    }
    LongBufferImpl(LongBufferImpl copy)
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
  private static MantaNative long[] nio_cast(byte[]copy);
  private static MantaNative long[] nio_cast(char[]copy);
  private static MantaNative long[] nio_cast(short[]copy);
  private static MantaNative long[] nio_cast(long[]copy);
  private static MantaNative long[] nio_cast(int[]copy);
  private static MantaNative long[] nio_cast(float[]copy);
  private static MantaNative long[] nio_cast(double[]copy);
  LongBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative byte nio_get_Byte(LongBufferImpl b, int index); private static MantaNative void nio_put_Byte(LongBufferImpl b, int index, byte value); public java.nio. ByteBuffer asByteBuffer() { return new manta.runtime. ByteBufferImpl(backing_buffer); }
  LongBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative char nio_get_Char(LongBufferImpl b, int index); private static MantaNative void nio_put_Char(LongBufferImpl b, int index, char value); public java.nio. CharBuffer asCharBuffer() { return new manta.runtime. CharBufferImpl(backing_buffer); }
  LongBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative short nio_get_Short(LongBufferImpl b, int index); private static MantaNative void nio_put_Short(LongBufferImpl b, int index, short value); public java.nio. ShortBuffer asShortBuffer() { return new manta.runtime. ShortBufferImpl(backing_buffer); }
  LongBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative int nio_get_Int(LongBufferImpl b, int index); private static MantaNative void nio_put_Int(LongBufferImpl b, int index, int value); public java.nio. IntBuffer asIntBuffer() { return new manta.runtime. IntBufferImpl(backing_buffer); }
  LongBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative long nio_get_Long(LongBufferImpl b, int index); private static MantaNative void nio_put_Long(LongBufferImpl b, int index, long value); public java.nio. LongBuffer asLongBuffer() { return new manta.runtime. LongBufferImpl(backing_buffer); }
  LongBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative float nio_get_Float(LongBufferImpl b, int index); private static MantaNative void nio_put_Float(LongBufferImpl b, int index, float value); public java.nio. FloatBuffer asFloatBuffer() { return new manta.runtime. FloatBufferImpl(backing_buffer); }
  LongBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative double nio_get_Double(LongBufferImpl b, int index); private static MantaNative void nio_put_Double(LongBufferImpl b, int index, double value); public java.nio. DoubleBuffer asDoubleBuffer() { return new manta.runtime. DoubleBufferImpl(backing_buffer); }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. LongBuffer slice()
    {
        LongBufferImpl A = new LongBufferImpl(this);
        A.array_offset = pos;
        return A;
    }
    public java.nio. LongBuffer duplicate()
    {
        return new LongBufferImpl(this);
    }
    public java.nio. LongBuffer asReadOnlyBuffer()
    {
        LongBufferImpl a = new LongBufferImpl(this);
        a.ro = true;
        return a;
    }
    public java.nio. LongBuffer compact()
    {
        return this;
    }
    public boolean isDirect()
    {
        return backing_buffer != null;
    }
    public long get()
    {
        long e = backing_buffer[pos];
        pos++;
        return e;
    }
    public java.nio. LongBuffer put(long b)
    {
        backing_buffer[pos] = b;
        pos++;
        return this;
    }
    public long get(int index)
    {
        return backing_buffer[index];
    }
    public java.nio. LongBuffer put(int index, long b)
    {
      backing_buffer[index] = b;
      return this;
    }
  public char getChar() { char a = nio_get_Char(this, pos); inc_pos(2); return a; } public java.nio. LongBuffer putChar(char value) { nio_put_Char(this, pos, value); inc_pos(2); return this; } public char getChar(int index) { char a = nio_get_Char(this, index); inc_pos(2); return a; } public java.nio. LongBuffer putChar(int index, char value) { nio_put_Char(this, index, value); inc_pos(2); return this; };
  public short getShort() { short a = nio_get_Short(this, pos); inc_pos(2); return a; } public java.nio. LongBuffer putShort(short value) { nio_put_Short(this, pos, value); inc_pos(2); return this; } public short getShort(int index) { short a = nio_get_Short(this, index); inc_pos(2); return a; } public java.nio. LongBuffer putShort(int index, short value) { nio_put_Short(this, index, value); inc_pos(2); return this; };
  public int getInt() { int a = nio_get_Int(this, pos); inc_pos(4); return a; } public java.nio. LongBuffer putInt(int value) { nio_put_Int(this, pos, value); inc_pos(4); return this; } public int getInt(int index) { int a = nio_get_Int(this, index); inc_pos(4); return a; } public java.nio. LongBuffer putInt(int index, int value) { nio_put_Int(this, index, value); inc_pos(4); return this; };
  public long getLong() { return get(); } public java.nio. LongBuffer putLong(long value) { return put(value); } public long getLong(int index) { return get(index); } public java.nio. LongBuffer putLong(int index, long value) { return put(index, value); };
  public float getFloat() { float a = nio_get_Float(this, pos); inc_pos(4); return a; } public java.nio. LongBuffer putFloat(float value) { nio_put_Float(this, pos, value); inc_pos(4); return this; } public float getFloat(int index) { float a = nio_get_Float(this, index); inc_pos(4); return a; } public java.nio. LongBuffer putFloat(int index, float value) { nio_put_Float(this, index, value); inc_pos(4); return this; };
  public double getDouble() { double a = nio_get_Double(this, pos); inc_pos(8); return a; } public java.nio. LongBuffer putDouble(double value) { nio_put_Double(this, pos, value); inc_pos(8); return this; } public double getDouble(int index) { double a = nio_get_Double(this, index); inc_pos(8); return a; } public java.nio. LongBuffer putDouble(int index, double value) { nio_put_Double(this, index, value); inc_pos(8); return this; };
}
