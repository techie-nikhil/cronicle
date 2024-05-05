package com.tech.cronicle.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity(name = "cron_audit")
public class CronAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "job_id")
	private long jobId;

	@Column(name = "job_name")
	private String jobName;

	@Column(name = "start_time")
	private Timestamp start_time;

	@Column(name = "end_time")
	private Timestamp end_time;

	@Column(name = "status")
	private String status;
	@Column(name = "retry_number")
	private int retryNumber;

	@Column(name = "last_failure_reason")
	private String lastFailureReason;

	@Column(name = "response_body")
	private String responseBody;

	@Override
	public String toString() {
		return "CronAudit{" + "id=" + id + ", jobId=" + jobId + ", jobName='" + jobName + '\'' + ", start_time=" + start_time + ", end_time="
				+ end_time + ", status='" + status + '\'' + ", retryNumber=" + retryNumber + ", lastFailureReason='" + lastFailureReason + '\'' + '}';
	}
}
