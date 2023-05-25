package com.piramal.lms.jobs.config.processor;

import com.mongodb.MongoException;
import com.piramal.lms.jobs.model.LoanDataRead;
import com.piramal.lms.jobs.model.LoanDataWrite;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterestAccrualProcessorTest {
    @InjectMocks
    private InterestAccrualProcessor interestAccrualProcessor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("InterestAccrualProcessor->testProcess()")
    @Test
    public void testProcess() throws Exception {
        // Set up the input data
        LoanDataRead loanDataRead = new LoanDataRead();
        loanDataRead.setLoanNumber("123");
        loanDataRead.setAnnualInterestRate(7);
        loanDataRead.setActive(true);
        loanDataRead.setSanctionAmount(1000.0);
        loanDataRead.setTimePeriodInYear(2);

        // Call the method under test
        LoanDataWrite loanDataWrite = interestAccrualProcessor.process(loanDataRead);

        // Verify the result
        assertEquals(loanDataWrite.getLoanNumber(), loanDataRead.getLoanNumber());
        assertEquals(loanDataWrite.getAnnualInterestRate(), loanDataRead.getAnnualInterestRate());
        assertEquals(loanDataWrite.isActive(), loanDataRead.isActive());
        assertEquals(loanDataWrite.getSanctionAmount(), loanDataRead.getSanctionAmount());
        assertEquals(loanDataWrite.getSimpleInterestAmount(), 14000.0);
        assertEquals(loanDataWrite.getTotalAmountWithInterest(), 15000.0);
        assertEquals(loanDataWrite.getTimePeriodInYear(), loanDataRead.getTimePeriodInYear());
    }

    @DisplayName("InterestAccrualProcessor->testProcessWithInvalidTimePeriod()")
    @Test
    public void testProcessWithInvalidTimePeriod() throws Exception {
        // Set up the input data with invalid time period
        LoanDataRead loanDataRead = new LoanDataRead();

        loanDataRead.setLoanNumber("123");
        loanDataRead.setAnnualInterestRate(5);
        loanDataRead.setActive(true);
        loanDataRead.setSanctionAmount(1000.0);
        loanDataRead.setTimePeriodInYear(0);

        // Call the method under test and verify that it throws an exception
        MongoException exception = org.junit.jupiter.api.Assertions.assertThrows(
                MongoException.class,
                () -> interestAccrualProcessor.process(loanDataRead)
        );
        assertEquals(exception.getMessage(), "Loan Time Period is Invalid: 0");
    }


    @AfterEach
    void tearDown() {
    }
}
