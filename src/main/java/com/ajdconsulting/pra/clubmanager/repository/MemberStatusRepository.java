package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.MemberStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MemberStatus entity.
 */
public interface MemberStatusRepository extends JpaRepository<MemberStatus,Long> {

}
