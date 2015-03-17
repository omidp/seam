package org.jboss.seam.test.integration.i8ln;

import java.util.TimeZone;

import javax.faces.component.UIOutput;
import javax.faces.event.ValueChangeEvent;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.international.TimeZoneSelector;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Pete Muir
 *
 */
@RunWith(Arquillian.class)
public class TimeZoneTest extends JUnitSeamTest
{
   @Deployment(name="TimeZoneTest")
   @OverProtocol("Servlet 3.0") 
   public static Archive<?> createDeployment()
   {
      return Deployments.defaultSeamDeployment();
   } 

   @Test
   public void timeZoneTest() throws Exception
   {
      new FacesRequest()
      {
         @Override
         protected void invokeApplication() throws Exception
         {
            assert org.jboss.seam.international.TimeZone.instance().equals(java.util.TimeZone.getDefault());
            
            TimeZone cet = TimeZone.getTimeZone("CET");
            TimeZoneSelector.instance().setTimeZone(cet);
            
            assert org.jboss.seam.international.TimeZone.instance().equals(cet);
          
            TimeZoneSelector.instance().setTimeZoneId("CET");
            
            assert org.jboss.seam.international.TimeZone.instance().equals(cet);
            
            TimeZoneSelector.instance().selectTimeZone("GMT");
            assert org.jboss.seam.international.TimeZone.instance().getID().equals("GMT");
            
            ValueChangeEvent valueChangeEvent = new ValueChangeEvent(new UIOutput(), "GMT", "PST");
            TimeZoneSelector.instance().select(valueChangeEvent);
            assert org.jboss.seam.international.TimeZone.instance().getID().equals("PST");
            
            // TODO Test cookie stuff (need to extend Mocks for this)
            
         }
      }.run();
   }
}
