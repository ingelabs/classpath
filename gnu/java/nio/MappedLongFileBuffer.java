package gnu.java.nio;
import java.nio.*;
final public class MappedLongFileBuffer
 extends LongBuffer
{
  public long address;
  boolean ro;
  boolean direct;
  public FileChannelImpl ch;
  public MappedLongFileBuffer(FileChannelImpl ch)
  {
    this.ch = ch;
    address = ch.address;
  }
  public MappedLongFileBuffer(MappedLongFileBuffer b)
  {
    this.ro = b.ro;
    this.ch = b.ch;
    address = b.address;
  }
  public boolean isReadOnly()
  {
    return ro;
  }
final public long get()
  {
    long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, position());
    position(position() + 8);
    return a;
  }
final public LongBuffer put(long b)
  {
    MappedByteFileBuffer.nio_write_Long_file_channel(ch, position(), b);
    position(position() + 8);
    return this;
  }
final public long get(int index)
  {
    long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, index);
    return a;
  }
final public LongBuffer put(int index, long b)
  {
    MappedByteFileBuffer.nio_write_Long_file_channel(ch, index, b);
    return this;
  }
final public LongBuffer compact()
  {
    return this;
  }
final public boolean isDirect()
  {
    return direct;
  }
final public LongBuffer slice()
  {
    MappedLongFileBuffer A = new MappedLongFileBuffer(this);
    return A;
  }
public LongBuffer duplicate()
  {
    return new MappedLongFileBuffer(this);
  }
public LongBuffer asReadOnlyBuffer()
  {
    MappedLongFileBuffer b = new MappedLongFileBuffer(this);
    b.ro = true;
    return b;
  }
  final public ByteBuffer asByteBuffer() { return new MappedByteFileBuffer(ch); } final public byte getByte() { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, position()); position(position() + 8); return a; } final public LongBuffer putByte(byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, position(), value); position(position() + 8); return this; } final public byte getByte(int index) { byte a = MappedByteFileBuffer.nio_read_Byte_file_channel(ch, index); return a; } final public LongBuffer putByte(int index, byte value) { MappedByteFileBuffer.nio_write_Byte_file_channel(ch, index, value); return this; };
  final public CharBuffer asCharBuffer() { return new MappedCharFileBuffer(ch); } final public char getChar() { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, position()); position(position() + 8); return a; } final public LongBuffer putChar(char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, position(), value); position(position() + 8); return this; } final public char getChar(int index) { char a = MappedByteFileBuffer.nio_read_Char_file_channel(ch, index); return a; } final public LongBuffer putChar(int index, char value) { MappedByteFileBuffer.nio_write_Char_file_channel(ch, index, value); return this; };
  final public ShortBuffer asShortBuffer() { return new MappedShortFileBuffer(ch); } final public short getShort() { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, position()); position(position() + 8); return a; } final public LongBuffer putShort(short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, position(), value); position(position() + 8); return this; } final public short getShort(int index) { short a = MappedByteFileBuffer.nio_read_Short_file_channel(ch, index); return a; } final public LongBuffer putShort(int index, short value) { MappedByteFileBuffer.nio_write_Short_file_channel(ch, index, value); return this; };
  final public IntBuffer asIntBuffer() { return new MappedIntFileBuffer(ch); } final public int getInt() { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, position()); position(position() + 8); return a; } final public LongBuffer putInt(int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, position(), value); position(position() + 8); return this; } final public int getInt(int index) { int a = MappedByteFileBuffer.nio_read_Int_file_channel(ch, index); return a; } final public LongBuffer putInt(int index, int value) { MappedByteFileBuffer.nio_write_Int_file_channel(ch, index, value); return this; };
  final public LongBuffer asLongBuffer() { return new MappedLongFileBuffer(ch); } final public long getLong() { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, position()); position(position() + 8); return a; } final public LongBuffer putLong(long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, position(), value); position(position() + 8); return this; } final public long getLong(int index) { long a = MappedByteFileBuffer.nio_read_Long_file_channel(ch, index); return a; } final public LongBuffer putLong(int index, long value) { MappedByteFileBuffer.nio_write_Long_file_channel(ch, index, value); return this; };
  final public FloatBuffer asFloatBuffer() { return new MappedFloatFileBuffer(ch); } final public float getFloat() { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, position()); position(position() + 8); return a; } final public LongBuffer putFloat(float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, position(), value); position(position() + 8); return this; } final public float getFloat(int index) { float a = MappedByteFileBuffer.nio_read_Float_file_channel(ch, index); return a; } final public LongBuffer putFloat(int index, float value) { MappedByteFileBuffer.nio_write_Float_file_channel(ch, index, value); return this; };
  final public DoubleBuffer asDoubleBuffer() { return new MappedDoubleFileBuffer(ch); } final public double getDouble() { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, position()); position(position() + 8); return a; } final public LongBuffer putDouble(double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, position(), value); position(position() + 8); return this; } final public double getDouble(int index) { double a = MappedByteFileBuffer.nio_read_Double_file_channel(ch, index); return a; } final public LongBuffer putDouble(int index, double value) { MappedByteFileBuffer.nio_write_Double_file_channel(ch, index, value); return this; };
}
