package com.piramal.lms.jobs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.piramal.lms.jobs.request.JobParamsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class JobService {
/*
	private final JobLauncher jobLauncher;

	private final Job interestAccrualJob;


	public JobService(JobLauncher jobLauncher, @Qualifier("interestAccrualJob") Job interestAccrualJob) {
		this.jobLauncher = jobLauncher;
		this.interestAccrualJob = interestAccrualJob;
	}

	@Async
	public void startJob(String jobName, List<JobParamsRequest> jobParamsRequestList) {
		Map<String, JobParameter<?>> params = new HashMap<>();
		params.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));

		jobParamsRequestList.forEach(jobPramReq -> {
			params.put(jobPramReq.getParamKey(),
					new JobParameter<>(jobPramReq.getParamValue(), String.class));
		});

		JobParameters jobParameters = new JobParameters(params);

		try {
			JobExecution jobExecution = null;
			if(jobName.equals("interestAccrualJob")) {
				jobExecution = jobLauncher.run(interestAccrualJob, jobParameters);
			} else {
				log.error("Job : " + jobName + " does not exists");
			}
			log.info("Job Execution ID = " + jobExecution.getId());
		}catch(Exception e) {
			log.error("Exception while starting job");
		}
	}

	*/

}
