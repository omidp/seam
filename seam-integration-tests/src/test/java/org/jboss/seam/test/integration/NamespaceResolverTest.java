package org.jboss.seam.test.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.init.NamespacePackageResolver;
import org.jboss.seam.mock.JUnitSeamTest;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.junit.Test;

@RunWith(Arquillian.class)
public class NamespaceResolverTest 
    extends JUnitSeamTest
{
	@Deployment(name="NamespaceResolverTest")
    @OverProtocol("Servlet 3.0") 
    public static Archive<?> createDeployment()
    {
        return Deployments.defaultSeamDeployment();
    }
	
	NamespacePackageResolver resolver = new NamespacePackageResolver();

	@Test
	public void testResolver() {
		
		test("java:foo", "foo");
		test("java:com.company.department",
		     "com.company.department");
		test("java:com.company.department.product", 
		     "com.company.department.product");
		test("http://www.company.com/department/product",
		     "com.company.department.product");
		test("https://my-company.com/department/product",
			 "com.my_company.department.product");
		test("http://ericjung:password@www.company.com:8080/foo/bar/baz#anchor?param1=332&param2=334",
			 "com.company.foo.bar.baz");
		test("http://cats.import.com",
             "com.import.cats");
		
		
		//testFail("http://bar#foo#com");
		
		testFail("java:");
		
		// need to think about this one
		//testFail("java:foo!bar");

		testFail("mailto:java-net@java.sun.com");
		testFail("news:comp.lang.java");
		testFail("urn:isbn:096139210x");
		
	}

	private void test(String namespace, String packageName) {
		Assert.assertEquals(packageName, resolver.resolve(namespace));
	}
	
	private void testFail(String namespace) {
		Assert.assertNull(namespace, resolver.resolve(namespace));
	}
}
