package java.security;

final class DummyKeyPairGenerator extends KeyPairGenerator
{
private KeyPairGeneratorSpi kpgSpi = null;

public DummyKeyPairGenerator( KeyPairGeneratorSpi kpgSpi, String algorithm )
{
	super( algorithm );
	this.kpgSpi = kpgSpi;
}

public void initialize(int keysize, SecureRandom random)
{
	kpgSpi.initialize( keysize, random );
}

public KeyPair generateKeyPair()
{
	return kpgSpi.generateKeyPair();
}

}
