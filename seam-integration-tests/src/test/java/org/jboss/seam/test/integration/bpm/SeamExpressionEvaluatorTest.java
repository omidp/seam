package org.jboss.seam.test.integration.bpm;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.jbpm.jpdl.el.ELException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Pete Muir
 *
 */
//@Ignore
@RunWith(Arquillian.class)
public class SeamExpressionEvaluatorTest extends JUnitSeamTest
{
   @Deployment(name="SeamExpressionEvaluatorTest")
   @OverProtocol("Servlet 3.0") 
   public static Archive<?> createDeployment()
   {
      return Deployments.jbpmSeamDeployment()
    		  .addClasses(SeamExpressionEvaluatorTestController.class);
   }

   // Test for JBSEAM-1937
   @Test
   public void testValueExpression() throws Exception
   {
      new FacesRequest()
      {

         @Override
         protected void invokeApplication() throws Exception
         {
            invokeAction("#{seamExpressionEvaluatorTestController.createProcess2}");
         }
          
      }.run();
   }
   
   // Test for JBSEAM-3250
   @Test
   public void testUnqualifiedValueExpression() throws Exception
   {
      new FacesRequest()
      {

         @Override
         protected void invokeApplication() throws Exception
         {
            invokeAction("#{seamExpressionEvaluatorTestController.createProcess4}");
         }
          
      }.run();
   }
   
   // Test for JBSEAM-2152
   @Test
   public void testMissingMethod() throws Exception
   {
      new FacesRequest()
      {

         @Override
         protected void invokeApplication() throws Exception
         {
            try
            {
               invokeAction("#{seamExpressionEvaluatorTestController.createProcess3}");
            }
            catch (Exception e)
            {
               if (!(isRootCause(e, ELException.class) || isRootCause(e, javax.el.ELException.class)))
               {
                  e.printStackTrace();
                  assert false;
               }
            }
         }
          
      }.run();
   }
   
   private static boolean isRootCause(Throwable t, Class clazz)
   {
      for (Throwable cause = t.getCause(); cause != null && cause != cause.getCause(); cause = cause.getCause())
      {
         if (clazz.isAssignableFrom(cause.getClass()))
         {
            return true;
         }
      }
      return false;
   }
   
}
