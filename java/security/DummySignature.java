package java.security;

final class DummySignature extends Signature
{
private SignatureSpi sigSpi = null;

public DummySignature( SignatureSpi sigSpi, String algorithm )
{
	super( algorithm );
	this.sigSpi = sigSpi;
}

protected void engineInitVerify(PublicKey publicKey)
                                  throws InvalidKeyException
{
	sigSpi.engineInitVerify( publicKey);
}

protected void engineInitSign(PrivateKey privateKey)
                                throws InvalidKeyException
{
	sigSpi.engineInitSign( privateKey);
}

protected void engineUpdate(byte b)
                              throws SignatureException
{
	sigSpi.engineUpdate(b );
}

protected void engineUpdate(byte[] b, int off, int len)
                              throws SignatureException
{
	sigSpi.engineUpdate(b, off,  len);
}

protected byte[] engineSign() throws SignatureException
{
	return sigSpi.engineSign();
}

protected boolean engineVerify(byte[] sigBytes)
                                 throws SignatureException
{
	return sigSpi.engineVerify( sigBytes);
}

protected void engineSetParameter(String param, Object value)
                                    throws InvalidParameterException
{
	sigSpi.engineSetParameter( param, value);
}

protected Object engineGetParameter(String param)
                                      throws InvalidParameterException
{
	return sigSpi.engineGetParameter( param);
}

}
