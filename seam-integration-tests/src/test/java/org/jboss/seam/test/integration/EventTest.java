package org.jboss.seam.test.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.Component;
import org.jboss.seam.core.Events;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * 
 * @author Pete Muir
 *
 */
@RunWith(Arquillian.class)
public class EventTest extends JUnitSeamTest {

	@Deployment(name="IdentifierTest")
	@OverProtocol("Servlet 3.0")
	public static Archive<?> createDeployment()
	{
		return Deployments.defaultSeamDeployment()
				.addClasses(BeanA.class, BeanB.class);
	}
	
    @Test
    public void testEventChain() throws Exception {

        new FacesRequest("/index.xhtml") {

            @Override
            protected void invokeApplication() throws Exception {
                BeanA beanA = (BeanA) Component.getInstance("beanA");
                BeanB beanB = (BeanB) Component.getInstance("beanB");

                assert "Foo".equals(beanA.getMyValue());
                assert beanB.getMyValue() == null;

                Events.instance().raiseEvent("BeanA.refreshMyValue");

                beanA = (BeanA) Component.getInstance("beanA");
                
                assert "Bar".equals(beanA.getMyValue());        
            }
            
            @Override
            protected void renderResponse() throws Exception
            {
               BeanB beanB = (BeanB) Component.getInstance("beanB");
               assert "Bar".equals(beanB.getMyValue());
            }
        }.run();
    }

}


