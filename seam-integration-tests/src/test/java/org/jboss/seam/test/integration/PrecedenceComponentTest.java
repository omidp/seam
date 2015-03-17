package org.jboss.seam.test.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.Component;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PrecedenceComponentTest 
   extends JUnitSeamTest
{
   @Deployment(name="JavaBeanEqualsTest")
   @OverProtocol("Servlet 3.0") 
   public static Archive<?> createDeployment()
   {
      return Deployments.defaultSeamDeployment("WEB-INF/components-precedence.xml")
            .addClasses(Component1.class, Component2.class);
   }
    
    /**
     * Test if precedence of component is working correctly
     * components.xml specifies component1 with 2 possible
     *  configuration - first has got higher precedence than
     *  second and the first should set component1.name property 
     *  to Componen1High. Result should be that even last component1
     *  is set the higher precedence configuration has to be set and 
     *  remain as the only one available.
     *  JBSEAM-3138
     * @throws Exception
     */
    @Test
    public void testPrecedenceComponents() throws Exception
    {       
       
       new FacesRequest()
       {
          @Override
          protected void invokeApplication() throws Exception {
              Object component = Component.getInstance("component1");
              if (!(component instanceof Component2))              
              {
                 Assert.fail("component is not expected Component2.class");
              }
              Component2 myPrecedenceComponent = (Component2) component;
              Assert.assertEquals("Component1High", myPrecedenceComponent.getName());
              Assert.assertEquals("Component2default", myPrecedenceComponent.getDefaultValue());
          }   
       }.run();
    }
    
}