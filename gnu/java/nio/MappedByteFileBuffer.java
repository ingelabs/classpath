package gnu.java.nio;
import java.nio.*;
import java.io.IOException;
final public class MappedByteFileBuffer
 extends MappedByteBuffer
{
  public long address;
  boolean ro;
  boolean direct;
  public FileChannelImpl ch;
  public MappedByteFileBuffer(FileChannelImpl ch)
  {
    this.ch = ch;
    address = ch.address;
    try {
      long si = ch.size() / 1;
      limit((int)si);
    } catch (IOException e) {
      System.err.println("failed to get size of file-channel's file");
    }
  }
  public MappedByteFileBuffer(MappedByteFileBuffer b)
  {
    this.ro = b.ro;
    this.ch = b.ch;
    address = b.address;
    limit(b.limit());
  }
  public boolean isReadOnly()
  {
    return ro;
  }
  public static native byte nio_read_Byte_file_channel(FileChannelImpl ch, int index, int limit, long address); public static native void nio_write_Byte_file_channel(FileChannelImpl ch, int index, int limit, byte value, long address);
  public static native short nio_read_Short_file_channel(FileChannelImpl ch, int index, int limit, long address); public static native void nio_write_Short_file_channel(FileChannelImpl ch, int index, int limit, short value, long address);
  public static native char nio_read_Char_file_channel(FileChannelImpl ch, int index, int limit, long address); public static native void nio_write_Char_file_channel(FileChannelImpl ch, int index, int limit, char value, long address);
  public static native int nio_read_Int_file_channel(FileChannelImpl ch, int index, int limit, long address); public static native void nio_write_Int_file_channel(FileChannelImpl ch, int index, int limit, int value, long address);
  public static native long nio_read_Long_file_channel(FileChannelImpl ch, int index, int limit, long address); public static native void nio_write_Long_file_channel(FileChannelImpl ch, int index, int limit, long value, long address);
  public static native float nio_read_Float_file_channel(FileChannelImpl ch, int index, int limit, long address); public static native void nio_write_Float_file_channel(FileChannelImpl ch, int index, int limit, float value, long address);
  public static native double nio_read_Double_file_channel(FileChannelImpl ch, int index, int limit, long address); public static native void nio_write_Double_file_channel(FileChannelImpl ch, int index, int limit, double value, long address);
final public byte get()
  {
    byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, position(), limit(), address);
    position(position() + 1);
    return a;
  }
final public ByteBuffer put(byte b)
  {
    MappedByteFileBuffer.nio_write_Byte_file_channel(ch, position(), limit(), b, address);
    position(position() + 1);
    return this;
  }
final public byte get(int index)
  {
    byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, index, limit(), address);
    return a;
  }
final public ByteBuffer put(int index, byte b)
  {
    MappedByteFileBuffer.nio_write_Byte_file_channel(ch, index, limit(), b, address);
    return this;
  }
final public ByteBuffer compact()
  {
    return this;
  }
final public boolean isDirect()
  {
    return direct;
  }
final public ByteBuffer slice()
  {
    MappedByteFileBuffer A = new MappedByteFileBuffer(this);
    return A;
  }
public ByteBuffer duplicate()
  {
    return new MappedByteFileBuffer(this);
  }
public ByteBuffer asReadOnlyBuffer()
  {
    MappedByteFileBuffer b = new MappedByteFileBuffer(this);
    b.ro = true;
    return b;
  }
  final public ByteBuffer asByteBuffer() { ByteBuffer res = new MappedByteFileBuffer(ch); res.limit((limit()*1)/1); return res; } final public byte getByte() { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, position(), limit(), address); position(position() + 1); return a; } final public ByteBuffer putByte(byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, position(), limit(), value, address); position(position() + 1); return this; } final public byte getByte(int index) { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, index, limit(), address); return a; } final public ByteBuffer putByte(int index, byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, index, limit(), value, address); return this; };
  final public CharBuffer asCharBuffer() { CharBuffer res = new MappedCharFileBuffer(ch); res.limit((limit()*1)/2); return res; } final public char getChar() { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, position(), limit(), address); position(position() + 1); return a; } final public ByteBuffer putChar(char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, position(), limit(), value, address); position(position() + 1); return this; } final public char getChar(int index) { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, index, limit(), address); return a; } final public ByteBuffer putChar(int index, char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, index, limit(), value, address); return this; };
  final public ShortBuffer asShortBuffer() { ShortBuffer res = new MappedShortFileBuffer(ch); res.limit((limit()*1)/2); return res; } final public short getShort() { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, position(), limit(), address); position(position() + 1); return a; } final public ByteBuffer putShort(short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, position(), limit(), value, address); position(position() + 1); return this; } final public short getShort(int index) { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, index, limit(), address); return a; } final public ByteBuffer putShort(int index, short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, index, limit(), value, address); return this; };
  final public IntBuffer asIntBuffer() { IntBuffer res = new MappedIntFileBuffer(ch); res.limit((limit()*1)/4); return res; } final public int getInt() { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, position(), limit(), address); position(position() + 1); return a; } final public ByteBuffer putInt(int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, position(), limit(), value, address); position(position() + 1); return this; } final public int getInt(int index) { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, index, limit(), address); return a; } final public ByteBuffer putInt(int index, int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, index, limit(), value, address); return this; };
  final public LongBuffer asLongBuffer() { LongBuffer res = new MappedLongFileBuffer(ch); res.limit((limit()*1)/8); return res; } final public long getLong() { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, position(), limit(), address); position(position() + 1); return a; } final public ByteBuffer putLong(long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, position(), limit(), value, address); position(position() + 1); return this; } final public long getLong(int index) { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, index, limit(), address); return a; } final public ByteBuffer putLong(int index, long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, index, limit(), value, address); return this; };
  final public FloatBuffer asFloatBuffer() { FloatBuffer res = new MappedFloatFileBuffer(ch); res.limit((limit()*1)/4); return res; } final public float getFloat() { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, position(), limit(), address); position(position() + 1); return a; } final public ByteBuffer putFloat(float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, position(), limit(), value, address); position(position() + 1); return this; } final public float getFloat(int index) { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, index, limit(), address); return a; } final public ByteBuffer putFloat(int index, float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, index, limit(), value, address); return this; };
  final public DoubleBuffer asDoubleBuffer() { DoubleBuffer res = new MappedDoubleFileBuffer(ch); res.limit((limit()*1)/8); return res; } final public double getDouble() { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, position(), limit(), address); position(position() + 1); return a; } final public ByteBuffer putDouble(double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, position(), limit(), value, address); position(position() + 1); return this; } final public double getDouble(int index) { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, index, limit(), address); return a; } final public ByteBuffer putDouble(int index, double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, index, limit(), value, address); return this; };
}
