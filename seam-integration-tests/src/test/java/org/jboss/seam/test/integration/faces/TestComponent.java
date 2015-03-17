package org.jboss.seam.test.integration.faces;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;

/**
 * Test Seam component for ViewUrlBuilderTest and s:link rewrite feature 
 * @author mnovotny
 *
 */
@Name("testslink")
public class TestComponent
{
   @In(required=false) @Out(required=false) String input;
   
   public String getInput()
   {
      return input;
   }
   
   public void setInput(String input)
   {
      this.input = input;
   }
  
  
}
