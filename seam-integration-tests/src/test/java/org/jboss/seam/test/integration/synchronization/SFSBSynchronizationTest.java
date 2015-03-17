package org.jboss.seam.test.integration.synchronization;

import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunAsClient
@RunWith(Arquillian.class)
public class SFSBSynchronizationTest
{
   @Deployment(name="SFSBSynchronizationTest")
   @OverProtocol("Servlet 3.0") 
   public static Archive<?> createDeployment()
   {
      // This is a client test, use a real (non-mocked) Seam deployment
      return Deployments.realSeamDeployment()
            .addClasses(TestAction.class, TestLocal.class)
            .addAsWebResource(new StringAsset(
                  "<html xmlns=\"http://www.w3.org/1999/xhtml\"" +
                  " xmlns:s=\"http://jboss.org/schema/seam/taglib\"" +
                  " xmlns:h=\"http://java.sun.com/jsf/html\"" +
                  " xmlns:f=\"http://java.sun.com/jsf/core\">" +
                  "<h:head></h:head>" +
                  "<h:body>" +
                  "<h:outputText value=\"#{test.test1()} \" /><h:outputText value=\"#{test.test2()}\" />" +
                  "</h:body>" + 
                  "</html>"), "test.xhtml");
   }
   
   @ArquillianResource
   private URL deploymentUrl;
   
   private volatile boolean exceptionOccured = false;
   
   private class ClientThread extends Thread {
      
      private String cookie;
      private URL url;
      
      private ClientThread(URL url, String cookie) {
         this.url = url;
         this.cookie = cookie;
      }
      
      @Override
      public void run()
      {
         try
         {
            // 10 iterations are enough to be very likely to reproduce the lock and takes only 2 seconds
            for (int i = 0; i < 10; ++i) {
               URLConnection urlConn;
               urlConn = url.openConnection();
               urlConn.setRequestProperty("Cookie", cookie);
               urlConn.connect();
               
               String content = IOUtils.toString(urlConn.getInputStream());
               assert content.contains("test1 test2");
            }
         }
         catch (Throwable e)
         {
            e.printStackTrace();
            exceptionOccured = true;
         }
      }
   }
   
   // JBPAPP-8869 (JBSEAM-4943)
   @Test
   public void synchronizationInterceptor() 
       throws Exception 
   {
      System.out.println(deploymentUrl.toString());
      
      // Initial request to get the session
      URL testUrl = new URL(deploymentUrl.toString() + "/test.seam");
      URLConnection urlConn = testUrl.openConnection();
      urlConn.connect();
      
      String cookie = urlConn.getHeaderField("Set-Cookie");
      assert cookie != null;
      assert cookie.startsWith("JSESSIONID=");
      
      Thread thread1 = new ClientThread(testUrl, cookie);
      Thread thread2 = new ClientThread(testUrl, cookie);
      
      thread1.start();
      thread2.start();
      
      thread1.join();
      thread2.join();
      
      assert !exceptionOccured;
   }
}
