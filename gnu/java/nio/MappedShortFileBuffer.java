package gnu.java.nio;
import java.nio.*;
final public class MappedShortFileBuffer
 extends ShortBuffer
{
  public long address;
  boolean ro;
  boolean direct;
  public FileChannelImpl ch;
  public MappedShortFileBuffer(FileChannelImpl ch)
  {
    this.ch = ch;
    address = ch.address;
  }
  public MappedShortFileBuffer(MappedShortFileBuffer b)
  {
    this.ro = b.ro;
    this.ch = b.ch;
    address = b.address;
  }
  public boolean isReadOnly()
  {
    return ro;
  }
final public short get()
  {
    short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, position());
    position(position() + 2);
    return a;
  }
final public ShortBuffer put(short b)
  {
    MappedByteFileBuffer.nio_write_Short_file_channel(ch, position(), b);
    position(position() + 2);
    return this;
  }
final public short get(int index)
  {
    short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, index);
    return a;
  }
final public ShortBuffer put(int index, short b)
  {
    MappedByteFileBuffer.nio_write_Short_file_channel(ch, index, b);
    return this;
  }
final public ShortBuffer compact()
  {
    return this;
  }
final public boolean isDirect()
  {
    return direct;
  }
final public ShortBuffer slice()
  {
    MappedShortFileBuffer A = new MappedShortFileBuffer(this);
    return A;
  }
public ShortBuffer duplicate()
  {
    return new MappedShortFileBuffer(this);
  }
public ShortBuffer asReadOnlyBuffer()
  {
    MappedShortFileBuffer b = new MappedShortFileBuffer(this);
    b.ro = true;
    return b;
  }
  final public ByteBuffer asByteBuffer() { return new MappedByteFileBuffer(ch); } final public byte getByte() { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, position()); position(position() + 2); return a; } final public ShortBuffer putByte(byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, position(), value); position(position() + 2); return this; } final public byte getByte(int index) { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, index); return a; } final public ShortBuffer putByte(int index, byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, index, value); return this; };
  final public CharBuffer asCharBuffer() { return new MappedCharFileBuffer(ch); } final public char getChar() { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, position()); position(position() + 2); return a; } final public ShortBuffer putChar(char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, position(), value); position(position() + 2); return this; } final public char getChar(int index) { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, index); return a; } final public ShortBuffer putChar(int index, char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, index, value); return this; };
  final public ShortBuffer asShortBuffer() { return new MappedShortFileBuffer(ch); } final public short getShort() { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, position()); position(position() + 2); return a; } final public ShortBuffer putShort(short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, position(), value); position(position() + 2); return this; } final public short getShort(int index) { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, index); return a; } final public ShortBuffer putShort(int index, short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, index, value); return this; };
  final public IntBuffer asIntBuffer() { return new MappedIntFileBuffer(ch); } final public int getInt() { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, position()); position(position() + 2); return a; } final public ShortBuffer putInt(int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, position(), value); position(position() + 2); return this; } final public int getInt(int index) { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, index); return a; } final public ShortBuffer putInt(int index, int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, index, value); return this; };
  final public LongBuffer asLongBuffer() { return new MappedLongFileBuffer(ch); } final public long getLong() { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, position()); position(position() + 2); return a; } final public ShortBuffer putLong(long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, position(), value); position(position() + 2); return this; } final public long getLong(int index) { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, index); return a; } final public ShortBuffer putLong(int index, long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, index, value); return this; };
  final public FloatBuffer asFloatBuffer() { return new MappedFloatFileBuffer(ch); } final public float getFloat() { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, position()); position(position() + 2); return a; } final public ShortBuffer putFloat(float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, position(), value); position(position() + 2); return this; } final public float getFloat(int index) { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, index); return a; } final public ShortBuffer putFloat(int index, float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, index, value); return this; };
  final public DoubleBuffer asDoubleBuffer() { return new MappedDoubleFileBuffer(ch); } final public double getDouble() { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, position()); position(position() + 2); return a; } final public ShortBuffer putDouble(double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, position(), value); position(position() + 2); return this; } final public double getDouble(int index) { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, index); return a; } final public ShortBuffer putDouble(int index, double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, index, value); return this; };
}
