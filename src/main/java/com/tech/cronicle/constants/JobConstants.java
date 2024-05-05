package com.tech.cronicle.constants;

public class JobConstants {
	public static final String JOB_ID = "jobID";
	public static final String JOB_EXISTS = "Job already exists!";
	public static final String JOB_NOT_AVAILABLE = "Job is unavailable/deleted";
	public static final String INVALID_CRON_EXPRESSION = "The cron expression is invalid!";
	public static final String JOB_ACTIVATED = "The job is activated!";
	public static final String JOB_NOT_ACTIVATED = "Unable to activate job!";
	public static final String JOB_DEACTIVATED = "The job is de-activated!";
	public static final String JOB_NOT_DEACTIVATED = "Unable to de-activate job!";
	public static final String SERVER_SIDE_ISSUE = "Server Side Issue";
	public static final String CLIENT_SIDE_ISSUE = "Client Side Issue";
	public static final String UNREACHABLE_SERVER_ISSUE = "Server is Unreachable";
	public static final String JOB_DELETED = "The job is deleted!";
	public static final String JOB_NOT_DELETED = "Unable to deleted job!";
	public static final long DB_CONNECTION_TIMEOUT_MILLISECONDS = 30 * 1000;
	public static final long DB_CONNECTION_MAX_LIFETIME_MILLISECONDS = 60 * 1000;

	public enum JobStatus {
		PENDING, SUCCESS, FAILURE
	}
}
