package gnu.java.security.provider;

import java.math.BigInteger;
import gnu.java.security.der.DEREncodingException;

public class DERReader
{
byte source[];
int pos;

static final int UNIVERSAL = 1;
static final int APPLICATION = 2;
static final int CONTEXT_SPECIFIC = 3;
static final int PRIVATE = 4;


public DERReader()
{
	source = null;
	pos = 0;
}

public DERReader( byte source[] )
{
	init( source );
}

public void init( String source )
{
	init( source.getBytes() );
}

public void init( byte source[] )
{
	this.source = source;
	pos = 0;
}

public BigInteger getBigInteger() throws DEREncodingException
{
	return new BigInteger( getPrimitive() );
}

//Reads Primitive, definite-length method
private byte[] getPrimitive() throws DEREncodingException
{
	int tmp = pos;
	
	//Read Identifier
	byte identifier = source[tmp++];
	if( (0x20 & identifier) != 0)
		throw new DEREncodingException();
	int type = translateLeadIdentifierByte(identifier);
	//System.out.println("Type: " + type);

	//get tag
	int tag = (0x1f & identifier);
	//if( tag == 0x1f)
	//	tag = getIdentifier(tmp);
	//System.out.println("Tag: " + tag);

	//get length
	byte len = source[tmp]; //may be length of length parameter
	long length =  0x7f & len;
	int i;
	if( (0x80 & len) != 0 ) {
		//System.out.println("Extra Long Length");
		len &= 0x7f;
		//System.out.println("Length of Length: " + len);
		//get length here
		length = 0;
		for( i = 0; i < len; i++ ) {
			tmp++;
			length <<= 8;
			length += (source[tmp] < 0 ) ? 
				(256 + source[tmp]) : 
				source[tmp];
		//System.out.println("Length of Length: " + length);
		}
		tmp++;
	} else
		tmp++;

	/*System.out.println("Position: " + tmp);
	System.out.println("Length: " + length);
	for( i = 0; i < 10; i++)
		System.out.print(source[tmp + i] + " ");
	System.out.println();*/

	byte tmpb[] = new byte[ (int)length ];
	System.arraycopy( source, tmp, tmpb, 0, (int)length);
	pos = (int)(tmp + length);
	return tmpb;	
}

private int translateLeadIdentifierByte(byte b)
{
	if( (0x3f & b ) == b)
		return UNIVERSAL;
	else if( (0x7f & b ) == b)
		return APPLICATION;
	else if( (0xbf & b ) == b)
		return CONTEXT_SPECIFIC;
	else 
		return PRIVATE;
}

private int getIdentifier(int tpos)
{
	while( (0x80 & source[tpos]) != 0)
		tpos++;
	return tpos;
}

}
