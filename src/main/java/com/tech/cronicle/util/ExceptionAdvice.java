package com.tech.cronicle.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tech.cronicle.exception.FinalException;

@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

	private final static LoggerWrapper LOG = LoggerWrapper.getLogger(ExceptionAdvice.class);

	@ExceptionHandler(value = { RuntimeException.class })
	protected ResponseEntity<Object> genericRuntimeExceptionHandler(RuntimeException ex, WebRequest request) {
		LOG.error("Runtime Exception: " + ex);
		return new ResponseEntity<Object>(ResponseGeneratorUtil.genericErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { FinalException.class })
	protected ResponseEntity<Object> finalExceptionHandler(FinalException ex, WebRequest request) {
		LOG.error("Final Exception: " + ex);
		return new ResponseEntity<Object>(ResponseGeneratorUtil.genericErrorResponse(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<Object> genericExceptionHandler(Exception ex, WebRequest request) {
		LOG.error("Generic Exception: " + ex);
		return new ResponseEntity<Object>(ResponseGeneratorUtil.genericErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
