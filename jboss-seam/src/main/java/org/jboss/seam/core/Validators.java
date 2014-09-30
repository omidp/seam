package org.jboss.seam.core;

import static org.jboss.seam.annotations.Install.BUILT_IN;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.el.ValueExpression;
import javax.validation.ConstraintViolation;

import org.jboss.seam.Component;
import org.jboss.seam.Instance;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.el.EL;

/**
 * Caches instances of Hibernate Validator ClassValidator
 * 
 * @author Gavin King
 * 
 */
@Name("org.jboss.seam.core.validators")
@BypassInterceptors
@Scope(ScopeType.APPLICATION)
@Install(precedence = BUILT_IN, classDependencies = "org.jboss.seam.core.ClassValidator")
public class Validators
{

  
   /**
    * Get the cached ClassValidator instance. If the argument is an instance of
    * a session bean Seam component instance, the returned validator will be
    * aware of constraints defined on the bean class. Therefore this method is
    * preferred to getValidator(Class) if the argument might be a session bean.
    * 
    * @param model the object to be validated
    */
   @SuppressWarnings("unchecked")
   public <T> ClassValidator<T> getValidator(T model)
   {
      Class<?> modelClass = model instanceof Instance ? ((Instance) model).getComponent().getBeanClass() : model.getClass();
      return getValidator((Class<T>) modelClass);
   }

   /**
    * Get the cached ClassValidator instance.
    * 
    * @param modelClass the class to be validated
    */
   public <T> ClassValidator<T> getValidator(Class<T> modelClass)
   {
      return createValidator(modelClass);
   }

   /**
    * Create a new ClassValidator for the given class, using the current Seam
    * ResourceBundle.
    * 
    * @param modelClass the class to be validated
    */
   protected <T> ClassValidator<T> createValidator(Class<T> modelClass)
   {
      return new ClassValidator<T>(modelClass);
   }

   /**
    * Validate that the given value can be assigned to the property given by the
    * value expression.
    * 
    * @param valueExpression a value expression, referring to a property
    * @param elContext the ELContext in which to evaluate the expression
    * @param value a value to be assigned to the property
    * @return a set of potential InvalidValues, from Hibernate Validator
    */
   public Set<ConstraintViolation<Object>> validate(ValueExpression valueExpression, ELContext elContext, Object value)
   {
      ValidatingResolver validatingResolver = new ValidatingResolver(elContext.getELResolver());
      ELContext decoratedContext = EL.createELContext(elContext, validatingResolver);
      valueExpression.setValue(decoratedContext, value);
      return validatingResolver.getInvalidValues();
   }

   class ValidatingResolver extends ELResolver
   {
      private ELResolver delegate;
      private Set<ConstraintViolation<Object>> invalidValues;

      public ValidatingResolver(ELResolver delegate)
      {
         this.delegate = delegate;
      }

      public Set<ConstraintViolation<Object>> getInvalidValues()
      {
         return invalidValues;
      }

      @Override
      public Class<?> getCommonPropertyType(ELContext context, Object value)
      {
         return delegate.getCommonPropertyType(context, value);
      }

      @Override
      public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object value)
      {
         return delegate.getFeatureDescriptors(context, value);
      }

      @Override
      public Class<?> getType(ELContext context, Object x, Object y) throws NullPointerException, PropertyNotFoundException, ELException
      {
         return delegate.getType(context, x, y);
      }

      @Override
      public Object getValue(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException
      {
         return delegate.getValue(context, base, property);
      }

      @Override
      public boolean isReadOnly(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException
      {
         return delegate.isReadOnly(context, base, property);
      }

      @Override
      public void setValue(ELContext context, Object base, Object property, Object value) throws NullPointerException, PropertyNotFoundException, PropertyNotWritableException, ELException
      {
         if (base != null && property != null)
         {
            context.setPropertyResolved(true);
            invalidValues = getValidator(base).getPotentialInvalidValues(property.toString(), value);
         }

      }

   }

   public static Validators instance()
   {
      if (!Contexts.isApplicationContextActive())
      {
         throw new IllegalStateException("No active application scope");
      }
      return (Validators) Component.getInstance(Validators.class, ScopeType.APPLICATION);
   }

}