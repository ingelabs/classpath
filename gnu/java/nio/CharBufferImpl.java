package manta.runtime;
import java.nio.*;
public final class CharBufferImpl extends java.nio. CharBuffer
{
    private int array_offset;
    char [] backing_buffer;
    private boolean ro;
    CharBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new char[cap];
      this.cap = cap;
      this.pos = off;
      this.limit = lim;
    }
    CharBufferImpl(char[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.cap = array.length;
      this.pos = off;
      this.limit = lim;
    }
    CharBufferImpl(CharBufferImpl copy)
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
  private static MantaNative char[] nio_cast(byte[]copy);
  private static MantaNative char[] nio_cast(char[]copy);
  private static MantaNative char[] nio_cast(short[]copy);
  private static MantaNative char[] nio_cast(long[]copy);
  private static MantaNative char[] nio_cast(int[]copy);
  private static MantaNative char[] nio_cast(float[]copy);
  private static MantaNative char[] nio_cast(double[]copy);
  CharBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative byte nio_get_Byte(CharBufferImpl b, int index); private static MantaNative void nio_put_Byte(CharBufferImpl b, int index, byte value); public java.nio. ByteBuffer asByteBuffer() { return new manta.runtime. ByteBufferImpl(backing_buffer); }
  CharBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative char nio_get_Char(CharBufferImpl b, int index); private static MantaNative void nio_put_Char(CharBufferImpl b, int index, char value); public java.nio. CharBuffer asCharBuffer() { return new manta.runtime. CharBufferImpl(backing_buffer); }
  CharBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative short nio_get_Short(CharBufferImpl b, int index); private static MantaNative void nio_put_Short(CharBufferImpl b, int index, short value); public java.nio. ShortBuffer asShortBuffer() { return new manta.runtime. ShortBufferImpl(backing_buffer); }
  CharBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative int nio_get_Int(CharBufferImpl b, int index); private static MantaNative void nio_put_Int(CharBufferImpl b, int index, int value); public java.nio. IntBuffer asIntBuffer() { return new manta.runtime. IntBufferImpl(backing_buffer); }
  CharBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative long nio_get_Long(CharBufferImpl b, int index); private static MantaNative void nio_put_Long(CharBufferImpl b, int index, long value); public java.nio. LongBuffer asLongBuffer() { return new manta.runtime. LongBufferImpl(backing_buffer); }
  CharBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative float nio_get_Float(CharBufferImpl b, int index); private static MantaNative void nio_put_Float(CharBufferImpl b, int index, float value); public java.nio. FloatBuffer asFloatBuffer() { return new manta.runtime. FloatBufferImpl(backing_buffer); }
  CharBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static MantaNative double nio_get_Double(CharBufferImpl b, int index); private static MantaNative void nio_put_Double(CharBufferImpl b, int index, double value); public java.nio. DoubleBuffer asDoubleBuffer() { return new manta.runtime. DoubleBufferImpl(backing_buffer); }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. CharBuffer slice()
    {
        CharBufferImpl A = new CharBufferImpl(this);
        A.array_offset = pos;
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
    public char get()
    {
        char e = backing_buffer[pos];
        pos++;
        return e;
    }
    public java.nio. CharBuffer put(char b)
    {
        backing_buffer[pos] = b;
        pos++;
        return this;
    }
    public char get(int index)
    {
        return backing_buffer[index];
    }
    public java.nio. CharBuffer put(int index, char b)
    {
      backing_buffer[index] = b;
      return this;
    }
  public char getChar() { return get(); } public java.nio. CharBuffer putChar(char value) { return put(value); } public char getChar(int index) { return get(index); } public java.nio. CharBuffer putChar(int index, char value) { return put(index, value); };
  public short getShort() { short a = nio_get_Short(this, pos); inc_pos(2); return a; } public java.nio. CharBuffer putShort(short value) { nio_put_Short(this, pos, value); inc_pos(2); return this; } public short getShort(int index) { short a = nio_get_Short(this, index); inc_pos(2); return a; } public java.nio. CharBuffer putShort(int index, short value) { nio_put_Short(this, index, value); inc_pos(2); return this; };
  public int getInt() { int a = nio_get_Int(this, pos); inc_pos(4); return a; } public java.nio. CharBuffer putInt(int value) { nio_put_Int(this, pos, value); inc_pos(4); return this; } public int getInt(int index) { int a = nio_get_Int(this, index); inc_pos(4); return a; } public java.nio. CharBuffer putInt(int index, int value) { nio_put_Int(this, index, value); inc_pos(4); return this; };
  public long getLong() { long a = nio_get_Long(this, pos); inc_pos(8); return a; } public java.nio. CharBuffer putLong(long value) { nio_put_Long(this, pos, value); inc_pos(8); return this; } public long getLong(int index) { long a = nio_get_Long(this, index); inc_pos(8); return a; } public java.nio. CharBuffer putLong(int index, long value) { nio_put_Long(this, index, value); inc_pos(8); return this; };
  public float getFloat() { float a = nio_get_Float(this, pos); inc_pos(4); return a; } public java.nio. CharBuffer putFloat(float value) { nio_put_Float(this, pos, value); inc_pos(4); return this; } public float getFloat(int index) { float a = nio_get_Float(this, index); inc_pos(4); return a; } public java.nio. CharBuffer putFloat(int index, float value) { nio_put_Float(this, index, value); inc_pos(4); return this; };
  public double getDouble() { double a = nio_get_Double(this, pos); inc_pos(8); return a; } public java.nio. CharBuffer putDouble(double value) { nio_put_Double(this, pos, value); inc_pos(8); return this; } public double getDouble(int index) { double a = nio_get_Double(this, index); inc_pos(8); return a; } public java.nio. CharBuffer putDouble(int index, double value) { nio_put_Double(this, index, value); inc_pos(8); return this; };
    public String toString()
    {
      if (backing_buffer != null)
        {
          return new String(backing_buffer, position(), limit());
        }
      return super.toString();
    }
}
