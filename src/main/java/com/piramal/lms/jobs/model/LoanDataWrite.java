package com.piramal.lms.jobs.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "loan_postprocessing")

public class LoanDataWrite {
    @Id
    private String loanNumber;
    @NotNull
    private float annualInterestRate;
    @NotNull
    private boolean active;
    @NotNull
    private double sanctionAmount;
    @NotNull
    private double simpleInterestAmount;
    @NotNull
    private double totalAmountWithInterest;
    @NotNull
    private int timePeriodInYear;
    @NotNull
    private Date creationDate;
}
