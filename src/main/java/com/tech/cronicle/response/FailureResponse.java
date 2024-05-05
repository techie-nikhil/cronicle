package com.tech.cronicle.response;

import java.util.Map;

import lombok.Data;

@Data
public class FailureResponse {
	private int responseCode;
	private String responseBody;
	private Map<String, String> headers;
	private String failureReason;
	private long jobId;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FailureResponse [responseCode=");
		builder.append(responseCode);
		builder.append(", responseBody=");
		builder.append(responseBody);
		builder.append(", headers=");
		builder.append(headers);
		builder.append(", failureReason=");
		builder.append(failureReason);
		builder.append(", jobId=");
		builder.append(jobId);
		builder.append("]");
		return builder.toString();
	}

}
