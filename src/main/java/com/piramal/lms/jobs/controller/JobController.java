package com.piramal.lms.jobs.controller;

import java.util.List;

import com.piramal.lms.jobs.request.JobParamsRequest;
import com.piramal.lms.jobs.service.JobService;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//@RestController
//@RequestMapping("/api/job")
public class JobController {
	
	private final JobService jobService;
	private final JobOperator jobOperator;

	public JobController(JobService jobService, JobOperator jobOperator) {
		this.jobService = jobService;
		this.jobOperator = jobOperator;
	}

	@GetMapping("/start/{jobName}")
	public String startJob(@PathVariable String jobName, 
			@RequestBody List<JobParamsRequest> jobParamsRequestList) {
		jobService.startJob(jobName, jobParamsRequestList);
		return "Job Started...";
	}
	
	@GetMapping("/stop/{jobExecutionId}")
	public String stopJob(@PathVariable long jobExecutionId) {
		try {
			jobOperator.stop(jobExecutionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Job Stopped...";
	}
}
