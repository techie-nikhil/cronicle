package com.tech.cronicle.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity(name = "cron_jobs")
public class JobModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "job_name")
	private String jobName;

	@Column(name = "description")
	private String description;

	@Column(name = "cron_expression")
	private String cronExpression;

	@Column(name = "request_url")
	private String requestUrl;

	@Column(name = "request_type")
	private String requestType;

	@Column(name = "callback_url")
	private String callbackUrl;

	@Column(name = "retry_count")
	private int retryCount;

	@Column(name = "delay_between_retries")
	private int delayBetweenRetries;

	@Column(name = "is_active")
	private boolean isActive;

	@Column(name = "is_deleted")
	private boolean isDeleted;

	@Column(columnDefinition = "JSON")
	private String headers;

	@Column(columnDefinition = "JSON")
	private String body;

	// null condition for header has been handled in prepareHttpEntity method of
	// RestTemplateUtil class
	public JsonNode getHeaders() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(headers);
	}

	public JsonNode getBody() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(body);
	}

	@Override
	public String toString() {
		return "JobModel{" + "jobID=" + id + ", jobName='" + jobName + '\'' + ", description='" + description + '\'' + ", cronExpression='"
				+ cronExpression + '\'' + ", requestUrl='" + requestUrl + '\'' + ", requestType='" + requestType + '\'' + ", callbackUrl='"
				+ callbackUrl + '\'' + ", retryCount=" + retryCount + ", delayBetweenRetries=" + delayBetweenRetries + ", isActive=" + isActive + '\''
				+ ", headers='" + headers + '\'' + ", body='" + body + '\'' + '}';
	}
}
