package com.piramal.lms.jobs.config.reader;

import com.piramal.lms.jobs.model.LoanDataRead;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class LoanItemReaderMongo {

    private final MongoTemplate mongoTemplate;

    public LoanItemReaderMongo(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public synchronized MongoItemReader<LoanDataRead> getMongoLoanItemReader() {
        MongoItemReader<LoanDataRead> reader = new MongoItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setCollection("loan_ds");
//        reader.setQuery("{}");
        reader.setQuery(new Query(Criteria.where("active").is(true)));
        reader.setTargetType(LoanDataRead.class);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);
        }});
        reader.setPageSize(100);
        return reader;
    }
}
