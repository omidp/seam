package org.jboss.seam.test.integration.i8ln;

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


@RunWith(Arquillian.class)
@RunAsClient
public class RealLocaleTest {
	   
	   @Deployment(name="RealLocaleTest")
	   @OverProtocol("Servlet 3.0") 
	   public static Archive<?> createDeployment()
	   {
	      return Deployments.realSeamDeployment()
	               .addClass(TestBean.class)
	               .addAsWebResource("locale.xhtml","locale.xhtml");
	   }

	   @ArquillianResource
	   URL url;
	   
 	   @Test
	   public void testDefaultLocale() throws Exception{
     	
		  WebClient client = new WebClient();
	      HtmlPage page = client.getPage(url+"locale.seam");
	      
	      String pageBody = page.getBody().asText();
	      assertTrue("Default locale is not set correctly!",pageBody.contains("Default locale: fr_CA"));
			   
	   } 
 	   
 	   @Test
	   public void testSupportedLocale() throws Exception{
     	
		  WebClient client = new WebClient();
	      HtmlPage page = client.getPage(url+"locale.seam");
	      
	      String pageBody = page.getBody().asText();
	      String[] locales = new String [] {"fr_CA", "fr_FR" , "en"};
	     
	      for (String locale : locales) {
	    	  assertTrue("Supported locale is not set correctly!", pageBody.contains("Supported locale: "+locale));
	      }
		   
	   }
	   
	   @Name("testBean")
	   public static class TestBean 
	   {
		   Application app = FacesContext.getCurrentInstance().getApplication();
		   
		   public String getDefaultLocale(){
			   
			   if(app.getDefaultLocale() == null){
				   return "";
			   }
			   return app.getDefaultLocale().toString();
		   }
		   
		   public List<Locale> getSupportedLocale(){
			   
			   List<Locale> locales = new ArrayList<Locale>();
			   
			   if(app.getSupportedLocales() == null){
				   return locales;
			   }
			   
			   Iterator<Locale> it = app.getSupportedLocales();
			   
			   while(it.hasNext()){
				   locales.add(it.next());
			   }
			   return locales;
		   }
	   }
}
