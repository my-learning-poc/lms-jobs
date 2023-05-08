package com.piramal.lms.jobs.repository;

import com.piramal.lms.jobs.model.LoanDataWrite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanInterestAccuralMongoRepository extends MongoRepository<LoanDataWrite,String> {
}
