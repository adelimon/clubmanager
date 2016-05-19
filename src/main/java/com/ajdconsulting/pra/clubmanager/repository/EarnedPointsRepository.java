package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.EarnedPoints;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the EarnedPoints entity.
 */
public interface EarnedPointsRepository extends JpaRepository<EarnedPoints,Long> {

    @Query("select e from EarnedPoints e where e.member.email = :userId")
    public Page<EarnedPoints> findForUser(Pageable pageable, @Param("userId") String userId);

}
