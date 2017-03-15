package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.MemberWork;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MemberWork entity.
 */
public interface MemberWorkRepository extends JpaRepository<MemberWork,Long> {

}
