/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.seam.mock;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jboss.seam.Seam;
import org.jboss.seam.contexts.ServletLifecycle;
import org.jboss.seam.log.LogProvider;
import org.jboss.seam.log.Logging;

/**
 * Used to retrieve real ServletContext for the AbstractSeamTest startSeam. 
 * 
 * @author Marek Schmidt
 */
public class MockSeamListener implements ServletContextListener, HttpSessionListener
{
   private static final LogProvider log = Logging.getLogProvider(ServletContextListener.class);
   
   private static ServletContext servletContext;
   
   public void contextInitialized(ServletContextEvent event) 
   {
      log.info( "Welcome to Mock Seam " + Seam.getVersion() );
      event.getServletContext().setAttribute( Seam.VERSION, Seam.getVersion() );
      servletContext = event.getServletContext();
      
      // Sabotage Mojarra initialization.
      // This is required as Mojarra will attempt to initialize even if there is no FacesServlet configured in web.xml
      servletContext.removeAttribute("com.sun.faces.facesInitializerMappingsAdded");
   }
   
   public static ServletContext getServletContext() {
      return servletContext;
   }
   
   public void contextDestroyed(ServletContextEvent event) 
   {
   }
   
   public void sessionCreated(HttpSessionEvent event) 
   {
      ServletLifecycle.beginSession( event.getSession() );
   }
   
   public void sessionDestroyed(HttpSessionEvent event) 
   {
      ServletLifecycle.endSession( event.getSession() );
   }
}
