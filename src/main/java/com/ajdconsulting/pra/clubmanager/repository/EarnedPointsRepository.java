package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.EarnedPoints;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EarnedPoints entity.
 */
public interface EarnedPointsRepository extends JpaRepository<EarnedPoints,Long> {

}
