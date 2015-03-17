package org.jboss.seam.test.integration.faces;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.net.URL;
import java.util.Deque;
import java.util.LinkedList;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * Test accessing conversation scoped component from a restore view phase (by a JSF validator attribute)
 * Related to JBSEAM-4976
 */
@RunWith(Arquillian.class)
@RunAsClient
public class RestoreViewComponentAccessTest
{
   private final WebClient client = new WebClient();

   @ArquillianResource
   URL contextPath;

   @Deployment(name="RestoreViewComponentAccessTest")
   @OverProtocol("Servlet 3.0")
   public static WebArchive createDeployment()
   {
      // This is a client test, use a real (non-mocked) Seam deployment
      WebArchive war = Deployments.realSeamDeployment()
            .addClasses(SequenceAction.class);

      war.delete("WEB-INF/pages.xml");
      war.delete("WEB-INF/components.xml");

      war.addAsWebResource(new StringAsset(
            "<html xmlns=\"http://www.w3.org/1999/xhtml\"" +
            " xmlns:h=\"http://java.sun.com/jsf/html\"" +
            " xmlns:f=\"http://java.sun.com/jsf/core\"" +
            " xmlns:s=\"http://jboss.org/schema/seam/taglib\"" +
            " xmlns:ui=\"http://java.sun.com/jsf/facelets\">" +
            "<h:head></h:head>" +
            "<h:body>" +
               "<h:form id='form'>" +
                  "<h:messages/>" +
                  "<h:outputText id='output' value='Sequence: #{sequence.output}'/>" +
                  "<h:inputText id='input' value='#{sequence.input}'>" +
                  "<f:validateLongRange minimum='#{sequence.minimum}' />" +
                  "</h:inputText>" +
                  "<h:commandButton id='append' value='Append' action='#{sequence.append}'/>" +
               "</h:form>" +
            "</h:body>" +
            "</html>"), "test.xhtml");

      war.addAsWebInfResource(new StringAsset(
            "<pages xmlns=\"http://jboss.org/schema/seam/pages\""+
            " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
            "<page view-id='/test.xhtml'>" +
            "<begin-conversation join='true'/>" +
            "<navigation><redirect view-id='/test.xhtml'/></navigation>" +
            "</page></pages>"), "pages.xml");

      return war;
   }

   //@Ignore // JBSEAM-4976
   @Test
   public void testConversationWithValidator() throws Exception {
      HtmlPage page = client.getPage(contextPath + "test.seam");
      assertTrue(page.getBody().getTextContent().contains("Sequence: "));

      ((HtmlTextInput)page.getElementById("form:input")).setText("1");
      page = page.getElementById("form:append").click();

      assertTrue(page.getBody().getTextContent().contains("Sequence: 1"));

      ((HtmlTextInput)page.getElementById("form:input")).setText("2");
      page = page.getElementById("form:append").click();

      assertTrue(page.getBody().getTextContent().contains("Sequence: 1, 2"));

      ((HtmlTextInput)page.getElementById("form:input")).setText("1");
      page = page.getElementById("form:append").click();

      assertFalse(page.getBody().getTextContent().contains("Sequence: 1, 2, 1"));
      assertTrue(page.getBody().getTextContent().contains("value must be greater than or equal to 2"));
   }

   @Name("sequence")
   @Scope(ScopeType.CONVERSATION)
   public static class SequenceAction implements Serializable {
      private static final long serialVersionUID = 1L;

      private Deque<Long> sequence;
      private Long input;

      @Create
      public void create() {
         sequence = new LinkedList<Long>();
      }

      public String getOutput() {
         StringBuilder sb = new StringBuilder();
         for (Long n : sequence) {
            sb.append(n);
            sb.append(", ");
         }

         return sb.toString();
      }

      public void append() {
         sequence.add(input);
      }

      public Long getInput()
      {
         return input;
      }

      public void setInput(Long input)
      {
         this.input = input;
      }

      public Long getMinimum() {
         if (sequence.isEmpty()) {
            return 0L;
         }

         return sequence.getLast();
      }
   }
}
