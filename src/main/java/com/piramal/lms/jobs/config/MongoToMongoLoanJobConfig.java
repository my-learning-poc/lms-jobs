package com.piramal.lms.jobs.config;

import com.piramal.lms.jobs.chunk.InterestAccrualProcessor;
import com.piramal.lms.jobs.listener.JobListener;
import com.piramal.lms.jobs.model.AccountingMongo;
import com.piramal.lms.jobs.model.AccountingPostgresql;
import com.piramal.lms.jobs.model.LoanDataRead;
import com.piramal.lms.jobs.model.LoanDataWrite;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
//@EnableJpaRepositories(basePackages = "com.piramal.lms.jobs.repository")
@EnableMongoRepositories(basePackages = "com.piramal.lms.jobs.repository")
public class MongoToMongoLoanJobConfig {

    private final LoanInterestAccuralWriter loanInterestAccuralWriter;

    private final InterestAccrualProcessor interestAccrualProcessor;
    private final DataSource dataSource;
    private final MongoTemplate mongoTemplate;
    private final EntityManagerFactory entityManagerFactory;
    private final MongoOperations mongoOperations;
    private final JobListener jobListener;

    public MongoToMongoLoanJobConfig(LoanInterestAccuralWriter loanInterestAccuralWriter, InterestAccrualProcessor interestAccrualProcessor, DataSource dataSource, MongoTemplate mongoTemplate, EntityManagerFactory entityManagerFactory, MongoOperations mongoOperations, JobListener jobListener) {
        this.loanInterestAccuralWriter = loanInterestAccuralWriter;
        this.interestAccrualProcessor = interestAccrualProcessor;
        this.dataSource = dataSource;
        this.mongoTemplate = mongoTemplate;
        this.entityManagerFactory = entityManagerFactory;
        this.mongoOperations = mongoOperations;
        this.jobListener = jobListener;
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

    @Bean(name = "getMongoLoanItemReader")
    @StepScope
    public MongoItemReader<LoanDataRead> getMongoLoanItemReader() {
        MongoItemReader<LoanDataRead> reader = new MongoItemReader<LoanDataRead>();
        reader.setTemplate(mongoTemplate);
        reader.setCollection("loan_ds");
        reader.setQuery(new Query(Criteria.where("active").is(true)));
        reader.setTargetType(LoanDataRead.class);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);
        }});
        return reader;
    }

    @Bean
    public Step getloanInterestAccuralStep(@Qualifier("jobRepositoryPostgresqlLoanInterest") JobRepository jobRepository, @Qualifier("transactionManagerLoanInterest") PlatformTransactionManager platformTransactionManager) throws Exception {
        return new StepBuilder("loanInterestAccuralStep", getJobRepositoryPostgresqlLoanInterest())
                .<LoanDataRead, LoanDataWrite>chunk(400, platformTransactionManager)
                .reader(getMongoLoanItemReader())
                .processor(interestAccrualProcessor)
                .writer(loanInterestAccuralWriter)
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
