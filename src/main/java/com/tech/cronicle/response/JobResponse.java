package com.tech.cronicle.response;

import lombok.Data;

@Data
public class JobResponse {
	private String jobName;
	private Long jobID;
	private String description;
	private String cronExpression;
	private String requestUrl;
	private String requestType;
	private String callbackUrl;
	private int retryCount;
	private int delayBetweenRetries;
	private boolean isActive;
	private boolean isDeleted;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JobResponse [jobName=");
		builder.append(jobName);
		builder.append(", jobID=");
		builder.append(jobID);
		builder.append(", description=");
		builder.append(description);
		builder.append(", cronExpression=");
		builder.append(cronExpression);
		builder.append(", requestUrl=");
		builder.append(requestUrl);
		builder.append(", requestType=");
		builder.append(requestType);
		builder.append(", callbackUrl=");
		builder.append(callbackUrl);
		builder.append(", retryCount=");
		builder.append(retryCount);
		builder.append(", delayBetweenRetries=");
		builder.append(delayBetweenRetries);
		builder.append(", isActive=");
		builder.append(isActive);
		builder.append(", isDeleted=");
		builder.append(isDeleted);
		builder.append("]");
		return builder.toString();
	}

}
