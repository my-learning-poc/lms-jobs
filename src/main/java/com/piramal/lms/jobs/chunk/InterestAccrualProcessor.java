package com.piramal.lms.jobs.chunk;

import com.piramal.lms.jobs.model.InterestAccrual;
import com.piramal.lms.jobs.model.Loan;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class InterestAccrualProcessor implements ItemProcessor<Loan, InterestAccrual> {
    @Override
    public InterestAccrual process(Loan item) throws Exception {
        InterestAccrual interestAccrual = new InterestAccrual();
        interestAccrual.setLoanNumber(item.getLoanNumber());
        interestAccrual.setInterest((new Random().nextDouble()) * 500);
        return interestAccrual;
    }
}
