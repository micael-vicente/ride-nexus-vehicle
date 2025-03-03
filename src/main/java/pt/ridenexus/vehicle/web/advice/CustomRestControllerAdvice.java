package pt.ridenexus.vehicle.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pt.ridenexus.vehicle.services.exception.EntityExistsException;
import pt.ridenexus.vehicle.services.exception.ObjectNotFoundException;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomRestControllerAdvice {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ProblemDetail objectNotFound(ObjectNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(EntityExistsException.class)
    public ProblemDetail entityAlreadyExists(EntityExistsException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ProblemDetail methodArgumentNotValid(BindException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "request body is invalid");
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse(""),
                (t, t2) -> t));

        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

}
