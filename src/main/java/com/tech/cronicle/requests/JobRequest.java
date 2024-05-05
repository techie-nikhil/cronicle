package com.tech.cronicle.requests;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class JobRequest {
	private Long jobID;
	private String jobName;
	private String description;
	private String cronExpression;
	private String requestUrl;
	private String requestType;
	private String callbackUrl;
	private Integer retryCount;
	private Integer delayBetweenRetries;
	private JsonNode param;
	private JsonNode headers;
	private JsonNode body;


	@Override
	public String toString() {
		return "JobRequest{" + "jobID=" + jobID + ", jobName='" + jobName + '\'' + ", description='" + description + '\'' + ", cronExpression='"
				+ cronExpression + '\'' + ", requestUrl='" + requestUrl + '\'' + ", requestType='" + requestType + '\'' + ", callbackUrl='"
				+ callbackUrl + '\'' + ", retryCount=" + retryCount + ", delayBetweenRetries=" + delayBetweenRetries + ", param=" + param
				+ ", headers=" + headers + ", body=" + body + '}';
	}
}
