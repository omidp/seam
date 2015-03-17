package org.jboss.seam.test.integration.faces;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Test for JBSEAM-4966
 */
@RunWith(Arquillian.class)
@RunAsClient
public class SessionScopedOutjectionOverwriteTest
{
   private final WebClient client = new WebClient();
   
   @ArquillianResource
   URL contextPath;
   
   @Deployment(name="SessionScopedOutjectionOverwriteTest", testable=false)
   @OverProtocol("Servlet 3.0") 
   public static Archive<?> createDeployment()
   {
      // This is a client test, use a real (non-mocked) Seam deployment
      return Deployments.realSeamDeployment()
            .addClasses(Foo.class, Bar.class)
            .addAsWebResource(new StringAsset(
                  "<html xmlns=\"http://www.w3.org/1999/xhtml\"" +
                  " xmlns:h=\"http://java.sun.com/jsf/html\"" +
                  " xmlns:f=\"http://java.sun.com/jsf/core\"" +
                  " xmlns:ui=\"http://java.sun.com/jsf/facelets\">" +
                  "<h:head></h:head>" +
                  "<h:body>" +
                     "<h:form id='form'>" +
                     "<h:outputText value='Output: #{output}.'/>" +
                     "<h:commandButton id='foo' action='#{faces.foo.foo}' value='foo' />" +
                     "<h:commandButton id='bar' action='#{faces.bar.bar}' value='bar' />" +
                     "<h:commandButton id='nop' action='test' value='nop' />" +
                     "</h:form>" +
                   "</h:body>" + 
                  "</html>"), "test.xhtml");
   }
   
   @Test
   public void testJBSEAM4966() throws Exception {
      HtmlPage page = client.getPage(contextPath + "test.seam");

      page = page.getElementById("form:foo").click();
      assertTrue(page.getBody().getTextContent().contains("Output: foo"));
      
      page = page.getElementById("form:bar").click();
      assertTrue(page.getBody().getTextContent().contains("Output: bar"));
      
      page = page.getElementById("form:nop").click();
      assertFalse("Output should stay 'bar' after a 'nop' operation.", page.getBody().getTextContent().contains("Output: foo"));
      assertTrue(page.getBody().getTextContent().contains("Output: bar"));
   }

   @Scope(ScopeType.SESSION)
   @Name("faces.foo")
   public static class Foo
   {
      @Out(scope=ScopeType.SESSION)
      private String output;
      
      public void foo()
      {
         output = "foo";
      }
   }
   
   @Scope(ScopeType.EVENT)
   @Name("faces.bar")
   public static class Bar
   {
      @Out(scope=ScopeType.SESSION)
      private String output;
      
      public void bar()
      {
         output = "bar";
      }
   }
}
