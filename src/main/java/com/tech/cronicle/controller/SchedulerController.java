package com.tech.cronicle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.tech.cronicle.exception.FinalException;
import com.tech.cronicle.requests.JobRequest;
import com.tech.cronicle.service.SchedulerService;
import com.tech.cronicle.util.ResponseGeneratorUtil;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

	@Autowired
	private SchedulerService schedulerService;

	@PostMapping(path = "/insert-job")
	public ResponseEntity<Object> insertJob(@RequestBody JobRequest jobRequest) throws FinalException {
		return new ResponseEntity<>(ResponseGeneratorUtil.okResponse(schedulerService.insertJob(jobRequest)), HttpStatus.OK);
	}

	@PutMapping(path = "/update-job")
	public ResponseEntity<Object> updateJob(@RequestBody JobRequest jobRequest) throws FinalException {
		return new ResponseEntity<>(ResponseGeneratorUtil.okResponse(schedulerService.updateJob(jobRequest)), HttpStatus.OK);
	}

	@GetMapping(path = "/get-job/{jobID}")
	public ResponseEntity<Object> getJobDetailsByID(@PathVariable("jobID") long jobID) throws FinalException {
		return new ResponseEntity<>(ResponseGeneratorUtil.okResponse(schedulerService.getJobDetailsByID(jobID)), HttpStatus.OK);
	}

	@PutMapping(path = "/activate-job/{jobID}")
	public ResponseEntity<Object> activateJob(@PathVariable("jobID") long jobID) throws FinalException {
		return new ResponseEntity<>(ResponseGeneratorUtil.okResponse(schedulerService.activateJob(jobID)), HttpStatus.OK);
	}

	@PutMapping(path = "/deactivate-job/{jobID}")
	public ResponseEntity<Object> deactivateJob(@PathVariable("jobID") long jobID) throws FinalException {
		return new ResponseEntity<>(ResponseGeneratorUtil.okResponse(schedulerService.deactivateJob(jobID)), HttpStatus.OK);
	}

	@PutMapping(path = "/delete-job/{jobID}")
	public ResponseEntity<Object> deleteJob(@PathVariable("jobID") long jobID) throws FinalException {
		return new ResponseEntity<>(ResponseGeneratorUtil.okResponse(schedulerService.deleteJob(jobID)), HttpStatus.OK);
	}

	// @PostMapping(path = "/postsample.json")
	public ResponseEntity<Object> postsample(@RequestBody JsonNode jobRequest) throws FinalException {
		System.out.println("####################################");
		System.out.println("####################################");
		System.out.println(jobRequest);
		System.out.println("####################################");
		System.out.println("####################################");
		return new ResponseEntity<>(ResponseGeneratorUtil.okResponse("Success"), HttpStatus.OK);
	}
}
