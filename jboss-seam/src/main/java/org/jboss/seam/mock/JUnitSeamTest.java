/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.seam.mock;

import org.junit.After;
import org.junit.Before;

/**
 * Provides BaseSeamTest functionality for Arquillian JUnit integration tests.
 * 
 * @author Gavin King
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @author Mike Youngstrom
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 * @author <a href="http://community.jboss.org/people/maschmid">Marek Schmidt</a>
 */
public class JUnitSeamTest extends AbstractSeamTest
{
   private boolean seamStarted = false;

   
   @Before
   @Override
   public void begin()
   {
      try {
         if (!seamStarted) {
            startSeam();
            setupClass();
            seamStarted = true;
         }
      }
      catch (Exception x) {
         throw new RuntimeException(x);
      }
      super.begin();
   }

   @After
   @Override
   public void end()
   {
      super.end();
   }
   
   /**
    * Call this method within a test method to end the previous
    * mock session and start another one. 
    */
   public void reset()
   {
      end();
      begin();
   }
}
