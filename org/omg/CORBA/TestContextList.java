

package org.omg.CORBA;

import junit.framework.TestCase;

public class TestContextList
  extends TestCase
{
  ORB orb = ORB.init();

  public void testCollections()
                       throws Exception
  {
    ContextList c = orb.create_context_list();

    c.add("a");
    c.add("b");
    c.add("c");
    c.add("d");
    c.remove(1);

    StringBuffer b = new StringBuffer();

    for (int i = 0; i < c.count(); i++)
      {
        b.append(c.item(i));
      }

    assertEquals(b.toString(), "acd");
  }
  
  public void testExceptionList()
                       throws Exception
  {
    ExceptionList c = orb.create_exception_list();

    c.add(orb.create_exception_tc("a","b", new StructMember[0]));
    
    assertEquals(c.count(), 1);
    
    assertEquals(c.item(0).id(), "a");
    assertEquals(c.item(0).name(), "b");
    
    c.remove(0);
    
    assertEquals(c.count(), 0);
  }
  
}
