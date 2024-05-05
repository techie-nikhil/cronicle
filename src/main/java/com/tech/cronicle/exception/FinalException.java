package com.tech.cronicle.exception;

import com.tech.cronicle.constants.ErrorCodes;
import com.tech.cronicle.response.FinalResponse;

public class FinalException extends Exception {
	private static final long serialVersionUID = 1L;
	private FinalResponse errorResponse;

	public FinalException(int code, String message) {
		super(message);
		if (errorResponse == null) {
			errorResponse = new FinalResponse();
		}
		errorResponse.setCode(code);
		errorResponse.setMessage(message);
	}

	@Override
	public String getMessage() {
		if (errorResponse != null) {
			return errorResponse.getMessage();
		}
		return super.getMessage();
	}

	public int getCode() {
		if (errorResponse != null) {
			return errorResponse.getCode();
		}
		return ErrorCodes.GENERIC_ERROR;
	}
}
