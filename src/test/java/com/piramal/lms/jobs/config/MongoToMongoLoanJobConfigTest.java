package com.piramal.lms.jobs.config;

import com.piramal.lms.jobs.config.processor.InterestAccrualProcessor;
import com.piramal.lms.jobs.config.reader.LoanItemReaderMongo;
import com.piramal.lms.jobs.config.writer.LoanInterestAccuralWriter;
import com.piramal.lms.jobs.listener.JobListener;
import com.piramal.lms.jobs.listener.SkipListener;
import com.piramal.lms.jobs.listener.SkipListenerImpl;
import com.piramal.lms.jobs.model.LoanDataRead;
import com.piramal.lms.jobs.model.LoanDataWrite;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class MongoToMongoLoanJobConfigTest {

    @InjectMocks
    MongoToMongoLoanJobConfig mongoToMongoLoanJobConfig;
    @Mock
    private LoanInterestAccuralWriter loanInterestAccuralWriter;
    @Mock
    private InterestAccrualProcessor interestAccrualProcessor;
    @Mock
    private DataSource dataSource;
    @Mock
    private MongoTemplate mongoTemplate;
    @Spy
    private LoanItemReaderMongo loanItemReaderMongo = new LoanItemReaderMongo(this.mongoTemplate);
    @Mock
    private EntityManagerFactory entityManagerFactory;
    @Mock
    private MongoOperations mongoOperations;
    @Mock
    private JobListener jobListener;
    @Mock
    private SkipListener skipListener;
    @Mock
    private SkipListenerImpl skipListenerImpl;
    private JobLauncherTestUtils jobLauncherTestUtils;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mongoToMongoLoanJobConfig = new MongoToMongoLoanJobConfig(loanInterestAccuralWriter, loanItemReaderMongo, interestAccrualProcessor,
                dataSource, mongoTemplate, entityManagerFactory, mongoOperations, jobListener, skipListener, skipListenerImpl);
        PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);
        jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJobLauncher(mock(JobLauncher.class));
        jobLauncherTestUtils.setJobRepository(mock(JobRepository.class));
        jobLauncherTestUtils.setJob(mongoToMongoLoanJobConfig.loanInterestAccuralJob(jobLauncherTestUtils.getJobRepository(), platformTransactionManager));
    }

    @Test
    public void testLoanInterestAccuralStep() throws Exception {
        JobRepository jobRepository = mock(JobRepository.class);
        PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);
        Step step = mongoToMongoLoanJobConfig.getloanInterestAccuralStep(jobRepository, platformTransactionManager);
        assertNotNull(step);
    }


    @Test
    public void testLoanInterestAccuralJob() throws Exception {
        JobRepository jobRepository = mock(JobRepository.class);
        PlatformTransactionManager platformTransactionManager = mock(PlatformTransactionManager.class);
        Job job = mongoToMongoLoanJobConfig.loanInterestAccuralJob(jobRepository, platformTransactionManager);
        assertNotNull(job);
    }

    //    @Test
    public void testRunJob() throws Exception {

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
//        String json = "[{\"paramKey\":\"sample-key-1\",\"paramValue\":\"sample-value-2\"},{\"paramKey\":\"inputFile\",\"paramValue\":\"C:/Users/racloop/Documents/MyProject/lms-jobs/data/input/loan.csv\"}]";
//        jobParametersBuilder.addString("jsonParams", json);
        jobParametersBuilder.addString("paramKey", "sample-value");
        jobParametersBuilder.addString("inputFile", "C:/Users/racloop/Documents/MyProject/lms-jobs/data/input/loan.csv");
        JobParameters jobParameters = jobParametersBuilder.toJobParameters();

        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters);
        when(loanItemReaderMongo.getMongoLoanItemReader()).thenReturn(getMongoLoanItemReader());
//        when(loanInterestAccuralWriter).thenReturn(mongoItemWriter());
//        when(interestAccrualProcessor.getEntityManagerFactory()).thenReturn(entityManagerFactory);
//        when(interestAccrualProcessor.getDataSource()).thenReturn(dataSource);

        StepExecution resultStepExecution = jobLauncherTestUtils.launchStep("loanInterestAccuralStep", jobParameters).getStepExecutions().iterator().next();
        assertEquals(stepExecution.getStepName(), resultStepExecution.getStepName());
    }

    public synchronized MongoItemReader<LoanDataRead> getMongoLoanItemReader() {
        MongoItemReader<LoanDataRead> reader = new MongoItemReader<>();
        reader.setTemplate(this.mongoTemplate);
        reader.setCollection("loan_ds");
        reader.setQuery(new Query(Criteria.where("active").is(true)));
        reader.setTargetType(LoanDataRead.class);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);
        }});
        reader.setPageSize(100);
        return reader;
    }


    public MongoItemWriter<LoanDataWrite> mongoItemWriter() {
        return new MongoItemWriterBuilder<LoanDataWrite>()
                .collection("loan_postprocessing")
                .template(mongoTemplate)
                .build();
    }


   /*

    @AfterEach
    void tearDown() {
    }

    @Test
    void getTransactionManagerLoanInterest() {
    }

    @Test
    void getJobRepositoryPostgresqlLoanInterest() {
    }

    @Test
    void getJobLauncher() {
    }

    @Test
    void jobBuilderLoanInterest() {
    }

    @Test
    void getloanInterestAccuralStep() {
    }

    @Test
    void loanInterestAccuralJob() {
    }
    */
}