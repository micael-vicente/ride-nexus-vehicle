package pt.ridenexus.vehicle.web.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;
import java.util.Set;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsKnownCountry.IsKnownCountryValidator.class)
public @interface IsKnownCountry {
    String message() default "Not a valid/registered country. Must be uppercase.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class IsKnownCountryValidator implements ConstraintValidator<IsKnownCountry, String> {

        private static final Set<String> ISO_COUNTRIES = Set.of(Locale.getISOCountries());

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value == null || ISO_COUNTRIES.contains(value);
        }
    }
}
