package com.ctsi.ssdc.exception;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.validation.ConstraintViolationProblem;

import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.LocalMessageSource;

/**
 * Controller advice to translate the server side exceptions to client-friendly
 * json structures. The error response follows RFC7807 - Problem Details for
 * HTTP APIs (https://tools.ietf.org/html/rfc7807)
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling {
	
	@Autowired
	private LocalMessageSource localMessageSource;//国际化

	/**
	 * Post-process Problem payload to add the message key for front-end if needed
	 */
	@Override
	public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
		if (entity == null || entity.getBody() == null) {
			return entity;
		}
		Problem problem = entity.getBody();

		ProblemBuilder builder = Problem.builder()
				.withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE
						: problem.getType())
				.withStatus(problem.getStatus())
				.withTitle(problem.getTitle())
				.withDetail(problem.getDetail())
				.with("path", request.getNativeRequest(HttpServletRequest.class).getRequestURI());

		if (problem instanceof ConstraintViolationProblem) {
			builder.with("violations", ((ConstraintViolationProblem) problem).getViolations())
				.with("message",localMessageSource.getMessage(ErrorConstants.ERR_VALIDATION, null, ErrorConstants.ERR_VALIDATION));
		} else if(problem instanceof DefaultProblem) {
			builder.withCause(((DefaultProblem) problem).getCause())
			.withInstance(problem.getInstance());

			problem.getParameters().forEach(builder::with);
		
			if (!problem.getParameters().containsKey("message") && problem.getStatus() != null) {
				String messageCode = "error.http." + problem.getStatus().getStatusCode();
				builder.with("message", localMessageSource.getMessage(messageCode, null, messageCode));
			}
			
		} else {
			
			if (problem.getParameters().containsKey("message")) {
				String messageCode = problem.getParameters().get("message").toString();
				builder.with("message", localMessageSource.getMessage(messageCode, null, messageCode));
			}
		}
		return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
	}

	@Override
	public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			@Nonnull NativeWebRequest request) {
		BindingResult result = ex.getBindingResult();
		List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
				.map(f -> new FieldErrorVM(f.getObjectName(), f.getField(), f.getCode())).collect(Collectors.toList());

		Problem problem = Problem.builder().withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
				.withTitle("Method argument not valid").withStatus(defaultConstraintViolationStatus())
				.with("message", ErrorConstants.ERR_VALIDATION).with("fieldErrors", fieldErrors).build();
		return create(ex, problem, request);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Problem> handleNoSuchElementException(NoSuchElementException ex, NativeWebRequest request) {
		Problem problem = Problem.builder().withStatus(Status.NOT_FOUND)
				.with("message", ErrorConstants.ENTITY_NOT_FOUND_TYPE).build();
		return create(ex, problem, request);
	}

	@ExceptionHandler(BadRequestAlertException.class)
	public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex,
			NativeWebRequest request) {
		return create(ex, request,
				HeaderUtil.createFailureAlert(ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));

	}

	@ExceptionHandler(ConcurrencyFailureException.class)
	public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
		Problem problem = Problem.builder().withStatus(Status.CONFLICT)
				.with("message", ErrorConstants.ERR_CONCURRENCY_FAILURE).build();
		return create(ex, problem, request);
	}
}
