package org.jboss.seam.test.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Pete Muir
 *
 */
@RunWith(Arquillian.class)
public class JavaBeanEqualsTest extends JUnitSeamTest
{
	@Deployment(name="JavaBeanEqualsTest")
	@OverProtocol("Servlet 3.0") 
	public static Archive<?> createDeployment()
	{
		return Deployments.defaultSeamDeployment()
				.addClasses(BeanA.class);
	}
   
   @Test
   // Test for JBSEAM-1257
   public void testReflexiveEquals() throws Exception
   {
      new ComponentTest()
      {

         @Override
         protected void testComponents() throws Exception
         {
            assert getInstance("beanA").equals(getInstance("beanA"));
            assert getValue("#{beanA.component}").equals(getValue("#{beanA.component}"));
         }
         
      }.run();
   }

}
