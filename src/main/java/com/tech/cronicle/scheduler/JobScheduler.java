package com.tech.cronicle.scheduler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.cronicle.constants.ErrorCodes;
import com.tech.cronicle.constants.JobConstants;
import com.tech.cronicle.dao.CronAuditRepository;
import com.tech.cronicle.dao.JobRepository;
import com.tech.cronicle.exception.FinalException;
import com.tech.cronicle.job.APITriggerJob;
import com.tech.cronicle.model.JobModel;
import com.tech.cronicle.util.JobUtil;
import com.tech.cronicle.util.LoggerWrapper;

@Service
public class JobScheduler {
	private final static LoggerWrapper LOG = LoggerWrapper.getLogger(JobScheduler.class);
	private final Scheduler scheduler;
	private final CronAuditRepository objCronAuditRepository;
	private final JobRepository objJobRepository;

	@Autowired
	public JobScheduler(Scheduler scheduler, CronAuditRepository objCronAuditRepository, JobRepository objJobRepository) {
		this.scheduler = scheduler;
		this.objCronAuditRepository = objCronAuditRepository;
		this.objJobRepository = objJobRepository;
	}

	public void insertJob(final JobModel jobModel) throws FinalException {
		try {
			final JobDetail jobDtl = this.scheduler.getJobDetail(new JobKey(String.valueOf(jobModel.getId())));
			if (jobDtl != null)
				throw new FinalException(ErrorCodes.CLIENT_ERROR, JobConstants.JOB_EXISTS);
			final JobDetail jobDetail = JobUtil.buildJobDetail(APITriggerJob.class, jobModel.getId());
			final Trigger trigger = JobUtil.buildTrigger(APITriggerJob.class, jobModel, jobModel.getId());
			this.scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException se) {
			LOG.error(se.getMessage(), se);
		}
	}

	public JobModel findJobById(Long jobID) throws FinalException {
		return objJobRepository.findById(jobID).orElseThrow(() -> new FinalException(ErrorCodes.JOB_NOT_FOUND, ErrorCodes.JOB_NOT_FOUND_MSG + jobID));
	}

	public JobModel updateJob(final JobModel jobModel) throws FinalException {
		Long jobID = jobModel.getId();
		try {
			final JobModel oldJobModel = findJobById(jobID);
			if (oldJobModel.isDeleted()) {
				throw new FinalException(ErrorCodes.DELETED_JOB, JobConstants.JOB_NOT_AVAILABLE);
			}
			// Updating the existing job with new trigger if job is active
			if (!oldJobModel.getCronExpression().equals(jobModel.getCronExpression())) {
				if (oldJobModel.isActive()) {
					final Trigger newTrigger = JobUtil.buildTrigger(APITriggerJob.class, jobModel, jobID);
					this.scheduler.rescheduleJob(new TriggerKey(String.valueOf(jobID)), newTrigger);
				}
			}
			return jobModel;
		} catch (SchedulerException se) {
			LOG.error("Scheduling error in updateJob for jobId " + jobID + " is " + se.getMessage());
			return null;
		}
	}

	public JobModel deleteJob(final long jobID) throws FinalException {
		try {
			final JobModel jobModel = findJobById(jobID);
			this.scheduler.deleteJob(new JobKey(String.valueOf(jobID)));
			return jobModel;
		} catch (SchedulerException se) {
			LOG.error("Scheduling error in deleteJob for jobId " + jobID + " is " + se.getMessage());
			return null;
		}
	}

	public boolean activateJob(final JobModel jobModel) throws FinalException, SchedulerException {
		Long jobID = jobModel.getId();
		final Trigger newTrigger = JobUtil.buildTrigger(APITriggerJob.class, jobModel, jobID);

		// before activating job, make sure that latest cron expression is picked
		this.scheduler.rescheduleJob(new TriggerKey(String.valueOf(jobID)), newTrigger);
		return true;
	}

	public boolean deactivateJob(final JobModel jobModel) throws FinalException, SchedulerException {
		Long jobID = jobModel.getId();
		this.scheduler.pauseTrigger(new TriggerKey(String.valueOf(jobID)));
		return true;
	}

	@PostConstruct
	public void init() {
		try {
			objCronAuditRepository.updateAllPendingJobs();
			this.scheduler.start();
		} catch (SchedulerException se) {
			LOG.error("Exception during init scheduler " + se.getMessage(), se);
		}
	}

	@PreDestroy
	public void destroy() {
		try {
			this.scheduler.shutdown();
		} catch (SchedulerException se) {
			LOG.error("Exception during destroy scheduler " + se.getMessage(), se);
		}
	}
}
