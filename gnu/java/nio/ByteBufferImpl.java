package manta.runtime;
import java.nio.*;
public final class ByteBufferImpl extends java.nio. ByteBuffer
{
    private int array_offset;
    byte [] backing_buffer;
    private boolean ro;
    ByteBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new byte[cap];
      this.cap = cap;
      this.pos = off;
      this.limit = lim;
    }
    ByteBufferImpl(byte[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.cap = array.length;
      this.pos = off;
      this.limit = lim;
    }
    ByteBufferImpl(ByteBufferImpl copy)
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
  private static MantaNative byte[] nio_cast(byte[]copy);
  private static MantaNative byte[] nio_cast(char[]copy);
  private static MantaNative byte[] nio_cast(short[]copy);
  private static MantaNative byte[] nio_cast(long[]copy);
  private static MantaNative byte[] nio_cast(int[]copy);
  private static MantaNative byte[] nio_cast(float[]copy);
  private static MantaNative byte[] nio_cast(double[]copy);
  ByteBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative byte nio_get_Byte(ByteBufferImpl b, int index); private static MantaNative void nio_put_Byte(ByteBufferImpl b, int index, byte value); public java.nio. ByteBuffer asByteBuffer() { return new manta.runtime. ByteBufferImpl(backing_buffer); }
  ByteBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative char nio_get_Char(ByteBufferImpl b, int index); private static MantaNative void nio_put_Char(ByteBufferImpl b, int index, char value); public java.nio. CharBuffer asCharBuffer() { return new manta.runtime. CharBufferImpl(backing_buffer); }
  ByteBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative short nio_get_Short(ByteBufferImpl b, int index); private static MantaNative void nio_put_Short(ByteBufferImpl b, int index, short value); public java.nio. ShortBuffer asShortBuffer() { return new manta.runtime. ShortBufferImpl(backing_buffer); }
  ByteBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative int nio_get_Int(ByteBufferImpl b, int index); private static MantaNative void nio_put_Int(ByteBufferImpl b, int index, int value); public java.nio. IntBuffer asIntBuffer() { return new manta.runtime. IntBufferImpl(backing_buffer); }
  ByteBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative long nio_get_Long(ByteBufferImpl b, int index); private static MantaNative void nio_put_Long(ByteBufferImpl b, int index, long value); public java.nio. LongBuffer asLongBuffer() { return new manta.runtime. LongBufferImpl(backing_buffer); }
  ByteBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative float nio_get_Float(ByteBufferImpl b, int index); private static MantaNative void nio_put_Float(ByteBufferImpl b, int index, float value); public java.nio. FloatBuffer asFloatBuffer() { return new manta.runtime. FloatBufferImpl(backing_buffer); }
  ByteBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative double nio_get_Double(ByteBufferImpl b, int index); private static MantaNative void nio_put_Double(ByteBufferImpl b, int index, double value); public java.nio. DoubleBuffer asDoubleBuffer() { return new manta.runtime. DoubleBufferImpl(backing_buffer); }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. ByteBuffer slice()
    {
        ByteBufferImpl A = new ByteBufferImpl(this);
        A.array_offset = pos;
        return A;
    }
    public java.nio. ByteBuffer duplicate()
    {
        return new ByteBufferImpl(this);
    }
    public java.nio. ByteBuffer asReadOnlyBuffer()
    {
        ByteBufferImpl a = new ByteBufferImpl(this);
        a.ro = true;
        return a;
    }
    public java.nio. ByteBuffer compact()
    {
        return this;
    }
    public boolean isDirect()
    {
        return backing_buffer != null;
    }
    public byte get()
    {
        byte e = backing_buffer[pos];
        pos++;
        return e;
    }
    public java.nio. ByteBuffer put(byte b)
    {
        backing_buffer[pos] = b;
        pos++;
        return this;
    }
    public byte get(int index)
    {
        return backing_buffer[index];
    }
    public java.nio. ByteBuffer put(int index, byte b)
    {
      backing_buffer[index] = b;
      return this;
    }
  public char getChar() { char a = nio_get_Char(this, pos); inc_pos(2); return a; } public java.nio. ByteBuffer putChar(char value) { nio_put_Char(this, pos, value); inc_pos(2); return this; } public char getChar(int index) { char a = nio_get_Char(this, index); inc_pos(2); return a; } public java.nio. ByteBuffer putChar(int index, char value) { nio_put_Char(this, index, value); inc_pos(2); return this; };
  public short getShort() { short a = nio_get_Short(this, pos); inc_pos(2); return a; } public java.nio. ByteBuffer putShort(short value) { nio_put_Short(this, pos, value); inc_pos(2); return this; } public short getShort(int index) { short a = nio_get_Short(this, index); inc_pos(2); return a; } public java.nio. ByteBuffer putShort(int index, short value) { nio_put_Short(this, index, value); inc_pos(2); return this; };
  public int getInt() { int a = nio_get_Int(this, pos); inc_pos(4); return a; } public java.nio. ByteBuffer putInt(int value) { nio_put_Int(this, pos, value); inc_pos(4); return this; } public int getInt(int index) { int a = nio_get_Int(this, index); inc_pos(4); return a; } public java.nio. ByteBuffer putInt(int index, int value) { nio_put_Int(this, index, value); inc_pos(4); return this; };
  public long getLong() { long a = nio_get_Long(this, pos); inc_pos(8); return a; } public java.nio. ByteBuffer putLong(long value) { nio_put_Long(this, pos, value); inc_pos(8); return this; } public long getLong(int index) { long a = nio_get_Long(this, index); inc_pos(8); return a; } public java.nio. ByteBuffer putLong(int index, long value) { nio_put_Long(this, index, value); inc_pos(8); return this; };
  public float getFloat() { float a = nio_get_Float(this, pos); inc_pos(4); return a; } public java.nio. ByteBuffer putFloat(float value) { nio_put_Float(this, pos, value); inc_pos(4); return this; } public float getFloat(int index) { float a = nio_get_Float(this, index); inc_pos(4); return a; } public java.nio. ByteBuffer putFloat(int index, float value) { nio_put_Float(this, index, value); inc_pos(4); return this; };
  public double getDouble() { double a = nio_get_Double(this, pos); inc_pos(8); return a; } public java.nio. ByteBuffer putDouble(double value) { nio_put_Double(this, pos, value); inc_pos(8); return this; } public double getDouble(int index) { double a = nio_get_Double(this, index); inc_pos(8); return a; } public java.nio. ByteBuffer putDouble(int index, double value) { nio_put_Double(this, index, value); inc_pos(8); return this; };
}
