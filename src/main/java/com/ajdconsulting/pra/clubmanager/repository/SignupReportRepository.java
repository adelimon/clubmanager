package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.SignupReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the SignupReport entity.
 */
public interface SignupReportRepository extends JpaRepository<SignupReport,Long> {

    @Query("select report from SignupReport report order by report.title")
    public Page<SignupReport> findAllOrdered(Pageable pageable);
}
