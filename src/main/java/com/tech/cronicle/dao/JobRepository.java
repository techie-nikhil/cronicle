package com.tech.cronicle.dao;

import org.springframework.data.repository.CrudRepository;

import com.tech.cronicle.model.JobModel;

public interface JobRepository extends CrudRepository<JobModel, Long> {
	JobModel findByJobName(String JobName);

	Integer deleteByJobName(String JobName);
}
