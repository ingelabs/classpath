package gnu.java.nio;
import java.nio.*;
import java.io.IOException;
final public class MappedDoubleFileBuffer
 extends DoubleBuffer
{
  public long address;
  boolean ro;
  boolean direct;
  public FileChannelImpl ch;
  public MappedDoubleFileBuffer(FileChannelImpl ch)
  {
    this.ch = ch;
    address = ch.address;
    try {
      long si = ch.size() / 8;
      limit((int)si);
    } catch (IOException e) {
      System.err.println("failed to get size of file-channel's file");
    }
  }
  public MappedDoubleFileBuffer(MappedDoubleFileBuffer b)
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
final public double get()
  {
    double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, position(), limit(), address);
    position(position() + 8);
    return a;
  }
final public DoubleBuffer put(double b)
  {
    MappedByteFileBuffer.nio_write_Double_file_channel(ch, position(), limit(), b, address);
    position(position() + 8);
    return this;
  }
final public double get(int index)
  {
    double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, index, limit(), address);
    return a;
  }
final public DoubleBuffer put(int index, double b)
  {
    MappedByteFileBuffer.nio_write_Double_file_channel(ch, index, limit(), b, address);
    return this;
  }
final public DoubleBuffer compact()
  {
    return this;
  }
final public boolean isDirect()
  {
    return direct;
  }
final public DoubleBuffer slice()
  {
    MappedDoubleFileBuffer A = new MappedDoubleFileBuffer(this);
    return A;
  }
public DoubleBuffer duplicate()
  {
    return new MappedDoubleFileBuffer(this);
  }
public DoubleBuffer asReadOnlyBuffer()
  {
    MappedDoubleFileBuffer b = new MappedDoubleFileBuffer(this);
    b.ro = true;
    return b;
  }
  final public ByteBuffer asByteBuffer() { ByteBuffer res = new MappedByteFileBuffer(ch); res.limit((limit()*8)/1); return res; } final public byte getByte() { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, position(), limit(), address); position(position() + 8); return a; } final public DoubleBuffer putByte(byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, position(), limit(), value, address); position(position() + 8); return this; } final public byte getByte(int index) { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, index, limit(), address); return a; } final public DoubleBuffer putByte(int index, byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, index, limit(), value, address); return this; };
  final public CharBuffer asCharBuffer() { CharBuffer res = new MappedCharFileBuffer(ch); res.limit((limit()*8)/2); return res; } final public char getChar() { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, position(), limit(), address); position(position() + 8); return a; } final public DoubleBuffer putChar(char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, position(), limit(), value, address); position(position() + 8); return this; } final public char getChar(int index) { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, index, limit(), address); return a; } final public DoubleBuffer putChar(int index, char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, index, limit(), value, address); return this; };
  final public ShortBuffer asShortBuffer() { ShortBuffer res = new MappedShortFileBuffer(ch); res.limit((limit()*8)/2); return res; } final public short getShort() { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, position(), limit(), address); position(position() + 8); return a; } final public DoubleBuffer putShort(short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, position(), limit(), value, address); position(position() + 8); return this; } final public short getShort(int index) { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, index, limit(), address); return a; } final public DoubleBuffer putShort(int index, short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, index, limit(), value, address); return this; };
  final public IntBuffer asIntBuffer() { IntBuffer res = new MappedIntFileBuffer(ch); res.limit((limit()*8)/4); return res; } final public int getInt() { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, position(), limit(), address); position(position() + 8); return a; } final public DoubleBuffer putInt(int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, position(), limit(), value, address); position(position() + 8); return this; } final public int getInt(int index) { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, index, limit(), address); return a; } final public DoubleBuffer putInt(int index, int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, index, limit(), value, address); return this; };
  final public LongBuffer asLongBuffer() { LongBuffer res = new MappedLongFileBuffer(ch); res.limit((limit()*8)/8); return res; } final public long getLong() { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, position(), limit(), address); position(position() + 8); return a; } final public DoubleBuffer putLong(long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, position(), limit(), value, address); position(position() + 8); return this; } final public long getLong(int index) { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, index, limit(), address); return a; } final public DoubleBuffer putLong(int index, long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, index, limit(), value, address); return this; };
  final public FloatBuffer asFloatBuffer() { FloatBuffer res = new MappedFloatFileBuffer(ch); res.limit((limit()*8)/4); return res; } final public float getFloat() { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, position(), limit(), address); position(position() + 8); return a; } final public DoubleBuffer putFloat(float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, position(), limit(), value, address); position(position() + 8); return this; } final public float getFloat(int index) { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, index, limit(), address); return a; } final public DoubleBuffer putFloat(int index, float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, index, limit(), value, address); return this; };
  final public DoubleBuffer asDoubleBuffer() { DoubleBuffer res = new MappedDoubleFileBuffer(ch); res.limit((limit()*8)/8); return res; } final public double getDouble() { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, position(), limit(), address); position(position() + 8); return a; } final public DoubleBuffer putDouble(double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, position(), limit(), value, address); position(position() + 8); return this; } final public double getDouble(int index) { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, index, limit(), address); return a; } final public DoubleBuffer putDouble(int index, double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, index, limit(), value, address); return this; };
}
