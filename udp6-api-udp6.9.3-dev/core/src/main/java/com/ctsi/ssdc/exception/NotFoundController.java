package com.ctsi.ssdc.exception;

import com.ctsi.ssdc.util.LocalMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
/**
 * @author lym
 * 设置spring.resources.add-mappings:false会导致静态资源无法访问
 * 所以自定义实现ErrorController，返回not found信息
 */
@RestController
public class NotFoundController implements ErrorController {
	
	private static final String PATH = "/error";

	@Autowired
	private ErrorAttributes errorAttributes;
	
	@Autowired
	private LocalMessageSource localMessageSource;
	
	@Override
	public String getErrorPath() {
		return PATH;
	}

	@RequestMapping(value = PATH, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Problem> error(HttpServletResponse response, HttpServletRequest request) throws NoHandlerFoundException {
		Map<String , Object > attributes = getErrorAttributes(request, false);
		String messageCode = "error.http."+attributes.get("status").toString();
		ProblemBuilder builder = Problem.builder()
				.withStatus(Status.NOT_FOUND)
				.withTitle(attributes.get("error").toString())
				.withType(ErrorConstants.DEFAULT_TYPE)
				.withDetail("No handler found for "+attributes.get("path").toString())
				.with("path", attributes.get("path").toString())
				.with("message", localMessageSource.getMessage(messageCode, null, messageCode) );
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(builder.build());
	}
	
	private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace){
		ServletWebRequest servletWebRequest = new ServletWebRequest(request);
		return errorAttributes.getErrorAttributes(servletWebRequest, includeStackTrace);
	}
}