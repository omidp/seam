package org.jboss.seam.test.integration.synchronization;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.JndiName;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Stateful
@Scope(ScopeType.SESSION)
@Name("factoryLock.test")
@JndiName("java:global/test/FactoryLockAction")
public class FactoryLockAction implements FactoryLockLocal
{
   public String testOtherFactory() {
      try
      {
         Thread.sleep(500);
      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }
      return (String)Component.getInstance("factoryLock.foo", true);
   }
   
   // gets instance produced by this component's factory 
   public String testSameFactory() {
      try
      {
         Thread.sleep(500);
      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }
      return (String)Component.getInstance("factoryLock.testString", true);
   }
   
   @Factory(value="factoryLock.testString", scope=ScopeType.SESSION)
   public String getTestString() {
      return "testString";
   }
   @Remove
   public void remove() {}
}
