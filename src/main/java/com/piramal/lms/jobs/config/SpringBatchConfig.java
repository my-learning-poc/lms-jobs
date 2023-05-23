package com.piramal.lms.jobs.config;

import com.piramal.lms.jobs.chunk.LogWriter;
import com.piramal.lms.jobs.config.processor.InterestAccrualProcessor;
import com.piramal.lms.jobs.listener.JobListener;
import com.piramal.lms.jobs.listener.StepListener;
import com.piramal.lms.jobs.model.InterestAccrual;
import com.piramal.lms.jobs.model.Loan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
@Slf4j
public class SpringBatchConfig {

    private final DataSource dataSource;
    private final JobListener jobListener;
    private final StepListener stepListener;
    private final FlatFileItemReader<Loan> flatFileItemReader;
    private final InterestAccrualProcessor interestAccrualProcessor;
    private final LogWriter logWriter;

    public SpringBatchConfig(DataSource dataSource, JobListener jobListener, StepListener stepListener, FlatFileItemReader<Loan> flatFileItemReader, InterestAccrualProcessor interestAccrualProcessor, LogWriter logWriter) {
        this.dataSource = dataSource;
        this.jobListener = jobListener;
        this.stepListener = stepListener;
        this.flatFileItemReader = flatFileItemReader;
        this.interestAccrualProcessor = interestAccrualProcessor;
        this.logWriter = logWriter;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean(name = "jobRepository")
    public JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(getTransactionManager());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "jobLauncher")
    public JobLauncher getJobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(getJobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    /*@Bean(name = "interestAccrualJob")
    public Job interestAccrualJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("interestAccrualJob", jobRepository).preventRestart()
                .start(getInterestAccrualStep(jobRepository, platformTransactionManager))
//                .next(getRps(jobRepository, platformTransactionManager))
                .listener(jobListener)
                .build();
    }*/

  /*  @Bean(name = "interestAccrualStep")
    public Step getInterestAccrualStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("interestAccrualStep", jobRepository)
                .<Loan, InterestAccrual>chunk(4, platformTransactionManager)
                .reader(flatFileItemReader)
                .processor(interestAccrualProcessor)
                .writer(logWriter)
                .listener(stepListener)
                .build();
    }*/

//    @Bean(name = "getLoans")
//    public Step getLoans(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("getLoans", jobRepository)
//                .tasklet(getLoansFromDB(), platformTransactionManager)
//                .listener(stepListener)
//                .build();
//    }

//    private Tasklet getLoansFromDB() {
//        return new Tasklet() {
//
//            @Override
//            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                log.info("getLoansFromDB tasklet step");
//                log.info("SEC = " + chunkContext.getStepContext().getStepExecutionContext());
//                return RepeatStatus.FINISHED;
//            }
//        };
//    }
//
//    @Bean(name = "getRps")
//    public Step getRps(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("getRps", jobRepository)
//                .tasklet(getRpsFromDB(), platformTransactionManager)
//                .listener(stepListener)
//                .build();
//    }
//
//    private Tasklet getRpsFromDB() {
//        return new Tasklet() {
//
//            @Override
//            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                log.info("getRpsFromDB tasklet step");
//                log.info("SEC = " + chunkContext.getStepContext().getStepExecutionContext());
//                return RepeatStatus.FINISHED;
//            }
//        };
//    }

//    private Step secondStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("secondStep", jobRepository)
//                .tasklet(secondTask(), platformTransactionManager)
//                .listener(stepListener)
//                .build();
//    }
//
//    private Tasklet secondTask() {
//        return new Tasklet() {
//            @Override
//            public RepeatStatus execute(StepContribution contribution,
//                                        ChunkContext chunkContext) throws Exception {
//                log.info("This is second tasklet step");
//                return
//                        RepeatStatus.FINISHED;
//            }
//        };
//    }

//
//	@Bean
//	public Job secondJob() {
//		return jobBuilderFactory.get("Second Job")
//				.incrementer(new RunIdIncrementer())
//				.start(firstChunkStep())
//				.build();
//	}
//
//	private Step firstChunkStep() {
//		return stepBuilderFactory.get("First Chunk Step")
//				.<Integer, Long>chunk(4)
//				.reader(firstItemReader)
//				.processor(firstItemProcessor)
//				.writer(firstItemWriter)
//				.build();
//	}
}
