package com.basoft.core.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.basoft.core.constants.CoreConstants;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=EmailConstraintValid.class)
public @interface EmailConstraint {
    String message() default CoreConstants.E_PARAM_INVALID;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
