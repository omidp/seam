package org.jboss.seam.test.integration;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.core.Init;
import org.jboss.seam.mock.JUnitSeamTest;
import org.junit.Test;

public class NamespaceTest 
    extends JUnitSeamTest 
{
    @Test
    public void nameSpaceComponent() 
        throws Exception 
    {
        new ComponentTest() {
            @Override
            protected void testComponents() throws Exception {
                assert getValue("#{namespaceTest.fooFactory}") != null;
            }
        }.run();
    }

    @Test
    public void nameSpaceFactory() 
        throws Exception 
    {
        new ComponentTest() {
            @Override
            protected void testComponents() throws Exception {
                assert getValue("#{namespaceTest.ns1.factory}") != null;
            }
        }.run();
    }
    

    @Test
    public void namespaceOutjection() 
        throws Exception 
    {
        new ComponentTest() {
            @Override
            protected void testComponents() throws Exception {
                FooFactory factory = (FooFactory) getValue("#{namespaceTest.fooFactory}");
                factory.someMethod();
                assert getValue("#{namespaceTest.ns2.outject}") != null;
            }
        }.run();
    }

    
    @Test
    public void factoryMethodExpression() 
        throws Exception 
    {
        new ComponentTest() {
            @Override
            protected void testComponents() throws Exception {
                Init init = Init.instance();
                init.addFactoryMethodExpression("namespaceTest.ns3.factory", "#{namespaceTest.fooFactory.createFoo}", ScopeType.SESSION);
                
                assert getValue("#{namespaceTest.ns3.factory}") != null;
            }
        }.run();
    }
    
    @Test
    public void factoryValueExpression() 
        throws Exception 
    {
        new ComponentTest() {
            @Override
            protected void testComponents() throws Exception {
                Init init = Init.instance();
                init.addFactoryValueExpression("namespaceTest.ns4.factory", "#{namespaceTest.fooFactory.createFoo()}", ScopeType.SESSION);
                
                assert getValue("#{namespaceTest.ns4.factory}") != null;
            }
        }.run();
    }
    
    @Name("namespaceTest.fooFactory")
    public static class FooFactory {
        public class Foo {}
        
        @Factory("namespaceTest.ns1.factory")
        public Foo createFoo() {
            return new Foo();
        }        

        @Out("namespaceTest.ns2.outject")
        public Foo outjectFoo() {
            return new Foo();
        }
        
        public void someMethod() {
        }
    }
}
