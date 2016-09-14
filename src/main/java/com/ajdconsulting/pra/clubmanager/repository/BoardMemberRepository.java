package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.BoardMember;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BoardMember entity.
 */
public interface BoardMemberRepository extends JpaRepository<BoardMember,Long> {

}
