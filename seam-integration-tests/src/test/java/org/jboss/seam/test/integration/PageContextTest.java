package org.jboss.seam.test.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Pete Muir
 *
 */
@RunWith(Arquillian.class)
public class PageContextTest extends JUnitSeamTest
{
   @Deployment(name="PageContextTest")
   @OverProtocol("Servlet 3.0") 
   public static Archive<?> createDeployment()
   {
      return Deployments.defaultSeamDeployment();
   }

   @Test
   public void pageContextTest() throws Exception {

      new FacesRequest("/index.xhtml") {
          
         @Override
         protected void invokeApplication() throws Exception
         {
            Contexts.getPageContext().set("foo", "bar");
            assert Contexts.getPageContext().get("foo") == null;
         }
         
         @Override
         protected void renderResponse() throws Exception
         {
             assert Contexts.getPageContext().get("foo") != null;
             assert "bar".equals(Contexts.getPageContext().get("foo"));
         }
      }.run();
      
  } 
   
}
