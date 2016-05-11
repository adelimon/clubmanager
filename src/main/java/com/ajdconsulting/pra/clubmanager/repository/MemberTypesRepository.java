package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.MemberTypes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MemberTypes entity.
 */
public interface MemberTypesRepository extends JpaRepository<MemberTypes,Long> {

}
