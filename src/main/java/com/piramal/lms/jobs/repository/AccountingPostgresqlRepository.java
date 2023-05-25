package com.piramal.lms.jobs.repository;

import com.piramal.lms.jobs.model.AccountingPostgresql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountingPostgresqlRepository extends JpaRepository<AccountingPostgresql,Number> {
}
