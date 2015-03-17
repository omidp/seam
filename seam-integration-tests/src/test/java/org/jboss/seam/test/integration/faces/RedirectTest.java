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
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.exception.Redirect;
import org.jboss.seam.core.Manager;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


@RunWith(Arquillian.class)
@RunAsClient
public class RedirectTest {

	private final WebClient client = new WebClient();
	   
	@ArquillianResource
	URL contextPath;
	   
	@Deployment(name="RedirectTest")
	@OverProtocol("Servlet 3.0") 
	public static Archive<?> createDeployment()
	{
		// This is a client test, use a real (non-mocked) Seam deployment
		return Deployments.realSeamDeployment()
				    .addClasses(MyComponent.class, MyException.class)
				    
		            .addAsWebResource(new StringAsset(
		            	 "<html xmlns=\"http://www.w3.org/1999/xhtml\"" +
		            	   " xmlns:h=\"http://java.sun.com/jsf/html\"" +
		                   " xmlns:f=\"http://java.sun.com/jsf/core\"" +
		                   " xmlns:ui=\"http://java.sun.com/jsf/facelets\">" +
		                 "<h:head></h:head>" +
		                 "<h:body>" +	
		                    "<h:form id='form'>" +
		                        "<h:commandButton id='beginNestedConversation' action='#{redirectTestComponent.beginNestedConversation}'/>" +
		                    	"<h:commandButton id='throwException' action='#{redirectTestComponent.throwException}'/>" +
		                        
		                    	"<h:commandButton id='redirectPropagation' action='#{redirectTestComponent.redirectPropagation}'/>" +
		                    	"<h:commandButton id='redirectNotPropagation' action='#{redirectTestComponent.redirectNotPropagation}'/>" +
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
		                    "<h:messages globalOnly=\"true\"/>"+
	                     "</h:body>" + 
		                 "</html>"), "error.xhtml");
	}
	   
	@Test
	public void testRedirect() throws Exception
	{
		HtmlPage page = client.getPage(contextPath + "test.seam"); 
		page = page.getElementById("form:redirectNotPropagation").click(); 
		assertFalse(page.getUrl().toString().contains("cid"));
		
		page = client.getPage(contextPath + "test.seam");		   
		page = page.getElementById("form:redirectPropagation").click(); 
		assertTrue(page.getUrl().toString().contains("cid"));
	}
	   
	@Test
	public void testErrorRedirect() throws Exception
	{
	   
		HtmlPage page = client.getPage(contextPath + "test.seam"); 
		
		page = page.getElementById("form:throwException").click();
		assertTrue(page.getBody().getTextContent().contains("Unexpected error, please try again"));
		assertTrue(page.getUrl().toString().contains("cid"));
		
		   
		page = client.getPage(contextPath + "test.seam"); 
		page = page.getElementById("form:beginNestedConversation").click();      
		page = page.getElementById("form:throwException").click();
		   
		assertTrue(page.getBody().getTextContent().contains("Unexpected error, please try again"));
		assertTrue(page.getUrl().toString().contains("cid"));
		assertTrue(page.getUrl().toString().contains("parentConversationId"));		   		   
	}

	
	@Scope(ScopeType.CONVERSATION)
	@Name("redirectTestComponent")
	public static class MyComponent implements Serializable
	{
		private static final long serialVersionUID = 1L;
	       
		public String value;
		
		@Create
		@Begin
		public void begin()
		{
		}
	       
		public void beginNestedConversation(){
			Manager.instance().beginNestedConversation();
		}
	       
		public void throwException(){
			throw new MyException();
		}
	       
		public void redirectPropagation(){
			org.jboss.seam.faces.Redirect redirect = org.jboss.seam.faces.Redirect.instance();
			redirect.setConversationPropagationEnabled(true);
			redirect.setViewId("/test.xhtml");
			redirect.execute();
		}
		
		public void redirectNotPropagation(){
			org.jboss.seam.faces.Redirect redirect = org.jboss.seam.faces.Redirect.instance();
			redirect.setConversationPropagationEnabled(false);
			redirect.setViewId("/test.xhtml");
			redirect.execute();
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
	
	@Redirect(viewId="/error.xhtml", message="Unexpected error, please try again", end=true)
	public static class MyException extends RuntimeException {

		private static final long serialVersionUID = 1L;
		   
	}
}
