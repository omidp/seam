//$Id: IteratorEnumeration.java 5629 2007-06-29 00:41:37Z gavin $
package org.jboss.seam.util;

import java.util.Enumeration;
import java.util.Iterator;

public class IteratorEnumeration implements Enumeration
{
   
   private Iterator iterator;
   
   public IteratorEnumeration(Iterator iterator)
   {
      this.iterator = iterator;
   }

   public boolean hasMoreElements()
   {
      return iterator.hasNext();
   }

   public Object nextElement()
   {
      return iterator.next();
   }

}
