package gnu.java.nio;
import java.nio.*;
import java.io.IOException;
final public class MappedIntFileBuffer
 extends IntBuffer
{
  public long address;
  boolean ro;
  boolean direct;
  public FileChannelImpl ch;
  public MappedIntFileBuffer(FileChannelImpl ch)
  {
    this.ch = ch;
    address = ch.address;
    try {
      long si = ch.size() / 4;
      limit((int)si);
    } catch (IOException e) {
      System.err.println("failed to get size of file-channel's file");
    }
  }
  public MappedIntFileBuffer(MappedIntFileBuffer b)
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
final public int get()
  {
    int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, position(), limit(), address);
    position(position() + 4);
    return a;
  }
final public IntBuffer put(int b)
  {
    MappedByteFileBuffer.nio_write_Int_file_channel(ch, position(), limit(), b, address);
    position(position() + 4);
    return this;
  }
final public int get(int index)
  {
    int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, index, limit(), address);
    return a;
  }
final public IntBuffer put(int index, int b)
  {
    MappedByteFileBuffer.nio_write_Int_file_channel(ch, index, limit(), b, address);
    return this;
  }
final public IntBuffer compact()
  {
    return this;
  }
final public boolean isDirect()
  {
    return direct;
  }
final public IntBuffer slice()
  {
    MappedIntFileBuffer A = new MappedIntFileBuffer(this);
    return A;
  }
public IntBuffer duplicate()
  {
    return new MappedIntFileBuffer(this);
  }
public IntBuffer asReadOnlyBuffer()
  {
    MappedIntFileBuffer b = new MappedIntFileBuffer(this);
    b.ro = true;
    return b;
  }
  final public ByteBuffer asByteBuffer() { ByteBuffer res = new MappedByteFileBuffer(ch); res.limit((limit()*4)/1); return res; } final public byte getByte() { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, position(), limit(), address); position(position() + 4); return a; } final public IntBuffer putByte(byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, position(), limit(), value, address); position(position() + 4); return this; } final public byte getByte(int index) { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, index, limit(), address); return a; } final public IntBuffer putByte(int index, byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, index, limit(), value, address); return this; };
  final public CharBuffer asCharBuffer() { CharBuffer res = new MappedCharFileBuffer(ch); res.limit((limit()*4)/2); return res; } final public char getChar() { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, position(), limit(), address); position(position() + 4); return a; } final public IntBuffer putChar(char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, position(), limit(), value, address); position(position() + 4); return this; } final public char getChar(int index) { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, index, limit(), address); return a; } final public IntBuffer putChar(int index, char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, index, limit(), value, address); return this; };
  final public ShortBuffer asShortBuffer() { ShortBuffer res = new MappedShortFileBuffer(ch); res.limit((limit()*4)/2); return res; } final public short getShort() { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, position(), limit(), address); position(position() + 4); return a; } final public IntBuffer putShort(short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, position(), limit(), value, address); position(position() + 4); return this; } final public short getShort(int index) { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, index, limit(), address); return a; } final public IntBuffer putShort(int index, short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, index, limit(), value, address); return this; };
  final public IntBuffer asIntBuffer() { IntBuffer res = new MappedIntFileBuffer(ch); res.limit((limit()*4)/4); return res; } final public int getInt() { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, position(), limit(), address); position(position() + 4); return a; } final public IntBuffer putInt(int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, position(), limit(), value, address); position(position() + 4); return this; } final public int getInt(int index) { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, index, limit(), address); return a; } final public IntBuffer putInt(int index, int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, index, limit(), value, address); return this; };
  final public LongBuffer asLongBuffer() { LongBuffer res = new MappedLongFileBuffer(ch); res.limit((limit()*4)/8); return res; } final public long getLong() { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, position(), limit(), address); position(position() + 4); return a; } final public IntBuffer putLong(long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, position(), limit(), value, address); position(position() + 4); return this; } final public long getLong(int index) { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, index, limit(), address); return a; } final public IntBuffer putLong(int index, long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, index, limit(), value, address); return this; };
  final public FloatBuffer asFloatBuffer() { FloatBuffer res = new MappedFloatFileBuffer(ch); res.limit((limit()*4)/4); return res; } final public float getFloat() { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, position(), limit(), address); position(position() + 4); return a; } final public IntBuffer putFloat(float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, position(), limit(), value, address); position(position() + 4); return this; } final public float getFloat(int index) { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, index, limit(), address); return a; } final public IntBuffer putFloat(int index, float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, index, limit(), value, address); return this; };
  final public DoubleBuffer asDoubleBuffer() { DoubleBuffer res = new MappedDoubleFileBuffer(ch); res.limit((limit()*4)/8); return res; } final public double getDouble() { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, position(), limit(), address); position(position() + 4); return a; } final public IntBuffer putDouble(double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, position(), limit(), value, address); position(position() + 4); return this; } final public double getDouble(int index) { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, index, limit(), address); return a; } final public IntBuffer putDouble(int index, double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, index, limit(), value, address); return this; };
}
