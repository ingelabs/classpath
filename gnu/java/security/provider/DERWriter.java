package gnu.java.security.provider;

import java.math.BigInteger;

public class DERWriter
{

static final int UNIVERSAL = 1;
static final int APPLICATION = 2;
static final int CONTEXT_SPECIFIC = 3;
static final int PRIVATE = 4;

public DERWriter()
{}

public byte[] writeBigInteger( BigInteger i)
{
	return writePrimitive( 0x02, UNIVERSAL, (int)Math.ceil((double)i.bitLength() / 8), i.toByteArray() );
}

private byte[] writePrimitive( int identifier, int identifierencoding,
	 int length, byte contents[])
{
	return joinarrays( generateIdentifier( identifier, identifierencoding ), generateLength( length ), contents);
}

public byte[] joinarrays( byte a[], byte b[])
{
	byte d[] = new byte[ a.length + b.length];
	System.arraycopy( a, 0, d, 0, a.length);
	System.arraycopy( b, 0, d, a.length, b.length);
	return d;
}

public byte[] joinarrays( byte a[], byte b[], byte c[])
{
	byte d[] = new byte[ a.length + b.length + c.length];
	System.arraycopy( a, 0, d, 0, a.length);
	System.arraycopy( b, 0, d, a.length, b.length);
	System.arraycopy( c, 0, d, a.length + b.length, c.length);
	return d;
}

private byte[] generateIdentifier(int identifier, 
	int identifierencoding)
{
	byte b[];
	if( identifier > 31 ) {
		int count = (int)(Math.log( identifier ) / Math.log( 256 ));
		b = new byte[ count + 1 ];
		b[0] = (byte)(translateLeadIdentifierByte(identifierencoding) 
			| 0x1f);
		int i;
		for( i = 1; i < (count + 1); i++) {
			b[i] = (byte)(0x7f & ( identifier >> (7 * (count - i)) ));
			b[i] |= 0x80;
		}
		b[i - 1] ^= 0x80;
		//System.out.println("Identifier1: " + b[0]);
		return b;
	} else {
		b = new byte[1];
		b[0] = (byte)((translateLeadIdentifierByte(identifierencoding)
		| (byte)( identifier & 0x1f )) & 0xdf);
		//System.out.println("Identifier2: " + b[0]);
		return b;
	}
}

private byte translateLeadIdentifierByte(int b)
{
	if( b == UNIVERSAL)
		return (byte)0x3f;
	else if( b == APPLICATION)
		return (byte)0x7f;
	else if( b == CONTEXT_SPECIFIC)
		return (byte)0xbf;
	else 
		return (byte)0xC0;
}

private byte[] generateLength( int length )
{
	byte b[];
	if( length > 127 ) {
		int count = (int)Math.ceil(Math.log( length ) / Math.log( 256 ));
		//System.out.println("Length byte count: " + count);
		b = new byte[ count + 1 ];
		b[0] = (byte)((count & 0x7f) | 0x80);
		for( int i = 1; i < (count + 1); i++) {
			b[i] = (byte)( length >>> (8 * ( count - i) ));
			//System.out.println("Length1 byte1: " + (length >>> (8 * ( count - i) )));
			//System.out.println("Length1 byte2: " + b[i]);
		}

		//System.out.println("Length1: " + length);
		return b;
	} else {
		b = new byte[1];
		b[0] = (byte)( length & 0x7f );
		//System.out.println("Length2: " + length);
		return b;
	}
}
}
