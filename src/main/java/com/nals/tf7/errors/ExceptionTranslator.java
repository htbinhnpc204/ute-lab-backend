package com.nals.tf7.errors;

import com.nals.tf7.config.ApplicationProperties;
import com.nals.tf7.errors.annotation.ErrorMapping;
import com.nals.tf7.errors.annotation.ErrorMappings;
import com.nals.tf7.helpers.StringHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.violations.ConstraintViolationProblem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nals.tf7.errors.ErrorCodes.BAD_REQUEST;
import static com.nals.tf7.errors.ErrorCodes.FORBIDDEN;
import static com.nals.tf7.errors.ErrorCodes.INTERNAL_SERVER;
import static com.nals.tf7.errors.ErrorCodes.METHOD_NOT_ALLOWED;
import static com.nals.tf7.errors.ErrorCodes.NOT_ACCEPTABLE;
import static com.nals.tf7.errors.ErrorCodes.NOT_FOUND;
import static com.nals.tf7.errors.ErrorCodes.NOT_IMPLEMENTED;
import static com.nals.tf7.errors.ErrorCodes.SECURITY;
import static com.nals.tf7.errors.ErrorCodes.UNAUTHORIZED;
import static com.nals.tf7.errors.ErrorCodes.UNSUPPORTED_MEDIA_TYPE;
import static com.nals.tf7.errors.ErrorType.RW_CONCURRENCY_FAILURE;
import static com.nals.tf7.errors.ErrorType.RW_HTTP;
import static com.nals.tf7.errors.ErrorType.RW_NOT_FOUND;
import static com.nals.tf7.errors.ErrorType.RW_SECURITY;
import static com.nals.tf7.errors.ErrorType.RW_VALIDATION;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionTranslator
    implements ProblemHandling {

    private static final String REQUEST_ID_ATTRIBUTE_KEY = "requestId";

    private final ApplicationProperties applicationProperties;

    @Override
    public ResponseEntity<Problem> process(@Nullable final ResponseEntity<Problem> entity,
                                           @Nonnull final NativeWebRequest request) {
        if (entity == null || entity.getBody() == null) {
            return entity;
        }

        Problem problem = entity.getBody();
        ResponseEntity<Problem> response;
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            response = entity;
        } else {
            problem = buildProblem(entity, request, problem);
            response = new ResponseEntity<>(problem, entity.getHeaders(), entity.getStatusCode());
        }

        return response;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Problem> handleForbiddenFailure(final AccessDeniedException ex,
                                                          final NativeWebRequest request) {

        Problem problem = Problem.builder()
                                 .withType(RW_SECURITY.getType())
                                 .withStatus(Status.FORBIDDEN)
                                 .with(ProblemKey.MESSAGE, RW_SECURITY.getMessage())
                                 .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler(ConcurrencyFailureException.class)
    public ResponseEntity<Problem> handleConcurrencyFailure(final ConcurrencyFailureException ex,
                                                            final NativeWebRequest request) {
        Problem problem = Problem.builder()
                                 .withType(RW_CONCURRENCY_FAILURE.getType())
                                 .withStatus(Status.CONFLICT)
                                 .with(ProblemKey.MESSAGE, RW_CONCURRENCY_FAILURE.getMessage())
                                 .build();
        return create(ex, problem, request);
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                final @Nonnull NativeWebRequest request) {
        var result = ex.getBindingResult();
        Map<String, String> errorCodeMap = new HashMap<>();

        Optional.ofNullable(result.getTarget()).ifPresent(target -> {
            for (Field field : target.getClass().getDeclaredFields()) {
                var errorMapping = field.getAnnotation(ErrorMapping.class);
                if (Objects.nonNull(errorMapping)) {
                    errorCodeMap.put(getKey(field, errorMapping), errorMapping.code());
                    continue;
                }

                var errorMappings = field.getAnnotation(ErrorMappings.class);
                if (Objects.nonNull(errorMappings)) {
                    for (ErrorMapping errMapping : errorMappings.value()) {
                        errorCodeMap.put(getKey(field, errMapping), errMapping.code());
                    }
                }
            }
        });

        var errors = result.getFieldErrors().stream().map(fieldError -> {
            var error = new ErrorProblem();
            error.setField(fieldError.getField());
            error.setMessage(getMessage(RW_VALIDATION, fieldError.getField(), fieldError.getCode()));
            String key = String.format("%s-%s", error.getField(), fieldError.getCode());
            error.setErrorCode(errorCodeMap.getOrDefault(key, BAD_REQUEST));
            return error;
        }).collect(Collectors.toList());

        Problem problem = Problem.builder()
                                 .withType(RW_VALIDATION.getType())
                                 .withStatus(defaultConstraintViolationStatus())
                                 .with(ProblemKey.MESSAGE, RW_VALIDATION.getMessage())
                                 .with(ProblemKey.ERROR_CODE, BAD_REQUEST)
                                 .with(ProblemKey.ERRORS, errors)
                                 .build();

        return create(ex, problem, request);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Problem> handleSecurity(final SecurityException ex,
                                                  final NativeWebRequest request) {
        Problem problem = Problem.builder()
                                 .withType(RW_SECURITY.getType())
                                 .withStatus(defaultConstraintViolationStatus())
                                 .with(ProblemKey.MESSAGE, getMessage(RW_SECURITY, ex.getMessage()))
                                 .with(ProblemKey.ERROR_CODE, SECURITY)
                                 .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Problem> handleAuthentication(final AuthenticationException ex,
                                                        final NativeWebRequest request) {

        var error = new ErrorProblem();
        error.setMessage(ex.getMessage());
        error.setErrorCode(UNAUTHORIZED);

        Problem problem = Problem.builder()
                                 .withType(RW_SECURITY.getType())
                                 .withStatus(Status.UNAUTHORIZED)
                                 .with(ProblemKey.MESSAGE, RW_SECURITY.getMessage())
                                 .with(ProblemKey.ERROR_CODE, UNAUTHORIZED)
                                 .with(ProblemKey.ERRORS, List.of(error))
                                 .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler(ValidatorException.class)
    public ResponseEntity<Problem> handleValidator(final ValidatorException ex,
                                                   final NativeWebRequest request) {
        Problem problem = Problem.builder()
                                 .withType(RW_VALIDATION.getType())
                                 .withStatus(defaultConstraintViolationStatus())
                                 .with(ProblemKey.MESSAGE, ex.getMessage())
                                 .with(ProblemKey.ERROR_CODE, BAD_REQUEST)
                                 .with(ProblemKey.ERRORS, ex.getErrors())
                                 .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Problem> handleObjectNotFound(final NotFoundException ex,
                                                        final NativeWebRequest request) {
        Problem problem = Problem.builder()
                                 .withType(RW_NOT_FOUND.getType())
                                 .withStatus(Status.NOT_FOUND)
                                 .with(ProblemKey.MESSAGE, ex.getMessage())
                                 .with(ProblemKey.ERROR_CODE, NOT_FOUND)
                                 .with(ProblemKey.ERRORS, ex.getErrors())
                                 .build();
        return create(ex, problem, request);
    }

    private String getMessage(final ErrorType errorType, final String message) {
        return MessageFormat.format("{0}.{1}",
                                    errorType.getMessage(),
                                    StringHelper.uppercaseToUnderscore(message));
    }

    private String getMessage(final ErrorType errorType, final String fieldName, final String message) {
        return MessageFormat.format("{0}.{1}.{2}",
                                    errorType.getMessage(),
                                    StringHelper.uppercaseToUnderscore(fieldName),
                                    StringHelper.uppercaseToUnderscore(message));
    }

    private String getKey(final Field field, final ErrorMapping errorMapping) {
        return String.format("%s-%s",
                             field.getName(),
                             errorMapping.value().getSimpleName());
    }

    private Problem buildProblem(final ResponseEntity<Problem> entity,
                                 final NativeWebRequest request,
                                 final Problem problem) {

        ProblemBuilder builder = Problem
            .builder()
            .withType(problem.getType())
            .withStatus(problem.getStatus())
            .withTitle(problem.getTitle())
            .with(ProblemKey.PATH, Objects.requireNonNull(request.getNativeRequest(HttpServletRequest.class))
                                          .getRequestURI());

        if (problem instanceof ConstraintViolationProblem) {
            builder.with(ProblemKey.VIOLATIONS, ((ConstraintViolationProblem) problem).getViolations())
                   .with(ProblemKey.MESSAGE, RW_VALIDATION.getMessage());

            if (problem.getParameters().containsKey(ProblemKey.ERROR_CODE)) {
                builder.with(ProblemKey.ERROR_CODE, problem.getParameters().get(ProblemKey.ERROR_CODE));
            }
        } else {
            builder.withCause(((DefaultProblem) problem).getCause())
                   .withDetail(problem.getDetail())
                   .withInstance(problem.getInstance());

            problem.getParameters().forEach(builder::with);

            if (!problem.getParameters().containsKey(ProblemKey.MESSAGE) && Objects.nonNull(problem.getStatus())) {
                builder.with(ProblemKey.MESSAGE, MessageFormat.format("{0}.{1}",
                                                                      RW_HTTP.getMessage(),
                                                                      problem.getStatus().getStatusCode()));
            }

            if (!problem.getParameters().containsKey(ProblemKey.ERROR_CODE)) {
                switch (entity.getStatusCode()) {
                    case BAD_REQUEST:
                        builder.with(ProblemKey.ERROR_CODE, BAD_REQUEST);
                        break;
                    case UNAUTHORIZED:
                        builder.with(ProblemKey.ERROR_CODE, UNAUTHORIZED);
                        break;
                    case FORBIDDEN:
                        builder.with(ProblemKey.ERROR_CODE, FORBIDDEN);
                        break;
                    case NOT_FOUND:
                        builder.with(ProblemKey.ERROR_CODE, NOT_FOUND);
                        break;
                    case METHOD_NOT_ALLOWED:
                        builder.with(ProblemKey.ERROR_CODE, METHOD_NOT_ALLOWED);
                        break;
                    case NOT_ACCEPTABLE:
                        builder.with(ProblemKey.ERROR_CODE, NOT_ACCEPTABLE);
                        break;
                    case UNSUPPORTED_MEDIA_TYPE:
                        builder.with(ProblemKey.ERROR_CODE, UNSUPPORTED_MEDIA_TYPE);
                        break;
                    case NOT_IMPLEMENTED:
                        builder.with(ProblemKey.ERROR_CODE, NOT_IMPLEMENTED);
                        break;
                    default:
                        builder.with(ProblemKey.ERROR_CODE, INTERNAL_SERVER)
                               .withDetail("Internal Server Error");
                }
            }
        }

        String requestId = (String) request.getAttribute(REQUEST_ID_ATTRIBUTE_KEY, SCOPE_REQUEST);
        if (Optional.ofNullable(requestId).isPresent()) {
            builder.with(ProblemKey.REQUEST_ID, requestId);
        }

        return builder.build();
    }

    public static class ProblemKey {
        public static final String PATH = "path";
        public static final String MESSAGE = "message";
        public static final String VIOLATIONS = "violations";
        public static final String REQUEST_ID = "request_id";
        public static final String ERRORS = "errors";
        public static final String ERROR_CODE = "error_code";
    }
}
