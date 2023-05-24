package com.piramal.lms.jobs.controller;

import com.piramal.lms.jobs.request.JobParamsRequest;
import com.piramal.lms.jobs.service.MongoLoanInterestJobService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@TestPropertySource(locations = "/src/test/resources/application-test.properties")
@TestPropertySource(locations = "classpath:application-test.properties")
class MongoLoanInterestJobControllerTest {

    @Mock
    private MongoLoanInterestJobService mongoLoanInterestJobService;
    @Mock
    private JobOperator jobOperator;
    private MongoLoanInterestJobController controller;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new MongoLoanInterestJobController(mongoLoanInterestJobService, jobOperator);
    }

    @Test
    void testStartMongoJob() {
        String jobName = "jobName";
        List<JobParamsRequest> jobParamsRequestList = new ArrayList<>();
        // Mock service method
        doNothing().when(mongoLoanInterestJobService).startJob(jobName, jobParamsRequestList);

        String result = controller.startMongoJob(jobName, jobParamsRequestList);
        verify(mongoLoanInterestJobService, times(1)).startJob(jobName, jobParamsRequestList);
        assertEquals("Job Started..." + jobName, result);
    }

    @Test
    void testStopMongoJob() throws NoSuchJobExecutionException, JobExecutionNotRunningException {
        long jobExecutionId = 123L;

        // Invoke the controller method
        String result = controller.stopMongoJob(jobExecutionId);

        // Verify the JobOperator method was called
        verify(jobOperator, times(1)).stop(jobExecutionId);
        assertEquals("Job Stopped...", result);
    }


}