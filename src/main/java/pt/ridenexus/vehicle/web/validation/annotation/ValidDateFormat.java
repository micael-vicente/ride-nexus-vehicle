package pt.ridenexus.vehicle.web.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.Getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDateFormat.ValidDateFormatValidator.class)
public @interface ValidDateFormat {
    String message() default "Invalid format for date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String format() default "yyyy-MM-dd";

    @Getter
    class ValidDateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {

        private DateTimeFormatter format;

        @Override
        public void initialize(ValidDateFormat constraintAnnotation) {
            try {
                this.format = DateTimeFormatter.ofPattern(constraintAnnotation.format());
                ConstraintValidator.super.initialize(constraintAnnotation);
            } catch (IllegalArgumentException e) {
                throw new CustomValidationException("Unsupported date format used");
            }
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if(value == null) {
                return true;
            }

            try {
                LocalDate.parse(value, getFormat());
            } catch (DateTimeParseException e) {
                return false;
            }

            return true;
        }
    }
}
