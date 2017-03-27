package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.Integration;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Integration entity.
 */
public interface IntegrationRepository extends JpaRepository<Integration,Long> {

}
