package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.SignupReport;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SignupReport entity.
 */
public interface SignupReportRepository extends JpaRepository<SignupReport,Long> {

}
