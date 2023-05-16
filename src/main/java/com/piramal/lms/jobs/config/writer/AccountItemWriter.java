package com.piramal.lms.jobs.config.writer;

import com.piramal.lms.jobs.model.AccountingPostgresql;
import com.piramal.lms.jobs.repository.AccountingPostgresqlRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountItemWriter implements ItemWriter<AccountingPostgresql> {
    @Autowired
    private AccountingPostgresqlRepository accountingPostgresqlRepository;


    @Override
    public void write(Chunk<? extends AccountingPostgresql> chunk) throws Exception {
            accountingPostgresqlRepository.saveAll(chunk);

    }
}
