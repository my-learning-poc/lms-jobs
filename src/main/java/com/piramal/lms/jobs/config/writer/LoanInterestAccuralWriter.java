package com.piramal.lms.jobs.config.writer;

import com.piramal.lms.jobs.model.LoanDataWrite;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class LoanInterestAccuralWriter implements ItemWriter<LoanDataWrite> {
    private final MongoTemplate mongoTemplate;

    public LoanInterestAccuralWriter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void write(Chunk<? extends LoanDataWrite> chunk) throws Exception {
        for (LoanDataWrite item : chunk) {
            mongoTemplate.save(item);
        }

    }
}
