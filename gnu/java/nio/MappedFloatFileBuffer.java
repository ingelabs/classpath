package manta.runtime;
import java.nio.*;
final public class MappedFloatFileBuffer
 extends FloatBuffer
{
  boolean ro;
  boolean direct;
  FileChannelImpl ch;
  MappedFloatFileBuffer(FileChannelImpl ch)
  {
    this.ch = ch;
  }
  MappedFloatFileBuffer(MappedFloatFileBuffer b)
  {
    this.ro = b.ro;
    this.ch = b.ch;
  }
  boolean isReadOnly()
  {
    return ro;
  }
public float get()
  {
    float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, pos);
    pos += 4;
    return a;
  }
public FloatBuffer put(float b)
  {
    MappedByteFileBuffer.nio_write_Float_file_channel(ch, pos, b);
    pos += 4;
    return this;
  }
public float get(int index)
  {
    float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, index);
    return a;
  }
public FloatBuffer put(int index, float b)
  {
    MappedByteFileBuffer.nio_write_Float_file_channel(ch, index, b);
    return this;
  }
public FloatBuffer compact()
  {
    return this;
  }
public boolean isDirect()
  {
    return direct;
  }
public FloatBuffer slice()
  {
    MappedFloatFileBuffer A = new MappedFloatFileBuffer(this);
    return A;
  }
public FloatBuffer duplicate()
  {
    return new MappedFloatFileBuffer(this);
  }
public FloatBuffer asReadOnlyBuffer()
  {
    MappedFloatFileBuffer b = new MappedFloatFileBuffer(this);
    b.ro = true;
    return b;
  }
  public ByteBuffer asByteBuffer() { return new MappedByteFileBuffer(ch); } public byte getByte() { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, pos); pos += 4; return a; } public FloatBuffer putByte(byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, pos, value); pos += 4; return this; } public byte getByte(int index) { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, index); return a; } public FloatBuffer putByte(int index, byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, index, value); return this; };
  public CharBuffer asCharBuffer() { return new MappedCharFileBuffer(ch); } public char getChar() { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, pos); pos += 4; return a; } public FloatBuffer putChar(char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, pos, value); pos += 4; return this; } public char getChar(int index) { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, index); return a; } public FloatBuffer putChar(int index, char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, index, value); return this; };
  public ShortBuffer asShortBuffer() { return new MappedShortFileBuffer(ch); } public short getShort() { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, pos); pos += 4; return a; } public FloatBuffer putShort(short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, pos, value); pos += 4; return this; } public short getShort(int index) { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, index); return a; } public FloatBuffer putShort(int index, short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, index, value); return this; };
  public IntBuffer asIntBuffer() { return new MappedIntFileBuffer(ch); } public int getInt() { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, pos); pos += 4; return a; } public FloatBuffer putInt(int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, pos, value); pos += 4; return this; } public int getInt(int index) { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, index); return a; } public FloatBuffer putInt(int index, int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, index, value); return this; };
  public LongBuffer asLongBuffer() { return new MappedLongFileBuffer(ch); } public long getLong() { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, pos); pos += 4; return a; } public FloatBuffer putLong(long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, pos, value); pos += 4; return this; } public long getLong(int index) { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, index); return a; } public FloatBuffer putLong(int index, long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, index, value); return this; };
  public FloatBuffer asFloatBuffer() { return new MappedFloatFileBuffer(ch); } public float getFloat() { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, pos); pos += 4; return a; } public FloatBuffer putFloat(float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, pos, value); pos += 4; return this; } public float getFloat(int index) { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, index); return a; } public FloatBuffer putFloat(int index, float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, index, value); return this; };
  public DoubleBuffer asDoubleBuffer() { return new MappedDoubleFileBuffer(ch); } public double getDouble() { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, pos); pos += 4; return a; } public FloatBuffer putDouble(double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, pos, value); pos += 4; return this; } public double getDouble(int index) { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, index); return a; } public FloatBuffer putDouble(int index, double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, index, value); return this; };
}
