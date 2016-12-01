package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.MemberMessage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MemberMessage entity.
 */
public interface MemberMessageRepository extends JpaRepository<MemberMessage,Long> {

}
