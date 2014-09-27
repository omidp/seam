//$Id: MessageManager.java 1584 2006-05-23 05:30:24Z gavin $
package org.jboss.seam.example.messages;

import javax.ejb.Local;

@Local
public interface MessageManager
{
   public void findMessages();
   public void select();
   public void delete();
   public void destroy();
}