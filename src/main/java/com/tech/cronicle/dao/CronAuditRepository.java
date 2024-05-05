package com.tech.cronicle.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.tech.cronicle.model.CronAudit;

public interface CronAuditRepository extends CrudRepository<CronAudit, Long> {
	@Transactional
	@Modifying
	@Query("UPDATE cron_audit audit SET audit.status = 'FAILURE' , audit.end_time = now() , audit.lastFailureReason = 'System Unexpected Down' WHERE (status = 'PENDING')")
	public void updateAllPendingJobs();
}
