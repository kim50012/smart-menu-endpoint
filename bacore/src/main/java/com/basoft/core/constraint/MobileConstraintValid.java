package com.basoft.core.constraint;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobileConstraintValid implements ConstraintValidator<MobileConstraint,Object> {

    public static final String REGEX_MOBILE_EXACT = "^1[3|4|5|7|8][0-9]{9}$";//验证是具体的11位的手机号码


    @Override
    public void initialize(MobileConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(StringUtils.isBlank(String.valueOf(value))){
            return false;
        }
        if(String.valueOf(value).matches(REGEX_MOBILE_EXACT)){
            return true;
        }
        return false;
    }
}
