package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.MemberNote;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MemberNote entity.
 */
public interface MemberNoteRepository extends JpaRepository<MemberNote,Long> {

}
