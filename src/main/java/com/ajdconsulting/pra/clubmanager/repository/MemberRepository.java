package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Member entity.
 */
public interface MemberRepository extends JpaRepository<Member,Long> {
    @Query("select m from Member m where viewOnline = true and endDate is null order by lastName")
    public Page<Member> findMembersOnline(Pageable pageable);

    @Query("select m from Member m where endDate is null order by lastName")
    public Page<Member> findAllMembersOrderByLastName(Pageable pageable);

    public Page<Member> findByEmail(String email, Pageable pageable);

    public Member findByEmail(String email);

    @Query("select m from Member m where endDate is null and m.status.type != 'Paid Labor' and m.status.type != 'Application Pending' order by lastName, firstName")
    public List<Member> findBillableMembers();
}
