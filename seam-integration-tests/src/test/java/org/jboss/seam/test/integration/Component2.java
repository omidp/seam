package org.jboss.seam.test.integration;

public class Component2
{
   
   private String name;
   private String defaultValue = "Component2default";
   
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
