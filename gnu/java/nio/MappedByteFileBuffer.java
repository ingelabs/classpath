package manta.runtime;
import java.nio.*;
final public class MappedByteFileBuffer
 extends MappedByteBuffer
{
  boolean ro;
  boolean direct;
  FileChannelImpl ch;
  MappedByteFileBuffer(FileChannelImpl ch)
  {
    this.ch = ch;
  }
  MappedByteFileBuffer(MappedByteFileBuffer b)
  {
    this.ro = b.ro;
    this.ch = b.ch;
  }
  boolean isReadOnly()
  {
    return ro;
  }
  static MantaNative byte nio_read_Byte_file_channel(FileChannelImpl ch, int index); static MantaNative void nio_write_Byte_file_channel(FileChannelImpl ch, int index, byte value);
  static MantaNative short nio_read_Short_file_channel(FileChannelImpl ch, int index); static MantaNative void nio_write_Short_file_channel(FileChannelImpl ch, int index, short value);
  static MantaNative char nio_read_Char_file_channel(FileChannelImpl ch, int index); static MantaNative void nio_write_Char_file_channel(FileChannelImpl ch, int index, char value);
  static MantaNative int nio_read_Int_file_channel(FileChannelImpl ch, int index); static MantaNative void nio_write_Int_file_channel(FileChannelImpl ch, int index, int value);
  static MantaNative long nio_read_Long_file_channel(FileChannelImpl ch, int index); static MantaNative void nio_write_Long_file_channel(FileChannelImpl ch, int index, long value);
  static MantaNative float nio_read_Float_file_channel(FileChannelImpl ch, int index); static MantaNative void nio_write_Float_file_channel(FileChannelImpl ch, int index, float value);
  static MantaNative double nio_read_Double_file_channel(FileChannelImpl ch, int index); static MantaNative void nio_write_Double_file_channel(FileChannelImpl ch, int index, double value);
public byte get()
  {
    byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, pos);
    pos += 1;
    return a;
  }
public ByteBuffer put(byte b)
  {
    MappedByteFileBuffer.nio_write_Byte_file_channel(ch, pos, b);
    pos += 1;
    return this;
  }
public byte get(int index)
  {
    byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, index);
    return a;
  }
public ByteBuffer put(int index, byte b)
  {
    MappedByteFileBuffer.nio_write_Byte_file_channel(ch, index, b);
    return this;
  }
public ByteBuffer compact()
  {
    return this;
  }
public boolean isDirect()
  {
    return direct;
  }
public ByteBuffer slice()
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
  public ByteBuffer asByteBuffer() { return new MappedByteFileBuffer(ch); } public byte getByte() { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, pos); pos += 1; return a; } public ByteBuffer putByte(byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, pos, value); pos += 1; return this; } public byte getByte(int index) { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, index); return a; } public ByteBuffer putByte(int index, byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, index, value); return this; };
  public CharBuffer asCharBuffer() { return new MappedCharFileBuffer(ch); } public char getChar() { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, pos); pos += 1; return a; } public ByteBuffer putChar(char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, pos, value); pos += 1; return this; } public char getChar(int index) { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, index); return a; } public ByteBuffer putChar(int index, char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, index, value); return this; };
  public ShortBuffer asShortBuffer() { return new MappedShortFileBuffer(ch); } public short getShort() { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, pos); pos += 1; return a; } public ByteBuffer putShort(short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, pos, value); pos += 1; return this; } public short getShort(int index) { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, index); return a; } public ByteBuffer putShort(int index, short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, index, value); return this; };
  public IntBuffer asIntBuffer() { return new MappedIntFileBuffer(ch); } public int getInt() { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, pos); pos += 1; return a; } public ByteBuffer putInt(int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, pos, value); pos += 1; return this; } public int getInt(int index) { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, index); return a; } public ByteBuffer putInt(int index, int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, index, value); return this; };
  public LongBuffer asLongBuffer() { return new MappedLongFileBuffer(ch); } public long getLong() { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, pos); pos += 1; return a; } public ByteBuffer putLong(long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, pos, value); pos += 1; return this; } public long getLong(int index) { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, index); return a; } public ByteBuffer putLong(int index, long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, index, value); return this; };
  public FloatBuffer asFloatBuffer() { return new MappedFloatFileBuffer(ch); } public float getFloat() { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, pos); pos += 1; return a; } public ByteBuffer putFloat(float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, pos, value); pos += 1; return this; } public float getFloat(int index) { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, index); return a; } public ByteBuffer putFloat(int index, float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, index, value); return this; };
  public DoubleBuffer asDoubleBuffer() { return new MappedDoubleFileBuffer(ch); } public double getDouble() { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, pos); pos += 1; return a; } public ByteBuffer putDouble(double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, pos, value); pos += 1; return this; } public double getDouble(int index) { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, index); return a; } public ByteBuffer putDouble(int index, double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, index, value); return this; };
}
