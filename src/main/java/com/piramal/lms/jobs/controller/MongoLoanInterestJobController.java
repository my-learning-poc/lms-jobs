package com.piramal.lms.jobs.controller;

import com.piramal.lms.jobs.request.JobParamsRequest;
import com.piramal.lms.jobs.service.MongoJobService;
import com.piramal.lms.jobs.service.MongoLoanInterestJobService;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/job")
public class MongoLoanInterestJobController {
    private final MongoLoanInterestJobService mongoLoanInterestJobService;
    private final JobOperator jobOperator;

    public MongoLoanInterestJobController( MongoLoanInterestJobService mongoLoanInterestJobService, JobOperator jobOperator) {
        this.mongoLoanInterestJobService = mongoLoanInterestJobService;
        this.jobOperator = jobOperator;
    }

    @GetMapping("/start/loan/interest/{jobName}")
    public String startMongoJob(@PathVariable String jobName,
                                @RequestBody List<JobParamsRequest> jobParamsRequestList) {
        mongoLoanInterestJobService.startJob(jobName, jobParamsRequestList);
        return "Job Started..."+jobName ;
    }

    @GetMapping("/stop/loan/interest/{jobExecutionId}")
    public String stopMongoJob(@PathVariable long jobExecutionId) {
        try {
            jobOperator.stop(jobExecutionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Job Stopped...";
    }
}
