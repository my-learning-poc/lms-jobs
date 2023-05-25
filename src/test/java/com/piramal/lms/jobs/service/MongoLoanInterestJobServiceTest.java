package com.piramal.lms.jobs.service;

import com.piramal.lms.jobs.request.JobParamsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MongoLoanInterestJobServiceTest {
    @Mock
    @Qualifier("jobLauncherLoanInterest")
    private JobLauncher jobLauncherLoanInterest;

    @Mock
    @Qualifier("loanInterestAccuralJob")
    private Job loanInterestAccuralJob;

    private MongoLoanInterestJobService jobService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobService = new MongoLoanInterestJobService(jobLauncherLoanInterest, loanInterestAccuralJob);
    }

    @Test
    void testStartJob() throws Exception {
        String jobName = "loanInterestAccuralJob";
        List<JobParamsRequest> jobParamsRequestList = new ArrayList<>();
        // Add some test data to jobParamsRequestList

        JobExecution jobExecution = mock(JobExecution.class);
        when(jobLauncherLoanInterest.run(eq(loanInterestAccuralJob), any(JobParameters.class))).thenReturn(jobExecution);

        jobService.startJob(jobName, jobParamsRequestList);

        verify(jobLauncherLoanInterest, times(1)).run(eq(loanInterestAccuralJob), any(JobParameters.class));

    }

}