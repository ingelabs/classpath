package manta.runtime;
import java.nio.*;
final public class MappedDoubleFileBuffer
 extends DoubleBuffer
{
  boolean ro;
  boolean direct;
  FileChannelImpl ch;
  MappedDoubleFileBuffer(FileChannelImpl ch)
  {
    this.ch = ch;
  }
  MappedDoubleFileBuffer(MappedDoubleFileBuffer b)
  {
    this.ro = b.ro;
    this.ch = b.ch;
  }
  boolean isReadOnly()
  {
    return ro;
  }
public double get()
  {
    double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, pos);
    pos += 8;
    return a;
  }
public DoubleBuffer put(double b)
  {
    MappedByteFileBuffer.nio_write_Double_file_channel(ch, pos, b);
    pos += 8;
    return this;
  }
public double get(int index)
  {
    double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, index);
    return a;
  }
public DoubleBuffer put(int index, double b)
  {
    MappedByteFileBuffer.nio_write_Double_file_channel(ch, index, b);
    return this;
  }
public DoubleBuffer compact()
  {
    return this;
  }
public boolean isDirect()
  {
    return direct;
  }
public DoubleBuffer slice()
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
  public ByteBuffer asByteBuffer() { return new MappedByteFileBuffer(ch); } public byte getByte() { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, pos); pos += 8; return a; } public DoubleBuffer putByte(byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, pos, value); pos += 8; return this; } public byte getByte(int index) { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, index); return a; } public DoubleBuffer putByte(int index, byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, index, value); return this; };
  public CharBuffer asCharBuffer() { return new MappedCharFileBuffer(ch); } public char getChar() { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, pos); pos += 8; return a; } public DoubleBuffer putChar(char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, pos, value); pos += 8; return this; } public char getChar(int index) { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, index); return a; } public DoubleBuffer putChar(int index, char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, index, value); return this; };
  public ShortBuffer asShortBuffer() { return new MappedShortFileBuffer(ch); } public short getShort() { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, pos); pos += 8; return a; } public DoubleBuffer putShort(short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, pos, value); pos += 8; return this; } public short getShort(int index) { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, index); return a; } public DoubleBuffer putShort(int index, short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, index, value); return this; };
  public IntBuffer asIntBuffer() { return new MappedIntFileBuffer(ch); } public int getInt() { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, pos); pos += 8; return a; } public DoubleBuffer putInt(int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, pos, value); pos += 8; return this; } public int getInt(int index) { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, index); return a; } public DoubleBuffer putInt(int index, int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, index, value); return this; };
  public LongBuffer asLongBuffer() { return new MappedLongFileBuffer(ch); } public long getLong() { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, pos); pos += 8; return a; } public DoubleBuffer putLong(long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, pos, value); pos += 8; return this; } public long getLong(int index) { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, index); return a; } public DoubleBuffer putLong(int index, long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, index, value); return this; };
  public FloatBuffer asFloatBuffer() { return new MappedFloatFileBuffer(ch); } public float getFloat() { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, pos); pos += 8; return a; } public DoubleBuffer putFloat(float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, pos, value); pos += 8; return this; } public float getFloat(int index) { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, index); return a; } public DoubleBuffer putFloat(int index, float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, index, value); return this; };
  public DoubleBuffer asDoubleBuffer() { return new MappedDoubleFileBuffer(ch); } public double getDouble() { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, pos); pos += 8; return a; } public DoubleBuffer putDouble(double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, pos, value); pos += 8; return this; } public double getDouble(int index) { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, index); return a; } public DoubleBuffer putDouble(int index, double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, index, value); return this; };
}
