package org.jboss.seam.test.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.bpm.CreateProcess;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

//@Ignore
@RunWith(Arquillian.class)
public class BusinessProcessTest 
    extends JUnitSeamTest
{
	@Deployment(name="BusinessProcessTest")
	@OverProtocol("Servlet 3.0") 
	public static Archive<?> createDeployment()
	{
		return Deployments.jbpmSeamDeployment().addClasses(ProcessComponent.class);
	}
	
    @Test
    public void noProcessDefinition() 
        throws Exception 
    {
        new FacesRequest() {
            @Override
            protected void invokeApplication() throws Exception {
                try {
                    invokeAction("#{bpmTest.startInvalid}");
                    assert false;
                } catch (Exception e) {
                    // expected
                }
            }

        }.run();
    }
    
    @Test
    public void noVariableStart() 
        throws Exception 
    {
        new FacesRequest() {
            @Override
            protected void invokeApplication() throws Exception {
                try {
                    invokeAction("#{bpmTest.startOne}");
                } catch (Exception e) {
                	e.printStackTrace();
                    assert false;
                }
            }

        }.run();
    }
    
    
    @Name("bpmTest")
    static public class ProcessComponent {
        @CreateProcess(definition="NoSuchProcess") 
        public void startInvalid() {
        }
        
        @CreateProcess(definition="TestProcess1") 
        public void startOne() {            
        }
    }
}
