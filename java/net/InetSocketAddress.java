package java.net;

/** 
 * InetSocketAddress instances represent socket addresses
 * in the java.nio package. They encapsulate a InetAddress and
 * a port number.
 */

public class InetSocketAddress extends SocketAddress
{
    InetAddress addr;
    int port;
    
    public InetSocketAddress(InetAddress addr, int port)
    {
	this.addr = addr;
	this.port = port;
    }

    public InetSocketAddress(int port)
    {
	this.port = port;
	try {
	    this.addr = InetAddress.getLocalHost();
	} catch (Exception e) {
	}
    }


    public InetSocketAddress(String hostname, int port)
    {
	this.port = port;
	try {
	    this.addr = InetAddress.getByName(hostname);
	} catch (Exception e) {
	}
    }
 
    /** 
     * Test if obj is a InetSocketAddress and
     * has the same address & port
     */
    public boolean equals(Object obj)
    {
	if (obj instanceof InetSocketAddress)
	    {
		InetSocketAddress a = (InetSocketAddress) obj;
		return addr.equals(a.addr) && a.port == port;
	    }
	return false;
    }

    public InetAddress getAddress()
    {
	return addr;
    }

    public String getHostName()
    {
	return addr.getHostName();
    }

    public int getPort()
    {
	return port;
    }
    
    /**
     * TODO: see what sun does here.
     */
    public int hashCode()
    {
	return port + addr.hashCode();
    }

    /**
     * TODO: see what sun does here.
     */
    public boolean isUnresolved()
    {
	return addr == null;
    }
    
    /**
     * TODO: see what sun does here.
     */
    public String toString()
    {
	return "SA:"+addr+":"+port;
    }
}
