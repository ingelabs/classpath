package gnu.java.nio;
import java.nio.*;
public final class DoubleBufferImpl extends java.nio. DoubleBuffer
{
    private int array_offset;
    private boolean ro;
  public DoubleBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new double[cap];
      this.capacity(cap);
      this.position(off);
      this.limit(lim);
    }
  public DoubleBufferImpl(double[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.capacity(array.length);
      this.position(off);
      this.limit(lim);
    }
  public DoubleBufferImpl(DoubleBufferImpl copy)
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
  private static native double[] nio_cast(byte[]copy);
  private static native double[] nio_cast(char[]copy);
  private static native double[] nio_cast(short[]copy);
  private static native double[] nio_cast(long[]copy);
  private static native double[] nio_cast(int[]copy);
  private static native double[] nio_cast(float[]copy);
  private static native double[] nio_cast(double[]copy);
  DoubleBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native byte nio_get_Byte(DoubleBufferImpl b, int index, int limit); private static native void nio_put_Byte(DoubleBufferImpl b, int index, int limit, byte value); public java.nio. ByteBuffer asByteBuffer() { gnu.java.nio. ByteBufferImpl res = new gnu.java.nio. ByteBufferImpl(backing_buffer); res.limit((limit()*1)/8); return res; }
  DoubleBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native char nio_get_Char(DoubleBufferImpl b, int index, int limit); private static native void nio_put_Char(DoubleBufferImpl b, int index, int limit, char value); public java.nio. CharBuffer asCharBuffer() { gnu.java.nio. CharBufferImpl res = new gnu.java.nio. CharBufferImpl(backing_buffer); res.limit((limit()*2)/8); return res; }
  DoubleBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native short nio_get_Short(DoubleBufferImpl b, int index, int limit); private static native void nio_put_Short(DoubleBufferImpl b, int index, int limit, short value); public java.nio. ShortBuffer asShortBuffer() { gnu.java.nio. ShortBufferImpl res = new gnu.java.nio. ShortBufferImpl(backing_buffer); res.limit((limit()*2)/8); return res; }
  DoubleBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native int nio_get_Int(DoubleBufferImpl b, int index, int limit); private static native void nio_put_Int(DoubleBufferImpl b, int index, int limit, int value); public java.nio. IntBuffer asIntBuffer() { gnu.java.nio. IntBufferImpl res = new gnu.java.nio. IntBufferImpl(backing_buffer); res.limit((limit()*4)/8); return res; }
  DoubleBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native long nio_get_Long(DoubleBufferImpl b, int index, int limit); private static native void nio_put_Long(DoubleBufferImpl b, int index, int limit, long value); public java.nio. LongBuffer asLongBuffer() { gnu.java.nio. LongBufferImpl res = new gnu.java.nio. LongBufferImpl(backing_buffer); res.limit((limit()*8)/8); return res; }
  DoubleBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native float nio_get_Float(DoubleBufferImpl b, int index, int limit); private static native void nio_put_Float(DoubleBufferImpl b, int index, int limit, float value); public java.nio. FloatBuffer asFloatBuffer() { gnu.java.nio. FloatBufferImpl res = new gnu.java.nio. FloatBufferImpl(backing_buffer); res.limit((limit()*4)/8); return res; }
  DoubleBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native double nio_get_Double(DoubleBufferImpl b, int index, int limit); private static native void nio_put_Double(DoubleBufferImpl b, int index, int limit, double value); public java.nio. DoubleBuffer asDoubleBuffer() { gnu.java.nio. DoubleBufferImpl res = new gnu.java.nio. DoubleBufferImpl(backing_buffer); res.limit((limit()*8)/8); return res; }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. DoubleBuffer slice()
    {
        DoubleBufferImpl A = new DoubleBufferImpl(this);
        A.array_offset = position();
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
  final public double get()
    {
        double e = backing_buffer[position()];
        position(position()+1);
        return e;
    }
  final public java.nio. DoubleBuffer put(double b)
    {
        backing_buffer[position()] = b;
        position(position()+1);
        return this;
    }
  final public double get(int index)
    {
        return backing_buffer[index];
    }
   final public java.nio. DoubleBuffer put(int index, double b)
    {
      backing_buffer[index] = b;
      return this;
    }
  final public char getChar() { char a = nio_get_Char(this, position(), limit()); inc_pos(2); return a; } final public java.nio. DoubleBuffer putChar(char value) { nio_put_Char(this, position(), limit(), value); inc_pos(2); return this; } final public char getChar(int index) { char a = nio_get_Char(this, index, limit()); return a; } final public java.nio. DoubleBuffer putChar(int index, char value) { nio_put_Char(this, index, limit(), value); return this; };
  final public short getShort() { short a = nio_get_Short(this, position(), limit()); inc_pos(2); return a; } final public java.nio. DoubleBuffer putShort(short value) { nio_put_Short(this, position(), limit(), value); inc_pos(2); return this; } final public short getShort(int index) { short a = nio_get_Short(this, index, limit()); return a; } final public java.nio. DoubleBuffer putShort(int index, short value) { nio_put_Short(this, index, limit(), value); return this; };
  final public int getInt() { int a = nio_get_Int(this, position(), limit()); inc_pos(4); return a; } final public java.nio. DoubleBuffer putInt(int value) { nio_put_Int(this, position(), limit(), value); inc_pos(4); return this; } final public int getInt(int index) { int a = nio_get_Int(this, index, limit()); return a; } final public java.nio. DoubleBuffer putInt(int index, int value) { nio_put_Int(this, index, limit(), value); return this; };
  final public long getLong() { long a = nio_get_Long(this, position(), limit()); inc_pos(8); return a; } final public java.nio. DoubleBuffer putLong(long value) { nio_put_Long(this, position(), limit(), value); inc_pos(8); return this; } final public long getLong(int index) { long a = nio_get_Long(this, index, limit()); return a; } final public java.nio. DoubleBuffer putLong(int index, long value) { nio_put_Long(this, index, limit(), value); return this; };
  final public float getFloat() { float a = nio_get_Float(this, position(), limit()); inc_pos(4); return a; } final public java.nio. DoubleBuffer putFloat(float value) { nio_put_Float(this, position(), limit(), value); inc_pos(4); return this; } final public float getFloat(int index) { float a = nio_get_Float(this, index, limit()); return a; } final public java.nio. DoubleBuffer putFloat(int index, float value) { nio_put_Float(this, index, limit(), value); return this; };
  final public double getDouble() { return get(); } final public java.nio. DoubleBuffer putDouble(double value) { return put(value); } final public double getDouble(int index) { return get(index); } final public java.nio. DoubleBuffer putDouble(int index, double value) { return put(index, value); };
}
