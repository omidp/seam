package org.jboss.seam.test.integration.faces;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


// JBSEAM-5002
@RunWith(Arquillian.class)
@RunAsClient
public class PageScopedUIIncludeTest
{
   private final WebClient client = new WebClient();

   @ArquillianResource
   URL contextPath;
   
   @Deployment(name="PageScopedUIIncludeTest")
   @OverProtocol("Servlet 3.0") 
   public static Archive<?> createDeployment()
   {
      // This is a client test, use a real (non-mocked) Seam deployment
      return Deployments.realSeamDeployment()
            .addClasses(Component.class, Component1.class, Component2.class)
            .addAsWebResource(new StringAsset(
                  "<html xmlns=\"http://www.w3.org/1999/xhtml\"" +
                  " xmlns:h=\"http://java.sun.com/jsf/html\"" +
                  " xmlns:f=\"http://java.sun.com/jsf/core\"" +
                  " xmlns:ui=\"http://java.sun.com/jsf/facelets\">" +
                  "<h:head></h:head>" +
                  "<h:body>" +
                     "<h:form id='controller'>" +
                        "<h:commandButton id='component1' action='#{viewController.component1}' value='Component 1' />" +
                        "<h:commandButton id='component2' action='#{viewController.component2}' value='Component 2' />" +
                     "</h:form>" +
                     "<ui:include src='#{viewController.viewId}'/>" +
                  "</h:body>" + 
                  "</html>"), "test.xhtml")
            .addAsWebResource(createComponentXhtmlAsset(1), "component1.xhtml")
            .addAsWebResource(createComponentXhtmlAsset(2), "component2.xhtml");
   }
   
   private static Asset createComponentXhtmlAsset(int i)
   {
      return new StringAsset("<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"" +
            " xmlns:ui=\"http://java.sun.com/jsf/facelets\"" +
            " xmlns:h=\"http://java.sun.com/jsf/html\">" +
            "<h3>Component " + i + "</h3>" +
            "<h:form id='form" + i + "'>" +
               "<h:inputText id='input' value='#{component" + i + ".input}' />" +
               "<h:commandButton id='save' value='Save' action='#{component" + i + ".save}' />" +
               "<h:outputText value='#{component" + i + ".output}' />" + 
            "</h:form>" +
            "</ui:composition>"); // Yay for Java String syntax!
   }
   
   @Test
   public void testComponent1() throws Exception
   {
      HtmlPage page = client.getPage(contextPath + "test.seam");
      assertTrue(page.getBody().getTextContent().contains("Component 1"));
      
      page.getElementById("form1:input").type("xyzzy");
      page = page.getElementById("form1:save").click();
      
      assertTrue(page.getBody().getTextContent().contains("Hello, xyzzy"));
   }
   
   @Ignore // JBSEAM-5002
   @Test
   public void testComponent2() throws Exception
   {
      HtmlPage page = client.getPage(contextPath + "test.seam");
      assertTrue(page.getBody().getTextContent().contains("Component 1"));
      page = page.getElementById("controller:component2").click();
      assertTrue(page.getBody().getTextContent().contains("Component 2"));
      
      page.getElementById("form2:input").type("foobar");
      page = page.getElementById("form2:save").click();
      
      assertTrue(page.getBody().getTextContent().contains("Hi, foobar"));
   }
   
   public abstract static class Component
   {
      protected String input;
      protected String output;

      public String getInput()
      {
         return input;
      }

      public void setInput(String input)
      {
         this.input = input;
      }
      
      public String getOutput()
      {
         return output;
      }
      
      abstract public void save();
   }
   
   @Scope(ScopeType.CONVERSATION)
   @Name("component1")
   public static class Component1 extends Component
   {
      public void save()
      {
         output = "Hello, " + input;
      }
   }
   
   @Scope(ScopeType.CONVERSATION)
   @Name("component2")
   public static class Component2 extends Component
   {
      public void save()
      {
         output = "Hi, " + input;
      }
   }
   
   @Scope(ScopeType.PAGE)
   @Name("viewController")
   public static class ViewController implements Serializable
   {
      private String viewId = "/component1.xhtml";
      
      public void setViewId(String viewId)
      {
         this.viewId = viewId;
      }
      
      public String getViewId()
      {
         return viewId;
      }
      
      public void component1()
      {
         setViewId("/component1.xhtml");
      }
      
      public void component2()
      {
         setViewId("/component2.xhtml");
      }
   }
}
