package com.basoft.core.constraint;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailConstraintValid implements ConstraintValidator<EmailConstraint, Object> {

    public static final String REGEX_EMAIL_EXACT = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$";

    @Override
    public void initialize(EmailConstraint emailConstraint) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(StringUtils.isBlank(String.valueOf(value))){
            return false;
        }
        if(String.valueOf(value).matches(REGEX_EMAIL_EXACT)){
            return true;
        }
        return false;
    }
}
