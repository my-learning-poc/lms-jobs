package com.piramal.lms.jobs.config;

import com.mongodb.MongoException;
import com.piramal.lms.jobs.config.processor.InterestAccrualProcessor;
import com.piramal.lms.jobs.config.reader.LoanItemReaderMongo;
import com.piramal.lms.jobs.config.writer.LoanInterestAccuralWriter;
import com.piramal.lms.jobs.listener.JobListener;
import com.piramal.lms.jobs.listener.SkipListener;
import com.piramal.lms.jobs.listener.SkipListenerImpl;
import com.piramal.lms.jobs.model.LoanDataRead;
import com.piramal.lms.jobs.model.LoanDataWrite;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
//@EnableJpaRepositories(basePackages = "com.piramal.lms.jobs.repository")
@EnableMongoRepositories(basePackages = "com.piramal.lms.jobs.repository")
public class MongoToMongoLoanJobConfig {

    private final LoanInterestAccuralWriter loanInterestAccuralWriter;
    private final LoanItemReaderMongo loanItemReaderMongo;

    private final InterestAccrualProcessor interestAccrualProcessor;
    private final DataSource dataSource;
    private final MongoTemplate mongoTemplate;
    private final EntityManagerFactory entityManagerFactory;
    private final MongoOperations mongoOperations;
    private final JobListener jobListener;
    private final SkipListener skipListener;

    private final SkipListenerImpl skipListenerImpl;

    public MongoToMongoLoanJobConfig(LoanInterestAccuralWriter loanInterestAccuralWriter, LoanItemReaderMongo loanItemReaderMongo, InterestAccrualProcessor interestAccrualProcessor, DataSource dataSource, MongoTemplate mongoTemplate, EntityManagerFactory entityManagerFactory, MongoOperations mongoOperations, JobListener jobListener, SkipListener skipListener, SkipListenerImpl skipListenerImpl) {
        this.loanInterestAccuralWriter = loanInterestAccuralWriter;
        this.loanItemReaderMongo = loanItemReaderMongo;
        this.interestAccrualProcessor = interestAccrualProcessor;
        this.dataSource = dataSource;
        this.mongoTemplate = mongoTemplate;
        this.entityManagerFactory = entityManagerFactory;
        this.mongoOperations = mongoOperations;
        this.jobListener = jobListener;
        this.skipListener = skipListener;
        this.skipListenerImpl = skipListenerImpl;
    }

    @Bean(name = "transactionManagerLoanInterest")
    @Primary
    public PlatformTransactionManager getTransactionManagerLoanInterest() {
        return new ResourcelessTransactionManager();
    }
    @Bean
    @Qualifier("jobRepositoryPostgresqlLoanInterest")
    public JobRepository getJobRepositoryPostgresqlLoanInterest() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(getTransactionManagerLoanInterest());
        factory.afterPropertiesSet();
        factory.setDatabaseType("POSTGRES");
        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        return factory.getObject();
    }


    @Bean(name = "jobLauncherLoanInterest")
    public JobLauncher getJobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(getJobRepositoryPostgresqlLoanInterest());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean (name = "jobBuilderLoanInterest")
    public JobBuilder jobBuilderLoanInterest() throws Exception {
        return new JobBuilder("loanInterestAccuralJob", getJobRepositoryPostgresqlLoanInterest());
    }

    @Bean
    public Step getloanInterestAccuralStep(@Qualifier("jobRepositoryPostgresqlLoanInterest") JobRepository jobRepository, @Qualifier("transactionManagerLoanInterest") PlatformTransactionManager platformTransactionManager) throws Exception {
        return new StepBuilder("loanInterestAccuralStep", getJobRepositoryPostgresqlLoanInterest())
                .<LoanDataRead, LoanDataWrite>chunk(400, platformTransactionManager)
                .reader(loanItemReaderMongo.getMongoLoanItemReader())
                .processor(interestAccrualProcessor)
                .writer(loanInterestAccuralWriter)
                .faultTolerant()
                .skip(MongoException.class)
                .skip(Throwable.class)
//                .skipLimit(Integer.MAX_VALUE)
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                //With retry avoid using skipPolicy (.skipPolicy(new AlwaysSkipItemSkipPolicy()))
//                .skipLimit(100)
//                .retryLimit(2)
//                .retry(Throwable.class)
//                .listener(skipListener)
                .listener(skipListenerImpl)
                .build();
    }



    @Bean(name = "loanInterestAccuralJob")
    public Job loanInterestAccuralJob(JobRepository jobRepository, @Qualifier("transactionManagerLoanInterest") PlatformTransactionManager platformTransactionManager) throws Exception {
        return new JobBuilder("loanInterestAccuralJob", jobRepository).preventRestart()
                .start(getloanInterestAccuralStep(jobRepository, platformTransactionManager))
                .repository(jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobListener)
                .build();
    }

     /*
     @Bean
    public ItemProcessor<Accounting, Accounting> accountingItemProcessor() {
        return accounting -> {
            // Add any necessary processing logic here
            return accounting;
        };
    }
    */

}
