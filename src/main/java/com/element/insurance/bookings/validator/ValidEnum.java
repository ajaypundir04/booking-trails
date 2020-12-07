package com.element.insurance.bookings.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD,  PARAMETER})
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidEnum {
    String message() default "enum not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
