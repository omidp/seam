package org.jboss.seam.test.integration.mock;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.seam.test.integration.Action;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.seam.test.integration.Person;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.runner.RunWith;
import org.junit.Test;

public class SeamTestTest extends JUnitSeamTest
{
   private static final String PETER_NAME = "Pete Muir";
   private static final String PETER_USERNAME = "pmuir";
   
   @Test
   public void testEl() throws Exception
   {
      new FacesRequest() 
      {  
         
         @Override
         protected void updateModelValues() throws Exception
         {
            setValue("#{person.name}", PETER_NAME);
         }
         
         @Override
         protected void renderResponse() throws Exception
         {
            assert getValue("#{person.name}").equals(PETER_NAME);
         }
         
         @Override
         protected void invokeApplication() throws Exception
         {
            invokeAction("#{action.go}");
            String result = getOutcome();
            assert "success".equals(result);
         }
      }.run();
   }
   
   @Test
   public void testSeamSecurity() throws Exception
   {
      new FacesRequest() 
      {  
         
         @Override
         protected void updateModelValues() throws Exception
         {
            setValue("#{identity.username}", PETER_USERNAME);
         }
         
         @Override
         protected void renderResponse() throws Exception
         {
            assert getValue("#{identity.username}").equals(PETER_USERNAME);
         }
      }.run();
   }

}
