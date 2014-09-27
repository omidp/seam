//$Id: Register.java 6826 2007-11-25 13:12:07Z pmuir $
package org.jboss.seam.example.booking;

import javax.ejb.Local;

@Local
public interface Register
{
   public void register();
   public void invalid();
   public String getVerify();
   public void setVerify(String verify);
   public boolean isRegistered();
   
   public void destroy();
}