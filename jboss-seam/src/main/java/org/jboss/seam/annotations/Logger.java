//$Id: Logger.java 9621 2008-11-24 17:31:44Z dan.j.allen $
package org.jboss.seam.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Injects a log
 * 
 * @author Gavin King
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface Logger 
{
   /**
    * @return the log category
    */
   String value() default "";
}
