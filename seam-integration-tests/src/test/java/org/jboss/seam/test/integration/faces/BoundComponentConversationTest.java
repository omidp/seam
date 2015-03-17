package org.jboss.seam.test.integration.faces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIInput;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


// JBSEAM-5020
@RunWith(Arquillian.class)
@RunAsClient
public class BoundComponentConversationTest
{
   private final WebClient client = new WebClient();
   
   @ArquillianResource
   URL contextPath;
   
   @Deployment(name="BoundComponentConversationTest")
   @OverProtocol("Servlet 3.0") 
   public static Archive<?> createDeployment()
   {
      // This is a client test, use a real (non-mocked) Seam deployment
      return Deployments.realSeamDeployment()
            .addClasses(MyComponent.class, MyBackingBean.class)
            .addAsWebResource(new StringAsset(
                  "<html xmlns=\"http://www.w3.org/1999/xhtml\"" +
                  " xmlns:h=\"http://java.sun.com/jsf/html\"" +
                  " xmlns:f=\"http://java.sun.com/jsf/core\"" +
                  " xmlns:ui=\"http://java.sun.com/jsf/facelets\">" +
                  "<h:head></h:head>" +
                  "<h:body>" +
                     "<h:form id='form'>" +
                     "<h:outputText value='Conversation id: #{conversation.id}.'/>" +
                     "<h:inputText value='#{myComponent.value}' binding='#{myBackingBean.input}'/>" +
                     "<h:commandButton id='test' action='test' value='Test' />" +
                     "</h:form>" +
                   "</h:body>" + 
                  "</html>"), "test.xhtml");
   }
   
   @Test
   @Ignore //This test is not 100% correct, because of conversation init/restore is done later when this expect
   public void testConversationRestoration() throws Exception
   {
      Pattern conversationIdPattern = Pattern.compile("Conversation id: (\\d+)\\.");
      HtmlPage page = client.getPage(contextPath + "test.seam");
      
      Matcher conversationIdMatcher = conversationIdPattern.matcher(page.getBody().getTextContent());
      assertTrue(conversationIdMatcher.find());
      
      String firstConversationId = conversationIdMatcher.group(1);

      page = page.getElementById("form:test").click();
      
      conversationIdMatcher = conversationIdPattern.matcher(page.getBody().getTextContent());
      assertTrue(conversationIdMatcher.find());
      
      String secondConversationId = conversationIdMatcher.group(1);
      assertEquals(firstConversationId, secondConversationId);
   }
   
   @Scope(ScopeType.CONVERSATION)
   @Name("myComponent")
   public static class MyComponent implements Serializable
   {
       private static final long serialVersionUID = 1L;
       
       public String value;
       
       @Create
       @Begin
       public void begin()
       {
       }

       public String getValue()
       {
           return value;
       }
       
       public void setValue(String value)
       {
           this.value = value;
       }
       
   }
   
   @Scope(ScopeType.EVENT)
   @Name("myBackingBean")
   public static class MyBackingBean 
   {
       private UIInput input;

       public UIInput getInput()
       {
           return input;
       }
       
       public void setInput(UIInput input)
       {
           this.input = input;
       }
   }
}
