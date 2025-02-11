package pt.ridenexus.vehicle.web.validation.annotation;

public class CustomValidationException extends RuntimeException {
    public CustomValidationException(String message) {
        super(message);
    }
}
