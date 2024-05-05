package com.tech.cronicle.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tech.cronicle.constants.ErrorCodes;
import com.tech.cronicle.constants.JobConstants;
import com.tech.cronicle.dao.JobRepository;
import com.tech.cronicle.exception.FinalException;
import com.tech.cronicle.model.JobModel;
import com.tech.cronicle.requests.JobRequest;
import com.tech.cronicle.response.JobResponse;
import com.tech.cronicle.scheduler.JobScheduler;
import com.tech.cronicle.util.JobUtil;
import com.tech.cronicle.util.LoggerWrapper;
import com.tech.cronicle.util.RequestValidatorUtil;

@Service
public class SchedulerService {
	private final static LoggerWrapper LOG = LoggerWrapper.getLogger(SchedulerService.class);
	private final JobRepository jobRepository;
	private final JobScheduler jobScheduler;
	private final RequestValidatorUtil requestValidatorUtil;

	@Autowired
	public SchedulerService(JobScheduler jobScheduler, JobRepository jobRepository, RequestValidatorUtil requestValidatorUtil) {
		this.jobScheduler = jobScheduler;
		this.jobRepository = jobRepository;
		this.requestValidatorUtil = requestValidatorUtil;
	}

	public JobResponse insertJob(JobRequest jobRequest) throws FinalException {
		try {
			LOG.request("insertJob" + " " + jobRequest);
			this.requestValidatorUtil.validateInsertJobRequest(jobRequest);
			JobModel jobModel = JobUtil.getInsertJobModel(jobRequest);
			jobModel = this.jobRepository.save(jobModel);
			jobRequest.setJobID(jobModel.getId());
			this.jobScheduler.insertJob(jobModel);
			JobResponse JobResponse = JobUtil.getJobResponse(jobModel);
			LOG.response("insertJob " + JobResponse);
			return JobResponse;
		} catch (FinalException fe) {
			throw fe;
		} catch (Exception de) {
			LOG.error("insertJob", de);
			throw new FinalException(ErrorCodes.CLIENT_ERROR, de.getMessage());
		}
	}

	public JobResponse updateJob(JobRequest jobRequest) throws FinalException {
		Long jobId = jobRequest.getJobID();
		try {
			LOG.request("updateJob for jobId " + jobId + " " + jobRequest);
			this.requestValidatorUtil.validateUpdateJobRequest(jobRequest);
			JobModel dbJobModel = jobScheduler.findJobById(jobId);
			JobModel jobModel = JobUtil.getUpdateJobModel(dbJobModel, jobRequest);
			this.jobScheduler.updateJob(jobModel);
			jobModel = this.jobRepository.save(jobModel);
			JobResponse JobResponse = JobUtil.getJobResponse(jobModel);
			LOG.response("updateJob " + JobResponse);
			return JobResponse;
		} catch (FinalException fe) {
			throw fe;
		} catch (Exception de) {
			LOG.error("updateJob for jobId " + jobId, de);
			throw new FinalException(ErrorCodes.CLIENT_ERROR, de.getMessage());
		}
	}

	public JobModel getJobDetailsByID(long jobID) throws FinalException {
		return this.jobScheduler.findJobById(jobID);
	}

	@Transactional
	public String deleteJob(long jobID) throws FinalException {
		try {
			JobModel jobModel = this.jobScheduler.deleteJob(jobID);
			if (jobModel.isDeleted())
				throw new FinalException(ErrorCodes.DELETED_JOB, JobConstants.JOB_NOT_AVAILABLE);
			jobModel.setDeleted(true);
			jobModel.setActive(false);
			this.jobRepository.save(jobModel);
			LOG.response("deleted job with jobId " + jobID + " " + jobModel.isDeleted());

			return jobModel.isDeleted() ? JobConstants.JOB_DELETED : JobConstants.JOB_NOT_DELETED;
		} catch (FinalException fe) {
			throw fe;
		} catch (Exception de) {
			throw new FinalException(ErrorCodes.CLIENT_ERROR, de.getMessage());
		}
	}

	public String activateJob(long jobID) throws FinalException {
		try {
			Optional<JobModel> jobModelOptional = this.jobRepository.findById(jobID);
			if (!jobModelOptional.isPresent() || jobModelOptional.get().isDeleted())
				throw new FinalException(ErrorCodes.DELETED_JOB, JobConstants.JOB_NOT_AVAILABLE);

			JobModel jobModel = jobModelOptional.get();
			boolean isActivated = this.jobScheduler.activateJob(jobModel);
			jobModel.setActive(isActivated);
			this.jobRepository.save(jobModel);

			LOG.response("activated job with jobId " + jobID + " " + isActivated);

			return isActivated ? JobConstants.JOB_ACTIVATED : JobConstants.JOB_NOT_ACTIVATED;
		} catch (FinalException fe) {
			throw fe;
		} catch (Exception de) {
			throw new FinalException(ErrorCodes.CLIENT_ERROR, de.getMessage());
		}
	}

	public String deactivateJob(long jobID) throws FinalException {
		try {
			Optional<JobModel> jobModelOptional = this.jobRepository.findById(jobID);
			if (!jobModelOptional.isPresent() || jobModelOptional.get().isDeleted())
				throw new FinalException(ErrorCodes.DELETED_JOB, JobConstants.JOB_NOT_AVAILABLE);

			JobModel jobModel = jobModelOptional.get();
			boolean isDeactivated = this.jobScheduler.deactivateJob(jobModel);
			jobModel.setActive(!isDeactivated);
			this.jobRepository.save(jobModel);

			LOG.response("deactivated job with jobId " + jobID + " " + isDeactivated);

			return isDeactivated ? JobConstants.JOB_DEACTIVATED : JobConstants.JOB_NOT_DEACTIVATED;
		} catch (FinalException fe) {
			throw fe;
		} catch (Exception de) {
			throw new FinalException(ErrorCodes.CLIENT_ERROR, de.getMessage());
		}
	}
}
