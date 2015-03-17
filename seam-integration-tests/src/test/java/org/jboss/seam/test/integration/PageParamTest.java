package org.jboss.seam.test.integration;

import java.util.List;

import javax.faces.application.FacesMessage;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Pete Muir
 * @author Dan Allen
 */
@RunWith(Arquillian.class)
public class PageParamTest extends JUnitSeamTest
{
   @Deployment(name="PageParamTest")
   @OverProtocol("Servlet 3.0") 
   public static Archive<?> createDeployment()
   {
      return Deployments.defaultSeamDeployment()
    		  .addClasses(Person.class);
   }

   @Test
   public void testPageParameter() throws Exception
   {
      new FacesRequest("/pageWithParameter.xhtml")
      {
         @Override
         protected void beforeRequest()
         {
            setParameter("personName", "pete");
         }
         
         @Override
         protected void invokeApplication() throws Exception
         {
            assert "pete".equals(getValue("#{person.name}"));
         }
      }.run();
      
      new FacesRequest("/pageWithParameter.xhtml")
      {
         @Override
         protected void beforeRequest()
         {
            setParameter("anotherPersonName", "pete");
         }
         
         @Override
         protected void invokeApplication() throws Exception
         {
            assert getValue("#{person.name}") == null;
         }
      }.run();
   }

   @Test
   public void testPageParameterFailsModelValidation() throws Exception
   {
      new FacesRequest("/pageWithParameter.xhtml")
      {
         @Override
         protected void beforeRequest()
         {
            setParameter("personName", "pe");
         }

         @Override
         protected void invokeApplication() throws Exception
         {
            List<FacesMessage> messages = (List<FacesMessage>) getValue("#{facesMessages.currentMessages}");
            assert messages.size() == 1;
            assert messages.get(0).getDetail().startsWith("'personName' parameter is invalid");
            assert getValue("#{person.name}") == null;
         }
      }.run();
   }

   @Test
   public void testPageParameterModelValidationDisabled() throws Exception
   {
      new FacesRequest("/pageWithValidateModelDisabledParameter.xhtml")
      {
         @Override
         protected void beforeRequest()
         {
            setParameter("personName", "pe");
         }

         @Override
         protected void invokeApplication() throws Exception
         {
            assert "pe".equals(getValue("#{person.name}"));
         }
      }.run();
   }
   
   @Test
   public void testRequiredPageParameter() throws Exception
   {
      new FacesRequest("/pageWithRequiredParameter.xhtml")
      {
         @Override
         protected void beforeRequest()
         {
            setParameter("personName", "pete");
         }
         
         @Override
         protected void invokeApplication() throws Exception
         {
            assert "pete".equals(getValue("#{person.name}"));
         }
      }.run();
      
   }
   
}
