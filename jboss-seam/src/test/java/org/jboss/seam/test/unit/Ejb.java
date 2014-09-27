//$Id: Ejb.java 6435 2007-10-08 18:15:49Z pmuir $
package org.jboss.seam.test.unit;

import javax.ejb.Local;

@Local
public interface Ejb
{
   public void foo();
   public void destroy();
}
