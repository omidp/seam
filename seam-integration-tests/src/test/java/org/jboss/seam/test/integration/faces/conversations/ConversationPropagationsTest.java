package org.jboss.seam.test.integration.faces.conversations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests various conversation propagation scenarios.
 * 
 * @author maschmid
 *
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ConversationPropagationsTest
{
   private static String FORM_PREFIX_ID = "form:";

   private static int JAVASCRIPT_WAIT = 1000;

   private final WebClient client = new WebClient();

   @ArquillianResource
   URL contextPath;

   @Deployment(name = "ConversationPropagationsTest")
   @OverProtocol("Servlet 3.0")
   public static Archive<?> createDeployment()
   {
      // This is a client test, use a real (non-mocked) Seam deployment
      WebArchive war = Deployments.realSeamDeployment().addClasses(ConversationAction.class, MyException.class,
            MyConversationEndingException.class);

      war.delete("WEB-INF/pages.xml");

      war.addAsWebResource("org/jboss/seam/test/integration/faces/conversations/conversations.xhtml",
            "conversations.xhtml")
            .addAsWebResource("org/jboss/seam/test/integration/faces/conversations/error.xhtml", "error.xhtml")
            .addAsWebResource("org/jboss/seam/test/integration/faces/conversations/home.xhtml", "home.xhtml")
            .addAsWebResource("org/jboss/seam/test/integration/faces/conversations/output.xhtml", "output.xhtml")
            .addAsWebInfResource("org/jboss/seam/test/integration/faces/conversations/pages.xml", "pages.xml");

      return war;
   }

   @Test
   public void conversationTest() throws FailingHttpStatusCodeException, MalformedURLException, IOException
   {
      HtmlPage page = client.getPage(contextPath + "conversations.seam");
      assertEquals("Seam Conversations Test", page.getTitleText());
   }

   private void testScenario(String output, String... ids) throws FailingHttpStatusCodeException,
         MalformedURLException, IOException
   {
      HtmlPage page = client.getPage(contextPath + "conversations.seam");

      for (String id : ids)
      {
         page.getElementById(FORM_PREFIX_ID + id).click();
         client.waitForBackgroundJavaScript(JAVASCRIPT_WAIT);
         page = (HtmlPage) client.getCurrentWindow().getEnclosedPage();
      }

      page = (HtmlPage) client.getCurrentWindow().getEnclosedPage();

      assertTrue("Page should contain '" + output + "'", page.getBody().getTextContent().contains(output));
   }

   private void testCommonScenario(String output, String... ids) throws FailingHttpStatusCodeException,
         MalformedURLException, IOException
   {
      List<String> allIds = new LinkedList<String>();
      allIds.add("begin");
      allIds.add("xyzzy");
      allIds.addAll(Arrays.asList(ids));
      testScenario(output, allIds.toArray(new String[]
      {}));

      // test with AJAX button
      allIds = new LinkedList<String>();
      allIds.add("begin");
      allIds.add("ajax");
      allIds.addAll(Arrays.asList(ids));
      testScenario(output, allIds.toArray(new String[]
      {}));
   }

   @Test
   public void testViewOutput() throws Exception
   {
      testCommonScenario("Output: begin;xyzzy;viewOutput;. Conversation: true.", "view_output");
   }

   @Test
   public void testRedirectOutput() throws Exception
   {
      testCommonScenario("Output: begin;xyzzy;redirectOutput;. Conversation: true.", "redirect_output");
   }

   @Test
   public void testPagesRender() throws Exception
   {
      testCommonScenario("Output: begin;xyzzy;pagesRender;. Conversation: true.", "pages_render");
   }

   @Test
   public void testPagesRedirect() throws Exception
   {
      testCommonScenario("Output: begin;xyzzy;pagesRedirect;. Conversation: true.", "pages_redirect");
   }

   @Test
   public void testProgrammaticRedirect() throws Exception
   {
      testCommonScenario("Output: begin;xyzzy;programmaticRedirect;. Conversation: true.", "programmatic_redirect");
   }

   @Test
   public void testProgrammaticRedirectAjax() throws Exception
   {
      testCommonScenario("Output: begin;xyzzy;programmaticRedirect;. Conversation: true.", "programmatic_redirect_ajax");
   }

   @Test
   public void testProgrammaticRedirectNoPropagation() throws Exception
   {
      testCommonScenario("Output: . Conversation: false.", "programmatic_redirect_no_propagation");
   }

   @Test
   public void testProgrammaticRedirectNoPropagationAjax() throws Exception
   {
      testCommonScenario("Output: . Conversation: false.", "programmatic_redirect_no_propagation_ajax");
   }

   @Test
   public void testOutputLink() throws Exception
   {
      testCommonScenario("Output: . Conversation: false.", "output_link");
   }

   @Test
   public void testOutputLinkWithCid() throws Exception
   {
      testCommonScenario("Output: begin;xyzzy;. Conversation: true.", "output_link_with_cid");
   }

   @Test
   public void testSButtonActionPropagationNone() throws Exception
   {
      testCommonScenario("Output: viewOutput;. Conversation: false.", "sbutton_view_action_propagation_none");
   }

   @Test
   public void testSButtonViewPropagationNone() throws Exception
   {
      testCommonScenario("Output: . Conversation: false.", "sbutton_view_propagation_none");
   }

   @Test
   public void testSButtonPropagationJoin() throws Exception
   {
      testCommonScenario("Output: begin;xyzzy;. Conversation: true.", "sbutton_view_propagation_join");
   }

   @Test
   public void testSButtonViewPropagationNoneWithConversationPropagationNone() throws Exception
   {
      testCommonScenario("Output: . Conversation: false.", "sbutton_view_propagation_none_with_conversationPropagation");
   }

   @Test
   public void testException() throws Exception
   {
      testCommonScenario("Output: begin;xyzzy;raiseException;. Conversation: true.", "exception");
   }

   @Test
   public void testConversationEndingException() throws Exception
   {
      testCommonScenario("Output: begin;xyzzy;raiseConversationEndingException;. Conversation: false.",
            "conversation_ending_exception");
   }

   @Test
   public void testExceptionByAjax() throws Exception
   {
      testCommonScenario("Output: begin;xyzzy;raiseException;. Conversation: true.", "exception_by_ajax");
   }

   @Test
   public void testConversationEndingExceptionByAjax() throws Exception
   {
      testCommonScenario("Output: begin;xyzzy;raiseConversationEndingException;. Conversation: false.",
            "conversation_ending_exception_by_ajax");
   }
}
