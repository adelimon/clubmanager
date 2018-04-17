package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.BoardMember;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the BoardMember entity.
 */
public interface BoardMemberRepository extends JpaRepository<BoardMember,Long> {

    @Query("select b from BoardMember b where b.member.id = :id and b.year = :year")
    public List<BoardMember> findByMemberId(@Param("id") long memberId, @Param("year") int year);
}
