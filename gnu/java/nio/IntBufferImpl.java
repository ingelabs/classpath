package manta.runtime;
import java.nio.*;
public final class IntBufferImpl extends java.nio. IntBuffer
{
    private int array_offset;
    int [] backing_buffer;
    private boolean ro;
    IntBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new int[cap];
      this.cap = cap;
      this.pos = off;
      this.limit = lim;
    }
    IntBufferImpl(int[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.cap = array.length;
      this.pos = off;
      this.limit = lim;
    }
    IntBufferImpl(IntBufferImpl copy)
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
  private static MantaNative int[] nio_cast(byte[]copy);
  private static MantaNative int[] nio_cast(char[]copy);
  private static MantaNative int[] nio_cast(short[]copy);
  private static MantaNative int[] nio_cast(long[]copy);
  private static MantaNative int[] nio_cast(int[]copy);
  private static MantaNative int[] nio_cast(float[]copy);
  private static MantaNative int[] nio_cast(double[]copy);
  IntBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative byte nio_get_Byte(IntBufferImpl b, int index); private static MantaNative void nio_put_Byte(IntBufferImpl b, int index, byte value); public java.nio. ByteBuffer asByteBuffer() { return new manta.runtime. ByteBufferImpl(backing_buffer); }
  IntBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative char nio_get_Char(IntBufferImpl b, int index); private static MantaNative void nio_put_Char(IntBufferImpl b, int index, char value); public java.nio. CharBuffer asCharBuffer() { return new manta.runtime. CharBufferImpl(backing_buffer); }
  IntBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative short nio_get_Short(IntBufferImpl b, int index); private static MantaNative void nio_put_Short(IntBufferImpl b, int index, short value); public java.nio. ShortBuffer asShortBuffer() { return new manta.runtime. ShortBufferImpl(backing_buffer); }
  IntBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative int nio_get_Int(IntBufferImpl b, int index); private static MantaNative void nio_put_Int(IntBufferImpl b, int index, int value); public java.nio. IntBuffer asIntBuffer() { return new manta.runtime. IntBufferImpl(backing_buffer); }
  IntBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative long nio_get_Long(IntBufferImpl b, int index); private static MantaNative void nio_put_Long(IntBufferImpl b, int index, long value); public java.nio. LongBuffer asLongBuffer() { return new manta.runtime. LongBufferImpl(backing_buffer); }
  IntBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative float nio_get_Float(IntBufferImpl b, int index); private static MantaNative void nio_put_Float(IntBufferImpl b, int index, float value); public java.nio. FloatBuffer asFloatBuffer() { return new manta.runtime. FloatBufferImpl(backing_buffer); }
  IntBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative double nio_get_Double(IntBufferImpl b, int index); private static MantaNative void nio_put_Double(IntBufferImpl b, int index, double value); public java.nio. DoubleBuffer asDoubleBuffer() { return new manta.runtime. DoubleBufferImpl(backing_buffer); }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. IntBuffer slice()
    {
        IntBufferImpl A = new IntBufferImpl(this);
        A.array_offset = pos;
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
    public int get()
    {
        int e = backing_buffer[pos];
        pos++;
        return e;
    }
    public java.nio. IntBuffer put(int b)
    {
        backing_buffer[pos] = b;
        pos++;
        return this;
    }
    public int get(int index)
    {
        return backing_buffer[index];
    }
    public java.nio. IntBuffer put(int index, int b)
    {
      backing_buffer[index] = b;
      return this;
    }
  public char getChar() { char a = nio_get_Char(this, pos); inc_pos(2); return a; } public java.nio. IntBuffer putChar(char value) { nio_put_Char(this, pos, value); inc_pos(2); return this; } public char getChar(int index) { char a = nio_get_Char(this, index); inc_pos(2); return a; } public java.nio. IntBuffer putChar(int index, char value) { nio_put_Char(this, index, value); inc_pos(2); return this; };
  public short getShort() { short a = nio_get_Short(this, pos); inc_pos(2); return a; } public java.nio. IntBuffer putShort(short value) { nio_put_Short(this, pos, value); inc_pos(2); return this; } public short getShort(int index) { short a = nio_get_Short(this, index); inc_pos(2); return a; } public java.nio. IntBuffer putShort(int index, short value) { nio_put_Short(this, index, value); inc_pos(2); return this; };
  public int getInt() { return get(); } public java.nio. IntBuffer putInt(int value) { return put(value); } public int getInt(int index) { return get(index); } public java.nio. IntBuffer putInt(int index, int value) { return put(index, value); };
  public long getLong() { long a = nio_get_Long(this, pos); inc_pos(8); return a; } public java.nio. IntBuffer putLong(long value) { nio_put_Long(this, pos, value); inc_pos(8); return this; } public long getLong(int index) { long a = nio_get_Long(this, index); inc_pos(8); return a; } public java.nio. IntBuffer putLong(int index, long value) { nio_put_Long(this, index, value); inc_pos(8); return this; };
  public float getFloat() { float a = nio_get_Float(this, pos); inc_pos(4); return a; } public java.nio. IntBuffer putFloat(float value) { nio_put_Float(this, pos, value); inc_pos(4); return this; } public float getFloat(int index) { float a = nio_get_Float(this, index); inc_pos(4); return a; } public java.nio. IntBuffer putFloat(int index, float value) { nio_put_Float(this, index, value); inc_pos(4); return this; };
  public double getDouble() { double a = nio_get_Double(this, pos); inc_pos(8); return a; } public java.nio. IntBuffer putDouble(double value) { nio_put_Double(this, pos, value); inc_pos(8); return this; } public double getDouble(int index) { double a = nio_get_Double(this, index); inc_pos(8); return a; } public java.nio. IntBuffer putDouble(int index, double value) { nio_put_Double(this, index, value); inc_pos(8); return this; };
}
