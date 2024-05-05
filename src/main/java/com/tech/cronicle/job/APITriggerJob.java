package com.tech.cronicle.job;

import java.sql.Timestamp;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.tech.cronicle.constants.JobConstants;
import com.tech.cronicle.dao.CronAuditRepository;
import com.tech.cronicle.dao.JobRepository;
import com.tech.cronicle.exception.RequestAssertionException;
import com.tech.cronicle.model.CronAudit;
import com.tech.cronicle.model.JobModel;
import com.tech.cronicle.util.LoggerWrapper;
import com.tech.cronicle.util.RestTemplateUtil;
import com.tech.cronicle.util.RetryListenerImpl;

@Component
public class APITriggerJob implements Job {

	private final static LoggerWrapper LOG = LoggerWrapper.getLogger(APITriggerJob.class);

	@Autowired
	private RestTemplateUtil restTemplateUtil;
	@Autowired
	private RetryListenerImpl objRetryListenerImpl;
	@Autowired
	private CronAuditRepository objCronAuditRepository;
	@Autowired
	private JobRepository objJobRepository;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
		String jobID = String.valueOf(jobDataMap.get(JobConstants.JOB_ID));
		JobModel jobModel = objJobRepository.findById(Long.parseLong(jobID)).get();
		LOG.info("The Job is >>> " + jobModel);
		CronAudit objCronAudit = new CronAudit();
		objCronAudit.setStart_time(new Timestamp(new Date().getTime()));
		objCronAudit.setJobId(jobModel.getId());
		objCronAudit.setJobName(jobModel.getJobName());
		objCronAudit.setStatus(JobConstants.JobStatus.PENDING.name());
		CronAudit resultAudit = objCronAuditRepository.save(objCronAudit);
		String reqType = jobModel.getRequestType().toUpperCase();
		RetryTemplate template = RetryTemplate.builder().maxAttempts(jobModel.getRetryCount()).fixedBackoff(jobModel.getDelayBetweenRetries())
				.retryOn(RequestAssertionException.class).build();
		template.registerListener(objRetryListenerImpl);
		try {
			template.execute(new RetryCallback<String, Exception>() {
				@Override
				public String doWithRetry(RetryContext context) throws Exception {
					context.setAttribute("model", jobModel);
					context.setAttribute("audit", resultAudit);
					switch (reqType) {
					case "GET":
						restTemplateUtil.performRequest(jobModel, HttpMethod.GET, context);
						break;
					case "POST":
						restTemplateUtil.performRequest(jobModel, HttpMethod.POST, context);
						break;
					case "PUT":
						restTemplateUtil.performRequest(jobModel, HttpMethod.PUT, context);
						break;
					// case "DELETE":
					// restTemplateUtil.performRequest(jobModel, HttpMethod.DELETE, context);
					// break;
					default:
						throw new JobExecutionException("Invalid HTTP method is provided. Valid HTTP methods are POST/GET/DELETE/PUT.");
					}
					return "Execution Completed";
				}
			});
		} catch (Exception e) {
//			LOG.error("Exception found while calling APIs", e);
		}
	}
}
