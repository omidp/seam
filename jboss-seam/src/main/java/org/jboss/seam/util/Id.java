//$Id: Id.java 6840 2007-11-28 19:14:03Z nrichards $
package org.jboss.seam.util;

import org.jboss.seam.core.ConversationIdGenerator;

public class Id
{
    @Deprecated
    public static String nextId() {
        return ConversationIdGenerator.instance().getNextId();
    }  
}
