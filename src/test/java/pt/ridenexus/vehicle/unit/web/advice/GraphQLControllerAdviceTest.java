package pt.ridenexus.vehicle.unit.web.advice;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindException;
import pt.ridenexus.vehicle.web.advice.GraphQLControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

class GraphQLControllerAdviceTest {

    private final GraphQLControllerAdvice advice = new GraphQLControllerAdvice();

    @Test
    void whenConstraintViolationExceptionThenValidationError() {
        ConstraintViolationException ex = new ConstraintViolationException(Set.of());
        DataFetchingEnvironment env = DataFetchingEnvironmentImpl.newDataFetchingEnvironment().build();
        Mono<List<GraphQLError>> listMono = advice.resolveException(ex, env);

        Assertions.assertNotNull(listMono);

        List<GraphQLError> block = listMono.block();

        Assertions.assertNotNull(block);
        Assertions.assertEquals(1, block.size());

        GraphQLError error = block.getFirst();

        Assertions.assertEquals(ErrorType.ValidationError, error.getErrorType());
    }

    @Test
    void whenNoneOfHandledThenDefaultAndExecutionAborted() {
        String errorMessage = "test error";
        IllegalArgumentException ex = new IllegalArgumentException(errorMessage);
        DataFetchingEnvironment env = DataFetchingEnvironmentImpl.newDataFetchingEnvironment().build();
        Mono<List<GraphQLError>> listMono = advice.resolveException(ex, env);

        Assertions.assertNotNull(listMono);

        List<GraphQLError> block = listMono.block();

        Assertions.assertNotNull(block);
        Assertions.assertEquals(1, block.size());

        GraphQLError error = block.getFirst();

        Assertions.assertEquals(ErrorType.ExecutionAborted, error.getErrorType());
        Assertions.assertEquals(errorMessage, error.getMessage());
    }

    @Test
    void whenBindExceptionThenValidationError() {
        String errorMessage = "Invalid argument provided";
        BindException ex = new BindException(new Object(), errorMessage);
        DataFetchingEnvironment env = DataFetchingEnvironmentImpl.newDataFetchingEnvironment().build();
        Mono<List<GraphQLError>> listMono = advice.resolveException(ex, env);

        Assertions.assertNotNull(listMono);

        List<GraphQLError> block = listMono.block();

        Assertions.assertNotNull(block);
        Assertions.assertEquals(1, block.size());

        GraphQLError error = block.getFirst();

        Assertions.assertEquals(ErrorType.ValidationError, error.getErrorType());
        Assertions.assertEquals(errorMessage, error.getMessage());
    }
}