package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.Integration;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Integration entity.
 */
public interface IntegrationRepository extends JpaRepository<Integration,Long> {

    @Query("select i from Integration i where platform = :platformKey")
    public Integration findPlatformById(@Param("platformKey") String platformKey);

    @Query("select i from Integration i where platform = 'clubmanager-default'")
    public Integration findClubManagerDefault();

}
