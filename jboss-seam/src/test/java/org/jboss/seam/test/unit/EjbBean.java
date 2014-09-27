//$Id: EjbBean.java 6435 2007-10-08 18:15:49Z pmuir $
package org.jboss.seam.test.unit;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.JndiName;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Stateful
@Name("ejb")
@Scope(ScopeType.EVENT)
@JndiName("x")
public class EjbBean implements Ejb
{
   public void foo() {}
   @Remove @Destroy
   public void destroy() {}
}
