package com.piramal.lms.jobs.config;

import com.piramal.lms.jobs.model.Loan;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ChunkConfig {

    @StepScope
    @Bean
    public FlatFileItemReader<Loan> flatFileItemReader(
            @Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource) {
        FlatFileItemReader<Loan> flatFileItemReader = new FlatFileItemReader<Loan>();
        flatFileItemReader.setResource(fileSystemResource);
        DefaultLineMapper<Loan> defaultLineMapper =  new DefaultLineMapper<Loan>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("Loan Number", "Product Id", "Active", "Sanction Amount", "Due Amount");
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        BeanWrapperFieldSetMapper<Loan> fieldSetMapper =new BeanWrapperFieldSetMapper<Loan>();
        fieldSetMapper.setTargetType(Loan.class);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        flatFileItemReader.setLineMapper(defaultLineMapper);
        flatFileItemReader.setLinesToSkip(1);
        return flatFileItemReader;
    }
}
