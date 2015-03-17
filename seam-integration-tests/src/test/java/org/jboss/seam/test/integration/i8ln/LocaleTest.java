package org.jboss.seam.test.integration.i8ln;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.component.UIOutput;
import javax.faces.event.ValueChangeEvent;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.international.LocaleConfig;
import org.jboss.seam.international.LocaleSelector;
import org.jboss.seam.Seam;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class LocaleTest extends JUnitSeamTest
{
   @Deployment(name="LocaleTest")
   @OverProtocol("Servlet 3.0") 
   public static Archive<?> createDeployment()
   {
      return Deployments.defaultSeamDeployment();
   }

   @Test
   public void localeTest() throws Exception
   {
      new NonFacesRequest()
      {
         @Override
         protected void renderResponse() throws Exception
         {
            // it's necessary to emulate the startup behavior of LocaleConfig since it alters the JSF Application
            // and we cannot be sure that the JSF Application wasn't cleared by an earlier class
            // NOTE: I wish this test suite had some better place of initializing the application context
            Contexts.getApplicationContext().remove(Seam.getComponentName(LocaleConfig.class));
            LocaleConfig.instance();
         }
      }.run();

      new FacesRequest()
      {

         @Override
         protected void invokeApplication() throws Exception
         {
            // <i18:locale-config default-locale="fr_CA" supported-locales="fr_CA fr_FR en"/>
            List<Locale> supportedLocales = new ArrayList<Locale>();
            for (Iterator<Locale> iter = getFacesContext().getApplication().getSupportedLocales(); iter.hasNext();)
            {
               supportedLocales.add(iter.next());
            }

            Assert.assertEquals(3, supportedLocales.size());
            Assert.assertTrue( supportedLocales.contains(Locale.CANADA_FRENCH) );
            Assert.assertTrue( supportedLocales.contains(Locale.ENGLISH) );
            Assert.assertTrue( supportedLocales.contains(Locale.FRANCE) );
            Assert.assertEquals(Locale.CANADA_FRENCH, getFacesContext().getApplication().getDefaultLocale());
            
            // why not? I guess be default locale means different things in different contexts (server vs user)
            //assert org.jboss.seam.international.Locale.instance().equals(Locale.CANADA_FRENCH);
            
            // reset the locale configuration (as it would be w/o <i18n:locale-config>)
            getFacesContext().getApplication().setDefaultLocale(Locale.ENGLISH);
            //getFacesContext().getApplication().setSupportedLocales(null);
            
            Assert.assertEquals(Locale.getDefault(), org.jboss.seam.international.Locale.instance());
            
            LocaleSelector.instance().setLocale(Locale.UK);
            
            Assert.assertEquals(Locale.UK, org.jboss.seam.international.Locale.instance());
          
            LocaleSelector.instance().setLocaleString(Locale.FRANCE.toString());
            
            Assert.assertEquals(Locale.FRANCE.getLanguage(), LocaleSelector.instance().getLanguage());
            Assert.assertEquals(Locale.FRANCE.getCountry(), LocaleSelector.instance().getCountry());
            Assert.assertEquals(Locale.FRANCE.getVariant(), LocaleSelector.instance().getVariant());
            
            Assert.assertEquals(Locale.FRANCE, org.jboss.seam.international.Locale.instance());
            Assert.assertEquals(Locale.FRANCE.toString(), LocaleSelector.instance().getLocaleString());
            
            LocaleSelector.instance().select();
            Assert.assertEquals(Locale.FRANCE, org.jboss.seam.international.Locale.instance());
            
            LocaleSelector.instance().selectLanguage(Locale.JAPANESE.getLanguage());
            Assert.assertEquals(Locale.JAPANESE.getLanguage(), org.jboss.seam.international.Locale.instance().getLanguage());
            
            ValueChangeEvent valueChangeEvent = new ValueChangeEvent(new UIOutput(), Locale.JAPANESE.toString(), Locale.TAIWAN.toString());
            LocaleSelector.instance().select(valueChangeEvent);
            Assert.assertEquals(Locale.TAIWAN, LocaleSelector.instance().getLocale());
            
            Locale uk_posix = new Locale(Locale.UK.getLanguage(), Locale.UK.getCountry(), "POSIX");
            LocaleSelector.instance().setLocale(uk_posix);
            
            Assert.assertEquals(uk_posix, org.jboss.seam.international.Locale.instance());
            Assert.assertEquals(uk_posix.getLanguage(), LocaleSelector.instance().getLanguage());
            Assert.assertEquals(uk_posix.getCountry(), LocaleSelector.instance().getCountry());
            Assert.assertEquals(uk_posix.getVariant(), LocaleSelector.instance().getVariant());
            
            LocaleSelector.instance().setLanguage(Locale.CHINA.getLanguage());
            LocaleSelector.instance().setCountry(Locale.CHINA.getCountry()); 
            LocaleSelector.instance().setVariant(null);
            
            Assert.assertEquals(Locale.CHINA, org.jboss.seam.international.Locale.instance());
            
            LocaleSelector.instance().setLanguage(Locale.ITALIAN.getLanguage());
            LocaleSelector.instance().setCountry(null);            
            LocaleSelector.instance().setVariant(null);
            
            Assert.assertEquals(Locale.ITALIAN, org.jboss.seam.international.Locale.instance());
            
            Assert.assertEquals(3, LocaleSelector.instance().getSupportedLocales().size());
            Assert.assertEquals(Locale.ENGLISH.toString(), LocaleSelector.instance().getSupportedLocales().get(2).getValue() );
            Assert.assertEquals(Locale.ENGLISH.getDisplayName(), LocaleSelector.instance().getSupportedLocales().get(2).getLabel() );

            boolean failed = false;
            try
            {
               LocaleSelector.instance().setLocale(null);
            }
            catch (NullPointerException e) 
            {
               failed = true;
            }
            Assert.assertEquals(true, failed);
            
            // TODO Test cookie stuff (need to extend Mocks for this)
            
         }
      }.run();
   }
}
