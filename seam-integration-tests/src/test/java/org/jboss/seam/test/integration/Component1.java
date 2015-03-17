package org.jboss.seam.test.integration;


public class Component1
{
   
   private String name;
   private String defaultValue = "Component1default";

   public String getName()
   {
      return name;
   }
   
   public void setName(String name)
   {
      this.name = name;
   }

   public String getDefaultValue()
   {
      return defaultValue;
   }

   public void setDefaultValue(String defaultValue)
   {
      this.defaultValue = defaultValue;
   }
}
