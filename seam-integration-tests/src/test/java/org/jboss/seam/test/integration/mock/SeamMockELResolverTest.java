package org.jboss.seam.test.integration.mock;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.seam.test.integration.Deployments;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.runner.RunWith;
import org.junit.Test;


/**
 * Test for adding EL resolvers to Seam MockFacesContext
 * 
 * @author Pete Muir
 * 
 */
@RunWith(Arquillian.class)
public class SeamMockELResolverTest extends JUnitSeamTest
{
   @Deployment(name="SecurityTest")
   @OverProtocol("Servlet 3.0") 
   public static Archive<?> createDeployment()
   {
      return Deployments.defaultSeamDeployment();
   }
   
   private static final String property = "customELResolverTest";

   @Override
   protected ELResolver[] getELResolvers()
   {
      ELResolver[] resolvers = new ELResolver[2];
      resolvers[0] = new ELResolver()
      {

         @Override
         public Class<?> getCommonPropertyType(ELContext arg0, Object arg1)
         {
            return null;
         }

         @Override
         public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext arg0, Object arg1)
         {
            return null;
         }

         @Override
         public Class<?> getType(ELContext arg0, Object base, Object property)
                  throws NullPointerException, PropertyNotFoundException, ELException
         {
            return null;
         }

         @Override
         public Object getValue(ELContext context, Object base, Object property)
                  throws NullPointerException, PropertyNotFoundException, ELException
         {
            if (SeamMockELResolverTest.property.equals(property))
            {
               context.setPropertyResolved(true);
               return "found";
            }
            return null;
         }

         @Override
         public boolean isReadOnly(ELContext arg0, Object base, Object property)
                  throws NullPointerException, PropertyNotFoundException, ELException
         {
            if (SeamMockELResolverTest.property.equals(property))
            {
               return false;
            }
            return false;
         }

         @Override
         public void setValue(ELContext context, Object base, Object property, Object value)
                  throws NullPointerException, PropertyNotFoundException,
                  PropertyNotWritableException, ELException
         {
            if (SeamMockELResolverTest.property.equals(property))
            {
               throw new PropertyNotWritableException();
            }
         }

      };
      resolvers[1] = new ELResolver() {

          @Override
          public Class<?> getCommonPropertyType(ELContext arg0, Object arg1)
          {
             return null;
          }

          @Override
          public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext arg0, Object arg1)
          {
             return null;
          }

          @Override
          public Class<?> getType(ELContext arg0, Object base, Object property)
                   throws NullPointerException, PropertyNotFoundException, ELException
          {
             return null;
          }

          @Override
          public Object getValue(ELContext context, Object base, Object property)
                   throws NullPointerException, PropertyNotFoundException, ELException
          {
             if (base != null && "className".equals(property))
             {
                context.setPropertyResolved(true);
                return base.getClass().getSimpleName();
             }
             return null;
          }

          @Override
          public boolean isReadOnly(ELContext arg0, Object base, Object property)
                   throws NullPointerException, PropertyNotFoundException, ELException
          {
             return true;
          }

          @Override
          public void setValue(ELContext context, Object base, Object property, Object value)
                   throws NullPointerException, PropertyNotFoundException,
                   PropertyNotWritableException, ELException
          {
             throw new PropertyNotWritableException();
          }
      };
      return resolvers;
   }

   @Test
   public void testCustomELResolver() throws Exception
   {
      new FacesRequest()
      {
         @Override
         protected void invokeApplication() throws Exception
         {
            assert "found".equals(getValue("#{" + property + "}"));
            assert "String".equals(getValue("#{" + property + ".className}"));
         }
      }.run();
   }

}
