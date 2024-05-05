package com.tech.cronicle.util;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tech.cronicle.constants.JobConstants;
import com.tech.cronicle.dao.CronAuditRepository;
import com.tech.cronicle.model.CronAudit;
import com.tech.cronicle.model.JobModel;
import com.tech.cronicle.response.FailureResponse;

@Service
public class RetryListenerImpl implements RetryListener {
	private final static LoggerWrapper LOG = LoggerWrapper.getLogger(RestTemplateUtil.class);
	@Autowired
	CronAuditRepository objCronAuditRepository;

	@Override
	public <T, E extends Throwable> boolean open(RetryContext retryContext, RetryCallback<T, E> retryCallback) {
		return true;
	}

	@Override
	public <T, E extends Throwable> void close(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
		JobModel model = (JobModel) retryContext.getAttribute("model");
		try {
			if (retryContext.getLastThrowable() != null) {
				String reasonOfFailure = retryContext.getLastThrowable().getMessage();

				LOG.info("Inside Close >>>>>>  retryCount: " + retryContext.getRetryCount() + "  >>>> for jobModelId  " + model.getId()
						+ ", failureReason: " + reasonOfFailure);
				if (reasonOfFailure == null || !reasonOfFailure.equals(JobConstants.CLIENT_SIDE_ISSUE)) {
					ResponseEntity<String> entity = (ResponseEntity<String>) retryContext.getAttribute("entity");
					FailureResponse failreponse = new FailureResponse();
					if (entity == null) {
						failreponse.setResponseCode(0);
						failreponse.setResponseBody(reasonOfFailure);
					} else {
						failreponse.setResponseCode(entity.getStatusCodeValue());
						failreponse.setHeaders(entity.getHeaders().toSingleValueMap());
						failreponse.setResponseBody(entity.getBody());
					}
					failreponse.setFailureReason(reasonOfFailure);
					failreponse.setJobId(model.getId());
					HttpEntity<Object> httpEntity = new HttpEntity<>(failreponse);
					saveExecutionAudit((CronAudit) retryContext.getAttribute("audit"), model, JobConstants.JobStatus.FAILURE.name(),
							failreponse.getFailureReason(), retryContext.getRetryCount(), reasonOfFailure, failreponse.getResponseBody());
					ResponseEntity<String> callbackResponse = new RestTemplate().exchange(model.getCallbackUrl(), HttpMethod.POST, httpEntity,
							String.class);
					if (callbackResponse == null) {
						LOG.error("jobModelId " + model.getId() + ", Call Back Response: " + callbackResponse);
					} else {
						LOG.error("jobModelId " + model.getId() + ", Call Back Response is status: " + callbackResponse.getStatusCode() + ", body: "
								+ callbackResponse.getBody());
					}
				}
			} else
				saveExecutionAudit((CronAudit) retryContext.getAttribute("audit"), model, JobConstants.JobStatus.SUCCESS.name(), "",
						retryContext.getRetryCount(), "", "");
		} catch (Exception ex) {
			LOG.error("Callback Failure", ex);
		}
	}

	@Override
	public <T, E extends Throwable> void onError(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
		JobModel model = (JobModel) retryContext.getAttribute("model");
		String reasonOfFailure = retryContext.getLastThrowable().getMessage();
		ResponseEntity<String> entity = (ResponseEntity<String>) retryContext.getAttribute("entity");
		String responseBody = null;
		if (entity != null) {
			responseBody = entity.getBody();
		}
		LOG.info("Inside OnError >>>>> retryCount: " + retryContext.getRetryCount() + "  >>>> for jobModelId  " + model.getId() + ", failureReason: "
				+ reasonOfFailure + ", responseBody:" + responseBody);
		try {
			if (reasonOfFailure.equals(JobConstants.CLIENT_SIDE_ISSUE)) {

				FailureResponse failreponse = new FailureResponse();
				if (entity != null) {
					failreponse.setResponseCode(entity.getStatusCodeValue());
					failreponse.setHeaders(entity.getHeaders().toSingleValueMap());
					failreponse.setResponseBody(responseBody);
				}
				failreponse.setFailureReason(reasonOfFailure);
				failreponse.setJobId(model.getId());
				HttpEntity<Object> httpEntity = new HttpEntity<>(failreponse);
				saveExecutionAudit((CronAudit) retryContext.getAttribute("audit"), model, JobConstants.JobStatus.FAILURE.name(), "Client Side Issue",
						retryContext.getRetryCount(), reasonOfFailure, entity.getBody());
				ResponseEntity<String> callbackResponse = new RestTemplate().exchange(model.getCallbackUrl(), HttpMethod.POST, httpEntity,
						String.class);
				if (callbackResponse == null) {
					LOG.error("jobModelId " + model.getId() + ", Call Back Response: " + callbackResponse);
				} else {
					LOG.error("jobModelId " + model.getId() + ", Call Back Response is status: " + callbackResponse.getStatusCode() + ", body: "
							+ callbackResponse.getBody());
				}
			}
		} catch (Exception ec) {
			LOG.error("Callback Failure for jobModelId: " + model.getId() + " is: " + ec.getMessage());
		}

	}

	private void saveExecutionAudit(CronAudit audit, JobModel model, String status, String reason, int retryNumber, String reasonOfFailure,
			String response) {
		audit.setEnd_time(new Timestamp(new Date().getTime()));
		audit.setRetryNumber(retryNumber + 1);
		audit.setStatus(status);
		audit.setResponseBody(response);
		audit.setLastFailureReason(reasonOfFailure);
		objCronAuditRepository.save(audit);
	}
}
