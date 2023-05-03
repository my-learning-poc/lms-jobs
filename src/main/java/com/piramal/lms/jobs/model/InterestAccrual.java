package com.piramal.lms.jobs.model;

import lombok.Data;

@Data
public class InterestAccrual {
    private String loanNumber;
    private double interest;
}
