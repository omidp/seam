package org.jboss.seam.test.integration.faces;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * JBSEAM-5067, handling exception in ExceptionFilter while long-running conversation with 
 * Page-scoped injection throws an exception in ServerConversationContext.flush
 *
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ExceptionRedirectTest
{
   private static final int AJAX_WAIT = 1000;
   
   private final WebClient client = new WebClient();
   
   @ArquillianResource
   URL contextPath;
   
   @Deployment(name="ExceptionRedirectTest")
   @OverProtocol("Servlet 3.0") 
   public static Archive<?> createDeployment()
   {
      // This is a client test, use a real (non-mocked) Seam deployment
      WebArchive war= Deployments.realSeamDeployment();
      war.delete("WEB-INF/pages.xml");
      war.addClasses(TestComponent.class, PageScopedComponent.class)
            .addAsWebResource(new StringAsset(
                  "<html xmlns=\"http://www.w3.org/1999/xhtml\"" +
                  " xmlns:h=\"http://java.sun.com/jsf/html\"" +
                  " xmlns:f=\"http://java.sun.com/jsf/core\"" +
                  " xmlns:ui=\"http://java.sun.com/jsf/facelets\">" +
                  "<h:head></h:head>" +
                  "<h:body>" +
                     "<h:form id='form'>" +
                     "<h:commandButton id='begin' action='#{redirecttestComponent.begin}' value='Begin' />" +
                     "<h:commandButton id='throw' action='#{redirecttestComponent.throwTestException}' value='Throw' />" +
                     "<h:commandButton id='throwAjax' action='#{redirecttestComponent.throwTestException}' value='Throw Ajax'>" +
                     "<f:ajax/>" +
                     "</h:commandButton>" +
                     "</h:form>" +
                   "</h:body>" + 
                  "</html>"), "test.xhtml")
            .addAsWebResource(new StringAsset(
                  "<html xmlns=\"http://www.w3.org/1999/xhtml\"" +
                  " xmlns:h=\"http://java.sun.com/jsf/html\"" +
                  " xmlns:f=\"http://java.sun.com/jsf/core\"" +
                  " xmlns:ui=\"http://java.sun.com/jsf/facelets\">" +
                  "<h:head></h:head>" +
                  "<h:body>" +
                   " Exception handled, state: <h:outputText value='#{redirecttestComponent.state}'/>" + 
                   "</h:body>" + 
                  "</html>"), "error.xhtml")
            .addAsWebInfResource(new StringAsset(
                  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                  "<pages xmlns=\"http://jboss.org/schema/seam/pages\"" +
                  " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                  " xsi:schemaLocation=\"http://jboss.org/schema/seam/pages http://jboss.org/schema/seam/pages-2.3.xsd\">\n" +
                  "<exception class=\"org.jboss.seam.test.integration.faces.ExceptionRedirectTest$TestException\">" + 
                   "<redirect view-id=\"/error.xhtml\">"+
                   "</redirect>"+
                   "</exception></pages>"), "pages.xml");

        return war;
   }
   
   public static class TestException extends Exception
   {
      private static final long serialVersionUID = 1L;
   }
   
   @Scope(ScopeType.CONVERSATION)
   @Name("redirecttestComponent")
   public static class TestComponent implements Serializable
   {
       private static final long serialVersionUID = 1L;
       
       public String state = "";
       
       @In(create=true)
       private PageScopedComponent pageScoped;
       
       @Begin
       public void begin()
       {
          state += "begin;";
       }
       
       public void throwTestException() throws TestException
       {
          state += "throwTestException;";
          throw new TestException();
       }

       public String getState()
       {
           return state;
       }
       
       /* // Workaround
       @org.jboss.seam.annotations.intercept.BypassInterceptors
       public boolean equals(Object obj)
       {
          return super.equals(obj);
       }*/
   }
   
   @Scope(ScopeType.PAGE)
   @Name("pageScoped")
   public static class PageScopedComponent implements Serializable
   {
      private static final long serialVersionUID = 1L;
   }
   
   @Test
   public void testExceptionHandingDuringConversationWithPageScopedInjection() throws Exception
   {
      HtmlPage page = client.getPage(contextPath + "test.seam");
      
      page = page.getElementById("form:begin").click();
      page = page.getElementById("form:throw").click();
      
      assertTrue(page.getBody().getTextContent().contains("Exception handled, state: begin;throwTestException;"));
   }
   
   // JBSEAM-5045
   @Test
   public void testExceptionHandingDuringConversationWithPageScopedInjectionWithAjax() throws Exception
   {
      HtmlPage page = client.getPage(contextPath + "test.seam");
      
      page = page.getElementById("form:begin").click();
      page = page.getElementById("form:throwAjax").click();
      
      Thread.sleep(AJAX_WAIT);
      
      page = (HtmlPage) client.getCurrentWindow().getEnclosedPage();
      
      assertFalse("Page should not contain form:begin button, as it should have been redirected.", page.getElementById("form:begin") != null);
      assertTrue("Page should contain 'Exception handled, state: begin;throwTestException;'", page.getBody().getTextContent().contains("Exception handled, state: begin;throwTestException;"));
   }
}
