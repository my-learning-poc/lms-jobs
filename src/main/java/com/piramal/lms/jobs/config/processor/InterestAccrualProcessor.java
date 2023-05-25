package com.piramal.lms.jobs.config.processor;

import com.piramal.lms.jobs.model.InterestAccrual;
import com.piramal.lms.jobs.model.Loan;
import com.piramal.lms.jobs.model.LoanDataRead;
import com.piramal.lms.jobs.model.LoanDataWrite;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class InterestAccrualProcessor implements ItemProcessor<LoanDataRead, LoanDataWrite> {
    @Override
    public LoanDataWrite process(LoanDataRead item) throws Exception {
        LoanDataWrite loanDataWrite = new LoanDataWrite();
        loanDataWrite.setLoanNumber(item.getLoanNumber());
        loanDataWrite.setAnnualInterestRate(item.getAnnualInterestRate());
        loanDataWrite.setActive(item.isActive());
        loanDataWrite.setSanctionAmount(item.getSanctionAmount());
        loanDataWrite.setSimpleInterestAmount(item.getSanctionAmount()*item.getTimePeriodInYear()
                                                * item.getAnnualInterestRate());
        double interestAmount = item.getSanctionAmount()* item.getTimePeriodInYear() * item.getAnnualInterestRate();
        loanDataWrite.setTotalAmountWithInterest(item.getSanctionAmount()+interestAmount);
        loanDataWrite.setTimePeriodInYear(item.getTimePeriodInYear());

        return loanDataWrite;
    }


}
