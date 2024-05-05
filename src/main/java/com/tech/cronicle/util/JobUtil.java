package com.tech.cronicle.util;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.tech.cronicle.constants.JobConstants;
import com.tech.cronicle.exception.FinalException;
import com.tech.cronicle.model.JobModel;
import com.tech.cronicle.requests.JobRequest;
import com.tech.cronicle.response.JobResponse;

public final class JobUtil {
	public static boolean isStringNotNullOrEmpty(String value) {
		if (value == null || value.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	public static JobDetail buildJobDetail(final Class<? extends Job> jobClass, final long jobID) throws FinalException {
		final JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(JobConstants.JOB_ID, jobID);
		return JobBuilder.newJob(jobClass).withIdentity(String.valueOf(jobID)).setJobData(jobDataMap).build();
	}

	public static Trigger buildTrigger(final Class<?> jobClass, final JobModel jobModel, final long jobID) throws FinalException {
		CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule(jobModel.getCronExpression());
		return TriggerBuilder.newTrigger().withIdentity(String.valueOf(jobID)).withSchedule(builder).startNow().build();
	}

	public static JobModel getInsertJobModel(JobRequest jobRequest) {
		JobModel jobModel = new JobModel();
		jobModel.setJobName(jobRequest.getJobName());
		jobModel.setCronExpression(jobRequest.getCronExpression());
		jobModel.setDescription(jobRequest.getDescription());
		jobModel.setRequestUrl(jobRequest.getRequestUrl());
		jobModel.setRequestType(jobRequest.getRequestType());
		jobModel.setCallbackUrl(jobRequest.getCallbackUrl());
		jobModel.setRetryCount(jobRequest.getRetryCount());
		jobModel.setDelayBetweenRetries(jobRequest.getDelayBetweenRetries());
		jobModel.setActive(true);
		jobModel.setHeaders(jobRequest.getHeaders().toString());
		jobModel.setBody(jobRequest.getBody().toString());
		return jobModel;
	}

	public static JobModel getUpdateJobModel(JobModel dbJobModel, JobRequest jobRequest) {
		if (isStringNotNullOrEmpty(jobRequest.getJobName())) {
			dbJobModel.setJobName(jobRequest.getJobName());
		}
		if (isStringNotNullOrEmpty(jobRequest.getCronExpression())) {
			dbJobModel.setCronExpression(jobRequest.getCronExpression());
		}
		if (isStringNotNullOrEmpty(jobRequest.getDescription())) {
			dbJobModel.setDescription(jobRequest.getDescription());
		}
		if (isStringNotNullOrEmpty(jobRequest.getRequestUrl())) {
			dbJobModel.setRequestUrl(jobRequest.getRequestUrl());
		}
		if (isStringNotNullOrEmpty(jobRequest.getRequestType())) {
			dbJobModel.setRequestType(jobRequest.getRequestType());
		}
		if (isStringNotNullOrEmpty(jobRequest.getCallbackUrl())) {
			dbJobModel.setCallbackUrl(jobRequest.getCallbackUrl());
		}
		if (jobRequest.getRetryCount() != null) {
			dbJobModel.setRetryCount(jobRequest.getRetryCount());
		}
		if (jobRequest.getDelayBetweenRetries() != null) {
			dbJobModel.setDelayBetweenRetries(jobRequest.getDelayBetweenRetries());
		}
		if (jobRequest.getHeaders() != null) {
			dbJobModel.setHeaders(jobRequest.getHeaders().toString());
		}
		if (jobRequest.getBody() != null) {
			dbJobModel.setBody(jobRequest.getBody().toString());
		}
		return dbJobModel;
	}

	public static JobResponse getJobResponse(JobModel jobModel) {
		JobResponse jobResponse = new JobResponse();
		jobResponse.setJobID(jobModel.getId());
		jobResponse.setJobName(jobModel.getJobName());
		jobResponse.setDescription(jobModel.getDescription());
		jobResponse.setRequestUrl(jobModel.getRequestUrl());
		jobResponse.setRequestType(jobModel.getRequestType());
		jobResponse.setCallbackUrl(jobModel.getCallbackUrl());
		jobResponse.setRetryCount(jobModel.getRetryCount());
		jobResponse.setDelayBetweenRetries(jobModel.getDelayBetweenRetries());
		jobResponse.setActive(jobModel.isActive());
		jobResponse.setDeleted(jobModel.isDeleted());
		jobResponse.setCronExpression(jobModel.getCronExpression());
		return jobResponse;
	}
}
