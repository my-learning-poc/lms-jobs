package com.piramal.lms.jobs.controller;

import com.piramal.lms.jobs.request.JobParamsRequest;
import com.piramal.lms.jobs.service.MongoJobService;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postgres/job")
public class MongoToPostgresJobController {
    private final MongoJobService mongoJobService;
    private final JobOperator jobOperator;

    public MongoToPostgresJobController(MongoJobService mongoJobService, JobOperator jobOperator) {
        this.mongoJobService = mongoJobService;
        this.jobOperator = jobOperator;
    }

    @GetMapping("/start/mongo/{jobName}")
    public String startMongoJob(@PathVariable String jobName,
                                @RequestBody List<JobParamsRequest> jobParamsRequestList) {
        mongoJobService.startJob(jobName, jobParamsRequestList);
        return "Job Started..."+jobName ;
    }

    @GetMapping("/stop/mongo/{jobExecutionId}")
    public String stopMongoJob(@PathVariable long jobExecutionId) {
        try {
            jobOperator.stop(jobExecutionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Job Stopped...";
    }
}
