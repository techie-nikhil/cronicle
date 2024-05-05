package com.tech.cronicle.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.cronicle.exception.FinalException;
import com.tech.cronicle.util.LoggerWrapper;
import com.tech.cronicle.util.ResponseGeneratorUtil;

@RestController
public class StatusController extends AbstractErrorController {
	private static final LoggerWrapper LOGGER = LoggerWrapper.getLogger(StatusController.class);

	@GetMapping(path = "/ping")
	public ResponseEntity<Object> ping() throws FinalException {
		return new ResponseEntity<>(ResponseGeneratorUtil.okResponse("Pong"), HttpStatus.OK);
	}

	@Override
	public String getErrorPath() {
		return null;
	}

	public StatusController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	@RequestMapping(path = "/error")
	public ResponseEntity<Object> error(HttpServletRequest request) {
		HttpStatus status = getStatus(request);
		ResponseEntity<Object> response;
		if (status == HttpStatus.NO_CONTENT) {
			response = new ResponseEntity<>(status);
		} else if (status == HttpStatus.NOT_FOUND) {
			response = new ResponseEntity<>(ResponseGeneratorUtil.genericNotFoundResponse(), status);
		} else {
			response = new ResponseEntity<>(ResponseGeneratorUtil.genericErrorResponse(), status);
		}
		LOGGER.error("Response " + request.getRequestURI() + " " + response.toString());
		return response;
	}

}
