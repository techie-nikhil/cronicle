package com.tech.cronicle.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class FinalResponse {
	private int code;
	private String message;
	@JsonInclude(Include.NON_NULL)
	private Object data;

	public FinalResponse() {

	}

	public FinalResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public FinalResponse(int code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[code=");
		builder.append(code);
		builder.append(", message=");
		builder.append(message);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

}
