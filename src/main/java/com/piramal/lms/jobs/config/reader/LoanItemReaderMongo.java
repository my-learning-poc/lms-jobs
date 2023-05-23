package com.piramal.lms.jobs.config.reader;

import com.piramal.lms.jobs.model.LoanDataRead;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
//        reader.setQuery(getQuery());
        reader.setQuery(new Query(Criteria.where("active").is(true)));
        reader.setTargetType(LoanDataRead.class);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);
        }});
        reader.setPageSize(100);
        return reader;
    }

    private Query getQuery() {
        Date newDateField = subtractDaysFromDate(new Date(), 2);

        Criteria dateRangeCriteria = Criteria.where("creationDate")
                .gte(subtractDaysFromDate(new Date(), 2))
                .lte(new Date());


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse("2022-01-01T18:30:00.000+00:00");
            endDate = dateFormat.parse("2022-12-31T18:30:00.000+00:00");
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
            // handle the exception
        }

        /*Criteria dateRangeCriteria = Criteria.where("creationDate")
                .gte(startDate)
                .lte(endDate);*/

        Criteria finalCriteria = new Criteria().andOperator(
                Criteria.where("active").is(true),
                dateRangeCriteria
        );

        return new Query(finalCriteria);
    }

    private Date subtractDaysFromDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        return calendar.getTime();
    }
}
