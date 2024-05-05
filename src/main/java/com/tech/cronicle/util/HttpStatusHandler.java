package com.tech.cronicle.util;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class HttpStatusHandler implements ResponseErrorHandler {
	@Override
	public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
		return false;
	}

	@Override
	public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
// no method body is required as handle error gets executed when error is occurred.
// In our case we are directly using false in has error so nothing will be executed.
// We will be hanlding everything in one single place (inside listeners).
	}
}
