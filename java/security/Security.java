
package java.security;

// This is just intended to get the ball rolling.

public final class Security
{
  public static int addProvider (Provider p) 
  {
    throw new Error ("Unimplemented.");
  }

  /**
     @deprecated
   */
  public static String getAlgorithmProperty (String alg, String prop)
  {
    throw new Error ("Unimplemented.");
  }
  
  public static String getProperty (String prop)
  {
    throw new Error ("Unimplemented.");
  }
  
  public static Provider getProvider (String p)
  {
    return provider;
  }
  
  public static Provider[] getProviders ()
  {
    return new Provider [] {provider};
  }
  
  public static int insertProviderAt (Provider p, int i)
  {
    throw new Error ("Unimplemented.");
  }
  
  public static void removeProvider (String p)
  {
    throw new Error ("Unimplemented.");
  }
  
  public static void setProperty (String key, String datum)
  {
    throw new Error ("Unimplemented.");
  }

  private static Provider provider = new GNUProvider ();
}

class GNUProvider extends Provider
{
  GNUProvider ()
  {
    super ("GNU", 0.0, "???");
  }

  public String getProperty (String name)
  {
    return name.startsWith ("MessageDigest.")
      ? "gnu.java.security.provider." + name.substring (14)
      : null;
  }
}
