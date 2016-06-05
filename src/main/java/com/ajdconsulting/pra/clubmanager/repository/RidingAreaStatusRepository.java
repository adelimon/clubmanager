package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.RidingAreaStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RidingAreaStatus entity.
 */
public interface RidingAreaStatusRepository extends JpaRepository<RidingAreaStatus,Long> {

}
