package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.PaidSignup;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PaidSignup entity.
 */
public interface PaidSignupRepository extends JpaRepository<PaidSignup,Long> {

}
