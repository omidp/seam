package org.jboss.seam.core;

import java.io.Serializable;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * 
 * @author Marek Novotny
 *
 * @param <T> type for validation
 */
public class ClassValidator<T> implements Serializable
{

   private static final long serialVersionUID = -726917267535562335L;

   // default validator from context
   private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

   private final Class<T> classForValidation;

   public ClassValidator(Class<T> clazz)
   {
      this.classForValidation = clazz;
   }

   public Set<ConstraintViolation<T>> getPotentialInvalidValues(String propertyName, Object value)
   {
      return validator.validateValue(classForValidation, propertyName, value);
   }

}