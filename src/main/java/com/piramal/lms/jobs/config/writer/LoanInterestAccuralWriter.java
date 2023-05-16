package com.piramal.lms.jobs.config.writer;

import com.piramal.lms.jobs.model.AccountingPostgresql;
import com.piramal.lms.jobs.model.LoanDataWrite;
import com.piramal.lms.jobs.repository.AccountingPostgresqlRepository;
import com.piramal.lms.jobs.repository.LoanInterestAccuralMongoRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanInterestAccuralWriter implements ItemWriter<LoanDataWrite> {
    @Autowired
    private LoanInterestAccuralMongoRepository loanInterestAccuralMongoRepository;

    @Override
    public void write(Chunk<? extends LoanDataWrite> chunk) throws Exception {
            loanInterestAccuralMongoRepository.saveAll(chunk);

    }
}
