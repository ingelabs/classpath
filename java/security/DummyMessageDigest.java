package java.security;

import java.security.MessageDigest;
import java.security.MessageDigestSpi;
import java.security.DigestException;

final class DummyMessageDigest extends MessageDigest
{
private MessageDigestSpi mdSpi = null;

public DummyMessageDigest( MessageDigestSpi mdSpi, String algorithm )
{
	super( algorithm );
	this.mdSpi = mdSpi;
}

protected void engineUpdate (byte input)
{
	mdSpi.engineUpdate( input );
}

protected void engineUpdate (byte[] input, int offset, int len)
{
	mdSpi.engineUpdate(input, offset, len);
}

protected byte[] engineDigest()
{
	return mdSpi.engineDigest();
}

protected void engineReset()
{
	mdSpi.engineReset();
}

}
