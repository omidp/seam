package org.jboss.seam.test.integration.synchronization;

import javax.ejb.Local;

@Local
public interface FactoryLockLocal
{
   public String getTestString();
   public String testOtherFactory();
   public String testSameFactory();
   public void remove();
}