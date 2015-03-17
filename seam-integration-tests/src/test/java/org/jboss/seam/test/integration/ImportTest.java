package org.jboss.seam.test.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Import;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ImportTest
    extends JUnitSeamTest
{
	@Deployment(name="IdentifierTest")
	@OverProtocol("Servlet 3.0")
	public static Archive<?> createDeployment()
	{
		return Deployments.defaultSeamDeployment()
				.addClasses(Importer.class);
	}

    @Test
    public void testImport() 
        throws Exception 
    {        
        new FacesRequest() {
            @Override
            protected void invokeApplication()
                throws Exception
            {
                assert getValue("#{importTest.otherValue}").equals("foobar2");
            }        
        }.run();
    }


    @Name("importTest")
    @Import("importTest.ns2")
    public static class Importer {
        @In
        String otherValue;

        public String getOtherValue() {
            return otherValue;
        }

        @Factory(value="importTest.ns2.otherValue", autoCreate=true)
        public String createOtherValue() {
            return "foobar2";
        }
    }
}
