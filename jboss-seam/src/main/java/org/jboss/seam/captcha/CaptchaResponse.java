package org.jboss.seam.captcha;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
@Constraint(validatedBy=CaptchaResponseValidator.class)
public @interface CaptchaResponse 
{
   String message() default "incorrect response";
   
   Class<?>[] groups() default {};
   
   Class<?>[] payload() default {};
}