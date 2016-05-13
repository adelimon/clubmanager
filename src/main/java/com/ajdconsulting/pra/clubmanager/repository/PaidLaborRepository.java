package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.PaidLabor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PaidLabor entity.
 */
public interface PaidLaborRepository extends JpaRepository<PaidLabor,Long> {

}
