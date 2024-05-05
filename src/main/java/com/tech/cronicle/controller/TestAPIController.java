package com.tech.cronicle.controller;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tech.cronicle.constants.ErrorCodes;
import com.tech.cronicle.exception.FinalException;
import com.tech.cronicle.requests.TestAPIRequest;
import com.tech.cronicle.response.TestAPIResponse;
import com.tech.cronicle.util.LoggerWrapper;
import com.tech.cronicle.util.ResponseGeneratorUtil;

@RestController
public class TestAPIController {
	private static final LoggerWrapper LOGGER = LoggerWrapper.getLogger(TestAPIController.class);

	@GetMapping(path = "/get-data/success")
	public ResponseEntity<Object> getData() throws FinalException {
		try {
			int randomInt = new Random().nextInt(10) + 1;
			Thread.sleep(randomInt * 1000);
		} catch (Exception e) {

		}

		TestAPIResponse response = new TestAPIResponse();
		response.setStatus("success");
		return new ResponseEntity<>(ResponseGeneratorUtil.okResponse(response), HttpStatus.OK);
	}

	@GetMapping(path = "/get-data/fail")
	public ResponseEntity<Object> getDataFail() throws FinalException {
		try {
			int randomInt = new Random().nextInt(10) + 1;
			Thread.sleep(randomInt * 1000);
		} catch (Exception e) {

		}
		throw new FinalException(ErrorCodes.GENERIC_ERROR, ErrorCodes.GENERIC_ERROR_MESSAGE);
	}

	@PostMapping(path = "/post-data/success")
	public ResponseEntity<Object> postDataSuccess(@RequestBody TestAPIRequest jobRequest) throws FinalException {
		try {

			int randomInt = new Random().nextInt(10) + 1;
			Thread.sleep(randomInt * 1000);
			LOGGER.info("Request data " + jobRequest);
		} catch (Exception e) {

		}
		TestAPIResponse response = new TestAPIResponse();
		response.setStatus("success");
		return new ResponseEntity<>(ResponseGeneratorUtil.okResponse(response), HttpStatus.OK);
	}

	@PostMapping(path = "/post-data/fail")
	public ResponseEntity<Object> insertJob(@RequestBody TestAPIRequest jobRequest) throws FinalException {
		try {

			int randomInt = new Random().nextInt(10) + 1;
			Thread.sleep(randomInt * 1000);
			LOGGER.info("Request data " + jobRequest);
		} catch (Exception e) {

		}
		throw new FinalException(ErrorCodes.GENERIC_ERROR, ErrorCodes.GENERIC_ERROR_MESSAGE);
	}
}
