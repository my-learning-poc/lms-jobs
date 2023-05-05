package com.piramal.lms.jobs.config;

import com.piramal.lms.jobs.model.AccountingMongo;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;

//@Component
public class  AccountingItemReader extends MongoItemReader<AccountingMongo> {
    private final MongoTemplate mongoTemplate;
    private final MongoOperations mongoOperations;

    public AccountingItemReader(MongoTemplate mongoTemplate, MongoOperations mongoOperations) {
        this.mongoTemplate = mongoTemplate;
        this.mongoOperations = mongoOperations;
    }
    private final String collectionName = "accounting";

    @Bean
    public MongoItemReader<AccountingMongo> mongoItemReader() {
        MongoItemReader<AccountingMongo> reader = new MongoItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setCollection("accounting");
        reader.setQuery("{}");
        reader.setTargetType(AccountingMongo.class);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);}});
        return reader;
    }
}
