package gnu.java.nio;
import java.nio.*;
public final class ByteBufferImpl extends java.nio. ByteBuffer
{
    private int array_offset;
    private boolean ro;
  public ByteBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new byte[cap];
      this.capacity(cap);
      this.position(off);
      this.limit(lim);
    }
  public ByteBufferImpl(byte[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.capacity(array.length);
      this.position(off);
      this.limit(lim);
    }
  public ByteBufferImpl(ByteBufferImpl copy)
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
  private static native byte[] nio_cast(byte[]copy);
  private static native byte[] nio_cast(char[]copy);
  private static native byte[] nio_cast(short[]copy);
  private static native byte[] nio_cast(long[]copy);
  private static native byte[] nio_cast(int[]copy);
  private static native byte[] nio_cast(float[]copy);
  private static native byte[] nio_cast(double[]copy);
  ByteBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native byte nio_get_Byte(ByteBufferImpl b, int index, int limit); private static native void nio_put_Byte(ByteBufferImpl b, int index, int limit, byte value); public java.nio. ByteBuffer asByteBuffer() { gnu.java.nio. ByteBufferImpl res = new gnu.java.nio. ByteBufferImpl(backing_buffer); res.limit((limit()*1)/1); return res; }
  ByteBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native char nio_get_Char(ByteBufferImpl b, int index, int limit); private static native void nio_put_Char(ByteBufferImpl b, int index, int limit, char value); public java.nio. CharBuffer asCharBuffer() { gnu.java.nio. CharBufferImpl res = new gnu.java.nio. CharBufferImpl(backing_buffer); res.limit((limit()*2)/1); return res; }
  ByteBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native short nio_get_Short(ByteBufferImpl b, int index, int limit); private static native void nio_put_Short(ByteBufferImpl b, int index, int limit, short value); public java.nio. ShortBuffer asShortBuffer() { gnu.java.nio. ShortBufferImpl res = new gnu.java.nio. ShortBufferImpl(backing_buffer); res.limit((limit()*2)/1); return res; }
  ByteBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native int nio_get_Int(ByteBufferImpl b, int index, int limit); private static native void nio_put_Int(ByteBufferImpl b, int index, int limit, int value); public java.nio. IntBuffer asIntBuffer() { gnu.java.nio. IntBufferImpl res = new gnu.java.nio. IntBufferImpl(backing_buffer); res.limit((limit()*4)/1); return res; }
  ByteBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native long nio_get_Long(ByteBufferImpl b, int index, int limit); private static native void nio_put_Long(ByteBufferImpl b, int index, int limit, long value); public java.nio. LongBuffer asLongBuffer() { gnu.java.nio. LongBufferImpl res = new gnu.java.nio. LongBufferImpl(backing_buffer); res.limit((limit()*8)/1); return res; }
  ByteBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native float nio_get_Float(ByteBufferImpl b, int index, int limit); private static native void nio_put_Float(ByteBufferImpl b, int index, int limit, float value); public java.nio. FloatBuffer asFloatBuffer() { gnu.java.nio. FloatBufferImpl res = new gnu.java.nio. FloatBufferImpl(backing_buffer); res.limit((limit()*4)/1); return res; }
  ByteBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native double nio_get_Double(ByteBufferImpl b, int index, int limit); private static native void nio_put_Double(ByteBufferImpl b, int index, int limit, double value); public java.nio. DoubleBuffer asDoubleBuffer() { gnu.java.nio. DoubleBufferImpl res = new gnu.java.nio. DoubleBufferImpl(backing_buffer); res.limit((limit()*8)/1); return res; }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. ByteBuffer slice()
    {
        ByteBufferImpl A = new ByteBufferImpl(this);
        A.array_offset = position();
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
  final public byte get()
    {
        byte e = backing_buffer[position()];
        position(position()+1);
        return e;
    }
  final public java.nio. ByteBuffer put(byte b)
    {
        backing_buffer[position()] = b;
        position(position()+1);
        return this;
    }
  final public byte get(int index)
    {
        return backing_buffer[index];
    }
   final public java.nio. ByteBuffer put(int index, byte b)
    {
      backing_buffer[index] = b;
      return this;
    }
  final public char getChar() { char a = nio_get_Char(this, position(), limit()); inc_pos(2); return a; } final public java.nio. ByteBuffer putChar(char value) { nio_put_Char(this, position(), limit(), value); inc_pos(2); return this; } final public char getChar(int index) { char a = nio_get_Char(this, index, limit()); return a; } final public java.nio. ByteBuffer putChar(int index, char value) { nio_put_Char(this, index, limit(), value); return this; };
  final public short getShort() { short a = nio_get_Short(this, position(), limit()); inc_pos(2); return a; } final public java.nio. ByteBuffer putShort(short value) { nio_put_Short(this, position(), limit(), value); inc_pos(2); return this; } final public short getShort(int index) { short a = nio_get_Short(this, index, limit()); return a; } final public java.nio. ByteBuffer putShort(int index, short value) { nio_put_Short(this, index, limit(), value); return this; };
  final public int getInt() { int a = nio_get_Int(this, position(), limit()); inc_pos(4); return a; } final public java.nio. ByteBuffer putInt(int value) { nio_put_Int(this, position(), limit(), value); inc_pos(4); return this; } final public int getInt(int index) { int a = nio_get_Int(this, index, limit()); return a; } final public java.nio. ByteBuffer putInt(int index, int value) { nio_put_Int(this, index, limit(), value); return this; };
  final public long getLong() { long a = nio_get_Long(this, position(), limit()); inc_pos(8); return a; } final public java.nio. ByteBuffer putLong(long value) { nio_put_Long(this, position(), limit(), value); inc_pos(8); return this; } final public long getLong(int index) { long a = nio_get_Long(this, index, limit()); return a; } final public java.nio. ByteBuffer putLong(int index, long value) { nio_put_Long(this, index, limit(), value); return this; };
  final public float getFloat() { float a = nio_get_Float(this, position(), limit()); inc_pos(4); return a; } final public java.nio. ByteBuffer putFloat(float value) { nio_put_Float(this, position(), limit(), value); inc_pos(4); return this; } final public float getFloat(int index) { float a = nio_get_Float(this, index, limit()); return a; } final public java.nio. ByteBuffer putFloat(int index, float value) { nio_put_Float(this, index, limit(), value); return this; };
  final public double getDouble() { double a = nio_get_Double(this, position(), limit()); inc_pos(8); return a; } final public java.nio. ByteBuffer putDouble(double value) { nio_put_Double(this, position(), limit(), value); inc_pos(8); return this; } final public double getDouble(int index) { double a = nio_get_Double(this, index, limit()); return a; } final public java.nio. ByteBuffer putDouble(int index, double value) { nio_put_Double(this, index, limit(), value); return this; };
}
