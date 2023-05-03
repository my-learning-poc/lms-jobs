package com.piramal.lms.jobs.chunk;

import java.util.List;

import com.piramal.lms.jobs.model.InterestAccrual;
import com.piramal.lms.jobs.model.Loan;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;


@Component
public class LogWriter implements ItemWriter<InterestAccrual> {

    @Override
    public void write(Chunk<? extends InterestAccrual> items) throws Exception {
        System.out.println("Inside Item Writer");
        items.getItems().stream().forEach(System.out::println);
    }

}
