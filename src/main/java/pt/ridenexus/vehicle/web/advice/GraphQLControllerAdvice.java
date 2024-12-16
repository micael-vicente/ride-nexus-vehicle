package pt.ridenexus.vehicle.web.advice;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pt.ridenexus.vehicle.services.exception.VehicleExistsException;

@RestControllerAdvice
public class GraphQLControllerAdvice extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {
        logger.warn("Exception handled by controller", ex);

        return switch (ex) {
            case ConstraintViolationException cause -> toGraphQLError(ErrorType.ValidationError, cause.getMessage());
            case BindException ignored -> toGraphQLError(ErrorType.ValidationError, "Invalid argument provided");
            case VehicleExistsException cause -> toGraphQLError(ErrorType.ExecutionAborted, cause.getMessage());
            default -> toGraphQLError(ErrorType.ExecutionAborted, ex.getMessage());
        };
    }

    private GraphQLError toGraphQLError(ErrorType errorType, String message) {
        return GraphQLError.newError()
                .errorType(errorType)
                .message(message)
                .build();
    }
}
