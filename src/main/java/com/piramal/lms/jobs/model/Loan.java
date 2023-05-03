package com.piramal.lms.jobs.model;

import lombok.Data;

@Data
public class Loan {

    private String loanNumber;
    private String productId;
    private boolean active;
    private double sanctionAmount;
    private double dueAmount;
}
