package manta.runtime;
import java.nio.*;
final public class MappedCharFileBuffer
 extends CharBuffer
{
  boolean ro;
  boolean direct;
  FileChannelImpl ch;
  MappedCharFileBuffer(FileChannelImpl ch)
  {
    this.ch = ch;
  }
  MappedCharFileBuffer(MappedCharFileBuffer b)
  {
    this.ro = b.ro;
    this.ch = b.ch;
  }
  boolean isReadOnly()
  {
    return ro;
  }
public char get()
  {
    char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, pos);
    pos += 2;
    return a;
  }
public CharBuffer put(char b)
  {
    MappedByteFileBuffer.nio_write_Char_file_channel(ch, pos, b);
    pos += 2;
    return this;
  }
public char get(int index)
  {
    char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, index);
    return a;
  }
public CharBuffer put(int index, char b)
  {
    MappedByteFileBuffer.nio_write_Char_file_channel(ch, index, b);
    return this;
  }
public CharBuffer compact()
  {
    return this;
  }
public boolean isDirect()
  {
    return direct;
  }
public CharBuffer slice()
  {
    MappedCharFileBuffer A = new MappedCharFileBuffer(this);
    return A;
  }
public CharBuffer duplicate()
  {
    return new MappedCharFileBuffer(this);
  }
public CharBuffer asReadOnlyBuffer()
  {
    MappedCharFileBuffer b = new MappedCharFileBuffer(this);
    b.ro = true;
    return b;
  }
  public ByteBuffer asByteBuffer() { return new MappedByteFileBuffer(ch); } public byte getByte() { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, pos); pos += 2; return a; } public CharBuffer putByte(byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, pos, value); pos += 2; return this; } public byte getByte(int index) { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, index); return a; } public CharBuffer putByte(int index, byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, index, value); return this; };
  public CharBuffer asCharBuffer() { return new MappedCharFileBuffer(ch); } public char getChar() { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, pos); pos += 2; return a; } public CharBuffer putChar(char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, pos, value); pos += 2; return this; } public char getChar(int index) { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, index); return a; } public CharBuffer putChar(int index, char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, index, value); return this; };
  public ShortBuffer asShortBuffer() { return new MappedShortFileBuffer(ch); } public short getShort() { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, pos); pos += 2; return a; } public CharBuffer putShort(short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, pos, value); pos += 2; return this; } public short getShort(int index) { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, index); return a; } public CharBuffer putShort(int index, short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, index, value); return this; };
  public IntBuffer asIntBuffer() { return new MappedIntFileBuffer(ch); } public int getInt() { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, pos); pos += 2; return a; } public CharBuffer putInt(int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, pos, value); pos += 2; return this; } public int getInt(int index) { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, index); return a; } public CharBuffer putInt(int index, int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, index, value); return this; };
  public LongBuffer asLongBuffer() { return new MappedLongFileBuffer(ch); } public long getLong() { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, pos); pos += 2; return a; } public CharBuffer putLong(long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, pos, value); pos += 2; return this; } public long getLong(int index) { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, index); return a; } public CharBuffer putLong(int index, long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, index, value); return this; };
  public FloatBuffer asFloatBuffer() { return new MappedFloatFileBuffer(ch); } public float getFloat() { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, pos); pos += 2; return a; } public CharBuffer putFloat(float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, pos, value); pos += 2; return this; } public float getFloat(int index) { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, index); return a; } public CharBuffer putFloat(int index, float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, index, value); return this; };
  public DoubleBuffer asDoubleBuffer() { return new MappedDoubleFileBuffer(ch); } public double getDouble() { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, pos); pos += 2; return a; } public CharBuffer putDouble(double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, pos, value); pos += 2; return this; } public double getDouble(int index) { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, index); return a; } public CharBuffer putDouble(int index, double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, index, value); return this; };
}
