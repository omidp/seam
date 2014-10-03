package org.jboss.seam.wicket;

import java.util.Set;

import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.jboss.seam.core.ClassValidator;
import org.jboss.seam.core.Validators;
import javax.validation.ConstraintViolation;


/**
 * 
 * Allows Hibernate Model Validation to be used in Wicket
 * 
 * @author Pete Muir
 *
 */
public class ModelValidator implements IValidator
{

   private Class<?> clazz;
   private String property;

   /**
    * Create a ModelValidator which will validate the specified property
    */
   public ModelValidator(Class clazz, String property)
   {
      this.clazz = clazz;
      this.property = property;
   }
   
   /**
    * Create a model validator that will validate the property specified by the
    * PropertyModel
    */
   public ModelValidator(PropertyModel propertyModel)
   {
      this(propertyModel.getTarget().getClass(), propertyModel.getPropertyExpression());
   }
   
   
   /**
    * Do the validation, normally called by Wicket
    */
   public void validate(IValidatable validatable)
   {
      ClassValidator<?> classValidator = Validators.instance().getValidator(clazz);
      Set<?> invalidValues = classValidator.getPotentialInvalidValues(property, validatable.getValue());
      
      if (invalidValues.size() > 0)
      {
         ConstraintViolation constraintViolation = (ConstraintViolation) invalidValues.iterator().next();
         String message =   constraintViolation.getMessage();
         IValidationError validationError = new ValidationError().setMessage(message);
         validatable.error(validationError);
      }
   }

}