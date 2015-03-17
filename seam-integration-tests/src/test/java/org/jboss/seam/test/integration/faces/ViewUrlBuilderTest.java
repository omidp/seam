package org.jboss.seam.test.integration.faces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import javax.validation.constraints.AssertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@RunWith(Arquillian.class)
@RunAsClient
public class ViewUrlBuilderTest
{
   @ArquillianResource
   URL contextPath;
   
   private final WebClient client = new WebClient();
   
   @Deployment(name="ViewUrlBuilderTest")
   @OverProtocol("Servlet 3.0") 
   public static Archive<?> createDeployment()
   {
      // This is a client test, use a real (non-mocked) Seam deployment
      WebArchive war = Deployments.realSeamDeployment().addClasses(TestComponent.class);
      war.delete("WEB-INF/pages.xml");
      war.delete("WEB-INF/components.xml");
      war.delete("WEB-INF/web.xml");
      
      war.addAsWebInfResource(new StringAsset(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<pages xmlns=\"http://jboss.org/schema/seam/pages\"" +
            " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
            " xsi:schemaLocation=\"http://jboss.org/schema/seam/pages http://jboss.org/schema/seam/pages-2.3.xsd\">\n" +
             "<page view-id=\"/testslink.xhtml\">"+
                "<rewrite pattern=\"/testslink/{input}\" />" +
                "<rewrite pattern=\"/testslink\" />"+
                "<param name=\"input\" value=\"#{testslink.input}\"/>" +
             "</page></pages>"), "pages.xml")
             .addAsWebResource("testslink.xhtml","testslink.xhtml")
             .addAsWebResource("WEB-INF/components-rewrite.xml","WEB-INF/components.xml")
             .addAsWebResource("WEB-INF/rewrite-web.xml","WEB-INF/web.xml");
      
      return war;
   }
  
   @Test   
   public void testRewriteSeamLink() throws Exception
   {
      HtmlPage page = client.getPage(contextPath + "testslink/1");
      // second request eliminates ";jsessionid=[a-Z0-9] parameter 
      page = client.getPage(contextPath + "testslink/1");
      assertTrue(!page.asText().isEmpty());
      String href = page.getElementById("form:testslink").getAttribute("href");
      assertEquals("/test/testslink/1", href);
   }
   

}
