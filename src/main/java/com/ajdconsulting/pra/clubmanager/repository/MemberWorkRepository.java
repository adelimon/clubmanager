package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.MemberWork;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the MemberWork entity.
 */
public interface MemberWorkRepository extends JpaRepository<MemberWork,Long> {

    @Query("select m from MemberWork where m.member.email = :email order by m.start desc")
    public List<MemberWork> findAllForMember(@Param("email") String email);
}
