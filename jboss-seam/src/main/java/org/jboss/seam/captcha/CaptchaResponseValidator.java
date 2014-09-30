package org.jboss.seam.captcha;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates that the input entered by the user matches
 * the captcha image.
 * 
 * @author Gavin King
 * @author Marek Novotny
 *
 */
public class CaptchaResponseValidator implements ConstraintValidator<CaptchaResponse,String>
{

   public void initialize(CaptchaResponse constraintAnnotation)   {   }

   public boolean isValid(String value, ConstraintValidatorContext context)
   {
      boolean result = Captcha.instance().validateResponse(value);
      if (!result)
      {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("org.jboss.seam.captcha.error").addConstraintViolation();
      }
      return result;
   }

}
