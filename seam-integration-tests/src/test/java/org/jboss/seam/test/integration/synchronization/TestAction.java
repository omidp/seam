package org.jboss.seam.test.integration.synchronization;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.JndiName;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;

@Stateful
@Scope(ScopeType.SESSION)
@Name("test")
@JndiName("java:global/test/TestAction")
@Synchronized(timeout=10000)
public class TestAction implements TestLocal
{
   public String test1() {
      try
      {
         Thread.sleep(100);
      }
      
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }
      return "test1";
   }
   
   public String test2() {
      try
      {
         Thread.sleep(100);
      }
      
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }
      return "test2";
   }
   
   @Remove
   public void remove() {}
}
