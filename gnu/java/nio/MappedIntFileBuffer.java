package manta.runtime;
import java.nio.*;
final public class MappedIntFileBuffer
 extends IntBuffer
{
  boolean ro;
  boolean direct;
  FileChannelImpl ch;
  MappedIntFileBuffer(FileChannelImpl ch)
  {
    this.ch = ch;
  }
  MappedIntFileBuffer(MappedIntFileBuffer b)
  {
    this.ro = b.ro;
    this.ch = b.ch;
  }
  boolean isReadOnly()
  {
    return ro;
  }
public int get()
  {
    int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, pos);
    pos += 4;
    return a;
  }
public IntBuffer put(int b)
  {
    MappedByteFileBuffer.nio_write_Int_file_channel(ch, pos, b);
    pos += 4;
    return this;
  }
public int get(int index)
  {
    int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, index);
    return a;
  }
public IntBuffer put(int index, int b)
  {
    MappedByteFileBuffer.nio_write_Int_file_channel(ch, index, b);
    return this;
  }
public IntBuffer compact()
  {
    return this;
  }
public boolean isDirect()
  {
    return direct;
  }
public IntBuffer slice()
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
  public ByteBuffer asByteBuffer() { return new MappedByteFileBuffer(ch); } public byte getByte() { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, pos); pos += 4; return a; } public IntBuffer putByte(byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, pos, value); pos += 4; return this; } public byte getByte(int index) { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, index); return a; } public IntBuffer putByte(int index, byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, index, value); return this; };
  public CharBuffer asCharBuffer() { return new MappedCharFileBuffer(ch); } public char getChar() { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, pos); pos += 4; return a; } public IntBuffer putChar(char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, pos, value); pos += 4; return this; } public char getChar(int index) { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, index); return a; } public IntBuffer putChar(int index, char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, index, value); return this; };
  public ShortBuffer asShortBuffer() { return new MappedShortFileBuffer(ch); } public short getShort() { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, pos); pos += 4; return a; } public IntBuffer putShort(short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, pos, value); pos += 4; return this; } public short getShort(int index) { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, index); return a; } public IntBuffer putShort(int index, short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, index, value); return this; };
  public IntBuffer asIntBuffer() { return new MappedIntFileBuffer(ch); } public int getInt() { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, pos); pos += 4; return a; } public IntBuffer putInt(int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, pos, value); pos += 4; return this; } public int getInt(int index) { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, index); return a; } public IntBuffer putInt(int index, int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, index, value); return this; };
  public LongBuffer asLongBuffer() { return new MappedLongFileBuffer(ch); } public long getLong() { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, pos); pos += 4; return a; } public IntBuffer putLong(long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, pos, value); pos += 4; return this; } public long getLong(int index) { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, index); return a; } public IntBuffer putLong(int index, long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, index, value); return this; };
  public FloatBuffer asFloatBuffer() { return new MappedFloatFileBuffer(ch); } public float getFloat() { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, pos); pos += 4; return a; } public IntBuffer putFloat(float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, pos, value); pos += 4; return this; } public float getFloat(int index) { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, index); return a; } public IntBuffer putFloat(int index, float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, index, value); return this; };
  public DoubleBuffer asDoubleBuffer() { return new MappedDoubleFileBuffer(ch); } public double getDouble() { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, pos); pos += 4; return a; } public IntBuffer putDouble(double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, pos, value); pos += 4; return this; } public double getDouble(int index) { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, index); return a; } public IntBuffer putDouble(int index, double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, index, value); return this; };
}
