package com.piramal.lms.jobs.config;

import com.piramal.lms.jobs.listener.JobListener;
import com.piramal.lms.jobs.model.AccountingMongo;
import com.piramal.lms.jobs.model.AccountingPostgresql;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(basePackages = "com.piramal.lms.jobs.repository")
public class MongoToPostgresJobConfig {

    private final AccountItemWriter accountItemWriter;
    private final DataSource dataSource;
    private final MongoTemplate mongoTemplate;
    private final EntityManagerFactory entityManagerFactory;
    private final MongoOperations mongoOperations;
    private final JobListener jobListener;

    public MongoToPostgresJobConfig(AccountItemWriter accountItemWriter, DataSource dataSource, MongoTemplate mongoTemplate, EntityManagerFactory entityManagerFactory, MongoOperations mongoOperations, JobListener jobListener) {
        this.accountItemWriter = accountItemWriter;
        this.dataSource = dataSource;
        this.mongoTemplate = mongoTemplate;
        this.entityManagerFactory = entityManagerFactory;
        this.mongoOperations = mongoOperations;
        this.jobListener = jobListener;
    }

    @Bean
    @Qualifier("jobRepositoryPostgresql")
    public JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(getTransactionManagerMongo());
        factory.afterPropertiesSet();
        factory.setDatabaseType("POSTGRES");
        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        return factory.getObject();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManagerMongo() {
        return new ResourcelessTransactionManager();
    }
    @Bean(name = "jobLauncher")
    public JobLauncher getJobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(getJobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public JobBuilder jobBuilder() throws Exception {
        return new JobBuilder("mongoToPostgresJob", getJobRepository());
    }

    @Bean
    @StepScope
    public MongoItemReader<AccountingMongo> getMongoItemReader() {
        MongoItemReader<AccountingMongo> reader = new MongoItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setCollection("accounting");
        reader.setQuery("{}");
        reader.setTargetType(AccountingMongo.class);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);
        }});
        return reader;
    }

    @Bean
    public Step getMongoToPostgresStep(JobRepository jobRepository, @Qualifier("transactionManager") PlatformTransactionManager platformTransactionManager) throws Exception {
        return new StepBuilder("mongoToPostgresStep", getJobRepository())
                .<AccountingMongo, AccountingPostgresql>chunk(40, platformTransactionManager)
                .reader(getMongoItemReader())
                .writer(accountItemWriter)
                .build();
    }

    @Bean(name = "mongoToPostgresJob")
    public Job mongoToPostgresJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new JobBuilder("mongoToPostgresJob", jobRepository).preventRestart()
                .start(getMongoToPostgresStep(jobRepository, transactionManager))
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
